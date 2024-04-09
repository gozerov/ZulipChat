package ru.gozerov.core.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.withStyledAttributes
import androidx.core.view.updateLayoutParams
import ru.gozerov.tfs_spring.R
import ru.gozerov.tfs_spring.screens.channels.chat.adapters.message.Reaction
import ru.gozerov.tfs_spring.utils.dp

class MessageCardLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0,
    defTheme: Int = 0
) : ViewGroup(context, attributeSet, defStyle, defTheme) {

    private val imageView: ImageView

    private val usernameTextView: TextView

    private val dateTextView: TextView

    private val messageTextView: TextView

    private val emojiLayout: FlexBoxLayout

    var username = ""
        set(value) {
            if (field != value) {
                field = value
                usernameTextView.text = field
                requestLayout()
                invalidate()
            }
        }

    var message = ""
        set(value) {
            if (field != value) {
                field = value
                messageTextView.text = field
                requestLayout()
                invalidate()
            }
        }

    var date = ""
        set(value) {
            if (field != value) {
                field = value
                dateTextView.text = field
                requestLayout()
                invalidate()
            }
        }

    private val imageSize: Int
    private val defaultMargin: Int
    private val backgroundPaint = Paint().apply {
        style = Paint.Style.FILL
        this.setColor(context.getColor(R.color.grey_dialog))
    }

    var imageDrawable: Drawable? = null
        set(value) {
            field = value
            imageView.setImageDrawable(field)
            requestLayout()
            invalidate()
        }

    init {
        setWillNotDraw(false)
        inflate(context, R.layout.layout_message_card, this)

        imageView = findViewById(R.id.imgAvatar)
        usernameTextView = findViewById(R.id.txtUsername)
        dateTextView = findViewById(R.id.txtDate)
        messageTextView = findViewById(R.id.txtMessage)
        emojiLayout = findViewById(R.id.emojiLayout)

        var margin = 0
        var imageSize = 0

        context.withStyledAttributes(attributeSet, R.styleable.MessageCardLayout) {
            val backgroundDrawable =
                this.getDrawable(R.styleable.MessageCardLayout_android_background)
            this@MessageCardLayout.background = backgroundDrawable

            val username = this.getString(R.styleable.MessageCardLayout_username)
            usernameTextView.text = username

            val message = this.getString(R.styleable.MessageCardLayout_message)
            messageTextView.text = message

            val drawable = this.getDrawable(R.styleable.MessageCardLayout_android_src)
            imageView.setImageDrawable(drawable)

            margin = this.getDimension(R.styleable.MessageCardLayout_defaultMargin, 8f.dp(context))
                .toInt()
            imageSize =
                this.getDimension(R.styleable.MessageCardLayout_imageSize, 40f.dp(context)).toInt()
        }
        defaultMargin = margin
        this.imageSize = imageSize
        initImage()
        initUsername(attributeSet)
        initDate()
        initMessage()
        initEmojiLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            measureChild(view, widthMeasureSpec, heightMeasureSpec)
        }
        val parentWidth = (MeasureSpec.getSize(widthMeasureSpec))
        val messageDesiredWidth = MeasureSpec.makeMeasureSpec(
            (parentWidth * 0.7f).toInt() - dateTextView.measuredWidth - imageView.measuredWidth - defaultMargin * 2,
            MeasureSpec.AT_MOST
        )
        messageTextView.measure(messageDesiredWidth, messageTextView.measuredHeight)
        emojiLayout.measure(messageDesiredWidth, emojiLayout.measuredHeight)
        usernameTextView.measure(messageDesiredWidth, usernameTextView.measuredHeight)

        val wantedHeight = maxOf(
            paddingTop + paddingBottom + imageView.measuredHeight + defaultMargin,
            paddingTop + paddingBottom + defaultMargin + usernameTextView.measuredHeight + messageTextView.measuredHeight + emojiLayout.measuredHeight
        ) + 3 * defaultMargin

        val actualHeight = resolveSize(
            wantedHeight,
            heightMeasureSpec
        )
        setMeasuredDimension(parentWidth, actualHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val imageStart = paddingStart + defaultMargin
        val imageEnd = imageStart + imageView.measuredWidth
        val imageTop = paddingTop + defaultMargin
        val imageBottom = imageTop + imageView.measuredHeight

        val nameStart = imageEnd + 2 * defaultMargin
        val nameBottom = imageTop + defaultMargin + usernameTextView.measuredHeight

        val mainContainerWidth = maxOf(
            maxOf(messageTextView.measuredWidth, usernameTextView.measuredWidth),
            emojiLayout.measuredWidth
        )

        val dateStart = nameStart + mainContainerWidth + defaultMargin
        val dateEnd = dateStart + dateTextView.measuredWidth

        val messageBottom = nameBottom + messageTextView.measuredHeight

        imageView.layout(imageStart, imageTop, imageEnd, imageBottom)
        //dateTextView.layout(dateStart, imageTop, dateEnd, imageTop + dateTextView.measuredHeight)

        usernameTextView.layout(
            nameStart,
            imageTop + defaultMargin,
            nameStart + usernameTextView.measuredWidth,
            nameBottom
        )

        messageTextView.layout(
            nameStart,
            nameBottom,
            nameStart + messageTextView.measuredWidth,
            messageBottom
        )

        emojiLayout.layout(
            imageEnd + defaultMargin,
            messageBottom + 2 * defaultMargin,
            nameStart + emojiLayout.measuredWidth,
            messageBottom + 2 * defaultMargin + emojiLayout.measuredHeight
        )
    }

    override fun onDraw(canvas: Canvas) {
        val start = (imageView.left + imageView.measuredWidth + defaultMargin).toFloat()

        val end = if (emojiLayout.lineCount > 1) width * 0.7f else minOf(
            (width * 0.7f).toInt(),
            maxOf(maxOf(emojiLayout.right, messageTextView.right), usernameTextView.right)
        ).toFloat() + defaultMargin
        val bottom =
            paddingTop + defaultMargin * 3f + usernameTextView.measuredHeight + messageTextView.measuredHeight

        canvas.drawRoundRect(
            start,
            imageView.top.toFloat(),
            end,
            bottom,
            16f.dp(context),
            16f.dp(context),
            backgroundPaint
        )
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    private fun initImage() {
        imageView.clipToOutline = true
        imageView.layoutParams = MarginLayoutParams(imageSize, imageSize)
        imageView.updateLayoutParams<MarginLayoutParams> {
            this.marginStart = defaultMargin
            this.topMargin = defaultMargin
        }
    }

    private fun initUsername(attributeSet: AttributeSet?) {
        usernameTextView.layoutParams = MarginLayoutParams(context, attributeSet)
        usernameTextView.updateLayoutParams<MarginLayoutParams> {
            this.marginStart = defaultMargin
        }
        usernameTextView.setTextColor(context.getColor(R.color.teal_400))
    }

    private fun initDate() {
        dateTextView.setTextColor(Color.BLACK)
    }

    private fun initMessage() {
        messageTextView.setTextColor(context.getColor(R.color.white))
    }

    private fun initEmojiLayout() {
    }

    fun addReaction(list: List<Reaction>) {
        val viewsToRemove = mutableListOf<View>()
        for (i in 1 until emojiLayout.childCount) {
            viewsToRemove.add(emojiLayout.getChildAt(i))
        }
        viewsToRemove.forEach {
            emojiLayout.removeView(it)
        }
        list.forEach { reaction ->
            emojiLayout.addEmoji(reaction)
        }
    }

    fun addOnAddButtonClickListener(listener: () -> Unit) {
        emojiLayout.addOnAddButtonClickListener(listener)
    }

    fun addOnEmojiChangedListener(listener: (view: EmojiView) -> Unit) {
        emojiLayout.onEmojiChangedListener = listener
    }

}
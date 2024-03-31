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
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import androidx.core.view.marginStart
import androidx.core.view.marginTop
import androidx.core.view.updateLayoutParams
import ru.gozerov.tfs_spring.R
import ru.gozerov.tfs_spring.screens.chat.adapters.message.Reaction
import ru.gozerov.tfs_spring.utils.dp

class MessageCardLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0,
    defTheme: Int = 0
) : ViewGroup(context, attributeSet, defStyle, defTheme) {

    private val imageView: ImageView
        get() = getChildAt(0) as ImageView

    private val usernameTextView: TextView
        get() = getChildAt(1) as TextView

    private val dateTextView: TextView
        get() = getChildAt(2) as TextView

    private val messageTextView: TextView
        get() = getChildAt(3) as TextView
    private val emojiLayout: FlexBoxLayout
        get() = getChildAt(4) as FlexBoxLayout

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

    private val imageSize = 40f.dp(context).toInt()
    private val defaultMargin = 8f.dp(context).toInt()
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
        context.withStyledAttributes(attributeSet, R.styleable.CharCardLayout) {
            val backgroundDrawable = this.getDrawable(R.styleable.CharCardLayout_android_background)
            this@MessageCardLayout.background = backgroundDrawable
        }

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
            (parentWidth * 0.7f).toInt() - dateTextView.measuredWidth - imageView.measuredWidth - defaultMargin * 4,
            MeasureSpec.AT_MOST
        )
        messageTextView.measure(messageDesiredWidth, messageTextView.measuredHeight)
        emojiLayout.measure(messageDesiredWidth, emojiLayout.measuredHeight)
        usernameTextView.measure(messageDesiredWidth, usernameTextView.measuredHeight)

        val wantedHeight = maxOf(
            paddingTop + paddingBottom + imageView.measuredHeight + imageView.marginTop,
            paddingTop + paddingBottom + imageView.marginTop + usernameTextView.measuredHeight + messageTextView.measuredHeight + emojiLayout.measuredHeight
        ) + 3 * defaultMargin
        var wantedWidth = paddingStart + paddingEnd + imageView.measuredWidth + maxOf(
            emojiLayout.measuredWidth,
            maxOf(usernameTextView.measuredWidth, messageTextView.measuredWidth)
        ) + defaultMargin * 4
        wantedWidth = if (emojiLayout.lineCount > 1) parentWidth else maxOf(
            wantedWidth,
            180f.dp(context).toInt()
        )

        val actualWidth = resolveSize(wantedWidth, widthMeasureSpec)
        val actualHeight = resolveSize(
            wantedHeight,
            heightMeasureSpec
        )
        setMeasuredDimension(parentWidth, actualHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val imageStart = paddingStart + imageView.marginStart
        val imageEnd = imageStart + imageView.measuredWidth
        val imageTop = paddingTop + imageView.marginTop
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

        val end = if (emojiLayout.lineCount > 1) width*0.7f else minOf(
            (width * 0.7f).toInt(),
            maxOf(maxOf(emojiLayout.right, messageTextView.right), usernameTextView.right)
        ).toFloat() + defaultMargin
        val bottom =
            paddingTop + 24f.dp(context) + usernameTextView.measuredHeight + messageTextView.measuredHeight

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
        val imageView = ImageView(context)
        imageView.id = R.id.emojiAvatar
        imageView.clipToOutline = true
        imageView.background = ContextCompat.getDrawable(context, R.drawable.image_bg)
        imageView.layoutParams = MarginLayoutParams(imageSize, imageSize)
        imageView.updateLayoutParams<MarginLayoutParams> {
            this.marginStart = defaultMargin
            this.topMargin = defaultMargin
        }
        addView(imageView)
    }

    private fun initUsername(attributeSet: AttributeSet?) {
        val usernameTextView = TextView(context)
        usernameTextView.id = R.id.emojiName
        usernameTextView.layoutParams = MarginLayoutParams(context, attributeSet)
        usernameTextView.updateLayoutParams<MarginLayoutParams> {
            this.marginStart = defaultMargin
        }
        usernameTextView.setTextColor(context.getColor(R.color.teal_400))
        addView(usernameTextView)
    }

    private fun initDate() {
        val dateTextView = TextView(context)
        dateTextView.id = R.id.emojiDate
        dateTextView.setTextColor(Color.BLACK)
        addView(dateTextView)
    }

    private fun initMessage() {
        val messageTextView = TextView(context)
        messageTextView.id = R.id.emojiMessage
        messageTextView.setTextColor(context.getColor(R.color.white))
        addView(messageTextView)
    }

    private fun initEmojiLayout() {
        val flexBoxLayout = FlexBoxLayout(context)
        flexBoxLayout.id = R.id.emojiLayout
        addView(flexBoxLayout)
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
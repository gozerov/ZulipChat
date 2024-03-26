package ru.gozerov.tfs_spring

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

    private val nameTextView: TextView
        get() = getChildAt(1) as TextView

    private val dateTextView: TextView
        get() = getChildAt(2) as TextView

    private val messageTextView: TextView
        get() = getChildAt(3) as TextView
    private val emojiLayout: FlexBoxLayout
        get() = getChildAt(4) as FlexBoxLayout

    var name = ""
        set(value) {
            if (field != value) {
                field = value
                nameTextView.text = field
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
        initName(attributeSet)
        initDate()
        initMessage()
        initEmojiLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            measureChild(view, widthMeasureSpec, heightMeasureSpec)
        }
        val parentWidth = (MeasureSpec.getSize(widthMeasureSpec) * 0.7f).toInt()
        val messageDesiredWidth = MeasureSpec.makeMeasureSpec(
            parentWidth - dateTextView.measuredWidth - imageView.measuredWidth - defaultMargin * 4,
            MeasureSpec.AT_MOST
        )
        messageTextView.measure(messageDesiredWidth, messageTextView.measuredHeight)
        emojiLayout.measure(messageDesiredWidth, emojiLayout.measuredHeight)
        val wantedHeight = maxOf(
            paddingTop + paddingBottom + imageView.measuredHeight + imageView.marginTop,
            paddingTop + paddingBottom + imageView.marginTop + nameTextView.measuredHeight + messageTextView.measuredHeight + emojiLayout.measuredHeight
        ) + 3 * defaultMargin
        /*var wantedWidth = paddingStart + paddingEnd + imageView.measuredWidth + maxOf(
            emojiLayout.measuredWidth,
            maxOf(nameTextView.measuredWidth, messageTextView.measuredWidth)
        ) + defaultMargin * 4*/

        val actualWidth = resolveSize(parentWidth, widthMeasureSpec)
        val actualHeight = resolveSize(
            wantedHeight,
            heightMeasureSpec
        )
        setMeasuredDimension(actualWidth, actualHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val imageStart = paddingStart + imageView.marginStart
        val imageEnd = imageStart + imageView.measuredWidth
        val imageTop = paddingTop + imageView.marginTop
        val imageBottom = imageTop + imageView.measuredHeight

        val nameStart = imageEnd + nameTextView.marginStart + defaultMargin
        val nameBottom = imageTop + defaultMargin + nameTextView.measuredHeight

        val dateEnd = width - defaultMargin
        val dateStart = dateEnd - dateTextView.measuredWidth

        val messageBottom = nameBottom + messageTextView.measuredHeight

        imageView.layout(imageStart, imageTop, imageEnd, imageBottom)
        //dateTextView.layout(dateStart, imageTop, dateEnd, imageTop + dateTextView.measuredHeight)

        nameTextView.layout(nameStart, imageTop + defaultMargin, dateStart, nameBottom)

        messageTextView.layout(nameStart, nameBottom, dateStart, messageBottom)

        emojiLayout.layout(
            imageEnd + defaultMargin,
            messageBottom + 2 * defaultMargin,
            dateStart,
            messageBottom + 2 * defaultMargin + emojiLayout.measuredHeight
        )
    }

    override fun onDraw(canvas: Canvas) {
        val start = (imageView.left + imageView.measuredWidth + defaultMargin).toFloat()

        val bottom =
            paddingTop + 24f.dp(context) + nameTextView.measuredHeight + messageTextView.measuredHeight

        canvas.drawRoundRect(
            start,
            imageView.top.toFloat(),
            measuredWidth.toFloat(),
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

    private fun initName(attributeSet: AttributeSet?) {
        val nameTextView = TextView(context)
        nameTextView.id = R.id.emojiName
        nameTextView.layoutParams = MarginLayoutParams(context, attributeSet)
        nameTextView.updateLayoutParams<MarginLayoutParams> {
            this.marginStart = defaultMargin
        }
        nameTextView.setTextColor(context.getColor(R.color.teal_400))
        addView(nameTextView)
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
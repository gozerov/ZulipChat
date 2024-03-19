package ru.gozerov.tfs_spring

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import androidx.core.view.marginStart
import androidx.core.view.marginTop
import androidx.core.view.updateLayoutParams
import ru.gozerov.tfs_spring.utils.dp

class ChatCardLayout @JvmOverloads constructor(
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
            }
        }

    var message = ""
        set(value) {
            if (field != value) {
                field = value
                messageTextView.text = field
                requestLayout()
            }
        }

    var date = ""
        set(value) {
            if (field != value) {
                field = value
                dateTextView.text = field
                requestLayout()
            }
        }

    private val imageSize = 72f.dp(context).toInt()
    private val defaultMargin = 8f.dp(context).toInt()

    var imageDrawable: Drawable? = null
        set(value) {
            field = value
            imageView.setImageDrawable(field)
            requestLayout()
        }

    init {
        context.withStyledAttributes(attributeSet, R.styleable.CharCardLayout) {
            val backgroundDrawable = this.getDrawable(R.styleable.CharCardLayout_android_background)
            this@ChatCardLayout.background = backgroundDrawable
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
        val parentWidth = MeasureSpec.getSize(widthMeasureSpec)
        val messageDesiredWidth = MeasureSpec.makeMeasureSpec(
            parentWidth - dateTextView.measuredWidth - imageView.measuredWidth - defaultMargin * 3,
            MeasureSpec.EXACTLY
        )
        messageTextView.measure(messageDesiredWidth, messageTextView.measuredHeight)
        emojiLayout.measure(messageDesiredWidth, emojiLayout.measuredHeight)
        val wantedHeight = maxOf(
            paddingTop + paddingBottom + imageView.measuredHeight + imageView.marginTop,
            paddingTop + paddingBottom + imageView.marginTop + nameTextView.measuredHeight + messageTextView.measuredHeight + emojiLayout.measuredHeight
        )
        val actualSize = resolveSize(
            wantedHeight,
            heightMeasureSpec
        )
        setMeasuredDimension(widthMeasureSpec, actualSize)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val imageStart = paddingStart + imageView.marginStart
        val imageEnd = imageStart + imageView.measuredWidth
        val imageTop = paddingTop + imageView.marginTop
        val imageBottom = imageTop + imageView.measuredHeight

        val nameStart = imageEnd + nameTextView.marginStart
        val nameBottom = imageTop + nameTextView.measuredHeight

        val dateEnd = width - defaultMargin
        val dateStart = dateEnd - dateTextView.measuredWidth

        val messageBottom = nameBottom + messageTextView.measuredHeight

        imageView.layout(imageStart, imageTop, imageEnd, imageBottom)
        dateTextView.layout(dateStart, imageTop, dateEnd, imageTop + dateTextView.measuredHeight)

        nameTextView.layout(nameStart, imageTop, dateStart, nameBottom)

        messageTextView.layout(nameStart, nameBottom, dateStart, messageBottom)

        emojiLayout.layout(
            nameStart,
            messageBottom + defaultMargin,
            dateStart,
            messageBottom + defaultMargin + emojiLayout.measuredHeight
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
        nameTextView.setTextColor(Color.BLACK)
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
        messageTextView.setTextColor(Color.BLACK)
        addView(messageTextView)
    }

    private fun initEmojiLayout() {
        val flexBoxLayout = FlexBoxLayout(context)
        flexBoxLayout.id = R.id.emojiLayout
        addView(flexBoxLayout)
    }

    fun addEmoji(list: List<Emoji>) {
        list.forEach { emoji ->
            emojiLayout.addEmoji(emoji)
        }
        requestLayout()
    }

    fun addEmoji(emoji: Emoji) {
        emojiLayout.addEmoji(emoji)
        requestLayout()
    }

    fun addOnAddButtonClickListener(listener: () -> Unit) {
        emojiLayout.addOnAddButtonClickListener(listener)
    }

    fun addOnEmojiChangedListener(listener: (view: EmojiView) -> Unit) {
        emojiLayout.onEmojiChangedListener = listener
    }

}
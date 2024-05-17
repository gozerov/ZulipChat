package ru.gozerov.tfs_spring.core.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.Html
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.withStyledAttributes
import ru.gozerov.tfs_spring.R
import ru.gozerov.tfs_spring.core.utils.dp
import ru.gozerov.tfs_spring.presentation.screens.channels.chat.adapters.message.Reaction

class UserMessageCardLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0,
    defTheme: Int = 0
) : ViewGroup(context, attributeSet, defStyle, defTheme) {

    private val dateTextView: TextView

    private val messageTextView: TextView

    private val emojiLayout: FlexBoxLayout

    var message = ""
        set(value) {
            if (field != value) {
                field = value
                messageTextView.text = Html.fromHtml(field, Html.FROM_HTML_MODE_COMPACT)
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

    private var defaultMargin = 8f.dp(context).toInt()
    private val backgroundPaint = Paint().apply {
        style = Paint.Style.FILL
        this.setColor(context.getColor(R.color.teal_400))
    }

    init {
        setWillNotDraw(false)
        inflate(context, R.layout.layout_user_message_card, this)

        dateTextView = findViewById(R.id.txtDate)
        messageTextView = findViewById(R.id.txtUserMessage)
        emojiLayout = findViewById(R.id.emojiLayout)

        var margin = 0

        context.withStyledAttributes(attributeSet, R.styleable.MessageCardLayout) {
            val backgroundDrawable =
                this.getDrawable(R.styleable.MessageCardLayout_android_background)
            this@UserMessageCardLayout.background = backgroundDrawable

            margin = this.getDimension(R.styleable.MessageCardLayout_defaultMargin, 8f.dp(context))
                .toInt()

            val message = this.getString(R.styleable.MessageCardLayout_message)
            messageTextView.text = message

        }
        defaultMargin = margin

        initDate()
        initMessage()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            measureChild(view, widthMeasureSpec, heightMeasureSpec)
        }
        val parentWidth = (MeasureSpec.getSize(widthMeasureSpec))
        val messageDesiredWidth = MeasureSpec.makeMeasureSpec(
            (parentWidth * 0.6).toInt() - dateTextView.measuredWidth,
            MeasureSpec.AT_MOST
        )
        messageTextView.measure(messageDesiredWidth, messageTextView.measuredHeight)
        emojiLayout.measure(messageDesiredWidth, emojiLayout.measuredHeight)
        val wantedHeight =
            paddingTop + paddingBottom + messageTextView.measuredHeight + emojiLayout.measuredHeight + 2 * defaultMargin

        val actualHeight = resolveSize(
            wantedHeight,
            heightMeasureSpec
        )
        setMeasuredDimension(parentWidth, actualHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val messageEnd = width - 2 * defaultMargin
        val messageStart = messageEnd - messageTextView.measuredWidth
        val messageTop = paddingTop + defaultMargin
        val messageBottom = messageTop + messageTextView.measuredHeight
        messageTextView.layout(messageStart, messageTop, messageEnd, messageBottom)

        emojiLayout.layout(
            messageEnd - emojiLayout.measuredWidth,
            messageBottom + 2 * defaultMargin,
            messageEnd,
            messageBottom + 2 * defaultMargin + emojiLayout.measuredHeight
        )
    }

    override fun onDraw(canvas: Canvas) {
        val minWidth = if (emojiLayout.childCount == 1) messageTextView.measuredWidth else maxOf(
            messageTextView.measuredWidth,
            emojiLayout.measuredWidth
        )
        val actualWidth = minOf((width * 0.6).toInt(), minWidth).toFloat()
        val end = width.toFloat() - defaultMargin
        val start = width - defaultMargin * 3 - actualWidth
        val top = paddingTop.toFloat()
        val bottom = top + messageTextView.measuredHeight.toFloat() + 2 * defaultMargin
        canvas.drawRoundRect(
            start,
            top,
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

    private fun initDate() {
        dateTextView.setTextColor(Color.BLACK)
    }

    private fun initMessage() {
        messageTextView.setTextColor(context.getColor(R.color.white))
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
            val emojiView = EmojiView(context)
            emojiView.emojiName = reaction.emojiName
            emojiView.count = reaction.count
            emojiView.emojiCode = reaction.emojiCode
            emojiView.isEmojiSelected = reaction.isSelected

            emojiView.onEmojiChangedListener = { view ->
                if (view.count == 0) emojiLayout.removeView(view)
                emojiLayout.onEmojiChangedListener?.invoke(view)
            }
            emojiView.setBackgroundResource(R.drawable.emoji_bg)
            emojiView.id = childCount
            emojiLayout.addChild(emojiView)
        }
    }

    fun addOnAddButtonClickListener(listener: () -> Unit) {
        emojiLayout.addOnAddButtonClickListener(listener)
    }

    fun addOnEmojiChangedListener(listener: (view: EmojiView) -> Unit) {
        emojiLayout.onEmojiChangedListener = listener
    }

}
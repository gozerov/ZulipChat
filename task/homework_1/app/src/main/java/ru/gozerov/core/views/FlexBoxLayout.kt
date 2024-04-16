package ru.gozerov.core.views

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.withStyledAttributes
import ru.gozerov.tfs_spring.R
import ru.gozerov.tfs_spring.screens.channels.chat.adapters.message.Reaction
import ru.gozerov.tfs_spring.utils.dp

class FlexBoxLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0,
    defTheme: Int = 0
) : ViewGroup(context, attributeSet, defStyle, defTheme) {

    private var innerMargin = 4f.dp(context).toInt()
    private val bottomMargin = 8f.dp(context).toInt()

    private val emojiHorizontalPadding = 12f.dp(context).toInt()
    private val emojiVerticalPadding = 8f.dp(context).toInt()

    private val addButtonPadding = 4f.dp(context).toInt()
    private val addButtonWidth = 42f.dp(context).toInt()
    private val addButtonHeight = 28f.dp(context).toInt()

    var lineCount = DEFAULT_LINE_COUNT
        private set

    var onEmojiChangedListener: ((view: EmojiView) -> Unit)? = null

    private val addButton
        get() = getChildAt(0)

    private val firstEmoji
        get() = getChildAt(1)

    init {
        val addButton = ImageView(context)
        addButton.setImageResource(R.drawable.ic_add_24)
        addButton.layoutParams = MarginLayoutParams(addButtonWidth, addButtonHeight)
        addButton.setPadding(
            addButtonPadding,
            addButtonPadding,
            addButtonPadding,
            addButtonPadding
        )
        addButton.setBackgroundResource(R.drawable.add_bg)

        context.withStyledAttributes(attributeSet, R.styleable.FlexBoxLayout) {
            innerMargin =
                getDimension(R.styleable.FlexBoxLayout_innerPadding, innerMargin.toFloat()).toInt()
        }
        addView(addButton)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        for (i in 0 until childCount) {
            val childView = getChildAt(i)
            if (childView.layoutParams !is MarginLayoutParams)
                measureChild(childView, widthMeasureSpec, heightMeasureSpec)
            else
                measureChildWithMargins(childView, widthMeasureSpec, 0, heightMeasureSpec, 0)
        }
        lineCount = DEFAULT_LINE_COUNT

        val parentWidth = MeasureSpec.getSize(widthMeasureSpec)
        var actualWidth = 0
        var wantedWidth = 0

        for (i in 1 until childCount) {
            val view = getChildAt(i)
            if (wantedWidth + view.measuredWidth > parentWidth) {
                lineCount++
                actualWidth = wantedWidth - innerMargin
                wantedWidth = view.measuredWidth
            } else {
                wantedWidth += view.measuredWidth + innerMargin
            }
        }
        if (actualWidth == 0) actualWidth = wantedWidth
        if (wantedWidth + addButton.measuredWidth > parentWidth)
            lineCount++
        else if (lineCount == 1)
            actualWidth += addButton.measuredWidth
        val cellHeight =
            if (firstEmoji != null) firstEmoji.measuredHeight else addButton.measuredWidth

        val actualHeight = if (lineCount > 1) {
            lineCount * (cellHeight + innerMargin) + bottomMargin
        } else {
            if (firstEmoji != null)
                cellHeight + innerMargin + bottomMargin
            else
                0
        } + paddingTop + paddingBottom

        setMeasuredDimension(actualWidth, actualHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var start = paddingStart
        var top = 0
        var bottom =
            if (firstEmoji != null) firstEmoji.measuredHeight else addButton.measuredWidth

        for (i in 1 until childCount) {
            val view = getChildAt(i)
            if (start + view.measuredWidth + innerMargin > width) {
                start = paddingStart
                top = bottom + innerMargin
                bottom = top + view.measuredHeight
            }
            val viewStart = start + if (start != 0) innerMargin else 0
            val viewEnd = viewStart + view.measuredWidth
            val viewTop = top
            val viewBottom = bottom
            view.layout(viewStart, viewTop, viewEnd, viewBottom)
            start = viewEnd
        }
        if (firstEmoji != null) {
            val addButton = this.addButton
            start += innerMargin
            if (start + addButton.measuredWidth > width) {
                start = paddingStart
                top = bottom + innerMargin
                bottom = top + addButton.measuredHeight
            }
            val addStart = start
            val addEnd = addStart + addButton.measuredWidth
            addButton.layout(addStart, top, addEnd, bottom)
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    fun addEmoji(reaction: Reaction) {
        val emojiView = EmojiView(context)
        addView(emojiView)
        emojiView.emojiName = reaction.emojiName
        emojiView.count = reaction.count
        emojiView.emojiCode = reaction.emojiCode
        emojiView.emojiType = reaction.emojiType
        emojiView.isEmojiSelected = reaction.isSelected
        emojiView.setPadding(
            emojiHorizontalPadding,
            emojiVerticalPadding,
            emojiHorizontalPadding,
            emojiVerticalPadding
        )
        emojiView.onEmojiChangedListener = { view ->
            removeEmojiView(view)
            onEmojiChangedListener?.invoke(view)
        }
        emojiView.setBackgroundResource(R.drawable.emoji_bg)
        emojiView.id = childCount
    }

    fun addOnAddButtonClickListener(listener: () -> Unit) {
        addButton.setOnClickListener {
            listener()
        }
    }

    private fun removeEmojiView(view: EmojiView) {
        if (view.count == 0) {
            removeView(view)
        }
    }

    companion object {
        private const val DEFAULT_LINE_COUNT = 1
    }

}
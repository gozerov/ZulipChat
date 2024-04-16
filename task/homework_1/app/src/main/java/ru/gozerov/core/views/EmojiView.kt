package ru.gozerov.core.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.os.Bundle
import android.os.Parcelable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import androidx.core.os.bundleOf
import ru.gozerov.tfs_spring.R
import ru.gozerov.tfs_spring.utils.sp

class EmojiView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0,
    defTheme: Int = 0
) : View(context, attributeSet, defStyle, defTheme), View.OnClickListener {

    var emojiCode: String = ""
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
                invalidate()
            }
        }

    var emojiName: String = ""
    var emojiType: String = ""

    var count: Int = DEFAULT_COUNT
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
                invalidate()
            }
        }

    var isEmojiSelected: Boolean = false
        set(value) {
            if (field != value) {
                field = value
                this.isSelected = field
                requestLayout()
                invalidate()
            }
        }

    var onEmojiChangedListener: ((view: EmojiView) -> Unit)? = null

    private val text: String
        get() = "$emojiCode $count"

    private val textPaint = TextPaint().apply {
        color = context.getColor(R.color.white)
    }

    private val textRect = Rect()
    private val defaultTextSize = 14f.sp(context)

    init {
        context.withStyledAttributes(attributeSet, R.styleable.EmojiView) {
            count = getInt(R.styleable.EmojiView_count, DEFAULT_COUNT)
            textPaint.textSize =
                getDimension(R.styleable.EmojiView_android_textSize, defaultTextSize)
        }
        setOnClickListener(this)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        textPaint.getTextBounds(text, 0, text.length, textRect)
        val actualWidth =
            resolveSize(paddingStart + paddingEnd + textRect.width(), widthMeasureSpec)
        val actualHeight =
            resolveSize(paddingTop + paddingBottom + textRect.height(), heightMeasureSpec)

        setMeasuredDimension(actualWidth, actualHeight)
    }

    override fun onDraw(canvas: Canvas) {
        val topOffset = height / 2 - textRect.exactCenterY()
        canvas.drawText(text, paddingStart.toFloat(), topOffset, textPaint)
    }

    override fun onClick(v: View?) {
        v?.let {
            if (it.isSelected)
                count--
            else
                count++
            isEmojiSelected = !isEmojiSelected
            onEmojiChangedListener?.invoke(v as EmojiView)
        }
    }

    override fun onSaveInstanceState(): Parcelable {
        return bundleOf(
            SUPER_STATE to super.onSaveInstanceState(),
            ARG_COUNT to count,
            ARG_IS_SELECTED to isSelected
        )
    }

    @Suppress("DEPRECATION")
    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle) {
            val bundle: Bundle = state
            count = bundle.getInt(ARG_COUNT)
            isSelected = bundle.getBoolean(ARG_IS_SELECTED)
            super.onRestoreInstanceState(bundle.getParcelable(SUPER_STATE))
        }
    }

    companion object {

        private const val DEFAULT_COUNT = 0

        private const val SUPER_STATE = "superState"
        private const val ARG_COUNT = "count"
        private const val ARG_IS_SELECTED = "isSelected"

    }

}
package ru.gozerov.tfs_spring.core.utils

import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView
import ru.gozerov.tfs_spring.R

class HorizontalMarginItemDecoration(
    @DimenRes private val innerMarginDimenId: Int = R.dimen._4dp,
    @DimenRes private val outerMarginDimenId: Int = R.dimen._8dp
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val adapter = parent.adapter ?: return
        val currentPosition =
            parent.getChildAdapterPosition(view).takeIf { it != RecyclerView.NO_POSITION } ?: return

        val isPrevTargetView = adapter.isPrevTargetView(currentPosition)
        val isNextTargetView = adapter.isNextTargetView(currentPosition)

        val innerMargin = view.resources.getDimensionPixelSize(innerMarginDimenId)
        val outerMargin = view.resources.getDimensionPixelSize(outerMarginDimenId)

        with(outRect) {
            left = if (isPrevTargetView) innerMargin else outerMargin
            right = if (isNextTargetView) innerMargin else outerMargin
        }
    }

    private fun RecyclerView.Adapter<*>.isPrevTargetView(
        currentPosition: Int
    ) = currentPosition != 0

    private fun RecyclerView.Adapter<*>.isNextTargetView(
        currentPosition: Int
    ): Boolean {
        val lastIndex = itemCount - 1
        return currentPosition != lastIndex
    }

}
package ru.gozerov.tfs_spring.utils

import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView
import ru.gozerov.tfs_spring.R

class GridMarginItemDecoration(
    @DimenRes private val innerMarginDimenId: Int = R.dimen._4dp,
    @DimenRes private val outerMarginDimenId: Int = R.dimen._8dp,
    private val spanCount: Int
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
        val isTopTow = adapter.isTopRow(currentPosition)
        val isBottomRow = adapter.isBottomRow(currentPosition)

        val innerMargin = view.resources.getDimensionPixelSize(innerMarginDimenId)
        val outerMargin = view.resources.getDimensionPixelSize(outerMarginDimenId)

        with(outRect) {
            top = if (isTopTow) outerMargin else innerMargin
            bottom = if (isBottomRow) outerMargin else innerMargin
            left = if (isPrevTargetView) innerMargin else outerMargin
            right = if (isNextTargetView) innerMargin else outerMargin
        }
    }

    private fun RecyclerView.Adapter<*>.isTopRow(currentPosition: Int) = currentPosition < 7

    private fun RecyclerView.Adapter<*>.isBottomRow(currentPosition: Int) =
        itemCount - currentPosition <= 7

    private fun RecyclerView.Adapter<*>.isPrevTargetView(
        currentPosition: Int
    ) = currentPosition % spanCount != 0

    private fun RecyclerView.Adapter<*>.isNextTargetView(
        currentPosition: Int
    ): Boolean = (currentPosition + 1) % spanCount != 0

}
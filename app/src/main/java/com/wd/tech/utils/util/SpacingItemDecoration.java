package com.wd.tech.utils.util;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 社区图片显示需要类
 */
public class SpacingItemDecoration extends RecyclerView.ItemDecoration {
    private int space;
    public SpacingItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.top = space / 2;
        outRect.bottom = space / 2;
        outRect.left = space / 2;
        outRect.right = space / 2;
    }
}

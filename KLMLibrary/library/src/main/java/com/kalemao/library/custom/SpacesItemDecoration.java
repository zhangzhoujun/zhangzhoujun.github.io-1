package com.kalemao.library.custom;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kalemao.library.logutils.LogUtil;

import static android.R.attr.tag;

/**
 * RecyclerView增加分割线
 *
 * Created by dim on 2017/6/12 10:33 邮箱：271756926@qq.com
 */

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpacesItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if(view.getTag() == null){
            outRect.left = 0;
            outRect.right = 0;
            outRect.top = 0;
            outRect.bottom = space;
        } else if(view.getTag().equals("item")){
            outRect.left = space;
            outRect.right = space;
            outRect.top = 0;
            outRect.bottom = 2 * space;
        } else if(view.getTag().equals("top")){
            outRect.left = 0;
            outRect.right = 0;
            outRect.top = 0;
            outRect.bottom = 0;
        } else if(view.getTag().equals("show")){
            outRect.left = 0;
            outRect.right = 0;
            outRect.top = 0;
            outRect.bottom = 2 * space;
        } else if(view.getTag().equals("photo")){
            outRect.left = space;
            outRect.right = space;
            outRect.top = space;
            outRect.bottom = space;
        }
    }
}
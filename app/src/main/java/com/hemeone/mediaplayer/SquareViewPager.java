package com.hemeone.mediaplayer;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by yanglinghui on 2016-06-11.
 */
public class SquareViewPager extends ViewPager {
    public SquareViewPager(Context context) {
        super(context);
    }

    public SquareViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getLayoutParams();
        View parent = (View) getParent();
        parent.setBackgroundColor(Color.BLUE);
        if (parent.getHeight() >= params.topMargin + widthMeasureSpec) {
            super.onMeasure(widthMeasureSpec, widthMeasureSpec - params.topMargin);
        } else {
            System.out.println(parent.getHeight() - params.topMargin);
            super.onMeasure(widthMeasureSpec, parent.getHeight() - params.topMargin);
        }
    }
}

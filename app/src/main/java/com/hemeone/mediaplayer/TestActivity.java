package com.hemeone.mediaplayer;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

/**
 * Created by yanglinghui on 2016-06-08.
 */
public class TestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyView(this));
    }

    private class MyView extends View {
        private float RADIUS = 10f;
        private ValueAnimator animator;
        private Point currentPoint;
        private Paint mPaint;

        public MyView(Context context) {
            super(context);
        }

        public MyView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        private boolean down = true;

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawColor(Color.WHITE);
            if (currentPoint == null) {
                mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                mPaint.setColor(Color.RED);
                currentPoint = new Point(RADIUS, RADIUS);
                drawCircle(canvas);
                startAnimation();
            } else {
                drawCircle(canvas);
            }
        }

        private void drawCircle(Canvas canvas) {
            float x = currentPoint.getX();
            float y = currentPoint.getY();
            canvas.drawCircle(x, y, RADIUS, mPaint);
        }

        private void startAnimation() {
            Point startPoint = new Point(RADIUS, RADIUS);
            Point endPoint = new Point(RADIUS, getHeight() - RADIUS);
            ValueAnimator anim = ValueAnimator.ofObject(new PointEvaluator(), startPoint, endPoint);
            anim.setInterpolator(new BounceInterpolator());
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    currentPoint = (Point) animation.getAnimatedValue();
                    invalidate();
                }
            });
            anim.setDuration(5000);
            anim.start();
        }
    }

    public static class Point {
        private float x;
        private float y;

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }
    }

    public static class PointEvaluator implements TypeEvaluator<Point> {
        private int xxxx = 0;

        @Override
        public Point evaluate(float fraction, Point startPoint, Point endPoint) {
            xxxx += 1.5;
            float x = startPoint.getX() + xxxx;
            float y = startPoint.getY() + fraction * (endPoint.getY() - startPoint.getY());
            Point point = new Point(x, y);
            return point;
        }
    }
}

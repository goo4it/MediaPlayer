package com.hemeone.mediaplayer;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by yanglinghui on 2016-06-07.
 */
public class MediaPlayView extends View {
    private Bitmap play_needle;
    private Bitmap play_disc;
    private Bitmap play_avater;
    private Paint mBitPaint;
    private ObjectAnimator mNeedleAnim;
    private float needleScale;
    private float disc_x;
    private float disc_y;
    private float needle_x;
    private float needle_y;
    private float bd_x, bd_y;

    private boolean isInited = false;

    public MediaPlayView(Context context) {
        super(context);
    }

    public MediaPlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MediaPlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private void init() {
        isInited = true;
        play_needle = ((BitmapDrawable) getResources().getDrawable(R.drawable.play_needle)).getBitmap();
        play_disc = ((BitmapDrawable) getResources().getDrawable(R.drawable.play_disc)).getBitmap();
        play_avater = ((BitmapDrawable) getResources().getDrawable(R.drawable.placeholder_disk_play_song)).getBitmap();
        needleScale = play_needle.getWidth() / 276f;
        disc_x = getWidth() / 2f - play_disc.getWidth() / 2f;
        disc_y = (365 - 210) * needleScale;
        needle_x = getWidth() / 2f;
        needle_y = -49 * needleScale;
        bd_x = disc_x + play_disc.getWidth() / 2;
        bd_y = disc_y + play_disc.getHeight() / 2;
        play_avater = centerSquareScaleBitmap(play_avater, (int) (needleScale * 550f));
        play_avater = createCircleImage(play_avater);
        mBitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBitPaint.setFilterBitmap(true);
        mBitPaint.setDither(true);
        mBitPaint.setAntiAlias(true);
    }

    private float needleRotareStart = 0,
            needleRotareEnd = -30,
            needleRotare = needleRotareEnd,
            discRotracre = 0;
    private boolean isPlay = false,
            isNext = false, isNextIn = false,
            isPrev = false, isPrevIn = false;

    @Override
    protected void onDraw(Canvas canvas) {
        if (isInEditMode()) {
            return;
        }
        if (!isInited) init();
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#1AFFFFFF"));
        canvas.drawCircle(bd_x, bd_y, play_disc.getWidth() / 2, paint);
        paint.setColor(Color.parseColor("#33000000"));
        canvas.drawCircle(bd_x, bd_y, play_disc.getWidth() / 2 - (10 * needleScale), paint);

        Matrix avaterMatrix = new Matrix();
        avaterMatrix.postTranslate(
                disc_x + play_disc.getWidth() / 2 - play_avater.getWidth() / 2,
                disc_y + play_disc.getHeight() / 2 - play_avater.getWidth() / 2);
        avaterMatrix.postRotate(discRotracre, disc_x + play_disc.getWidth() / 2, disc_y + play_disc.getHeight() / 2);
        canvas.drawBitmap(play_avater, avaterMatrix, mBitPaint);

        Matrix discMatrix = new Matrix();
        discMatrix.postTranslate(disc_x, disc_y);
        discMatrix.postRotate(discRotracre, disc_x + play_disc.getWidth() / 2, disc_y + play_disc.getHeight() / 2);
        canvas.drawBitmap(play_disc, discMatrix, mBitPaint);
        if (isPrev || isPrevIn) {
            // prev
            float start = disc_x - getWidth() / 2f - play_disc.getWidth() / 2;
            canvas.drawBitmap(play_avater, start + play_disc.getWidth() / 2f - play_avater.getWidth() / 2,
                    disc_y + play_disc.getHeight() / 2 - play_avater.getWidth() / 2, mBitPaint);
            canvas.drawBitmap(play_disc, start, disc_y, mBitPaint);
        } else if (isNextIn || isNext) {
            // next
            float start1 = getWidth() / 2f + play_disc.getWidth() / 2f + disc_x;
            canvas.drawBitmap(play_avater, start1 + play_disc.getWidth() / 2f - play_avater.getWidth() / 2,
                    disc_y + play_disc.getHeight() / 2 - play_avater.getWidth() / 2, mBitPaint);
            canvas.drawBitmap(play_disc, start1, disc_y, mBitPaint);

        }

        Matrix matrix = new Matrix();
        matrix.postTranslate(needle_x, needle_y);
        matrix.postRotate(needleRotare, needle_x + 49 * needleScale, needle_y + 49 * needleScale);
        canvas.drawBitmap(play_needle, matrix, mBitPaint);

        Paint linePaint = new Paint();
        linePaint.setAntiAlias(true);
        Shader shader = new RadialGradient(getWidth() / 2, 1, getWidth() / 2,
                new int[]{0xFFFFFFFF, 0x19FFFFFF}, null, Shader.TileMode.REPEAT);
        linePaint.setShader(shader);
        linePaint.setStrokeWidth(0.5f);
        canvas.drawLine(0, 0, getWidth(), 0, linePaint);

        if (isPlay) {
            discRotracre += 0.2f;
            if (discRotracre >= 360f) discRotracre = 0;
            if (needleRotare < needleRotareStart) needleRotare += 1;
            invalidate();
        } else if (isNextIn || isNext || isPrev || isPrevIn) {
            if (needleRotare > needleRotareEnd) needleRotare -= 1;
            if (isNext) {
                disc_x -= 40;
                if (disc_x <= -(play_disc.getWidth()) + 10 * needleScale) {
//                    disc_x = getWidth() - 10 * needleScale;
                    isNext = false;
                    isNextIn = true;
                    discRotracre = 0;
                }
            }
            if (isNextIn) {
                disc_x -= 40;
                if (disc_x <= getWidth() / 2f - play_disc.getWidth() / 2f) {
                    disc_x = getWidth() / 2f - play_disc.getWidth() / 2f;
                    isNextIn = false;
                    isPlay = true;
                    reflashUIHandler();
                    invalidate();
                    return;
                }
            }
            if (isPrev) {
                disc_x += 40;
                if (disc_x >= getWidth()) {
//                    disc_x = -(play_disc.getWidth()) + 10 * needleScale;
                    isPrev = false;
                    isPrevIn = true;
                    discRotracre = 0;
                }
            }
            if (isPrevIn) {
                disc_x += 40;
                if (disc_x >= getWidth() / 2f - play_disc.getWidth() / 2f) {
                    disc_x = getWidth() / 2f - play_disc.getWidth() / 2f;
                    isPrevIn = false;
                    isPlay = true;
                    reflashUIHandler();
                    invalidate();
                    return;
                }
            }
            invalidate();
        } else if (needleRotare > needleRotareEnd) {
            needleRotare -= 1;
            invalidate();
        }
    }

    /**
     * @param bitmap     原图
     * @param edgeLength 希望得到的正方形部分的边长
     * @return 缩放截取正中部分后的位图。
     */
    public static Bitmap centerSquareScaleBitmap(Bitmap bitmap, int edgeLength) {
        if (null == bitmap || edgeLength <= 0) {
            return null;
        }
        Bitmap result = bitmap;
        int widthOrg = bitmap.getWidth();
        int heightOrg = bitmap.getHeight();

//        if (widthOrg > edgeLength && heightOrg > edgeLength) {
        //压缩到一个最小长度是edgeLength的bitmap
        int longerEdge = (int) (edgeLength * Math.max(widthOrg, heightOrg) / Math.min(widthOrg, heightOrg));
        int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
        int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
        Bitmap scaledBitmap;
        try {
            scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
        } catch (Exception e) {
            return null;
        }

        //从图中截取正中间的正方形部分。
        int xTopLeft = (scaledWidth - edgeLength) / 2;
        int yTopLeft = (scaledHeight - edgeLength) / 2;
        try {
            result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, edgeLength, edgeLength);
            scaledBitmap.recycle();
        } catch (Exception e) {
            return null;
        }
//        }
        return result;
    }

    /**
     * 根据原图和变长绘制圆形图片
     *
     * @param source
     * @return
     */
    private Bitmap createCircleImage(Bitmap source) {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        int min = Math.min(source.getWidth(), source.getHeight());
        Bitmap target = Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_8888);
        /**
         * 产生一个同样大小的画布
         */
        Canvas canvas = new Canvas(target);
        /**
         * 首先绘制圆形
         */
        canvas.drawCircle(min / 2, min / 2, min / 2, paint);
        /**
         * 使用SRC_IN
         */
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        /**
         * 绘制图片
         */
        canvas.drawBitmap(source, 0, 0, paint);
        return target;
    }

    public boolean isPlay() {
        return isPlay;
    }

    public void play() {
        this.isPlay = true;
        uiHandler.sendEmptyMessage(0x113);
        reflashUIHandler();
        invalidate();
    }

    public void next() {
        this.isPlay = false;
        this.isNext = true;
        this.isNextIn = false;
        invalidate();
    }

    public void prev() {
        this.isPlay = false;
        this.isPrev = true;
        this.isPrevIn = false;
        invalidate();
    }

    public void pause() {
        this.isPlay = false;
        reflashUIHandler();
    }

    private void reflashUIHandler() {
        if (uiHandler == null) return;
        Message message = new Message();
        message.what = isPlay ? 0x112 : 0x111;
        uiHandler.sendMessage(message);
    }

    private Handler uiHandler;

    public void setUiHandler(Handler uiHandler) {
        this.uiHandler = uiHandler;
    }

}

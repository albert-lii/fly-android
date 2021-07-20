package org.fly.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/10/19 5:10 PM
 * @description: 带阴影的控件
 * @since: 1.0.0
 */
public class ShadowLayout extends FrameLayout {
    private static final String SHADOW_COLOR = "#14000000";
    private static final int SHADOW_RADIUS = 12;
    private static final int CORNER_RADIUS = 12;

    private int mShadowColor; // 阴影颜色
    private float mShadowRadius; // 阴影半径
    private float mCornerRadius; // 阴影四角的弧度
    private float mDx; // 阴影的横向偏移距离
    private float mDy; // 阴影的纵向偏移距离

    private boolean mInvalidateShadowOnSizeChanged = true;
    private boolean mForceInvalidateShadow = false;

    public ShadowLayout(Context context) {
        super(context);
        initView(context, null);
    }

    public ShadowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public ShadowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        initAttributes(context, attrs);
        int xPadding = (int) (mShadowRadius + Math.abs(mDx));
        int yPadding = (int) (mShadowRadius + Math.abs(mDy));
        setPadding(xPadding, yPadding, xPadding, yPadding);
    }

    private void initAttributes(Context context, AttributeSet attrs) {
        TypedArray attr = getTypedArray(context, attrs, R.styleable.fly_ShadowLayout);
        if (attr == null) {
            return;
        }
        try {
            mShadowColor = attr.getColor(R.styleable.fly_ShadowLayout_sl_shadowColor, Color.parseColor(SHADOW_COLOR));
            mShadowRadius = attr.getDimension(R.styleable.fly_ShadowLayout_sl_shadowRadius, CORNER_RADIUS);
            mCornerRadius = attr.getDimension(R.styleable.fly_ShadowLayout_sl_cornerRadius, SHADOW_RADIUS);
            mDx = attr.getDimension(R.styleable.fly_ShadowLayout_sl_dx, 0);
            mDy = attr.getDimension(R.styleable.fly_ShadowLayout_sl_dy, 0);
        } finally {
            attr.recycle();
        }
    }

    private TypedArray getTypedArray(Context context, AttributeSet attributeSet, int[] attr) {
        return context.obtainStyledAttributes(attributeSet, attr, 0, 0);
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        return 0;
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        return 0;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w > 0 && h > 0 && (getBackground() == null || mInvalidateShadowOnSizeChanged || mForceInvalidateShadow)) {
            mForceInvalidateShadow = false;
            setBackgroundCompat(w, h);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mForceInvalidateShadow) {
            mForceInvalidateShadow = false;
            setBackgroundCompat(right - left, bottom - top);
        }
    }

    @SuppressWarnings("deprecation")
    private void setBackgroundCompat(int w, int h) {
        Bitmap bitmap = createShadowBitmap(w, h, mCornerRadius, mShadowRadius, mDx, mDy, mShadowColor, Color.TRANSPARENT);
        BitmapDrawable drawable = new BitmapDrawable(getResources(), bitmap);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
            setBackgroundDrawable(drawable);
        } else {
            setBackground(drawable);
        }
    }

    private Bitmap createShadowBitmap(int shadowWidth, int shadowHeight, float cornerRadius, float shadowRadius,
                                      float dx, float dy, int shadowColor, int fillColor) {

        Bitmap output = Bitmap.createBitmap(shadowWidth, shadowHeight, Bitmap.Config.ALPHA_8);
        Canvas canvas = new Canvas(output);

        RectF shadowRect = new RectF(
                shadowRadius,
                shadowRadius,
                shadowWidth - shadowRadius,
                shadowHeight - shadowRadius);

        if (dy > 0) {
            shadowRect.top += dy;
            shadowRect.bottom -= dy;
        } else if (dy < 0) {
            shadowRect.top += Math.abs(dy);
            shadowRect.bottom -= Math.abs(dy);
        }

        if (dx > 0) {
            shadowRect.left += dx;
            shadowRect.right -= dx;
        } else if (dx < 0) {
            shadowRect.left += Math.abs(dx);
            shadowRect.right -= Math.abs(dx);
        }

        Paint shadowPaint = new Paint();
        shadowPaint.setAntiAlias(true);
        shadowPaint.setColor(fillColor);
        shadowPaint.setStyle(Paint.Style.FILL);

        if (!isInEditMode()) {
            shadowPaint.setShadowLayer(shadowRadius, dx, dy, shadowColor);
        }

        canvas.drawRoundRect(shadowRect, cornerRadius, cornerRadius, shadowPaint);
        return output;
    }

    public void setInvalidateShadowOnSizeChanged(boolean invalidateShadowOnSizeChanged) {
        mInvalidateShadowOnSizeChanged = invalidateShadowOnSizeChanged;
    }

    /**
     * 刷新阴影
     */
    public void invalidateShadow() {
        mForceInvalidateShadow = true;
        requestLayout();
        invalidate();
    }

    public void setShadowColor(int color) {
        this.mShadowColor = color;
    }

    public void setShadowRadius(int radius) {
        this.mShadowRadius = radius;
    }

    public void setCornerRadius(int radius) {
        this.mCornerRadius = radius;
    }

    public void setXOffset(int dx) {
        this.mDx = dx;
    }

    public void setYOffset(int dy) {
        this.mDy = dy;
    }
}


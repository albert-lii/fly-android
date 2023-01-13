package org.fly.uikit.progress;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import org.fly.uikit.R;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 圆形进度条，线程安全的View，可直接在线程中更新进度
 */
public class CircleProgressBar extends View {

    /**
     * 画笔对象的引用
     */
    private Paint paint;

    /**
     * 圆环的颜色
     */
    private int roundColor;

    /**
     * 圆环进度的颜色
     */
    private int roundProgressColor;

    /**
     * 中间进度百分比的字符串的颜色
     */
    private int textColor;

    /**
     * 中间进度百分比的字符串的字体
     */
    private float textSize;

    /**
     * 圆环的宽度
     */
    private float roundWidth;

    /**
     * 最大进度
     */
    private float max;

    /**
     * 当前进度
     */
    private float progress;

    /**
     * 是否显示中间的进度
     */
    private boolean showProgressText;

    /**
     * 进度的风格，实心或者空心
     */
    private int style;

    public static final int STROKE = 0;
    public static final int FILL = 1;

    public CircleProgressBar(Context context) {
        this(context, null);
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        paint = new Paint();
        if (attrs != null) {
            @SuppressLint("CustomViewStyleable") TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.fly_uikit_CircleProgressBar);
            // 获取自定义属性和默认值
            roundColor = mTypedArray.getColor(R.styleable.fly_uikit_CircleProgressBar_fu_color, Color.RED);
            roundProgressColor = mTypedArray.getColor(R.styleable.fly_uikit_CircleProgressBar_fu_progressColor, Color.GREEN);
            textColor = mTypedArray.getColor(R.styleable.fly_uikit_CircleProgressBar_fu_textColor, Color.GREEN);
            textSize = mTypedArray.getDimension(R.styleable.fly_uikit_CircleProgressBar_fu_textSize, 15);
            roundWidth = mTypedArray.getDimension(R.styleable.fly_uikit_CircleProgressBar_fu_width, 5);
            max = mTypedArray.getFloat(R.styleable.fly_uikit_CircleProgressBar_fu_max, 100);
            showProgressText = mTypedArray.getBoolean(R.styleable.fly_uikit_CircleProgressBar_fu_showProgressText, true);
            style = mTypedArray.getInt(R.styleable.fly_uikit_CircleProgressBar_fu_style, 0);
            mTypedArray.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         * 画最外层的大圆环
         */
        int centre = getWidth() / 2; //获取圆心的x坐标
        int radius = (int) (centre - roundWidth / 2); //圆环的半径
        paint.setColor(roundColor); //设置圆环的颜色
        paint.setStyle(Paint.Style.STROKE); //设置空心
        paint.setStrokeWidth(roundWidth); //设置圆环的宽度
        paint.setAntiAlias(true); //消除锯齿
        canvas.drawCircle(centre, centre, radius, paint); //画出圆环

        /**
         * 画进度百分比
         */
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(textColor);
        paint.setTextSize(textSize);
//        paint.setTypeface(Typeface.DEFAULT_BOLD); //设置字体
//        int percent = (int) (((float) progress / (float) max) * 100); //中间的进度百分比，先转换成float在进行除法运算，不然都为0
//        float textWidth = paint.measureText(percent + "%"); //测量字体宽度，我们需要根据字体的宽度设置在圆环中间
        BigDecimal p = new BigDecimal(progress + "");
        BigDecimal m = new BigDecimal(max + "");
        BigDecimal t = new BigDecimal("100");
        int percent = p.divide(m, 2, RoundingMode.DOWN).multiply(t).intValue();
        float textWidth = paint.measureText(percent + "%");
        if (showProgressText && style == STROKE) {
            canvas.drawText(percent + "%", centre - textWidth / 2, centre + textSize / 2 - 1, paint); //画出进度百分比
        }

        /**
         * 画圆弧 ，画圆环的进度
         */
        // 设置进度是实心还是空心
        paint.setStrokeWidth(roundWidth); //设置圆环的宽度
        paint.setColor(roundProgressColor); //设置进度的颜色
        RectF oval = new RectF(centre - radius, centre - radius, centre
                + radius, centre + radius); //用于定义的圆弧的形状和大小的界限

        BigDecimal cp = new BigDecimal(progress + "");
        BigDecimal cm = new BigDecimal(max + "");
        BigDecimal ct = new BigDecimal("360");
        BigDecimal sweepAngle = cp.divide(cm, 10, RoundingMode.DOWN).multiply(ct);
        switch (style) {
            case STROKE: {
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawArc(oval, -90, sweepAngle.floatValue(), false, paint); //根据进度画圆弧
                break;
            }
            case FILL: {
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                if (progress != 0)
                    canvas.drawArc(oval, -90, sweepAngle.floatValue(), true, paint); //根据进度画圆弧
                break;
            }
        }
    }

    public synchronized float getMax() {
        return max;
    }

    /**
     * 设置进度的最大值
     */
    public synchronized void setMax(float max) {
        if (max < 0) {
            throw new IllegalArgumentException("max not less than 0");
        }
        this.max = max;
    }

    /**
     * 获取进度.需要同步
     */
    public synchronized float getProgress() {
        return progress;
    }

    /**
     * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步
     * 刷新界面调用postInvalidate()能在非UI线程刷新
     */
    public synchronized void setProgress(float progress) {
        if (progress < 0) {
            throw new IllegalArgumentException("progress not less than 0");
        }
        if (progress > max) {
            progress = max;
        }
        if (progress <= max) {
            this.progress = progress;
            postInvalidate();
        }

    }

    public int getCircleColor() {
        return roundColor;
    }

    public void setCircleColor(int cricleColor) {
        this.roundColor = cricleColor;
    }

    public int getCircleProgressColor() {
        return roundProgressColor;
    }

    public void setCircleProgressColor(int cricleProgressColor) {
        this.roundProgressColor = cricleProgressColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public float getRoundWidth() {
        return roundWidth;
    }

    public void setRoundWidth(float roundWidth) {
        this.roundWidth = roundWidth;
    }
}

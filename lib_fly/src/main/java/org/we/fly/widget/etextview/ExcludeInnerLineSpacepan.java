package org.we.fly.widget.etextview;

import android.graphics.Paint;
import android.text.style.LineHeightSpan;

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/26 11:28 PM
 * @description: 去除TextView的自带行间距的类
 * @since: 1.0.0
 */
public class ExcludeInnerLineSpacepan implements LineHeightSpan {

    // TextView行高
    private int mHeight;

    public ExcludeInnerLineSpacepan(int height) {
        mHeight = height;
    }

    @Override
    public void chooseHeight(CharSequence text, int start, int end,
                             int spanstartv, int lineHeight,
                             Paint.FontMetricsInt fm) {
        // 原始行高
        final int originHeight = fm.descent - fm.ascent;
        if (originHeight <= 0) {
            return;
        }
        // 计算比例值
        final float ratio = mHeight * 1.0f / originHeight;
        // 根据最新行高，修改descent
        fm.descent = Math.round(fm.descent * ratio);
        // 根据最新行高，修改ascent
        fm.ascent = fm.descent - mHeight;
    }
}

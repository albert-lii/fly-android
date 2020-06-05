package org.we.fly.base.ui.recyclerview;

import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/5/30 10:09 PM
 * @description: ViewHolder的基类
 * @since: 1.0.0
 */
public class BaseFlyViewHolder extends RecyclerView.ViewHolder {
    private Map<Integer, View> mViews;

    public BaseFlyViewHolder(@NonNull View itemView) {
        super(itemView);
        mViews = new HashMap();
    }

    public <T extends View> T getView(@IdRes int resId) {
        View v = mViews.get(resId);
        if (v == null) {
            v = itemView.findViewById(resId);
            mViews.put(resId, v);
        }
        return (T) v;
    }
}

package org.we.fly.base.ui.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/5/30 9:20 PM
 * @description: RecyclerView Adapter封装
 * @since: 1.0.0
 */
public abstract class BaseFlyAdapter<T> extends RecyclerView.Adapter<BaseFlyViewHolder> {
    private List<T> mData;
    private OnItemClickListener mItemClickListener;

    @NonNull
    @Override
    public BaseFlyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(this.getLayoutId(viewType), parent, false);
        return new BaseFlyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final BaseFlyViewHolder holder, final int position) {
        this.onBindItem(holder, mData.get(position), position);
        if (mItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(holder.itemView, mData.get(position), position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    public abstract @LayoutRes
    int getLayoutId(int viewType);

    public abstract void onBindItem(@NonNull BaseFlyViewHolder holder, T item, int position);

    public void setData(List<T> data) {
        this.mData = data;
    }

    public List<T> getData() {
        return mData;
    }

    public void refreshData(List<T> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    public void refreshItem(int position, T item) {
        if (mData != null && mData.size() > position) {
            mData.set(position, item);
            notifyItemChanged(position);
        }
    }

    public void removeItem(int position) {
        if (mData != null && mData.size() > position) {
            mData.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        if (mData != null) {
            mData.clear();
            mData = null;
        }
    }

    public interface OnItemClickListener<T> {
        void onItemClick(View itemView, T item, int position);
    }
}

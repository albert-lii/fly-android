package org.we.fly.base.ui.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/5/30 10:25 PM
 * @description: 基于DataBinding的RecyclerView Adapter封装
 * @since: 1.0.0
 */
public abstract class BaseFlyBindingAdapter<T, B extends ViewDataBinding> extends RecyclerView.Adapter<BaseFlyViewHolder> {
    private List<T> mData;
    private OnItemClickListener mItemClickListener;

    @NonNull
    @Override
    public BaseFlyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        B binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), this.getLayoutId(viewType), parent, false);
        return new BaseFlyViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull BaseFlyViewHolder holder, final int position) {
        final B binding = DataBindingUtil.getBinding(holder.itemView);
        this.onBindItem(binding, mData.get(position), position);
        binding.executePendingBindings();
        if (mItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(binding, mData.get(position), position);
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

    public abstract void onBindItem(B binding, T item, int position);

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

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public interface OnItemClickListener<B, T> {
        void onItemClick(B binding, T item, int position);
    }
}

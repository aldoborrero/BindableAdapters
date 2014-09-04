package com.aldoborrero.bindableadapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * An implementation of {@link BaseAdapter} which uses the new/bind pattern and
 * view holder pattern for its views.
 * <p/>
 * Inspired by/based on Jake Wharton's BindingAdapter:
 * https://gist.github.com/JakeWharton/5423616
 * <p/>
 * With some modifications done by Patrick Hammond:
 * https://gist.github.com/patrickhammond/6094827
 *
 * @param <T>          The type of item being displayed.
 * @param <ViewHolder> The view holder type being used.
 */
public abstract class BindableAdapter<T, ViewHolder> extends BaseAdapter {

    private final Context context;
    private final LayoutInflater inflater;

    public BindableAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public Context getContext() {
        return context;
    }

    @Override
    public abstract T getItem(int position);

    @Override
    public long getItemId(int position) {
        return getItem(position) == null ? 0l : position;
    }

    @Override
    public final View getView(int position, View reusableView, ViewGroup container) {
        if (reusableView == null) {
            reusableView = newView(inflater, position, container);
            if (reusableView == null) {
                throw new NullPointerException("newView result must not be null!");
            }
            associateViewHolder(position, reusableView);
        }
        bindView(getItem(position), position, reusableView, getViewHolder(reusableView));
        return reusableView;
    }

    /**
     * Create a new instance of a view for the specified position.
     */
    public abstract View newView(LayoutInflater inflater, int position, ViewGroup container);

    /**
     * If your ViewHolder implementation looks something like this:
     * <pre>
     * {@code
     * static class ViewHolder {
     *     final TextView textView;
     *
     *     ViewHolder(View view) {
     *         textView = (TextView) view.findViewById(R.id.textView);
     *     }
     * }
     * </pre>
     * <p/>
     * This method only needs this as its implementation:
     * <pre>
     * {@code
     * return new ViewHolder(view);
     * }
     * </pre>
     * <p/>
     * If implementations do not need/want a view holder, just return <code>null</code>.
     */
    public abstract ViewHolder buildViewHolder(int position, View view);

    /**
     * Bind the data for the specified {@code position} to the view using a
     * {@code viewHolder} created from {@link #buildViewHolder(View)}.
     */
    public abstract void bindView(T item, int position, View view, ViewHolder viewHolder);

    @Override
    public final View getDropDownView(int position, View reusableView, ViewGroup container) {
        if (reusableView == null) {
            reusableView = newDropDownView(inflater, position, container);
            if (reusableView == null) {
                throw new NullPointerException("newDropDownView result must not be null!");
            }
            associateViewHolder(position, reusableView);
        }
        bindDropDownView(getItem(position), position, reusableView, getViewHolder(reusableView));
        return reusableView;
    }

    private void associateViewHolder(int position, View view) {
        ViewHolder viewHolder = buildViewHolder(position, view);
        view.setTag(viewHolder);
    }

    @SuppressWarnings("unchecked")
    private ViewHolder getViewHolder(View view) {
        return (ViewHolder) view.getTag();
    }

    /**
     * Create a new instance of a drop-down view for the specified position.
     */
    public View newDropDownView(LayoutInflater inflater, int position, ViewGroup container) {
        return newView(inflater, position, container);
    }

    /**
     * Bind the data for the specified {@code position} to the drop-down view.
     */
    public void bindDropDownView(T item, int position, View view, ViewHolder viewHolder) {
        bindView(item, position, view, viewHolder);
    }

}
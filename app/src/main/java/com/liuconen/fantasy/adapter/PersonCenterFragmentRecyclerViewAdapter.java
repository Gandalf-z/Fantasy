package com.liuconen.fantasy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liuconen.fantasy.R;
import com.liuconen.fantasy.model.OnRecyclerViewItemClickListener;

/**
 * Created by liuconen on 2016/9/7.
 */
public class PersonCenterFragmentRecyclerViewAdapter extends RecyclerView.Adapter<PersonCenterFragmentRecyclerViewAdapter
        .MyViewHolder> implements View.OnClickListener {
    private Context context;
    private int[] items;
    private OnRecyclerViewItemClickListener mOnItemClickListener;

    public PersonCenterFragmentRecyclerViewAdapter(Context context, int[] items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public PersonCenterFragmentRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.person_center_fragment_recycler_view_list_item,
                parent, false);
        view.setOnClickListener(this);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PersonCenterFragmentRecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.itemText.setText(items[position]);
        holder.itemView.setTag(items[position]);
    }

    @Override
    public int getItemCount() {
        return items.length;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemCLick(v, v.getTag());
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView itemText;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemText = (TextView) itemView.findViewById(R.id.tv_person_center_recycler_item_text);
        }
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}

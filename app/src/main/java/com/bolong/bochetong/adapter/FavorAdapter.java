package com.bolong.bochetong.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bolong.bochetong.activity.R;
import com.bolong.bochetong.bean2.Favor;

import java.util.List;

public class FavorAdapter extends RecyclerView.Adapter<FavorAdapter.ViewHolder>{

    private List<Favor.ContentBean> favors;

    public FavorAdapter(List<Favor.ContentBean> favors) {
        this.favors = favors;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_zssj, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv1.setText(favors.get(position).getTitle());
        holder.tv2.setText(favors.get(position).getReceiveTime());
        holder.tv3.setText(favors.get(position).getTotalMinute()+"");
        holder.tv4.setText(favors.get(position).getMinutes()+"");
    }

    @Override
    public int getItemCount() {
        return favors.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv1,tv2,tv3,tv4;
        public ViewHolder(View itemView) {
            super(itemView);
            tv1 = (TextView) itemView.findViewById(R.id.tv_name);
            tv2 = (TextView) itemView.findViewById(R.id.tv_time);
            tv3 = (TextView) itemView.findViewById(R.id.tv_time2);
            tv4 = (TextView) itemView.findViewById(R.id.tv_time3);
        }
    }
}

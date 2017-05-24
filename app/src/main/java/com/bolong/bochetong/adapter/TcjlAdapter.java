package com.bolong.bochetong.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bolong.bochetong.activity.R;
import com.bolong.bochetong.bean.RecordList;

import java.util.List;

/**
 * Created by admin on 2017/5/3.
 */

public class TcjlAdapter extends RecyclerView.Adapter<TcjlAdapter.ViewHolder> {

    private Context context;
    private List<RecordList.ContentBean.DateBean> parkRecords;

    public TcjlAdapter(Context context ,List<RecordList.ContentBean.DateBean> parkRecords) {
        this.context=context;
        this.parkRecords=parkRecords;

        Log.e("数据",parkRecords.size()+"");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_tcjl, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv1.setText(parkRecords.get(position).getRecordIntime());
        holder.tv2.setText(parkRecords.get(position).getParkName());
        holder.tv3.setText(parkRecords.get(position).getCarNumber());
        holder.tv4.setText(parkRecords.get(position).getDurationTime());
        holder.tv5.setText(parkRecords.get(position).getRecordPaymoney() + "");
        holder.tv6.setText(parkRecords.get(position).getOriginalPrice() + "");
        holder.tv7.setText(parkRecords.get(position).getPreferentialPrice() + "");
    }

    @Override
    public int getItemCount() {

        return parkRecords.size();


    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv1,tv2, tv3, tv4, tv5, tv6, tv7;

        public ViewHolder(View itemView) {
            super(itemView);

            tv1 = (TextView) itemView.findViewById(R.id.tv1);
            tv2 = (TextView) itemView.findViewById(R.id.tv2);
            tv3 = (TextView) itemView.findViewById(R.id.tv3);
            tv4 = (TextView) itemView.findViewById(R.id.tv4);
            tv5 = (TextView) itemView.findViewById(R.id.tv5);
            tv6 = (TextView) itemView.findViewById(R.id.tv6);
            tv7 = (TextView) itemView.findViewById(R.id.tv7);
        }
    }
}

package com.bolong.bochetong.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bolong.bochetong.activity.R;
import com.bolong.bochetong.bean.Bill;
import com.bolong.bochetong.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/5/3.
 */

public class ZdmxAdapter extends RecyclerView.Adapter<ZdmxAdapter.ViewHolder>{

    private List<Bill.PagenationBean.DateBean> bills;
    private Context context;
    public ZdmxAdapter(Context context,List<Bill.PagenationBean.DateBean> bills) {
        this.context = context;
        this.bills = bills;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_bill, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv1.setText(bills.get(position).getPayType());
        holder.tv2.setText(bills.get(position).getBillType());

        holder.tv3.setText(bills.get(position).getBillTime());
        holder.tv4.setText(bills.get(position).getBillMoney()+"元");
    }

    @Override
    public int getItemCount() {
        return bills.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv1,tv2,tv3,tv4;
        public ViewHolder(View itemView) {
            super(itemView);
            tv1 = (TextView) itemView.findViewById(R.id.tv1);
            tv2 = (TextView) itemView.findViewById(R.id.tv2);
            tv3 = (TextView) itemView.findViewById(R.id.tv3);
            tv4 = (TextView) itemView.findViewById(R.id.tv4);
        }

    }
}

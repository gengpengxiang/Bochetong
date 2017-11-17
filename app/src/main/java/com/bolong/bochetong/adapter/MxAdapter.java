package com.bolong.bochetong.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bolong.bochetong.activity.R;
import com.bolong.bochetong.bean2.Bill;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MxAdapter extends RecyclerView.Adapter<MxAdapter.ViewHolder>{

    private List<Bill.PagenationBean.DataBean> bills;

    public MxAdapter( List<Bill.PagenationBean.DataBean> bills) {
        this.bills = bills;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_newbill, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Bill.PagenationBean.DataBean newBill = bills.get(position);
        holder.tv2.setText(newBill.getPayType());
        holder.tv3.setText(newBill.getBillType());
        holder.tv4.setText(newBill.getCreateTime());
        holder.tv5.setText(newBill.getBillMoney());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd h:mm");
        try {
            Date date = dateFormat.parse(newBill.getCreateTime());
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int month = cal.get(Calendar.MONTH);

//            String s = newBill.getBillTime();
//            String symbol = s.substring(5,9);
//            int selection = Integer.parseInt(symbol);
            int selection = month;
            int positionForSelection = getPositionForSelection(selection);
            //add

            //add
            if (position == positionForSelection){
                holder.layout.setVisibility(View.VISIBLE);
                //holder.layout.setText(String.valueOf(month+1)+"月");
                holder.tv1.setText(newBill.getCreateTime().substring(0,7));
                //holder.tv2.setText("支出￥");
            }else {
                holder.layout.setVisibility(View.GONE);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return bills.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv1,tv2,tv3,tv4,tv5;
        LinearLayout layout;
        public ViewHolder(View itemView) {
            super(itemView);
            layout = (LinearLayout) itemView.findViewById(R.id.layout_date);
            tv1 = (TextView) itemView.findViewById(R.id.tv_date);
            tv2 = (TextView) itemView.findViewById(R.id.tv_payType);
            tv3 = (TextView) itemView.findViewById(R.id.tv_billType);
            tv4 = (TextView) itemView.findViewById(R.id.tv_billTime);
            tv5 = (TextView) itemView.findViewById(R.id.tv_billMoney);

        }
    }

    //add
    public int getPositionForSelection(int selection) {
        for (int i = 0; i < bills.size(); i++) {
            String dates = bills.get(i).getCreateTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd h:mm");

            try {
                Date date = dateFormat.parse(dates);
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                int month = cal.get(Calendar.MONTH);

                int first = month;
                //char first = date.charAt(6);
                if (first == selection) {
                    return i;
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        return -1;

    }
}

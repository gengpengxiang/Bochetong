package com.bolong.bochetong.adapter;

import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bolong.bochetong.activity.R;
import com.bolong.bochetong.bean2.Invoice;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2017/6/9.
 */

public class FpAdapter extends RecyclerView.Adapter<FpAdapter.ViewHolder>{

    private List<Invoice.ContentBean> invoices;

    public FpAdapter(List<Invoice.ContentBean> invoices) {
        this.invoices = invoices;
    }

    //点击监听
    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);

    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_fp, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final Invoice.ContentBean invoice = invoices.get(position);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        try {
            Date date = dateFormat.parse(invoice.getIntime());
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int month = cal.get(Calendar.MONTH);

            String s = invoice.getIntime();
            String symbol = s.substring(5,7);
            int selection = Integer.parseInt(symbol);

            int positionForSelection = getPositionForSelection(selection);

            if (position == positionForSelection){
//                holder.tv1.setVisibility(View.VISIBLE);
                holder.tv1.setVisibility(View.GONE);
                holder.tv1.setText(String.valueOf(month+1)+"月");
            }else {
                holder.tv1.setVisibility(View.GONE);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }


        holder.tv2.setText(invoice.getParkName());
        holder.tv3.setText(invoice.getIntime());
        holder.tv4.setText(invoice.getOuttime());
        holder.tv5.setText(invoice.getPrice()+"");

        if(invoice.isSelect()){
            holder.iv.setImageResource(R.mipmap.icon_select_blue);
        }else {
            holder.iv.setImageResource(R.mipmap.icon_select_gray);
        }
        //已开发票判断
//        if(invoice.hasKai){
//            holder.tv6.setVisibility(View.VISIBLE);
//            holder.iv.setVisibility(View.INVISIBLE);
//        }else {
//            holder.tv6.setVisibility(View.GONE);
//            holder.iv.setVisibility(View.VISIBLE);
//        }
        if (mOnItemClickLitener != null)
        {
//            if(!invoice.hasKai){
                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        int pos = holder.getLayoutPosition();
                        mOnItemClickLitener.onItemClick(holder.itemView, pos);

                        if(invoice.isSelect()){
                            holder.iv.setImageResource(R.mipmap.icon_select_blue);
                        }else {
                            holder.iv.setImageResource(R.mipmap.icon_select_gray);
                        }

                    }
                });
//            }
//            else {
//                return;
//            }

        }

    }

    @Override
    public int getItemCount() {
        return invoices.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{


        TextView tv1,tv2,tv3,tv4,tv5,tv6;
        ImageView iv;

        public ViewHolder(View itemView) {
            super(itemView);

            //checkBox = (AppCompatCheckBox)itemView.findViewById(R.id.checkBox);
            iv = (ImageView) itemView.findViewById(R.id.checkBox);

            tv1 = (TextView) itemView.findViewById(R.id.tv_month);
            tv2 = (TextView) itemView.findViewById(R.id.tv_address);
            tv3 = (TextView) itemView.findViewById(R.id.tv_entertime);
            tv4 = (TextView) itemView.findViewById(R.id.tv_outtime);
            tv5 = (TextView) itemView.findViewById(R.id.tv_price);

            tv6 = (TextView) itemView.findViewById(R.id.tv_hasPrint);
        }
    }

    //add
    public int getPositionForSelection(int selection) {
        for (int i = 0; i < invoices.size(); i++) {
            String date = invoices.get(i).getIntime();
            //add
            String s = date.substring(5,7);
            int first = Integer.parseInt(s);
            //char first = date.charAt(6);
            if (first == selection) {
                return i;
            }
        }
        return -1;

    }

//    public void notifyAdapter(List<Invoice> myLiveList,boolean isAdd){
//        if (!isAdd){
//            this.invoices=myLiveList;
//        }else {
//            this.invoices.addAll(myLiveList);
//        }
//        notifyDataSetChanged();
//    }
//
//    public List<Invoice> getMyLiveList(){
//        if (invoices == null)  {
//            invoices =new ArrayList<>();
//        }
//        return  invoices;
//    }

    public Invoice.ContentBean getItem(int pos){
        return invoices.get(pos);
    }
}

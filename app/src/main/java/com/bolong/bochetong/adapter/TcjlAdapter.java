package com.bolong.bochetong.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.bolong.bochetong.activity.R;
import com.bolong.bochetong.bean2.CarRecord;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TcjlAdapter extends RecyclerView.Adapter<TcjlAdapter.ViewHolder> {

    private List<CarRecord.DataBean> parkRecords;

    public TcjlAdapter(List<CarRecord.DataBean> parkRecords) {
        this.parkRecords=parkRecords;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_tcjl, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        StringBuffer stringBuffer = new StringBuffer(parkRecords.get(position).getCarNumber());
        holder.tv2.setText(parkRecords.get(position).getParkName());
        holder.tv3.setText(stringBuffer.insert(2,"-"));
        holder.tv4.setText(parkRecords.get(position).getDurationTime());
        holder.tv5.setText(" ¥ "+parkRecords.get(position).getRecordPaymoney() + "");

        //holder.tv6.setText("进场时间："+parkRecords.get(position).getRecordIntime());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd h:mm");
        try {
            Date date = dateFormat.parse(parkRecords.get(position).getRecordIntime());
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int month = cal.get(Calendar.MONTH);

            int selection = month;
            int positionForSelection = getPositionForSelection(selection);

            holder.tv1.setText("进场时间："+parkRecords.get(position).getRecordIntime());
            /*if (position == positionForSelection){
                holder.tv1.setVisibility(View.VISIBLE);
                //holder.layout.setText(String.valueOf(month+1)+"月");

                String inTime = parkRecords.get(position).getRecordIntime();
                String s = inTime.substring(0,10);

                holder.tv1.setText("进场时间："+parkRecords.get(position).getRecordIntime());
                //holder.tv1.setText(parkRecords.get(position).getRecordIntime());

            }else {
                holder.tv1.setVisibility(View.GONE);
            }*/

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return parkRecords.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv1,tv2, tv3, tv4, tv5;

        public ViewHolder(View itemView) {
            super(itemView);
            tv1 = (TextView) itemView.findViewById(R.id.tv1);
            tv2 = (TextView) itemView.findViewById(R.id.tv_address);
            tv3 = (TextView) itemView.findViewById(R.id.tv_carplate);
            tv4 = (TextView) itemView.findViewById(R.id.tv_time);
            tv5 = (TextView) itemView.findViewById(R.id.tv_price);
            //tv6 = (TextView) itemView.findViewById(R.id.tv_date);
//            tv7 = (TextView) itemView.findViewById(R.id.tv7);

        }
    }

    public int getPositionForSelection(int selection) {
        for (int i = 0; i < parkRecords.size(); i++) {
            String dates = parkRecords.get(i).getRecordIntime();
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

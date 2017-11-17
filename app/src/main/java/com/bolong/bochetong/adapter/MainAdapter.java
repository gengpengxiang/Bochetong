package com.bolong.bochetong.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.bolong.bochetong.activity.R;
import com.bolong.bochetong.bean2.NearbyPark;
import java.util.List;


public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<NearbyPark.ZonesBean> zones;
    //社区停车
    List<NearbyPark.ParksBean> parks;

    private NaviClickListener mListener;

    public MainAdapter(List<NearbyPark.ZonesBean> zones, List<NearbyPark.ParksBean> parks, NaviClickListener listener) {
        this.zones = zones;
        this.parks = parks;
        this.mListener = listener;
    }


    //点击监听
    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

    }

    private BcAdapter.OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(BcAdapter.OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public enum ITEM_TYPE {
        ITEM_TYPE_ONE,
        ITEM_TYPE_TWO,
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.ITEM_TYPE_ONE.ordinal()) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_park_item_one, parent, false);

            return new ViewHolderOne(view);

        } else if (viewType == ITEM_TYPE.ITEM_TYPE_TWO.ordinal()) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_park_item_two, parent, false);
            return new ViewHolderTwo(view);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        try {

        if (zones.size()!=0 && parks.size()!=0) {


        if (holder instanceof ViewHolderOne) {
            ((ViewHolderOne) holder).tv0.setText("路内停车");
            ((ViewHolderOne) holder).tv1.setText(zones.get(position).getPark_name());
            //((ViewHolderOne) holder).tv2.setText(zones.get(position).getDistance()+"km");
            ((ViewHolderOne) holder).tv3.setText(zones.get(position).getPark_address());
            ((ViewHolderOne) holder).tv4.setText(zones.get(position).getEmptyPosition() + "");

            ((ViewHolderOne) holder).tv5.setText(zones.get(position).getStandard().replace("小时","h").replace("分钟","min"));

            ((ViewHolderOne) holder).tv6.setText(zones.get(position).getDistance()+"km");
            ((ViewHolderOne) holder).tv6.setTag(position);
            ((ViewHolderOne) holder).tv6.setOnClickListener(mListener);

        } else if (holder instanceof ViewHolderTwo) {
            if (position == 1) {
                ((ViewHolderTwo) holder).tv0.setVisibility(View.VISIBLE);
            } else {
                ((ViewHolderTwo) holder).tv0.setVisibility(View.GONE);
            }
            ((ViewHolderTwo) holder).tv0.setText("路外停车");
            ((ViewHolderTwo) holder).tv1.setText(parks.get(position - 1).getPark_name());
            //((ViewHolderTwo) holder).tv2.setText(parks.get(position - 1).getDistance()+"km");
            //地址
            String address = parks.get(position - 1).getPark_address();

            if ((address.contains("北京市")||address.contains("门头沟区"))&&(address.length()>14)) {
                String str = address.replace("北京市", "");
                str = str.replace("门头沟区","");
                ((ViewHolderTwo) holder).tv3.setText(str);
            } else {
                ((ViewHolderTwo) holder).tv3.setText(parks.get(position - 1).getPark_address()+"");
            }
            ((ViewHolderTwo) holder).tv4.setText(parks.get(position - 1).getEmptyPosition() + "");

            //Log.e("测试",parks.get(position - 1).getStandard());

                if (parks.get(position - 1).getStandard().contains("小时")) {
                    ((ViewHolderTwo) holder).tv5.setText(parks.get(position - 1).getStandard().replace("小时", "h") + "");
                }
                if (parks.get(position - 1).getStandard().contains("分钟")) {
                    ((ViewHolderTwo) holder).tv5.setText(parks.get(position - 1).getStandard().replace("分钟", "min") + "");
                }


            //((ViewHolderTwo) holder).tv5.setText(parks.get(position - 1).getStandard() + "");


            ((ViewHolderTwo) holder).tv6.setText(parks.get(position-1).getDistance()+"km");
            ((ViewHolderTwo) holder).tv6.setTag(position);
            ((ViewHolderTwo) holder).tv6.setOnClickListener(mListener);
        }

        }
        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                    //Log.e("item","item");
                }
            });

        }
        if(zones.size()==0){
            if (holder instanceof ViewHolderTwo){
                if (position == 0) {
                    ((ViewHolderTwo) holder).tv0.setVisibility(View.VISIBLE);
                } else {
                    ((ViewHolderTwo) holder).tv0.setVisibility(View.GONE);
                }
                ((ViewHolderTwo) holder).tv0.setText("路外停车");
                ((ViewHolderTwo) holder).tv1.setText(parks.get(position ).getPark_name()+"");
                //((ViewHolderTwo) holder).tv2.setText(parks.get(position - 1).getDistance()+"km");
                //地址
                String address = parks.get(position ).getPark_address();

                if ((address.contains("北京市")||address.contains("门头沟区"))&&(address.length()>14)) {
                    String str = address.replace("北京市", "");
                    str = str.replace("门头沟区","");
                    ((ViewHolderTwo) holder).tv3.setText(str);
                } else {
                    ((ViewHolderTwo) holder).tv3.setText(parks.get(position).getPark_address()+"");
                }
                ((ViewHolderTwo) holder).tv4.setText(parks.get(position).getEmptyPosition() + "");
                ((ViewHolderTwo) holder).tv5.setText(parks.get(position).getStandard().replace("小时","h").replace("分钟","min")+"");

                ((ViewHolderTwo) holder).tv6.setText(parks.get(position).getDistance()+"km");
                ((ViewHolderTwo) holder).tv6.setTag(position);
                ((ViewHolderTwo) holder).tv6.setOnClickListener(mListener);
            }
        }

        }catch (Exception e){
            Log.e("异常","exception");
        }
    }

    @Override
    public int getItemCount() {

        return parks.size() + zones.size();
    }

    @Override
    public int getItemViewType(int position) {

        if(zones.size()!=0){
            if (position == 0) {
                return ITEM_TYPE.ITEM_TYPE_ONE.ordinal();
            } else {
                return ITEM_TYPE.ITEM_TYPE_TWO.ordinal();
            }
        }else {
            return ITEM_TYPE.ITEM_TYPE_TWO.ordinal();
        }

    }

    public class ViewHolderOne extends RecyclerView.ViewHolder {

        TextView tv0, tv1, tv2, tv3, tv4, tv5, tv6;

        public ViewHolderOne(View itemView) {
            super(itemView);
            tv0 = (TextView) itemView.findViewById(R.id.tv0);
            tv1 = (TextView) itemView.findViewById(R.id.tv_name);
            //tv2 = (TextView) itemView.findViewById(R.id.tv_distance);
            tv3 = (TextView) itemView.findViewById(R.id.tv_address);
            tv4 = (TextView) itemView.findViewById(R.id.tv_empty2);
            tv5 = (TextView) itemView.findViewById(R.id.tv_price2);
            tv6 = (TextView) itemView.findViewById(R.id.bt_navi);
        }
    }

    public class ViewHolderTwo extends RecyclerView.ViewHolder {
        TextView tv0, tv1, tv2, tv3, tv4, tv5, tv6;

        public ViewHolderTwo(View itemView) {
            super(itemView);
            tv0 = (TextView) itemView.findViewById(R.id.tv0);
            tv1 = (TextView) itemView.findViewById(R.id.tv_name);
            tv2 = (TextView) itemView.findViewById(R.id.tv_distance);
            tv3 = (TextView) itemView.findViewById(R.id.tv_address);
            tv4 = (TextView) itemView.findViewById(R.id.tv_empty2);
            tv5 = (TextView) itemView.findViewById(R.id.tv_price2);
            tv6 = (TextView) itemView.findViewById(R.id.bt_navi);
        }
    }

    public static abstract class NaviClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            myOnClick((Integer) v.getTag(), v);
        }

        public abstract void myOnClick(int position, View v);
    }
}

package com.bolong.bochetong.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bolong.bochetong.activity.R;
import com.bolong.bochetong.bean2.Zone;

import java.util.List;

/**
 * Created by admin on 2017/6/7.
 */

public class LntcAdapter extends RecyclerView.Adapter<LntcAdapter.ViewHolder> {

    private List<Zone.DataBean> parks;
    private NaviClickListener mListener;

    public LntcAdapter(List<Zone.DataBean> parks, NaviClickListener listener) {
        this.parks = parks;
        this.mListener = listener;
    }

    //点击监听
    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);

    }

    private LntcAdapter.OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(LntcAdapter.OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public LntcAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_park_item, parent, false);

        LntcAdapter.ViewHolder vh = new LntcAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final LntcAdapter.ViewHolder holder, int position) {
        try {
            if (position == 0) {
                holder.iv1.setVisibility(View.VISIBLE);
            } else {
                holder.iv1.setVisibility(View.INVISIBLE);
            }

            holder.tv1.setText(parks.get(position).getPark_name());
            holder.tv2.setText(parks.get(position).getDistance() + "km");
            holder.tv3.setText(parks.get(position).getPark_address());
            holder.tv4.setText(parks.get(position).getEmptyPosition() + "");
            holder.tv5.setText(parks.get(position).getStandard().replace("小时", "h").replace("分钟", "min"));
            holder.iv2.setTag(position);
            holder.iv2.setOnClickListener(mListener);

            if (mOnItemClickLitener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getLayoutPosition();
                        mOnItemClickLitener.onItemClick(holder.itemView, pos);
                    }
                });
            }
        }catch (Exception e){

        }
    }

    @Override
    public int getItemCount() {
        return parks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv1, tv2, tv3, tv4, tv5;
        ImageView iv1,iv2;
        public ViewHolder(View itemView) {
            super(itemView);
            tv1 = (TextView) itemView.findViewById(R.id.tv_name);
            tv2 = (TextView) itemView.findViewById(R.id.tv_distance);
            tv3 = (TextView) itemView.findViewById(R.id.tv_address);
            tv4 = (TextView) itemView.findViewById(R.id.tv_empty2);
            tv5 = (TextView) itemView.findViewById(R.id.tv_price2);
            iv1 = (ImageView) itemView.findViewById(R.id.iv_recommend);
            iv2 = (ImageView) itemView.findViewById(R.id.bt_navi);

        }

    }

    public static abstract class NaviClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            myOnClick((Integer) v.getTag(),v);
        }

        public abstract void myOnClick(int position, View v);
    }
}

package com.bolong.bochetong.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bolong.bochetong.activity.R;
import com.bolong.bochetong.bean.Bill;
import com.bolong.bochetong.bean.Park;
import com.bolong.bochetong.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/5/3.
 */

public class BcAdapter extends RecyclerView.Adapter<BcAdapter.ViewHolder> {

    private List<Park.ContentBean.DateBean> parks;

    public BcAdapter(List<Park.ContentBean.DateBean> parks) {
        this.parks = parks;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_bc, parent, false);

        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.tv1.setText(parks.get(position).getParkName());
        holder.tv2.setText(parks.get(position).getParkDistance());
        holder.tv3.setText(parks.get(position).getParkAddress());
        holder.tv4.setText(parks.get(position).getEmptyPosition());
        holder.tv5.setText(parks.get(position).getParkedPeopleNum());

        if (mOnItemClickLitener != null)
        {
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });


        }
    }

    @Override
    public int getItemCount() {
        return parks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv1, tv2, tv3, tv4, tv5;

        public ViewHolder(View itemView) {
            super(itemView);
            tv1 = (TextView) itemView.findViewById(R.id.tv1);
            tv2 = (TextView) itemView.findViewById(R.id.tv2);
            tv3 = (TextView) itemView.findViewById(R.id.tv3);
            tv4 = (TextView) itemView.findViewById(R.id.tv4);
            tv5 = (TextView) itemView.findViewById(R.id.tv5);
        }

    }
}

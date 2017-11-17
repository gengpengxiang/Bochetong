package com.bolong.bochetong.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bolong.bochetong.activity.R;
import com.bolong.bochetong.bean2.FpHistory;

import java.util.List;

/**
 * Created by admin on 2017/6/26.
 */

public class KpHistoryAdapter extends RecyclerView.Adapter<KpHistoryAdapter.ViewHolder>{

    private List<FpHistory.ContentBean> historys;

    public KpHistoryAdapter(List<FpHistory.ContentBean> historys) {
        this.historys = historys;
    }

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_kpls, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.tv1.setText(historys.get(position).getBuildDate());
        holder.tv2.setText(historys.get(position).getAmount()+"");
        holder.tv3.setText(historys.get(position).getMaterielName());

        if(historys.get(position).getOpenStatus()==1){
            holder.tv4.setText("已开票");
        }
        if(historys.get(position).getOpenStatus()==0){
            holder.tv4.setText("未开票");
        }


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
        return historys.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv1,tv2,tv3,tv4;
        public ViewHolder(View itemView) {
            super(itemView);
            tv1 = (TextView) itemView.findViewById(R.id.tv_time);
            tv2 = (TextView) itemView.findViewById(R.id.tv_money);
            tv3 = (TextView) itemView.findViewById(R.id.tv_type);
            tv4 = (TextView) itemView.findViewById(R.id.bt_ykp);
        }
    }
}

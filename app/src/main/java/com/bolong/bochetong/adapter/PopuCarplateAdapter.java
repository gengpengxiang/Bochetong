package com.bolong.bochetong.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bolong.bochetong.activity.R;
import com.bolong.bochetong.bean2.CarCard;

import java.util.List;

public class PopuCarplateAdapter extends RecyclerView.Adapter<PopuCarplateAdapter.ViewHolder> {


    private Context context;
    private List<CarCard.ContentBean> carPlates;

    public PopuCarplateAdapter( List<CarCard.ContentBean> carPlates) {

        this.carPlates = carPlates;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_popu_city_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tv.setText(carPlates.get(position).getCarCard());

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
        return carPlates.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv;

        public ViewHolder(View itemView) {
            super(itemView);

            tv = (TextView) itemView.findViewById(R.id.tv_cityName);

        }
    }

}

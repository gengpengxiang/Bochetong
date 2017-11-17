package com.bolong.bochetong.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bolong.bochetong.activity.R;
import com.bolong.bochetong.bean2.City;

import java.util.List;

/**
 * Created by zhouwei on 16/11/30.
 */

public class PopuCityAdapter extends RecyclerView.Adapter<PopuCityAdapter.ViewHolder>{
    private List<City.ContentBean> citys;

    public PopuCityAdapter(List<City.ContentBean> citys) {
        this.citys = citys;
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

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.tv.setText(citys.get(position).getCityName());

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
        return citys.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv;
        public ViewHolder(View itemView) {
            super(itemView);

            tv = (TextView) itemView.findViewById(R.id.tv_cityName);
        }
    }
}

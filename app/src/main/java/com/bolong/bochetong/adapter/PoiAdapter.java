package com.bolong.bochetong.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.baidu.mapapi.search.core.PoiInfo;
import com.bolong.bochetong.activity.R;
import java.util.List;

public class PoiAdapter extends RecyclerView.Adapter<PoiAdapter.ViewHolder> {

    private List<PoiInfo> poiInfos;

    public PoiAdapter(List<PoiInfo> poiInfos) {
        this.poiInfos = poiInfos;
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_poi, parent, false);

        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.tv1.setText(poiInfos.get(position).name);
        holder.tv2.setText(poiInfos.get(position).address);

        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });


        }
    }

    @Override
    public int getItemCount() {
        return poiInfos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv1, tv2;

        public ViewHolder(View itemView) {
            super(itemView);
            tv1 = (TextView) itemView.findViewById(R.id.tv_name);
            tv2 = (TextView) itemView.findViewById(R.id.tv_address);
        }
    }
}

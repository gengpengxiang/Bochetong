package com.bolong.bochetong.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bolong.bochetong.activity.R;
import com.bolong.bochetong.bean.CarPlate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/4/28.
 */

public class BdcpAdapter extends RecyclerView.Adapter<BdcpAdapter.ViewHolder> {

    private ButtonInterface buttonInterface;
    private Context context;
    private List<CarPlate> carPlates;

    public BdcpAdapter(Context context,List<CarPlate> carPlates) {
        this.context = context;
        this.carPlates = carPlates;
    }


    //点击监听
//    public interface OnItemClickLitener {
//        void onItemClick(View view, int position);
//
//        void onItemLongClick(View view, int position);
//    }
//
//    private OnItemClickLitener mOnItemClickLitener;
//
//    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
//        this.mOnItemClickLitener = mOnItemClickLitener;
//    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_carplate, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tv.setText(carPlates.get(position).getCarPlate());

        // 如果设置了回调，则设置点击事件
//        if (mOnItemClickLitener != null) {
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int pos = holder.getLayoutPosition();
//                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
//                }
//            });
//
//            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    int pos = holder.getLayoutPosition();
//                    mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
//                    return false;
//                }
//            });
//        }

        holder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buttonInterface!=null) {
//                  接口实例化后的而对象，调用重写后的方法
                    buttonInterface.onclick(v,position);
                }
            }
        });

    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return carPlates.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv;

        public ViewHolder(View itemView) {
            super(itemView);

            tv = (TextView) itemView.findViewById(R.id.tv);

        }
    }

    //    public void addData(int position,Bean s) {
//        user.add(position, s);
//        notifyItemInserted(position);
//    }

    public void removeData(int position) {
//        carPlates.remove(position);
//        notifyItemRemoved(position);
//        notifyItemRangeRemoved(position,carPlates.size());
        notifyItemRemoved(position);
        notifyItemRangeRemoved(position,carPlates.size());
        carPlates.remove(position);
    }

    /**
     *按钮点击事件需要的方法
     */
    public void buttonSetOnclick(ButtonInterface buttonInterface){
        this.buttonInterface=buttonInterface;
    }

    /**
     * 按钮点击事件对应的接口
     */
    public interface ButtonInterface{
        public void onclick( View view,int position);
    }
}

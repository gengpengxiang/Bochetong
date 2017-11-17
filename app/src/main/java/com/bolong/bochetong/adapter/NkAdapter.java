package com.bolong.bochetong.adapter;

import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bolong.bochetong.activity.NkActivity;
import com.bolong.bochetong.activity.R;
import com.bolong.bochetong.bean2.MsgEvent;
import com.bolong.bochetong.bean2.YearCard;
import com.bolong.bochetong.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import static com.bolong.bochetong.activity.NkActivity.ACTION_ADDCARDCARPLATE;

public class NkAdapter extends RecyclerView.Adapter<NkAdapter.ViewHolder> {

    private List<YearCard.MonthCardBean> list;

    public static final int ACTION_CARPLATE = 611;
    public static final int ACTION_ADDCAR = 612;

    private YearCard.MonthCardBean monthCard;

    public interface OnItemClickLitener {
        void onItemClick(View view, int position, int n);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public NkAdapter(List<YearCard.MonthCardBean> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_nk, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        monthCard = list.get(position);
        holder.tv1.setText(monthCard.getParkName());
        holder.tv2.setText(monthCard.getUserPhoneNumber());
        holder.tv5.setText(monthCard.getCarportNum());
        holder.tv6.setText(monthCard.getCreateTime());
        holder.tv7.setText(monthCard.getCardTermofvalidity());
        NkCarAdapter adapter = new NkCarAdapter(monthCard.getCarNumbers());
        holder.lv.setAdapter(adapter);
        adapter.setListViewHeight(holder.lv);
        holder.lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position2, long id) {
                EventBus.getDefault().post(new MsgEvent(ACTION_CARPLATE,list.get(position).getCardId(),list.get(position).getCarNumbers().get(position2),list.get(position).getCarNumbers().size()));
            }
        });

       /* holder.btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new MsgEvent(ACTION_ADDCAR, list.get(position).getCardId(),monthCard.getCarNumbers().size(),position));
            }
        });*/

        if (mOnItemClickLitener != null) {
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.layout, pos, 3);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv1, tv2, tv4, tv5, tv6, tv7, tvPhoneNumber;
        //EditText et1;
        RelativeLayout layout;
        LinearLayout menuNumber;
        ListView lv;
        ImageView btAdd;

        public ViewHolder(View itemView) {
            super(itemView);
            tv1 = (TextView) itemView.findViewById(R.id.tv_parkName);
             tv2= (TextView) itemView.findViewById(R.id.et_phoneNumber);
            //tv3 = (TextView) itemView.findViewById(R.id.tv_carport);
            tv4 = (TextView) itemView.findViewById(R.id.tv_carplate);
            tv5 = (TextView) itemView.findViewById(R.id.tv_carPort);
            tv6 = (TextView) itemView.findViewById(R.id.tv_startTime);
            tv7 = (TextView) itemView.findViewById(R.id.tv_stopTime);
            layout = (RelativeLayout) itemView.findViewById(R.id.layout_xf);

            tvPhoneNumber = (TextView) itemView.findViewById(R.id.tv_phoneNumber);
            lv = (ListView) itemView.findViewById(R.id.listView);
            btAdd = (ImageView) itemView.findViewById(R.id.bt_add);
        }
    }

    class NkCarAdapter extends BaseAdapter {

        private List<String> newList;

        public NkCarAdapter(List<String> newList) {
            this.newList = newList;
        }

        @Override
        public int getCount() {
            return newList.size();
        }

        @Override
        public Object getItem(int position) {
            return newList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder2 holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_nkcarplate, parent, false);
                holder = new ViewHolder2();
                holder.tv = (TextView) convertView.findViewById(R.id.tv_carPlate);
                holder.layout = (LinearLayout) convertView.findViewById(R.id.layout_menu_carplate);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder2) convertView.getTag();
            }
            holder.tv.setText(newList.get(position));

            return convertView;
        }

        class ViewHolder2 {
            TextView tv;
            LinearLayout layout;
        }

        public void setListViewHeight(ListView listView) {
            NkCarAdapter adapter = (NkCarAdapter) listView.getAdapter();
            if(adapter == null){
                return;
            }
            int totalHeight = 0;
            for (int i = 0; i < adapter.getCount(); i++) {
                View listItem = adapter.getView(i, null, listView);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
            listView.setLayoutParams(params);
        }
    }

}

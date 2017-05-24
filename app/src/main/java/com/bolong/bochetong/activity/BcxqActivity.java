package com.bolong.bochetong.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.bolong.bochetong.bean.Park;
import com.bolong.bochetong.utils.NaviUtils;
import com.bumptech.glide.Glide;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.bolong.bochetong.activity.LocationActivity.ROUTE_PLAN_NODE;

public class BcxqActivity extends BaseActivity {

    private Unbinder unbinder;
    @BindView(R.id.iv_parkphoto)
    ImageView ivParkphoto;
    @BindView(R.id.tvparkName)
    TextView tvparkName;
    @BindView(R.id.tv_zong)
    TextView tvZong;
    @BindView(R.id.tv_kong)
    TextView tvKong;
    @BindView(R.id.tv_distance)
    TextView tvDistance;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.iv_navi)
    ImageView ivNavi;

    private double startLongitude;
    private double startLatitude;
    private double stopLongitude;
    private double stopLatitude;

    private static final String ACTION = "com.bolong.bochetong.activity.STARTNAVI";

    private String status = "1";

    @Override
    public void onBaseCreate(Bundle bundle) {

        setContentViewId(R.layout.activity_bcxq2);

        unbinder = ButterKnife.bind(this);
        Intent intent = this.getIntent();
        startLongitude = intent.getDoubleExtra("startLongitude", 0);
        startLatitude = intent.getDoubleExtra("startLongitude", 0);
        Park.ContentBean.DateBean park = (Park.ContentBean.DateBean) intent.getSerializableExtra("parkInfo");
        if (park != null) {
            tvparkName.setText(park.getParkName());
            tvZong.setText(park.getParkMaxnum() + "车位");
            tvKong.setText(park.getEmptyPosition() + "车位");
            tvAddress.setText(park.getParkAddress());
            tvDistance.setText(park.getParkDistance());

            Glide.with(getApplicationContext()).load(park.getParkPhoto()).centerCrop().into(ivParkphoto);
            //加载图片失败时
            Glide.with(getApplicationContext()).load(park.getParkPhoto()).error(R.mipmap.bg_bcxq_nophoto).into(ivParkphoto);

            stopLongitude = Double.valueOf(park.getPartLongitude());
            stopLatitude = Double.valueOf(park.getPartLatitude());
        }

        activityList.add(BcxqActivity.this);

        //BNOuterLogUtil.setLogSwitcher(true);
//        if (NaviUtils.initDirs()) {
//            NaviUtils.initNavi(BcxqActivity.this);
//        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                /**
                 *要执行的操作
                 */
                if (NaviUtils.initDirs()) {
                    NaviUtils.initNavi(BcxqActivity.this);
                    ivNavi.setVisibility(View.VISIBLE);

                    Animation alpha= AnimationUtils.loadAnimation(BcxqActivity.this,R.anim.anim_navi);
                    ivNavi.startAnimation(alpha);

                    Log.e("初始化成功","success");
                }
            }
        }, 500);//3秒后执行Runnable中的run方法

    }

    @Override
    public void initView() {
        setTitle("详情");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }


    @OnClick(R.id.iv_navi)
    public void onViewClicked() {

        BNRoutePlanNode.CoordinateType coType = BNRoutePlanNode.CoordinateType.BD09LL;
        BNRoutePlanNode sNode = null;
        sNode = new BNRoutePlanNode(startLongitude, stopLatitude, "起点", null, coType);
        NaviUtils.routeplanToNavi(BcxqActivity.this, startLongitude, stopLatitude, stopLongitude, stopLatitude,
                new BcxqActivity.DemoRoutePlanListener(sNode));


    }

    public static List<Activity> activityList = new LinkedList<Activity>();

    public class DemoRoutePlanListener implements BaiduNaviManager.RoutePlanListener {

        private BNRoutePlanNode mBNRoutePlanNode = null;

        public DemoRoutePlanListener(BNRoutePlanNode node) {
            mBNRoutePlanNode = node;
        }

        @Override
        public void onJumpToNavigator() {
            /*
             * 设置途径点以及resetEndNode会回调该接口
             */
            for (Activity ac : activityList) {
                if (ac.getClass().getName().endsWith("BNDemoGuideActivity")) {
                    return;
                }
            }
            Intent intent = new Intent(BcxqActivity.this, BNDemoGuideActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ROUTE_PLAN_NODE, (BNRoutePlanNode) mBNRoutePlanNode);
            intent.putExtras(bundle);

            startActivity(intent);
        }

        @Override
        public void onRoutePlanFailed() {
            // TODO Auto-generated method stub
            Toast.makeText(BcxqActivity.this, "算路失败", Toast.LENGTH_SHORT).show();
        }
    }


}

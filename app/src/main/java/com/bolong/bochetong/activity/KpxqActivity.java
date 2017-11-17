package com.bolong.bochetong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bolong.bochetong.bean.User;
import com.bolong.bochetong.bean2.FpHistory;
import com.bolong.bochetong.bean2.InvoiceDetail;
import com.bolong.bochetong.bean2.MsgEvent;
import com.bolong.bochetong.utils.HttpUtils;
import com.bolong.bochetong.utils.Param;
import com.bolong.bochetong.utils.SharedPreferenceUtil;
import com.bolong.bochetong.view.CustomPopDialog;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.gson.Gson;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class KpxqActivity extends BaseActivity {

    @BindView(R.id.layout_header)
    RelativeLayout layoutHeader;
    @BindView(R.id.bt_download)
    Button btDownload;
    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_fpxx)
    TextView tvFpxx;
    @BindView(R.id.tv_fptt)
    TextView tvFptt;
    @BindView(R.id.tv_fpnr)
    TextView tvFpnr;
    @BindView(R.id.tv_fpje)
    TextView tvFpje;
    @BindView(R.id.tv_sbh)
    TextView tvSbh;
    @BindView(R.id.tv_kjsj)
    TextView tvKjsj;
    @BindView(R.id.tv_bz)
    TextView tvBz;
    @BindView(R.id.activity_kpxq)
    RelativeLayout activityKpxq;
    private Unbinder unbind;
    private CustomPopDialog dialog;
    private CustomPopDialog.Builder dialogBuild;
    private PDFView mPdfView;
    private File file;
    private String status;
    public static final int ACTION_GETINVOICEDETAIL = 1001;
    public static final int ACTION_DOWNCOMPLETED = 1002;
    private String pdfUrl;
    private FpHistory.ContentBean invoiceBean;

    @Override
    public void onBaseCreate(Bundle bundle) {
        setContentViewId(R.layout.activity_kpxq);
        unbind = ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        Intent intent = this.getIntent();
        invoiceBean = (FpHistory.ContentBean) intent.getSerializableExtra("invoiceInfo");

        dialogBuild = new CustomPopDialog.Builder(this);
        dialog = dialogBuild.create(R.layout.layout_dialog_picture, 0.4, 1.0);
        dialog.setCanceledOnTouchOutside(true);
        mPdfView = (PDFView) dialog.findViewById(R.id.mPdfView);
       // getInvoice();

        showInvoiceInfo();
    }
    /**
     * 显示发票信息
     */
    private void showInvoiceInfo() {
        activityKpxq.setVisibility(View.VISIBLE);
        tvTime.setText(invoiceBean.getBuildDate());
        tvFptt.setText("发票抬头: " + invoiceBean.getBuyerName());
        tvFpnr.setText("发票内容: " + invoiceBean.getMaterielName());
        tvFpje.setText("发票金额: " + invoiceBean.getAmount() + "");
        if (invoiceBean.getBuyerTaxCode() != null) {
            tvSbh.setText("纳税人识别号: " + invoiceBean.getBuyerTaxCode());
        } else {
            tvSbh.setText("纳税人识别号: " + "无");
        }
        tvKjsj.setText("开具时间: " + invoiceBean.getBuildDate());
//        if (invoiceBean.getRemarks() != null) {
//            tvBz.setText("备注: " + invoiceBean.getRemarks());
//        } else {
//            tvBz.setText("备注: " + "无");
//        }
        pdfUrl = invoiceBean.getPdfFileUrl();
    }

    /**
     * 获取发票信息
     */
    private void getInvoice() {
        String uid = null;
        String token = null;
        if (SharedPreferenceUtil.getBean(getApplicationContext(), "userInfo") != null) {
            User user = (User) SharedPreferenceUtil.getBean(getApplicationContext(), "userInfo");
            uid = user.getUserId();
            token = user.getToken();
        } else {
            uid = Param.UID;
            token = Param.TOKEN;
        }
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("token", token);
//        map.put("invoiceId", invoiceId);
//        Log.e("invoiceId",invoiceId+"");
        HttpUtils.post(Param.GETINVOICE, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setContentViewId(R.layout.layout_nonet);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonDatas = response.body().string();
                Log.e("发票信息", jsonDatas);
                try {
                    try {
                        JSONObject jsonObject = new JSONObject(jsonDatas);
                        String content = jsonObject.optString("content");
                        Gson gson = new Gson();
                        InvoiceDetail invoiceDetail = gson.fromJson(content, InvoiceDetail.class);
                        status = invoiceDetail.getStatus();
                        //invoiceBean = invoiceDetail.getInvoice();

                        EventBus.getDefault().post(new MsgEvent(ACTION_GETINVOICEDETAIL, status));

                    } catch (JSONException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setContentViewId(R.layout.layout_noinfo);
                            }
                        });
                    }
                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setContentViewId(R.layout.layout_noinfo);
                        }
                    });
                }
            }
        }, map);
    }

    @Override
    public void initView() {
        setTitle("开票详情");
    }

    @OnClick({R.id.layout_header, R.id.bt_download})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_header:
                showPic();
                break;
            case R.id.bt_download:
                //downPic("1");
                break;
        }
    }

    private void showPic() {
        downPic();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unbind.unbind();
    }

    private void downPic() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url(pdfUrl)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("下载发票", "true");
                InputStream is = response.body().byteStream();
                //以下为下载操作
                byte[] arr = new byte[1];
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                BufferedOutputStream bos = new BufferedOutputStream(baos);
                int n = is.read(arr);
                while (n > 0) {
                    bos.write(arr);
                    n = is.read(arr);
                }
                bos.close();
                String path = Environment.getExternalStorageDirectory()
                        + "/download/";
                String[] name = pdfUrl.split("/");
                String path2 = path + name[name.length - 1];

                file = new File(path2);
                //判断是否存在文件
                if (file.exists()) {
                    //创建新文件
                    file.createNewFile();
                } else {
                    file.delete();
                    file.createNewFile();
                }
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(baos.toByteArray());
                fos.close();
                mPdfView.fromFile(file).load();
                EventBus.getDefault().post(new MsgEvent(ACTION_DOWNCOMPLETED, path));

            }

        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUI(MsgEvent event) {
        if (event.getAction() == ACTION_GETINVOICEDETAIL) {
            if (event.getStr().equals("1")) {
                activityKpxq.setVisibility(View.VISIBLE);
                //Toast.makeText(KpxqActivity.this, "发票信息获取成功", Toast.LENGTH_SHORT).show();
            }
            if (event.getStr().equals("0")) {
                activityKpxq.setVisibility(View.GONE);
                setContentViewId(R.layout.layout_noinfo);
                //Toast.makeText(KpxqActivity.this, "发票信息获取失败", Toast.LENGTH_SHORT).show();
                return;
            }

            int openStatus = invoiceBean.getOpenStatus();
            if (openStatus == 1) {
                tv.setText("电子发票已开具");
            }
            if (openStatus == 0) {
                tv.setText("电子发票已开具");
            }

            tvTime.setText(invoiceBean.getBuildDate());
            tvFptt.setText("发票抬头: " + invoiceBean.getBuyerName());
            tvFpnr.setText("发票内容: " + invoiceBean.getMaterielName());
            tvFpje.setText("发票金额: " + invoiceBean.getAmount() + "");
            if (invoiceBean.getBuyerTaxCode() != null) {
                tvSbh.setText("纳税人识别号: " + invoiceBean.getBuyerTaxCode());
            } else {
                tvSbh.setText("纳税人识别号: " + "无");
            }
            tvKjsj.setText("开具时间: " + invoiceBean.getBuildDate());
//            if (invoiceBean.getRemarks() != null) {
//                tvBz.setText("备注: " + invoiceBean.getRemarks());
//            } else {
//                tvBz.setText("备注: " + "无");
//            }
            pdfUrl = invoiceBean.getPdfFileUrl();
        }
        if (event.getAction() == ACTION_DOWNCOMPLETED) {
            Toast.makeText(KpxqActivity.this, "文件已保存至"+event.getStr()+"文件夹", Toast.LENGTH_SHORT).show();
            dialog.show();
        }
    }


}

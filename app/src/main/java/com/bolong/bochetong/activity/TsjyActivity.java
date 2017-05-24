package com.bolong.bochetong.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.bolong.bochetong.bean.User;
import com.bolong.bochetong.utils.HttpUtils;
import com.bolong.bochetong.utils.Param;
import com.bolong.bochetong.utils.SharedPreferenceUtil;
import com.bolong.bochetong.view.CustomPopDialog;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class TsjyActivity extends BaseActivity {

    private Unbinder unbinder;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.bt_submit)
    Button btSubmit;

    @Override
    public void onBaseCreate(Bundle bundle) {

        setContentViewId(R.layout.activity_tsjy);

        unbinder = ButterKnife.bind(this);
        showKeyBoard();
        changeBtn();

        btSubmit.setBackgroundResource(R.drawable.shape_code_ing);
        btSubmit.setEnabled(false);

    }

    private void changeBtn() {

        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    btSubmit.setBackgroundResource(R.drawable.shape_code);
                    btSubmit.setEnabled(true);
                } else {

                    btSubmit.setBackgroundResource(R.drawable.shape_code_ing);
                    btSubmit.setEnabled(false);
                }
            }
        });
    }

    private void showKeyBoard() {
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(etContent, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    @Override
    public void initView() {
        setTitle("投诉建议");
    }


    @OnClick(R.id.bt_submit)
    public void onViewClicked() {

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

        String suggestContent = etContent.getText().toString();
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("token", token);
        map.put("suggestContent", suggestContent);
        HttpUtils.post(Param.COMPLAINTSSUGGEST, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(TsjyActivity.this, "请求服务器超时", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonDatas = response.body().string();
                Log.e("投诉建议", jsonDatas);
                try {
                    try {
                        JSONObject jsonObject = new JSONObject(jsonDatas);
                        String content = jsonObject.opt("content").toString();
                        JSONObject jb = new JSONObject(content);
                        final String status = jb.optString("status");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showDialog();
                                //Toast.makeText(TsjyActivity.this, status, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(TsjyActivity.this, "反馈失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(TsjyActivity.this, "服务器异常", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        }, map);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    public void showDialog() {
        CustomPopDialog.Builder dialogBuild = new CustomPopDialog.Builder(this);
        CustomPopDialog dialog = dialogBuild.create(R.layout.layout_dialog_tsjy);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    TsjyActivity.this.finish();
                    return true;
                } else {
                    return false;
                }
            }
        });
    }
}

package com.bolong.bochetong.activity;

import android.content.Context;
import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.bolong.bochetong.bean.User;
import com.bolong.bochetong.bean2.MsgEvent;
import com.bolong.bochetong.utils.HttpUtils;
import com.bolong.bochetong.utils.Param;
import com.bolong.bochetong.utils.SharedPreferenceUtil;
import com.bolong.bochetong.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class BdcpActivity2 extends BaseActivity implements View.OnFocusChangeListener, View.OnTouchListener {
    private Unbinder unbinder;
    @BindView(R.id.edit_province)
    EditText editProvince;
    @BindView(R.id.edit_1)
    EditText edit1;
    @BindView(R.id.edit_2)
    EditText edit2;
    @BindView(R.id.edit_3)
    EditText edit3;
    @BindView(R.id.edit_4)
    EditText edit4;
    @BindView(R.id.edit_5)
    EditText edit5;
    @BindView(R.id.edit_6)
    EditText edit6;
    @BindView(R.id.bt_bind)
    Button btBind;
    @BindView(R.id.keyboard_view)
    KeyboardView keyboardView;

    String license = "";

    private EditText[] mArray;
    private Context mContext;
    private KeyboardView mKeyboardView;
    private Keyboard province_keyboard;
    private Keyboard number_keyboar;
    private EditText currentEdit;
    private int currentIndex;
    private String symbol;
    private String cardId;
    public static final int ACTION_ADDMONTHCAR_SUCCESS = 4875;
    public static final int ACTION_ADDYK_SUCCESS = 487569875;
    public static final int ACTION_BUY_BIND_SUCCESS = 487565557;
    private int position;

    @Override
    public void onBaseCreate(Bundle bundle) {
        setContentViewId(R.layout.activity_bdcp2);

        unbinder = ButterKnife.bind(this);
        btBind.setEnabled(false);

        mContext = this;
        province_keyboard = new Keyboard(mContext, R.xml.province_abbreviation);
        number_keyboar = new Keyboard(mContext, R.xml.number_or_letters);
        mKeyboardView = (KeyboardView)
                findViewById(R.id.keyboard_view);
        mKeyboardView.setKeyboard(province_keyboard);
        mKeyboardView.setEnabled(true);
        mKeyboardView.setPreviewEnabled(false);
        mKeyboardView.setOnKeyboardActionListener(listener);
        editProvince = (EditText) findViewById(R.id.edit_province);
        edit1 = (EditText) findViewById(R.id.edit_1);
        edit2 = (EditText) findViewById(R.id.edit_2);
        edit3 = (EditText) findViewById(R.id.edit_3);
        edit4 = (EditText) findViewById(R.id.edit_4);
        edit5 = (EditText) findViewById(R.id.edit_5);
        edit6 = (EditText) findViewById(R.id.edit_6);
        mArray = new EditText[]{editProvince, edit1,
                edit2, edit3, edit4, edit5, edit6};

        editProvince.setOnFocusChangeListener(this);
        edit1.setOnFocusChangeListener(this);
        edit2.setOnFocusChangeListener(this);
        edit3.setOnFocusChangeListener(this);
        edit4.setOnFocusChangeListener(this);
        edit5.setOnFocusChangeListener(this);
        edit6.setOnFocusChangeListener(this);

        editProvince.setOnTouchListener(this);
        edit1.setOnTouchListener(this);
        edit2.setOnTouchListener(this);
        edit3.setOnTouchListener(this);
        edit4.setOnTouchListener(this);
        edit5.setOnTouchListener(this);
        edit6.setOnTouchListener(this);

        for (int i = 0; i < mArray.length; i++) {
            final int j = i;
            mArray[j].addTextChangedListener(new TextWatcher() {

                private CharSequence temp;
                private int sStart;
                private int sEnd;
                @Override
                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {
                    temp = s;
                }
                @Override
                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                }
                @Override
                public void afterTextChanged(Editable s) {
                    sStart = mArray[j].getSelectionStart();
                    sEnd = mArray[j].getSelectionEnd();
                    //焦点后移
                    if (temp.length() == 1 && j < mArray.length - 1) {
                        mArray[j + 1].setFocusable(true);
                        mArray[j + 1].setFocusableInTouchMode(true);
                        mArray[j + 1].requestFocus();
                    }
                    //新内容替换旧内容
                    if (temp.length() > 1) {
                        s.delete(0, 1);
                        int tSelection = sStart;
                        mArray[j].setText(s);
                        mArray[j].setSelection(tSelection);
                        mArray[j].setFocusable(true);
                    }
                    if (editProvince.getText().length() == 1 && edit1.getText().length() == 1 && edit2.getText().length() == 1 && edit3.getText().length() == 1
                            && edit4.getText().length() == 1 && edit5.getText().length() == 1 && edit6.getText().length() == 1) {
                        btBind.setEnabled(true);
                        license = editProvince.getText().toString() + edit1.getText().toString() + edit2.getText().toString() + edit3.getText().toString()
                                + edit4.getText().toString() + edit5.getText().toString() + edit6.getText().toString();
                    } else {
                        btBind.setEnabled(false);
                    }
                }
            });
        }
        editProvince.setFocusable(true);
        editProvince.requestFocus();
    }

    @Override
    public void initView() {
        if (getIntent() == null) {
            setTitle("绑定车牌");
        } else {
            Intent intent = getIntent();
            symbol = intent.getStringExtra("symbol");
            if(symbol.equals("normal")){
                setTitle("绑定车牌");
            }
            if (symbol.equals("yearCard")) {
                Log.e("执行","true");
                setTitle("绑定年卡车");
                cardId = intent.getStringExtra("yearCardCar");
                position = intent.getIntExtra("position",0);
                Log.e("执行","true2");
            }
            if (symbol.equals("monthCard")) {
                setTitle("绑定月卡车");
                cardId = intent.getStringExtra("monthCardCar");
                position = intent.getIntExtra("position",0);
            }else {
                //setTitle("绑定车牌");
            }
        }

    }

    public final static int RESULT_CODE = 1;

    @OnClick(R.id.bt_bind)
    public void onViewClicked() {

        if (cardId != null) {
            addMonthCar();
        }else {
            addCarCard();
            setResult(1);
        }
    }

    private void addMonthCar() {
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
        map.put("cardId", cardId);
        map.put("carNumber", license);
        HttpUtils.post(Param.BANDMONTHCARDCAR, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonDatas = response.body().string();
                Log.e("绑定月卡车", jsonDatas);
                try {
                    try {
                        JSONObject jsonObject = new JSONObject(jsonDatas);
                        String content = jsonObject.optString("content");
                        final String errMessage = jsonObject.optString("errMessage");
                        JSONObject jb = new JSONObject(content);
                        final String status = jb.optString("status");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (status.equals("1")) {
                                    ToastUtil.showShort(BdcpActivity2.this, "添加成功");
                                    EventBus.getDefault().post(new MsgEvent(ACTION_ADDMONTHCAR_SUCCESS,position));
                                    EventBus.getDefault().post(new MsgEvent(ACTION_ADDYK_SUCCESS,position));
                                    //Log.e("添加成功后",position+"");
                                    finish();
                                } else {
                                    ToastUtil.showShort(BdcpActivity2.this, errMessage);
                                }
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                }
            }
        }, map);
    }

    private void addCarCard() {
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
        map.put("carCard", license);
        HttpUtils.post(Param.ADDCARCARD, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showShort(BdcpActivity2.this, "绑定失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonDatas = response.body().string();
                Log.e("绑定车牌", jsonDatas);
                try {
                    try {
                        JSONObject jsonObject = new JSONObject(jsonDatas);
                        String content = jsonObject.optString("content");
                        //add
                        final String errMessage = jsonObject.optString("errMessage");
                        JSONObject jb = new JSONObject(content);
                        String status = jb.optString("status");
                        if (status.equals("1")) {
                            Intent intent = new Intent();
                            setResult(RESULT_CODE, intent);
                            finish();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    EventBus.getDefault().post(new MsgEvent(ACTION_BUY_BIND_SUCCESS,license));
                                    ToastUtil.showShort(BdcpActivity2.this, "绑定成功");
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.showShort(BdcpActivity2.this, errMessage);
                                }
                            });
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShort(BdcpActivity2.this, "绑定失败");
                            }
                        });
                    }
                } catch (Exception e) {
                    ToastUtil.showShort(BdcpActivity2.this, "绑定失败");
                }
            }
        }, map);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();

        //EventBus.getDefault().post(new MsgEvent(ACTION_ADDMONTHCAR_SUCCESS,position));
    }

    private KeyboardView.OnKeyboardActionListener listener = new KeyboardView.OnKeyboardActionListener() {

        @Override
        public void swipeUp() {
        }

        @Override
        public void swipeRight() {
        }

        @Override
        public void swipeLeft() {
        }

        @Override
        public void swipeDown() {
        }

        @Override
        public void onText(CharSequence text) {
        }

        @Override
        public void onRelease(int primaryCode) {
        }

        @Override
        public void onPress(int primaryCode) {
        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            Editable editable = currentEdit.getText();
            int start = currentEdit.getSelectionStart();
            if (primaryCode == -3) {// 回退

                if (editable.length() == 0) {
                    if (currentIndex > 0) {
                        currentIndex = currentIndex - 1;
                        mArray[currentIndex].setFocusable(true);
                        mArray[currentIndex].requestFocus();
                    }
                }
                if (editable.length() == 1) {
                    editable.clear();
                }

            } else {
                editable.insert(start, Character.toString((char) primaryCode));
            }
        }
    };

    public void changeKeyboard(boolean isnumber) {
        if (isnumber) {
            mKeyboardView.setKeyboard(number_keyboar);
        } else {
            mKeyboardView.setKeyboard(province_keyboard);
        }
    }

    //软键盘展示

    public void showKeyboard() {
        int visibility = mKeyboardView.getVisibility();
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            mKeyboardView.setVisibility(View.VISIBLE);
        }
    }

    //软键盘隐藏
    public void hideKeyboard() {
        int visibility = mKeyboardView.getVisibility();
        if (visibility == View.VISIBLE) {
            mKeyboardView.setVisibility(View.INVISIBLE);
        }
    }

    //禁掉系统软键盘
    public void hideSoftInputMethod() {

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        int currentVersion = Build.VERSION.SDK_INT;
        String methodName = null;
        if (currentVersion >= 16) {
            // 4.2
            methodName = "setShowSoftInputOnFocus";
        } else if (currentVersion >= 14) {
            // 4.0
            methodName = "setSoftInputShownOnFocus";
        }
        if (methodName == null) {
            currentEdit.setInputType(InputType.TYPE_NULL);
        } else {
            Class<EditText> cls = EditText.class;
            Method setShowSoftInputOnFocus;
            try {
                setShowSoftInputOnFocus = cls.getMethod(methodName,
                        boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(currentEdit, false);
            } catch (NoSuchMethodException e) {
                currentEdit.setInputType(InputType.TYPE_NULL);
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    //软键盘展示状态
    public boolean isShow() {
        return mKeyboardView.getVisibility() == View.VISIBLE;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isShow()) {
                hideKeyboard();
                finish();
            } else {
                finish();
            }
        }
        return false;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        if (hasFocus) {

            currentEdit = (EditText) v;

            for (int i = 0; i < mArray.length; i++) {

                EditText temp = mArray[i];
                if (temp == (EditText) v) {
                    currentIndex = i;
                }
            }

            if (currentIndex == 0) {
                changeKeyboard(false);
            } else {
                changeKeyboard(true);
            }
            hideSoftInputMethod();

            if (!isShow()) {
                showKeyboard();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        return super.onTouchEvent(event);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        //add
        EditText currentEdit2 = (EditText) view;
        Editable s = currentEdit2.getText();
        s.clear();
        if (!isShow()) {
            showKeyboard();
        }
        return false;

    }

}

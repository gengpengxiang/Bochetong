package com.bolong.bochetong.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class KpsmActivity extends BaseActivity {


    @BindView(R.id.tv_kpsm)
    TextView tvKpsm;
    private Unbinder unbind;

    @Override
    public void onBaseCreate(Bundle bundle) {
        setContentViewId(R.layout.activity_kpsm);
        unbind = ButterKnife.bind(this);
        getTextContent();

    }

    private void getTextContent() {
        try {
            InputStream is = getResources().openRawResource(R.raw.kpsm);
            String text = readTextFromSDcard(is);
            tvKpsm.setText(text);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String readTextFromSDcard(InputStream is) throws Exception {
        InputStreamReader reader = new InputStreamReader(is);
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuffer buffer = new StringBuffer("");
        String str;
        while ((str = bufferedReader.readLine()) != null) {
            buffer.append(str);
            buffer.append("\n");
        }
        is.close();
        reader.close();
        bufferedReader.close();
        return buffer.toString();
    }

    @Override
    public void initView() {
        setTitle("开票说明");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbind.unbind();
    }
}

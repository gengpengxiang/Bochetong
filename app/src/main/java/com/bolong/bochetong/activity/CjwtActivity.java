package com.bolong.bochetong.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CjwtActivity extends BaseActivity {

    @BindView(R.id.mWebView)
    WebView mWebView;
    private Unbinder unbind;

    @Override
    public void onBaseCreate(Bundle bundle) {

        setContentViewId(R.layout.activity_cjwt);
        unbind = ButterKnife.bind(this);

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mWebView.loadUrl("file:///android_asset/cjwt.html");

    }

    @Override
    public void initView() {
        setTitle("常见问题");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbind.unbind();
    }


}

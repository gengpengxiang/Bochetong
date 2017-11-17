package com.bolong.bochetong.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.ClientCertRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.bolong.bochetong.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class WebActivity extends BaseActivity {

    @BindView(R.id.mWebView)
    WebView mWebView;
    private Unbinder unbind;
    private String urlAd;

    @Override
    public void onBaseCreate(Bundle bundle) {
        setContentViewId(R.layout.activity_web);
        unbind = ButterKnife.bind(this);

        Intent intent = getIntent();
        urlAd = intent.getStringExtra("url");
        //js点击事件等
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mWebView.loadUrl(urlAd);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if(urlAd.equals("http://www.blpark.com/bolongshare")){
                    setTitle("隆恒瑞");
                }else {
                    setTitle(view.getTitle());
                }

            }

        });
        //判断页面加载过程
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    // 网页加载完成
                } else {
                    // 加载中
                }

            }
        });
    }

    @Override
    public void initView() {
        setTitle("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbind.unbind();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();//返回上一页面
                return true;
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}

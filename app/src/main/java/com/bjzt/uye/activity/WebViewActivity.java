package com.bjzt.uye.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;

import com.bjzt.uye.R;
import com.bjzt.uye.activity.base.BaseActivity;
import com.bjzt.uye.http.HttpCommon;
import com.bjzt.uye.listener.IHeaderListener;
import com.bjzt.uye.util.IntentUtils;
import com.bjzt.uye.views.component.BlankEmptyView;
import com.bjzt.uye.views.component.MWebView;
import com.bjzt.uye.views.component.YHeaderView;
import com.common.common.MyLog;
import com.common.common.NetCommon;
import com.common.controller.LoginController;
import com.common.http.HttpEngine;
import com.common.util.DeviceUtil;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import butterknife.BindView;

/**
 * 芝麻信用
 * Created by billy on 2017/10/16.
 */
public class WebViewActivity extends BaseActivity{

    @BindView(R.id.webview_header)
    YHeaderView mHeader;
    @BindView(R.id.progressbar)
    ProgressBar progressBar;
    @BindView(R.id.blank_empty)
    BlankEmptyView emptyView;
    @BindView(R.id.webview)
    MWebView mWebView;

    private final int FLAG_EMPYTVIEW = 0x10;

    private String mUrl;
    private String mTitle;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_webview_layout;
    }

    @Override
    protected void initLayout() {
        mHeader.updateType(YHeaderView.TYPE_ABOUT);
        String title = getResources().getString(R.string.webview_title);
        if(!TextUtils.isEmpty(mTitle)){
            title = this.mTitle;
        }
        mHeader.setTitle(title);
        mHeader.setIListener(new IHeaderListener() {
            @Override
            public void onLeftClick() {
                finish();
            }
        });

        mWebView.setWebViewClient(webViewClient);
        mWebView.setWebChromeClient(chromeClient);
        //设置可缩放
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setSupportZoom(true);
        //打开js开关
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        //cache信息
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setDatabaseEnabled(true);
        //
        WebSettings webSetting = mWebView.getSettings();
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setSupportMultipleWindows(true);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);

        //webview远程注入漏洞进行移除
        int ver = DeviceUtil.getAndroidSDKVersion();
        if(ver >= 11 && ver <= 18){
            mWebView.removeJavascriptInterface("searchBoxJavaBridge_");
        }

        //load url
        if(!TextUtils.isEmpty(mUrl)){
            setSynCookies(mUrl);
            mWebView.loadUrl(mUrl);
        }
    }

    @Override
    protected void initExtras(Bundle bundle) {
        Intent intent = getIntent();
        if(intent != null){
            mUrl = intent.getStringExtra(IntentUtils.KEY_WEB_URL);
            mTitle = intent.getStringExtra(IntentUtils.KEY_TITLE);
        }
    }

    private WebViewClient webViewClient = new WebViewClient(){
        public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
            showLoading("");
        };

        public void onPageFinished(WebView view, String url) {
            hideLoadingDialog();
        };

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(mWebView != null){
                setSynCookies(mUrl);
                if(MyLog.isDebugable()){
                    MyLog.debug(TAG, "[shouldOverrideUrlLoading]" + "url:" + url);
                }
                if(!TextUtils.isEmpty(url)){
                    if(url.startsWith("tel:") || url.startsWith("mailto:") || url.startsWith("geo:")){
                        IntentUtils.startSysActivity(WebViewActivity.this,url);
                        return true;
                    }
                }
                mWebView.loadUrl(url);
            }
            return true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            MyLog.debug(TAG,"[onReceivedError]" + " errorCode:" + errorCode);
            Message msg = Message.obtain();
            msg.what = FLAG_EMPYTVIEW;
            msg.arg1 = errorCode;
            msg.obj = description;
            sendMsg(msg);
        };
    };


    /**
     *同步设置cookie
     */
    private void setSynCookies(String url) {
        String keyDomain = HttpEngine.getInstance().getRefer(NetCommon.NET_INTERFACE_TYPE_UYE);
        if(!TextUtils.isEmpty(url) && url.contains(keyDomain)){
            Context mContext = this.getApplicationContext();
            CookieSyncManager.createInstance(mContext);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.removeSessionCookie();//移除
            String cookies = LoginController.getInstance().getCookie();
            String strExtras = "Max-Age=3600;Domain=" + keyDomain + ";" + "Path = /";
            String strCookie = cookies + strExtras;
            cookieManager.setCookie(keyDomain, strCookie);//cookies是在HttpClient中获得的cookie

            if(MyLog.isDebugable()){
                MyLog.debug(TAG,"[setSynCookies]" + " key:" + url + " strCookie:" + strCookie);
            }

            CookieSyncManager.getInstance().sync();
        }
    }

    private WebChromeClient chromeClient = new WebChromeClient(){
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if(progressBar != null){
                progressBar.setProgress(newProgress);
            }
            if(newProgress >= 100){
                progressBar.setVisibility(View.GONE);
            }else{
                if(newProgress > 0){
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        };

        @Override
        public void onReceivedTitle(WebView view, String title) {
            if(mHeader != null &&  !TextUtils.isEmpty(title)){
                mHeader.setTitle(title);
            }
        }
    };


}

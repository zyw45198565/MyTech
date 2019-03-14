package com.wd.tech.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.wd.tech.R;
import com.wd.tech.utils.util.WDActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdvertiseActivity extends WDActivity {

    @BindView(R.id.mywebview)
    WebView mywebview;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.progressBar1)
    ProgressBar progressBar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_advertise;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        String zurl = intent.getStringExtra("zurl");

        WebSettings mWebSettings = mywebview.getSettings();
        /* 设置支持Js,必须设置的,不然网页基本上不能看 */
        mWebSettings.setJavaScriptEnabled(true);
        /* 设置WebView是否可以由JavaScript自动打开窗口，默认为false，通常与JavaScript的window.open()配合使用。*/
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        /* 设置缓存模式,我这里使用的默认,不做多讲解 */
        mWebSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        /* 设置为true表示支持使用js打开新的窗口 */
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        /* 大部分网页需要自己保存一些数据,这个时候就的设置下面这个属性 */
        mWebSettings.setDomStorageEnabled(true);
        /* 设置为使用webview推荐的窗口 */
        mWebSettings.setUseWideViewPort(true);
        /* 设置网页自适应屏幕大小 ---这个属性应该是跟上面一个属性一起用 */
        mWebSettings.setLoadWithOverviewMode(true);
        /* HTML5的地理位置服务,设置为true,启用地理定位 */
        mWebSettings.setGeolocationEnabled(true);
        /* 设置是否允许webview使用缩放的功能,我这里设为false,不允许 */
        mWebSettings.setBuiltInZoomControls(false);
        /* 提高网页渲染的优先级 */
        mWebSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        /* 设置显示水平滚动条,就是网页右边的滚动条.我这里设置的不显示 */
        mywebview.setHorizontalScrollBarEnabled(false);
        /* 指定垂直滚动条是否有叠加样式 */
        mywebview.setVerticalScrollbarOverlay(true);
        /* 设置滚动条的样式 */
        mywebview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        /* 这个不用说了,重写WebChromeClient监听网页加载的进度,从而实现进度条 */
        mywebview.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
                super.onProgressChanged(view, newProgress);
            }

        });
        /* 同上,重写WebViewClient可以监听网页的跳转和资源加载等等... */
        mywebview.setWebViewClient(new WebViewClient() {


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("scheme:") || url.startsWith("scheme:")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                }
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressBar.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(AdvertiseActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                super.onReceivedError(view, errorCode, description, failingUrl);
            }


        });
        mywebview.loadUrl(zurl);

    }

    @Override
    protected void destoryData() {

    }

}

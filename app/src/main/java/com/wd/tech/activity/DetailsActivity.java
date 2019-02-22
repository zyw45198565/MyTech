package com.wd.tech.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wd.tech.R;
import com.wd.tech.WDApp;
import com.wd.tech.bean.DetailsBean;
import com.wd.tech.bean.Result;
import com.wd.tech.presenter.DetailsPresenter;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.exception.ApiException;
import com.wd.tech.utils.util.WDActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends WDActivity {


    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.thumbnail)
    SimpleDraweeView thumbnail;
    @BindView(R.id.mywebview)
    WebView mywebview;
    @BindView(R.id.source)
    TextView source;
    private DetailsBean detailsBean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_details;
    }

    @Override
    protected void initView() {

        int userid = WDApp.getShare().getInt("userid", 0);
        String sessionid = WDApp.getShare().getString("sessionid", "");
        Intent intent = getIntent();
        int zid = intent.getIntExtra("zid", 0);
        DetailsPresenter detailsPresenter = new DetailsPresenter(new DetailsCall());
        detailsPresenter.reqeust(userid, sessionid, zid);
    }

    @Override
    protected void destoryData() {

    }



    private class DetailsCall implements DataCall<Result<DetailsBean>> {
        @Override
        public void success(Result<DetailsBean> data) {
            detailsBean = data.getResult();
            title.setText(detailsBean.getTitle());
            time.setText(detailsBean.getReleaseTime() + "");
            source.setText(detailsBean.getSource());
            thumbnail.setImageURI(detailsBean.getThumbnail());

            WebSettings settings = mywebview.getSettings();
            settings.setTextZoom(250); // 通过百分比来设置文字的大小，默认值是100。
            settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            settings.setUseWideViewPort(true);
            settings.setLoadWithOverviewMode(true);


            mywebview.loadDataWithBaseURL(null, detailsBean.getContent(), "text/html", "utf-8", null);
            mywebview.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    int w = View.MeasureSpec.makeMeasureSpec(0,
                            View.MeasureSpec.UNSPECIFIED);
                    int h = View.MeasureSpec.makeMeasureSpec(0,
                            View.MeasureSpec.UNSPECIFIED);
                    //重新测量
                    view.measure(w, h);
//                    mWebViewHeight = view.getHeight();
//                    Log.i(TAG, "WEBVIEW高度:" + view.getHeight());
                }
            });
        }

        @Override
        public void fail(ApiException e) {

        }
    }
}

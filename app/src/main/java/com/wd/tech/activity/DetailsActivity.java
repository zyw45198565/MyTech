package com.wd.tech.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wd.tech.R;
import com.wd.tech.WDApp;
import com.wd.tech.adapter.CommentAdapter;
import com.wd.tech.adapter.InforAdapter;
import com.wd.tech.adapter.PlateAdapter;
import com.wd.tech.bean.DetailsBean;
import com.wd.tech.bean.MyComment;
import com.wd.tech.bean.Result;
import com.wd.tech.presenter.AddInfoCommentPresenter;
import com.wd.tech.presenter.DetailsPresenter;
import com.wd.tech.presenter.MyCommentPresenter;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.exception.ApiException;
import com.wd.tech.utils.util.DateUtils;
import com.wd.tech.utils.util.WDActivity;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

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
    @BindView(R.id.platerecycler)
    RecyclerView platerecycler;
    @BindView(R.id.inforrecycler)
    RecyclerView inforrecycler;
    @BindView(R.id.commentrecycler)
    RecyclerView commentrecycler;
    @BindView(R.id.comment_one)
    LinearLayout commentOne;
    @BindView(R.id.publish)
    TextView publish;
    @BindView(R.id.comment_two)
    LinearLayout commentTwo;
    @BindView(R.id.mycomment_one)
    EditText mycommentOne;
    @BindView(R.id.mycomment_two)
    EditText mycommentTwo;
    private DetailsBean detailsBean;
    private PlateAdapter plateAdapter;
    private InforAdapter inforAdapter;
    private CommentAdapter commentAdapter;
    private MyCommentPresenter myCommentPresenter;
    private AddInfoCommentPresenter addInfoCommentPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_details;
    }

    @Override
    protected void initView() {

        final int userid = WDApp.getShare().getInt("userid", 0);
        final String sessionid = WDApp.getShare().getString("sessionid", "");
        Intent intent = getIntent();
        final int zid = intent.getIntExtra("zid", 0);
        DetailsPresenter detailsPresenter = new DetailsPresenter(new DetailsCall());
        detailsPresenter.reqeust(userid, sessionid, zid);

        //板块
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        platerecycler.setLayoutManager(linearLayoutManager);
        plateAdapter = new PlateAdapter(this);
        platerecycler.setAdapter(plateAdapter);

        //推荐
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        inforrecycler.setLayoutManager(linearLayoutManager2);
        inforAdapter = new InforAdapter(this);
        inforrecycler.setAdapter(inforAdapter);

        //评论
        myCommentPresenter = new MyCommentPresenter(new CommentCall());
        myCommentPresenter.reqeust(userid, sessionid, zid, 1, 5);
        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(this);
        commentrecycler.setLayoutManager(linearLayoutManager3);
        commentAdapter = new CommentAdapter(this);
        commentrecycler.setAdapter(commentAdapter);

        //发布评论
        addInfoCommentPresenter = new AddInfoCommentPresenter(new AddInfoComment());
        mycommentOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentOne.setVisibility(View.GONE);
                commentTwo.setVisibility(View.VISIBLE);
            }
        });
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String trim = mycommentTwo.getText().toString().trim();
                if (trim.equals("")){
                    Toast.makeText(DetailsActivity.this,"请输入内容!",Toast.LENGTH_SHORT).show();
                }else{
                    addInfoCommentPresenter.reqeust(userid,sessionid,trim,zid);
                    commentAdapter.notifyDataSetChanged();
                    commentOne.setVisibility(View.VISIBLE);
                    commentTwo.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    protected void destoryData() {

    }


    private class DetailsCall implements DataCall<Result<DetailsBean>> {
        @Override
        public void success(Result<DetailsBean> data) {
            detailsBean = data.getResult();
            title.setText(detailsBean.getTitle());
            try {
                time.setText(DateUtils.dateFormat(new Date(detailsBean.getReleaseTime()), DateUtils.MINUTE_PATTERN));
            } catch (ParseException e) {
                e.printStackTrace();
            }
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

            //板块
            List<DetailsBean.PlateBean> plate = data.getResult().getPlate();
            plateAdapter.addAll(plate);
            plateAdapter.notifyDataSetChanged();

            //推荐
            List<DetailsBean.InformationListBean> informationList = data.getResult().getInformationList();
            inforAdapter.addAll(informationList);
            inforAdapter.notifyDataSetChanged();

        }

        @Override
        public void fail(ApiException e) {

        }
    }

    private class CommentCall implements DataCall<Result<List<MyComment>>> {
        @Override
        public void success(Result<List<MyComment>> data) {
            List<MyComment> result = data.getResult();
            commentAdapter.addAll(result);
            commentAdapter.notifyDataSetChanged();
        }

        @Override
        public void fail(ApiException e) {

        }
    }

    private class AddInfoComment implements DataCall<Result> {
        @Override
        public void success(Result data) {
            Toast.makeText(DetailsActivity.this,data.getMessage(),Toast.LENGTH_SHORT).show();

        }

        @Override
        public void fail(ApiException e) {

        }
    }
}

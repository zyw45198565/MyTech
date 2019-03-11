package com.wd.tech.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wd.tech.R;
import com.wd.tech.WDApp;
import com.wd.tech.adapter.CommentAdapter;
import com.wd.tech.adapter.HomeAllAdapter;
import com.wd.tech.adapter.InforAdapter;
import com.wd.tech.adapter.PlateAdapter;
import com.wd.tech.bean.DetailsBean;
import com.wd.tech.bean.HomeAll;
import com.wd.tech.bean.MyComment;
import com.wd.tech.bean.Result;
import com.wd.tech.frag.Frag_01;
import com.wd.tech.presenter.AddCollectionPresenter;
import com.wd.tech.presenter.AddGreatRecordPresenter;
import com.wd.tech.presenter.AddInfoCommentPresenter;
import com.wd.tech.presenter.CancelCollectionPresenter;
import com.wd.tech.presenter.CancelGreatPresenter;
import com.wd.tech.presenter.DetailsPresenter;
import com.wd.tech.presenter.HomeAllPresenter;
import com.wd.tech.presenter.MyCommentPresenter;
import com.wd.tech.presenter.TheTaskPresenter;
import com.wd.tech.presenter.WDPresenter;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.exception.ApiException;
import com.wd.tech.utils.util.DateUtils;
import com.wd.tech.utils.util.WDActivity;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends BaseActivity {


    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.thumbnail)
    SimpleDraweeView thumbnail;
    @BindView(R.id.mytext)
    TextView mytext;
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
    @BindView(R.id.buy)
    Button buy;
    @BindView(R.id.buy_all)
    RelativeLayout buyAll;
    @BindView(R.id.comment_num)
    TextView commentNum;
    @BindView(R.id.hand_num)
    TextView handNum;
    @BindView(R.id.share_num)
    TextView shareNum;
    @BindView(R.id.hand)
    CheckBox hand;
    @BindView(R.id.like)
    CheckBox like;
    @BindView(R.id.share)
    CheckBox share;
    @BindView(R.id.comment_all)
    LinearLayout commentAll;
    @BindView(R.id.back)
    ImageView back;
    private DetailsBean detailsBean;
    private PlateAdapter plateAdapter;
    private InforAdapter inforAdapter;
    private CommentAdapter commentAdapter;
    private MyCommentPresenter myCommentPresenter;
    private AddInfoCommentPresenter addInfoCommentPresenter;
    private int userid;
    private String sessionid;
    private int zid;
    private int bid;
    private DetailsPresenter detailsPresenter;
    private Dialog dialog;
    private Intent intent;
    private int classify;
    private IWXAPI wxapi;
    int page = 1;
    int count = 5;
    int plateId = 0;
    private HomeAllPresenter homeAllPresenter;
    private HomeAllAdapter homeAllAdapter = new HomeAllAdapter(this);
    private boolean zai;
    private TheTaskPresenter theTaskPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_details;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (resultCode == 1) {
            buyAll.setVisibility(View.GONE);
            dialog.dismiss();
        }
    }

    @Override
    protected void initView() {
        // 通过WXAPIFactory工厂，获取IWXAPI的实例  APP_ID为微信的AppID
        wxapi = WXAPIFactory.createWXAPI(DetailsActivity.this, "wx4c96b6b8da494224", true);

        // 将应用的appId注册到微信
        wxapi.registerApp("wx4c96b6b8da494224");
        zai = WDApp.getShare().getBoolean("zai", false);

        if (WDApp.getShare().getBoolean("zai", false)) {
            userid = WDApp.getShare().getInt("userid", 0);
            sessionid = WDApp.getShare().getString("sessionid", "");
        } else {
            userid = 0;
            sessionid = "";
        }
        intent = getIntent();
        zid = intent.getIntExtra("zid", 0);
        bid = intent.getIntExtra("zurl", 1);
        classify = intent.getIntExtra("classify", 0);

        theTaskPresenter = new TheTaskPresenter(new TheTask());


        ImageLoader imageLoader = ImageLoader.getInstance();//ImageLoader需要实例化
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));

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
        myCommentPresenter.reqeust(userid, sessionid, this.zid, 1, 5);
        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(this);
        commentrecycler.setLayoutManager(linearLayoutManager3);
        commentAdapter = new CommentAdapter(this);
        commentrecycler.setAdapter(commentAdapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
                if (!zai) {
                    Toast.makeText(DetailsActivity.this, "请登录！", Toast.LENGTH_SHORT).show();
                    mycommentTwo.setText("");
                    commentOne.setVisibility(View.VISIBLE);
                    commentTwo.setVisibility(View.GONE);

                } else {
                    String trim = mycommentTwo.getText().toString().trim();
                    if (trim.equals("")) {
                        Toast.makeText(DetailsActivity.this, "请输入内容!", Toast.LENGTH_SHORT).show();
                    } else {
                        theTaskPresenter.reqeust(userid,sessionid,1002);
                        addInfoCommentPresenter.reqeust(userid, sessionid, trim, DetailsActivity.this.zid);
                        mycommentTwo.setText("");
                        commentOne.setVisibility(View.VISIBLE);
                        commentTwo.setVisibility(View.GONE);
                    }
                }
            }
        });

        //点赞 喜欢 分享
        hand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!WDApp.getShare().getBoolean("zai", false)) {
                    Toast.makeText(DetailsActivity.this, "请登录", Toast.LENGTH_SHORT).show();
                    hand.setChecked(false);

                    hand.setEnabled(false);
                } else {

                    CheckBox checkBox = (CheckBox) v;
                    boolean checked = checkBox.isChecked();
                    if (checked) {
                        AddGreatRecordPresenter addGreatRecordPresenter = new AddGreatRecordPresenter(new HandCall());
                        addGreatRecordPresenter.reqeust(userid, sessionid, DetailsActivity.this.zid);
                    } else {

                        CancelGreatPresenter cancelGreatPresenter = new CancelGreatPresenter(new CancelHandCall());
                        cancelGreatPresenter.reqeust(userid, sessionid, DetailsActivity.this.zid);

                    }
                }

            }
        });

        dialog = new Dialog(DetailsActivity.this, R.style.DialogTheme);
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!zai) {
                    Toast.makeText(DetailsActivity.this, "请登录！", Toast.LENGTH_SHORT).show();
                    like.setChecked(false);
                    like.setEnabled(false);

                } else {
                    CheckBox checkBox = (CheckBox) v;
                    boolean checked = checkBox.isChecked();
                    if (checked) {
                        AddCollectionPresenter addCollectionPresenter = new AddCollectionPresenter(new MyCollect());
                        addCollectionPresenter.reqeust(userid, sessionid, DetailsActivity.this.zid);
                    } else {
                        String eid = DetailsActivity.this.zid + "";
                        CancelCollectionPresenter cancelCollectionPresenter = new CancelCollectionPresenter(new MyCancelCollect());
                        cancelCollectionPresenter.reqeust(userid, sessionid, eid);
                    }
                }
            }
        });
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = View.inflate(DetailsActivity.this, R.layout.twobuy, null);
                dialog.setContentView(view);
                dialog.getWindow().setGravity(Gravity.BOTTOM);
                dialog.show();
                getshoud(2);
                TextView vip = view.findViewById(R.id.vip);
                TextView integral = view.findViewById(R.id.integral);
                vip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!zai) {
                            Toast.makeText(DetailsActivity.this, "请登录！", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent1 = new Intent(DetailsActivity.this, VipActivity.class);
                            intent1.putExtra("did", detailsBean.getId());
                            startActivity(intent1);
                        }

                    }
                });
                integral.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean zai = WDApp.getShare().getBoolean("zai", false);
                        Log.i("hahah", !zai + "66666666");

                        if (!zai) {
                            Toast.makeText(DetailsActivity.this, "请登录！", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent1 = new Intent(DetailsActivity.this, IntegralActivity.class);
                            intent1.putExtra("did", detailsBean.getId());
                            intent1.putExtra("integralCost", detailsBean.getIntegralCost());
                            intent1.putExtra("summary", detailsBean.getSummary());
                            intent1.putExtra("thumbnail", detailsBean.getThumbnail());
                            intent1.putExtra("title", detailsBean.getTitle());
                            intent1.putExtra("source", detailsBean.getSource());
                            intent1.putExtra("share", detailsBean.getShare());
                            intent1.putExtra("whetherCollection", detailsBean.getWhetherCollection());
                            startActivityForResult(intent1, 1);
                        }
                    }
                });

            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    theTaskPresenter.reqeust(userid,sessionid,1004);
                /*if (!zai) {
                    Toast.makeText(DetailsActivity.this, "请登录！", Toast.LENGTH_SHORT).show();
                    share.setChecked(false);
                    share.setEnabled(false);
                } else {*/
                    dialog = new Dialog(DetailsActivity.this, R.style.DialogTheme);

                    View view = View.inflate(DetailsActivity.this, R.layout.twoshare, null);
                    dialog.setContentView(view);
                    dialog.getWindow().setGravity(Gravity.BOTTOM);
                    dialog.show();
                    getshoud(1);
                    TextView cancle = view.findViewById(R.id.cancel);
                    ImageView wxfriend = view.findViewById(R.id.wxfriend);
                    ImageView weixinf = view.findViewById(R.id.weixinf);
                    cancle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    wxfriend.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //微信朋友圈
                            WeChatShare(1);
                        }
                    });
                    weixinf.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //微信朋友圈
                            WeChatShare(2);
                        }
                    });
                }
        });



    }

    private void getshoud(int bottom) {
        Window dialogWindow = dialog.getWindow();
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高度
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.width = (int) (d.getWidth()); // 宽度设置为屏幕的0.65，根据实际情况调整
        if (bottom == 1) {
            p.height = (int) (d.getHeight() * 0.2);

        } else {
            p.height = (int) (d.getHeight() * 0.65);

        }
        dialogWindow.setAttributes(p);
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


            //数量
            commentNum.setText(detailsBean.getComment() + "");
            handNum.setText(detailsBean.getPraise() + "");
            shareNum.setText(detailsBean.getShare() + "");

            //0评论
            if (detailsBean.getComment() == 0) {
                commentAll.setVisibility(View.VISIBLE);
            }
            //是否点赞 是否收藏
            int whetherGreat = detailsBean.getWhetherGreat();
            int whetherCollection = detailsBean.getWhetherCollection();
            if (whetherGreat == 1) {
                hand.setChecked(true);
            }
            if (whetherCollection == 1) {
                like.setChecked(true);
            }

            //付费

            int readPower = detailsBean.getReadPower();
            if (readPower == 2) {
                buyAll.setVisibility(View.VISIBLE);
            } else {
                URLImageParser imageGetter = new URLImageParser(mytext);
                String string = detailsBean.getContent();
                mytext.setText(Html.fromHtml(string, imageGetter, null));
                /*WebSettings settings = mywebview.getSettings();
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
                });*/
            }

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

    public class URLImageParser implements Html.ImageGetter {
        TextView mTextView;

        public URLImageParser(TextView textView) {
            this.mTextView = textView;
        }

        @Override
        public Drawable getDrawable(String source) {
            final URLDrawable urlDrawable = new URLDrawable();
            ImageLoader.getInstance().loadImage(source,
                    new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            urlDrawable.bitmap = loadedImage;
                            urlDrawable.setBounds(0, 0, loadedImage.getWidth(), loadedImage.getHeight());
                            mTextView.invalidate();
                            mTextView.setText(mTextView.getText());
                        }
                    });
            return urlDrawable;
        }
    }

    public class URLDrawable extends BitmapDrawable {
        protected Bitmap bitmap;

        @Override
        public void draw(Canvas canvas) {
            if (bitmap != null) {
                canvas.drawBitmap(bitmap, 0, 0, getPaint());
            }
        }
    }

    //评论
    private class CommentCall implements DataCall<Result<List<MyComment>>> {
        @Override
        public void success(Result<List<MyComment>> data) {
            List<MyComment> result = data.getResult();
            commentAdapter.addAll(result);
            commentAdapter.notifyDataSetChanged();
            commentAll.setVisibility(View.GONE);


        }

        @Override
        public void fail(ApiException e) {

        }
    }

    private class AddInfoComment implements DataCall<Result> {
        @Override
        public void success(Result data) {
//            Toast.makeText(DetailsActivity.this,data.getMessage(),Toast.LENGTH_SHORT).show();
            detailsPresenter.reqeust(userid, sessionid, zid);
            myCommentPresenter.reqeust(userid, sessionid, zid, 1, 5);
            commentAdapter.clear();
            commentAdapter.notifyDataSetChanged();
        }

        @Override
        public void fail(ApiException e) {

        }
    }

    //点赞
    private class HandCall implements DataCall<Result> {
        @Override
        public void success(Result data) {
            Toast.makeText(DetailsActivity.this, data.getMessage(), Toast.LENGTH_SHORT).show();
            detailsPresenter.reqeust(userid, sessionid, zid);

        }

        @Override
        public void fail(ApiException e) {

        }
    }

    //取消点赞
    private class CancelHandCall implements DataCall<Result> {
        @Override
        public void success(Result data) {
            Toast.makeText(DetailsActivity.this, data.getMessage(), Toast.LENGTH_SHORT).show();
            detailsPresenter.reqeust(userid, sessionid, zid);

        }

        @Override
        public void fail(ApiException e) {

        }
    }

    private class MyCollect implements DataCall<Result> {
        @Override
        public void success(Result data) {
            Toast.makeText(DetailsActivity.this, data.getMessage(), Toast.LENGTH_SHORT).show();
            detailsPresenter.reqeust(userid, sessionid, zid);
            homeAllPresenter.reqeust(userid, sessionid, plateId, page, count);

        }

        @Override
        public void fail(ApiException e) {

        }
    }

    private class MyCancelCollect implements DataCall<Result> {
        @Override
        public void success(Result data) {
            Toast.makeText(DetailsActivity.this, data.getMessage(), Toast.LENGTH_SHORT).show();
            detailsPresenter.reqeust(userid, sessionid, zid);
            homeAllPresenter.reqeust(userid, sessionid, plateId, page, count);

        }

        @Override
        public void fail(ApiException e) {

        }
    }
    private class TheTask implements DataCall<Result> {
        @Override
        public void success(Result data) {

        }

        @Override
        public void fail(ApiException e) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        detailsPresenter = new DetailsPresenter(new DetailsCall());
        if (classify == 1) {
            //banner
            detailsPresenter.reqeust(userid, sessionid, bid);

        } else {
            //列表
            detailsPresenter.reqeust(userid, sessionid, zid);

        }

    }

    //分享链接
    public void WeChatShare(int classify) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = "http://www.hooxiao.com";

        initSend(webpage, classify);
    }

    private void initSend(WXMediaMessage.IMediaObject webpage, int classify) {
        WXMediaMessage msg = new WXMediaMessage();
        msg.title = detailsBean.getTitle();
        msg.description = detailsBean.getSummary();
        msg.mediaObject = webpage;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        if (classify == 1) {
            req.scene = SendMessageToWX.Req.WXSceneTimeline;    //设置发送到朋友圈
        } else if (classify == 2) {
            req.scene = SendMessageToWX.Req.WXSceneSession;    //设置发送到朋友
        }
        wxapi.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}

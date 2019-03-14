package com.wd.tech.picter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.wd.tech.R;
import com.wd.tech.utils.util.WDActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoViewActivity extends WDActivity {

    public static final String TAG = PhotoViewActivity.class.getSimpleName();
    @BindView(R.id.view_pager_photo)
    PhotoViewPager mViewPager;
    @BindView(R.id.tv_image_count)
    TextView mTvImageCount;
    private int currentPosition;
    private MyImageAdapter adapter;
    private List<String> Urls = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_photo_view;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        Intent intent = getIntent();
        currentPosition = intent.getIntExtra("position", 0);//下标
        ArrayList<String> imageArrays = (ArrayList<String>) intent.getSerializableExtra("imageArray");//图片集合
        for (int i = 0; i < imageArrays.size(); i++) {
            String s = imageArrays.get(i);
            Urls.add(s);
        }

        adapter = new MyImageAdapter(Urls, this);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(currentPosition, false);
        mTvImageCount.setText(currentPosition + 1 + "/" + Urls.size());
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPosition = position;
                mTvImageCount.setText(currentPosition + 1 + "/" + Urls.size());
            }
        });
    }

    @Override
    protected void destoryData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
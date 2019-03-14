package com.wd.tech.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wd.tech.R;
import com.wd.tech.adapter.SearchByTitleAdapter;
import com.wd.tech.adapter.SearchHotCiAdapter;
import com.wd.tech.bean.InformationSearchByTitleBean;
import com.wd.tech.bean.Result;
import com.wd.tech.presenter.ByTitlePresenter;
import com.wd.tech.utils.DataCall;
import com.wd.tech.utils.exception.ApiException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity {


    @BindView(R.id.search_text)
    EditText searchText;
    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.bigsou)
    LinearLayout bigsou;
    @BindView(R.id.ad)
    TextView ad;
    @BindView(R.id.resoucilist)
    RecyclerView resoucilist;
    @BindView(R.id.resou)
    RelativeLayout resou;
    @BindView(R.id.searchlist)
    RecyclerView searchlist;
    @BindView(R.id.liebiao)
    RelativeLayout liebiao;
    @BindView(R.id.wu)
    RelativeLayout wu;
    @BindView(R.id.sou)
    ImageView sou;
    private SearchHotCiAdapter mSearchHotCiAdapter;
    private SearchByTitleAdapter mSearchByTitleAdapter;
    private ByTitlePresenter mSearchByTitlePresenter;
    private String text;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        text = searchText.getText().toString().trim();

        mSearchByTitlePresenter = new ByTitlePresenter(new SearchCall());
        searchlist.setLayoutManager(new LinearLayoutManager(SearchActivity.this, OrientationHelper.VERTICAL, false));
        mSearchByTitleAdapter = new SearchByTitleAdapter(this);
        searchlist.setAdapter(mSearchByTitleAdapter);

        //点击键盘的搜索（enter）
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH || i == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    text = searchText.getText().toString().trim();

                    //此处做逻辑处理
                    if (text.equals("")) {
                        Toast.makeText(SearchActivity.this, "请输入关键词！", Toast.LENGTH_SHORT).show();
                        searchlist.setVisibility(View.GONE);
                        resou.setVisibility(View.VISIBLE);
                    } else {
                        hideKeyboard(searchText);
                        mSearchByTitlePresenter.reqeust(text, 1, 30);
                        resou.setVisibility(View.GONE);
                        searchlist.setVisibility(View.VISIBLE);
                    }
                    return true;
                }
                return false;
            }
        });

        List<String> list = new ArrayList<>();
        list.add("区块链");
        list.add("中年危机");
        list.add("锤子科技");
        list.add("子弹短信");
        list.add("民营企业");
        list.add("特斯拉");
        list.add("支付宝");
        list.add("资本市场");
        list.add("电视剧");
        resoucilist.setLayoutManager(new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL));
        mSearchHotCiAdapter = new SearchHotCiAdapter(this, list);
        resoucilist.setAdapter(mSearchHotCiAdapter);

        mSearchHotCiAdapter.notifyDataSetChanged();
        mSearchHotCiAdapter.setFuzhi(new SearchHotCiAdapter.fuzhi() {
            @Override
            public void hotci(String s) {
                searchText.setText(s);
                resou.setVisibility(View.GONE);
                searchlist.setVisibility(View.VISIBLE);
                mSearchByTitlePresenter.reqeust(s, 1, 30);

            }
        });
    }

    @Override
    protected void destoryData() {

    }


    @OnClick({R.id.sou, R.id.back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sou:
                text = searchText.getText().toString().trim();
                searchlist.setVisibility(View.GONE);
                resou.setVisibility(View.VISIBLE);
                mSearchByTitlePresenter.reqeust(text, 1, 30);
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    private class SearchCall implements DataCall<Result<List<InformationSearchByTitleBean>>> {

        @Override
        public void success(Result<List<InformationSearchByTitleBean>> result) {
            Toast.makeText(SearchActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
            List<InformationSearchByTitleBean> searchlist = result.getResult();
            if (searchlist.size() == 0) {
                resou.setVisibility(View.VISIBLE);
                wu.setVisibility(View.VISIBLE);
                mSearchByTitleAdapter.reset(searchlist);

            } else {
                wu.setVisibility(View.GONE);
                mSearchByTitleAdapter.reset(searchlist);
            }
        }

        @Override
        public void fail(ApiException e) {

        }
    }

    /**
     * 隐藏软键盘
     *
     * @param view :一般为EditText
     */
    public static void hideKeyboard(View view) {
        InputMethodManager manager = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}

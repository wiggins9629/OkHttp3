package com.wiggins.okhttp3.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wiggins.okhttp3.R;
import com.wiggins.okhttp3.adapter.QueryDetailPicAdapter;
import com.wiggins.okhttp3.base.BaseActivity;
import com.wiggins.okhttp3.bean.QueryDetailPicUrl;
import com.wiggins.okhttp3.bean.TodayHistoryQueryDetail;
import com.wiggins.okhttp3.http.HttpCallback;
import com.wiggins.okhttp3.http.OkHttpUtils;
import com.wiggins.okhttp3.http.ResultDesc;
import com.wiggins.okhttp3.utils.Constant;
import com.wiggins.okhttp3.utils.DialogUtil;
import com.wiggins.okhttp3.utils.ToastUtil;
import com.wiggins.okhttp3.utils.UIUtils;
import com.wiggins.okhttp3.widget.TitleView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 事件列表详情
 * @Author 一花一世界
 */

public class TodayHistoryDetailActivity extends BaseActivity {

    private TodayHistoryDetailActivity mActivity = null;
    private TitleView titleView;
    private TextView mTvDetailTitle;
    private TextView mTvDetailContent;
    private TextView mTvEmpty;
    private ListView mLvData;

    private List<TodayHistoryQueryDetail> todayHistoryQueryDetail;
    private QueryDetailPicAdapter queryDetailPicAdapter;
    private Gson gson = null;
    private String e_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_history_detail);
        mActivity = this;

        initView();
        initData();
    }

    private void initView() {
        titleView = (TitleView) findViewById(R.id.titleView);
        titleView.setAppTitle(UIUtils.getString(R.string.event_list_details));
        titleView.setLeftImgOnClickListener();
        mTvDetailTitle = (TextView) findViewById(R.id.tv_detail_title);
        mTvDetailContent = (TextView) findViewById(R.id.tv_detail_content);
        mTvDetailContent.setMovementMethod(ScrollingMovementMethod.getInstance());
        mTvEmpty = (TextView) findViewById(R.id.tv_empty);
        mLvData = (ListView) findViewById(R.id.lv_data);
        mLvData.setEmptyView(mTvEmpty);
    }

    private void initData() {
        if (gson == null) {
            gson = new Gson();
        }
        if (todayHistoryQueryDetail == null) {
            todayHistoryQueryDetail = new ArrayList<>();
        }
        if (queryDetailPicAdapter == null) {
            queryDetailPicAdapter = new QueryDetailPicAdapter(todayHistoryQueryDetail.size() > 0 ? todayHistoryQueryDetail.get(0).getPicUrl() : new ArrayList<QueryDetailPicUrl>(), mActivity);
            mLvData.setAdapter(queryDetailPicAdapter);
        } else {
            queryDetailPicAdapter.notifyDataSetChanged();
        }

        Intent intent = getIntent();
        if (intent != null) {
            e_id = intent.getStringExtra("e_id");
        }

        getTodayHistoryQueryDetail();
    }

    /**
     * @Description 历史上的今天 事件列表详情
     */
    private void getTodayHistoryQueryDetail() {
        DialogUtil.showDialogLoading(mActivity, "");
        Map<String, String> mMap = new HashMap<>();
        mMap.put("key", Constant.APP_KEY);
        mMap.put("e_id", e_id);
        OkHttpUtils.postAsyn(Constant.queryDetail, mMap, new HttpCallback() {
            @Override
            public void onSuccess(ResultDesc resultDesc) {
                super.onSuccess(resultDesc);
                DialogUtil.hideDialogLoading();
                if (resultDesc.getError_code() == 0) {
                    try {
                        JSONArray jsonArray = new JSONArray(resultDesc.getResult());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            TodayHistoryQueryDetail bean = gson.fromJson(jsonObject.toString(), TodayHistoryQueryDetail.class);
                            todayHistoryQueryDetail.add(bean);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    mTvDetailTitle.setText(todayHistoryQueryDetail.get(0).getTitle().trim());
                    mTvDetailContent.setText(todayHistoryQueryDetail.get(0).getContent().trim());
                    queryDetailPicAdapter.setData(todayHistoryQueryDetail.get(0).getPicUrl());

                    Log.e(Constant.LOG_TAG, "历史上的今天 - 事件详情:" + todayHistoryQueryDetail.toString());
                    Log.e(Constant.LOG_TAG, "历史上的今天 - 事件详情 - 图片详情:" + todayHistoryQueryDetail.get(0).getPicUrl().toString());
                } else {
                    ToastUtil.showText(resultDesc.getReason());
                }
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
                DialogUtil.hideDialogLoading();
                ToastUtil.showText(message);
            }
        });
    }
}

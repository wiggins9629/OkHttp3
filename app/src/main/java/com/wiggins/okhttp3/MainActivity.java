package com.wiggins.okhttp3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wiggins.okhttp3.adapter.TodayHistoryQueryAdapter;
import com.wiggins.okhttp3.base.BaseActivity;
import com.wiggins.okhttp3.bean.TodayHistoryQuery;
import com.wiggins.okhttp3.cookies.CookiesManager;
import com.wiggins.okhttp3.http.HttpCallback;
import com.wiggins.okhttp3.http.OkHttpUtils;
import com.wiggins.okhttp3.http.ResultDesc;
import com.wiggins.okhttp3.utils.Constant;
import com.wiggins.okhttp3.utils.DialogUtil;
import com.wiggins.okhttp3.utils.StringUtil;
import com.wiggins.okhttp3.utils.ToastUtil;
import com.wiggins.okhttp3.utils.UIUtils;
import com.wiggins.okhttp3.view.TodayHistoryDetailActivity;
import com.wiggins.okhttp3.widget.TitleView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.CookieJar;

/**
 * @Description OkHttp3使用详解
 * @Author 一花一世界
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {

    private MainActivity mActivity = null;
    private TitleView titleView;
    private EditText mEdtData;
    private Button mBtnQuery;
    private TextView mTvEmpty;
    private ListView mLvData;

    private List<TodayHistoryQuery> todayHistoryQuery;
    private TodayHistoryQueryAdapter todayHistoryQueryAdapter;
    private Gson gson = null;
    private String data = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivity = this;

        initView();
        initData();
        setLinstener();
    }

    private void initData() {
        if (gson == null) {
            gson = new Gson();
        }
        if (todayHistoryQuery == null) {
            todayHistoryQuery = new ArrayList<>();
        }
        if (todayHistoryQueryAdapter == null) {
            todayHistoryQueryAdapter = new TodayHistoryQueryAdapter(todayHistoryQuery, mActivity);
            mLvData.setAdapter(todayHistoryQueryAdapter);
        } else {
            todayHistoryQueryAdapter.notifyDataSetChanged();
        }
    }

    private void initView() {
        titleView = (TitleView) findViewById(R.id.titleView);
        titleView.setAppTitle(UIUtils.getString(R.string.event_list));
        titleView.setLeftImageVisibility(View.GONE);
        mEdtData = (EditText) findViewById(R.id.edt_data);
        mBtnQuery = (Button) findViewById(R.id.btn_query);
        mTvEmpty = (TextView) findViewById(R.id.tv_empty);
        mLvData = (ListView) findViewById(R.id.lv_data);
        mLvData.setEmptyView(mTvEmpty);
    }

    private void setLinstener() {
        mBtnQuery.setOnClickListener(this);
        mLvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(mActivity, TodayHistoryDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("e_id", String.valueOf(todayHistoryQuery.get(position).getE_id()));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    /**
     * @Description 历史上的今天 事件列表
     */
    private void getTodayHistoryQuery() {
        DialogUtil.showDialogLoading(mActivity, "");
        Map<String, String> mMap = new HashMap<>();
        mMap.put("key", Constant.APP_KEY);
        mMap.put("date", data);
        OkHttpUtils.getAsyn(Constant.queryEvent, mMap, new HttpCallback() {
            @Override
            public void onSuccess(ResultDesc resultDesc) {
                super.onSuccess(resultDesc);
                DialogUtil.hideDialogLoading();
                todayHistoryQuery.clear();
                if (resultDesc.getError_code() == 0) {
                    try {
                        JSONArray jsonArray = new JSONArray(resultDesc.getResult());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            TodayHistoryQuery bean = gson.fromJson(jsonObject.toString(), TodayHistoryQuery.class);
                            todayHistoryQuery.add(bean);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    todayHistoryQueryAdapter.setData(todayHistoryQuery);
                    Log.e(Constant.LOG_TAG, "历史上的今天 - 事件列表:" + todayHistoryQuery.toString());
                } else {
                    todayHistoryQueryAdapter.setData(todayHistoryQuery);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_query:
                data = mEdtData.getText().toString().trim();
                if (StringUtil.isEmpty(data)) {
                    ToastUtil.showText(UIUtils.getString(R.string.query_date_not_empty));
                    return;
                }
                getTodayHistoryQuery();
                break;
        }
    }

    public void clearCookie() {
        CookieJar cookieJar = OkHttpUtils.getInstance().getOkHttpClient().cookieJar();
        if (cookieJar instanceof CookiesManager) {
            ((CookiesManager) cookieJar).getCookieStore().removeAll();
        }
    }
}

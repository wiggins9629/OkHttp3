package com.wiggins.okhttp3.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wiggins.okhttp3.R;
import com.wiggins.okhttp3.bean.QueryDetailPicUrl;
import com.wiggins.okhttp3.http.HttpCallback;
import com.wiggins.okhttp3.http.OkHttpUtils;
import com.wiggins.okhttp3.utils.StringUtil;

import java.util.List;

/**
 * @Description 历史上的今天 - 事件详情图片列表适配器
 * @Author 一花一世界
 */
public class QueryDetailPicAdapter extends BaseAdapter {

    private List<QueryDetailPicUrl> data;
    private LayoutInflater inflater;

    public QueryDetailPicAdapter(List<QueryDetailPicUrl> data, Context context) {
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }

    public void setData(List<QueryDetailPicUrl> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.adapter_today_history_detail, null);
            holder = new ViewHolder();
            holder.mIvUrl = (ImageView) convertView.findViewById(R.id.iv_url);
            holder.mTvTitle = (TextView) convertView.findViewById(R.id.tv_title);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        QueryDetailPicUrl item = data.get(position);
        OkHttpUtils.displayAsynImage(item.getUrl(), new HttpCallback() {
            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
                holder.mIvUrl.setImageResource(R.drawable.image_error);
            }

            @Override
            public void onBitmapSuccess(Bitmap bitmap) {
                super.onBitmapSuccess(bitmap);
                holder.mIvUrl.setImageBitmap(bitmap);
            }
        });
        if (StringUtil.isEmpty(item.getPic_title())) {
            holder.mTvTitle.setVisibility(View.GONE);
        } else {
            holder.mTvTitle.setVisibility(View.VISIBLE);
            holder.mTvTitle.setText(item.getPic_title());
        }
        return convertView;
    }

    private class ViewHolder {
        private ImageView mIvUrl;
        private TextView mTvTitle;
    }
}

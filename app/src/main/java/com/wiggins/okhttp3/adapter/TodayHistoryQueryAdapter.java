package com.wiggins.okhttp3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wiggins.okhttp3.R;
import com.wiggins.okhttp3.bean.TodayHistoryQuery;

import java.util.List;

/**
 * @Description 历史上的今天 - 事件列表适配器
 * @Author 一花一世界
 */
public class TodayHistoryQueryAdapter extends BaseAdapter {

    private List<TodayHistoryQuery> data;
    private LayoutInflater inflater;

    public TodayHistoryQueryAdapter(List<TodayHistoryQuery> data, Context context) {
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }

    public void setData(List<TodayHistoryQuery> data) {
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.adapter_today_history_query, null);
            holder = new ViewHolder();

            holder.mTvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            holder.mTvDate = (TextView) convertView.findViewById(R.id.tv_date);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        TodayHistoryQuery item = data.get(position);
        holder.mTvTitle.setText(item.getTitle());
        holder.mTvDate.setText(item.getDate());
        return convertView;
    }

    private class ViewHolder {
        private TextView mTvTitle;
        private TextView mTvDate;
    }
}

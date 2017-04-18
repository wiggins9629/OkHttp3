package com.wiggins.okhttp3.bean;

/**
 * @Description 历史上的今天 - 事件列表(v2.0推荐)
 * @Author 一花一世界
 */

public class TodayHistoryQuery {

    private String day;//日期
    private String date;//	事件日期
    private int e_id;//	事件id
    private String title;//	事件标题

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getE_id() {
        return e_id;
    }

    public void setE_id(int e_id) {
        this.e_id = e_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "TodayHistoryQuery{" +
                "day='" + day + '\'' +
                ", date='" + date + '\'' +
                ", e_id=" + e_id +
                ", title='" + title + '\'' +
                '}';
    }
}

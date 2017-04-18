package com.wiggins.okhttp3.bean;

import java.util.List;

/**
 * @Description 历史上的今天 - 事件详情(v2.0推荐)
 * @Author 一花一世界
 */

public class TodayHistoryQueryDetail {

    private String e_id;//事件id
    private String title;//事件标题
    private String content;//事件详情
    private String picNo;//图片数量
    private List<QueryDetailPicUrl> picUrl;

    public String getE_id() {
        return e_id;
    }

    public void setE_id(String e_id) {
        this.e_id = e_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPicNo() {
        return picNo;
    }

    public void setPicNo(String picNo) {
        this.picNo = picNo;
    }

    public List<QueryDetailPicUrl> getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(List<QueryDetailPicUrl> picUrl) {
        this.picUrl = picUrl;
    }

    @Override
    public String toString() {
        return "TodayHistoryQueryDetail{" +
                "e_id='" + e_id + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", picNo='" + picNo + '\'' +
                ", picUrl=" + picUrl +
                '}';
    }
}

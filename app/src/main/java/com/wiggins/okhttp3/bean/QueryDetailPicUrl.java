package com.wiggins.okhttp3.bean;

/**
 * @Description 历史上的今天 - 事件详情(v2.0推荐) - 图片列表
 * @Author 一花一世界
 */
public class QueryDetailPicUrl {

    private String pic_title;//图片标题
    private String url;//图片地址
    private int id;//图片顺序id

    public String getPic_title() {
        return pic_title;
    }

    public void setPic_title(String pic_title) {
        this.pic_title = pic_title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "QueryDetailPicUrl{" +
                "pic_title='" + pic_title + '\'' +
                ", url='" + url + '\'' +
                ", id=" + id +
                '}';
    }
}

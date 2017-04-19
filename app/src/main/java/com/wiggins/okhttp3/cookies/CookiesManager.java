package com.wiggins.okhttp3.cookies;

import com.wiggins.okhttp3.app.MyApplication;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * @Description 自动管理Cookies
 * 使用：new OkHttpClient.Builder().cookieJar(new CookiesManager());
 * form http://www.qingpingshan.com/rjbc/az/110232.html
 * @Author 一花一世界
 */
public class CookiesManager implements CookieJar {

    private final PersistentCookieStore cookieStore = new PersistentCookieStore(MyApplication.getContext());

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookies != null && cookies.size() > 0) {
            for (Cookie item : cookies) {
                cookieStore.add(url, item);
            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = cookieStore.get(url);
        return cookies;
    }

    public CookieStore getCookieStore() {
        return cookieStore;
    }
}

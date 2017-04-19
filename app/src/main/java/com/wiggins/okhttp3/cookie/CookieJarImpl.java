package com.wiggins.okhttp3.cookie;

import com.wiggins.okhttp3.cookie.store.CookieStore;
import com.wiggins.okhttp3.utils.Exceptions;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * 参考android-async-http库写一个
 * <p>
 * 参考两个类，
 * 一个是 PersistentCookieStore.java (https://github.com/loopj/android-async-http/blob/master/library/src/main/java/com/loopj/android/http/PersistentCookieStore.java)，
 * 另一个是 SerializableCookie.java (https://github.com/loopj/android-async-http/blob/master/library/src/main/java/com/loopj/android/http/SerializableCookie.java)
 * <p>
 * 参考：OkHttp3实现Cookies管理及持久化
 * 地址：http://www.codeceo.com/article/okhttp3-cookies-manage.html
 */
public class CookieJarImpl implements CookieJar {
    private CookieStore cookieStore;

    public CookieJarImpl(CookieStore cookieStore) {
        if (cookieStore == null) Exceptions.illegalArgument("cookieStore can not be null.");
        this.cookieStore = cookieStore;
    }

    @Override
    public synchronized void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        cookieStore.add(url, cookies);
    }

    @Override
    public synchronized List<Cookie> loadForRequest(HttpUrl url) {
        return cookieStore.get(url);
    }

    public CookieStore getCookieStore() {
        return cookieStore;
    }
}

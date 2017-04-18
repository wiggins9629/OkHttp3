package com.wiggins.okhttp3.http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.wiggins.okhttp3.R;
import com.wiggins.okhttp3.utils.UIUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.BufferedSink;
import okio.ByteString;

/**
 * @Description OkHttp请求
 * @Author 一花一世界
 */
public class OkHttpRequest {

    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");//JSON数据格式
    private static final MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream");//二进制流数据
    private static final MediaType MEDIA_TYPE_TEXT = MediaType.parse("text/plain");//纯文本格式

    private static OkHttpClient getOkHttpClient() {
        return OkHttpUtils.getInstance().getOkHttpClient();
    }

    /**
     * @Description 请求方式
     */
    enum HttpMethodType {
        GET,
        POST,
    }

    /**--------------------    GET请求参数拼接    --------------------**/

    /**
     * @param url    请求地址
     * @param params 请求参数
     * @Description GET请求参数拼接
     */
    public static String appendGetParams(String url, Map<String, String> params) {
        if (url == null || params == null || params.isEmpty()) {
            return url;
        }

        Uri.Builder builder = Uri.parse(url).buildUpon();
        Set<String> keys = params.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            builder.appendQueryParameter(key, params.get(key));
        }

        return builder.build().toString();
    }

    /**--------------------    同步数据请求    --------------------**/

    /**
     * @param request  Request对象
     * @param callback 请求回调
     * @Description 同步数据请求
     */
    public static void doExecute(final Request request, HttpCallback callback) {
        try {
            Response response = getOkHttpClient().newCall(request).execute();
            if (response.isSuccessful()) {
                OkHttpUtils.sendSuccessResultCallback(DataAnalysis.getReturnData(response.body().string()), callback);
            } else {
                OkHttpUtils.sendFailResultCallback(response.code(), response.message(), callback);
            }
        } catch (Exception e) {
            OkHttpUtils.sendFailResultCallback(-1, e.getMessage(), callback);
        }
    }

    /**--------------------    异步数据请求    --------------------**/

    /**
     * @param methodType 请求方式
     * @param url        请求地址
     * @param params     请求参数
     * @param json       json数据格式
     * @Description Request对象
     */
    public static Request builderRequest(HttpMethodType methodType, String url, Map<String, String> params, String json) {
        Request.Builder builder = new Request.Builder()
                .url(url);

        if (methodType == HttpMethodType.POST) {
            if (json != null) {
                RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, json);
                builder.post(body);
            } else {
                RequestBody body = builderFormData(params);
                builder.post(body);
            }
        } else if (methodType == HttpMethodType.GET) {
            builder.get();
        }

        return builder.build();
    }

    /**
     * @param params 请求参数
     * @Description RequestBody对象
     */
    private static RequestBody builderFormData(Map<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();

        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                builder.add(key, params.get(key));
            }
        }

        return builder.build();
    }

    /**
     * @param request  Request对象
     * @param callback 请求回调
     * @Description 异步请求
     */
    public static void doEnqueue(final Request request, final HttpCallback callback) {
        getOkHttpClient().newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                OkHttpUtils.sendFailResultCallback(-1, e.getMessage(), callback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        OkHttpUtils.sendSuccessResultCallback(DataAnalysis.getReturnData(response.body().string()), callback);
                    } else {
                        OkHttpUtils.sendFailResultCallback(response.code(), response.message(), callback);
                    }
                } catch (Exception e) {
                    OkHttpUtils.sendFailResultCallback(-1, e.getMessage(), callback);
                }
            }
        });
    }

    /**--------------------    异步文件上传    --------------------**/

    /**
     * @param url      请求地址
     * @param file     上传文件
     * @param pic_key  上传图片关键字
     * @param files    上传文件集合
     * @param params   请求参数
     * @param callback 请求回调
     * @Description Request对象
     */
    public static Request builderFileRequest(String url, File file, String pic_key, List<File> files, Map<String, String> params, HttpCallback callback) {
        Request.Builder builder = new Request.Builder()
                .url(url);

        if (file != null) {
            RequestBody body = builderFileFormData(file, null, null, null, callback);
            builder.post(body);
        } else {
            RequestBody body = builderFileFormData(null, pic_key, files, params, callback);
            builder.post(body);
        }

        return builder.build();
    }

    /**
     * @param file     上传文件
     * @param pic_key  上传图片关键字
     * @param files    上传文件集合
     * @param params   请求参数
     * @param callback 请求回调
     * @Description RequestBody对象
     */
    private static RequestBody builderFileFormData(File file, String pic_key, List<File> files, Map<String, String> params, final HttpCallback callback) {
        CountingRequestBody countingRequestBody = null;

        if (file != null) {
            RequestBody requestBody = RequestBody.create(MEDIA_TYPE_STREAM, file);
            countingRequestBody = new CountingRequestBody(requestBody, new CountingRequestBody.Listener() {
                @Override
                public void onRequestProgress(long byteWritted, long contentLength) {
                    OkHttpUtils.sendProgressResultCallback(byteWritted, contentLength, callback);
                }
            });
        } else {
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM);//设置为表单类型，如果提交的是表单一定要设置这句

            if (params != null && !params.isEmpty()) {
                for (String key : params.keySet()) {
                    builder.addFormDataPart(key, params.get(key));
                }
            }

            if (files != null && !files.isEmpty()) {
                for (File mFile : files) {
                    RequestBody fileBody = RequestBody.create(MediaType.parse(guessMimeType(mFile.getName())), mFile);
                    builder.addFormDataPart(pic_key, mFile.getName(), fileBody);
                }
            }

            countingRequestBody = new CountingRequestBody(builder.build(), new CountingRequestBody.Listener() {
                @Override
                public void onRequestProgress(long byteWritted, long contentLength) {
                    OkHttpUtils.sendProgressResultCallback(byteWritted, contentLength, callback);
                }
            });
        }

        return countingRequestBody;
    }

    /**
     * @param filename 文件名
     * @Description 获取MediaType
     */
    private static String guessMimeType(String filename) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = null;
        try {
            contentTypeFor = fileNameMap.getContentTypeFor(URLEncoder.encode(filename, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (contentTypeFor == null) {
            contentTypeFor = "image/png";
        }
        return contentTypeFor;
    }

    /**--------------------    异步文件下载    --------------------**/

    /**
     * @param request      Request对象
     * @param destFileDir  目标文件存储的文件目录
     * @param destFileName 目标文件存储的文件名
     * @param callback     请求回调
     * @Description 异步下载请求
     */
    public static void doDownloadEnqueue(final Request request, final String destFileDir, final String destFileName, final HttpCallback callback) {
        getOkHttpClient().newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                OkHttpUtils.sendFailResultCallback(-1, e.getMessage(), callback);
            }

            @Override
            public void onResponse(Call call, Response response) {
                InputStream inputStream = response.body().byteStream();
                FileOutputStream fileOutputStream = null;
                try {
                    byte[] buffer = new byte[2048];
                    int len;
                    long currentTotalLen = 0L;
                    long totalLen = response.body().contentLength();

                    File dir = new File(destFileDir);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    File file = new File(dir, destFileName);
                    if (file.exists()) {
                        //如果文件存在则删除
                        file.delete();
                    }
                    fileOutputStream = new FileOutputStream(file);

                    while ((len = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, len);
                        currentTotalLen += len;
                        OkHttpUtils.sendProgressResultCallback(currentTotalLen, totalLen, callback);
                    }
                    fileOutputStream.flush();
                    OkHttpUtils.sendSuccessResultCallback(new ResultDesc(0, UIUtils.getString(R.string.download_success), file.getAbsolutePath()), callback);
                } catch (IOException e) {
                    e.printStackTrace();
                    OkHttpUtils.sendFailResultCallback(-1, e.getMessage(), callback);
                } finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    /**--------------------    异步图片显示    --------------------**/

    /**
     * @param request  Request对象
     * @param callback 请求回调
     * @Description 异步图片显示请求
     */
    public static void doDisplayEnqueue(final Request request, final HttpCallback callback) {
        getOkHttpClient().newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                OkHttpUtils.sendFailResultCallback(-1, e.getMessage(), callback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    InputStream inputStream = response.body().byteStream();
                    final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    OkHttpUtils.sendBitmapSuccessResultCallback(bitmap, callback);
                    inputStream.close();
                } else {
                    OkHttpUtils.sendFailResultCallback(response.code(), response.message(), callback);
                }
            }
        });
    }

    /**--------------------    异步流式提交    --------------------**/

    /**
     * @param url     请求地址
     * @param content 提交内容
     * @Description Request对象
     */
    public static Request builderStreamRequest(String url, final String content) {
        Request.Builder builder = new Request.Builder()
                .url(url);

        RequestBody body = new RequestBody() {
            @Override
            public MediaType contentType() {
                return MEDIA_TYPE_TEXT;
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                sink.writeUtf8(content);
            }

            @Override
            public long contentLength() throws IOException {
                return content.length();
            }
        };
        builder.post(body);

        return builder.build();
    }

    /**--------------------    Websocket    --------------------**/

    /**
     * @param request Request对象
     * @Description Websocket连接
     */
    public static void doNewWebSocket(final Request request) {
        getOkHttpClient().newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
                //连接成功
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);
                //当收到文本消息
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                super.onMessage(webSocket, bytes);
                //收到字节消息，可转换为文本
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                super.onClosing(webSocket, code, reason);
                //当不再传进来消息时调用
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
                //连接被关闭
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                super.onFailure(webSocket, t, response);
                //连接失败
            }
        });
    }

    /**
     * @param builder Request.Builder
     * @param name    名称
     * @param value   值
     * @Description 添加单个头部信息
     */
    public static Request.Builder appendHeader(Request.Builder builder, String name, String value) {
        builder.header(name, value);
        return builder;
    }

    /**
     * @param builder Request.Builder
     * @param headers 头部参数
     * @Description 添加多个头部信息
     */
    public static Request.Builder appendHeaders(Request.Builder builder, Map<String, String> headers) {
        Headers.Builder headerBuilder = new Headers.Builder();
        if (headers == null || headers.isEmpty()) {
            return builder;
        }
        for (String key : headers.keySet()) {
            headerBuilder.add(key, headers.get(key));
        }
        builder.headers(headerBuilder.build());
        return builder;
    }
}

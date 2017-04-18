package com.wiggins.okhttp3.http;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * @Description 文件上传进度RequestBody
 * @Author 一花一世界
 */
public class CountingRequestBody extends RequestBody {

    private RequestBody delegate;
    private Listener listener;
    private CountingSink countingSink;

    /**
     * @Description 构造函数初始化成员变量
     */
    public CountingRequestBody(RequestBody delegate, Listener listener) {
        this.delegate = delegate;
        this.listener = listener;
    }

    @Override
    public MediaType contentType() {
        return delegate.contentType();
    }

    /**
     * @Description 返回文件总的字节大小，如果文件大小获取失败则返回-1
     */
    @Override
    public long contentLength() {
        try {
            return delegate.contentLength();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        countingSink = new CountingSink(sink);
        //将CountingSink转化为BufferedSink供writeTo()使用
        BufferedSink bufferedSink = Okio.buffer(countingSink);
        delegate.writeTo(bufferedSink);
        bufferedSink.flush();
    }

    protected final class CountingSink extends ForwardingSink {

        private long byteWritten = 0;

        public CountingSink(Sink delegate) {
            super(delegate);
        }

        /**
         * @Description 上传时调用该方法，在其中调用回调函数将上传进度暴露出去，该方法提供了缓冲区的自己大小
         */
        @Override
        public void write(Buffer source, long byteCount) throws IOException {
            super.write(source, byteCount);
            byteWritten += byteCount;
            listener.onRequestProgress(byteWritten, contentLength());
        }
    }

    /**
     * @Description 上传进度回调监听接口
     */
    public interface Listener {

        /**
         * @param byteWritted   已经上传的字节大小
         * @param contentLength 文件的总字节大小
         */
        void onRequestProgress(long byteWritted, long contentLength);
    }
}

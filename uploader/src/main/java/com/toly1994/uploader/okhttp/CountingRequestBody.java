package com.toly1994.uploader.okhttp;

import android.support.annotation.Nullable;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/10/16 0016:13:44<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：
 */
public class CountingRequestBody extends RequestBody {
    protected RequestBody delegate;


    private Listener mListener;
    private CountingSink mCountingSink;

    public CountingRequestBody(RequestBody delegate, Listener listener) {
        this.delegate = delegate;
        mListener = listener;
    }

    protected final class CountingSink extends ForwardingSink{
        private long byteWritten;
        public CountingSink(Sink delegate) {
            super(delegate);
        }

        @Override
        public void write(Buffer source, long byteCount) throws IOException {
            super.write(source, byteCount);
            byteWritten += byteCount;
            mListener.onReqProgress(byteWritten, contentLength());
        }
    }

    public static interface Listener {
        void onReqProgress(long byteWritten, long contentLength);
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return delegate.contentType();
    }

    @Override
    public long contentLength()  {
        try {
            return delegate.contentLength();
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        mCountingSink = new CountingSink(sink);
        BufferedSink buffer = Okio.buffer(mCountingSink);
        delegate.writeTo(buffer);
        buffer.flush();
    }
}

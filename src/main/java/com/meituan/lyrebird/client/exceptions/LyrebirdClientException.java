package com.meituan.lyrebird.client.exceptions;

public class LyrebirdClientException extends Exception{
    public LyrebirdClientException() {
        super();
    }

    public LyrebirdClientException(String message) {
        super(message);
    }

    public LyrebirdClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public LyrebirdClientException(Throwable cause) {
        super(cause);
    }

    protected LyrebirdClientException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

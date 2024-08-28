package com.trionesdev.csi.api.ses;

public class SesException extends RuntimeException {
    public SesException() {
    }

    public SesException(String message) {
        super(message);
    }

    public SesException(String message, Throwable cause) {
        super(message, cause);
    }

    public SesException(Throwable cause) {
        super(cause);
    }

    public SesException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

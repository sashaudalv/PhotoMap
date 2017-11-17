package com.alexdev.photomap.utils.exceptions;


public class ThisDrawableNotSupportedException extends Exception {
    public ThisDrawableNotSupportedException() {
        super();
    }

    public ThisDrawableNotSupportedException(String message) {
        super(message);
    }

    public ThisDrawableNotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ThisDrawableNotSupportedException(Throwable cause) {
        super(cause);
    }
}

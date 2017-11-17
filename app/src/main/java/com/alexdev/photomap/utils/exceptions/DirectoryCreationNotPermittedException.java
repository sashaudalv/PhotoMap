package com.alexdev.photomap.utils.exceptions;


public class DirectoryCreationNotPermittedException extends Exception {
    public DirectoryCreationNotPermittedException() {
        super();
    }

    public DirectoryCreationNotPermittedException(String message) {
        super(message);
    }

    public DirectoryCreationNotPermittedException(String message, Throwable cause) {
        super(message, cause);
    }

    public DirectoryCreationNotPermittedException(Throwable cause) {
        super(cause);
    }
}

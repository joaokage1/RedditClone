package com.joao.reddit.clone.exceptions;

public class SpringRedditException extends RuntimeException {
    public SpringRedditException(String exMessage) {
        super(exMessage);
    }

    public SpringRedditException(String exMessage, Exception e ) {
        super(exMessage + e.getMessage());
    }
}

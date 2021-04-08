package com.dryxtech.software.sample.exception;

import java.util.Map;

public class InvalidSearchException extends ContextualRuntimeException {

    public InvalidSearchException(String message, Map<String, Object> context) {
        super(message, context);
    }

    public InvalidSearchException(String message, Map<String, Object> context, Throwable cause) {
        super(message, context, cause);
    }
}

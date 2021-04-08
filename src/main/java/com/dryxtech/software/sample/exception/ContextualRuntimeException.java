package com.dryxtech.software.sample.exception;

import java.util.Map;
import java.util.Optional;

/**
 * Runtime Exception that includes context information stored in a map
 */
public class ContextualRuntimeException extends RuntimeException {

    private final transient Map<String, Object> context;

    public ContextualRuntimeException(String message, Map<String, Object> context) {
        super(message, null);
        this.context = context;
    }

    public ContextualRuntimeException(String message, Map<String, Object> context, Throwable cause) {
        super(message, cause);
        this.context = context;
    }

    public String getMessageWithContext() {
        return getContext().map(map -> getMessage() + " context: " + map.toString())
                .orElseGet(this::getMessage);
    }

    public Optional<Map<String, Object>> getContext() {
        return Optional.ofNullable(this.context);
    }
}

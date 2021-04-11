package com.dryxtech.software.sample.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class ContextualRuntimeExceptionTest {

    private ContextualRuntimeException exception;

    @BeforeEach
    void setUp() {
        this.exception = new ContextualRuntimeException("test", Collections.singletonMap("foo", "bar"));
    }

    @Test
    void getMessageWithContext() {
        assertEquals("test [context {foo=bar}]", exception.getMessageWithContext());
    }

    @Test
    void getContext() {
        assertEquals(Collections.singletonMap("foo", "bar"), exception.getContext().get());
    }
}

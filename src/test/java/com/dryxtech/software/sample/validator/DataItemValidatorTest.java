package com.dryxtech.software.sample.validator;

import com.dryxtech.software.sample.model.DataItem;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DataItemValidatorTest {

    private DataItemValidator validator;

    @BeforeEach
    void setUp() {
        validator = new DataItemValidator();
    }

    @Test
    void validate_emptyName() {
        Errors errors = validate("", "123");
        assertTrue(errors.hasErrors());
    }

    @Test
    void validate_tooLongName() {
        Errors errors = validate("ABCDE", "123");
        assertTrue(errors.hasErrors());
    }

    @Test
    void validate_invalidContentName() {
        Errors errors = validate("ABC1", "123");
        assertTrue(errors.hasErrors());
    }

    @Test
    void validate_emptyCode() {
        Errors errors = validate("ABC", "");
        assertTrue(errors.hasErrors());
    }

    @Test
    void validate_tooLongCode() {
        Errors errors = validate("ABC", "01234567891");
        assertTrue(errors.hasErrors());
    }

    @Test
    void validate_invalidContentCode() {
        Errors errors = validate("ABC", "123X");
        assertTrue(errors.hasErrors());
    }

    @Test
    void validate_validItems() {
        for (int i=2; i<=4; i++) {
            String name = generateString(i, 'A');
            for (int j=1; j<=10; j++) {
                String code = generateString(j, '1');
                Errors errors = validate(name, code);
                assertFalse(errors.hasErrors());
            }
        }
    }

    private String generateString(int size, char c) {
        return StringUtils.leftPad("", size, c);
    }

    private Errors validate(String name, String code) {
        DataItem item = new DataItem(name, code);
        Errors errors = new MapBindingResult(new HashMap<String, Object>(), item.toString());
        validator.validate(item, errors);
        return errors;
    }
}

package com.dryxtech.software.sample.validator;

import com.dryxtech.software.sample.model.DataItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.HashMap;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class DataItemValidatorTest {

    private Errors errors;

    @Autowired
    private DataItemValidator validator;

    @Autowired
    MessageSource messageSource;

    @Autowired
    ApplicationContext context;

    @BeforeEach
    void setUp() {
        this.errors = new MapBindingResult(new HashMap<String, Object>(), "test");
    }

    @Test
    void validate() {
        System.out.println(messageSource.getMessage("field.min.length", new Object[]{}, Locale.getDefault()));
        DataItem item = new DataItem("", "");
        validator.validate(item, errors);
        assertTrue(errors.hasErrors());
        errors.getAllErrors().forEach(e -> {

            String realMessage = context.getMessage(e, Locale.getDefault());
            System.out.println("realMessage = " + realMessage);
            System.out.println(e.toString());
        });
    }
}

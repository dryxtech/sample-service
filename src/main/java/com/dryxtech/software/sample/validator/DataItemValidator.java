package com.dryxtech.software.sample.validator;

import com.dryxtech.software.sample.model.DataItem;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

@Component
public class DataItemValidator implements Validator {

    public final int NAME_MIN_SIZE = 2;
    public final int NAME_MAX_SIZE = 4;
    public final int CODE_MIN_SIZE = 1;
    public final int CODE_MAX_SIZE = 10;
    private final Pattern NAME_PATTERN = Pattern.compile("[a-zA-Z]+");
    private final Pattern CODE_PATTERN = Pattern.compile("\\d+");

    @Override
    public boolean supports(Class clazz) {
        return DataItem.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors e) {

        DataItem item = (DataItem) obj;

        String name = item.getName().trim();
        String code = item.getCode().trim();

        if (name.length() < NAME_MIN_SIZE) {
            e.rejectValue("name", "field.min.length", new Object[]{NAME_MIN_SIZE}, "field.min.length violated");
        } else if (name.length() > NAME_MAX_SIZE) {
            e.rejectValue("name", "field.max.length", new Object[]{NAME_MAX_SIZE}, "field.max.length violated");
        }

        if (name.length() > 0 && !NAME_PATTERN.matcher(name).matches()) {
            e.rejectValue("name", "field.content.letters", new Object[]{},"field.content.letters violated");
        }

        if (code.length() < CODE_MIN_SIZE) {
            e.rejectValue("code", "field.min.length", new Object[]{CODE_MIN_SIZE}, "field.min.length violated");
        } else if (code.length() > CODE_MAX_SIZE) {
            e.rejectValue("code", "field.max.length", new Object[]{CODE_MAX_SIZE}, "field.max.length violated");
        }

        if (code.length() > 0 && !CODE_PATTERN.matcher(code).matches()) {
            e.rejectValue("code", "field.content.numbers", new Object[]{},"field.content.numbers violated");
        }
    }
}

package com.dryxtech.software.sample.validator;

import com.dryxtech.software.sample.model.DataItem;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

@Component
public class DataItemValidator implements Validator {

    private final Pattern namePattern = Pattern.compile("[a-zA-Z]+");
    private final Pattern codePattern = Pattern.compile("\\d+");

    @Override
    public boolean supports(Class clazz) {
        return DataItem.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors e) {

        DataItem item = (DataItem) obj;

        String name = item.getName().trim();
        String code = item.getCode().trim();

        if (name.length() < 2) {
            e.rejectValue("name", "", "name must be at least 2 characters long");
        } else if (name.length() > 4) {
            e.rejectValue("name", "", "name must be no more than 4 characters long");
        }

        if (!namePattern.matcher(name).matches()) {
            e.rejectValue("name", "", "name must be letters only");
        }

        if (code.length() < 1) {
            e.rejectValue("code", "", "code must be at least 1 characters long");
        } else if (code.length() > 10) {
            e.rejectValue("code", "", "code must be no more than 10 characters long");
        }

        if (!codePattern.matcher(code).matches()) {
            e.rejectValue("code", "", "code must be numeric only");
        }
    }
}

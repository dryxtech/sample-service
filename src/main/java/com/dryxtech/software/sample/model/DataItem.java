package com.dryxtech.software.sample.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode
public class DataItem {

    @NonNull
    private String name;
    @NonNull
    private String code;

    @Override
    public String toString() {
        return name + code;
    }
}

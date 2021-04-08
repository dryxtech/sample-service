package com.dryxtech.software.sample.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.time.LocalDateTime;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class DataFact {

    @NonNull
    private final int rank;
    @NonNull
    private final long id;
    @NonNull
    private final String organization;
    @NonNull
    private final String name;
    @NonNull
    private final String code;
    @NonNull
    private final Integer value;
    @NonNull
    private final LocalDateTime createdTimestamp;
}

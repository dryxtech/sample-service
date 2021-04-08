package com.dryxtech.software.sample.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode
public class DataRequest<T> {

    private List<T> searchCriteria;
    private String guid;
    private String organization;
    private String user;
    private String type;
    private int limit;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int responseCount;
    private int responseStatusCode;

    public DataRequest(String organization, String user, String type, int responseStatusCode) {
        this();
        this.organization = organization;
        this.user = user;
        this.type = type;
        this.responseStatusCode = responseStatusCode;
    }

    public DataRequest() {
        searchCriteria = new ArrayList<>();
        guid = UUID.randomUUID().toString();
        limit = Integer.MAX_VALUE;
        startTime = LocalDateTime.now();
        endTime = startTime;
        responseCount = 0;
        responseStatusCode = HttpStatus.OK.value();
    }

    @Override
    public String toString() {
        return "DataRequest{" +
                "guid='" + guid + '\'' +
                ", organization='" + organization + '\'' +
                ", user='" + user + '\'' +
                ", type='" + type + '\'' +
                ", limit=" + limit +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", responseCount=" + responseCount +
                ", responseStatusCode=" + responseStatusCode +
                '}';
    }
}

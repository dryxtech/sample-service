package com.dryxtech.software.sample.controller;

import com.dryxtech.software.sample.exception.InvalidSearchException;
import com.dryxtech.software.sample.model.ApiError;
import com.dryxtech.software.sample.model.DataFact;
import com.dryxtech.software.sample.model.DataItem;
import com.dryxtech.software.sample.model.DataRequest;
import com.dryxtech.software.sample.model.DataRequestEvent;
import com.dryxtech.software.sample.service.DataService;
import com.dryxtech.software.sample.util.DataItemUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/api/data")
public class DataController {

    private static final String DEFAULT_ORG = "anonymous";
    private static final String DEFAULT_USER = "anonymous";
    private static final String DEFAULT_DATA_ITEM_LIMIT = "3";

    private final DataService dataService;
    private final ApplicationEventPublisher eventPublisher;

    public DataController(DataService dataService, ApplicationEventPublisher eventPublisher) {
        this.dataService = dataService;
        this.eventPublisher = eventPublisher;
    }

    @GetMapping(value = "search")
    public ResponseEntity<Object> searchDataFacts(@RequestParam String searchCriteria,
                                                  @RequestParam(defaultValue = DEFAULT_ORG) String organization,
                                                  @RequestParam(defaultValue = DEFAULT_USER) String user,
                                                  @RequestParam(defaultValue = DEFAULT_DATA_ITEM_LIMIT) int itemLimit) {
        List<DataItem> searchDataItems;
        try {
            searchDataItems = DataItemUtil.convert(searchCriteria, DataService.MAX_REQUEST_ITEMS);
        } catch (IllegalArgumentException ex) {
            eventPublisher.publishEvent(new DataRequestEvent(new DataRequest<>(organization, user,
                    DataFact.class.getSimpleName(), HttpStatus.BAD_REQUEST.value())));
            throw new InvalidSearchException("invalid search criteria string: " + searchCriteria,
                    Collections.singletonMap("searchCriteria", searchCriteria), ex);
        }

        return searchDataFacts(searchDataItems, organization, user, itemLimit);
    }

    @PostMapping(value = "search")
    public ResponseEntity<Object> searchDataFacts(@RequestBody List<DataItem> searchDataItems,
                                                  @RequestParam(defaultValue = DEFAULT_ORG) String organization,
                                                  @RequestParam(defaultValue = DEFAULT_USER) String user,
                                                  @RequestParam(defaultValue = DEFAULT_DATA_ITEM_LIMIT) int itemLimit) {

        DataRequest<DataItem> dataRequest = new DataRequest<>(organization, user, DataFact.class.getSimpleName(),
                HttpStatus.OK.value());
        dataRequest.setSearchCriteria(searchDataItems);
        dataRequest.setLimit(itemLimit);

        try {
            dataService.validate(dataRequest);

            Map<String, List<DataFact>> dataFacts = dataService.getDataFacts(dataRequest);

            return ResponseEntity.ok(dataFacts);

        } catch (DataAccessException ex) {
            log.error("failed to process request " + dataRequest.getGuid(), ex);
            dataRequest.setResponseStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR,
                            "failed to process request " + dataRequest.getGuid(), ex));
        } finally {
            dataRequest.setEndTime(LocalDateTime.now());
            eventPublisher.publishEvent(new DataRequestEvent(dataRequest));
        }
    }
}

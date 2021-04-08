package com.dryxtech.software.sample.service;

import com.dryxtech.software.sample.dao.DataFactDAO;
import com.dryxtech.software.sample.dao.DataItemDAO;
import com.dryxtech.software.sample.exception.InvalidSearchException;
import com.dryxtech.software.sample.model.DataFact;
import com.dryxtech.software.sample.model.DataItem;
import com.dryxtech.software.sample.model.DataRequest;
import com.dryxtech.software.sample.util.DataItemUtil;
import com.dryxtech.software.sample.validator.DataItemValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class DataService {

    public static final int MAX_REQUEST_ITEMS = 10000;

    private final DataFactDAO dataFactDAO;
    private final DataItemDAO dataItemDAO;
    private final Validator dataItemValidator;

    public DataService(DataFactDAO dataFactDAO, DataItemDAO dataItemDAO, DataItemValidator validator) {
        this.dataFactDAO = dataFactDAO;
        this.dataItemDAO = dataItemDAO;
        this.dataItemValidator = validator;
    }

    public Map<String, List<DataFact>> getDataFacts(DataRequest<DataItem> dataRequest) {

        log.info("getting data facts for organization: {} in request: {}",
                dataRequest.getOrganization(), dataRequest.getGuid());

        LinkedHashMap<String, List<DataFact>> result = new LinkedHashMap<>();

        // remove duplicates and normalize case and padding
        List<DataItem> dataItems = DataItemUtil.normalize(dataRequest.getSearchCriteria());

        // reduce to valid data items only
        dataItems = dataItemDAO.getValidDataItems(dataItems);

        // create/ensure empty entry for each valid data item
        dataItems.forEach(item -> result.put(item.toString(), new ArrayList<>()));

        // get data facts from data warehouse
        List<DataFact> dataFacts = dataFactDAO.getDataFacts(dataItems, dataRequest.getLimit());

        // populate item entries with results
        dataFacts.forEach(fact -> result.get(fact.getName() + fact.getCode()).add(fact));
        dataRequest.setResponseCount(dataFacts.size());

        return result;
    }

    public void validate(DataRequest<DataItem> dataRequest) {

        if (dataRequest.getSearchCriteria().size() > MAX_REQUEST_ITEMS) {
            throw new IllegalArgumentException("data request exceeds maximum items limit of " + MAX_REQUEST_ITEMS);
        }

        int badDataItemCount = 0;
        Map<String, Object> errorMap = new HashMap<>();
        for (DataItem dataItem : dataRequest.getSearchCriteria()) {
            String objectName = dataItem.toString();
            Errors errors = new MapBindingResult(new HashMap<String, Object>(), objectName);
            dataItemValidator.validate(dataItem, errors);
            if (errors.hasErrors()) {
                errorMap.put(objectName, errors);
                badDataItemCount++;
            }
        }

        if (!errorMap.isEmpty()) {
            dataRequest.setResponseStatusCode(HttpStatus.BAD_REQUEST.value());
            throw new InvalidSearchException(badDataItemCount + " invalid data items used in search", errorMap);
        }
    }
}

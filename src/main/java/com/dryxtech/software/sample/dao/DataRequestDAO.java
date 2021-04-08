package com.dryxtech.software.sample.dao;

import com.dryxtech.software.sample.model.DataRequest;
import com.dryxtech.software.sample.util.DataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Timestamp;

@Slf4j
@Component
public class DataRequestDAO {

    public static final String OPERATION_SCHEMA = "SAMPLE";
    public static final String REQUEST_HISTORY_TABLE = "REQUEST_HISTORY";
    public static final String REQUEST_GUID_COLUMN = "REQUEST_GUID";
    public static final String REQUEST_ORGANIZATION_COLUMN = "REQUEST_ORGANIZATION";
    public static final String REQUEST_USER_COLUMN = "REQUEST_USER";
    public static final String REQUEST_TYPE_COLUMN = "REQUEST_TYPE";
    public static final String REQUEST_START_TS_COLUMN = "REQUEST_START_TS";
    public static final String REQUEST_END_TS_COLUMN = "REQUEST_END_TS";
    public static final String RESPONSE_COUNT_COLUMN = "RESPONSE_COUNT";
    public static final String RESPONSE_STATUS_CODE_COLUMN = "RESPONSE_STATUS_CODE";

    private final JdbcTemplate operationJdbcTemplate;

    public DataRequestDAO(@Qualifier(value = "operationJdbcTemplate") JdbcTemplate operationJdbcTemplate) {
        this.operationJdbcTemplate = operationJdbcTemplate;
    }

    public void recordDataRequest(@NonNull DataRequest<?> dataRequest) {

        String sql = DataUtil.getInsertSql(OPERATION_SCHEMA, REQUEST_HISTORY_TABLE, REQUEST_GUID_COLUMN,
                REQUEST_ORGANIZATION_COLUMN, REQUEST_USER_COLUMN, REQUEST_TYPE_COLUMN, REQUEST_START_TS_COLUMN,
                REQUEST_END_TS_COLUMN, RESPONSE_COUNT_COLUMN, RESPONSE_STATUS_CODE_COLUMN);

        log.debug("recording data request {} using sql {}", dataRequest, sql);

        operationJdbcTemplate.update(connection -> {
            int index = 0;
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(++index, dataRequest.getGuid());
            ps.setString(++index, dataRequest.getOrganization());
            ps.setString(++index, dataRequest.getUser());
            ps.setString(++index, dataRequest.getType());
            ps.setTimestamp(++index, Timestamp.valueOf(dataRequest.getStartTime()));
            ps.setTimestamp(++index, Timestamp.valueOf(dataRequest.getEndTime()));
            ps.setInt(++index, dataRequest.getResponseCount());
            ps.setInt(++index, dataRequest.getResponseStatusCode());
            return ps;
        });
    }
}

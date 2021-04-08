package com.dryxtech.software.sample.dao;

import com.dryxtech.software.sample.model.DataItem;
import com.dryxtech.software.sample.util.DataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class DataItemDAO {

    public static final String DATA_NAME_COLUMN = "DATA_NAME";
    public static final String DATA_CODE_COLUMN = "DATA_CODE";

    private final JdbcTemplate operationJdbcTemplate;

    public DataItemDAO(@Qualifier(value = "operationJdbcTemplate") JdbcTemplate operationJdbcTemplate) {
        this.operationJdbcTemplate = operationJdbcTemplate;
    }

    public List<DataItem> getValidDataItems(@NonNull Collection<DataItem> dataItems) {

        if (dataItems.isEmpty()) {
            return Collections.emptyList();
        }

        String sql =
                "SELECT DISTINCT DATA_NAME, DATA_CODE FROM SAMPLE.DATA_ITEM" +
                        " WHERE (DATA_NAME, DATA_CODE) IN (" + DataUtil.getInClausePairs(dataItems.size()) + ")" +
                        " AND DATA_STATUS IN ('A', 'P')";

        log.debug("querying data items for {} active data items using sql {}", dataItems.size(), sql);

        return operationJdbcTemplate.query(sql,
                preparedStatement -> {
                    int counter = 0;
                    for (DataItem dataItem : dataItems) {
                        preparedStatement.setString(++counter, dataItem.getName());
                        preparedStatement.setString(++counter, dataItem.getCode());
                    }
                },
                (rs, rowNum) -> new DataItem(rs.getString(DATA_NAME_COLUMN), rs.getString(DATA_CODE_COLUMN)));
    }
}

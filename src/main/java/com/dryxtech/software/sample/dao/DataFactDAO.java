package com.dryxtech.software.sample.dao;

import com.dryxtech.software.sample.model.DataFact;
import com.dryxtech.software.sample.model.DataItem;
import com.dryxtech.software.sample.util.DataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class DataFactDAO {

    public static final String DATA_RANK_COLUMN = "DATA_RANK";
    public static final String DATA_ID_COLUMN = "DATA_ID";
    public static final String DATA_ORGANIZATION_COLUMN = "DATA_ORGANIZATION";
    public static final String DATA_NAME_COLUMN = "DATA_NAME";
    public static final String DATA_CODE_COLUMN = "DATA_CODE";
    public static final String DATA_VALUE_COLUMN = "DATA_VALUE";
    public static final String DATA_CREATED_TS_COLUMN = "DATA_CREATED_TS";

    private final JdbcTemplate dataWarehouseJdbcTemplate;

    public DataFactDAO(@Qualifier(value = "dataWarehouseJdbcTemplate") JdbcTemplate dataWarehouseJdbcTemplate) {
        this.dataWarehouseJdbcTemplate = dataWarehouseJdbcTemplate;
    }

    public List<DataFact> getDataFacts(@NonNull List<DataItem> dataItems, int limit) {

        if (dataItems.isEmpty() || limit < 1) {
            log.warn("skipping query of data facts for {} data items with limit {}", dataItems.size(), limit);
            return Collections.emptyList();
        }

        String sql =
                "SELECT DATA_RANK, DATA_ID, DATA_ORGANIZATION, DATA_NAME, DATA_CODE, DATA_VALUE, DATA_CREATED_TS FROM (" +
                        " SELECT *, ROW_NUMBER() " +
                        " OVER(PARTITION BY DATA_NAME, DATA_CODE ORDER BY DATA_CREATED_TS DESC, DATA_ID DESC) as DATA_RANK" +
                        " FROM SAMPLE.DATA_FACT" +
                        " WHERE (DATA_NAME, DATA_CODE) IN (" + DataUtil.getInClausePairs(dataItems.size()) + ")" +
                        " AND DATA_VALUE != 0)" +
                        " WHERE DATA_RANK <= ?" +
                        " ORDER BY DATA_RANK ASC";

        log.debug("querying data facts for {} data items using sql {}", dataItems.size(), sql);

        return dataWarehouseJdbcTemplate.query(sql,
                preparedStatement -> {
                    int counter = 0;
                    for (DataItem dataItem : dataItems) {
                        preparedStatement.setString(++counter, dataItem.getName());
                        preparedStatement.setString(++counter, dataItem.getCode());
                    }
                    preparedStatement.setInt(++counter, limit);
                },
                (rs, rowNum) -> DataFact.builder()
                        .rank(rs.getInt(DATA_RANK_COLUMN))
                        .id(rs.getLong(DATA_ID_COLUMN))
                        .organization(rs.getString(DATA_ORGANIZATION_COLUMN))
                        .name(rs.getString(DATA_NAME_COLUMN))
                        .code(rs.getString(DATA_CODE_COLUMN))
                        .value(rs.getInt(DATA_VALUE_COLUMN))
                        .createdTimestamp(rs.getTimestamp(DATA_CREATED_TS_COLUMN).toLocalDateTime())
                        .build());
    }
}

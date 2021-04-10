CREATE SCHEMA IF NOT EXISTS SAMPLE AUTHORIZATION SAMPLE;

CREATE TABLE IF NOT EXISTS SAMPLE.REQUEST_HISTORY
(
    request_guid         VARCHAR(36)  PRIMARY KEY,
    request_organization VARCHAR2(64) NOT NULL,
    request_user         VARCHAR2(64) NOT NULL,
    request_type         VARCHAR2(64) NOT NULL,
    request_start_ts     TIMESTAMP    NOT NULL,
    request_end_ts       TIMESTAMP    NOT NULL,
    request_count        INTEGER      NOT NULL,
    response_count       INTEGER      NOT NULL,
    response_status_code INTEGER      NOT NULL
);

CREATE INDEX SAMPLE.REQUEST_HISTORY_ORG_IDX ON SAMPLE.REQUEST_HISTORY(request_organization);

CREATE TABLE IF NOT EXISTS SAMPLE.DATA_ITEM
(
    data_name         VARCHAR2(4)  NOT NULL,
    data_code         VARCHAR2(10) NOT NULL,
    data_status       VARCHAR(1)   NOT NULL
);

CREATE UNIQUE INDEX SAMPLE.DATA_ITEM_NAME_CODE_IDX ON SAMPLE.DATA_ITEM(data_name, data_code);

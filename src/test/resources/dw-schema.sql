CREATE SCHEMA IF NOT EXISTS SAMPLE AUTHORIZATION SAMPLE;

CREATE SEQUENCE IF NOT EXISTS SAMPLE.DATA_FACT_ID_SEQ
 START WITH 100 INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS SAMPLE.DATA_FACT
(
    data_id           BIGINT PRIMARY KEY,
    data_organization VARCHAR2(64) NOT NULL,
    data_name         VARCHAR2(64) NOT NULL,
    data_code         VARCHAR2(64) NOT NULL,
    data_value        INTEGER      NOT NULL,
    data_created_ts   TIMESTAMP    NOT NULL
);

CREATE INDEX SAMPLE.DATA_FACT_NAME_CODE_IDX ON SAMPLE.DATA_FACT(data_name, data_code);
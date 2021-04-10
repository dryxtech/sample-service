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

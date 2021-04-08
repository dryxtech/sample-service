CREATE SCHEMA IF NOT EXISTS SAMPLE AUTHORIZATION SAMPLE;

CREATE TABLE IF NOT EXISTS SAMPLE.DATA_ITEM
(
    data_name         VARCHAR2(4)  NOT NULL,
    data_code         VARCHAR2(10) NOT NULL,
    data_status       VARCHAR(1)   NOT NULL
);

CREATE UNIQUE INDEX SAMPLE.DATA_ITEM_NAME_CODE_IDX ON SAMPLE.DATA_ITEM(data_name, data_code);

insert into sample.data_item (data_name, data_code, data_status) values ('ABC', '0000000123', 'A');
insert into sample.data_item (data_name, data_code, data_status) values ('DEF', '0000000456', 'N');
insert into sample.data_item (data_name, data_code, data_status) values ('GHI', '0000000789', 'A');
insert into sample.data_item (data_name, data_code, data_status) values ('JKL', '0000000123', 'A');
insert into sample.data_item (data_name, data_code, data_status) values ('MNO', '0000000456', 'A');
insert into sample.data_item (data_name, data_code, data_status) values ('PQR', '0000000789', 'A');
insert into sample.data_item (data_name, data_code, data_status) values ('STU', '0000000123', 'A');
insert into sample.data_item (data_name, data_code, data_status) values ('VWX', '0000000456', 'A');
insert into sample.data_item (data_name, data_code, data_status) values ('WZ', '0000000789', 'A');
insert into sample.data_item (data_name, data_code, data_status) values ('XXX', '0000009123', 'A');

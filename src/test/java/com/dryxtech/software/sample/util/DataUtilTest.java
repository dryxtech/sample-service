package com.dryxtech.software.sample.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataUtilTest {

    @Test
    void getInsertSql() {
        String result = DataUtil.getInsertSql("TEST-SCHEMA", "TEST-TABLE", "COL1", "COL2");
        String expected = "INSERT INTO TEST-SCHEMA.TEST-TABLE (COL1, COL2) VALUES (?,?)";
        assertEquals(expected, result);
    }

    @Test
    void getInsertValuesWildcards() {
        String result = DataUtil.getInsertValuesWildcards(5);
        String expected = "(?,?,?,?,?)";
        assertEquals(expected, result);
    }

    @Test
    void getInClausePairs() {
        String result = DataUtil.getInClausePairs(3);
        String expected = "(?,?), (?,?), (?,?)";
        assertEquals(expected, result);
    }
}

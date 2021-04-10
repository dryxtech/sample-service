package com.dryxtech.software.sample.util;

import com.dryxtech.software.sample.model.DataItem;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DataItemUtilTest {

    @Test
    void normalize() {
        List<DataItem> original = Arrays.asList(
                new DataItem(" ABC", " 123"),
                new DataItem("DEF ", "456 "),
                new DataItem("DEF ", "456 ")
        );

        List<DataItem> expected = Arrays.asList(
                new DataItem("ABC", "0000000123"),
                new DataItem("DEF", "0000000456")
        );

        List<DataItem> result = DataItemUtil.normalize(original);

        assertEquals(expected, result);
        assertNotEquals(original, result);
    }

    @Test
    void normalize_empty() {
        assertTrue(DataItemUtil.normalize(new ArrayList<>()).isEmpty());
    }

    @Test
    void convert_single() {
        List<DataItem> result = DataItemUtil.convert("ABC1", 1);
        List<DataItem> expected = Arrays.asList(
                new DataItem("ABC", "1")
        );
        assertEquals(expected, result);
    }

    @Test
    void convert_range() {
        List<DataItem> result = DataItemUtil.convert("ABC1-3", 3);
        List<DataItem> expected = Arrays.asList(
                new DataItem("ABC", "1"),
                new DataItem("ABC", "2"),
                new DataItem("ABC", "3")
        );
        assertEquals(expected, result);
    }

    @Test
    void fill() {
        List<DataItem> list = new ArrayList<>();
        DataItemUtil.fill(list, "TEST", 1, 2);
        List<DataItem> expected = Arrays.asList(
                new DataItem("TEST", "1"),
                new DataItem("TEST", "2")
        );
        assertEquals(expected, list);
    }

    @Test
    void parse() {
        DataItem item = DataItemUtil.parse("ABC123");
        assertEquals("ABC", item.getName());
        assertEquals("123", item.getCode());
    }

    @Test
    void parse_missingName() {
        assertThrows(IllegalArgumentException.class, () -> {
            DataItemUtil.parse("123");
        });
    }

    @Test
    void parse_missingCode() {
        assertThrows(IllegalArgumentException.class, () -> {
            DataItemUtil.parse("ABC");
        });
    }
}

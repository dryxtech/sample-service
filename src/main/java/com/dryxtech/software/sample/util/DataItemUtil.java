package com.dryxtech.software.sample.util;

import com.dryxtech.software.sample.model.DataItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class DataItemUtil {

    private DataItemUtil() {
        // Utility Class
    }

    public static List<DataItem> normalize(Collection<DataItem> original) {

        if (Objects.isNull(original)) {
            return Collections.emptyList();
        }

        return original.stream()
                .map(item -> new DataItem(item.getName().trim().toUpperCase(),
                        StringUtils.leftPad(item.getCode().trim(), 10, "0")))
                .distinct()
                .collect(Collectors.toList());
    }

    public static List<DataItem> convert(String searchStr, int maxSizeAllowed) {

        List<DataItem> dataItems = new ArrayList<>();
        for (String range : searchStr.split("[\\s,]+")) {
            String[] parts = range.split("-");
            if (parts.length == 1) {
                dataItems.add(parse(parts[0]));
            } else if (parts.length == 2) {
                DataItem start = parse(parts[0]);
                DataItem end;
                if (parts[1].startsWith(start.getName())) {
                    end = parse(parts[1]);
                } else if (Character.isDigit(parts[1].charAt(0))) {
                    end = new DataItem(start.getName(), parts[1]);
                } else {
                    throw new IllegalArgumentException("invalid range " + range);
                }

                int startRange = Integer.parseInt(start.getCode());
                int endRange = Integer.parseInt(end.getCode());
                Assert.isTrue(startRange <= endRange, "range start value greater than end value in: " + range);

                int totalCount = dataItems.size() + (endRange - startRange);
                Assert.isTrue(totalCount <= maxSizeAllowed, "max allowed size exceeded " + maxSizeAllowed);

                fill(dataItems, start.getName(), startRange, endRange);

            } else {
                throw new IllegalArgumentException("more than one hyphen found in: " + range);
            }
        }

        return dataItems;
    }

    public static void fill(List<DataItem> list, String name, int start, int end) {
        for (int i = start; i <= end; i++) {
            list.add(new DataItem(name, String.valueOf(i)));
        }
    }

    public static DataItem parse(@NonNull String dataItemStr) {

        int codeStartIndex = 0;
        for (char c : dataItemStr.toCharArray()) {
            if (!Character.isDigit(c)) {
                ++codeStartIndex;
            } else {
                break;
            }
        }

        if (codeStartIndex == 0 || codeStartIndex >= dataItemStr.length()) {
            throw new IllegalArgumentException("invalid data item string " + dataItemStr);
        }

        return new DataItem(dataItemStr.substring(0, codeStartIndex).toUpperCase(), dataItemStr.substring(codeStartIndex));
    }
}

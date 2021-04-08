package com.dryxtech.software.sample.util;

public final class DataUtil {

    private DataUtil() {
        // Utility Class
    }

    public static String getInsertSql(String schema, String table, String... columns) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ");
        sb.append(schema);
        sb.append(".");
        sb.append(table);
        sb.append(" (");
        for (int i = 0; i < columns.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(columns[i]);
        }
        sb.append(") VALUES ");
        sb.append(getInsertValuesWildcards(columns.length));
        return sb.toString();
    }

    public static String getInsertValuesWildcards(int total) {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (int i = 0; i < total; i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append("?");
        }
        sb.append(")");
        return sb.toString();
    }

    public static String getInClausePairs(int totalPairs) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < totalPairs; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append("(?,?)");
        }
        return sb.toString();
    }
}

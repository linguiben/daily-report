package com.webbora.tools;

import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @desc: TODO
 * @author: Jupiter.Lin
 * @date: 2025/5/4
 */
@Slf4j
public class ExcelCommon {

    public static Integer[] getIndexPosition(String position) {
        String letters = "";
        String numbers = "";

        // Regular expression to match letters and numbers
        Pattern pattern = Pattern.compile("([A-Za-z]+)(\\d+)");
        Matcher matcher = pattern.matcher(position);

        if (matcher.matches()) {
            letters = matcher.group(1); // Group 1: Letters
            numbers = matcher.group(2); // Group 2: Numbers
        }
        int columnIndex = getColumnIndex(letters);
        int rowIndex = Integer.parseInt(numbers) - 1;

        log.info("sheet position: {}, index: [{}, {}]", position, columnIndex, rowIndex);

        return new Integer[]{getColumnIndex(letters), Integer.parseInt(numbers) -1 };
    }


    /**
     * Converts an Excel column name (e.g., "A", "AZ") to its corresponding zero-based column index.
     *
     * The method interprets the column name as a base-26 number, where 'A' corresponds to 1, 'B' to 2,
     * and so on. For multi-character column names, it calculates the index using positional values.
     *
     * @param columnName The Excel column name to convert (e.g., "A", "AZ", "BA").
     * @return The zero-based column index (e.g., "A" -> 0, "AZ" -> 51).
     */
    public static int getColumnIndex(String columnName) {
        int colIndex = 0;
        for (int i = 0; i < columnName.length(); i++) {
            // Convert each character to its corresponding value and calculate the index
            colIndex = colIndex * 26 + (columnName.charAt(i) - 'A' + 1);
        }
        return colIndex - 1; // Convert to zero-based index
    }

    // TODO: 未验证
    private static int[] getCellPosition(String cellReference) {
        String column = cellReference.replaceAll("\\d", "");
        int row = Integer.parseInt(cellReference.replaceAll("\\D", ""));
        int colIndex = column.charAt(0) - 'A';
        return new int[]{colIndex - 1, row - 2}; // Convert to zero-based index
    }
}

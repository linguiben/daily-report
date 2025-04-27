package com.webbora.service.impl;

/**
 * @desc: TODO
 * @author: Jupiter.Lin
 * @date: 2025/4/16
 */

import com.webbora.config.ExcelDataConfig;
import com.webbora.pojo.Person;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExcelServiceImpl implements com.webbora.service.ExcelService {

    final ExcelDataConfig excelDataConfig;

    /**
     * @param file the Excel file to read
     * @return List<String [ ]>
     * @desc read excel data from startPosition to endPosition
     * @author Jupiter.Lin
     * @date 2025/4/27
     */
    public List<String[]> readExcel(MultipartFile file) {
        List<String[]> rowList = new ArrayList<>();
        log.info("retrieve excel data from file: {}", file.getOriginalFilename());
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {
            // retrieve the sheet name, startRow, endRow, startColumn, endColumn
            String sheetName = excelDataConfig.getExhibitionMap().get("exhibition2").get("sheetName");
            Sheet sheet = workbook.getSheet(sheetName);
            String startPosition = excelDataConfig.getExhibitionMap().get("exhibition2").get("startPosition");
            String endPosition = excelDataConfig.getExhibitionMap().get("exhibition2").get("endPosition");
            Integer[] startPositionIndex = getIndexPosition(startPosition);
            Integer[] endPositionIndex = getIndexPosition(endPosition);
            rowList = readExcelRange(sheet,
                    startPositionIndex[1] , // startRow
                    startPositionIndex[0], // startColumn
                    endPositionIndex[1] , // endRow
                    endPositionIndex[0]); // endColumn

        } catch (IOException e) {
            e.printStackTrace();
        }
        return rowList;
    }

    private List<String[]> readExcelRange(Sheet sheet, int startRow, int startCol, int endRow, int endCol) {
        List<String[]> rowList = new ArrayList<>();
        log.info("retrieve excel data from startRow: {}, startCol: {}, endRow: {}, endCol: {}",
                startRow, startCol, endRow, endCol);
        // Iterate through the rows in the specified range
        for (int rowNum = startRow; rowNum <= endRow; rowNum++) {
            Row currentRow = sheet.getRow(rowNum);
            if (currentRow == null) {
                continue;
            }
            String[] rowData = new String[endCol - startCol + 1];
            for (int colNum = startCol; colNum <= endCol; colNum++) {
                Cell cell = currentRow.getCell(colNum);
                if (cell != null) {
                    switch (cell.getCellType()) {
                        case STRING:
                            rowData[colNum - startCol] = cell.getStringCellValue();
                            break;
                        case NUMERIC:
                            rowData[colNum - startCol] = String.valueOf(cell.getNumericCellValue());
                            break;
                        case BOOLEAN:
                            rowData[colNum - startCol] = String.valueOf(cell.getBooleanCellValue());
                            break;
                        default:
                            rowData[colNum - startCol] = "";
                    }
                } else {
                    rowData[colNum - startCol] = "";
                }
            }
            rowList.add(rowData);
        }

        return rowList;
    }

    public List<String[]> readExcel(String filePath) throws IOException {
        List<String[]> rowList = new ArrayList<>();
        log.info("retrieve excel data from filePath: {}", filePath);
        try (FileInputStream fis = new FileInputStream(filePath); Workbook workbook = new XSSFWorkbook(fis)) {
            // retrieve the sheet name, startRow, endRow, startColumn, endColumn
            String sheetName = excelDataConfig.getExhibitionMap().get("exhibition2").get("sheetName");
            Sheet sheet = workbook.getSheet(sheetName);
            String startPosition = excelDataConfig.getExhibitionMap().get("exhibition2").get("startPosition");
            String endPosition = excelDataConfig.getExhibitionMap().get("exhibition2").get("endPosition");
            Integer[] startPositionIndex = getIndexPosition(startPosition);
            Integer[] endPositionIndex = getIndexPosition(endPosition);
            rowList = readExcelRange(sheet, startPositionIndex[1], // startRow
                    startPositionIndex[0], // startColumn
                    endPositionIndex[1], // endRow
                    endPositionIndex[0]); // endColumn
        }
        return rowList;
    }


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

            log.info("Letters of position: {}", letters);
            log.info("Numbers of position: {}", numbers);

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
}


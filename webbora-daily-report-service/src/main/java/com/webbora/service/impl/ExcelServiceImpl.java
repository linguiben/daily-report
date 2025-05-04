package com.webbora.service.impl;

/**
 * @desc: TODO
 * @author: Jupiter.Lin
 * @date: 2025/4/16
 */

import com.webbora.config.ExcelDataConfig;
import com.webbora.pojo.Person;
import com.webbora.tools.ApachePoiXLSBReader;
import com.webbora.tools.ExcelCommon;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.eventusermodel.XSSFBReader;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
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
//             Workbook workbook = new XSSFWorkbook(inputStream)) {
             Workbook workbook = createWorkbook(inputStream, file.getOriginalFilename())) {
            // retrieve the sheet name, startRow, endRow, startColumn, endColumn
            String sheetName = excelDataConfig.getExhibitionMap().get("exhibition2").get("sheetName");
            Sheet sheet = workbook.getSheet(sheetName);
            String startPosition = excelDataConfig.getExhibitionMap().get("exhibition2").get("startPosition");
            String endPosition = excelDataConfig.getExhibitionMap().get("exhibition2").get("endPosition");
            Integer[] startPositionIndex = ExcelCommon.getIndexPosition(startPosition);
            Integer[] endPositionIndex = ExcelCommon.getIndexPosition(endPosition);
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
                if (cell == null) {
                    rowData[colNum - startCol] = "";
                    continue;
                }
                // Check if the cell is part of a merged region
                for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
                    CellRangeAddress range = sheet.getMergedRegion(i);
                    if (range.isInRange(rowNum, colNum)) {
                        // If the cell is part of a merged region, get the value from the first cell in the range
                        Cell firstCell = sheet.getRow(range.getFirstRow()).getCell(range.getFirstColumn());
                        if (firstCell != null) {
                            rowData[colNum - startCol] = firstCell.getStringCellValue();
                        }
                        break;
                    }
                }
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
            }
            rowList.add(rowData);
        }

        return rowList;
    }

    public List<String[]> readExcel(String filePath) throws IOException {
        List<String[]> rowList = new ArrayList<>();
        String sheetName = excelDataConfig.getExhibitionMap().get("exhibition1").get("sheetName");
        String startPosition = excelDataConfig.getExhibitionMap().get("exhibition1").get("startPosition");
        String endPosition = excelDataConfig.getExhibitionMap().get("exhibition1").get("endPosition");
        log.info("retrieve excel data from filePath: {}", filePath);
        String suffix = filePath.substring(filePath.lastIndexOf("."));

        // For .xlsb files
        if (suffix.equalsIgnoreCase(".xlsb")) {
            String[][] data = ApachePoiXLSBReader.readXLSBFile(filePath, sheetName, startPosition, endPosition);
            for (String[] row : data) {
                rowList.add(row);
            }
            log.debug("rowList: {}", Arrays.deepToString(rowList.toArray()));
            return rowList;
        }

        // For .xlsx and .xls files
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = createWorkbook(fis, filePath)) {
            Sheet sheet = workbook.getSheet(sheetName);
            Integer[] startPositionIndex = ExcelCommon.getIndexPosition(startPosition);
            Integer[] endPositionIndex = ExcelCommon.getIndexPosition(endPosition);
            rowList = readExcelRange(sheet, startPositionIndex[1], // startRow
                    startPositionIndex[0], // startColumn
                    endPositionIndex[1], // endRow
                    endPositionIndex[0]); // endColumn
        }
        return rowList;
    }


    private Workbook createWorkbook(InputStream inputStream, String fileName) throws IOException {
        final String FILE_NAME = fileName.toUpperCase();
        if (FILE_NAME.endsWith(".XLSX")) {
            return new XSSFWorkbook(inputStream);
        } else if (FILE_NAME.endsWith(".XLS")) {
            return new HSSFWorkbook(inputStream);
        } else {
            throw new IllegalArgumentException("Unsupported file format: " + fileName);
        }
    }
}


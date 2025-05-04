package com.webbora.tools;

/**
 * @desc: TODO
 * @author: Jupiter.Lin
 * @date: 2025/5/2
 */
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.binary.XSSFBSharedStringsTable;
import org.apache.poi.xssf.binary.XSSFBSheetHandler;
import org.apache.poi.xssf.binary.XSSFBStylesTable;
import org.apache.poi.xssf.eventusermodel.XSSFBReader;
import org.xml.sax.SAXException;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.usermodel.XSSFComment;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class ApachePoiXLSBReader {

    public static String[][] readXLSBFile (String xlsbFileName, String sheetName, String startPosition, String endPosition) {

        OPCPackage pkg;
        String[][] data = null;

        try {
            pkg = OPCPackage.open(xlsbFileName);
            XSSFBReader r = new XSSFBReader(pkg);
            XSSFBSharedStringsTable sst = new XSSFBSharedStringsTable(pkg);
            XSSFBStylesTable xssfbStylesTable = r.getXSSFBStylesTable();
            XSSFBReader.SheetIterator it = (XSSFBReader.SheetIterator) r.getSheetsData();

            List<String> sheetTexts = new ArrayList<>();
            while (it.hasNext()) {
                InputStream is = it.next();
                String name = it.getSheetName();
                if (!name.equals(sheetName)) {
                    continue;
                }
                XlsbSheetHandler xlsbSheetHandler = new XlsbSheetHandler(startPosition, endPosition);
                xlsbSheetHandler.startSheet(name);
                XSSFBSheetHandler sheetHandler = new XSSFBSheetHandler(is,
                        xssfbStylesTable,
                        it.getXSSFBSheetComments(),
                        sst, xlsbSheetHandler,
                        new DataFormatter(),
                        false);
                sheetHandler.parse();
                xlsbSheetHandler.endSheet();
                sheetTexts.add(xlsbSheetHandler.toString());
                data = xlsbSheetHandler.getData();
                // 打印二维数组data
                if (log.isDebugEnabled()) {
                    log.debug("sheet name: {}, data: {}", name, Arrays.deepToString(data));
                }
            }
            // System.out.println("output text:"+sheetTexts);

        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (OpenXML4JException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return data;
    }
}



class XlsbSheetHandler implements XSSFSheetXMLHandler.SheetContentsHandler {

    private final StringBuilder sb = new StringBuilder();
    private final ArrayList<String[]> rowList = new ArrayList<>();
    private final Integer[] startPositionIndex; // B2 = {1, 1}
    private final Integer[] endPositionIndex; // D6 = {3, 5}
    String[][] data; // For B2:D6 (5 rows, 3 columns)
    ArrayList<String[]> dataList;

    public XlsbSheetHandler(String startPosition, String endPosition) {
        this.startPositionIndex = ExcelCommon.getIndexPosition(startPosition);
        this.endPositionIndex = ExcelCommon.getIndexPosition(endPosition);
        this.data = new String[endPositionIndex[1] - startPositionIndex[1] + 1][endPositionIndex[0] - startPositionIndex[0] + 1];
    }

    public void startSheet(String sheetName) {
        sb.append("<sheet name=\"").append(sheetName).append(">");
    }

    public void endSheet() {
        sb.append("</sheet>");
    }

    @Override
    public void startRow(int rowNum) {
        sb.append("\n<tr num=\"").append(rowNum).append(">");
    }

    @Override
    public void endRow(int rowNum) {
        sb.append("\n</tr num=\"").append(rowNum).append(">");
    }

    @Override
    public void cell(String cellReference, String formattedValue, XSSFComment comment) {
        int[] cellPos = getCellPosition(cellReference);
        formattedValue = (formattedValue == null) ? "" : formattedValue;
        if (cellPos[0] >= this.startPositionIndex[0] && cellPos[1] >= this.startPositionIndex[1]
                && cellPos[0] <= this.endPositionIndex[0] && cellPos[1] <= this.endPositionIndex[1]) {
            data[cellPos[1] - this.startPositionIndex[1]][cellPos[0] - this.startPositionIndex[0]] = formattedValue;
        }
        if (comment == null) {
            sb.append("\n\t<td ref=\"").append(cellReference).append("\">").append(formattedValue).append("</td>");
        } else {
            sb.append("\n\t<td ref=\"").append(cellReference).append("\">")
                    .append(formattedValue)
                    .append("<span type=\"comment\" author=\"")
                    .append(comment.getAuthor()).append("\">")
                    .append(comment.getString().toString().trim()).append("</span>")
                    .append("</td>");
        }
    }

    @Override
    public void headerFooter(String text, boolean isHeader, String tagName) {
        if (isHeader) {
            sb.append("<header tagName=\"").append(tagName).append("\">").append(text).append("</header>");
        } else {
            sb.append("<footer tagName=\"").append(tagName).append("\">").append(text).append("</footer>");
        }
    }

    @Override
    public String toString() {
        return sb.toString();
    }

    private static int[] getCellPosition(String cellReference) {
        String column = cellReference.replaceAll("\\d", "");
        int row = Integer.parseInt(cellReference.replaceAll("\\D", ""));
        int colIndex = column.charAt(0) - 'A';
        return new int[]{colIndex, row - 1}; // Convert to zero-based index
    }

    public String[][] getData() {
        return data;
    }
}
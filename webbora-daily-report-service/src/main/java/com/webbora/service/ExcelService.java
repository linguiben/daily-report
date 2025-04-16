package com.webbora.service;

/**
 * @desc: TODO
 * @author: Jupiter.Lin
 * @date: 2025/4/16
 */
import com.webbora.pojo.Person;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ExcelService {

    public List<Person> readExcel(MultipartFile file) {
        List<Person> personList = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            // Skip the header row
            if (rows.hasNext()) {
                rows.next();
            }

            while (rows.hasNext()) {
                Row currentRow = rows.next();
                Iterator<Cell> cellsInRow = currentRow.iterator();

                int id = (int) cellsInRow.next().getNumericCellValue();
                String name = cellsInRow.next().getStringCellValue();
                int age = (int) cellsInRow.next().getNumericCellValue();

                personList.add(new Person(id, name, age));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return personList;
    }

    public List<Person> readExcel(String filePath) throws IOException {
        List<Person> personList = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row
                Person person = new Person();
                person.setId((int) row.getCell(0).getNumericCellValue());
                person.setName(row.getCell(1).getStringCellValue());
                person.setAge((int) (row.getCell(2).getNumericCellValue()));
                personList.add(person);
            }
        }
        return personList;
    }
}

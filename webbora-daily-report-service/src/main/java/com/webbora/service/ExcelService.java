package com.webbora.service;

import com.webbora.pojo.Person;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @desc: TODO
 * @author: Jupiter.Lin
 * @date: 2025/4/26
 */
public interface ExcelService {

    /**
     * Reads an Excel file and returns a list of Person objects.
     *
     * @param file the Excel file to read
     * @return a list of Person objects
     */
    public List<String[]> readExcel(MultipartFile file);

    public List<String[]> readExcel(String filePath) throws IOException;
}

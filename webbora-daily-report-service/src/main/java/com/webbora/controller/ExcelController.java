package com.webbora.controller;

/**
 * @desc: TODO
 * @author: Jupiter.Lin
 * @date: 2025/4/16
 */

import com.webbora.exception.BizException;
import com.webbora.pojo.Person;
import com.webbora.service.ExcelService;
import com.webbora.service.FileSaveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ExcelController {

    @Resource
    private String applicationHomePath;
    @Value("${file-upload-path}")
    private String fileUploadPath;

    private final FileSaveService fileSaveService;
    private final ExcelService excelService;

    @PostMapping("/uploadAndSave")
    public String uploadAndSave(@RequestParam("file") MultipartFile file,
                                @RequestParam("shortDesc") String shortDesc, Model model) {
        try {
            fileSaveService.save(file, shortDesc);
        } catch (IOException e) {
            throw new BizException(e.getMessage());
        }
        return "uploadSuccess";
    }

    @PostMapping("/uploadAndView")
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model) {
//        List<Person> personList = excelService.readExcel(file);
//        model.addAttribute("persons", personList);
//        return "result";

        List<String[]> rowList = excelService.readExcel(file);
        model.addAttribute("rowList", rowList);
        return "exhibitions";
    }

    @GetMapping("/viewExcel")
    public String viewExcel(@RequestParam("fileName") String fileName, Model model) {
        try {
            File file = new File(applicationHomePath + "/" + fileUploadPath + "/" + fileName);
            List<String[]> rowList = excelService.readExcel(file.getAbsolutePath());
            model.addAttribute("rowList", rowList);
            model.addAttribute("fileName", fileName);
        } catch (IOException e) {
            e.printStackTrace();
            throw new BizException("Failed to read the Excel file: " + fileName);
        }
        return "exhibitions";
    }

    @GetMapping("/listExcelFiles")
    @ResponseBody
    public List<String> listExcelFiles() {
        File folder = new File(applicationHomePath + "/" + fileUploadPath);
        List<String> fileNames = new ArrayList<>();
        if (folder.exists() && folder.isDirectory()) {
            for (File file : folder.listFiles()) {
                if (file.isFile() && file.getName().endsWith(".xlsx")) {
                    fileNames.add(file.getName());
                }
            }
        }
        log.info("Excel files in the directory: {}", fileNames);
        return fileNames;
    }
}

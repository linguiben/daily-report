package com.webbora.controller;

/**
 * @desc: TODO
 * @author: Jupiter.Lin
 * @date: 2025/4/16
 */
import com.webbora.pojo.Person;
import com.webbora.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
public class ExcelController {

    @Autowired
    private ExcelService excelService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/uploadAndView")
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model) {
        List<Person> personList = excelService.readExcel(file);
        model.addAttribute("persons", personList);
        return "result";
    }

    @GetMapping("/display")
    public String showExcelData(Model model) {
        try {
            String filePath = "/Users/jupiter/Downloads/test.xlsx"; // Replace with your file path
            List<Person> personList = excelService.readExcel(filePath);
            model.addAttribute("persons", personList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "result";
    }
}

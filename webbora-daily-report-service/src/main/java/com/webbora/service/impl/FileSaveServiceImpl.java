/**
 * @className FileSaveService
 * @desc TODO
 * @author Jupiter.Lin
 * @date 2024-03-23 23:02
 */
package com.webbora.service.impl;

import com.webbora.service.FileSaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;

/**
 * @desc TODO
 * @author Jupiter.Lin
 */
@Slf4j
@Service
public class FileSaveServiceImpl implements FileSaveService {

    @Value("${file-upload-path}")
    private String fileUploadPath;

    @Resource
    private String applicationHomePath;

    @Override
    public String save(MultipartFile multipartFile, String desc){
        if (multipartFile.isEmpty()) {
            return "Can not upload empty file";
        }

        try {
            String fileName = multipartFile.getOriginalFilename();
            if (fileName == null || fileName.isEmpty()) {
                return "File name is empty";
            }
            byte[] bytes = multipartFile.getBytes();

            // add your file save logic here
            File folder=new File(applicationHomePath+"/"+fileUploadPath);
            if(!folder.isDirectory()){
                folder.mkdirs();
            }
            multipartFile.transferTo(new File(folder, fileName)); //  save file
            log.info("file:{} saved to {}",fileName,new File(folder, fileName).getAbsolutePath()); // output (upload file) saved absolute path
            return "succeeded upload：" + fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return "failed to upload：" + e.getMessage();
        }
    }
}

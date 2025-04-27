/**
 * @className FileSaveService
 * @desc TODO
 * @author Jupiter.Lin
 * @date 2024-03-23 23:36
 */
package com.webbora.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @desc Save file to servers.
 * @author Jupiter.Lin
 * @date 2025-04-26
 */
public interface FileSaveService {

    /**
     * Save file to servers.
     * @param multipartFile the file to save
     * @param desc a description of the file
     * @return a message indicating the result of the operation
     * @throws IOException if an I/O error occurs
     */
    String save(MultipartFile multipartFile, String desc) throws IOException;
}

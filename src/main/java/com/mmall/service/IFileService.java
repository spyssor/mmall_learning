package com.mmall.service;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

public interface IFileService {

    public String upload(@RequestParam(value = "upload_file", required = false) MultipartFile file, String path);
}

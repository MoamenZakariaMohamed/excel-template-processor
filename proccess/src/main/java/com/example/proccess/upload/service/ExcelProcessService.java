package com.example.proccess.upload.service;

import com.example.proccess.upload.domain.Dto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ExcelProcessService {
    List<Dto> readExcel(MultipartFile file);
    void processInParallel(List<Dto> data);

}

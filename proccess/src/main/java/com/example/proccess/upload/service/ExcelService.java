package com.example.proccess.upload.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExcelService {

    private final  ExcelProcessService excelProcessor;

    @Async
    public void processExcel(MultipartFile file) {
        LocalDateTime now = LocalDateTime.now();
        log.info("start processing excel file at " + now);
        excelProcessor.processInParallel(excelProcessor.readExcel(file));
        log.info("end processing excel file at " + LocalDateTime.now());

    }
}

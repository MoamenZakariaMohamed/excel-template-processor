package com.example.proccess;

import com.example.proccess.populator.service.BulkImportWorkbookPopulatorService;
import com.example.proccess.upload.service.ExcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/excel")
@RequiredArgsConstructor
public class ExcelController {

    private final  ExcelService excelService;
   private final BulkImportWorkbookPopulatorService bulkImportWorkbookPopulatorService;
    @PostMapping("/process")
    public ResponseEntity<String> processExcel(@RequestParam("file") MultipartFile file) {
        excelService.processExcel(file);
        return ResponseEntity.ok("Excel processing started successfully.");
    }

    @GetMapping(value = "/downloadtemplate")
    public ResponseEntity<byte[]> getClientTemplate(@RequestParam("entityType")  String entityType, @RequestParam("officeId") final Long officeId) {
        byte[] excelBytes = bulkImportWorkbookPopulatorService.getTemplate(entityType, officeId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDispositionFormData("attachment", "filename.xls");
        return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
    }


}


package com.example.proccess.populator.service;

import com.example.proccess.upload.domain.ServiceType;
import com.example.proccess.upload.domain.ServiceTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BulkImportWorkbookPopulatorServiceImpl implements BulkImportWorkbookPopulatorService {


    private final ServiceTypeRepository serviceTypeRepository;



    @Override
    public byte[] getTemplate(String entityType, Long officeId) {
        final Workbook workbook = new XSSFWorkbook(); // Use XSSFWorkbook instead of HSSFWorkbook
        WorkbookPopulator populate = populateUserWorkbook();
        populate.populate(workbook);
        return buildResponse(workbook);
    }


    private List<ServiceType> fetchServiceTypes() {
           return this.serviceTypeRepository.findAll();
    }

    private byte[] buildResponse(final Workbook workbook) {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            workbook.write(baos);
        } catch (IOException e) {
            log.error("Problem occurred in buildResponse function", e);
        }
       return  baos.toByteArray();
    }

    private WorkbookPopulator populateUserWorkbook() {
        List<ServiceType> serviceTypeList = fetchServiceTypes();
        return new ServicesWorkbookPopulator(new ServiceTypeSheetPopulator(serviceTypeList));
    }



}

package com.example.proccess.upload.service;

import com.example.proccess.populator.constants.ServicesConstants;
import com.example.proccess.upload.domain.Dto;
import com.example.proccess.upload.domain.LogEntity;
import com.example.proccess.upload.domain.LogEntityRepository;
import com.example.proccess.upload.domain.ServiceType;
import com.example.proccess.upload.domain.ServiceTypeRepository;
import com.example.proccess.upload.domain.Services;
import com.example.proccess.upload.domain.ServicesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExcelProcessor implements ExcelProcessService {
    private final ServiceTypeRepository serviceTypeRepo;
    private final ServicesRepository serviceRepo;
    private final LogEntityRepository logEntityRepo;
    private Map<Long, ServiceType> serviceTypeMap;


    @Override
    public List<Dto> readExcel(MultipartFile file) {
        List<Dto> items = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = WorkbookFactory.create(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 2; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    Dto excelRecord = createExcelRecordFromRow(row);
                    items.add(excelRecord);
                }
            }

        } catch (IOException e) {
            log.error("Error reading excel file", e);
        }
        return items;
    }

    @Override
    public void processInParallel(List<Dto> data) {
        loadServiceTypes();
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failureCount = new AtomicInteger();
        List<CompletableFuture<Void>> futures = data.stream()
                .collect(Collectors.groupingByConcurrent(e -> e.hashCode() % 3))
                .values()
                .stream()
                .map(chunk -> CompletableFuture.runAsync(() -> processChunk(chunk, successCount, failureCount)))
                .toList();
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        saveTotalCounts(successCount.get(), failureCount.get());
    }

    private void processChunk(List<Dto> chunk, AtomicInteger successCount, AtomicInteger failureCount) {
        System.out.println(chunk.size() + " records to process" + Thread.currentThread().getName() + "now time is " + System.currentTimeMillis() / 1000 + "s");

        List<Services> entities = new ArrayList<>();

        for (Dto row : chunk) {
            try {
                Services entity = createEntityFromDto(row ,failureCount);
                if (entity != null) {
                    entities.add(entity);
                    successCount.incrementAndGet();
                }
            } catch (Exception e) {
                failureCount.incrementAndGet();
            }
        }
        saveInBulk(entities);
    }

    private void saveInBulk(List<Services> entities) {
        serviceRepo.saveAll(entities);
    }

    private Services createEntityFromDto(Dto dto,  AtomicInteger failureCount) {
        ServiceType serviceType = getServiceType(dto.getServiceType());
        if (serviceType == null) {
            failureCount.incrementAndGet();
            return null;
        }
        return Services.buildService(serviceType, dto);
    }
    private void saveTotalCounts(int successCount, int failureCount) {
        LogEntity logEntity = new LogEntity();
        logEntity.setSuccess(successCount);
        logEntity.setFail(failureCount);
        logEntity.setTotal(successCount + failureCount);
        logEntityRepo.save(logEntity);
    }

    public void loadServiceTypes() {
        List<ServiceType> types = serviceTypeRepo.findAll();
        serviceTypeMap = types.stream()
                .collect(Collectors.toMap(ServiceType::getId, t -> t));
    }

    public ServiceType getServiceType(Long id) {
        return serviceTypeMap.get(id);
    }




    private Dto createExcelRecordFromRow(Row row) {
        Dto excelRecord = new Dto();
        excelRecord.setCode(getLongCellValue(row.getCell(ServicesConstants.CODE)));
        excelRecord.setPrice(getBigDecimalCellValue(row.getCell(ServicesConstants.PRICE)));
        excelRecord.setDescription(getStringCellValue(row.getCell(ServicesConstants.DESCRIPTION)));
        excelRecord.setQty(getBigDecimalCellValue(row.getCell(ServicesConstants.QTY)));
        excelRecord.setMeasure(getBigDecimalCellValue(row.getCell(ServicesConstants.MEASURE)));
        excelRecord.setServiceType(getLongCellValue(row.getCell(ServicesConstants.SERVICE_TYPE)));
        excelRecord.setCategory(getLongCellValue(row.getCell(ServicesConstants.CATEGORY)));
        excelRecord.setIsActive(getBooleanCellValue(row.getCell(ServicesConstants.IS_ACTIVE)));
        return excelRecord;
    }
    private BigDecimal getBigDecimalCellValue(Cell cell) {
        return (cell == null || cell.getCellType() == CellType.BLANK) ? null : BigDecimal.valueOf(cell.getNumericCellValue());
    }

    private Long getLongCellValue(Cell cell) {
        return (cell == null || cell.getCellType() == CellType.BLANK) ? null : (long) cell.getNumericCellValue();
    }

    private String getStringCellValue(Cell cell) {
        return (cell == null) ? null : cell.getStringCellValue();
    }

    private Boolean getBooleanCellValue(Cell cell) {
        if(cell == null) {
            return false;
        }
        return switch (cell.getCellType()) {
            case BOOLEAN -> cell.getBooleanCellValue();
            case NUMERIC -> cell.getNumericCellValue() == 1;
            default -> false;
        };
    }


}
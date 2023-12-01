package com.example.proccess.populator.service;

import com.example.proccess.upload.domain.ServiceType;
import com.example.proccess.populator.constants.TemplatePopulateImportConstants;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import java.util.List;

public class ServiceTypeSheetPopulator extends AbstractWorkbookPopulator {

    private List<ServiceType> serviceTypeList;

    private static final int ID_COL = 0;
    private static final int Service_NAME_COL = 1;

    public ServiceTypeSheetPopulator(final List<ServiceType> serviceTypes) {
        this.serviceTypeList = serviceTypes;
    }

    @Override
    public void populate(final Workbook workbook) {
        int rowIndex = 1;
        Sheet serviceTypeSheet = workbook.createSheet(TemplatePopulateImportConstants.SERVICE_TYPE_NAME);
        setLayout(serviceTypeSheet);
        populateServiceType(serviceTypeSheet, rowIndex);
        serviceTypeSheet.protectSheet("");
    }

    private void populateServiceType(Sheet serviceTypeSheet, int rowIndex) {
        for (ServiceType serviceType : serviceTypeList) {
            Row row = serviceTypeSheet.createRow(rowIndex);
            writeLong(ID_COL, row, serviceType.getId());
            writeString(Service_NAME_COL, row, serviceType.getName().trim());
            rowIndex++;
        }
    }

    private void setLayout(Sheet worksheet) {
        worksheet.setColumnWidth(ID_COL, TemplatePopulateImportConstants.SMALL_COL_SIZE);
        worksheet.setColumnWidth(Service_NAME_COL, TemplatePopulateImportConstants.MEDIUM_COL_SIZE);
        Row rowHeader = worksheet.createRow(TemplatePopulateImportConstants.ROWHEADER_INDEX);
        rowHeader.setHeight(TemplatePopulateImportConstants.ROW_HEADER_HEIGHT);
        writeString(ID_COL, rowHeader, "ID");
        writeString(Service_NAME_COL, rowHeader, "Name");
    }

    public List<ServiceType> getServiceTypeList() {
        return serviceTypeList;
    }

    public void writeString(int columnIndex, Row row, String value) {
        Cell cell = row.createCell(columnIndex, CellType.STRING);
        cell.setCellValue(value);
    }
}

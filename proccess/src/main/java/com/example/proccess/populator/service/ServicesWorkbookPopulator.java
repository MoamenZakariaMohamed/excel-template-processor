package com.example.proccess.populator.service;

import com.example.proccess.upload.domain.ServiceType;
import com.example.proccess.populator.constants.TemplatePopulateImportConstants;
import com.example.proccess.populator.constants.ServicesConstants;
import org.apache.poi.hssf.usermodel.HSSFDataValidationHelper;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.util.List;


public class ServicesWorkbookPopulator extends AbstractWorkbookPopulator {

    private final ServiceTypeSheetPopulator serviceTypeSheetPopulator;


    public ServicesWorkbookPopulator(ServiceTypeSheetPopulator serviceTypeSheetPopulator) {
        this.serviceTypeSheetPopulator = serviceTypeSheetPopulator;
    }

    @Override
    public void populate(Workbook workbook) {
        Sheet servicesSheet = workbook.createSheet(TemplatePopulateImportConstants.SERVICES_SHEET_NAME);
        serviceTypeSheetPopulator.populate(workbook);
        setLayout(servicesSheet);

        setRules(servicesSheet);
    }

    private void setRules(Sheet usersheet) {
        CellRangeAddressList serviceType = new CellRangeAddressList(1, SpreadsheetVersion.EXCEL97.getLastRowIndex(),
                ServicesConstants.SERVICE_TYPE, ServicesConstants.SERVICE_TYPE);
        CellRangeAddressList isActiveConstraint = new CellRangeAddressList(1, SpreadsheetVersion.EXCEL97.getLastRowIndex(),
                ServicesConstants.IS_ACTIVE, ServicesConstants.IS_ACTIVE);
        DataValidationHelper validationHelper = new HSSFDataValidationHelper((HSSFSheet) usersheet);
        List<ServiceType> serviceTypes = serviceTypeSheetPopulator.getServiceTypeList();
        setNames(usersheet, serviceTypes);
        DataValidationConstraint serviceTypeConstraint = validationHelper.createFormulaListConstraint("ServiceType");
        DataValidationConstraint booleanConstraint = validationHelper.createExplicitListConstraint(new String[] { "True", "False" });
        DataValidation serviceTypeValidation = validationHelper.createValidation(serviceTypeConstraint, serviceType);
        DataValidation isActiveValidation = validationHelper.createValidation(booleanConstraint, isActiveConstraint);
        usersheet.addValidationData(serviceTypeValidation);
        usersheet.addValidationData(isActiveValidation);
    }

    private void setNames(Sheet usersheet, List<ServiceType> serviceTypes) {
        Workbook userWorkbook = usersheet.getWorkbook();
        Name serviceTye = userWorkbook.createName();
        serviceTye.setNameName("ServiceType");
        serviceTye.setRefersToFormula(TemplatePopulateImportConstants.SERVICE_TYPE_NAME + "!$B$2:$B$" + (serviceTypes.size() + 1));
        userWorkbook.createName();

    }

    private void setLayout(Sheet servicesSheet) {
        Row rowHeader = servicesSheet.createRow(TemplatePopulateImportConstants.ROWHEADER_INDEX);
        rowHeader.setHeight(TemplatePopulateImportConstants.ROW_HEADER_HEIGHT);
        servicesSheet.setColumnWidth(ServicesConstants.CODE, TemplatePopulateImportConstants.MEDIUM_COL_SIZE);
        servicesSheet.setColumnWidth(ServicesConstants.PRICE, TemplatePopulateImportConstants.MEDIUM_COL_SIZE);
        servicesSheet.setColumnWidth(ServicesConstants.DESCRIPTION, TemplatePopulateImportConstants.MEDIUM_COL_SIZE);
        servicesSheet.setColumnWidth(ServicesConstants.QTY, TemplatePopulateImportConstants.MEDIUM_COL_SIZE);
        servicesSheet.setColumnWidth(ServicesConstants.MEASURE, TemplatePopulateImportConstants.MEDIUM_COL_SIZE);
        servicesSheet.setColumnWidth(ServicesConstants.SERVICE_TYPE, TemplatePopulateImportConstants.MEDIUM_COL_SIZE);
        servicesSheet.setColumnWidth(ServicesConstants.CATEGORY, TemplatePopulateImportConstants.MEDIUM_COL_SIZE);
        servicesSheet.setColumnWidth(ServicesConstants.IS_ACTIVE, TemplatePopulateImportConstants.MEDIUM_COL_SIZE);
        servicesSheet.setColumnWidth(ServicesConstants.STATUS_COL, TemplatePopulateImportConstants.MEDIUM_COL_SIZE);

        writeString(ServicesConstants.CODE, rowHeader, "Code");
        writeString(ServicesConstants.PRICE, rowHeader, "Price");
        writeString(ServicesConstants.DESCRIPTION, rowHeader, "Description *");
        writeString(ServicesConstants.QTY, rowHeader, "QTY *");
        writeString(ServicesConstants.MEASURE, rowHeader, "Measure *");
        writeString(ServicesConstants.SERVICE_TYPE, rowHeader, "Service Type *");
        writeString(ServicesConstants.CATEGORY, rowHeader, "Category *");
        writeString(ServicesConstants.IS_ACTIVE, rowHeader, "Is Active *");

    }

}

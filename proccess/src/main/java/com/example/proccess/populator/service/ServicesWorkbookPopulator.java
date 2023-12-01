package com.example.proccess.populator.service;

import com.example.proccess.upload.domain.ServiceType;
import com.example.proccess.populator.constants.TemplatePopulateImportConstants;
import com.example.proccess.populator.constants.ServicesConstants;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;

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
    private void setRules(Sheet sheet) {
        String serviceTypeSheetName = TemplatePopulateImportConstants.SERVICE_TYPE_NAME;

        DataValidationHelper dvHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);

        List<ServiceType> serviceTypes = serviceTypeSheetPopulator.getServiceTypeList();

        // Create a named range for the service types
        Name serviceTypeName = sheet.getWorkbook().createName();
        serviceTypeName.setNameName("ServiceTypes");
        serviceTypeName.setRefersToFormula(serviceTypeSheetName + "!$B$2:$B$" + (serviceTypes.size() + 1));

        // Cell ranges based on service type list size
        int numRows = serviceTypes.size() + 1;

        // Create a data validation for the "Service Type" column
        CellRangeAddressList serviceTypeRange = new CellRangeAddressList(1, numRows,
                ServicesConstants.SERVICE_TYPE, ServicesConstants.SERVICE_TYPE);

        // Use the values from the serviceTypes list for validation
        DataValidationConstraint serviceTypeConstraint = dvHelper.createFormulaListConstraint("ServiceTypes");
        DataValidation serviceTypeValidation = dvHelper.createValidation(serviceTypeConstraint, serviceTypeRange);

        // Set the error message and title
        serviceTypeValidation.createErrorBox("Invalid Value", "Please select a valid service type");

        // Set the error style to stop the user from entering invalid values
        serviceTypeValidation.setShowErrorBox(true);
        serviceTypeValidation.setShowPromptBox(true);

        CellRangeAddressList booleanRange = new CellRangeAddressList(1, numRows,
                ServicesConstants.IS_ACTIVE, ServicesConstants.IS_ACTIVE);

        DataValidationConstraint boolConstraint = dvHelper.createExplicitListConstraint(new String[]{"True", "False"});
        DataValidation booleanValidation = dvHelper.createValidation(boolConstraint, booleanRange);

        // Set the error message and title for the "Is Active" column
        booleanValidation.createErrorBox("Invalid Value", "Please enter either 'True' or 'False'");

        // Set the error style to stop the user from entering invalid values
        booleanValidation.setShowErrorBox(true);
        booleanValidation.setShowPromptBox(true);

        // Apply the data validation to the sheet
        sheet.addValidationData(serviceTypeValidation);
        sheet.addValidationData(booleanValidation);
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

    public void writeString(int columnIndex, Row row, String value) {
        Cell cell = row.createCell(columnIndex, CellType.STRING);
        cell.setCellValue(value);
    }
}

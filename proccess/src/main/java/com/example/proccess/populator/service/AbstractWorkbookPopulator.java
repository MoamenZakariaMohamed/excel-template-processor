
package com.example.proccess.populator.service;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;
import java.util.regex.Pattern;

public abstract class AbstractWorkbookPopulator implements WorkbookPopulator {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractWorkbookPopulator.class);
    private static final Pattern NAME_REGEX = Pattern.compile("[ @#&()<>,;.:$£€§°\\\\/=!\\?\\-\\+\\*\"\\[\\]]");

    protected void writeInt(int colIndex, Row row, int value) {
        row.createCell(colIndex).setCellValue(value);
    }

    protected void writeLong(int colIndex, Row row, long value) {
        row.createCell(colIndex).setCellValue((double) value);
    }

    protected void writeString(int colIndex, Row row, String value) {
        row.createCell(colIndex).setCellValue(value);
    }

    protected void writeBoolean(int colIndex, Row row, Boolean value) {
        row.createCell(colIndex).setCellValue(value);
    }

    protected void writeDouble(int colIndex, Row row, double value) {
        row.createCell(colIndex).setCellValue(value);
    }

    protected void writeFormula(int colIndex, Row row, String formula) {
        row.createCell(colIndex).setCellFormula(formula);
    }


    protected void writeBigDecimal(int colIndex, Row row, BigDecimal value) {
        row.createCell(colIndex).setCellValue(((value != null) ? value.doubleValue() : 0));
    }


    protected void setSanitized(Name poiName, String roughName) {
        String sanitized = NAME_REGEX.matcher(roughName.trim()).replaceAll("_");
        poiName.setNameName(sanitized);
    }
}

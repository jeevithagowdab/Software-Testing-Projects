package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads tabular test data from .xlsx files using Apache POI so the same
 * @Test method can run against many data rows via a TestNG DataProvider,
 * instead of hardcoding values into the test class.
 *
 * Expected sheet layout: row 0 = headers, row 1+ = data.
 */
public class ExcelUtils {

    private static final Logger logger = LogManager.getLogger(ExcelUtils.class);

    private ExcelUtils() {
    }

    /**
     * Reads an entire sheet into a List of String arrays (one array per row,
     * header row excluded). Suitable for direct return from a
     * @DataProvider method as Object[][].
     */
    public static Object[][] getSheetData(String filePath, String sheetName) {
        List<String[]> rows = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet '" + sheetName + "' not found in " + filePath);
            }

            int rowCount = sheet.getPhysicalNumberOfRows();
            int colCount = sheet.getRow(0).getLastCellNum();

            // skip header row (i = 1)
            for (int i = 1; i < rowCount; i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String[] rowData = new String[colCount];
                for (int j = 0; j < colCount; j++) {
                    Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    rowData[j] = getCellValueAsString(cell);
                }
                rows.add(rowData);
            }

            logger.info("Loaded {} data rows from sheet '{}'", rows.size(), sheetName);

        } catch (IOException e) {
            logger.error("Failed to read Excel file: {}", filePath, e);
            throw new RuntimeException("Failed to read Excel file: " + filePath, e);
        }

        Object[][] data = new Object[rows.size()][];
        for (int i = 0; i < rows.size(); i++) {
            data[i] = rows.get(i);
        }
        return data;
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null) return "";

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                double numValue = cell.getNumericCellValue();
                if (numValue == Math.floor(numValue)) {
                    return String.valueOf((long) numValue);
                }
                return String.valueOf(numValue);
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
            default:
                return "";
        }
    }
}

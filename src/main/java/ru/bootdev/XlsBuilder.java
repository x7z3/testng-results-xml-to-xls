package ru.bootdev;

import org.apache.poi.ss.usermodel.*;
import ru.bootdev.model.Suite;
import ru.bootdev.model.Test;
import ru.bootdev.model.TestMethod;

import java.io.File;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class XlsBuilder {

    private static final String REGULAR_FONT = "regular";
    private static final String REGULAR_BOLD_FONT = "bold";
    private static final String HEADER_FONT = "header";
    private static final String PASS_FONT = "pass";
    private static final String FAIL_FONT = "fail";

    private final Map<Suite, Map<Test, List<TestMethod>>> data;
    private XlsEditor xlsEditor;

    private String excludeMethodNames = "";

    public XlsBuilder(Map<Suite, Map<Test, List<TestMethod>>> data) {
        this.data = data;
    }

    public XlsBuilder(Map<Suite, Map<Test, List<TestMethod>>> data, String excludeMethodNames) {
        this.data = data;
        this.excludeMethodNames = excludeMethodNames;
    }

    public void setExcludeMethodNames(String excludeMethodNames) {
        this.excludeMethodNames = excludeMethodNames;
    }

    public void generateXls() {
        data.forEach(this::createWorkbook);
    }

    private void createWorkbook(Suite suite, Map<Test, List<TestMethod>> testListMap) {
        xlsEditor = new XlsEditor();

        xlsEditor.createStyle((cellStyle, font) -> {
                    font.setFontHeightInPoints((short) 12);
                    cellStyle.setFont(font);
                    cellStyle.setWrapText(true);
                    cellStyle.setAlignment(HorizontalAlignment.LEFT);
                    cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                }).saveStyle(REGULAR_FONT)

                .createStyle((cellStyle, font) -> {
                    font.setBold(true);
                    font.setFontHeightInPoints((short) 12);
                    cellStyle.setFont(font);
                    cellStyle.setWrapText(true);
                    cellStyle.setAlignment(HorizontalAlignment.LEFT);
                    cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                }).saveStyle(REGULAR_BOLD_FONT)

                .createStyle((cellStyle, font) -> {
                    font.setBold(true);
                    font.setFontHeightInPoints((short) 14);
                    cellStyle.setFont(font);
                    cellStyle.setWrapText(true);
                    cellStyle.setAlignment(HorizontalAlignment.CENTER);
                    cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                    cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
                    cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    cellStyle.setBorderTop(BorderStyle.THIN);
                    cellStyle.setBorderBottom(BorderStyle.THIN);
                    cellStyle.setBorderLeft(BorderStyle.THIN);
                    cellStyle.setBorderRight(BorderStyle.THIN);
                }).saveStyle(HEADER_FONT)

                .createStyle((cellStyle, font) -> {
                    font.setBold(true);
                    font.setFontHeightInPoints((short) 12);
                    cellStyle.setFont(font);
                    cellStyle.setWrapText(true);
                    cellStyle.setAlignment(HorizontalAlignment.CENTER);
                    cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                    cellStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
                    cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                }).saveStyle(PASS_FONT)

                .createStyle((cellStyle, font) -> {
                    font.setBold(true);
                    font.setFontHeightInPoints((short) 12);
                    cellStyle.setFont(font);
                    cellStyle.setWrapText(true);
                    cellStyle.setAlignment(HorizontalAlignment.CENTER);
                    cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                    cellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
                    cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                }).saveStyle(FAIL_FONT)
        ;
        testListMap.forEach(this::createSheet);
        xlsEditor.saveToFile(new File(suite.getName() + ".xlsx"));
    }

    private void createSheet(Test test, List<TestMethod> testMethods) {
        xlsEditor.createSheet(test.getName())
                .createRow()
                .createCell().setCellValue("Test name").styleToCell(REGULAR_BOLD_FONT)
                .createCell().setCellValue(test.getName()).styleToCell(REGULAR_FONT)
                .createRow()
                .createCell().setCellValue("Test duration in minutes").styleToCell(REGULAR_BOLD_FONT)
                .createCell().setCellValue(Duration.ofMillis(test.getDurationMs()).toMinutes()).styleToCell(REGULAR_FONT)
                .createRow()
                .createCell().setCellValue("Start time").styleToCell(REGULAR_BOLD_FONT)
                .createCell().setCellValue(test.getStartedAt()).styleToCell(REGULAR_FONT)
                .createRow()
                .createCell().setCellValue("Finish time").styleToCell(REGULAR_BOLD_FONT)
                .createCell().setCellValue(test.getFinishedAt()).styleToCell(REGULAR_FONT)
                .createRow()
                .createRow().row(row -> row.setHeightInPoints(18))
                .createCell().setCellValue("Description").styleToCell(HEADER_FONT)
                .createCell().setCellValue("Status").styleToCell(HEADER_FONT)
                .createCell().setCellValue("Error type").styleToCell(HEADER_FONT)
                .createCell().setCellValue("Error message").styleToCell(HEADER_FONT)
        ;

        testMethods.forEach(this::fillSheet);
        xlsEditor.sheet(sheet -> IntStream.range(0, 10).forEach(sheet::autoSizeColumn));
    }

    private void fillSheet(TestMethod testMethod) {
        if (testMethod.getName().matches(excludeMethodNames)) return;
        xlsEditor.createRow()
                .createCell().setCellValue(testMethod.getDescription()).styleToCell(REGULAR_FONT)
                .createCell().setCellValue(testMethod.getStatus()).styleToCell(REGULAR_FONT)
                .styleToCell(testMethod.getStatus().matches("(?i)pass") ? PASS_FONT : FAIL_FONT)
                .createCell().setCellValue(testMethod.getExceptionClass()).styleToCell(REGULAR_FONT)
                .createCell().setCellValue(testMethod.getExceptionMessage()).styleToCell(REGULAR_FONT)
        ;
    }
}

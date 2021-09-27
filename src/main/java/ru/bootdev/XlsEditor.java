package ru.bootdev;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class XlsEditor {

    private final Workbook workbook;

    private Sheet currentSheet;
    private Row currentRow;
    private Cell currentCell;

    private int sheetIndex = 0;
    private int rowIndex = 0;
    private int cellIndex = 0;

    private CellStyle currentStyle;
    private Font currentFont;

    private final Map<String, CellStyle> savedStyles = new HashMap<>();

    public XlsEditor(Workbook workbook) {
        this.workbook = workbook;
    }

    public XlsEditor() {
        this.workbook = new XSSFWorkbook();
    }

    public XlsEditor sheet(int i) {
        currentSheet = workbook.getSheetAt(i);
        return this;
    }

    public XlsEditor sheet(Consumer<Sheet> lambda) {
        lambda.accept(currentSheet);
        return this;
    }

    public XlsEditor row(int i) {
        currentRow = currentSheet.getRow(i);
        return this;
    }

    public XlsEditor row(Consumer<Row> lambda) {
        lambda.accept(currentRow);
        return this;
    }

    public XlsEditor cell(int i) {
        currentCell = currentRow.getCell(i);
        return this;
    }

    public XlsEditor cell(Consumer<Cell> lambda) {
        lambda.accept(currentCell);
        return this;
    }

    public XlsEditor setCellValue(String s) {
        currentCell.setCellValue(s);
        return this;
    }

    public XlsEditor setCellValue(double v) {
        currentCell.setCellValue(v);
        return this;
    }

    public XlsEditor setCellValue(boolean b) {
        currentCell.setCellValue(b);
        return this;
    }

    public XlsEditor setCellValue(Date date) {
        currentCell.setCellValue(date);
        return this;
    }

    public XlsEditor setCellValue(LocalDate value) {
        currentCell.setCellValue(value);
        return this;
    }

    public XlsEditor setCellValue(Calendar calendar) {
        currentCell.setCellValue(calendar);
        return this;
    }

    public XlsEditor setCellValue(LocalDateTime localDateTime) {
        currentCell.setCellValue(localDateTime);
        return this;
    }

    public XlsEditor setCellValue(RichTextString richTextString) {
        currentCell.setCellValue(richTextString);
        return this;
    }

    public XlsEditor createSheet(String name) {
        currentSheet = workbook.createSheet(name);
        cellIndex = rowIndex = 0;
        sheetIndex++;
        return this;
    }

    public XlsEditor createSheet() {
        currentSheet = workbook.createSheet();
        return this;
    }

    public XlsEditor createRow(int i) {
        currentRow = currentSheet.createRow(i);
        return this;
    }

    public XlsEditor createRow() {
        currentRow = currentSheet.createRow(rowIndex++);
        cellIndex = 0;
        return this;
    }

    public XlsEditor createCell(int i) {
        currentCell = currentRow.createCell(i);
        return this;
    }

    public XlsEditor createCell() {
        currentCell = currentRow.createCell(cellIndex++);
        return this;
    }

    public XlsEditor createStyle(BiConsumer<CellStyle, Font> lambda) {
        currentStyle = workbook.createCellStyle();
        lambda.accept(currentStyle, workbook.createFont());
        return this;
    }

    public XlsEditor saveStyle(String styleName) {
        savedStyles.put(styleName, currentStyle);
        return this;
    }

    public XlsEditor loadStyle(String styleName) {
        currentStyle = savedStyles.get(styleName);
        return this;
    }

    public XlsEditor styleToCell() {
        currentCell.setCellStyle(currentStyle);
        return this;
    }

    public XlsEditor styleToCell(String styleName) {
        return loadStyle(styleName).styleToCell();
    }

    public XlsEditor styleToRow() {
        currentRow.setRowStyle(currentStyle);
        return this;
    }

    public XlsEditor styleToRow(String styleName) {
        return loadStyle(styleName).styleToRow();
    }

    public XlsEditor takeRowStyle() {
        currentStyle = currentRow.getRowStyle();
        return this;
    }

    public XlsEditor takeCellStyle() {
        currentStyle = currentCell.getCellStyle();
        return this;
    }

    public XlsEditor createFont(Consumer<Font> lambda) {
        currentFont = workbook.createFont();
        lambda.accept(currentFont);
        return this;
    }

    public XlsEditor applyFont() {
        currentStyle.setFont(currentFont);
        return this;
    }

    public void saveToFile(File file) {
        try (OutputStream outputFile = new FileOutputStream(file)) {
            workbook.write(outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getSheetIndex() {
        return sheetIndex;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public int getCellIndex() {
        return cellIndex;
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public Sheet getCurrentSheet() {
        return currentSheet;
    }

    public Row getCurrentRow() {
        return currentRow;
    }

    public Cell getCurrentCell() {
        return currentCell;
    }

    public CellStyle getCurrentStyle() {
        return currentStyle;
    }

    public Font getCurrentFont() {
        return currentFont;
    }

    public void setCurrentSheet(Sheet currentSheet) {
        this.currentSheet = currentSheet;
    }

    public void setCurrentRow(Row currentRow) {
        this.currentRow = currentRow;
    }

    public void setCurrentCell(Cell currentCell) {
        this.currentCell = currentCell;
    }

    public void setCurrentStyle(CellStyle currentStyle) {
        this.currentStyle = currentStyle;
    }

    public void setCurrentFont(Font currentFont) {
        this.currentFont = currentFont;
    }

    public Map<String, CellStyle> getSavedStyles() {
        return savedStyles;
    }
}

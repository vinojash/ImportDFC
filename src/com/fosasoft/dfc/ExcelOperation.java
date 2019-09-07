package com.fosasoft.dfc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.fosasoft.bean.ApplicationConstant;
import com.fosasoft.bean.Attribute;
import com.fosasoft.bean.ExcelObject;

public class ExcelOperation {

	private String successJobSheet = null;
	private String failureJobSheet = null;
	private List<String> allSheetName = new ArrayList<String>();
	private HashMap<Integer, String> hashAttribute = new HashMap<Integer, String>();

	public ExcelOperation() throws FileNotFoundException, IOException {
		ApplicationConstant constant = new ApplicationConstant();
		this.successJobSheet = constant.getPathSuccessfullyUploaded() + "\\SuccessJobSheet.xls";
		this.failureJobSheet = constant.getPathFailedUpload() + "\\FailureJobSheet.xls";
	}

	public String getSuccessJobSheet() {
		return this.successJobSheet;
	}

	public String getFailureJobSheet() {
		return this.failureJobSheet;
	}

	public void saveWorkBook(Workbook workbook, String pathExcel) {
		try {
			OutputStream fileOut = new FileOutputStream(pathExcel);
			workbook.write(fileOut);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Workbook createExcel(Boolean isSuccessJobSheet) {
		String pathExcel = null;
		if (isSuccessJobSheet) {
			pathExcel = successJobSheet;
		} else {
			pathExcel = failureJobSheet;
		}

		//Workbook wb = new HSSFWorkbook();
		Workbook wb = new XSSFWorkbook();
		try (OutputStream fileOut = new FileOutputStream(pathExcel)) {
			Iterator<String> iteratorSheetName = allSheetName.iterator();
			while (iteratorSheetName.hasNext()) {
				Sheet tempSheet = wb.createSheet(iteratorSheetName.next());
				Iterator<String> iteratorColumn = hashAttribute.values().iterator();
				int count = 0;
				Row rowTemp = tempSheet.createRow(0);
				while (iteratorColumn.hasNext()) {
					String columnValue = iteratorColumn.next();
					Cell cellTemp = rowTemp.createCell(count);
					cellTemp.setCellValue(columnValue);
					count++;
				}
			}
			wb.createSheet("others");
			wb.write(fileOut);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wb;
	}

	public List<ExcelObject> readMetaDataFromExcel(String excelPath) {

		Workbook workbook = null;
		List<ExcelObject> listExcelObject = new ArrayList<ExcelObject>();
		try {
			FileInputStream excelFile = new FileInputStream(new File(excelPath));
			workbook = new XSSFWorkbook(excelFile);
			Iterator<Sheet> sheetIterator = workbook.sheetIterator();
			while (sheetIterator.hasNext()) {
				Sheet tempSheet = sheetIterator.next();
				allSheetName.add(tempSheet.getSheetName());
				listExcelObject.addAll(getSheetInformtion(tempSheet));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(workbook);
		}
		return listExcelObject;
	}

	private HashMap<Integer, String> getAttributeNames(Row firstRow) {
		Iterator<Cell> cellIterator = firstRow.iterator();

		while (cellIterator.hasNext()) {
			Cell currentCell = cellIterator.next();
			hashAttribute.put(currentCell.getColumnIndex(), currentCell.getStringCellValue().toLowerCase());
		}
		return hashAttribute;
	}

	private List<ExcelObject> getSheetInformtion(Sheet sheet) {

		Row firstRow = sheet.getRow(sheet.getFirstRowNum());
		HashMap<Integer, String> attribute = getAttributeNames(firstRow);

		List<ExcelObject> listExcelObject = new ArrayList<ExcelObject>();

		Iterator<Row> rowIterator = sheet.iterator();

		while (rowIterator.hasNext()) {

			Row currentRow = rowIterator.next();

			if (currentRow.getRowNum() != 0) {
				Iterator<Cell> cellIterator = currentRow.iterator();

				ExcelObject excelObject = new ExcelObject();

				while (cellIterator.hasNext()) {
					Cell currentCell = cellIterator.next();
					if (currentCell.getColumnIndex() == 0) {
						excelObject.setObjectId(currentCell.getStringCellValue().trim());
					} else {
						String value = "";
						switch (currentCell.getCellType()) {
						case STRING: {
							value = currentCell.getStringCellValue();
							break;
						}
						case NUMERIC: {
							value = NumberToTextConverter.toText(currentCell.getNumericCellValue());
							break;
						}
						default:
							break;
						}

						String label = attribute.get(currentCell.getColumnIndex());
						excelObject.addAttributes(label, value);
					}
				}
				excelObject.setObjectType(sheet.getSheetName());
				listExcelObject.add(excelObject);
			}
		}
		return listExcelObject;
	}

	public Workbook insertEntry(Workbook workbook, ExcelObject excelObject) {
		Sheet tempSheet = workbook.getSheet(excelObject.getObjectType());
		Row rowTemp = tempSheet.createRow(tempSheet.getLastRowNum() + 1);
		Iterator<Attribute> iteratorAttribute = excelObject.getAttributes().iterator();

		Cell cellTemp = rowTemp.createCell(0);
		cellTemp.setCellValue(excelObject.getObjectId());
		int count = 1;
		while (iteratorAttribute.hasNext()) {
			Attribute attribute = iteratorAttribute.next();
			cellTemp = rowTemp.createCell(count);
			cellTemp.setCellValue(attribute.getValue());
			count++;
		}
		return workbook;
	}
}

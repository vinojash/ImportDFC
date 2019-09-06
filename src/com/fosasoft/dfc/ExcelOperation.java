package com.fosasoft.dfc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

import com.fosasoft.bean.ExcelObject;

public class ExcelOperation {

	public List<ExcelObject> readMetaDataFromExcel(String excelPath) {

		Workbook workbook = null;
		List<ExcelObject> listExcelObject = new ArrayList<ExcelObject>();
		try {
			FileInputStream excelFile = new FileInputStream(new File(excelPath));
			workbook = new XSSFWorkbook(excelFile);
			Iterator<Sheet> sheetIterator = workbook.sheetIterator();
			while (sheetIterator.hasNext()) {
				Sheet tempSheet = sheetIterator.next();
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
		HashMap<Integer, String> hashAttribute = new HashMap<Integer, String>();
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
}

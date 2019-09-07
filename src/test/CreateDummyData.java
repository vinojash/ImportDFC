package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.fosasoft.bean.ApplicationConstant;
import com.fosasoft.bean.Attribute;
import com.fosasoft.bean.ExcelObject;

public class CreateDummyData {

	private static String pathFolder = null;
	private static int numberOfObject = 0;
	private static String pathExcel = null;
	private static String pathPDF = System.getProperty("user.dir") + "\\src\\sample.pdf";
	private static List<String> sheetNames = new ArrayList<String>();
	private static Workbook myWorkbook = null;

	public static void main(String[] args) throws Exception {
		System.out.println("Test data creation started..!");
		ApplicationConstant constant = new ApplicationConstant();
		pathFolder = constant.getPathTest();
		numberOfObject = constant.getNoOfTestObject();
		pathExcel = pathFolder + "\\Garrett_ObjectTypes_Execution.xlsx";

		createDirectory(pathFolder + "\\SuccessFolder");
		createDirectory(pathFolder + "\\FailedFolder");
		doOperation();
		System.out.println("Test data creation done..!");
	}

	private static void doOperation() throws FileNotFoundException, IOException {
		sheetNames.add("hon_ie75_import_job_docs");
		sheetNames.add("hon_ie75_export_job_docs");
		sheetNames.add("hon_ie75_foc_job_docs");
		myWorkbook = createExcel();

		for (int i = 1; i <= numberOfObject; i++) {
			String temp = "0000000000000000" + i;
			String reverseString = new StringBuilder(temp).reverse().toString();
			String dummyObjectId = "09" + new StringBuilder(reverseString.substring(0, 14)).reverse().toString();
			if (i < numberOfObject / 3) {
				createTestData1(dummyObjectId, i);
			} else if (i < (numberOfObject * 2) / 3) {
				createTestData2(dummyObjectId, i);
			} else {
				createTestData3(dummyObjectId, i);
			}
		}
		OutputStream fileOut = new FileOutputStream(pathExcel);
		myWorkbook.write(fileOut);
	}

	private static void createTestData1(String id, int count) throws IOException {
		// hon_ie75_import_job_docs
		myWorkbook = insertEntry(myWorkbook, getExcelObject(id, "hon_ie75_import_job_docs", count));
		int lastDigit = count % 10;
		copyFile(pathPDF, pathFolder + "\\Trade Compliance Cabinet\\Import Jobs\\" + "02764735452019" + lastDigit + "\\"
				+ id + "-sample.pdf");
	}

	private static void createTestData2(String id, int count) throws IOException {
		// hon_ie75_export_job_docs
		myWorkbook = insertEntry(myWorkbook, getExcelObject(id, "hon_ie75_export_job_docs", count));
		int lastDigit = count % 10;
		copyFile(pathPDF, pathFolder + "\\Trade Compliance Cabinet\\Export Jobs\\" + "02764735452019" + lastDigit + "\\"
				+ id + "-sample.pdf");
	}

	private static void createTestData3(String id, int count) throws IOException {
		// hon_ie75_foc_job_docs
		myWorkbook = insertEntry(myWorkbook, getExcelObject(id, "hon_ie75_foc_job_docs", count));
		int lastDigit = count % 10;
		copyFile(pathPDF, pathFolder + "\\Trade Compliance Cabinet\\FOC\\" + "02764735452019" + lastDigit + "\\" + id
				+ "-sample.pdf");
	}

	public static Workbook createExcel() {
		Workbook wb = new XSSFWorkbook();
		try (OutputStream fileOut = new FileOutputStream(pathExcel)) {
			Iterator<String> iteratorSheetName = sheetNames.iterator();

			while (iteratorSheetName.hasNext()) {
				Sheet tempSheet = wb.createSheet(iteratorSheetName.next());
				Row rowTemp = tempSheet.createRow(0);
				Cell cellTemp = null;
				cellTemp = rowTemp.createCell(0);
				cellTemp.setCellValue("Object_ID");
				cellTemp = rowTemp.createCell(1);
				cellTemp.setCellValue("File_Name");
				cellTemp = rowTemp.createCell(2);
				cellTemp.setCellValue("Job_Id");
				cellTemp = rowTemp.createCell(3);
				cellTemp.setCellValue("Document_Type");
				cellTemp = rowTemp.createCell(4);
				cellTemp.setCellValue("Subject_to_Export_Control");
				cellTemp = rowTemp.createCell(5);
				cellTemp.setCellValue("Subject_to_Data_Piracy");
			}

			wb.write(fileOut);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return wb;
	}

	public static Workbook insertEntry(Workbook workbook, ExcelObject excelObject) {
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

	private static void copyFile(String source, String destination) throws IOException {
		File sourceFile = new File(source);
		File destinationFile = new File(destination);
		destinationFile.getParentFile().mkdirs();

		Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
	}

	private static ExcelObject getExcelObject(String id, String objectType, int count) {
		ExcelObject excelobj = new ExcelObject();
		excelobj.setObjectId(id);
		excelobj.setObjectType(objectType);
		excelobj.addAttributes("File_Name", "sample file name " + count);
		excelobj.addAttributes("Job_Id", count + "");
		excelobj.addAttributes("Document_Type", "some type");
		excelobj.addAttributes("Subject_to_Export_Control", "Yes");
		excelobj.addAttributes("Subject_to_Data_Piracy", "No");
		return excelobj;
	}

	private static Boolean createDirectory(String path) {
		Boolean success = (new File(path)).mkdirs();
		return success;
	}
}

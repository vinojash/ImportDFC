package com.fosasoft.dfc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.documentum.fc.client.IDfDocument;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;
import com.fosasoft.bean.ContentObject;

public class Import {

	public String createDocument(IDfSession session, ContentObject cObject) throws DfException {
		IDfDocument document = null;
		try {
			document = (IDfDocument) session.newObject(cObject.getObjectType());
			if (document != null) {
				document.setObjectName(cObject.getObjectName());
				document.setContentType("pdf");
				document.setFile(cObject.getPath());
				// document.link("");
				document.save();
			}
		} catch (Exception e) {
			logFiledInfo(cObject);
			e.printStackTrace();
		}

		return document.getObjectId().toString();
	}

	public List<Path> readFilesFromPath(String localPath) throws IOException {
		List<Path> files = new ArrayList<Path>();
		try (Stream<Path> paths = Files.walk(Paths.get(localPath))) {
			paths.filter(Files::isRegularFile).forEach(files::add);
		}
		return files;
	}

	public void createContentObjectFromExcel(IDfSession session, String excelPath, String sheetName) throws DfException {
		List<ContentObject> ls = readMetaDataFromExcel(excelPath, sheetName);
		Iterator<ContentObject> iterator = ls.iterator();
		String objectId = null;
		while (iterator.hasNext()) {
			ContentObject cobject = iterator.next();
			System.out.println(cobject.getObjectName());
			System.out.println(cobject.getPath());
			objectId = createDocument(session, cobject);
			System.out.println(objectId);
		}
	}

	private List<ContentObject> readMetaDataFromExcel(String excelPath, String sheetName) {

		List<ContentObject> cObjects = new ArrayList<>();
		try {

			FileInputStream excelFile = new FileInputStream(new File(excelPath));
			Workbook workbook = new XSSFWorkbook(excelFile);
			Sheet datatypeSheet = workbook.getSheet(sheetName);
			Iterator<Row> iterator = datatypeSheet.iterator();

			while (iterator.hasNext()) {

				Row currentRow = iterator.next();

				if (currentRow.getRowNum() != 0) {
					Iterator<Cell> cellIterator = currentRow.iterator();
					ContentObject cObject = new ContentObject();
					while (cellIterator.hasNext()) {
						Cell currentCell = cellIterator.next();

						if (currentCell.getColumnIndex() == 0) {
							cObject.setObjectName(currentCell.getStringCellValue());
						}
						if (currentCell.getColumnIndex() == 7) {
							cObject.setPath(currentCell.getStringCellValue());
						}
					}
					cObjects.add(cObject);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cObjects;
	}

	private void logFiledInfo(ContentObject cObject) {
		System.err.println(cObject.getObjectName());
		System.err.println(cObject.getPath());
	}
}

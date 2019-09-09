package com.fosasoft.dfc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import org.apache.poi.ss.usermodel.Workbook;

import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;
import com.fosasoft.bean.ApplicationConstant;
import com.fosasoft.bean.ExcelObject;

public class Import {

	private String pathFolder = null;
	private String pathContentServer = null;
	private String pathSuccessfullyUploaded = null;
	private String pathFailedUpload = null;
	private List<ExcelObject> excelObjects;
	private Iterator<ExcelObject> iteratorExcelObject;
	private IDfSession session = null;
	private HelperDFC helperDfc = new HelperDFC();
	private SessionDFC sessionDFC = new SessionDFC();
	private Workbook successWorkBook = null;
	private Workbook failedWorkBook = null;
	private ExcelOperation excelOperation = new ExcelOperation();
	private File fileFolder = null;

	public Import() throws Exception {
		ApplicationConstant constant = new ApplicationConstant();
		this.pathFolder = constant.getPathFolder();
		this.pathContentServer = constant.getPathContentServer();
		this.pathSuccessfullyUploaded = constant.getPathSuccessfullyUploaded();
		this.pathFailedUpload = constant.getPathFailedUpload();
		excelObjects = excelOperation.readMetaDataFromExcel(constant.getPathExcel());
		successWorkBook = excelOperation.createExcel(true);
		failedWorkBook = excelOperation.createExcel(false);
		this.fileFolder = new File(pathFolder);
		this.session = sessionDFC.createSessionManager(constant.getRepository(), constant.getUserName(), constant.getPassword());
	}

	private ExcelObject getAttributeFromExcel(String objectId) {
		iteratorExcelObject = excelObjects.iterator();
		while (iteratorExcelObject.hasNext()) {
			ExcelObject excelObject = iteratorExcelObject.next();
			if (excelObject.getObjectId().equalsIgnoreCase(objectId)) {
				excelObjects.remove(excelObject);
				return excelObject;
			}
		}
		return null;
	}

	private void getAttributes(Path path) throws DfException, IOException {
		String absolutePath = path.toAbsolutePath().toString();
		String fileName = path.getFileName().toString();
		String pathContentServer = this.pathContentServer + absolutePath.replace(pathFolder, "").replace("\\" + fileName, "");
		String objectId = fileName.split("-")[0].trim();
		String objectName = fileName.split("-")[1].split("\\.")[0].trim();
		String fileExtension = fileName.split("-")[1].split("\\.")[1].trim();
		pathContentServer = pathContentServer.replace("\\", "/");
		ExcelObject excelObject = null;
		excelObject = this.getAttributeFromExcel(objectId);
		if (null != excelObject) {
			excelObject.setPathLocalFile(absolutePath);
			excelObject.setPathContentServer(pathContentServer);
			excelObject.setObjectName(objectName);
			excelObject.setFileExtension(fileExtension);
			excelObject = helperDfc.createDocument(session, excelObject);
			System.err.println(excelObject.print());
			if (excelObject.getIsSuccess()) {
				successWorkBook = excelOperation.insertEntry(successWorkBook, excelObject);
				String newPath = absolutePath.replace(pathFolder, pathSuccessfullyUploaded);
				moveFile(absolutePath, newPath);
			} else {
				String newPath = absolutePath.replace(pathFolder, pathFailedUpload);
				Files.move(path, Paths.get(newPath), StandardCopyOption.REPLACE_EXISTING);
				moveFile(absolutePath, newPath);
			}

		} else {
			excelObject = new ExcelObject();
			excelObject.setObjectType("others");
			excelObject.setObjectId(objectId);
			excelObject.addAttributes("message", "metadata not found in excel sheet");
			failedWorkBook = excelOperation.insertEntry(failedWorkBook, excelObject);
			String newPath = absolutePath.replace(pathFolder, pathFailedUpload);
			moveFile(absolutePath, newPath);
			failedWorkBook = excelOperation.insertEntry(failedWorkBook, excelObject);
		}

	}

	private void moveFile(String oldPath, String newPath) {
		File oFile = new File(oldPath);
		File nFile = new File(newPath);
		nFile.getParentFile().mkdirs();

		if (oFile.renameTo(nFile)) {
			oFile.delete();
		}
	}

	public void readFilesFromPath() throws Exception {
		try (Stream<Path> paths = Files.walk(Paths.get(pathFolder))) {
			paths.filter(Files::isRegularFile).forEach(path -> {
				try {
					getAttributes(path);
				} catch (DfException | IOException e) {
					e.printStackTrace();
				}
			});
		} finally {
			if (null != this.session) {
				this.sessionDFC.releaseSession(this.session);
			}
			excelOperation.saveWorkBook(successWorkBook, excelOperation.getSuccessJobSheet());
			excelOperation.saveWorkBook(failedWorkBook, excelOperation.getFailureJobSheet());
		}
	}

	/*
	 * 
	 * For older version of JAVA
	 * 
	 */

//	public void readFilesFromPath() {
//		getFilesFromPath(fileFolder);
//	}

	public void getFilesFromPath(File folder) throws Exception {
		try {
			for (final File fileEntry : folder.listFiles()) {
				if (fileEntry.isDirectory()) {
					getFilesFromPath(fileEntry);
				} else {
					System.out.println(fileEntry.getName());
				}
			}
		} finally {
			if (null != this.session) {
				this.sessionDFC.releaseSession(this.session);
			}
			excelOperation.saveWorkBook(successWorkBook, excelOperation.getSuccessJobSheet());
			excelOperation.saveWorkBook(failedWorkBook, excelOperation.getFailureJobSheet());

		}

	}
}

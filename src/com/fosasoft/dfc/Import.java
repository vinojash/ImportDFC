package com.fosasoft.dfc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;
import com.fosasoft.bean.ApplicationConstant;
import com.fosasoft.bean.ExcelObject;

public class Import {

	private String pathFolder = null;
	private String pathContentServer = null;
	private List<ExcelObject> excelObjects;
	private Iterator<ExcelObject> iteratorExcelObject;
	private IDfSession session = null;
	private HelperDFC helperDfc = new HelperDFC();
	private SessionDFC sessionDFC = new SessionDFC();

	public Import() throws Exception {
		ApplicationConstant constant = new ApplicationConstant();
		this.pathFolder = constant.getPathFolder();
		this.pathContentServer = constant.getPathContentServer();
		excelObjects = new ExcelOperation().readMetaDataFromExcel(constant.getPathExcel());
		this.session = sessionDFC.createSessionManager(constant.getRepository(), constant.getUserName(),
				constant.getPassword());
	}

	private ExcelObject getAttributeFromExcel(String objectId) {
		iteratorExcelObject = excelObjects.iterator();
		while (iteratorExcelObject.hasNext()) {
			ExcelObject excelObject = iteratorExcelObject.next();
			if (excelObject.getObjectId().equalsIgnoreCase(objectId)) {
				return excelObject;
			}
		}
		return null;
	}

	private void getAttributes(Path path) throws DfException {
		String absolutePath = path.toAbsolutePath().toString();
		String fileName = path.getFileName().toString();
		String pathContentServer = this.pathContentServer
				+ absolutePath.replace(pathFolder, "").replace("\\" + fileName, "");
		String objectId = fileName.split("-")[0].trim();
		String objectName = fileName.split("-")[1].split("\\.")[0].trim();
		String fileExtension = fileName.split("-")[1].split("\\.")[1].trim();

		ExcelObject excelObject = this.getAttributeFromExcel(objectId);
		excelObject.setPathLocalFile(absolutePath);
		excelObject.setPathContentServer(pathContentServer);
		excelObject.setObjectName(objectName);
		excelObject.setFileExtension(fileExtension);

		// print the output
		excelObject.print();

		//helperDfc.createDocument(session, excelObject);

	}

	public void readFilesFromPath() throws IOException {
		try (Stream<Path> paths = Files.walk(Paths.get(pathFolder))) {
			paths.filter(Files::isRegularFile).forEach(path -> {
				try {
					getAttributes(path);
				} catch (DfException e) {
					e.printStackTrace();
				}
			});
		}
	}
}

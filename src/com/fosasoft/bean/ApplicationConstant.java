package com.fosasoft.bean;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ApplicationConstant {
	private Properties properties;
	private String importPropertyPath = null;
	private String pathFolder = null;
	private String pathExcel = null;
	private String pathContentServer = null;
	private String repository = null;
	private String userName = null;
	private String password = null;
	private String pathSuccessfullyUploaded = null;
	private String pathFailedUpload = null;
	private String pathTest = null;
	private int NoOfTestObject = 0;

	public ApplicationConstant() throws FileNotFoundException, IOException {
		properties = new Properties();
		importPropertyPath = System.getProperty("user.dir") + "\\src\\import.properties";
		properties.load(new FileInputStream(importPropertyPath));
		this.pathExcel = properties.getProperty("pathExcel");
		this.pathFolder = properties.getProperty("pathFolder");
		this.pathContentServer = properties.getProperty("pathContentServer");
		this.repository = properties.getProperty("repository");
		this.userName = properties.getProperty("userName");
		this.password = properties.getProperty("password");
		this.pathSuccessfullyUploaded = properties.getProperty("pathSuccessfullyUploaded");
		this.pathFailedUpload = properties.getProperty("pathFailedUpload");
		this.pathTest = properties.getProperty("pathTest");
		this.NoOfTestObject = Integer.parseInt(properties.getProperty("NoOfTestObject"));
	}

	
	public void setPathExcel(String pathExcel) {
		properties.setProperty("pathExcel", pathExcel);
		this.pathExcel = pathFolder;
		this.storeProperty();
	}

	private void storeProperty() {
		try {
			properties.store(new FileOutputStream(importPropertyPath), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setPathFolder(String pathFolder) {
		properties.setProperty("pathFolder", pathFolder);
		this.pathFolder = pathFolder;
		this.storeProperty();
	}

	public void setPathContentServer(String pathContentServer) {
		properties.setProperty("pathContentServer", pathContentServer);
		this.pathContentServer = pathContentServer;
		this.storeProperty();
	}

	public String getPathFolder() {
		return pathFolder;
	}

	public String getPathExcel() {
		return pathExcel;
	}

	public String getPathContentServer() {
		return pathContentServer;
	}

	public String getRepository() {
		return repository;
	}

	public void setRepository(String repository) {
		properties.setProperty("repository", repository);
		this.repository = repository;
		this.storeProperty();
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		properties.setProperty("userName", userName);
		this.userName = userName;
		this.storeProperty();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		properties.setProperty("password", password);
		this.password = password;
		this.storeProperty();
	}

	public String getPathSuccessfullyUploaded() {
		return pathSuccessfullyUploaded;
	}

	public void setPathSuccessfullyUploaded(String pathSuccessfullyUploaded) {
		properties.setProperty("pathSuccessfullyUploaded", pathSuccessfullyUploaded);
		this.pathSuccessfullyUploaded = pathSuccessfullyUploaded;
		this.storeProperty();
	}

	public String getPathFailedUpload() {
		return pathFailedUpload;
	}

	public void setPathFailedUpload(String pathFailedUpload) {
		properties.setProperty("pathFailedUpload", pathFailedUpload);
		this.pathFailedUpload = pathFailedUpload;
		this.storeProperty();
	}

	public String getPathTest() {
		return pathTest;
	}

	public void setPathTest(String pathTest) {
		this.pathTest = pathTest;
		properties.setProperty("pathTest", pathTest);
		this.storeProperty();
	}

	public int getNoOfTestObject() {
		return NoOfTestObject;
	}

	public void setNoOfTestObject(int noOfTestObject) {
		NoOfTestObject = noOfTestObject;
		properties.setProperty("noOfTestObject", noOfTestObject + "");
		this.storeProperty();
	}
}

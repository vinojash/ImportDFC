package com.fosasoft.bean;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ApplicationConstant {
	private static Properties properties;

	public ApplicationConstant() throws FileNotFoundException, IOException {
		properties = new Properties();
		String importPropertyPath = System.getProperty("user.dir") + "\\src\\import.properties";
		properties.load(new FileInputStream(importPropertyPath));
		this.pathExcel = properties.getProperty("pathExcel");
		this.pathFolder = properties.getProperty("pathFolder");
		this.pathContentServer = properties.getProperty("pathContentServer");
		this.repository = properties.getProperty("repository");
		this.userName = properties.getProperty("userName");
		this.password = properties.getProperty("password");
		this.pathSuccessfullyUploaded = properties.getProperty("pathSuccessfullyUploaded");
		this.pathFailedUpload = properties.getProperty("pathFailedUpload");
	}

	private String pathFolder = null;
	private String pathExcel = null;
	private String pathContentServer = null;
	private String repository = null;
	private String userName = null;
	private String password = null;
	private String pathSuccessfullyUploaded=null;
	private String pathFailedUpload=null;

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
		this.repository = repository;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPathSuccessfullyUploaded() {
		return pathSuccessfullyUploaded;
	}

	public void setPathSuccessfullyUploaded(String pathSuccessfullyUploaded) {
		this.pathSuccessfullyUploaded = pathSuccessfullyUploaded;
	}

	public String getPathFailedUpload() {
		return pathFailedUpload;
	}

	public void setPathFailedUpload(String pathFailedUpload) {
		this.pathFailedUpload = pathFailedUpload;
	}
}

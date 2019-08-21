package com.fosasoft.bean;

public class ContentObject {
	private String objectName = null;
	private String path = null;
	private String objectType = null;


	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getObjectType() {
		return "dm_document";
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}
}

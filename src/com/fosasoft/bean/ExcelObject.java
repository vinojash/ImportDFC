package com.fosasoft.bean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelObject {
	private String message = null;
	private String objectId = null;
	private String objectName = null;
	private String newObjectId = null;
	private String objectType = null;
	private String fileExtension = null;
	private String pathLocalFile = null;
	private String pathContentServer = null;
	private Boolean isSuccess = true;

	private List<Attribute> attributes = new ArrayList<Attribute>();

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public List<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}

	public void addAttributes(String label, String value) {
		Attribute attribute = new Attribute();
		attribute.setLabel(label);
		attribute.setValue(value);
		this.attributes.add(attribute);
	}

	public String getPathLocalFile() {
		return pathLocalFile;
	}

	public void setPathLocalFile(String pathLocalFile) {
		this.pathLocalFile = pathLocalFile;
	}

	public String getPathContentServer() {
		return pathContentServer;
	}

	public void setPathContentServer(String pathContentServer) {
		this.pathContentServer = pathContentServer;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public String getFileExtension() {
		return fileExtension;
	}

	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	public String print() {
		Iterator<Attribute> iteratorAttribute = this.attributes.iterator();
		String output = "**************************************\n";
		output += "objectId \t:" + this.objectId + "\nnewObjectId \t:" + this.newObjectId + "\nobjectName \t:"
				+ this.objectName + "\nfileExtension \t:" + this.fileExtension + "\nobjectType \t:" + this.objectType
				+ "\npathLocalFile \t:" + this.pathLocalFile + "\npathCServer \t:" + this.pathContentServer;
		while (iteratorAttribute.hasNext()) {
			Attribute attribute = iteratorAttribute.next();
			output += "\nLabel \t:" + attribute.getLabel() + "\nValue \t:" + attribute.getValue();
		}
		return output;

	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getNewObjectId() {
		return newObjectId;
	}

	public void setNewObjectId(String newObjectId) {
		this.newObjectId = newObjectId;
	}

	public Boolean getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

}

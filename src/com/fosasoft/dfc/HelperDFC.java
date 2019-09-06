package com.fosasoft.dfc;

import java.util.Iterator;

import com.documentum.fc.client.IDfDocument;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;
import com.fosasoft.bean.Attribute;
import com.fosasoft.bean.ExcelObject;

public class HelperDFC {
	public String createDocument(IDfSession session, ExcelObject excelObject) throws DfException {
		IDfDocument document = null;
		try {
			document = (IDfDocument) session.newObject(excelObject.getObjectType());
			if (document != null) {
				Iterator<Attribute> iteratorAttribute = excelObject.getAttributes().iterator();
				document.setObjectName(excelObject.getObjectName());
				document.setContentType(excelObject.getFileExtension());
				document.setFile(excelObject.getPathLocalFile());
				document.link(excelObject.getPathContentServer());
				while (iteratorAttribute.hasNext()) {
					Attribute attribute = iteratorAttribute.next();
					document.setString(attribute.getLabel(), attribute.getValue());
				}
				document.save();
			}
			successObjectCreation(excelObject);
		} catch (Exception e) {
			excelObject.setMessage(e.getMessage());
			failedObjectCreation(excelObject);
			e.printStackTrace();
		}

		return document.getObjectId().toString();
	}

	private void failedObjectCreation(ExcelObject excelObject) {

	}
	
	private void successObjectCreation(ExcelObject excelObject) {

	}
}

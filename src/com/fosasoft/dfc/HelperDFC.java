package com.fosasoft.dfc;

import java.util.HashMap;
import java.util.Iterator;

import com.documentum.fc.client.IDfDocument;
import com.documentum.fc.client.IDfFolder;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;
import com.fosasoft.bean.Attribute;
import com.fosasoft.bean.ExcelObject;

public class HelperDFC {
	private HashMap<String, Boolean> dctmPath = new HashMap<String, Boolean>();

	public ExcelObject createDocument(IDfSession session, ExcelObject excelObject) throws DfException {
		IDfDocument document = null;
		try {
			document = (IDfDocument) session.newObject(excelObject.getObjectType());
			Boolean isPathExist = checkFolderPathExist(session, excelObject.getPathContentServer());
			if (null != document && isPathExist) {
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
				excelObject.setNewObjectId(document.getObjectId().toString());
			}
			excelObject.setIsSuccess(true);
		} catch (Exception e) {
			excelObject.setIsSuccess(false);
			excelObject.setMessage(e.getMessage());
			e.printStackTrace();
		}
		return excelObject;
	}

	public Boolean checkFolderPathExist(IDfSession session, String path) throws Exception {
		if (dctmPath.containsKey(path)) {
			return true;
		} else {
			IDfFolder folder = null;
			folder = this.dmCreateStoragePath(session, path);
			if (null != folder) {
				dctmPath.put(path, true);
				return true;
			} else {
				return false;
			}

		}
	}

	private IDfFolder dmCreateStoragePath(IDfSession session, String path) throws Exception {
		IDfFolder folder = null;

		// first see if the folder already exists
		folder = (IDfFolder) session.getObjectByQualification("dm_folder where any r_folder_path='" + path + "'");

		// if not build it
		if (null == folder) {
			// split path into separate folders
			String[] dirs = path.split("/");

			// loop through path folders and build
			String dm_path = "";
			for (int i = 0; i < dirs.length; i++) {

				if (dirs[i].length() > 0) {

					// build up path
					dm_path = dm_path + "/" + dirs[i];

					// see if this path exists
					IDfFolder testFolder = (IDfFolder) session
							.getObjectByQualification("dm_folder where any r_folder_path='" + dm_path + "'");
					if (null == testFolder) {

						// check if a cabinet need to be made
						if (dm_path.equalsIgnoreCase("/" + dirs[i])) {
							IDfFolder cab = (IDfFolder) session.newObject("dm_cabinet");
							cab.setObjectName(dirs[i]);
							cab.save();
							// else make a folder
						} else {
							folder = (IDfFolder) session.newObject("dm_folder");
							folder.setObjectName(dirs[i]);

							// link it to parent
							String parent_path = "";
							for (int j = 0; j < i; j++) {
								if (dirs[j].length() > 0) {
									parent_path = parent_path + "/" + dirs[j];
								}
							}
							folder.link(parent_path);
							folder.save();
						}
					}
				}
			}
		}
		return folder;
	}
}

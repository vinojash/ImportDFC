package test;

import com.fosasoft.dfc.Import;


public class testExcel {
	public static void main(String[] args) throws Exception {
		Import imp = new Import(true);

		String excelPath = "C:\\temp\\test.xlsx";
		String sheetName = "Sheet1";

		// imp.readFilesFromPath(localPath);
		imp.readMetaDataFromExcel(excelPath, sheetName);
	}
}

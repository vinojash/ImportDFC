package test;

import java.io.IOException;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;
import com.fosasoft.dfc.Import;
import com.fosasoft.dfc.SessionDFC;

public class test {

	private static void doTesting(IDfSession session) throws IOException, DfException {
		Import imp = new Import(true);

		String excelPath = "C:\\temp\\test.xlsx";
		String sheetName = "Sheet1";

		// imp.readFilesFromPath(localPath);
		imp.createContentObjectFromExcel(session, excelPath, sheetName);
	}

	public static void main(String[] args) throws Exception {
		IDfSession session = null;
		try {
			session = SessionDFC.createSessionManager("xcprepo", "dmadmin", "dmadmin").getSession("xcprepo");
			doTesting(session);
		} finally {
			 SessionDFC.releaseSession(session);

		}
	}

}

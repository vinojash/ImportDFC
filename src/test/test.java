package test;
import java.io.IOException;

import com.documentum.com.DfClientX;
import com.documentum.com.IDfClientX;
import com.documentum.fc.client.IDfClient;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSessionManager;
import com.documentum.fc.common.IDfLoginInfo;
import com.fosasoft.dfc.Import;

public class test {
	
	
	private static void doTesting(IDfSession session) {
		
	}

	public static void main(String[] args) throws IOException {

		String localPath = "C:\\temp\\PARKED\\Temp";
		Import imp = new Import();
		imp.readFilesFromPath(localPath);

		IDfSession session = null;
		try {
			session = createSessionManager("xcprepo", "dmadmin", "dmadmin").getSession("xcprepo");
			doTesting(session);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				IDfSessionManager sMgr = session.getSessionManager();
				sMgr.release(session);
				System.err.println("Session Released..!");
			}
		}
	}

	private static IDfSessionManager createSessionManager(String docbase, String user, String pass) throws Exception {
		IDfClientX clientx = new DfClientX();
		IDfClient client = clientx.getLocalClient();
		IDfSessionManager sMgr = client.newSessionManager();
		IDfLoginInfo loginInfoObj = clientx.getLoginInfo();
		loginInfoObj.setUser(user);
		loginInfoObj.setPassword(pass);
		loginInfoObj.setDomain(null);
		sMgr.setIdentity(docbase, loginInfoObj);
		System.err.println("Session Created..!");
		return sMgr;
	}

}

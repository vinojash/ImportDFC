package test;

import com.documentum.fc.client.IDfSession;
import com.fosasoft.dfc.SessionDFC;

public class TestSession {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		SessionDFC sessionDFC = new SessionDFC();
		IDfSession session= sessionDFC.createSessionManager("xcprepo", "dmadmin", "dmadmin");
		System.out.println("Done");
		sessionDFC.releaseSession(session);
	}

}

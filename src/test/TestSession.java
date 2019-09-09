package test;

import com.documentum.fc.client.IDfSession;
import com.fosasoft.dfc.SessionDFC;

public class TestSession {

	public static void main(String[] args) throws Exception {
		SessionDFC sessionDFC = new SessionDFC();
		IDfSession session = sessionDFC.createSessionManager();
		sessionDFC.releaseSession(session);
		System.out.println("Done");
	}
}

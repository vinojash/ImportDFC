package com.fosasoft.dfc;

import com.documentum.com.DfClientX;
import com.documentum.com.IDfClientX;
import com.documentum.fc.client.IDfClient;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSessionManager;
import com.documentum.fc.common.IDfLoginInfo;

public class SessionDFC {
	public static IDfSessionManager createSessionManager(String docbase, String user, String pass) throws Exception {
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

	public static void releaseSession(IDfSession session) throws Exception {
		if (session != null) {
			IDfSessionManager sMgr = session.getSessionManager();
			sMgr.release(session);
			System.err.println("Session Released..!");
		}
	}

}

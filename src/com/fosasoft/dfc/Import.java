package com.fosasoft.dfc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import com.documentum.fc.client.IDfDocument;
import com.documentum.fc.client.IDfSession;

public class Import {

	public IDfDocument createDocument(IDfSession session) throws Exception {

		IDfDocument document = (IDfDocument) session.newObject("");
		if (document != null) {
			document.setObjectName("");
			document.setContentType("");
			document.setFile("");
			document.link("");
			document.save();
		}
		return document;
	}

	public List<Path> readFilesFromPath(String localPath) throws IOException {
		List<Path> files = new ArrayList<Path>();
		try (Stream<Path> paths = Files.walk(Paths.get(localPath))) {
			paths.filter(Files::isRegularFile).forEach(files::add);
		}

//		Iterator<Path> p = files.iterator();
//		while (p.hasNext()) {
//			Path temp = p.next();
//			System.err.println(temp.toAbsolutePath());
//		}

		return files;
	}

}

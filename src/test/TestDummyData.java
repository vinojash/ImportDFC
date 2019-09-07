package test;

import com.fosasoft.dfc.Import;

public class TestDummyData {
	public static void main(String[] args) throws Exception {
		Import imp = new Import();
		System.err.println("Started...");
		imp.readFilesFromPath();
		System.err.println("Done..!");
	}
}

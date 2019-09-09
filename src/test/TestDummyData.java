package test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.fosasoft.dfc.Import;

public class TestDummyData {
	public static void main(String[] args) throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
		Date datestartOperation = Calendar.getInstance().getTime();

		Import imp = new Import();
		System.err.println("Started...");
		imp.readFilesFromPath();
		System.err.println("Done..!");

		Date dateEndOperation = Calendar.getInstance().getTime();

		System.err.println("datestartOperation\t:" + dateFormat.format(datestartOperation));
		System.err.println("dateEndOperation\t:" + dateFormat.format(dateEndOperation));

	}
}

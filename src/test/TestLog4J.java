package test;

import org.apache.log4j.Logger;

public class TestLog4J {
	static Logger log = Logger.getLogger(TestLog4J.class.getName());

	public static void main(String[] args) {
		log.info("Heyyyyyyyyyy");
	}

}

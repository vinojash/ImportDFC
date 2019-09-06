package test;

import java.util.Iterator;
import java.util.List;

import com.fosasoft.bean.ApplicationConstant;
import com.fosasoft.bean.ExcelObject;
import com.fosasoft.dfc.ExcelOperation;

public class testExcel {

	public static void main(String[] args) throws Exception {

		ApplicationConstant constant = new ApplicationConstant();
		System.out.println(constant.getPathExcel());
		System.out.println(constant.getPathFolder());

		ExcelOperation excel = new ExcelOperation();
		List<ExcelObject> listExcelObject = excel.readMetaDataFromExcel(constant.getPathExcel());
		Iterator<ExcelObject> iteratorExcelObject = listExcelObject.iterator();

		while (iteratorExcelObject.hasNext()) {
			iteratorExcelObject.next().print();
		}
	}
}

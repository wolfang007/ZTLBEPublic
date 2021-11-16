package it.regioneveneto.myp3.gestgraduatorie.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.RichTextString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UtilUploadMassivo {
	private static final Logger logger = LoggerFactory.getLogger(UtilUploadMassivo.class.getName());
	private static final String path_home = System.getProperty("user.home") + File.separator+"store-istances"+ File.separator;
	private final static String LINE_SEPARATOR = System.getProperty("line.separator");
	 
	private UtilUploadMassivo() { }
	
	private static void writeAudit(String line) {
	FileOutputStream fos = null;
	try {
		String strDate=new SimpleDateFormat("dd-MM-yyyy HH-mm-ss.SSS").format(new Date());
		fos = new FileOutputStream(path_home+"Audit-"+strDate+".txt", true);
	} catch (FileNotFoundException e) {
		logger.error("UtilUploadMassivo writeAudit ", e);
	}
	PrintWriter pw = new PrintWriter(fos);
	String lineLog = line.toString();

	pw.println(lineLog.toString());
	pw.flush();
	pw.close();

	}
	
	public static String checkValueString(Cell cell) {

		switch (cell.getCellType()) {
		case NUMERIC:
			return String.valueOf(Math.round(cell.getNumericCellValue()));
		case STRING:
			RichTextString textString = cell.getRichStringCellValue();
			return textString.getString();
		case FORMULA:
			return "";
		case BLANK:
			return "";
		case BOOLEAN:
			return String.valueOf(cell.getBooleanCellValue());
		case ERROR:
			return "";
		default:
			return "";
		}

	}

	public static Double checkValueNumber(Cell cell) {
		switch (cell.getCellType()) {
		case NUMERIC:
			return cell.getNumericCellValue();
		case STRING:
			RichTextString textString = cell.getRichStringCellValue();
			return  Double.valueOf(textString.getString());
		case BLANK:
			return  Double.MIN_VALUE;
		case BOOLEAN:
			return Double.MIN_VALUE;
		case ERROR:
			return Double.MIN_VALUE;
		default:
			return Double.MIN_VALUE;
		}

		 
	}

	public static boolean checkValueBoolean(Cell cell) {

		switch (cell.getCellType()) {
		case NUMERIC:
			if (cell.getNumericCellValue()<1) 
				return false;
			if (cell.getNumericCellValue()>0) 
				return true;
		case STRING:
			RichTextString textString = cell.getRichStringCellValue();
			String boolStr=textString.getString();
			if (boolStr.trim().equalsIgnoreCase("TRUE"))
				return true;
			if (boolStr.trim().equalsIgnoreCase("FALSE"))
				return false;
			if (boolStr.trim().equalsIgnoreCase("VERO"))
				return true;
			if (boolStr.trim().equalsIgnoreCase("FALSO"))
				return false;
			if (boolStr.trim().equalsIgnoreCase("SI"))
				return true;
			if (boolStr.trim().equalsIgnoreCase("NO"))
				return false;
			if (boolStr.trim().equalsIgnoreCase("1"))
				return true;
			if (boolStr.trim().equalsIgnoreCase("0"))
				return false;
			return false;
		case BLANK:
			return false;
		case BOOLEAN:
			return cell.getBooleanCellValue();
		case ERROR:
			return false;
		default:
			return false;
		}

		 
	}
	
	private static boolean checkAllDir() {

		File f = new File(path_home);
		if (!f.exists()) {
			boolean bol = f.mkdirs();
			return bol;
		}

		return true;
	}
 

}

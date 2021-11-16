package it.regioneveneto.myp3.gestgraduatorie.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.validation.constraints.NotNull;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;



/**
 * Classe di utilità per le date.
 */
public class DateUtils {

	private static final Logger LOG = LoggerFactory.getLogger(DateUtils.class);
	
	public static final String PATTERN_YYYY_MM_DD = "yyyy-MM-dd";
	public static final String PATTERN_YYYY_MM_DD_SLASH = "yyyy/MM/dd";
	public static final String PATTERN_DD_MMMMM_YYYY = "dd MMMMM yyyy";
	public static final String PATTERN_DD_MM_YYYY_SLASH = "dd/MM/yyyy";
	public static final String PATTERN_DD_MM_YYYY = "dd-MM-yyyy";
	
	/**
	 * Restituisce la data corrente come oggetto {@link java.util.Date}
	 * @return la data corrente.
	 */
	public static Date now(){
		return Calendar.getInstance().getTime();
	}
	

	public static XMLGregorianCalendar nowAsXMLGregorianCalendar(){
		return DateUtils.dateAsXMLGregorianCalendar(new Date());
	}
	
	public static XMLGregorianCalendar dateAsXMLGregorianCalendar(Date date){
		if(date == null) return null;
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.setTime(date);
		try {
			return DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
		} catch (DatatypeConfigurationException e) {
			LOG.error("Si è verificato un errore nella creazione di un XMLGregorianCalendar.", e);
			return null;
		}
	}

	/**
	 * Restituisce la data relativa a ieri.
	 * @return ieri
	 */
	public static Date yesterday() {
		Calendar calendar = Calendar.getInstance();
		calendar.add( Calendar.DAY_OF_YEAR, -1 );
		return calendar.getTime( );
	}
	
	public static Date oggi() {
		Calendar cal = Calendar.getInstance();
		cal.setTime( new Date() );
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.HOUR, 0);
		return cal.getTime();
	}
	
	public static Date oggiPiuTotGiorni(int tot) {
		Calendar cal = Calendar.getInstance();
		cal.setTime( new Date() );
		cal.set(Calendar.MILLISECOND, 999);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.add(Calendar.DATE, tot);
		return cal.getTime();
	}
	
	public static Date normalizzaInizio(@NotNull Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.HOUR, 0);
		return cal.getTime();
	}
	
	public static Date normalizzaFine(@NotNull Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.MILLISECOND, 999);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		return cal.getTime();
	}
	
	public static int numeroSettimana(@NotNull Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.WEEK_OF_YEAR);
	}
	
	public static int numeroAnno(@NotNull Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.YEAR);
	}
	
	/**
	 * 
	 * @param dateAsString
	 * @param pattern
	 * @return
	 */
	public static Date stringToDate(String dateAsString, String pattern) {
		Date date = null;
		if(StringUtils.hasText(dateAsString)){
			try{
				SimpleDateFormat format = new SimpleDateFormat(pattern);
				date = format.parse(dateAsString);
			}catch( ParseException pe ){
				LOG.error("DateUtils --> errore di conversione della stringa {} in data.", dateAsString);
				return null;
			}
		}
		return date;
	}
	
	/**
	 * Converte una data in stringa
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String dateToString( Date date, String pattern ) {
		String dateAsString = null;
		if( date != null ){
			try{
				SimpleDateFormat format = new SimpleDateFormat(pattern);
				dateAsString = format.format(date);
			}catch( Exception e ){
				LOG.error("DateUtils --> errore di conversione della data in stringa.");
				return null;
			}
		}
		return dateAsString != null ? dateAsString.toUpperCase() : null;
	}
	
	/**
	 * Converte la data di scadenza di un pass blu in una stringa utile ad essere rappresentata
	 * sul talloncino del pass blu.
	 * @param date è la data di scadenza del pass
	 * @return
	 */
	public static String vlToDateToReportString( Date date ) {
		
		String dateAsString = null;
		if( date != null ){
			try{
				SimpleDateFormat format = new SimpleDateFormat( PATTERN_DD_MMMMM_YYYY, Locale.ITALIAN );
				dateAsString = format.format(date);
			}catch( Exception e ){
				LOG.error("DateUtils --> errore di conversione della data in stringa.");
				return null;
			}
		}
		return dateAsString != null ? dateAsString.toUpperCase() : null;
		
	}

	/**
	 * 
	 * @return
	 */
	public static int currentYear( ) {
		return Calendar.getInstance().get( Calendar.YEAR );
	}
	
	/**
	 * 
	 * @return
	 */
	public static Date thisYearStartingDate( ){
		Calendar cal = Calendar.getInstance();
		cal.set(
				Calendar.getInstance().get( Calendar.YEAR ), 	     // YEAR			     //
				0, 												    // MONTH ZERO-BASED	    //
				1,												   // DAY_OF_MONTH 		   //
				0, 										  		  // HOUR				  //
				0, 										 		 // MINUTE				 //
				0												// SECOND				//
				);
		return cal.getTime();
	}
	
	public static Date thisYearEndingDate( ){
		Calendar cal = Calendar.getInstance();
		cal.set(
				Calendar.getInstance().get( Calendar.YEAR ), 		// YEAR			     	 //
				11, 										  	   // MONTH ZERO-BASED	    //
				31,												  // DAY_OF_MONTH 		   //
				23, 										  	 // HOUR				  //
				59, 										    // MINUTE				 //
				59											   // SECOND				//
				);
		return cal.getTime();
	}



	
}

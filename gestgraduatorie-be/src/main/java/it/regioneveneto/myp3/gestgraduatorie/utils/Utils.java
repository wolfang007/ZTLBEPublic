package it.regioneveneto.myp3.gestgraduatorie.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.SignedJWT;

import it.regioneveneto.myp3.gestgraduatorie.web.dto.JwtDto;
import net.minidev.json.JSONObject;

public class Utils {
	private static final Logger LOG = LoggerFactory.getLogger(Utils.class.getName());
	
	public static final String DATETIME_FORMAT_JSON_MORFEO = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"; //2020-11-12T23:00:00.000Z
	public static final String DATETIME_FORMAT = "dd-MM-yyyy HH:mm";
	public static final String DATE_FORMAT = "dd-MM-yyyy";
	public static final String TIME_FORMAT = "HH:mm";
	public static final String DATA_NO_SCAD = "4000-01-01";
	
	public static Date fromStringToDate(String stringDate, String stringFormat) throws ParseException {
		if( !StringUtils.hasText(stringDate) ) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(stringFormat);
		sdf.setLenient(false);
		return sdf.parse(stringDate);
	}
	
	public static String fromDateToString(Date stringDate, String stringFormat) {
		if( stringDate == null ) {
			return "";
		}
		return new SimpleDateFormat(stringFormat).format(stringDate);
	}
	
	public static String toUpperCase(String stringa) {
		if (StringUtils.hasText(stringa)) {
			return stringa.toUpperCase();
		}
		return stringa;
	}
	
	public static String getTemporaryDirectory() {
		return System.getProperty("java.io.tmpdir");
	}
	
	public static JwtDto extractJwtDto(SignedJWT signedJWT) throws ParseException, JsonProcessingException, JsonMappingException {
		JSONObject fullJWT = (JSONObject) signedJWT.getJWTClaimsSet().toJSONObject(true);
        ObjectMapper objectMapper = new ObjectMapper();
        JwtDto jwtDto = objectMapper.readValue(fullJWT.toString(), JwtDto.class);
		return jwtDto;
	}
}

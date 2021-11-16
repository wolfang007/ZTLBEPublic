package it.regioneveneto.myp3.gestgraduatorie.utils;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

@Component
public class DateDeserializer extends StdDeserializer<Date> {
	
	private static final long serialVersionUID = -1699767466696321246L;
	
	public DateDeserializer() {
		this(null);
	}

	public DateDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public Date deserialize(JsonParser jsonparser, DeserializationContext context)
			throws IOException, JsonProcessingException {
		String date = jsonparser.getText();
		if ("".equals(date)) {
			return null;
		}
		try {
			return Utils.fromStringToDate(date, Utils.DATE_FORMAT);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
}
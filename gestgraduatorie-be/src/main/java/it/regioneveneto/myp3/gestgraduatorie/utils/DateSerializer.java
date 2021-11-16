package it.regioneveneto.myp3.gestgraduatorie.utils;

import java.io.IOException;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

@Component
public class DateSerializer extends StdSerializer<Date> {
	
    private static final long serialVersionUID = -2894356342227378312L;

    public DateSerializer() {
        this(null);
    }

    public DateSerializer(final Class<Date> t) {
        super(t);
    }

	@Override
	public void serialize(Date value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(Utils.fromDateToString(value, Utils.DATE_FORMAT));
	}
}
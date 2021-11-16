package it.regioneveneto.myp3.gestgraduatorie.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(
		value = HttpStatus.INTERNAL_SERVER_ERROR, 

		reason = "Si � verificato un errore di conversione dei dati"
		)
public class ConverterException extends RuntimeException{

	private static final long serialVersionUID = -3806219549340689481L;

	public ConverterException(String s) {
		super(s);
	}

	public ConverterException(String s, Throwable throwable) {
		super(s, throwable);
	}

	public ConverterException(Throwable throwable) {
		super(throwable);
	}
	
}
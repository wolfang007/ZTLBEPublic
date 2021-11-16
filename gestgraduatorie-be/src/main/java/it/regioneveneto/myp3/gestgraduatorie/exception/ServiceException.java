package it.regioneveneto.myp3.gestgraduatorie.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(
		value = HttpStatus.INTERNAL_SERVER_ERROR, 
		reason = "Si è verificato un errore nell'esecuzione di un metodo dei service."
		)
public class ServiceException extends RuntimeException{

	private static final long serialVersionUID = -3806219549340689481L;

	private String codiceEsito;

	public String getCodiceEsito() {
		return codiceEsito;
	}

	public ServiceException(String codiceEsito, String message) {
		super(message);
		this.codiceEsito = codiceEsito;
	}

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public ServiceException(Throwable throwable) {
		super(throwable);
	}
	
}
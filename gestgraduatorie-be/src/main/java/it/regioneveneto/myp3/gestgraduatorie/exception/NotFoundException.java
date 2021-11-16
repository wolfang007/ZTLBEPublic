package it.regioneveneto.myp3.gestgraduatorie.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(
		value = HttpStatus.BAD_REQUEST,
		reason = "Impossibile recuperare i dati con le chiavi passate")
public class NotFoundException extends ServiceException {

	private static final long serialVersionUID = 352177667454633639L;

	public NotFoundException(String s) {
		super(s);
	}

	public NotFoundException(String s, Throwable throwable) {
		super(s, throwable);
	}

	public NotFoundException(Throwable throwable) {
		super(throwable);
	}

}

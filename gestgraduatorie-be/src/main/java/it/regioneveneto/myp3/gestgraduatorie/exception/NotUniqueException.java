package it.regioneveneto.myp3.gestgraduatorie.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(
		value = HttpStatus.BAD_REQUEST, 
		reason = "Vincolo di univocità violato sull'inserimento dei dati.")
public class NotUniqueException  extends RuntimeException{

	private static final long serialVersionUID = 352177667454633639L;

	public NotUniqueException(String s) {
		super(s);
	}

	public NotUniqueException(String s, Throwable throwable) {
		super(s, throwable);
	}

	public NotUniqueException(Throwable throwable) {
		super(throwable);
	}

}

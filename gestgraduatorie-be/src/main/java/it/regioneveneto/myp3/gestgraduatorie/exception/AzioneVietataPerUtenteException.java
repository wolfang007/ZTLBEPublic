package it.regioneveneto.myp3.gestgraduatorie.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(
		value = HttpStatus.BAD_REQUEST)
public class AzioneVietataPerUtenteException  extends RuntimeException {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -243297048782555058L;

	public AzioneVietataPerUtenteException(String s) {
		super(s);
	}

	public AzioneVietataPerUtenteException(String s, Throwable throwable) {
		super(s, throwable);
	}

	public AzioneVietataPerUtenteException(Throwable throwable) {
		super(throwable);
	}

}

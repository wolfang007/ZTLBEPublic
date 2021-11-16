package it.regioneveneto.myp3.gestgraduatorie.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(
		value = HttpStatus.BAD_REQUEST)
public class AzioneNonConsentitaException  extends RuntimeException {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -243297048782555058L;

	public AzioneNonConsentitaException(String s) {
		super(s);
	}

	public AzioneNonConsentitaException(String s, Throwable throwable) {
		super(s, throwable);
	}

	public AzioneNonConsentitaException(Throwable throwable) {
		super(throwable);
	}

}

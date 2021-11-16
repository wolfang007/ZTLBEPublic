package it.regioneveneto.myp3.gestgraduatorie.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;



@ResponseStatus(
		value = HttpStatus.BAD_REQUEST, 
		reason = "Azione non replicabile ripetuta più volte")
public class ReplyActionException  extends RuntimeException {

	private static final long serialVersionUID = 5169091206106603168L;
	
	public ReplyActionException(String s) {
		super(s);
	}

	public ReplyActionException(String s, Throwable throwable) {
		super(s, throwable);
	}

	public ReplyActionException(Throwable throwable) {
		super(throwable);
	}

}

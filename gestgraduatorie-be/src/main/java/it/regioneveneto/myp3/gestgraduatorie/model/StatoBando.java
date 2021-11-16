package it.regioneveneto.myp3.gestgraduatorie.model;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatoBando implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	long id;
	String codiceStatoBando;
	String descrizione;
	
}

package it.regioneveneto.myp3.gestgraduatorie.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Istanza implements Serializable {
	/**
	 * 
	 */

	
	private static final long serialVersionUID = 1L;

	Long id;
	Long idBando;
	String nome;
	String cognome;
	String codiceFiscale;
	String telefono;
	String cellulare;
	String email;
	String punteggio;
	String numeroProtocollo;
	Long posizione;
	Boolean residenteComuneRiferimento;
	Boolean cittadinoUE;
	TipologiaBando tipoBando; 
	StatoBando statoBando; 
	String denominazioneBando;
	//oggetto json della richiesta
	String richiesta;
	List<Allegato> allegati = new ArrayList<Allegato>();;

	public void addAllegato(Allegato allegato) {
    	this.allegati.add(allegato);

    }
}

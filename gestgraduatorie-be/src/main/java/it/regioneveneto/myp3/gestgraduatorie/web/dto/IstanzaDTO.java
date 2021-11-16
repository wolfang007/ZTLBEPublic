package it.regioneveneto.myp3.gestgraduatorie.web.dto;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public class IstanzaDTO {

		Long id;
		Long idBando;
		Long idRichiesta;
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
		List<AllegatoDTO> allegati;

		public void addAllegato(AllegatoDTO allegato) {
	    	this.allegati.add(allegato);

	    }
	}
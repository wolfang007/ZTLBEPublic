package it.regioneveneto.myp3.gestgraduatorie.web.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public class IstanzaFilterDTO {
		Long idBando;
		String nome;
		String cognome;
		String codiceFiscale;
		String telefono;
		String cellulare;
		String email;
		Double punteggioDa;
		Double punteggioA;
		String numeroProtocollo;

		

		
	}
package it.regioneveneto.myp3.gestgraduatorie.web.dto;


import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public class RichiestaFilterDTO {
		Long idBando;
		String nome;
		String cognome;
		String codiceFiscale;
		String telefono;
		String cellulare;
		String email;
		
		String punteggioDa;
		String punteggioA;
		String numProtocollo;

		
	}
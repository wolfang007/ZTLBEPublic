package it.regioneveneto.myp3.gestgraduatorie.web.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public class BandoFilterDTO {
		String denominazione;
		String dataInizioDomandaDa;
		String dataInizioDomandaA;
		String dataFineDomandaDa;
		String dataFineDomandaA;
		Double importoFinanziatoDa;
		Double importoFinanziatoA;


		
	}
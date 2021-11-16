package it.regioneveneto.myp3.gestgraduatorie.web.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BandoDTO implements Serializable {
	/**
	 * 
	 */

	
	private static final long serialVersionUID = 1L;


	Long id;
	String idProcedimento;
	String denominazione;
	String oggetto;
	String codiceIdentificativoBando;
	Date dataFinePresentazioneDomande;
	Date dataInizioPresentazioneDomande;
	Date dataAttoGraduatoriaDefinitiva;
	String importoFinanziatoTotale;
	Date dataAttoGraduatoriaPreliminare;
	Date dataDeliberaBando;
	String estermiAttoGraduatoriaDefinitiva;
	String estermiAttoGraduatoriaPreliminare;
	String estremiDeliberaBando;
	String stato;
	Date dataScadenzaGraduatoriaPreliminare;
	String tipologia;
	String note;
	Date dataModifica;
	Date dataInserimento;
	Date dataInizioValidita;
	Date dataFineValidita;
	String ente;
	

}

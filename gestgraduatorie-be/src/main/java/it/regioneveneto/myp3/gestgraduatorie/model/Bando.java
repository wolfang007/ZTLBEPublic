package it.regioneveneto.myp3.gestgraduatorie.model;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Bando implements Serializable {
	/**
	 * 
	 */

	
	private static final long serialVersionUID = 1L;


	Long id;
	String idProcedimento;
	String codiceIdentificativoBando;
	Date dataAttoGraduatoriaDefinitiva;
	Date dataAttoGraduatoriaPreliminare;
	Date dataDeliberaBando;
	Date dataFinePresentazioneDomande;
	Date dataInizioPresentazioneDomande;
	
	String estermiAttoGraduatoriaDefinitiva;
	String estermiAttoGraduatoriaPreliminare;
	String estremiDeliberaBando;
	String denominazione;
	String oggetto;
	String stato;
	Date dataScadenzaGraduatoriaPreliminare;
	String importoFinanziatoTotale;
	String tipologia;
	String note;
	Date dataModifica;
	Date dataInserimento;
	Date dataInizioValidita;
	Date dataFineValidita;
	String ente;
	

}

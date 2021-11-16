package it.regioneveneto.myp3.gestgraduatorie.web.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BeneficiarioDTO  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Long id;
	Long idRichiesta;
	Long idBando;
	String nome;
	String cognome;
	String codiceFiscale;
	String telefono;
	String cellulare;
	String email;
	Boolean residendeComune;
	Boolean cittadinoUE;
	Date dataModifica;
	Date dataInserimento;
	Date dataInizioValidita;
	Date dataFineValidita;
	String ente;
	
}

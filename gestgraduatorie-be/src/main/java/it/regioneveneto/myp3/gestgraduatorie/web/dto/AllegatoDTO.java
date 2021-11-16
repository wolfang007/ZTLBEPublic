package it.regioneveneto.myp3.gestgraduatorie.web.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AllegatoDTO implements Serializable {
	/**
	 * 
	 */

	
	private static final long serialVersionUID = 1L;

	Long id;
	String idAllegato;
	Long idRichiesta;
	String userId;
	String descrizioneAllegato;
	String nrProt;
	Date dataProt;
	String procId;
	String idMyBox;
	String fieldName;
	String nomeFile;
	String mimeType;
	Long length;
	String idFile;
	String tipologia;
	String note;
	Date dataModifica;
	Date dataInserimento;
	Date dataInizioValidita;
	Date dataFineValidita;
	String ente;
	

}
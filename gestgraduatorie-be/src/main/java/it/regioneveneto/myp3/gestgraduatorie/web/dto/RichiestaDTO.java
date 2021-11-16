package it.regioneveneto.myp3.gestgraduatorie.web.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RichiestaDTO implements Serializable {
	/**
	 * 
	 */

	
	private static final long serialVersionUID = 1L;

	Long id;
	String idIstanza;
	String cfRichiedente;
	Long idBando;
	String punteggioGraduatoriaPreliminare;
	String punteggioGraduatoriaDefinitiva;
	String importoFinanziatoRichiesto;
	String importoFinanziato;
	Boolean accolto;
	String numProtocolloEntrata;
	String numProtocolloUscita;
	Date dataProtEntrata;
	Date dataProtUscita;
	String nome;
	String cognome;
	Boolean idoneo;
	String note;
	String motivoDiniego;
	String numeroMandatoPagamento;
	String datiRichiesta;
	String idFascicolo;
	Date dataModifica;
	Date dataInserimento;
	Date dataInizioValidita;
	Date dataFineValidita;
	String ente;
	List<AllegatoDTO> allegati;
    private String procId;
    private String userRegistryId;
	BandoDTO bando;
	BeneficiarioDTO beneficiarioDTO;
    
	public void addAllegato(AllegatoDTO allegato) {
    	this.allegati.add(allegato);

    }

}
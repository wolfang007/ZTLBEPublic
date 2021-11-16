package it.regioneveneto.myp3.gestgraduatorie.web.dto;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UploadMassivo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Double idRichiesta;
	private Double idBando;
	private String punteggio;
	private String nome;
	private String cognome;
	private String codFiscale;
	private String dataModifica;
	private String operatoreModifica;
	private Integer esitoOperazione;
	private String codiceBando;

	public UploadMassivo(Double idRichiesta, Double idBando, String punteggio) {
		super();
		this.idRichiesta = idRichiesta;
		this.idBando = idBando;
		this.punteggio = punteggio;
		this.dataModifica = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS").format(new Date());
	}

	public UploadMassivo(Double idRichiesta, Double idBando, String punteggio, String codFiscale,
			String operatoreModifica) {
		super();
		this.idRichiesta = idRichiesta;
		this.idBando = idBando;
		this.punteggio = punteggio;
		this.codFiscale = codFiscale;
		this.dataModifica = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS").format(new Date());
		this.operatoreModifica = operatoreModifica;
	}

	public UploadMassivo(Double idRichiesta, Double idBando, String punteggio, String codFiscale,
			String operatoreModifica, Integer esitoOperazione) {
		super();
		this.idRichiesta = idRichiesta;
		this.idBando = idBando;
		this.punteggio = punteggio;
		this.codFiscale = codFiscale;
		this.dataModifica = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS").format(new Date());
		this.operatoreModifica = operatoreModifica;
		this.esitoOperazione = esitoOperazione;
	}

	public UploadMassivo() {
		this.dataModifica = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS").format(new Date());
	}

	public Double getIdRichiesta() {
		return idRichiesta;
	}

	public void setIdRichiesta(Double idRichiesta) {
		this.idRichiesta = idRichiesta;
	}

	public Double getIdBando() {
		return idBando;
	}

	public void setIdBando(Double idBando) {
		this.idBando = idBando;
	}

	public String getPunteggio() {
		return punteggio;
	}

	public void setPunteggio(String punteggio) {
		this.punteggio = punteggio;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getCodFiscale() {
		return codFiscale;
	}

	public void setCodFiscale(String codFiscale) {
		this.codFiscale = codFiscale;
	}

	public String getOperatoreModifica() {
		return operatoreModifica;
	}

	public void setOperatoreModifica(String operatoreModifica) {
		this.operatoreModifica = operatoreModifica;
	}

	public Integer getEsitoOperazione() {
		return esitoOperazione;
	}

	public void setEsitoOperazione(Integer esitoOperazione) {
		this.esitoOperazione = esitoOperazione;
	}

	public String getDataModifica() {
		return dataModifica;
	}
	

	public String getCodiceBando() {
		return codiceBando;
	}

	public void setCodiceBando(String codiceBando) {
		this.codiceBando = codiceBando;
	}


	@Override
	public String toString() {
		return "Audit-Upload-Massivo [IDRichiesta=" + idRichiesta + ", IDBando=" + idBando + ", Punteggio=" + punteggio
				+ ", Cod-Fiscale=" + codFiscale + ", Data-Modifica=" + dataModifica + ", CF-Operatore="
				+ operatoreModifica + ", Esito-Op=" + esitoOperazione + "]";
	}

}

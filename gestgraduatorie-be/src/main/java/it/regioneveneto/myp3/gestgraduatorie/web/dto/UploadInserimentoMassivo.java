package it.regioneveneto.myp3.gestgraduatorie.web.dto;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;

public class UploadInserimentoMassivo implements Serializable {

	private static final long serialVersionUID = 1L;
	private String codiceBando;
	private String oggetto;
	private String nome;
	private String cognome;
	private String codFiscale;
	private String telefono;
	private String cellulare;
	private String email;
	private Boolean residenteInComune;
	private Boolean cittadinoUE;
	private String dataOperazione;
	private String esitoOperazione;
	private String cfOperatore;
	private String nomeOperatore;
	private String cognomeOperatore;

	public UploadInserimentoMassivo() {
		this.dataOperazione = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS").format(new Date());
	}

	public UploadInserimentoMassivo(String codiceBando, String oggetto, String nome, String cognome, String codFiscale,
			String telefono, String cellulare, String email, Boolean residenteInComune, Boolean cittadinoUE) {
		super();
		this.codiceBando = codiceBando;
		this.oggetto = oggetto;
		this.nome = nome;
		this.cognome = cognome;
		this.codFiscale = codFiscale;
		this.telefono = telefono;
		this.cellulare = cellulare;
		this.email = email;
		this.residenteInComune = residenteInComune;
		this.cittadinoUE = cittadinoUE;
	}

	public String getCodiceBando() {
		return codiceBando;
	}

	public void setCodiceBando(String codiceBando) {
		this.codiceBando = codiceBando;
	}

	public String getOggetto() {
		return oggetto;
	}

	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
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

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getCellulare() {
		return cellulare;
	}

	public void setCellulare(String cellulare) {
		this.cellulare = cellulare;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getResidenteInComune() {
		return residenteInComune;
	}

	public void setResidenteInComune(Boolean residenteInComune) {
		this.residenteInComune = residenteInComune;
	}

	public Boolean getCittadinoUE() {
		return cittadinoUE;
	}

	public void setCittadinoUE(Boolean cittadinoUE) {
		this.cittadinoUE = cittadinoUE;
	}

	public String getEsitoOperazione() {
		return esitoOperazione;
	}

	public void setEsitoOperazione(String esitoOperazione) {
		this.esitoOperazione = esitoOperazione;
	}

	public String getDataOperazione() {
		return dataOperazione;
	}

	public String getCfOperatore() {
		return cfOperatore;
	}

	public void setCfOperatore(String cfOperatore) {
		this.cfOperatore = cfOperatore;
	}

	public String getNomeOperatore() {
		return nomeOperatore;
	}

	public void setNomeOperatore(String nomeOperatore) {
		this.nomeOperatore = nomeOperatore;
	}

	public String getCognomeOperatore() {
		return cognomeOperatore;
	}

	public void setCognomeOperatore(String cognomeOperatore) {
		this.cognomeOperatore = cognomeOperatore;
	}

	@Override
	public String toString() {
		return "InserimentoMassivo [CodiceBando=" + codiceBando + ", Oggetto=" + oggetto + ", Nome=" + nome
				+ ", Cognome=" + cognome + ", codFiscale=" + codFiscale + ", telefono=" + telefono + ", cellulare="
				+ cellulare + ", Email=" + email + ", ResidenteInComune=" + residenteInComune + ", CittadinoUE="
				+ cittadinoUE + ", DataOperazione=" + dataOperazione + ", EsitoOperazione=" + esitoOperazione
				+ ", CfOperatore=" + cfOperatore + ", NomeOperatore=" + nomeOperatore + ", CognomeOperatore="
				+ cognomeOperatore + "]";
	}


}

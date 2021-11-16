package it.regioneveneto.myp3.gestgraduatorie.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(

		name="reg_regioni"
		)
public class Regione implements Serializable {

	private static final long serialVersionUID = -5901643330974349730L;

	@Id 
	@Column(name = "id_regione", unique = true, nullable = false, updatable=false )
	private Long id;
	
	@Column( name = "codice_istat", length = 3 )
	private String codiceIstat;
	
	@Column( name = "codice_ssn", length = 3 )
	private String codiceSsn;
	
	@Column( name = "codice_mf", length = 3 )
	private String codiceBelfiore;
	
	@Column( name = "denominazione", nullable = false, updatable = false, length = 50 )
	private String denominazione;
	
	@Column( name = "codice_area", length = 1 )
	private String codiceArea;
	
	@Column( name = "vl_inizio" )
	@Temporal( value = TemporalType.DATE )
	private Date vlInizio;

	@Column( name = "vl_fine" )
	@Temporal( value = TemporalType.DATE )
	private Date vlFine;
	
	@Column( name = "dt_agg" )
	@Temporal( value = TemporalType.DATE )
	private Date dtAgg;
	
	@OneToMany(
	        mappedBy = "regione", 
	        fetch=FetchType.LAZY
	    )
	private List<Provincia> province;
	
	public Regione() { }
	
	public Regione(Long id) { 
		super();
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodiceIstat() {
		return codiceIstat;
	}

	public void setCodiceIstat(String codiceIstat) {
		this.codiceIstat = codiceIstat;
	}

	public String getCodiceSsn() {
		return codiceSsn;
	}

	public void setCodiceSsn(String codiceSsn) {
		this.codiceSsn = codiceSsn;
	}

	public String getCodiceBelfiore() {
		return codiceBelfiore;
	}

	public void setCodiceBelfiore(String codiceBelfiore) {
		this.codiceBelfiore = codiceBelfiore;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public String getCodiceArea() {
		return codiceArea;
	}

	public void setCodiceArea(String codiceArea) {
		this.codiceArea = codiceArea;
	}

	public Date getVlInizio() {
		return vlInizio;
	}

	public void setVlInizio(Date vlInizio) {
		this.vlInizio = vlInizio;
	}

	public Date getVlFine() {
		return vlFine;
	}

	public void setVlFine(Date vlFine) {
		this.vlFine = vlFine;
	}

	public Date getDtAgg() {
		return dtAgg;
	}

	public void setDtAgg(Date dtAgg) {
		this.dtAgg = dtAgg;
	}

	public List<Provincia> getProvince() {
		return province;
	}

	public void setProvince(List<Provincia> province) {
		this.province = province;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Regione [");
		if (id != null)
			builder.append("id=").append(id).append(", ");
		if (codiceIstat != null)
			builder.append("codiceIstat=").append(codiceIstat).append(", ");
		if (codiceSsn != null)
			builder.append("codiceSsn=").append(codiceSsn).append(", ");
		if (codiceBelfiore != null)
			builder.append("codiceMf=").append(codiceBelfiore).append(", ");
		if (denominazione != null)
			builder.append("denominazione=").append(denominazione).append(", ");
		if (codiceArea != null)
			builder.append("codiceArea=").append(codiceArea);
		builder.append("]");
		return builder.toString();
	}
	
}

package it.regioneveneto.myp3.gestgraduatorie.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

@Entity
@Table(
		name="reg_province",
		uniqueConstraints = @UniqueConstraint(
				columnNames={ "codice_istat", "vl_inizio", "vl_fine" }
				)
		)
public class Provincia implements Serializable {

	private static final long serialVersionUID = 2156131577190011295L;

	@Id 
	@Column(name = "id_provincia", unique = true, nullable = false, updatable=false)
	private Long id;
	
	@Column(name = "codice_istat", nullable = false, updatable=false, length = 3 )
	private String codiceIstat;
	
	@Column(name = "sigla", nullable = false, updatable = false, length = 2 )
	private String sigla;
	
	@Column( name = "denominazione", nullable = false, updatable = false, length = 50 )
	private String denominazione;
	
	@Column( name = "vl_inizio" )
	@Temporal( value = TemporalType.DATE )
	private Date vlInizio;

	@Column( name = "vl_fine" )
	@Temporal( value = TemporalType.DATE )
	private Date vlFine;
	
	@Column( name = "dt_agg" )
	@Temporal( value = TemporalType.DATE )
	private Date dtAgg;
	
	@ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name="id_regione", nullable=false, updatable=false )
	private Regione regione;

	@OneToMany(
	        mappedBy = "provincia", 
	        fetch=FetchType.LAZY
	    )
	private List<Comune> comuni;
	
	public Provincia() { }
	
	public Provincia(Long id) {
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

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
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

	public Regione getRegione() {
		return regione;
	}

	public void setRegione(Regione regione) {
		this.regione = regione;
	}
	
	public List<Comune> getComuni() {
		return comuni;
	}

	public void setComuni(List<Comune> comuni) {
		this.comuni = comuni;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Provincia [");
		if (id != null)
			builder.append("id=").append(id).append(", ");
		if (codiceIstat != null)
			builder.append("codiceIstat=").append(codiceIstat).append(", ");
		if (sigla != null)
			builder.append("sigla=").append(sigla).append(", ");
		if (denominazione != null)
			builder.append("denominazione=").append(denominazione);
		builder.append("]");
		return builder.toString();
	}
	
}

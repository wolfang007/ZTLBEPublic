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

		name="reg_comuni",
		uniqueConstraints = @UniqueConstraint(
				columnNames={ "codice_istat", "vl_inizio", "vl_fine" }
				)
		)
public class Comune implements Serializable {

	private static final long serialVersionUID = -8530936579936456168L;
	
	@Id 
	@Column(name = "id_comune", unique = true, nullable = false, updatable=false)
	private Long id;
	
	@Column(name = "sigla_provincia", nullable = false, updatable=false, length = 2 )
	private String siglaProvincia;
	
	@Column(name = "codice_istat", nullable = true, updatable=false, length = 6 )
	private String codiceIstat;
	
	@Column(name = "codice_mf", updatable=false, length = 4 )
	private String codiceBelfiore;
	
	@Column(name = "denominazione", nullable = false, updatable=false, length = 3 )
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
    @JoinColumn( name="id_provincia", nullable=false, updatable=false )
	private Provincia provincia;
	
	@OneToMany(
			mappedBy = "comune", 
			fetch = FetchType.LAZY
			)
	private List<CAP> listaCap;
	
	public Comune() { }
	
	public Comune(Long id) {
		super();
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSiglaProvincia() {
		return siglaProvincia;
	}

	public void setSiglaProvincia(String siglaProvincia) {
		this.siglaProvincia = siglaProvincia;
	}

	public String getCodiceIstat() {
		return codiceIstat;
	}

	public void setCodiceIstat(String codiceIstat) {
		this.codiceIstat = codiceIstat;
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

	public Provincia getProvincia() {
		return provincia;
	}

	public void setProvincia(Provincia provincia) {
		this.provincia = provincia;
	}

	public List<CAP> getListaCap() {
		return listaCap;
	}

	public void setCap( List<CAP> listaCap) {
		this.listaCap = listaCap;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Comune [");
		if (id != null)
			builder.append("id=").append(id).append(", ");
		if (siglaProvincia != null)
			builder.append("siglaProvincia=").append(siglaProvincia).append(", ");
		if (codiceIstat != null)
			builder.append("codiceIstat=").append(codiceIstat).append(", ");
		if (codiceBelfiore != null)
			builder.append("codiceBelfiore=").append(codiceBelfiore).append(", ");
		if (denominazione != null)
			builder.append("denominazione=").append(denominazione);
		builder.append("]");
		return builder.toString();
	}

}

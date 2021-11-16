package it.regioneveneto.myp3.gestgraduatorie.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(
		name="reg_cap_comuni"
		)
public class CAP implements Serializable {

	private static final long serialVersionUID = -7469870732758565452L;
	
	@Id 
	@Column( name = "id_cap", unique = true, nullable = false, updatable = false )
	private Long id;
	
	@Column( name = "cap", nullable = false, updatable = false, length = 5 )
	private String cap;
	
	@ManyToOne( fetch = FetchType.LAZY )
	@JoinColumn( name = "id_comune" )
	private Comune comune;
	
	@Column( name = "vl_inizio" )
	@Temporal( value = TemporalType.DATE )
	private Date vlInizio;

	@Column( name = "vl_fine" )
	@Temporal( value = TemporalType.DATE )
	private Date vlFine;
	
	@Column( name = "dt_agg" )
	@Temporal( value = TemporalType.DATE )
	private Date dtAgg;
	
	public CAP() { }
	
	public CAP( Long id ) { 
		super();
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public Comune getComune() {
		return comune;
	}

	public void setComune(Comune comune) {
		this.comune = comune;
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CAP [");
		if (id != null)
			builder.append("id=").append(id).append(", ");
		if (cap != null)
			builder.append("cap=").append(cap);
		builder.append("]");
		return builder.toString();
	}
	

}

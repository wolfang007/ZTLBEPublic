package it.regioneveneto.myp3.gestgraduatorie.saml;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.util.Date;

@JsonIdentityInfo(generator=ObjectIdGenerators.UUIDGenerator.class,property="@idUte")
public class UteUtenteBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1922380224329621188L;
	private Long idUtente;
	private Date dtLastCheckin;
	private Date dtmModifica;
	private String email;
	private Boolean isAbilitato;
	private Boolean isCancellato;

	@JsonIgnore
	private String password;
	private String username;


	private Date dtRichAbilitazione;

	// IAM
	private String companyNameIam;
	private String countyOfBirthIam;
	private String dateOfBirthIam;
	private String emailIam;
	private String familyNameIam;
	private String fiscalNumberIam;
	private String genderIam;
	private String idCardIam;
	private String ivaCodeIam;
	private String jwtToken;
	private String nameIam;
	private String placeOfBirthIam;
	private String registeredOfficeIam;


	public UteUtenteBean() {}

    public UteUtenteBean(Long idUtente, Date dtLastCheckin, Date dtmModifica, String email, Boolean isAbilitato,
			Boolean isCancellato, String password, String username, Date dtRichAbilitazione) {
		super();
		this.idUtente = idUtente;
		this.dtLastCheckin = dtLastCheckin;
		this.dtmModifica = dtmModifica;
		this.email = email;
		this.isAbilitato = isAbilitato;
		this.isCancellato = isCancellato;
		this.password = password;
		this.username = username;
		this.dtRichAbilitazione = dtRichAbilitazione;
	}



	public Long getIdUtente() {
		return idUtente;
	}

	public void setIdUtente(Long idUtente) {
		this.idUtente = idUtente;
	}

	public Date getDtLastCheckin() {
		return dtLastCheckin;
	}

	public void setDtLastCheckin(Date dtLastCheckin) {
		this.dtLastCheckin = dtLastCheckin;
	}

	public Date getDtmModifica() {
		return dtmModifica;
	}

	public void setDtmModifica(Date dtmModifica) {
		this.dtmModifica = dtmModifica;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getIsAbilitato() {
		return isAbilitato;
	}

	public void setIsAbilitato(Boolean isAbilitato) {
		this.isAbilitato = isAbilitato;
	}

	public Boolean getIsCancellato() {
		return isCancellato;
	}

	public void setIsCancellato(Boolean isCancellato) {
		this.isCancellato = isCancellato;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}



	public Date getDtRichAbilitazione() {
		return dtRichAbilitazione;
	}

	public void setDtRichAbilitazione(Date dtRichAbilitazione) {
		this.dtRichAbilitazione = dtRichAbilitazione;
	}

	public String getCompanyNameIam() {
		return companyNameIam;
	}

	public String getCountyOfBirthIam() {
		return countyOfBirthIam;
	}

	public String getDateOfBirthIam() {
		return dateOfBirthIam;
	}

	public String getEmailIam() {
		return emailIam;
	}

	public String getFamilyNameIam() {
		return familyNameIam;
	}

	public String getFiscalNumberIam() {
		return fiscalNumberIam;
	}

	public String getGenderIam() {
		return genderIam;
	}

	public String getIdCardIam() {
		return idCardIam;
	}

	public String getIvaCodeIam() {
		return ivaCodeIam;
	}

	public String getJwtToken() {
		return jwtToken;
	}

	public String getNameIam() {
		return nameIam;
	}

	public String getPlaceOfBirthIam() {
		return placeOfBirthIam;
	}

	public String getRegisteredOfficeIam() {
		return registeredOfficeIam;
	}

	public void setCompanyNameIam(String companyNameIam) {
		this.companyNameIam = companyNameIam;
	}

	public void setCountyOfBirthIam(String countyOfBirthIam) {
		this.countyOfBirthIam = countyOfBirthIam;
	}

	public void setDateOfBirthIam(String dateOfBirthIam) {
		this.dateOfBirthIam = dateOfBirthIam;
	}

	public void setEmailIam(String emailIam) {
		this.emailIam = emailIam;
	}

	public void setFamilyNameIam(String familyNameIam) {
		this.familyNameIam = familyNameIam;
	}

	public void setFiscalNumberIam(String fiscalNumberIam) {
		this.fiscalNumberIam = fiscalNumberIam;
	}

	public void setGenderIam(String genderIam) {
		this.genderIam = genderIam;
	}

	public void setIdCardIam(String idCardIam) {
		this.idCardIam = idCardIam;
	}

	public void setIvaCodeIam(String ivaCodeIam) {
		this.ivaCodeIam = ivaCodeIam;
	}

	public void setJwtToken(String jwtToken) {
		this.jwtToken = jwtToken;
	}

	public void setNameIam(String nameIam) {
		this.nameIam = nameIam;
	}

	public void setPlaceOfBirthIam(String placeOfBirthIam) {
		this.placeOfBirthIam = placeOfBirthIam;
	}

	public void setRegisteredOfficeIam(String registeredOfficeIam) {
		this.registeredOfficeIam = registeredOfficeIam;
	}

}
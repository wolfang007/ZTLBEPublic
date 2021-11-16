package it.regioneveneto.myp3.gestgraduatorie.repository;

import java.text.SimpleDateFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.regioneveneto.myp3.gestgraduatorie.model.Bando;
import it.regioneveneto.myp3.gestgraduatorie.model.Istanza;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.BandoDTO;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.BandoFilterDTO;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.IstanzaFilterDTO;

public interface ServiceContributoRepository {
	public static final Logger logger = LoggerFactory.getLogger(ServiceContributoRepository.class);
	public static final SimpleDateFormat FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ssZ");

	public static final String selectBandi = "SELECT "
			

			+ "ban.id,"
			+ "ban.id_procedimento,"
			+ "ban.codiceidentificativobando,"
			+ "ban.dataattograduatoriadefinitiva,"
			+ "ban.dataattograduatoriapreliminare,"
			+ "ban.datadeliberabando,"
			+ "ban.datafinepresentazionedomande,"
			+ "ban.datainiziopresentazionedomande,"
			+ "ban.estremiattograduatoriadefinitiva,"
			+ "ban.estremiattograduatoriapreliminare,"
			+ "ban.estremideliberabando,"
			+ "ban.oggetto,"
			+ "ban.oggetto denominazione,"
			+ "ban.id_stato_bando,"
			+ "ban.datascadenzagraduatoriapreliminare,"
			+ "ban.importofinanziatototale,"
			+ "ban.id_tipologia_bando,"
			+ "ban.note,"
			+ "ban.data_inizio_validita,"
			+ "ban.data_fine_validita,"
			+ "ban.data_inserimento,"
			+ "ban.data_modifica,"  
			+ "ban.ente "
    		+ "FROM "
    		+ "gest_bando ban "
/*    		+ "LEFT JOIN reg_situazione_economica de "
	        + "ON de.codice_fiscale = ana.codice_fiscale "
	        + "LEFT JOIN reg_dati_catastali dc "
	        + "ON dc.codice_fiscale = ana.codice_fiscale "
	        + "LEFT JOIN reg_situazione_sociosanitaria ss "
	        + "ON ss.codice_fiscale = ana.codice_fiscale "
	        + "LEFT JOIN reg_cittadino_fragilita cit_fr "
	        + "ON cit_fr.id_cittadino = ana.id "
	        + "LEFT JOIN reg_tp_fragilita fr "
	        + "ON cit_fr.id_fragilita = fr.id_fragilita "
	        + "LEFT JOIN reg_tp_misura mis "
	        + "ON cit_fr.id_misura = mis.id_misura "
	        + "LEFT JOIN reg_cittadino_care_giver cit_car "
	        + "ON cit_car.id_cittadino = ana.id "
	        + "LEFT JOIN reg_care_giver car "
	        + "ON cit_car.id_care_giver = car.id_care_giver "
	        + "LEFT JOIN reg_comuni c "
	        + "ON c.codice_istat = ana.codice_comune_residenza "
	        + "LEFT JOIN reg_comuni c1 "
	        + "ON c1.codice_istat = ana.codice_comune_domicilio "
	        + "LEFT JOIN reg_tp_stato_civile sc "
	        + "ON sc.id_stato_civile = ana.stato_civile "
	        + "LEFT JOIN reg_tp_rapporto_parentela rp "
	        + "ON rp.id_parentela = ana.rapporto_parentela "*/
	        + "WHERE "
	        + "ban.data_fine_validita is null "
/*
	        + "ban de.data_fine_validita is null "
	        + "and dc.data_fine_validita is null "*/
	        + "and ban.ente = :enteLoggato ";
	
	public static final String selectRichieste = "SELECT "

			+ "ric.id ist_id,"
			+ "ric.numprotocolloentrata ist_numeroProtocollo,"
			+ "ric.punteggiograduatoriadefinitiva ist_punteggio, "
			
			+ "ric.datiRichiesta::json ist_richiesta,"
			+ "al.id all_id,"
			+ "al.descrizione_allegato all_descrizioneAllegato,"
			+ "al.nr_prot all_nrProt,"
			+ "al.data_prot all_dataProt,"
			+ "al.procid all_procId,"
			+ "al.idmybox all_idMyBox,"
			+ "al.fieldname all_fieldName,"
			+ "al.nomefile all_nomeFile,"
			+ "al.mimetype all_mimeType,"
			+ "al.length all_length,"
			+ "al.id_file all_idFile,"
			+ "tb.id tb_id,"
			+ "tb.codice_tipologiabando tb_codiceTipologiaBando,"
			+ "tb.descrizione tb_descrizione,"
			+ "sb.id sb_id,"
			+ "sb.codice_statobando sb_codiceStatoBando,"
			+ "sb.descrizione sb_descrizione,"
			+ "ban.oggetto ist_denominazioneBando, "
			+ "ban.id ist_idBando, "
			+ "ben.nome ist_nome,"
			+ "ben.cognome ist_cognome,"
			+ "ben.codice_fiscale ist_codiceFiscale,"
			+ "ben.telefono ist_telefono,"
			+ "ben.cellulare ist_cellulare,"
			+ "ben.residende_comune ist_residenteComuneRiferimento,"
			+ "ben.cittadino_ue ist_cittadinoUE,"
			+ "ben.email ist_email "
    		+ "FROM "
    		+ "gest_richiesta ric "
	        + "LEFT JOIN gest_beneficiario ben "
	        + "ON ben.id_richiesta = ric.id "
	        + "LEFT JOIN gest_allegato al "
	        + "ON al.id_richiesta = ric.id "
	        + "LEFT JOIN gest_bando ban "
	        + "ON ban.id = ric.id_bando "
	        + "LEFT JOIN gest_tp_tipologiabando tb "
	        + "ON tb.id = ban.id_tipologia_bando "
	        + "LEFT JOIN gest_tp_statobando sb "
	        + "ON sb.id = ban.id_stato_bando "
/*    		+ "LEFT JOIN reg_situazione_economica de "
	        + "ON de.codice_fiscale = ana.codice_fiscale "
	        + "LEFT JOIN reg_dati_catastali dc "
	        + "ON dc.codice_fiscale = ana.codice_fiscale "
	        + "LEFT JOIN reg_situazione_sociosanitaria ss "
	        + "ON ss.codice_fiscale = ana.codice_fiscale "
	        + "LEFT JOIN reg_cittadino_fragilita cit_fr "
	        + "ON cit_fr.id_cittadino = ana.id "
	        + "LEFT JOIN reg_tp_fragilita fr "
	        + "ON cit_fr.id_fragilita = fr.id_fragilita "
	        + "LEFT JOIN reg_tp_misura mis "
	        + "ON cit_fr.id_misura = mis.id_misura "
	        + "LEFT JOIN reg_cittadino_care_giver cit_car "
	        + "ON cit_car.id_cittadino = ana.id "
	        + "LEFT JOIN reg_care_giver car "
	        + "ON cit_car.id_care_giver = car.id_care_giver "
	        + "LEFT JOIN reg_comuni c "
	        + "ON c.codice_istat = ana.codice_comune_residenza "
	        + "LEFT JOIN reg_comuni c1 "
	        + "ON c1.codice_istat = ana.codice_comune_domicilio "
	        + "LEFT JOIN reg_tp_stato_civile sc "
	        + "ON sc.id_stato_civile = ana.stato_civile "
	        + "LEFT JOIN reg_tp_rapporto_parentela rp "
	        + "ON rp.id_parentela = ana.rapporto_parentela "*/
	        + "WHERE "
	        + "ric.data_fine_validita is null "
	        + "and ben.data_fine_validita is null "
/*
	        + "ban de.data_fine_validita is null "
	        + "and dc.data_fine_validita is null "*/
	        + "and ben.ente = :enteLoggato "
	        + "and ric.ente = :enteLoggato ";
	
	
	public static final String selectNucleoFamiliare = "SELECT "
			+ "ana.id idAnagraficaCittadino, "
    		+ "ana.nome, ana.cognome, ana.sesso, "
    		+ "ana.data_nascita,ana.codice_fiscale,"
    		+ "ana.cittadinanza,"
    		+ "rp.codice_parentela rapportoParentela," 
    		+ "rp.descrizione rapportoParentelaDescrizione,"
    		+ "ana.indirizzo_residenza, "
    		+ "ana.codice_comune_residenza comuneResidenza, "
    		+ "c.denominazione descrizioneComuneResidenza "
    		+ "FROM " 
    		+ "reg_anagrafica_cittadino ana "
    		+ "left join reg_situazione_sociosanitaria ss "
    		+ "on ss.codice_fiscale = ana.codice_fiscale "
	        + "LEFT JOIN reg_comuni c "
	        + "ON c.codice_istat = ana.codice_comune_residenza "
	        + "LEFT JOIN reg_tp_rapporto_parentela rp "
	        + "ON rp.id_parentela = ana.rapporto_parentela "
    		+ "WHERE "
    		+ "ana.data_fine_validita is null "
    		+ "and ss.data_fine_validita is null "
    		+ "and ana.codice_famiglia = :codiceFamiglia "
			//+ "and ana.codice_fiscale <> :codiceFiscale "
			+ "and ana.ente = :enteLoggato ";
	
	public static final String raggruppamento = "and ana.id in ("
	        + "select distinct ana.id " 
	        + "FROM reg_anagrafica_cittadino ana "
	        + "LEFT JOIN reg_situazione_economica de "
	        + "ON de.codice_fiscale = ana.codice_fiscale "
	        + "LEFT JOIN reg_dati_catastali dc "
	        + "ON dc.codice_fiscale = ana.codice_fiscale "
	        + "LEFT JOIN reg_situazione_sociosanitaria ss "
	        + "ON ss.codice_fiscale = ana.codice_fiscale "
	        + "LEFT JOIN reg_cittadino_fragilita cit_fr "
	        + "ON cit_fr.id_cittadino = ana.id "
	        + "LEFT JOIN reg_tp_fragilita fr "
	        + "ON cit_fr.id_fragilita = fr.id_fragilita "
	        + "LEFT JOIN reg_tp_misura mis "
	        + "ON cit_fr.id_misura = mis.id_misura "
	        + "LEFT JOIN reg_cittadino_care_giver cit_car "
	        + "ON cit_car.id_cittadino = ana.id "
	        + "LEFT JOIN reg_care_giver car "
	        + "ON cit_car.id_care_giver = car.id_care_giver "
	        + "WHERE  "
	        + "ana.data_fine_validita is null "
	        + "and de.data_fine_validita is null "
	        + "and dc.data_fine_validita is null "
	        + "and ana.ente = :enteLoggato "
	        + "<if(limit)>LIMIT :limit <endif> "
	        + "<if(offset)>OFFSET :offset <endif> "
	        + ")"
	        + " ORDER BY <orderBy>  <order> ";
	
	public static final String raggruppamentoPerExport = "and ana.id in ("
	        + "select distinct ana.id " 
	        + "FROM reg_anagrafica_cittadino ana "
	        + "LEFT JOIN reg_situazione_economica de "
	        + "ON de.codice_fiscale = ana.codice_fiscale "
	        + "LEFT JOIN reg_dati_catastali dc "
	        + "ON dc.codice_fiscale = ana.codice_fiscale "
	        + "LEFT JOIN reg_situazione_sociosanitaria ss "
	        + "ON ss.codice_fiscale = ana.codice_fiscale "
	        + "LEFT JOIN reg_cittadino_fragilita cit_fr "
	        + "ON cit_fr.id_cittadino = ana.id "
	        + "LEFT JOIN reg_tp_fragilita fr "
	        + "ON cit_fr.id_fragilita = fr.id_fragilita "
	        + "LEFT JOIN reg_tp_misura mis "
	        + "ON cit_fr.id_misura = mis.id_misura "
	        + "LEFT JOIN reg_cittadino_care_giver cit_car "
	        + "ON cit_car.id_cittadino = ana.id "
	        + "LEFT JOIN reg_care_giver car "
	        + "ON cit_car.id_care_giver = car.id_care_giver "
	        + "WHERE  "
	        + "ana.data_fine_validita is null "
	        + "and de.data_fine_validita is null "
	        + "and dc.data_fine_validita is null "
	        + "and ana.ente = :enteLoggato "
	        + ")"
	        + " ORDER BY ana.cognome,ana.nome ASC  ";
	
	List<Bando> getBandi(BandoFilterDTO filterBando,String ente, 
			Long offset, Long limit, String orderBy, String order) throws Exception;
	

	List<Istanza> getRichieste(IstanzaFilterDTO filterRichiesta, String ente, long offset, 
			long limit,  String orderBy, String order) throws Exception;
	
	List<Istanza> getRichieste(IstanzaFilterDTO filterRichiesta, String ente) throws Exception;

	Istanza getIstanzaById(long id, String ente) throws Exception;



	//void updateAnagraficaCittadino(AnagraficaCittadinoDTO anagraficaCittadino);
}

package it.regioneveneto.myp3.gestgraduatorie.repository.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.reflect.BeanMapper;
import org.jdbi.v3.core.statement.Query;
import org.jdbi.v3.core.statement.SqlStatements;
import org.jdbi.v3.stringtemplate4.StringTemplateEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import it.regioneveneto.myp3.gestgraduatorie.model.Allegato;
import it.regioneveneto.myp3.gestgraduatorie.model.Bando;
import it.regioneveneto.myp3.gestgraduatorie.model.Istanza;
import it.regioneveneto.myp3.gestgraduatorie.model.StatoBando;
import it.regioneveneto.myp3.gestgraduatorie.model.TipologiaBando;
import it.regioneveneto.myp3.gestgraduatorie.repository.RichiestaReducer;
import it.regioneveneto.myp3.gestgraduatorie.repository.ServiceContributoRepository;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.BandoFilterDTO;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.IstanzaFilterDTO;
import it.regioneveneto.myp3.gestgraduatorie.exception.ServiceException;
@Repository
@Transactional
public class ServiceContributoRepositoryImpl implements ServiceContributoRepository {
	private static final Logger LOG = LoggerFactory.getLogger(ServiceContributoRepositoryImpl.class);
    public static final SimpleDateFormat FORMAT = new SimpleDateFormat(
    		"EEE MMM dd yyyy HH:mm:ss Z", Locale.US);
    //Wed Feb 03 2021 00:00:00 GMT%2B0100 (Ora standard dell�Europa centrale)
	@Autowired
	Jdbi jdbi;
	
    @Autowired
    public Environment env;
	
    
 

	@Override
	public List<Bando> getBandi(BandoFilterDTO filterBando,String ente, Long offset,
			Long limit, String orderBy, String order) throws Exception{
		List<Bando> bandoList = 
				jdbi.withHandle(handle -> {
					
			handle.getConfig(SqlStatements.class).setUnusedBindingAllowed(true);
			
			Date dataInizioPresentazioneDomandaDa = null;
			Date dataInizioPresentazioneDomandaA = null;
			Date dataFinePresentazioneDomandaDa = null;
			Date dataFinePresentazioneDomandaA = null;
			String whereDataCondition = "";
			try {
				if(filterBando.getDataInizioDomandaDa()!=null) {
					dataInizioPresentazioneDomandaDa = FORMAT.parse(filterBando.getDataInizioDomandaDa());
					whereDataCondition += "  and ban.datainiziopresentazionedomande \\>= '" + dataInizioPresentazioneDomandaDa + "' ";
				}
				
				if(filterBando.getDataInizioDomandaA()!=null) {
					dataInizioPresentazioneDomandaA = FORMAT.parse(filterBando.getDataInizioDomandaA());
					whereDataCondition += "  and ban.datainiziopresentazionedomande \\<= '" + dataInizioPresentazioneDomandaA + "' ";
				}
				
				if(filterBando.getDataFineDomandaDa()!=null) {
					dataFinePresentazioneDomandaDa = FORMAT.parse(filterBando.getDataFineDomandaDa());
					whereDataCondition += "  and ban.datafinepresentazionedomande \\>= '" + dataFinePresentazioneDomandaDa + "' ";
				}
				if(filterBando.getDataFineDomandaA()!=null) {
					dataFinePresentazioneDomandaA = FORMAT.parse(filterBando.getDataFineDomandaA());
					whereDataCondition += "  and ban.datafinepresentazionedomande \\<= '" + dataFinePresentazioneDomandaA + "' ";
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			String query = selectBandi
					
					+ "<if(denominazione)>and upper(ban.oggetto) = upper(:denominazione) <endif> "
					+ whereDataCondition
					/*+ "<if(dataInizioDomandaDa)>and ban.datainiziopresentazionedomande "
					+ "between :dataInizioDomandaDa and :dataInizioDomandaA <endif> "
					+ "<if(dataFineDomandaDa)>and ban.datafinepresentazionedomande "
					+ "between :dataFineDomandaDa and :dataFineDomandaA <endif> "*/
					+ "<if(importoFinanziatoDa)>and CAST (ban.importofinanziatototale AS DOUBLE PRECISION) \\>=:importoFinanziatoDa <endif> "
					+ "<if(importoFinanziatoA)>and CAST (ban.importofinanziatototale AS DOUBLE PRECISION) \\<=:importoFinanziatoA <endif> "
		
			        //query per calcolare i dati da visualizzare in base all'offset e limit
			/*
			        + "and ana.id in ("
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
				        + "WHERE 1 = 1 "
				        + "and ana.data_fine_validita is null "
				        + "and de.data_fine_validita is null "
				        + "and dc.data_fine_validita is null "
				        + "and ana.ente = :enteLoggato "
						+ "<if(fragilita)>and fr.codice_fragilita = :fragilita<endif> "  
						+ "<if(misura)>and mis.codice_misura = :misura<endif> "
						+ "<if(nome)>and upper(ana.nome) like upper(:nome)||'%' <endif> "
						+ "<if(cognome)>and upper(ana.cognome) like upper(:cognome)||'%' <endif> " 
						+ "<if(codiceFiscale)>and upper(ana.codice_fiscale) like upper(:codiceFiscale)||'%' <endif> " 
						+ "<if(indirizzoResidenza)>and upper(ana.indirizzo_residenza) like upper(:indirizzoResidenza)||'%' <endif> " 
						+ "<if(indirizzoDomicilio)>and upper(ana.indirizzo_domicilio) like upper(:indirizzoDomicilio)||'%' <endif> " 
						//+ whereDataCondition
						+ "<if(numeroNucleoFamiliare)>and "
						+ "(select count(*) from reg_anagrafica_cittadino ana2 where ana.codice_famiglia = ana2.codice_famiglia) = :numeroNucleoFamiliare <endif> "
						+ "<if(comuneNascita)>and upper(ana.codice_comune_nascita) like upper(:comuneNascita)||'%' <endif> "
						+ "<if(sesso)>and upper(ana.sesso) = upper(:sesso) <endif> "
						+ "<if(cittadinanza)>and upper(ana.cittadinanza) like upper(:cittadinanza)||'%' <endif> "
						+ "<if(note)>and upper(ana.note) like upper('%'||:note)||'%' <endif> "
						+ "<if(numeroPersoneACarico)>and de.num_persone_carico = :numeroPersoneACarico <endif> "
						+ "<if(isee)>and de.valore_isee = :isee <endif> "
						+ "<if(famigliaMonoreddito)>and de.famiglia_monoreddito = :famigliaMonoreddito <endif> "
						+ "<if(pensioneInvalidita)>and ss.pensione_invalidita = :pensioneInvalidita <endif> "
						+ "<if(numeroComponentiConDisabilita)>and :numeroComponentiConDisabilita = "
			    		+ "(select count(*) from reg_anagrafica_cittadino ana2 "
			    		+ "left join reg_situazione_sociosanitaria ss2 "
			    		+ "ON ss2.codice_fiscale = ana2.codice_fiscale "
			    		+ "where ana.codice_famiglia = ana2.codice_famiglia "
			    		+ "and ss2.disabile = true and ana2.ente = :enteLoggato) <endif> "
			    		+ "<if(numeroComponentiConPatologie)>and :numeroComponentiConPatologie = "
						+ "(select count(*) from reg_anagrafica_cittadino ana2 "
						+ "left join reg_situazione_sociosanitaria ss2 "
						+ "ON ss2.codice_fiscale = ana2.codice_fiscale "
						+ "where ana.codice_famiglia = ana2.codice_famiglia "
						+ "and ss2.patologie = true and ana2.ente = :enteLoggato) <endif> "
						+ "<if(icare)>and ss.icare = :icare <endif> "
				        + "<if(limit)>LIMIT :limit <endif> "
				        + "<if(offset)>OFFSET :offset <endif> "
		        + ")"*/
		        + " ORDER BY <orderBy>  <order> ";

			
			
			LOG.info("query su gest_bando: {"+ query+"}");
			LOG.info("parametri: {"+ filterBando.toString() + "}");
			LOG.info("offset: " + offset + ", limit: " + limit + ", orderBy: " + orderBy + " order: "+ order);
			Query qq = handle.createQuery(query).setTemplateEngine(new StringTemplateEngine());
			
			qq.bind("enteLoggato", ente);
			qq.bindBean(filterBando);

			qq.bind("limit", limit);
			qq.bind("offset", offset);
			

			switch (orderBy) {
				case "denominazione": qq.define("orderBy", "ban.oggetto"); break;
				case "datainiziodomande": qq.define("orderBy", "ban.datainiziopresentazionedomande"); break;
				case "datafinedomande": qq.define("orderBy", "ban.datafinepresentazionedomande"); break;
				case "datagraduatoriadefinitiva": qq.define("orderBy", "ban.dataattograduatoriadefinitiva"); break;
				case "importofinanziato": qq.define("orderBy", "ban.importofinanziatototale"); break;
				default: qq.define("orderBy", "ban.oggetto");
			}
			
			qq.define("order", order.contentEquals("ASC") ? "ASC" : "DESC");
			qq.defineNamedBindings(); //<-- senza questo setting non vengono automaticamente creati i booleani usati nel template (saranno false)
			
			qq.registerRowMapper(BeanMapper.factory(Bando.class));
			
			return qq.mapTo(Bando.class).list();
			//return qq.reduceRows(new BandoReducer()).collect(Collectors.toList());
		});
		
		
		LOG.info("trovati " + bandoList.size() + " bandi");
		
		

		
		return bandoList;
	}




	@Override
	public List<Istanza> getRichieste(IstanzaFilterDTO filterRichiesta, String ente, long offset, long limit,
			String orderBy, String order) throws Exception {
		List<Istanza> istanzaList = 
				jdbi.withHandle(handle -> {
					
			handle.getConfig(SqlStatements.class).setUnusedBindingAllowed(true);
			
			String query = selectRichieste
					+ "<if(idBando)>and ben.id_bando = :idBando and ric.id_bando = :idBando <endif> "
					+ "<if(codiceFiscale)>and upper(ben.codice_fiscale) = upper(:codiceFiscale) <endif> "
					+ "<if(nome)>and upper(ben.nome) = upper(:nome) <endif> "
					+ "<if(cognome)>and upper(ben.cognome) = upper(:cognome) <endif> "
					+ "<if(telefono)>and ben.telefono = :telefono <endif> "
					+ "<if(cellulare)>and ben.cellulare = :cellulare <endif> "
					+ "<if(email)>and upper(ben.email) = upper(:email) <endif> "
					+ "<if(numeroProtocollo)>and upper(ric.numprotocolloentrata) = upper(:numeroProtocollo) <endif> "
					+ "<if(punteggioDa)>and CAST (ric.punteggiograduatoriadefinitiva AS DOUBLE PRECISION) \\>=:punteggioDa <endif> "
					+ "<if(punteggioA)>and CAST (ric.punteggiograduatoriadefinitiva AS DOUBLE PRECISION) \\<=:punteggioA <endif> "
		
			        //query per calcolare i dati da visualizzare in base all'offset e limit
			/*
			        + "and ana.id in ("
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
				        + "WHERE 1 = 1 "
				        + "and ana.data_fine_validita is null "
				        + "and de.data_fine_validita is null "
				        + "and dc.data_fine_validita is null "
				        + "and ana.ente = :enteLoggato "
						+ "<if(fragilita)>and fr.codice_fragilita = :fragilita<endif> "  
						+ "<if(misura)>and mis.codice_misura = :misura<endif> "
						+ "<if(nome)>and upper(ana.nome) like upper(:nome)||'%' <endif> "
						+ "<if(cognome)>and upper(ana.cognome) like upper(:cognome)||'%' <endif> " 
						+ "<if(codiceFiscale)>and upper(ana.codice_fiscale) like upper(:codiceFiscale)||'%' <endif> " 
						+ "<if(indirizzoResidenza)>and upper(ana.indirizzo_residenza) like upper(:indirizzoResidenza)||'%' <endif> " 
						+ "<if(indirizzoDomicilio)>and upper(ana.indirizzo_domicilio) like upper(:indirizzoDomicilio)||'%' <endif> " 
						//+ whereDataCondition
						+ "<if(numeroNucleoFamiliare)>and "
						+ "(select count(*) from reg_anagrafica_cittadino ana2 where ana.codice_famiglia = ana2.codice_famiglia) = :numeroNucleoFamiliare <endif> "
						+ "<if(comuneNascita)>and upper(ana.codice_comune_nascita) like upper(:comuneNascita)||'%' <endif> "
						+ "<if(sesso)>and upper(ana.sesso) = upper(:sesso) <endif> "
						+ "<if(cittadinanza)>and upper(ana.cittadinanza) like upper(:cittadinanza)||'%' <endif> "
						+ "<if(note)>and upper(ana.note) like upper('%'||:note)||'%' <endif> "
						+ "<if(numeroPersoneACarico)>and de.num_persone_carico = :numeroPersoneACarico <endif> "
						+ "<if(isee)>and de.valore_isee = :isee <endif> "
						+ "<if(famigliaMonoreddito)>and de.famiglia_monoreddito = :famigliaMonoreddito <endif> "
						+ "<if(pensioneInvalidita)>and ss.pensione_invalidita = :pensioneInvalidita <endif> "
						+ "<if(numeroComponentiConDisabilita)>and :numeroComponentiConDisabilita = "
			    		+ "(select count(*) from reg_anagrafica_cittadino ana2 "
			    		+ "left join reg_situazione_sociosanitaria ss2 "
			    		+ "ON ss2.codice_fiscale = ana2.codice_fiscale "
			    		+ "where ana.codice_famiglia = ana2.codice_famiglia "
			    		+ "and ss2.disabile = true and ana2.ente = :enteLoggato) <endif> "
			    		+ "<if(numeroComponentiConPatologie)>and :numeroComponentiConPatologie = "
						+ "(select count(*) from reg_anagrafica_cittadino ana2 "
						+ "left join reg_situazione_sociosanitaria ss2 "
						+ "ON ss2.codice_fiscale = ana2.codice_fiscale "
						+ "where ana.codice_famiglia = ana2.codice_famiglia "
						+ "and ss2.patologie = true and ana2.ente = :enteLoggato) <endif> "
						+ "<if(icare)>and ss.icare = :icare <endif> "
				        + "<if(limit)>LIMIT :limit <endif> "
				        + "<if(offset)>OFFSET :offset <endif> "
		        + ")"*/
		        + " ORDER BY <orderBy>  <order> ";

			
			
			LOG.info("query su gest_richieste: {"+ query+"}");
			LOG.info("parametri: {"+ filterRichiesta.toString() + "}");
			LOG.info("offset: " + offset + ", limit: " + limit + ", orderBy: " + orderBy + " order: "+ order);
			Query qq = handle.createQuery(query).setTemplateEngine(new StringTemplateEngine());
			
			qq.bind("enteLoggato", ente);
			qq.bindBean(filterRichiesta);

			qq.bind("limit", limit);
			qq.bind("offset", offset);
			

			switch (orderBy) {
				case "denominazione": qq.define("orderBy", "ban.oggetto"); break;
				case "numeroProtocollo": qq.define("orderBy", "ric.numprotocolloentrata"); break;
				case "nome": qq.define("orderBy", "ben.nome"); break;
				case "cognome": qq.define("orderBy", "ben.cognome"); break;
				case "codiceFiscale": qq.define("orderBy", "ben.codice_fiscale"); break;
				case "punteggio": qq.define("orderBy", "ric.punteggiograduatoriadefinitiva"); break;
			
				default: qq.define("orderBy", "ban.oggetto");
			}
			
			qq.define("order", order.contentEquals("ASC") ? "ASC" : "DESC");
			qq.defineNamedBindings(); //<-- senza questo setting non vengono automaticamente creati i booleani usati nel template (saranno false)
			
			qq.registerRowMapper(BeanMapper.factory(Istanza.class,"ist"));  
			qq.registerRowMapper(BeanMapper.factory(Allegato.class,"all")); 
			qq.registerRowMapper(BeanMapper.factory(TipologiaBando.class,"tb"));
			qq.registerRowMapper(BeanMapper.factory(StatoBando.class,"sb"));
			//return qq.mapTo(Istanza.class).list();
			return qq.reduceRows(new RichiestaReducer()).collect(Collectors.toList());
		});
		
		
		LOG.info("trovati " + istanzaList.size() + " richieste");
		
		

		
		return istanzaList;
	}
	
	
	@Override
	public List<Istanza> getRichieste(IstanzaFilterDTO filterRichiesta, String ente) throws Exception {
		List<Istanza> istanzaList = 
				jdbi.withHandle(handle -> {
					
			handle.getConfig(SqlStatements.class).setUnusedBindingAllowed(true);
			
			String query = selectRichieste
					+ "<if(idBando)>and ben.id_bando = :idBando and ric.id_bando = :idBando <endif> "
					+ "<if(codiceFiscale)>and upper(ben.codice_fiscale) = upper(:codiceFiscale) <endif> "
					+ "<if(nome)>and upper(ben.nome) = upper(:nome) <endif> "
					+ "<if(cognome)>and upper(ben.cognome) = upper(:cognome) <endif> "
					+ "<if(telefono)>and ben.telefono = :telefono <endif> "
					+ "<if(cellulare)>and ben.cellulare = :cellulare <endif> "
					+ "<if(email)>and upper(ben.email) = upper(:email) <endif> "
					+ "<if(numeroProtocollo)>and upper(ric.numprotocolloentrata) = upper(:numeroProtocollo) <endif> "
					+ "<if(punteggioDa)>and CAST (ric.punteggiograduatoriadefinitiva AS INTEGER) "
					+ "\\<:punteggioA and CAST (ric.punteggiograduatoriadefinitiva AS INTEGER) \\>:punteggioDa <endif> "
		            + " ORDER BY <orderBy>  <order> ";

			
			
			LOG.info("query su gest_richieste: {"+ query+"}");
			LOG.info("parametri: {"+ filterRichiesta.toString() + "}");
		
			Query qq = handle.createQuery(query).setTemplateEngine(new StringTemplateEngine());
			
			qq.bind("enteLoggato", ente);
			qq.bindBean(filterRichiesta);
			
			
			qq.define("orderBy", "ben.data_modifica");
		
			
			qq.define("order", "DESC");
			qq.defineNamedBindings(); //<-- senza questo setting non vengono automaticamente creati i booleani usati nel template (saranno false)
			
			qq.registerRowMapper(BeanMapper.factory(Istanza.class,"ist"));  
			qq.registerRowMapper(BeanMapper.factory(Allegato.class,"all")); 
			qq.registerRowMapper(BeanMapper.factory(TipologiaBando.class,"tb"));
			qq.registerRowMapper(BeanMapper.factory(StatoBando.class,"sb"));
			//return qq.mapTo(Istanza.class).list();
			return qq.reduceRows(new RichiestaReducer()).collect(Collectors.toList());
		});
		
		
		LOG.info("trovati " + istanzaList.size() + " richieste");
		
		

		
		return istanzaList;
	}




	@Override
	public Istanza getIstanzaById(long id, String ente) throws Exception {

		Optional<Istanza> richiesta = 
				jdbi.withHandle(handle -> {
					
			handle.getConfig(SqlStatements.class).setUnusedBindingAllowed(true);

			String query = selectRichieste

		        + "and ric.id = :id ";

			
			
				LOG.info("query su gest_bando: {"+ query+"}");
				Query qq = handle.createQuery(query);
				
		
				qq.bind("id", id);
				qq.bind("enteLoggato", ente);
				
				qq.registerRowMapper(BeanMapper.factory(Istanza.class,"ist"));  
				qq.registerRowMapper(BeanMapper.factory(Allegato.class,"all")); 
				qq.registerRowMapper(BeanMapper.factory(TipologiaBando.class,"tb"));
				qq.registerRowMapper(BeanMapper.factory(StatoBando.class,"sb"));
				
				return qq.reduceRows(new RichiestaReducer()).findFirst();
			});
		if(richiesta.isEmpty())
			
			throw new ServiceException("Richiesta con id: " + id + " non presente!");
		
		
		
		
		return richiesta.get();
	}


}

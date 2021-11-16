package it.regioneveneto.myp3.gestgraduatorie.repository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlBatch;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.regioneveneto.myp3.gestgraduatorie.model.Allegato;
import it.regioneveneto.myp3.gestgraduatorie.model.AuditUpload;
import it.regioneveneto.myp3.gestgraduatorie.model.Bando;
import it.regioneveneto.myp3.gestgraduatorie.model.Beneficiario;
import it.regioneveneto.myp3.gestgraduatorie.model.Richiesta;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.AllegatoDTO;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.AuditUploadDTO;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.BandoDTO;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.BeneficiarioDTO;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.RichiestaDTO;

public interface ServiceRepository {


	public static final Logger logger = LoggerFactory.getLogger(ServiceRepository.class);
	public static final SimpleDateFormat FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ssZ");
	
	
	
	default Richiesta saveRichiestaContributo(RichiestaDTO richiesta) {
		logger.info("saveRichiestaContributo");
		
		BandoDTO bandoDTO = richiesta.getBando();
		
		Bando bando = null;
		//se il bando non esiste creo un nuovo bando
		if(!exitsBando(bandoDTO.getCodiceIdentificativoBando(),richiesta.getEnte())) {
			bando = createBando(bandoDTO);
		}else {
			bando = getBandoByCodiceIdentificativoBando(bandoDTO.getCodiceIdentificativoBando(),richiesta.getEnte());
			
		}
		richiesta.setIdBando(bando.getId());
		//altrimenti inserisco la richiesta senza creare un nuovo bando 
		Richiesta richiestaInserita = createRichiestaContributo(richiesta);
		richiesta.setId(richiestaInserita.getId());
		BeneficiarioDTO beneficiarioDTO = richiesta.getBeneficiarioDTO();
		beneficiarioDTO.setIdBando(bando.getId());
		beneficiarioDTO.setIdRichiesta(richiestaInserita.getId());
		Beneficiario beneficiario = createBeneficiario(beneficiarioDTO,richiesta.getEnte());
		
		//richiesta.setEnte(richiestaInserita.getEnte());
		List<Allegato> allegatiList = insertAllegati(richiesta.getAllegati(),richiestaInserita.getId(),richiestaInserita.getEnte());
		if(allegatiList!=null)
			
			logger.info("numero di allegati inseriti: " + allegatiList.size());
		else
			logger.info("non ci sono allegati da inserire ");
		return richiestaInserita;
	}


	@SqlUpdate("insert into gest_beneficiario "
			+ "(id_richiesta,"
			+ "id_bando, "
			+ "nome,"
			+ "cognome,"
			+ "codice_fiscale, "
			+ "telefono,"
			+ "cellulare,"
			+ "email, "
			+ "residende_comune,"
			+ "cittadino_ue,"
			+ "data_inizio_validita,"
			+ "data_fine_validita,"
			+ "data_inserimento,"
			+ "data_modifica,"
			+ "ente) "
			+ "values ("
			+ ":idRichiesta,"
			+ ":idBando,"
			+ ":nome,"
			+ ":cognome,"
			+ ":codiceFiscale,"
			+ ":telefono,"
			+ ":cellulare,"
			+ ":email,"
			+ ":residendeComune,"
			+ ":cittadinoUE,"
			+ "now(),"
			+ "null,"
			+ "now(),"
			+ "now(),"
			+ ":enteLoggato)" )
	@RegisterBeanMapper(Beneficiario.class)
	@GetGeneratedKeys
	Beneficiario createBeneficiario(@BindBean BeneficiarioDTO beneficiarioDTO,@Bind("enteLoggato") String ente);



	@SqlUpdate("insert into gest_bando "
			+ "(codiceidentificativobando,"
			+ "datainiziopresentazionedomande, "
			+ "datafinepresentazionedomande, "
			+ "oggetto,"
			+ "id_stato_bando,"
			+ "data_inizio_validita,"
			+ "data_fine_validita,"
			+ "data_inserimento,"
			+ "data_modifica,"
			+ "ente) "
			+ "values ("
			+ ":codiceIdentificativoBando,"
			+ ":dataInizioPresentazioneDomande,"
			+ ":dataFinePresentazioneDomande,"
			+ ":oggetto,"
			+ "1,"
			+ "now(),"
			+ "null,"
			+ "now(),"
			+ "now(),"
			+ ":ente)" )
	@RegisterBeanMapper(Bando.class)
	@GetGeneratedKeys
	Bando createBando(@BindBean BandoDTO bandoDTO);


	@SqlQuery("SELECT "
			+ "id"
			+ " FROM gest_bando WHERE "
			+ "upper(codiceidentificativobando) = upper(:codiceidentificativobando) "
			+ "and ente = :enteLoggato")
	@RegisterBeanMapper(value = Bando.class)
	Bando getBandoByCodiceIdentificativoBando(@Bind("codiceidentificativobando") String codiceIdentificativoBando,@Bind("enteLoggato") String ente);


	@SqlQuery("SELECT EXISTS(SELECT * FROM gest_bando WHERE "
			+ "upper(codiceidentificativobando) = upper(:codiceidentificativobando) "
			+ "and ente = :enteLoggato)")
	boolean exitsBando(@Bind("codiceidentificativobando") String codiceIdentificativoBando,@Bind("enteLoggato") String ente);



	@SqlBatch("insert into gest_allegato (id_richiesta, descrizione_allegato, nr_prot, "
			+ "data_prot, procid, idmybox, fieldname, nomefile,"
			+ "mimetype,length,id_file,tipologia,note,data_inizio_validita,data_fine_validita,"
			+ "data_inserimento,data_modifica,ente,user_id) "
			+ "values (:richiesta, :descrizioneAllegato, :nrProt, "
			+ ":dataProt, :procId, :idMyBox,:fieldName,:nomeFile,"
			+ ":mimeType,:length,:idFile,:tipologia,:note,now(),null,"
			+ "now(),now(),:enteLoggato,:userId)")
	@RegisterBeanMapper(Allegato.class)
	@GetGeneratedKeys
	List<Allegato> insertAllegati(@BindBean List<AllegatoDTO> allegati,@Bind("richiesta") Long idRichiesta,@Bind("enteLoggato") String ente);


	@SqlUpdate("INSERT INTO gest_richiesta(" +
			"nome," +
			"cognome," +
			"id_bando,"+ 
			"numprotocolloentrata," +
			"data_prot_entrata," +
			"datirichiesta," +
			"data_inizio_validita," +
    		"data_fine_validita," +
    		"data_inserimento," +
    		"data_modifica,"
    		+ "ente)" +
    		"VALUES(" +
    		":nome," +
    		":cognome," +
    		":idBando," +
    		":numProtocolloEntrata," +
    		"now()," +
    		"to_json(:datiRichiesta::json)," +
    		"now()," +
    		"null," +
    		"now()," +
    		"now(),"
    		+ ":ente" +
			")")
	@RegisterBeanMapper(Richiesta.class)
    @GetGeneratedKeys
    Richiesta createRichiestaContributo(@BindBean RichiestaDTO richiesta);





	@SqlQuery("SELECT * FROM gest_allegato "
			+ "WHERE id = :id_all "
			+ "and ente = :enteLoggato")
	@RegisterBeanMapper(value = Allegato.class)
	Allegato getAllegatoById(@Bind("id_all") Long id,@Bind("enteLoggato") String ente);
	
	@SqlUpdate("UPDATE gest_richiesta SET " + 
			"punteggiograduatoriadefinitiva = :punteggio"
			+ " WHERE id = :idRichiesta"
			+ " and id_bando = :idBando") 
	int aggiornaRichiestaMassiva(@Bind("punteggio") String punteggio, @Bind("idRichiesta") Long idRichiesta, @Bind("idBando") Long idBando);

	@SqlQuery("SELECT EXISTS(select * from gest_richiesta gr  "
			+ "where gr.punteggiograduatoriadefinitiva is not null "
			+ "and gr.id_bando = :id and gr.ente = :enteLoggato) ")
	boolean verificaBandoDaChiudere(@Bind("id") Long id,@Bind("enteLoggato") String ente);
	
	@SqlQuery("SELECT EXISTS(SELECT * FROM gest_bando gb WHERE "
			+ "gb.dataattograduatoriadefinitiva is not null "
			+ "and gb.id = :id and gb.id_stato_bando = 2 and ente = :enteLoggato)")			
	boolean verificaBandoChiuso(@Bind("id") Long id,@Bind("enteLoggato") String ente);

	@SqlUpdate("UPDATE gest_bando set "
			+ "id_stato_bando = 2, "
			+ "dataattograduatoriadefinitiva = now() "
			+ "where id = :id and ente = :enteLoggato")
	int chiudiBando(@Bind("id") long id,@Bind("enteLoggato") String ente);
	

	@SqlUpdate("INSERT INTO gest_richiesta(" +
			"id_bando," +
			"cf_richiedente,"+
			"nome," +
			"cognome," +
			"datirichiesta," +
			"data_inizio_validita," +
    		"data_fine_validita," +
    		"data_inserimento," +
    		"data_modifica," +
    		"ente)" +
    		"VALUES(" +
    		":idBando," +
    		":cfRichiedente," +
    		":nome," +
    		":cognome," +
    		"to_json(:datiRichiesta::json)," +
    		"now()," +
    		"null," +
    		"now()," +
    		"now()," +
    		":ente" +
			")")
	@RegisterBeanMapper(Richiesta.class)
    @GetGeneratedKeys
    Richiesta saveInsertMassivo(@BindBean RichiestaDTO richiesta);
	
	 
    @SqlQuery("SELECT * FROM gest_bando "
        + "WHERE codiceidentificativobando = :codiceBando "
        + "and ente = :enteLoggato")
	@RegisterBeanMapper(value = Bando.class)
    Bando getBandoByCodiceIdentify(@Bind("codiceBando") String codiceBando,@Bind("enteLoggato") String ente);

    
    @SqlQuery("SELECT id " 
    		+ "FROM gest_bando "
    		+ "WHERE codiceidentificativobando = :codiceBando")
    Optional<Integer> getIdBandoByCodeIdentify(@Bind("codiceBando") String codiceBando);
    
    
	@SqlUpdate("INSERT INTO gest_bando(" +
			"codiceIdentificativoBando," +
			"oggetto," +
			"data_inizio_validita," +
    		"data_fine_validita," +
    		"data_inserimento," +
    		"data_modifica," +
    		"ente)" +
    		"VALUES(" +
    		":codiceIdentificativoBando," +
    		":oggetto," +
    		"now()," +
    		"null," +
    		"now()," +
    		"now()," +
    		":ente" +
			")")
	@RegisterBeanMapper(Bando.class)
    @GetGeneratedKeys
    Bando insertBando(@BindBean BandoDTO bandoDTO);
    
    
	@SqlUpdate("INSERT INTO gest_beneficiario(" +
			"id_richiesta," +
			"id_bando," +
			"nome," +
			"cognome," +
			"codice_fiscale," +
			"telefono," +
			"cellulare," +
			"email," +
			"residende_comune," +
			"cittadino_ue," +
			"data_inizio_validita," +
    		"data_fine_validita," +
    		"data_inserimento," +
    		"data_modifica," +
    		"ente)" +
    		"VALUES(" +
    		":idRichiesta," +
    		":idBando," +
    		":nome," +
    		":cognome," +
    		":codiceFiscale," +
    		":telefono," +
    		":cellulare," +
    		":email," +
    		":residendeComune," +
    		":cittadinoUE," +
    		"now()," +
    		"null," +
    		"now()," +
    		"now()," +
    		":ente" +
			")")
	@RegisterBeanMapper(Beneficiario.class)
    @GetGeneratedKeys
    Beneficiario insertBeneficiario(@BindBean BeneficiarioDTO beneficiarioDTO);
	
	
	
	@SqlUpdate("INSERT INTO gest_audit_uploadfile(" +
			"nome_utente," +
			"id_file," +
			"data_caricamento," +
			"id_bando," +
			"esito)" +
    		"VALUES(" +
    		":nomeUtente," +
    		":idFile," +
    		"now()," +
    		":idBando," +
    		":esito" +
			")")
	@RegisterBeanMapper(AuditUpload.class)
    @GetGeneratedKeys
    AuditUpload insertAuditUpload(@BindBean AuditUploadDTO auditUploadDTO);
	
	
    @SqlQuery("SELECT count(*)" 
    		+ " FROM gest_beneficiario"
    		+ " WHERE id_bando = :idBando"
    		+ " AND codice_fiscale = :codiceFiscale"
    		+ " AND data_fine_validita is null")
    Optional<Integer> verificaBeneficiario(@Bind("idBando") Long idBando,@Bind("codiceFiscale") String codiceFiscale);
	
}

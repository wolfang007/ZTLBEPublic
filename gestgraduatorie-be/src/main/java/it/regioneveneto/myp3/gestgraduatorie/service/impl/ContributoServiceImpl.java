package it.regioneveneto.myp3.gestgraduatorie.service.impl;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.regioneveneto.myp3.gestgraduatorie.exception.ServiceException;
import it.regioneveneto.myp3.gestgraduatorie.repository.ServiceContributoRepository;
import it.regioneveneto.myp3.gestgraduatorie.repository.ServiceRepository;
import it.regioneveneto.myp3.gestgraduatorie.service.ContributoService;
import it.regioneveneto.myp3.gestgraduatorie.utils.ObjectMapperUtils;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.AllegatoDTO;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.AuditUploadDTO;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.BandoDTO;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.BandoFilterDTO;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.BeneficiarioDTO;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.IstanzaDTO;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.IstanzaFilterDTO;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.RichiestaDTO;


@Service
public class ContributoServiceImpl implements ContributoService{

	private static final Logger logger = LoggerFactory.getLogger(ContributoServiceImpl.class);
	public static final SimpleDateFormat FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ssZ");
	public static final SimpleDateFormat FORMAT2 = new SimpleDateFormat(
            "yyyy-MM-dd");
	
	
	@Autowired
	ServiceRepository serviceRepo;
	
	@Autowired
	ServiceContributoRepository contributoRepo;
	
	@Value("${gestgraduatorie.myregistry.url}")
    private String clientMyRegistry;
	
	@Value("${gestgraduatorie.mypa.url}")
    private String clientMyPa;
	
	@Value("${gestgraduatorie.myoperatorregistry.url}")
    private String clientMyOperatorRegistry;
	
	@Transactional( rollbackFor = Exception.class, readOnly = false )
	@Override
	public RichiestaDTO createRichiestaContributo(String richiesta) throws ServiceException {

		RichiestaDTO richiestaInserita = null;
		try {
			logger.info("creazione di una richiesta di contributo");
			
			Date today = FORMAT.parse(FORMAT.format(new Date()));
			RichiestaDTO richiestaDTO = createRichiesta(richiesta);
			
			
			String instanceFormId = getInstanceFormId(richiestaDTO.getEnte(), 
					richiestaDTO.getProcId(),richiestaDTO.getUserRegistryId());
			
			JSONObject payload = new JSONObject(richiesta);
			
			Object jsonDataFineBando = getStringFromJSONPayload(payload, "data_fine_bando");
			String dataFineBando =  isObjectNotNull(jsonDataFineBando) ? (String) jsonDataFineBando : null;
			logger.info("dataFineBando: " + dataFineBando);
			
			Object jsonCodiceFiscaleProfilo = getStringFromJSONPayload(payload, "codice_fiscale_profilo");
			String codiceFiscaleProfilo =  isObjectNotNull(jsonCodiceFiscaleProfilo) ? (String) jsonCodiceFiscaleProfilo : null;
			logger.info("codiceFiscaleProfilo: " + codiceFiscaleProfilo);
			
			
			
			BandoDTO bando = getDatiPubblicazioneBando(richiestaDTO.getEnte(),instanceFormId);
			
			bando.setDataFinePresentazioneDomande(FORMAT2.parse(dataFineBando));
			logger.info("bando: " + bando.toString());
			List<AllegatoDTO> allegatiObj = getFileCaricatiForm(richiestaDTO.getEnte(), 
					richiestaDTO.getProcId(),richiestaDTO.getUserRegistryId(),codiceFiscaleProfilo);
	   		 
	   		 
			//richiestaDTO.setTipo_registry(tipoRegistry);
	   		 
			logger.info("file trovati : " + allegatiObj.size());
			List<AllegatoDTO> allegatiObjTORichiestae = new ArrayList<AllegatoDTO>();
			for(int i =0;i<allegatiObj.size();i++) {
				logger.info("Field name : " + allegatiObj.get(i).getFieldName() + " *OK*");
				if(!allegatiObj.get(i).getFieldName().equals("*"))
				{
					logger.info(" *OK*");
					allegatiObjTORichiestae.add(allegatiObj.get(i));
				}
				  
			}
			richiestaDTO.setBando(bando);
			richiestaDTO.setAllegati(allegatiObjTORichiestae);
			
			
			
			
			
			richiestaDTO.setDataInserimento(today);
			richiestaDTO.setDataModifica(today);
			richiestaInserita =  ObjectMapperUtils.map(serviceRepo.saveRichiestaContributo(richiestaDTO),RichiestaDTO.class);
		}catch (Exception e) {
            logger.error("Errore creando una richiesta di contributo " + e.getMessage());
            throw new ServiceException(e);
        }
		return richiestaInserita;
	}






	private RichiestaDTO createRichiesta(String richiesta) {
		logger.info("richiesta: " + richiesta);
		RichiestaDTO richiestaDTO = new RichiestaDTO();
		JSONObject payload = new JSONObject(richiesta);
		Object jsonNome = getStringFromJSONPayload(payload, "nome_richiedente");
		String nomeRichiedente =  isObjectNotNull(jsonNome) ? (String) jsonNome : null;
		logger.info("nome: " + nomeRichiedente);
		Object jsonCognome = getStringFromJSONPayload(payload, "cognome_richiedente");
		String cognomeRichiedente =  isObjectNotNull(jsonCognome) ? (String) jsonCognome : null;
		logger.info("cognome: " + cognomeRichiedente);
		
		Object jsonProtocollo_in_entrata = getStringFromJSONPayload(payload, "numero_protocollo_manuale");
		String protocollo_in_entrata =  isObjectNotNull(jsonProtocollo_in_entrata) ? (String) jsonProtocollo_in_entrata : null;
		logger.info("protocollo_in_entrata: " + protocollo_in_entrata);
		
		
		Object jsonProcId = getStringFromJSONPayload(payload, "processIstanceId");
		String processIstanceId =  isObjectNotNull(jsonNome) ? (String) jsonProcId : null;
		logger.info("processIstanceId: " + processIstanceId);
		Object jsonUserRegistryId = getStringFromJSONPayload(payload, "userRegistryId");
		String userRegistryId =  isObjectNotNull(jsonNome) ? (String) jsonUserRegistryId : null;
		logger.info("userRegistryId: " + userRegistryId);
		
		BeneficiarioDTO beneficiarioDTO = new BeneficiarioDTO();
		
		Object jsonNomeBeneficiario = getStringFromJSONPayload(payload, "nome_beneficiario");
		String nomeBeneficiario =  isObjectNotNull(jsonNomeBeneficiario) ? (String) jsonNomeBeneficiario : null;
		logger.info("nomeBeneficiario: " + nomeBeneficiario);
		Object jsonCognomeBeneficiario = getStringFromJSONPayload(payload, "cognome_beneficiario");
		String cognomeBeneficiario =  isObjectNotNull(jsonCognomeBeneficiario) ? (String) jsonCognomeBeneficiario : null;
		logger.info("cognomeBeneficiario: " + cognomeBeneficiario);
		Object jsonCodiceFiscaleBeneficiario = getStringFromJSONPayload(payload, "codice_fiscale_beneficiario");
		String codiceFiscaleBeneficiario =  isObjectNotNull(jsonCodiceFiscaleBeneficiario) ? (String) jsonCodiceFiscaleBeneficiario : null;
		logger.info("codiceFiscaleBeneficiario: " + codiceFiscaleBeneficiario);
		Object jsonCellulare = getStringFromJSONPayload(payload, "numero_di_telefono_beneficiario");
		String cellulare =  isObjectNotNull(jsonCellulare) ? (String) jsonCellulare : null;
		logger.info("cellulare: " + cellulare);
		
		
		
		Object jsonResidenteComuneRif = getStringFromJSONPayload(payload, "residente_nel_comune_di_riferimento");
		String residenteComuneRif =  isObjectNotNull(jsonResidenteComuneRif) ? (String) jsonResidenteComuneRif : null;
		logger.info("residenteComuneRif: " + residenteComuneRif);
		
		Object jsonEmail = getStringFromJSONPayload(payload, "indirizzo_posta_elettronica_beneficiario");
		String email =  isObjectNotNull(jsonEmail) ? (String) jsonEmail : null;
		logger.info("email: " + email);
		
		
		Object jsonCittadinoItalianoOUE = getStringFromJSONPayload(payload, "cittadino_italiano_o_di_un_paese_unione_europea");
		String cittadinoItalianoOUE =  isObjectNotNull(jsonCittadinoItalianoOUE) ? (String) jsonCittadinoItalianoOUE : null;
		logger.info("cittadinoItalianoOUE: " + cittadinoItalianoOUE);
		
		Object jsonTenant = getStringFromJSONPayload(payload, "tenant");
		String ente =  isObjectNotNull(jsonTenant) ? (String) jsonTenant : null;
		logger.info("tenant: " + ente);
		
		
		beneficiarioDTO.setNome(nomeBeneficiario);
		beneficiarioDTO.setCognome(cognomeBeneficiario);
		beneficiarioDTO.setCodiceFiscale(codiceFiscaleBeneficiario);
		beneficiarioDTO.setCellulare(cellulare);
		beneficiarioDTO.setEmail(email);
		if(cittadinoItalianoOUE.equalsIgnoreCase("si"))
			beneficiarioDTO.setCittadinoUE(true);
		else
			beneficiarioDTO.setCittadinoUE(false);
		if(residenteComuneRif.equalsIgnoreCase("si"))
			beneficiarioDTO.setResidendeComune(true);
		else
			beneficiarioDTO.setResidendeComune(false);
		
		richiestaDTO.setBeneficiarioDTO(beneficiarioDTO);
		
		
		
		richiestaDTO.setProcId(processIstanceId);
		richiestaDTO.setUserRegistryId(userRegistryId);
		richiestaDTO.setNumProtocolloEntrata(protocollo_in_entrata);
		
		richiestaDTO.setEnte(ente);
		richiestaDTO.setNome(nomeRichiedente);
		richiestaDTO.setCognome(cognomeRichiedente);
		richiestaDTO.setDatiRichiesta(richiesta);
		return richiestaDTO;
	}
	
	  private Object getStringFromJSONPayload(JSONObject payload, String propertyName) {
		  logger.info("called getStringFromJSONPayload....");
		  return payload.get(propertyName);
//		  logger.info("payload propertyName " + propertyName + ": " + jsonObjectFromPayload.toString());
//	      return jsonObjectFromPayload.get(jsonObjectFromPayload.keySet().stream().findFirst().orElse(null));
	  }
	  
	  private Boolean isObjectNotNull(Object object) {
	      return !JSONObject.NULL.equals(object) && object.toString().length() > 0;
	  }

	@Transactional( rollbackFor = Exception.class, readOnly = false )
	@Override
	public Page<BandoDTO> getBandi(BandoFilterDTO filterBando, String ente, Pageable pageable) throws Exception {
		long offset = pageable.getOffset();
		int number = pageable.getPageNumber();
		int pageSize = pageable.getPageSize();
		Sort sort = pageable.getSort();
		Optional<Order> orderOpt = sort.get().findFirst();
		
		logger.debug("offset: " + offset + ", number: " + number + ", pageSize: " + pageSize + " order: "+ orderOpt.toString());
		
		int limit = pageSize;
		
		List<BandoDTO> bandoList = ObjectMapperUtils.mapAll(
				contributoRepo.getBandi(
						filterBando, ente,offset, (long) limit, 
				orderOpt.isPresent() ? orderOpt.get().getProperty() : "", 
						orderOpt.isPresent() ? orderOpt.get().getDirection().toString() : ""), 
				BandoDTO.class);
		
		int rows = bandoList.size();
		logger.info("numero di righe ritornate : " + rows);
		Page<BandoDTO> pages = new PageImpl<BandoDTO>(bandoList, pageable,  rows);
		return pages;
	}

	@Transactional( rollbackFor = Exception.class, readOnly = false )
	@Override
	public Page<IstanzaDTO> getRichieste(IstanzaFilterDTO filterRichiesta, String ente, Pageable pageable)
			throws Exception {
		long offset = pageable.getOffset();
		int number = pageable.getPageNumber();
		int pageSize = pageable.getPageSize();
		Sort sort = pageable.getSort();
		Optional<Order> orderOpt = sort.get().findFirst();
		
		logger.debug("offset: " + offset + ", number: " + number + ", pageSize: " + pageSize + " order: "+ orderOpt.toString());
		
		int limit = pageSize;
		
		List<IstanzaDTO> richiestaList = ObjectMapperUtils.mapAll(
				contributoRepo.getRichieste(
						filterRichiesta, ente,offset, (long) limit, 
				orderOpt.isPresent() ? orderOpt.get().getProperty() : "", 
						orderOpt.isPresent() ? orderOpt.get().getDirection().toString() : ""), 
				IstanzaDTO.class);
		
		int rows = richiestaList.size();
		logger.info("numero di righe ritornate : " + rows);
		Page<IstanzaDTO> pages = new PageImpl<IstanzaDTO>(richiestaList, pageable,  rows);
		return pages;
	}

	@Transactional( rollbackFor = Exception.class, readOnly = false )
	@Override
	public IstanzaDTO getRichiestaById(long id, String ente) throws Exception {
		IstanzaDTO richiesta = ObjectMapperUtils.map(
				contributoRepo.getIstanzaById(
						id,ente), 
				IstanzaDTO.class);
		
		
		return richiesta;
		
		
	}
	

	@Transactional( rollbackFor = Exception.class, readOnly = false )
	@Override
	public Boolean verificaBandoDaChiudere(long id,String ente) throws ServiceException {
		Boolean result = ObjectMapperUtils.map(
				serviceRepo.verificaBandoDaChiudere(id,ente),Boolean.class);
		return result;
	}
	
	@Transactional( rollbackFor = Exception.class, readOnly = false )
	@Override
	public Boolean verificaBandoChiuso(long id,String ente) throws ServiceException {
		Boolean result = ObjectMapperUtils.map(
				serviceRepo.verificaBandoChiuso(id,ente),Boolean.class);
		return result;
	}
	
	@Transactional( rollbackFor = Exception.class, readOnly = false )
	@Override
	public int chiudiBando(long id, String ente) throws ServiceException {
		int modificaInserita = 0;
		try {
			logger.info("Update per una chiusura bando");
			modificaInserita = 
				serviceRepo.chiudiBando(
						id,ente);
		}catch (Exception e) {
            logger.error("Errore Update per una chiusura bando " + e.getMessage());
            throw new ServiceException(e);
        }
		
		return modificaInserita;
	}
	
   private JSONObject getUserMapEntry(String id, String name, String key) throws Exception {
    	
    	//clientRegistry = env.getProperty(Constants.SEGNALAZIONE_MYREGISTRY_URL);
  	  	logger.info("called clientMyRegistry "+ clientMyRegistry);
        Client client = ClientBuilder.newClient();
        logger.info("id "+ id);
        logger.info("name "+ name);
        logger.info("key "+ key);
        
        
        WebTarget target = client.target(clientMyRegistry);
        
        target = target.queryParam("id", id)
        		.queryParam("name", name)
        		.queryParam("key", key);
        logger.info("target "+ target);
        String response = "";
        JSONObject obj = null;
        try {
        	response = target.request(MediaType.APPLICATION_JSON).get(String.class);
        	obj = new JSONObject(response);
            logger.info("response "+ response);
        }catch (Exception e) {

        	//clientRegistry = env.getProperty(Constants.SEGNALAZIONE_MYOPERATORREGISTRY_URL);
  	  		logger.info("called clientMyOperatorRegistry "+ clientMyOperatorRegistry);
  	  		target = client.target(clientMyOperatorRegistry);
  	  		
  	  		target = target.queryParam("id", id)
        		.queryParam("name", name)
        		.queryParam("key", key);
  	  		logger.info("target "+ target);
  	  		try {
  	  			response = target.request(MediaType.APPLICATION_JSON).get(String.class);
  	  			logger.info("response "+ response);
  	  			obj = new JSONObject(response);
  	  		}catch (Exception e1) {
      			logger.error("Error : document not found con userRegistryId ("+ id +")");
      			throw new Exception("Error : document not found con userRegistryId ("+ id +")");
  	  		}
		}
        
        logger.info("obj "+ obj);
        return obj;
    }
	   
	private String getInstanceFormId(String tenant,String procId,String userRegistryId) throws Exception{
		
  	  	JSONObject obj = getUserMapEntry(userRegistryId, "instanceuserfiles", tenant + ":"+procId);
  	  	
  	  	JSONObject entity = obj.getJSONObject("entity");
  	  	JSONObject value = entity.getJSONObject("value");
        JSONObject instanceForm = value.getJSONObject("instanceForm");
        String instanceFormId =instanceForm.getString("id");
        
		return instanceFormId;
	}
   
	private BandoDTO getDatiPubblicazioneBando(String tenant, String instanceFormId)  throws Exception{
		JSONObject obj = getInstanceFormEntry(tenant,instanceFormId);
		JSONObject entity = obj.getJSONObject("entity");
		Object dataPubblicazioneJson = entity.get("publishedAt");
		String dataPubblicazioneString =  isObjectNotNull(dataPubblicazioneJson) ? (String) dataPubblicazioneJson : null;
		Instant instant = Instant.parse(dataPubblicazioneString); //Pass your date.

		Date dataPubblicazione = Date.from(instant);
		String nomeBando = entity.getString("name");
		String description = entity.getString("description");
		BandoDTO bando = new BandoDTO();
		bando.setCodiceIdentificativoBando(instanceFormId);
		bando.setDataInizioValidita(dataPubblicazione);
		bando.setDataInizioPresentazioneDomande(dataPubblicazione);
		bando.setDenominazione(description);
		bando.setOggetto(nomeBando);
		bando.setStato("APERTO");
		bando.setEnte(tenant);
		//Date dataPubblicazione = FORMAT.parse(dataPubblicazioneString);
		return bando;
	}

	private JSONObject getInstanceFormEntry(String tenant, String instanceFormId) throws Exception {
    	
  	  	logger.info("called clientMyIntranet "+ clientMyPa);
        Client client = ClientBuilder.newClient();
        logger.info("id "+ instanceFormId);
        logger.info("tenant "+ tenant);
        
        String url = clientMyPa + tenant+ "/instanceform/" + instanceFormId;
        logger.info("url "+ url);
        WebTarget target = client.target(url);
        
        String response = "";
        JSONObject obj = null;
        try {
        	response = target.request(MediaType.APPLICATION_JSON).get(String.class);
        	obj = new JSONObject(response);
            logger.info("response "+ response);
        }catch (Exception e) {


  			logger.error("Error : document not found con instanceFormId ("+ instanceFormId +")");
  			throw new Exception("Error : document not found con instanceFormId ("+ instanceFormId +")");

		}
        
        logger.info("obj "+ obj);
        return obj;
    }
	
	
	
    private List<AllegatoDTO> getFileCaricatiForm(String tenant,String procId,String userRegistryId,String codiceFiscaleProfilo) throws Exception{
    	
        

        List<AllegatoDTO> allegatoList = new ArrayList<>();
        
  	  	JSONObject obj = getUserMapEntry(userRegistryId, "instanceuserfiles", tenant + ":"+procId);
  	  	
  	  	JSONObject entity = obj.getJSONObject("entity");
  	  	JSONObject value = entity.getJSONObject("value");
        JSONArray jsonArray = value.getJSONArray("contents");
        for (int i = 0; i < jsonArray.length(); i++) {
        	AllegatoDTO allegato = new AllegatoDTO();
        	Object jsonFieldName =jsonArray.getJSONObject(i).getString("fieldName");
    		  Object jsonIdAllegato =jsonArray.getJSONObject(i).getString("id");
    		  Object jsonId =jsonArray.getJSONObject(i).getJSONObject("file").getString("id");
    		  Object jsonName = jsonArray.getJSONObject(i).getJSONObject("file").getString("name");
    		  Object jsonLength = jsonArray.getJSONObject(i).getJSONObject("file").getInt("length");
    		  Object jsonMimeType = jsonArray.getJSONObject(i).getJSONObject("file").getString("mimeType");

    		  logger.info("FIELDNAME: " + jsonFieldName + "*************");
    	      String fieldName =  isObjectNotNull(jsonFieldName) ? (String) jsonFieldName : null;
    	      logger.info("IDALLEGATO: " + jsonIdAllegato + "*************");
    	      String idAllegato =  isObjectNotNull(jsonIdAllegato) ? (String) jsonIdAllegato : null;
    	      
    	      logger.info("ID: " + jsonId + "*************");
    	      String id =  isObjectNotNull(jsonId) ? (String) jsonId : null;
    	      logger.info("ID: " + id + "*************");
    	      String name =  isObjectNotNull(jsonName) ? (String) jsonName : null;
    	      logger.info("NAME: " + name + "*************");
    	      long length =  isObjectNotNull(jsonLength) ? (Integer) jsonLength : null;
    	      logger.info("LENGTH: " + length + "*************");
    	      String mimeType =  isObjectNotNull(jsonMimeType) ? (String) jsonMimeType : null;
    	      logger.info("MYMETYPE: " + mimeType + "*************");
    	      
    	      allegato.setProcId(procId);
    	      allegato.setIdMyBox(id);
    	      allegato.setFieldName(fieldName);
    	      allegato.setIdAllegato(idAllegato);
    	      allegato.setNomeFile(name);
    	      allegato.setMimeType(mimeType);
    	      allegato.setLength(length);
    	      allegato.setUserId(codiceFiscaleProfilo);
    	      //allegato.setTipologia(tipoAllegato);
    	      allegatoList.add(allegato);   
        	
            
        }

        
        return allegatoList;
    }


	@Transactional( rollbackFor = Exception.class, readOnly = false )
	@Override
	public int aggiornaRichiestaMassiva(String punteggio,Double idRichiesta,Double idBando) throws ServiceException {

		int modificaInserita = 0;
		try {
			logger.info("Update di una richiesta di contributo");
					 
			modificaInserita = serviceRepo.aggiornaRichiestaMassiva(punteggio,idRichiesta.longValue(),idBando.longValue());
		}catch (Exception e) {
            logger.error("Errore Update una richiesta di contributo " + e.getMessage());
            throw new ServiceException(e);
        }
		return modificaInserita;
	}
	
	
	@Transactional( rollbackFor = Exception.class, readOnly = false )
	@Override
	public RichiestaDTO createRichiesta(RichiestaDTO richiestaDTO) throws ServiceException {
		RichiestaDTO richiestaInserita = null;
		try {
			logger.info("creazione di una richiesta");
			
			Date today = FORMAT.parse(FORMAT.format(new Date()));
			richiestaDTO.setDataInserimento(today);
			richiestaDTO.setDataModifica(today);
			richiestaInserita =  ObjectMapperUtils.map(serviceRepo.saveInsertMassivo(richiestaDTO),RichiestaDTO.class);
		}catch (Exception e) {
            logger.error("Errore creando una richiesta " + e.getMessage());
            throw new ServiceException(e);
        }
		return richiestaInserita;
	}

	@Override
	public Optional<Integer> getIdBandoByCodeIdentify(String codiceBando) throws ServiceException {
		Optional<Integer> optional=null;
		try {
			logger.info("getIdBandoByCodeIdentify");
			
			 optional=serviceRepo.getIdBandoByCodeIdentify(codiceBando);
		
		}catch (Exception e) {
            logger.error("Errore getIdBandoByCodeIdentify " + e.getMessage());
            throw new ServiceException(e);
        } 
		return optional;
	}

	@Transactional( rollbackFor = Exception.class, readOnly = false )
	@Override
	public BandoDTO insertBando(BandoDTO bandoDTO) throws ServiceException {
		BandoDTO bandoInserito = null;
		try {
			logger.info("creazione di un Bando");
			
			Date today = FORMAT.parse(FORMAT.format(new Date()));
			bandoDTO.setDataInserimento(today);
			bandoDTO.setDataModifica(today);
			bandoInserito =  ObjectMapperUtils.map(serviceRepo.insertBando(bandoDTO),BandoDTO.class);
		}catch (Exception e) {
            logger.error("Errore creando un Bando " + e.getMessage());
            throw new ServiceException(e);
        }
		return bandoInserito;
	}
	
	@Transactional( rollbackFor = Exception.class, readOnly = false )
	@Override
	public BeneficiarioDTO insertBeneficiario(BeneficiarioDTO beneficiarioDTO) throws ServiceException {
		BeneficiarioDTO beneficiarioInserito = null;
		try {
			logger.info("creazione di un Beneficiario");
			
			Date today = FORMAT.parse(FORMAT.format(new Date()));
			beneficiarioDTO.setDataInserimento(today);
			beneficiarioDTO.setDataModifica(today);
			beneficiarioInserito =  ObjectMapperUtils.map(serviceRepo.insertBeneficiario(beneficiarioDTO),BeneficiarioDTO.class);
		}catch (Exception e) {
            logger.error("Errore creando un Beneficiario " + e.getMessage());
            throw new ServiceException(e);
        }
		return beneficiarioInserito;
	}
	
	@Transactional( rollbackFor = Exception.class, readOnly = false )
	@Override
	public AuditUploadDTO insertAuditUpload(AuditUploadDTO auditUploadDTO) throws ServiceException {
		AuditUploadDTO auditUploadInserito = null;
		try {
			logger.info("creazione di un AuditUpload");
		 
			auditUploadInserito =  ObjectMapperUtils.map(serviceRepo.insertAuditUpload(auditUploadDTO),AuditUploadDTO.class);
		}catch (Exception e) {
            logger.error("Errore creando un AuditUpload " + e.getMessage());
            throw new ServiceException(e);
        }
		return auditUploadInserito;
	}






	@Override
	public Optional<Integer> verificaBeneficiario(Long idBando, String codiceFiscale) throws ServiceException {
		Optional<Integer> optional=null;
		try {
			logger.info("verificaBeneficiario");
			
			 optional=serviceRepo.verificaBeneficiario(idBando, codiceFiscale);
		
		}catch (Exception e) {
            logger.error("Errore verificaBeneficiario " + e.getMessage());
            throw new ServiceException(e);
        } 
		return optional;
	}	








}

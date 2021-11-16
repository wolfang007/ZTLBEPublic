package it.regioneveneto.myp3.gestgraduatorie.web.controller;

import static it.regioneveneto.myp3.gestgraduatorie.utils.Constants.DEFAULT_SEARCH_PAGE_SIZE;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nimbusds.jwt.SignedJWT;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.regioneveneto.myp3.gestgraduatorie.exception.AzioneVietataPerUtenteException;
import it.regioneveneto.myp3.gestgraduatorie.exception.ServiceException;
import it.regioneveneto.myp3.gestgraduatorie.exporter.GraduatorieExcelExporter;
import it.regioneveneto.myp3.gestgraduatorie.service.ContributoService;
import it.regioneveneto.myp3.gestgraduatorie.service.FileStorageService;
import it.regioneveneto.myp3.gestgraduatorie.service.ReportService;
import it.regioneveneto.myp3.gestgraduatorie.utils.Acl;
import it.regioneveneto.myp3.gestgraduatorie.utils.Constants;
import it.regioneveneto.myp3.gestgraduatorie.utils.UtilUploadMassivo;
import it.regioneveneto.myp3.gestgraduatorie.utils.Utils;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.AuditUploadDTO;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.BandoDTO;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.BandoFilterDTO;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.Bean;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.BeneficiarioDTO;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.EsitoServizio;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.IstanzaDTO;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.IstanzaFilterDTO;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.JwtDto;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.Result;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.RichiestaDTO;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.UploadInserimentoMassivo;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.UploadMassivo;

@RestController
@RequestMapping("/api/bandi")
@Api(value = "/bandi", tags = "tag_api_bandi")
public class ContributoController {

	private static final String ente_default = "C_DEMO";
	private static final String codiceFiscale_default = "mlonnn59h29z125g";
	private static final Logger logger = LoggerFactory.getLogger(ContributoController.class);
	public static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyyMMdd");

	@Value("${gestgraduatorie.report.elencoIstanzeXLS}")
	private String elencoIstanzeXLS;

	@Value("${gestgraduatorie.report.elencoIstanzeCSV}")
	private String elencoIstanzeCSV;

	@Value("${gestgraduatorie.report.elencoIstanzePDF}")
	private String elencoIstanzePDF;
	@Value("${gestgraduatorie.report.templateBasePath}")
    private String templateBasePath;
	
    @Autowired
    private ContributoService iContributoService;
    
    @Autowired
    private ReportService iReportService;
    
    @Autowired
    private FileStorageService iFileStorageService;
    
	SimpleDateFormat DATAFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
    
    /*
    @PostMapping(value = "/insertRichiesta",consumes  = MediaType.APPLICATION_JSON)
    public ResponseEntity<Result<RichiestaDTO>> insertRichiestaContributo(
			@AuthenticationPrincipal SignedJWT signedJWT,
			@ApiParam(value = "Richiesta da inserire", required = true, name = "richiesta")
			@RequestBody( required=true ) String richiesta ,
			HttpServletRequest req) 
					throws Exception{
    	RichiestaDTO richiestaInserita = null;
		EsitoServizio esitoServizio = null;
		Date today = FORMAT.parse(FORMAT.format(new Date()));
		logger.info("Start insertRichiestaContributo (" + today + ") ");
		String ente = ente_default;
		String codiceFiscale = codiceFiscale_default;

		if (signedJWT != null) {
			JwtDto jwtDto = Utils.extractJwtDto(signedJWT);

			if (!jwtDto.getAcl().getAcl().equals(Acl.OPE.getId())) {
				logger.error("Operazione consentita solo per utente Ente terzo settore");
				throw new AzioneVietataPerUtenteException("Operazione consentita solo per utente Ente terzo settore");
			}
			ente = jwtDto.getAcl().getEnte();
			codiceFiscale = jwtDto.getCf();
		} else {

			// valorizzo ente per i test
			ente = ente_default;
		}
 
		JSONObject payload = new JSONObject(richiesta);
		for (String keyStr : payload.keySet()) {
			Object keyvalue = payload.get(keyStr);
			// if (keyvalue instanceof JSONObject) {
			logger.info("key: " + keyStr + " value: " + keyvalue.toString());
			// }

		}

		try {
			richiestaInserita = iContributoService.createRichiestaContributo(richiesta, codiceFiscale, ente);
			esitoServizio = new EsitoServizio(Constants.CODICE_OK, Constants.DESCRIZIONE_OK);
			logger.info("POST eseguito con successo. Inserito un nuovo oggetto con id {}.", richiestaInserita.getId());
		} catch (ServiceException e) {
			// TODO: handle exception
			logger.error("Errore calling insertRichiestaContributo.... " + e.getMessage());
			esitoServizio = new EsitoServizio(Constants.CODICE_KO, e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new Result<RichiestaDTO>(esitoServizio, richiestaInserita));
		}
		*/
		/*
		 * if(signedJWT!=null) { JwtDto jwtDto = Utils.extractJwtDto(signedJWT);
		 * 
		 * if
		 * (!jwtDto.getAcl().getAcl().equals(Acl.OPE.getId())&&!jwtDto.getAcl().getAcl(
		 * ).equals(Acl.OPTS.getId())) { logger.
		 * error("Operazione consentita solo per utente Ente terzo settore e utente operatore assistente sociale"
		 * ); throw new
		 * AzioneVietataPerUtenteException("Operazione consentita solo per utente Ente terzo settore e utente operatore assistente sociale"
		 * ); } ente = jwtDto.getAcl().getEnte(); }else {
		 * 
		 * //valorizzo ente per i test ente = ente_default; }
		 * 
		 * try { richiestaInserita =
		 * iContributoService.createRichiestaContributo(richiesta); esitoServizio = new
		 * EsitoServizio(Constants.CODICE_OK, Constants.DESCRIZIONE_OK); logger.
		 * info("POST eseguito con successo. Inserito un nuovo oggetto con id {}.",
		 * richiestaInserita.getId()); } catch (ServiceException e) { // TODO: handle
		 * exception logger.error("Errore calling insertSegnalazione.... " +
		 * e.getMessage()); esitoServizio = new EsitoServizio(Constants.CODICE_KO,
		 * e.getMessage()); return
		 * ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) .body(new
		 * Result<RichiestaDTO>(esitoServizio, richiestaInserita)); } /* String ente =
		 * null; if(signedJWT!=null) { JwtDto jwtDto = Utils.extractJwtDto(signedJWT);
		 * 
		 * if
		 * (!jwtDto.getAcl().getAcl().equals(Acl.OPE.getId())&&!jwtDto.getAcl().getAcl(
		 * ).equals(Acl.OPTS.getId())) { logger.
		 * error("Operazione consentita solo per utente Ente terzo settore e utente operatore assistente sociale"
		 * ); throw new
		 * AzioneVie22tataPerUtenteException("Operazione consentita solo per utente Ente terzo settore e utente operatore assistente sociale"
		 * ); } ente = jwtDto.getAcl().getEnte(); }else {
		 * 
		 * //valorizzo ente per i test ente = ente_default; }
		 * 
		 * try { segnalazioneInserita =
		 * iSegnalazioniService.createSegnalazione(segnalazione,ente); esitoServizio =
		 * new EsitoServizio(Constants.CODICE_OK,Constants.DESCRIZIONE_OK); logger.
		 * info("POST segnalazione eseguito con successo. Inserito un nuovo oggetto con id {}."
		 * , segnalazioneInserita.getIdSegnalazione() ); }catch (ServiceException e) {
		 * // TODO: handle exception
		 * logger.error("Errore calling insertSegnalazione.... " + e.getMessage());
		 * esitoServizio = new EsitoServizio(Constants.CODICE_KO,e.getMessage()); return
		 * ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new
		 * Result<SegnalazioneDTO>(esitoServizio, segnalazioneInserita) ); }
		 */
/*
		return ResponseEntity.ok(new Result<RichiestaDTO>(esitoServizio, richiestaInserita));

	}
*/
	/**
	 * Metodo POST per ricercare i bandi
	 * 
	 * @return {@link BandoDTO}
	 * @throws Exception
	 */
	@ApiOperation(value = "Recupera la lista dei bandi", response = ResponseEntity.class, consumes = "application/json", produces = "application/json", httpMethod = "POST")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved liste cittadini"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	@PostMapping("/listBandi")
	public ResponseEntity<Result<Page<BandoDTO>>> getBandiPaginati(
			@ApiParam(value = "Filtro con cui cercare la lista di bandi", name = "filterBando") @RequestBody BandoFilterDTO filterBando,
			@AuthenticationPrincipal SignedJWT signedJWT,
			@ApiParam(value = "page", required = false, name = "page") @RequestParam(value = "page", required = false, defaultValue = "0") int page,
			@ApiParam(value = "size", required = false, defaultValue = ""
					+ DEFAULT_SEARCH_PAGE_SIZE, name = "size") @RequestParam(value = "size", required = false, defaultValue = ""
							+ DEFAULT_SEARCH_PAGE_SIZE) int size,
			@ApiParam(value = "sort", required = false, defaultValue = "id,ASC", name = "sort") @RequestParam(value = "sort", required = false, defaultValue = "id,ASC") String sort,
			Pageable pageable) throws Exception {

		logger.info("getBandiPaginati --> POST Lista filtrata e paginata");

		String ente = ente_default;

		if (signedJWT != null) {
			JwtDto jwtDto = Utils.extractJwtDto(signedJWT);

			if (!jwtDto.getAcl().getAcl().equals(Acl.OPE.getId())) {
				logger.error("Operazione consentita solo per utente Ente terzo settore");
				throw new AzioneVietataPerUtenteException("Operazione consentita solo per utente operatore consultazione graduatorie");
			}
			ente = jwtDto.getAcl().getEnte();
		} else {

			// valorizzo ente per i test
			ente = ente_default;
		}

		Page<BandoDTO> bandoList = null;
		EsitoServizio esitoServizio = null;
		try {
			bandoList = iContributoService.getBandi(filterBando, ente, pageable);

			logger.info("getBandiPaginati --> POST Lista filtrata e paginata. Total: " + bandoList.toString());
			esitoServizio = new EsitoServizio(Constants.CODICE_OK, Constants.DESCRIZIONE_OK);

		} catch (Exception e) {
			logger.error("Errore calling getBandiPaginati.... " + e.getMessage());
			esitoServizio = new EsitoServizio(Constants.CODICE_KO, e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new Result<Page<BandoDTO>>(esitoServizio, bandoList));
		}
		return ResponseEntity.ok(new Result<Page<BandoDTO>>(esitoServizio, bandoList));
	}

	/**
	 * Metodo POST per ricercare le graduatorie
	 * 
	 * @return {@link RichiestaDTO}
	 * @throws Exception
	 */
	@ApiOperation(value = "Recupera la lista delle Richieste", response = ResponseEntity.class, consumes = "application/json", produces = "application/json", httpMethod = "POST")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved lista richieste"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	@PostMapping("/listRichieste")
	public ResponseEntity<Result<Page<IstanzaDTO>>> getRichiestePaginate(
			@ApiParam(value = "Filtro con cui cercare la lista di richieste", name = "filterRichiesta") @RequestBody IstanzaFilterDTO filterRichiesta,
			@AuthenticationPrincipal SignedJWT signedJWT,
			@ApiParam(value = "page", required = false, name = "page") @RequestParam(value = "page", required = false, defaultValue = "0") int page,
			@ApiParam(value = "size", required = false, defaultValue = ""
					+ DEFAULT_SEARCH_PAGE_SIZE, name = "size") @RequestParam(value = "size", required = false, defaultValue = ""
							+ DEFAULT_SEARCH_PAGE_SIZE) int size,
			@ApiParam(value = "sort", required = false, defaultValue = "id,ASC", name = "sort") @RequestParam(value = "sort", required = false, defaultValue = "id,ASC") String sort,
			Pageable pageable) throws Exception {

		logger.info("getRichiestePaginate --> POST Lista filtrata e paginata");

		String ente = ente_default;

		if (signedJWT != null) {
			JwtDto jwtDto = Utils.extractJwtDto(signedJWT);

			if (!jwtDto.getAcl().getAcl().equals(Acl.OPE.getId())) {
				logger.error("Operazione consentita solo per utente Ente terzo settore");
				throw new AzioneVietataPerUtenteException("Operazione consentita solo per utente operatore consultazione graduatorie");
			}
			ente = jwtDto.getAcl().getEnte();
		} else {

			// valorizzo ente per i test
			ente = ente_default;
		}

		Page<IstanzaDTO> richiestaList = null;
		EsitoServizio esitoServizio = null;
		try {
			richiestaList = iContributoService.getRichieste(filterRichiesta, ente, pageable);

			logger.info("getRichiestePaginate --> POST Lista filtrata e paginata. Total: " + richiestaList.toString());
			esitoServizio = new EsitoServizio(Constants.CODICE_OK, Constants.DESCRIZIONE_OK);

		} catch (Exception e) {
			logger.error("Errore calling getRichiestePaginate.... " + e.getMessage());
			esitoServizio = new EsitoServizio(Constants.CODICE_KO, e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new Result<Page<IstanzaDTO>>(esitoServizio, richiestaList));
		}
		return ResponseEntity.ok(new Result<Page<IstanzaDTO>>(esitoServizio, richiestaList));
	}

	@GetMapping("/richiestaById")
	public ResponseEntity<Result<IstanzaDTO>> getRichiestaById(@AuthenticationPrincipal SignedJWT signedJWT,
			@ApiParam(value = "Filtro con cui cercare la richiesta", name = "id", required = true) @RequestParam(value = "id", required = true) long id)
			throws Exception {
		IstanzaDTO richiesta = null;
		EsitoServizio esitoServizio = null;
		logger.info("Start getRichiestaById");
		String ente = null;
		if (signedJWT != null) {
			JwtDto jwtDto = Utils.extractJwtDto(signedJWT);

			if (!jwtDto.getAcl().getAcl().equals(Acl.OPE.getId())) {
				logger.error("Operazione consentita solo per utente Ente terzo settore");
				throw new AzioneVietataPerUtenteException("Operazione consentita solo per utente operatore consultazione graduatorie");
			}
			ente = jwtDto.getAcl().getEnte();
		} else {

			// valorizzo ente per i test
			ente = ente_default;
		}

		try {
			richiesta = iContributoService.getRichiestaById(id, ente);

			esitoServizio = new EsitoServizio(Constants.CODICE_OK, Constants.DESCRIZIONE_OK);
			logger.info("GET AnagraficaCittadino eseguita con successo. Recuperata istanza con id {}.",
					richiesta.getId());
		} catch (Exception e) {

			logger.error("Errore calling getRichiestaById.... " + e.getMessage());
			esitoServizio = new EsitoServizio(Constants.CODICE_KO, e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new Result<IstanzaDTO>(esitoServizio, richiesta));
		}

		return ResponseEntity.ok(new Result<IstanzaDTO>(esitoServizio, richiesta));

	}
    /**
     * <code>checkChiusuraBando</code> verifica se il bando al quale si sta accedendo alla graduatoria
     * possiede tutti i punteggi valorizzati e ritorna true nel caso che il bando si possa chiudere, false altrimenti
     * 
     * @param signedJWT
     * @param id
     * @return
     * @throws Exception
     */
	
	@GetMapping("/checkChiusuraBando")
	public ResponseEntity<Result<Boolean>> checkChiusuraBando(
			@AuthenticationPrincipal SignedJWT signedJWT,
			@ApiParam(value = "Filtro con cui cercare il bando", name = "id", required = true) 
			@RequestParam(value = "id", required = true) long id)
					throws Exception {
		EsitoServizio esitoServizio = null;
		Boolean checkReturn = false;
		logger.info("Start checkChiusuraBando");
		String ente = null;
		if (signedJWT != null) {
			JwtDto jwtDto = Utils.extractJwtDto(signedJWT);

			if (!jwtDto.getAcl().getAcl().equals(Acl.OPE.getId())) {
				logger.error("Operazione consentita solo per utente operatore consultazione graduatorie");
				throw new AzioneVietataPerUtenteException("Operazione consentita solo per utente operatore consultazione graduatorie");
			}
			ente = jwtDto.getAcl().getEnte();
		} else {

			// valorizzo ente per i test
			ente = ente_default;
		}

		try {
			
			checkReturn = iContributoService.verificaBandoDaChiudere(id,ente);
			esitoServizio = new EsitoServizio(Constants.CODICE_OK,Constants.DESCRIZIONE_OK);
		} catch (Exception e) {

			logger.error("Errore calling checkChiusuraBando.... " + e.getMessage());
			esitoServizio = new EsitoServizio(Constants.CODICE_KO, e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new Result<Boolean>(esitoServizio, false));
		}

		return ResponseEntity.ok(new Result<Boolean>(esitoServizio, checkReturn));

	}
    /**
     * <code>isBandoChiuso</code> verifica se il bando al quale si sta accedendo alla graduatoria
     * e' chiuso (return true) oppure aperto (return false)
     * 
     * @param signedJWT
     * @param id
     * @return
     * @throws Exception
     */
	@GetMapping("/isBandoChiuso")
	public ResponseEntity<Result<Boolean>> isBandoChiuso(
			@AuthenticationPrincipal SignedJWT signedJWT,
			@ApiParam(value = "Filtro con cui cercare il bando", name = "id", required = true) 
			@RequestParam(value = "id", required = true) long id)
					throws Exception {
		EsitoServizio esitoServizio = null;
		Boolean checkReturn = false;
		logger.info("Start isBandoChiuso");
		String ente = null;
		if (signedJWT != null) {
			JwtDto jwtDto = Utils.extractJwtDto(signedJWT);

			if (!jwtDto.getAcl().getAcl().equals(Acl.OPE.getId())) {
				logger.error("Operazione consentita solo per utente operatore consultazione graduatorie");
				throw new AzioneVietataPerUtenteException("Operazione consentita solo per utente operatore consultazione graduatorie");
			}
			ente = jwtDto.getAcl().getEnte();
		} else {

			// valorizzo ente per i test
			ente = ente_default;
		}

		try {
			
			checkReturn = iContributoService.verificaBandoChiuso(id,ente);
			esitoServizio = new EsitoServizio(Constants.CODICE_OK,Constants.DESCRIZIONE_OK);
		} catch (Exception e) {

			logger.error("Errore calling isBandoChiuso.... " + e.getMessage());
			esitoServizio = new EsitoServizio(Constants.CODICE_KO, e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new Result<Boolean>(esitoServizio, false));
		}

		return ResponseEntity.ok(new Result<Boolean>(esitoServizio, checkReturn));

	}
	
	
	@GetMapping("/chiusuraBando")
	public ResponseEntity<Result<Integer>> chiusuraBando(
			@AuthenticationPrincipal SignedJWT signedJWT,
			@ApiParam(value = "Filtro con cui cercare il bando", name = "id", required = true) 
			@RequestParam(value = "id", required = true) long id)
					throws Exception {
		EsitoServizio esitoServizio = null;
		int result = 0;
		logger.info("Start checkChiusuraBando");
		String ente = null;
		if (signedJWT != null) {
			JwtDto jwtDto = Utils.extractJwtDto(signedJWT);

			if (!jwtDto.getAcl().getAcl().equals(Acl.OPE.getId())) {
				logger.error("Operazione consentita solo per utente operatore consultazione graduatorie");
				throw new AzioneVietataPerUtenteException("Operazione consentita solo per operatore consultazione graduatorie");
			}
			ente = jwtDto.getAcl().getEnte();
		} else {

			// valorizzo ente per i test
			ente = ente_default;
		}

		try {
			if(iContributoService.verificaBandoDaChiudere(id,ente)) {
				result = iContributoService.chiudiBando(id,ente);
				esitoServizio = new EsitoServizio(Constants.CODICE_OK,Constants.DESCRIZIONE_OK);
			}else {
				esitoServizio = new EsitoServizio(Constants.CODICE_KO, "Imoossibile chiudere il bando. "
						+ "Risultano delle richieste senza il punteggio assegnato.");
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body(new Result<Integer>(esitoServizio, result));
			}
				
		} catch (Exception e) {

			logger.error("Errore calling checkChiusuraBando.... " + e.getMessage());
			esitoServizio = new EsitoServizio(Constants.CODICE_KO, e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new Result<Integer>(esitoServizio, result));
		}

		return ResponseEntity.ok(new Result<Integer>(esitoServizio, result));

	}
	
	// controller incaricato per la logica di esportazione dell'elenco delle istanze
	// in un file xls
	@PostMapping("/exportRicercaManualeXls")
	public ResponseEntity<Resource> exportManualeXLS(@AuthenticationPrincipal SignedJWT signedJWT,
			@ApiParam(value = "Filtro con cui cercare la lista di modifiche", name = "filterConsultazione") @RequestBody IstanzaFilterDTO filterRichiesta)
			throws Exception {

		logger.info("exportRicercaManualeXls --> GET Lista filtrata By Ricerca Manuale");
		try {

			String ente = null;
			if (signedJWT != null) {
				JwtDto jwtDto = Utils.extractJwtDto(signedJWT);

				if (!jwtDto.getAcl().getAcl().equals(Acl.OPE.getId())) {
					logger.error("Operazione consentita solo per utente Ente terzo settore");
					throw new AzioneVietataPerUtenteException(
							"Operazione consentita solo per utente Ente terzo settore");
				}
				ente = jwtDto.getAcl().getEnte();
			} else {

				// valorizzo ente per i test
				ente = ente_default;
			}

			// byte[] file2Download = iReportService.getElencoIstanze(filterRichiesta, ente,
			// "XLS");

			Collection<Bean> richiestaList = iReportService.getElencoDtoIstanze(filterRichiesta, ente);

			GraduatorieExcelExporter excelExporter = new GraduatorieExcelExporter(richiestaList);

			byte[] file2Download = excelExporter.export();

			// Path path = Paths.get(file2Download.getAbsolutePath());
			ByteArrayResource resource = new ByteArrayResource(file2Download);
			String filename = "attachment; filename=\"" + elencoIstanzeXLS + "\"";
			HttpHeaders headers = new HttpHeaders();
			headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
			headers.add("Pragma", "no-cache");
			headers.add("Expires", "0");
			headers.add(HttpHeaders.CONTENT_DISPOSITION, filename);

			return ResponseEntity.ok().headers(headers).contentLength(file2Download.length)
					.contentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM).body(resource);

		} catch (Exception e) {
			logger.error("Errore calling exportCSV.... " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// controller incaricato per la logica di esportazione dell'elenco delle istanze
	// in un file csv
	@PostMapping("/exportRicercaManualeCsv")
	public ResponseEntity<Resource> exportManualeCSV(@AuthenticationPrincipal SignedJWT signedJWT,
			@ApiParam(value = "Filtro con cui cercare la lista di modifiche", name = "filterRichiesta") @RequestBody IstanzaFilterDTO filterRichiesta)
			throws Exception {

		logger.info("exportRicercaManualeCsv --> GET Lista filtrata By Ricerca Manuale");

		try {

			String ente = null;
			if (signedJWT != null) {
				JwtDto jwtDto = Utils.extractJwtDto(signedJWT);

				if (!jwtDto.getAcl().getAcl().equals(Acl.OPE.getId())) {
					logger.error("Operazione consentita solo per utente Ente terzo settore");
					throw new AzioneVietataPerUtenteException(
							"Operazione consentita solo per utente Ente terzo settore");
				}
				ente = jwtDto.getAcl().getEnte();
			} else {

				// valorizzo ente per i test
				ente = ente_default;
			}

			byte[] file2Download = iReportService.getElencoIstanze(filterRichiesta, ente, "CSV");
			String filename = "attachment; filename=\"" + elencoIstanzeCSV + "\"";
			HttpHeaders headers = new HttpHeaders();
			headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
			headers.add("Pragma", "no-cache");
			headers.add("Expires", "0");
			headers.add(HttpHeaders.CONTENT_DISPOSITION, filename);

			// Path path = Paths.get(file2Download.getAbsolutePath());
			ByteArrayResource resource = new ByteArrayResource(file2Download);

			return ResponseEntity.ok().headers(headers).contentLength(file2Download.length)
					.contentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM).body(resource);
		} catch (Exception e) {
			logger.error("Errore calling exportCSV.... " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}

	// controller incaricato per la logica di esportazione dell'elenco delle istanze
	// in un file pdf
	@PostMapping("/exportRicercaManualePdf")
	public ResponseEntity<Resource> exportManualePDF(@AuthenticationPrincipal SignedJWT signedJWT,
			@ApiParam(value = "Filtro con cui cercare la lista di modifiche", name = "filterRichiesta") @RequestBody IstanzaFilterDTO filterRichiesta)
			throws Exception {

		logger.info("exportRicercaManualePdf --> GET Lista filtrata By Ricerca Manuale");

		try {

			String ente = null;
			if (signedJWT != null) {
				JwtDto jwtDto = Utils.extractJwtDto(signedJWT);

				if (!jwtDto.getAcl().getAcl().equals(Acl.OPE.getId())) {
					logger.error("Operazione consentita solo per utente Ente terzo settore");
					throw new AzioneVietataPerUtenteException(
							"Operazione consentita solo per utente Ente terzo settore");
				}
				ente = jwtDto.getAcl().getEnte();
			} else {

				// valorizzo ente per i test
				ente = ente_default;
			}

			byte[] file2Download = iReportService.getElencoIstanze(filterRichiesta, ente, "PDF");
			String filename = "attachment; filename=\"" + elencoIstanzePDF + "\"";
			HttpHeaders headers = new HttpHeaders();
			headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
			headers.add("Pragma", "no-cache");
			headers.add("Expires", "0");
			headers.add(HttpHeaders.CONTENT_DISPOSITION, filename);

			// Path path = Paths.get(file2Download.getAbsolutePath());
			ByteArrayResource resource = new ByteArrayResource(file2Download);

			return ResponseEntity.ok().headers(headers).contentLength(file2Download.length)
					.contentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM).body(resource);
		} catch (Exception e) {
			logger.error("Errore calling exportPDF.... " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}

	// @SuppressWarnings("unchecked")
	@GetMapping("/downloadFile")
	public ResponseEntity<Resource> downloadFile(@AuthenticationPrincipal SignedJWT signedJWT,
			@RequestParam Long id_all, HttpServletRequest request) {

		ResponseEntity<Resource> response = null;
		try {
			String ente = null;
			if (signedJWT != null) {
				JwtDto jwtDto = Utils.extractJwtDto(signedJWT);

				if (!jwtDto.getAcl().getAcl().equals(Acl.OPE.getId())) {
					logger.error("Operazione consentita solo per utente Ente terzo settore");
					throw new AzioneVietataPerUtenteException(
							"Operazione consentita solo per utente Ente terzo settore");
				}
				ente = jwtDto.getAcl().getEnte();
			} else {

				// valorizzo ente per i test
				ente = ente_default;
			}

			response = iFileStorageService.loadFileAsResource(id_all, ente, request);
		} catch (Exception e) {
			logger.error("Errore calling exportPDF.... " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return response;
	}
	
	@PostMapping(value = "/uploadUpdateMassivo")
	public ResponseEntity<Result<List<UploadMassivo>>> uploadUpdateMassivo(@AuthenticationPrincipal SignedJWT signedJWT,
			@ApiParam(value = "Upload Update Massivo", required = true, name = "file") @RequestParam("file") MultipartFile file)
			throws Exception {

		EsitoServizio esitoServizio = null;
		List<UploadMassivo> istancesList = new ArrayList<>();
		Date today = FORMAT.parse(FORMAT.format(new Date()));
		logger.info("Start uploadUpdateMassivo (" + today + ") ");
		AuditUploadDTO auditUploadDTO = new AuditUploadDTO();
		String esitoOperazione="";
	
		String nomeOperatore = "NOME-TEST";
		String cognomeOperatore = "COGNOME-TEST";
		String ente = ente_default;

		String codiceFiscaleOperatore = "CFTESTEST";


		if (signedJWT != null) {
			JwtDto jwtDto = Utils.extractJwtDto(signedJWT);

			if (!jwtDto.getAcl().getAcl().equals(Acl.OPE.getId())) {
				logger.error("Operazione consentita solo per utente Ente terzo settore");
				throw new AzioneVietataPerUtenteException("Operazione consentita solo per utente Ente terzo settore");
			}
			ente = jwtDto.getAcl().getEnte();
			codiceFiscaleOperatore = jwtDto.getCf();
			nomeOperatore = jwtDto.getAcl().getNome();
			cognomeOperatore = jwtDto.getAcl().getCognome();
		} else {

			// valorizzo ente per i test
			ente = ente_default;
		}
		
		auditUploadDTO.setDataCaricamento(DATAFORMAT.parse(DATAFORMAT.format(new Date())));
		auditUploadDTO.setNomeUtente(codiceFiscaleOperatore);
		auditUploadDTO.setEsito("KO");
		
		//con l'api storeFile inserisco il file appena caricato dall'utente sul repository mybox 
		//e ricevo come valore di ritorno l'id del file appenca caricato sul repo (idMyBox)
		String idMyBox = iFileStorageService.storeFile(file, ente);
		auditUploadDTO.setIdFile(idMyBox);

		

		InputStream input = file.getInputStream();
		//POIFSFileSystem fs = new POIFSFileSystem(input);
	    //Workbook wb = getWorkbook(input, file.getOriginalFilename());	  
		//HSSFWorkbook wb = new HSSFWorkbook(fs);
		//HSSFSheet sheet = wb.getSheetAt(0);
		Workbook wb = WorkbookFactory.create(input);
		Sheet sheet = wb.getSheetAt(0);
		Iterator<Row> rows = sheet.rowIterator();
		Row row = null;

		UploadMassivo uMassivo = null;

		while (rows.hasNext()) {
			try {
				row = rows.next();

				if (row.getRowNum() != 0) {

					Cell idBando_ = row.getCell(0);
					Cell idRichiesta_ = row.getCell(1);
					Cell codiceFiscale_ = row.getCell(6);
					Cell punteggio_ = row.getCell(7);

					uMassivo = new UploadMassivo();
					uMassivo.setIdRichiesta(UtilUploadMassivo.checkValueNumber(idRichiesta_));
					uMassivo.setIdBando(UtilUploadMassivo.checkValueNumber(idBando_));
					uMassivo.setPunteggio(UtilUploadMassivo.checkValueString(punteggio_));
					uMassivo.setCodFiscale(UtilUploadMassivo.checkValueString(codiceFiscale_));
					uMassivo.setOperatoreModifica(codiceFiscaleOperatore);
					istancesList.add(uMassivo);

				}
			} catch (Exception e) {				
				
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body(new Result<List<UploadMassivo>>(esitoServizio, istancesList));
			}
		}
		esitoOperazione = "OK";
		try {
			updateMassivo(istancesList);
			esitoOperazione = "ok";
			auditUploadDTO.setEsito(esitoOperazione);
			 
			iContributoService.insertAuditUpload(auditUploadDTO);
			esitoServizio = new EsitoServizio(Constants.CODICE_OK, Constants.DESCRIZIONE_OK);
			logger.info("POST Upload eseguito con successo.");
		} catch (ServiceException e) {
			esitoOperazione = "ok";
			auditUploadDTO.setEsito(esitoOperazione);
			 
			iContributoService.insertAuditUpload(auditUploadDTO);
			logger.error("Errore calling uploadUpdateMassivo.... " + e.getMessage());
			esitoServizio = new EsitoServizio(Constants.CODICE_KO, e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new Result<List<UploadMassivo>>(esitoServizio, istancesList));
		}

		return ResponseEntity.ok(new Result<List<UploadMassivo>>(esitoServizio, istancesList));

	}

	private String updateMassivo(List<UploadMassivo> istances) {

		int result = 0;

		for (UploadMassivo u : istances) {
			if (u.getIdBando() != 0 && u.getIdRichiesta() != 0 && !u.getPunteggio().isEmpty()) {
				logger.info("Richiesta ID {} Bando ID {}  Punteggio {} .", u.getIdRichiesta(), u.getIdBando(),
						u.getPunteggio());

				result = iContributoService.aggiornaRichiestaMassiva(u.getPunteggio(), u.getIdRichiesta(),
						u.getIdBando());
				u.setEsitoOperazione(result);

				if (result < 1) {
					logger.info("Errore non e stato possibile eseguire la modifica per la Richiesta ID {} .",
							u.getIdRichiesta());
				}
			}
		}
		return "";
	}

	@PostMapping(value = "/uploadInsertMassivo")
	public ResponseEntity<Result<List<UploadInserimentoMassivo>>> uploadInsertMassivo(
			@AuthenticationPrincipal SignedJWT signedJWT,
			@ApiParam(value = "Upload Insert Massivo", required = true, name = "file") @RequestParam("file") MultipartFile file)
			throws Exception {

		EsitoServizio esitoServizio = null;
		List<UploadInserimentoMassivo> istancesList = new ArrayList<>();
		Date today = FORMAT.parse(FORMAT.format(new Date()));

	

		logger.info("Start uploadInsertMassivo (" + today + ") ");
		AuditUploadDTO auditUploadDTO = new AuditUploadDTO();

		String codiceFiscaleOperatore = "CODFISCAlE-TEST";
		String nomeOperatore = "NOME-TEST";
		String cognomeOperatore = "COGNOME-TEST";
		String ente = ente_default;

		if (signedJWT != null) {
			JwtDto jwtDto = Utils.extractJwtDto(signedJWT);
			if (!jwtDto.getAcl().getAcl().equals(Acl.OPE.getId())) {
				logger.error("Operazione non consentita");
				throw new AzioneVietataPerUtenteException("Operazione non consentita");
			}
			ente = jwtDto.getAcl().getEnte();
			codiceFiscaleOperatore = jwtDto.getCf();
			nomeOperatore = jwtDto.getNome();
			cognomeOperatore = jwtDto.getCognome();
		} else {
			ente = ente_default;
		}

		auditUploadDTO.setDataCaricamento(DATAFORMAT.parse(DATAFORMAT.format(new Date())));
		auditUploadDTO.setNomeUtente(codiceFiscaleOperatore);
		auditUploadDTO.setEsito("KO");
		
		//con l'api storeFile inserisco il file appena caricato dall'utente sul repository mybox 
		//e ricevo come valore di ritorno l'id del file appenca caricato sul repo (idMyBox)
		String idMyBox = iFileStorageService.storeFile(file, ente);
		auditUploadDTO.setIdFile(idMyBox);

		InputStream input = file.getInputStream();
		//POIFSFileSystem fs = new POIFSFileSystem(input);		  
		//HSSFWorkbook wb = new HSSFWorkbook(fs);
		//HSSFSheet sheet = wb.getSheetAt(0);
		Workbook wb = WorkbookFactory.create(input);
		Sheet sheet = wb.getSheetAt(0);
		Iterator<Row> rows = sheet.rowIterator();
		Row row = null;
		UploadInserimentoMassivo uMassivo = null;
		String esitoOperazione = "KO";

		while (rows.hasNext()) {
			try {
				row = rows.next();

				if (row.getRowNum() != 0) {

					Cell codiceBando_ = row.getCell(0);
					Cell oggetto_ = row.getCell(1);
					Cell nome_ = row.getCell(2);
					Cell cognome_ = row.getCell(3);
					Cell codiceFiscale_ = row.getCell(4);
					Cell telefono_ = row.getCell(5);
					Cell cellulare_ = row.getCell(6);
					Cell email_ = row.getCell(7);
					Cell residenteInComune_ = row.getCell(8);
					Cell cittadinoUE_ = row.getCell(9);

					uMassivo = new UploadInserimentoMassivo();
					uMassivo.setCodiceBando(UtilUploadMassivo.checkValueString(codiceBando_));
					uMassivo.setOggetto(UtilUploadMassivo.checkValueString(oggetto_));
					uMassivo.setNome(UtilUploadMassivo.checkValueString(nome_));
					uMassivo.setCognome(UtilUploadMassivo.checkValueString(cognome_));
					uMassivo.setCodFiscale(UtilUploadMassivo.checkValueString(codiceFiscale_));
					uMassivo.setTelefono(UtilUploadMassivo.checkValueString(telefono_));
					uMassivo.setCellulare(UtilUploadMassivo.checkValueString(cellulare_));
					uMassivo.setEmail(UtilUploadMassivo.checkValueString(email_));
					uMassivo.setResidenteInComune(UtilUploadMassivo.checkValueBoolean(residenteInComune_));
					uMassivo.setCittadinoUE(UtilUploadMassivo.checkValueBoolean(cittadinoUE_));
					uMassivo.setCfOperatore(codiceFiscaleOperatore);
					uMassivo.setNomeOperatore(nomeOperatore);
					uMassivo.setCognomeOperatore(cognomeOperatore);
					istancesList.add(uMassivo);
					esitoOperazione = "OK";
				}
			} catch (Exception e) {
				esitoOperazione = "KO";
			}
		}
		try {
			insertMassivo(istancesList, ente);
			auditUploadDTO.setEsito(esitoOperazione);
			 
			iContributoService.insertAuditUpload(auditUploadDTO);
		 
			esitoServizio = new EsitoServizio(Constants.CODICE_OK, Constants.DESCRIZIONE_OK);
			logger.info("POST Upload eseguito con successo.");
		} catch (ServiceException e) {
			esitoOperazione = "KO";
			logger.error("Errore calling uploadInsertMassivo.... " + e.getMessage());
			esitoServizio = new EsitoServizio(Constants.CODICE_KO, e.getMessage());
			auditUploadDTO.setEsito(esitoOperazione);
			iContributoService.insertAuditUpload(auditUploadDTO);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new Result<List<UploadInserimentoMassivo>>(esitoServizio, istancesList));
		}

		return ResponseEntity.ok(new Result<List<UploadInserimentoMassivo>>(esitoServizio, istancesList));

	}

	private String insertMassivo(List<UploadInserimentoMassivo> istances, String ente) {
		 
		RichiestaDTO result = null;
		RichiestaDTO richiestaDTO = null;
		String codiceBando = "";
		Long id_Bando = 0L;
		for (UploadInserimentoMassivo u : istances) {
			if (!u.getCodiceBando().isEmpty()) {
				logger.info("Codice Bando {}   .", u.getCodiceBando());
				richiestaDTO = new RichiestaDTO();
				richiestaDTO.setNome(u.getNomeOperatore());
				richiestaDTO.setCognome(u.getCognomeOperatore());
				richiestaDTO.setCfRichiedente(u.getCfOperatore());
				richiestaDTO.setDatiRichiesta("{\"codice_fiscale_richiedente\":\"" + u.getCodFiscale() + "\"}");
				richiestaDTO.setEnte(ente);

				if (!codiceBando.equals(u.getCodiceBando()) && !u.getCodiceBando().isEmpty()) {

					Optional<Integer> optID = iContributoService.getIdBandoByCodeIdentify(u.getCodiceBando());
					if (optID != null && optID.isPresent() && optID.get() > 0) {
						richiestaDTO.setIdBando(optID.get().longValue());
						codiceBando = u.getCodiceBando();
						id_Bando = richiestaDTO.getIdBando();
					} else {
						BandoDTO bandoDTO = new BandoDTO();
						bandoDTO.setCodiceIdentificativoBando(u.getCodiceBando());
						bandoDTO.setOggetto(u.getOggetto());
						bandoDTO.setEnte(ente);
						BandoDTO dto = iContributoService.insertBando(bandoDTO);
						codiceBando = dto.getCodiceIdentificativoBando();
						richiestaDTO.setIdBando(dto.getId());
						id_Bando = richiestaDTO.getIdBando();
					}
				}

				if (!codiceBando.isEmpty()) {
					richiestaDTO.setIdBando(id_Bando);
					Optional<Integer> count =iContributoService.verificaBeneficiario(id_Bando, u.getCodFiscale());
					boolean isPresent=false;
					if(count!=null && count.isPresent() && count.get().intValue()<1)
						isPresent=false;
					if(count!=null && count.isPresent() && count.get().intValue()>0)
						isPresent=true;
				
					if(!isPresent) {
					result = iContributoService.createRichiesta(richiestaDTO);
					u.setEsitoOperazione((result != null && result.getId() != 0) ? "OK" : "KO");
					if (result != null) {
						BeneficiarioDTO beneficiarioDTO = new BeneficiarioDTO();
						beneficiarioDTO.setCodiceFiscale(u.getCodFiscale());
						beneficiarioDTO.setCognome(u.getCognome());
						beneficiarioDTO.setNome(u.getNome());
						beneficiarioDTO.setIdBando(result.getIdBando());
						beneficiarioDTO.setIdRichiesta(result.getId());
						beneficiarioDTO.setCellulare(u.getCellulare());
						beneficiarioDTO.setTelefono(u.getTelefono());
						beneficiarioDTO.setEmail(u.getEmail());
						beneficiarioDTO.setResidendeComune(u.getResidenteInComune());
						beneficiarioDTO.setCittadinoUE(u.getCittadinoUE());
						beneficiarioDTO.setEnte(ente);
						BeneficiarioDTO dto = iContributoService.insertBeneficiario(beneficiarioDTO);
						if (dto != null && dto.getId() > 0) {
							u.setEsitoOperazione("OK");
						 
						} else {
							u.setEsitoOperazione("KO");
						}
					 
					}
					} else { 
							u.setEsitoOperazione("UT-PRESENTE");
						 
					}
				}

				if (result != null && result.getId() < 1) {
					logger.info("Errore non e stato possibile la Richiesta  pei il codice bando {} .",
							u.getCodiceBando());
				}
			}
		}
		return "";
	}

	@GetMapping("/exportTemplateInsertXLS")
	public ResponseEntity<Resource> exportTemplateInsertXLS() throws Exception {

		logger.info("exportTemplateInsertXLS");
		try {
 
			FileInputStream stream = new FileInputStream(
					templateBasePath + File.separator + "Template_inserimento_massivo_graudatorie.xls");

			POIFSFileSystem templatexls = new POIFSFileSystem(stream);
			HSSFWorkbook template = new HSSFWorkbook(templatexls);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			template.write(baos);
			byte[] xls = baos.toByteArray();
			template.close();

			ByteArrayResource resource = new ByteArrayResource(xls);
			String filename = "attachment; filename=\"Template_inserimento_massivo_graudatorie.xls\"";
			HttpHeaders headers = new HttpHeaders();
			headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
			headers.add("Pragma", "no-cache");
			headers.add("Expires", "0");
			headers.add(HttpHeaders.CONTENT_DISPOSITION, filename);

			return ResponseEntity.ok().headers(headers).contentLength(xls.length)
					.contentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM).body(resource);

		} catch (Exception e) {
			logger.error("Errore calling exportCSV.... " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}	


}

package it.regioneveneto.myp3.gestgraduatorie.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiParam;
import it.regioneveneto.myp3.gestgraduatorie.exception.ServiceException;
import it.regioneveneto.myp3.gestgraduatorie.service.ContributoService;
import  it.regioneveneto.myp3.gestgraduatorie.utils.Constants;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.EsitoServizio;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.Result;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.RichiestaDTO;

@RestController
@RequestMapping("/open")
public class ContributoTaskController {

    private static final Logger logger = LoggerFactory.getLogger(ContributoTaskController.class);
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
    
    
    @PostMapping(value = "/insertRichiesta",consumes  = MediaType.APPLICATION_JSON)
    public ResponseEntity<Result<RichiestaDTO>> insertRichiestaContributo(
			@ApiParam(value = "Richiesta da inserire", required = true, name = "richiesta")
			@RequestBody( required=true ) String richiesta) 
					throws Exception{
    	RichiestaDTO richiestaInserita = null;
		EsitoServizio esitoServizio = null;
    	Date today = FORMAT.parse(FORMAT.format(new Date()));
    	logger.info("Start insertRichiestaContributo ("+today+") ");

    	JSONObject payload = new JSONObject(richiesta);
    	for (String keyStr : payload.keySet()) {
			Object keyvalue = payload.get(keyStr);
			// if (keyvalue instanceof JSONObject) {
			logger.info("key: " + keyStr + " value: " + keyvalue.toString());
			// }

		}
    	
    	
    	try {
    		richiestaInserita = iContributoService.createRichiestaContributo(richiesta);
	    	esitoServizio = new EsitoServizio(Constants.CODICE_OK,Constants.DESCRIZIONE_OK);
	    	logger.info("POST eseguito con successo. Inserito un nuovo oggetto con id {}.", richiestaInserita.getId() );
		}catch (ServiceException e) {
			// TODO: handle exception
			logger.error("Errore calling insertRichiestaContributo.... " + e.getMessage());
			esitoServizio = new EsitoServizio(Constants.CODICE_KO,e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Result<RichiestaDTO>(esitoServizio, richiestaInserita) );
		} 	


		return ResponseEntity.ok(new Result<RichiestaDTO>(esitoServizio, richiestaInserita));

	}

	
}

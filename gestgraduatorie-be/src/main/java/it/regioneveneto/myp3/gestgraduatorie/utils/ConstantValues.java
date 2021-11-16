package it.regioneveneto.myp3.gestgraduatorie.utils;

import org.springframework.stereotype.Component;

@Component
public class ConstantValues {

	public static final String HEADER_TENANT_CONTEXT = "X-MYPLACE-TenantContext";
	public static final String HEADER_SECURITY_CONTEXT = "X-MYPLACE-SecurityContext";
	public static final String HEADER_REQUEST_CONTEXT = "X-MYPLACE-RequestContext";
	public static final String LOG_HEADERS_FORMAT = "Request headers: X-MYPLACE-TenantContext {}, X-MYPLACE-SecurityContext {}, X-MYPLACE-RequestContext {}";
	
	
/***
 *        ______                 ______            _____                        __  _           
 *       /  _/ /____  ____ ___  / ____/___  ____  / __(_)___ ___  ___________ _/ /_(_)___  ____ 
 *       / // __/ _ \/ __ `__ \/ /   / __ \/ __ \/ /_/ / __ `/ / / / ___/ __ `/ __/ / __ \/ __ \
 *     _/ // /_/  __/ / / / / / /___/ /_/ / / / / __/ / /_/ / /_/ / /  / /_/ / /_/ / /_/ / / / /
 *    /___/\__/\___/_/ /_/ /_/\____/\____/_/ /_/_/ /_/\__, /\__,_/_/   \__,_/\__/_/\____/_/ /_/ 
 *                                                   /____/                                     

 * Costanti utilizzate per tutto ci� che riguarda le item configuration
 */
	/**
	 * Valore per il domain di default per i parametri di configurazione
	 */
	public static final String C_DEFAULT = "C_DEFAULT";
	
	/**
	 * Valore del campo category per recuperare i nomi delle sezioni e/o delle liste di valori
	 */
	public static final String CATEGORY_NOME_CATEGORIE = "nomeCategorie";
	
	/**
	 * Valore utilizzato per le sezioni di menu
	 */
	public static final String TYPE_SECTION = "section";
	
	/**
	 * Valore utilizzato per le liste di selezione
	 */
	public static final String TYPE_SECTION_LIST = "section_list";
	
	/**
	 * Valore utilizzato per gli elementi di una lista di selezione
	 */
	public static final String TYPE_DYNAMIC_LIST = "dynamiclist";
	
	/**
	 * Valore utilizzato per i parametri di una sezione
	 */
	public static final String TYPE_PARAM = "param";
	
	/**
	 * Valore utilizzato per la sezione dei dati dell'ente
	 */
	public static final String CATEGORY_DATI_ENTE = "datiEnte";
	
	/**
	 * Valore utilizzato per la sezione dei servizi integrati
	 */
	public static final String CATEGORY_SERVIZI_INTEGRATI = "serviziIntegrati";
	
	public static final String NON_DEFINITO = "n.d.";

	public static final String TRUE = "true";

	public static final String FALSE = "false";
	
	/**
	 * 
	 */
	public static Integer MAX_GIORNI_PER_RINNOVO_DEFAULT_VALUE;
	
	/**
	 * MAIL
	 */
	public static String SPRING_MAIL_DEFAULT_ENCODING;
	public static String SPRING_MAIL_PROTOCOL;
	public static String SPRING_MAIL_SMTP_AUTH;
	public static String SPRING_MAIL_SMTP_STARTTLS_ENABLE;
	
	/**
	 * Enumeration che racchiude la lista di tutte le possibili tipologiche.
	 */
	public enum CategoriaTipologica {
		
		PRACTICE_OUTCOME("practiceOutcome"),				// ESITO PRATICA
		PASS_STATUS("passStatus"),							// STATO PASS
		PERMISSION_TYPE("permissionType"),					// TIPOLOGIA PASS/PERMESSO
		ACQUISITION_CHANNEL("acquisitionChannel"),			// CANALE DI ACQUISIZIONE
		VEHICLE_TYPE("vehicleType"),						// TIPO VEICOLO
		VEHICLE_USE_TYPE("vehicleUseType"),					// TIPO USO VEICOLO
		STATE_SEND_REGISTRY("stateSendRegistry"),			// STATO DI INVIO AL REGISTRY
		PAYMENT_TYPE("paymentType"),						// TIPO DI PAGAMENTO
		DOCUMENT_TYPE("documentType"),						// TIPO DI DOCUMENTO
		PASS_DURATION("passDuration"), 						// DURATA DEL PASS
		PAYMENT_TERMS("paymentTerms"),						// MODALITA DI PAGAMENTO
		REFERENT_TYPE("referentType"),						// TITOLO PERSONA DI RIFERIMENTO
		CAUSA_SOSP_CESS("causaSospCess"); 					// CAUSA SOSPENSIONE/CESSAZIONE
		
		private String categoria;
		
		CategoriaTipologica(String categoria){
			this.categoria = categoria;
		}
		
		public String getCategoria(){
			return this.categoria;
		}
		
		public static CategoriaTipologica getCategoriaTipologica(String categoria){
			for(CategoriaTipologica c : CategoriaTipologica.values() ){
				if(c.getCategoria().equalsIgnoreCase(categoria))
					return c;
			}
			return null;
		}
		
	}
	
	

/***
 *       _____ __                                 ____  ___   __________
 *      / ___// /_____ _____ ___  ____  ____ _   / __ \/   | / ___/ ___/
 *      \__ \/ __/ __ `/ __ `__ \/ __ \/ __ `/  / /_/ / /| | \__ \\__ \ 
 *     ___/ / /_/ /_/ / / / / / / /_/ / /_/ /  / ____/ ___ |___/ /__/ / 
 *    /____/\__/\__,_/_/ /_/ /_/ .___/\__,_/  /_/   /_/  |_/____/____/  
 *                            /_/                                       
 *  Costanti utilizzate per il servizio di stampa del PASS BLU
 */
	
	public static String DOCUMENT_TYPE_FIRMA;
	public static String DOCUMENT_TYPE_FOTOTESSERA;
	public static String NOME_SEZIONE_STAMPA_PASS_BLU;
	public static String PRACTICE_OUTCOME_ACCOLTA;
	
	//PATH JRXML
	public static final String REPORT_FRONTE_JRXML_PATH = "stampa/fronte.jrxml";
	public static final String REPORT_RETRO_JRXML_PATH = "stampa/retro.jrxml";
	public static final String REPORT_EMAIL_JRXML_PATH = "stampa/email.jrxml";
	public static final String REPORT_PASS_IN_SCADENZA_PATH = "stampa/passInScadenza.jrxml";
	public static final String REPORT_PASS_INSERITI_PATH = "stampa/passInseriti.jrxml";
	
	//PATH IMMAGINI
	public static final String SFONDO_PASS_BLU_RETRO = "stampa/retro.png";
	public static final String SFONDO_PASS_BLU_FRONTE = "stampa/fronte.png";
	public static final String SFONDO_EMAIL_SCADENZA_PASS = "stampa/emailBackground.png";
	public static final String LOGO_REPORT_PATH = "stampa/regione-veneto.png";
	
	//PATH IMMAGINI DEFAULT
	public static final String REPORT_RETRO_DEFAULT_FIRMA_PATH = "stampa/defaultFirma.png";
	public static final String REPORT_RETRO_DEFAULT_FOTOTESSERA_PATH = "stampa/defaultFototessera.png";
	
	//NOMI PARAMETRI REPORT
	public static final String REPORT_FRONTE_NOME_PARAMETRO_SCADENZA = "scadenza";
	public static final String REPORT_FRONTE_NOME_PARAMETRO_NUMERO = "numero";
	public static final String REPORT_FRONTE_NOME_PARAMETRO_ENTE = "ente";
	public static final String REPORT_RETRO_NOME_PARAMETRO_NOME = "nome";
	public static final String REPORT_RETRO_NOME_PARAMETRO_COGNOME = "cognome";
	public static final String REPORT_RETRO_NOME_PARAMETRO_FIRMA = "firma";
	public static final String REPORT_RETRO_NOME_PARAMETRO_FOTOTESSERA = "fototessera";
	public static final String REPORT_NOME_PARAMETRO_STAMPA_SFONDO = "stampaSfondo";
	public static final String REPORT_NOME_PARAMETRO_SFONDO = "sfondo";
	public static final String REPORT_NOME_PARAMETRO_IS_DUPLICATO = "isDuplicato";
	public static final String REPORT_NOME_PARAMETRO_TITOLARE = "titolare";
	public static final String REPORT_NOME_PARAMETRO_DATA_SCADENZA = "dataScadenza";
	public static final String REPORT_NOME_PARAMETRO_NUMERO_PERMESSO = "numeroPermesso";
	
	//KEY PER IDENTIFICARE GLI ELEMENTI DEL REPORT
	public static final String REPORT_FRONTE_KEY_ELEMENT_SCADENZA = "scadenza";
	public static final String REPORT_FRONTE_KEY_ELEMENT_NUMERO = "numero";
	public static final String REPORT_FRONTE_KEY_ELEMENT_ENTE = "ente";
	public static final String REPORT_RETRO_KEY_ELEMENT_NOME = "nome";
	public static final String REPORT_RETRO_KEY_ELEMENT_COGNOME = "cognome";
	public static final String REPORT_RETRO_KEY_ELEMENT_FIRMA = "firmaFrame";
	public static final String REPORT_RETRO_KEY_ELEMENT_FOTOTESSERA = "fototesseraFrame";
	public static final String REPORT_FRONTE_KEY_ELEMENT_DUPLICATO = "duplicato";
	
	//DIMENSIONI STANDARD ELEMENTI REPORT
	public static String REPORT_ELEMENT_NAME_HEIGHT;
	public static String REPORT_ELEMENT_NAME_WIDTH;
	public static String REPORT_ELEMENT_COGNOME_HEIGHT;
	public static String REPORT_ELEMENT_COGNOME_WIDTH;
	public static String REPORT_ELEMENT_FIRMA_HEIGHT;
	public static String REPORT_ELEMENT_FIRMA_WIDTH;
	public static String REPORT_ELEMENT_FOTOTESSERA_HEIGHT;
	public static String REPORT_ELEMENT_FOTOTESSERA_WIDTH;
	public static String REPORT_ELEMENT_SCADENZA_HEIGHT;
	public static String REPORT_ELEMENT_SCADENZA_WIDTH;
	public static String REPORT_ELEMENT_NUMERO_PASS_HEIGHT;
	public static String REPORT_ELEMENT_NUMERO_PASS_WIDTH;
	public static String REPORT_ELEMENT_ENTE_HEIGHT;
	public static String REPORT_ELEMENT_ENTE_WIDTH;
	public static String REPORT_ELEMENT_DUPLICATO_HEIGHT;
	public static String REPORT_ELEMENT_DUPLICATO_WIDTH;
	
	//FORMATO IMMAGINE PNG
	public static final String REPORT_FORMATO_IMMAINE_PNG = "png";

	public enum TipoReport { FRONTE_PASS_BLU, RETRO_PASS_BLU };
	
	public enum TipoParametroStampa {
		
		//scadenza
		DISTANZA_TOP_DATA_SCADENZA("distTopScadenza"),
		DISTANZA_LEFT_DATA_SCADENZA("distLeftScadenza"),
		//numero
		DISTANZA_TOP_NUMERO_PASS("distTopNumPass"),
		DISTANZA_LEFT_NUMERO_PASS("distLeftNumPass"),
		//cognome
		DISTANZA_TOP_COGNOME("distTopCognome"),
		DISTANZA_LEFT_COGNOME("distLeftCognome"),
		//nome
		DISTANZA_TOP_NOME("distTopNome"),
		DISTANZA_LEFT_NOME("distLeftNome"),
		//fototessera
		DISTANZA_TOP_FOTOTESSERA("distTopFoto"),
		DISTANZA_LEFT_FOTOTESSERA("distLeftFoto"),
		//firma
		DISTANZA_TOP_FIRMA("distTopFotoFirma"),
		DISTANZA_LEFT_FIRMA("distLeftFotoFirma"),
		//duplicato
		DISTANZA_TOP_DUPLICATO("distTopDuplicato"),
		DISTANZA_LEFT_DUPLICATO("distLeftDuplicato"),
		//ente
		DISTANZA_TOP_ENTE("distTopEnte"),
		DISTANZA_LEFT_ENTE("distLeftEnte"),
		//path
		PATH_FOTO_FIRMA("pathFotoFirma"),
		PATH_FOTO("pathFoto");
		
		private String nomeParametro;
		
		TipoParametroStampa(String nomeParametro) {
			this.nomeParametro = nomeParametro;
		}
		
		public String nomeParametro(){
			return this.nomeParametro;
		}
		
	}
	
	public enum TipoAllegato {
		
		FOTOTESSERA, FIRMA;
		
		public String docType() {
			switch(this) {
			case FIRMA: return ConstantValues.DOCUMENT_TYPE_FIRMA;
			case FOTOTESSERA: return ConstantValues.DOCUMENT_TYPE_FOTOTESSERA;
			}
			return null;
		}
		
	}


	/***
	 *       ____________    __
	 *      / ____/ ___/ |  / /
	 *     / /    \__ \| | / / 
	 *    / /___ ___/ /| |/ /  
	 *    \____//____/ |___/   
	 *                         
	 * Costanti per l'export in CSV                        
	 */
	public static final String CONTENT_TYPE_TEXT_PLAIN = "text/plain";
	public static final String CONTENT_TYPE_TEXT_CSV = "text/csv";
	public static final char DELIMITER_CSV = ';';
	public static final char DELIMITER_NEW_LINE = '\n';
	public static final String[] HEADER_CSV_TARGA = new String[] {
			"Targa",
			"Tipo Veicolo",
			"Data Inizio Validita",
			"Data Fine Validita",
			"Tipo Uso",
			"Estera",
			"Flag Network",
			"Codice ente"
	};
	public static final String[] HEADER_CSV_PASS = new String[] {
			"Numero Permesso",
			"RFID",
			"Codice Fiscale",
			"Nome",
			"Cognome",
			"Valido dal",
			"Valido al",
			"Data rilascio",
			"Stato pratica",
			"Stato pass",
			"Data cessazione",
			"Durata",
			"Codice ente"
	};
	public static final String[] HEADER_LISTA_ERRORI = new String[] {
			"Indice record", "Campo", "Descrizione errore"
	};

	public enum HeaderCSVImportMassivo {
		NUMERO_PERMESSO("Numero permesso"), RFID("RFID"), CODICE_FISCALE("Codice fiscale"), NOME("Nome"), COGNOME("Cognome"), 
		DATA_INIZIO_DECORRENZA("Data inizio decorrenza"), DATA_RILASCIO("Data rilascio"), DATA_SCADENZA("Data scadenza"),
		STATO("Stato"), CODICE_COMUNE("Codice comune"), TIPOLOGIA_PASS("Tipo Pass"), DATA_CESSAZIONE("Data di Cessazione del Pass"), 
		CAUSALE_CESSAZIONE("Causale di Cessazione"), FLAG_TEMPORANEO("Flag Pass Temporaneo"), ANNOTAZIONI("Annotazioni"), 
		TARGA("Targa"), TIPO_VEICOLO("Tipo veicolo"), DATA_INIZIO_VALIDITA_TARGA("Data inizio validita"), 
		DATA_FINE_VALIDITA_TARGA("Data fine validita"),	FLAG_ZTL_NETWORK("Flag ZTL Network");
		
		private String header;
		
		HeaderCSVImportMassivo(String header) {
			this.header = header;
		}
		public static String[] headers() {
			String[] headers = new String[HeaderCSVImportMassivo.values().length];
			for(int i=0;i<HeaderCSVImportMassivo.values().length;i++) {
				headers[i] = HeaderCSVImportMassivo.values()[i].header;
			}
			return headers;
		}
		public String header() {
			return header;
		}
	}

	public static final String DEFAULT_TIPOLOGIA_PASS = "permissionType_h_pass_blu";
	
	

	/**
	 * 
	 */
	public static final String REGISTRY_RESPONSE_SENT = "Richiesta inviata";
	public static final String REGISTRY_RESPONSE_ERROR = "Errore";
	public static final String REGISTRY_RESPONSE_RECEIVED = "Richiesta ricevuta";
	public static final String REGISTRY_RESPONSE_CODE_SENT = "SENT";
	public static final String REGISTRY_RESPONSE_CODE_ERROR = "ERROR";
	public static final String REGISTRY_RESPONSE_CODE_RECEIVED = "RECEIVED";
	
	public static final String R000 = "R000";
	public static final String A000 = "A000";

	public static final String OPERATION_TYPE_RICERCA_MASSIVA = "RA";
	
	public static final String[] valoriAmmessiTrue = new String[] {
		"1", "true", "vero"	
	};
	public static final String[] valoriAmmessiFalse = new String[] {
		"0", "false", "falso"	
	};
	
}

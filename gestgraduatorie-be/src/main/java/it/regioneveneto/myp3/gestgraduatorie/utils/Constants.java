package it.regioneveneto.myp3.gestgraduatorie.utils;

public class Constants {
	//Lista operazioni
	public static final int DEFAULT_SEARCH_PAGE_SIZE = 10; 
	
    public static final String CODICE_ERRORE_GENERICO = "9999";
    public static final String CODICE_OK = "0";
    public static final String DESCRIZIONE_OK = "Operazione riuscita correttamente";
    public static final String CODICE_KO = "1";
    public static final String DESC_ERRORE_GENERICO = "Errore generico";
    
    public static final String YEAR = "YEAR";
    public static final String SOGLIA_ETA = "soglia.eta";
    public static final String SOGLIA_ISEE = "soglia.isee";
    public static final String SOGLIA_NUCLEO = "soglia.nucleo";
    public static final String SOGLIA_COMUNE = "soglia.comune";
//////////////////////// VECCHIE OPERAZIONI COME RIFERIMENTO, DA ELIMINARE //////////////////////////////////////
    //Lista stati impianti
    public static final String STATO_IMPIANTO_PRE_INSERIMENTO = "PRE-INS";
    public static final String STATO_IMPIANTO_INSERITO = "INS";
    public static final String STATO_IMPIANTO_ELIMINATO = "DEL";
    public static final String STATO_IMPIANTO_VALIDATO = "VAL";
    public static final String STATO_IMPIANTO_DISMESSO = "DIS";

    //Lista Tipo operazioni in corso
    public static final String TIPO_OPERAZIONE_IN_CORSO_INS = "INS";
    public static final String TIPO_OPERAZIONE_IN_CORSO_MOD = "MOD";
    public static final String TIPO_OPERAZIONE_IN_CORSO_DIS = "DIS";
    public static final String TIPO_OPERAZIONE_IN_CORSO_VAL = "VAL";

    //Lista tipo operazioni su IDT
    public static final String ALLINEA_IDT_OPER_CREA = "INS";
    public static final String ALLINEA_IDT_OPER_MODIFICA = "MOD";
    public static final String ALLINEA_IDT_OPER_CANCELLA = "DEL";

    //Lista tipo geometrie
    public static final String IMPIANTO_TIPO_GEOMETRIA_POINT = "Point";
    public static final String IMPIANTO_TIPO_GEOMETRIA_LINE = "LineString";


    //Autore operazione in corso fixed
    public static final String AUTORE_OPERAZIONE_IN_CORSO_QUARTZ = "SCHEDULED";

    //Codici vari
    public static final String CATEGORIA_OSTACOLO_LINEARE = "OL";

    //Costanti relative a IDT
    public static final Integer CARTOG_ALLINEA_ESITO_NEGATIVO = 0;
    public static final String IDT_CREA_FEATURE_TYPE = "Feature";
    public static final String IDT_IMPIANTO_DENOMINAZIONE_NON_DISPONIBILE = "N.D.";
    public static final String IDT_SIMBOLOGIA_ATT = "ATT";


    public static final String TIPO_MY_REGISTRY = "myregistry";
    public static final String TIPO_MY_OPERATOR_REGISTRY = "myoperatorregistry";

    // Lista codici operazioni
    public static final String OPER_AVVIO = "avvio";
    public static final String OPER_CHIUDI = "chiudi";
    public static final String OPER_INSAGG = "insagg";
    public static final String OPER_DISM = "dismissione";
    public static final String OPER_DEL = "eliminazione";
    public static final String OPER_CONT = "controllo";
    public static final String OPER_DEL_ROW = "eliminazioneRiga";
    public static final String OPER_VAL = "validazione";
    public static final String OPER_DEL_CONT = "eliminaControllo";
    public static final String OPER_CARTOG_IMP = "allineaEsitoIdt";

    // Lista codici errore
    public static final String CODICE_ERRORE_OK = "0";

    //Codici errori PUT_IMP
    public static final String CODICE_ERRORE_DATI_OPERAZIONE_PUT_IMP = "100";
    public static final String CODICE_ERRORE_CAMPI_DATI_OPE_NON_COINCIDONO = "101";
    public static final String CODICE_ERRORE_DATI_GESTORE_PUT_IMP = "102";
    public static final String CODICE_ERRORE_AGGIORNAMENTO_IMPIANTO_PUT_IMP = "111";
    public static final String CODICE_ERRORE_DATI_IMPIANTO_PUT_IMP = "112";
    public static final String CODICE_ERRORE_DENOMINAZIONE_UNIVOCA_PUT_IMP = "113";
    public static final String CODICE_ERRORE_GEST_COD_FISC_PUT_IMP = "114";
    public static final String CODICE_ERRORE_IMP_ELEM_CHANGED_PUT_IMP = "115";

    public static final String CODICE_ERRORE_GENERICO_PUT_IMP = "199";

    //Codici errori CLOSE_IMP
    public static final String CODICE_ERRORE_IDENTIFICATIVO_IMPIANTO = "201";
    public static final String CODICE_ERRORE_IMPIANTO_NON_ESISTENTE = "202";
    public static final String CODICE_ERRORE_CAMPI_DATI_OPE_NON_COINCIDONO_CLOSE_OPER = "203";
    public static final String CODICE_ERRORE_GENERICO_CLOSE_OPER = "299";

    //Codici errori START_IMP
    public static final String CODICE_ERRORE_CAMPI_DATI_OPE_NON_COINCIDONO_START_OP_IMP = "301";
    public static final String CODICE_ERRORE_GENERICO_START_OP_IMP = "399";

    //Codici errori VAL_IMP
    public static final String CODICE_ERRORE_ID_IMPIANTO_VAL_IMP = "401";
    public static final String CODICE_ERRORE_CAMPI_DATI_OPE_NON_COINCIDONO_VAL_IMP = "402";
    public static final String CODICE_ERRORE_GENERICO_VAL_IMP = "499";

    //Codici errori DISM_IMP
    public static final String CODICE_ERRORE_ID_IMPIANTO_DISM_IMP = "501";
    public static final String CODICE_ERRORE_CAMPI_DATI_OPE_NON_COINCIDONO_DISM_IMP = "502";
    public static final String CODICE_ERRORE_GENERICO_DISM_IMP = "599";

    //Codici errori DEL_IMP
    public static final String CODICE_ERRORE_ID_IMPIANTO_DEL_IMP = "601";
    public static final String CODICE_ERRORE_CAMPI_DATI_OPE_NON_COINCIDONO_DEL_IMP = "602";
    public static final String CODICE_ERRORE_GENERICO_DEL_IMP = "699";

    //Codici errori CONT_IMP
    public static final String CODICE_ERRORE_ID_IMPIANTO_CONT_IMP = "701";
    public static final String CODICE_ERRORE_CAMPI_DATI_OPE_NON_COINCIDONO_CONT_IMP = "702";
    public static final String CODICE_ERRORE_GENERICO_CONT_IMP = "799";

    //Codici errori DEL_ROW_IMP
    public static final String CODICE_ERRORE_ID_IMPIANTO_DEL_ROW_IMP = "801";
    public static final String CODICE_ERRORE_CAMPI_DATI_OPE_NULL_DEL_ROW_IMP = "802";
    public static final String CODICE_ERRORE_CAMPI_DATI_OPE_NON_COINCIDONO_DEL_ROW_IMP = "803";
    public static final String CODICE_ERRORE_GENERICO_DEL_ROW_IMP = "899";

    //Codici errori CARTOG_IMP
    public static final String CODICE_ERRORE_ID_IMPIANTO_CARTOG_IMP = "901";
    public static final String CODICE_ERRORE_CAMPI_DATI_OPE_NULL_CARTOG_IMP = "902";
    public static final String CODICE_ERRORE_CAMPI_DATI_OPE_NON_COINCIDONO_CARTOG_IMP = "903";
    public static final String CODICE_ERRORE_GENERICO_CARTOG_IMP = "999";

    //Codici errori DEL_CONT
    public static final String CODICE_ERRORE_ID_IMPIANTO_DEL_CONT_IMP = "1001";
    public static final String CODICE_ERRORE_CAMPI_DATI_OPE_NON_COINCIDONO_DEL_CONT_IMP = "1002";
    public static final String CODICE_ERRORE_GENERICO_OPER_DEL_CONT = "1099";


    //Codici errori GET_IMP
    public static final String CODICE_ERRORE_ERRORE_GENERICO_GET_IMP = "10000";
    //Codici errori EXIST_IMP
    public static final String CODICE_ERRORE_ERRORE_GENERICO_EXIST_IMP = "10100";



    //Errori generici con codice 9xxx
    public static final String CODICE_ERRORE_CAMPI_OBBLIGATORI_MANCANTI = "9000";
    public static final String CODICE_ERRORE_EXPORT_CSV = "9001";
    public static final String CODICE_ERRORE_FORMATO_EXPORT = "9002";



    // Lista Messaggi errore
    public static final String DESC_ERRORE_CAMPI_OBBLIGATOR_MANCANTI = "Campi obbligatori mancanti";
    public static final String DESC_ERRORE_DATI_OPERAZIONE = "Non sono state inviate le informazioni sull'operazione in corso";
    public static final String DESC_ERRORE_AGGIORNAMENTO_IMPIANTO = "Aggiornamento non possibile: non è presente a sistema un impianto con identificativo passato in input";
    public static final String DESC_ERRORE_CAMPI_DATI_OPE_NON_COINCIDONO = "I dati dell’operazione richiesta non coincidono con quella in corso";
    public static final String DESC_ERRORE_ID_IMPIANTO_MANCANTE = "Richiesta non valida: identificativo dell'impianto nullo";
    public static final String DESC_ERRORE_DENOMINAZIONE_UNIVOCA_PUT_IMP = "E' già presente un impianto con la stessa denominazione.";
    public static final String DESC_ERRORE_GEST_COD_FISC_PUT_IMP = "E' già presente un gestore con il stesso codice fiscale.";
    public static final String DESC_ERRORE_IMP_ELEM_CHANGED_PUT_IMP = "Errore nella scrittura in JSON degli elementi impianto modificati";



    public static final String DESC_ERRORE_GENERICO_PUT_IMP = "Errore generico nell'inserimento / modifica impianto";
    public static final String DESC_ERRORE_ERRORE_GENERICO_START_OP_IMP = "Errore generico nell'avvio operazione su un impianto";
    public static final String DESC_ERRORE_ERRORE_GENERICO_GET_IMP = "Errore generico nella get impianto";
    public static final String DESC_ERRORE_ERRORE_GENERICO_CLOSE_OPER = "Errore generico nella chiusura di un'operazione";
    public static final String DESC_ERRORE_GENERICO_VAL_IMP = "Errore generico nella validazione di un impianto";
    public static final String DESC_ERRORE_GENERICO_DISM_IMP = "Errore generico nella dismissione di un impianto";
    public static final String DESC_ERRORE_GENERICO_DEL_IMP = "Errore generico nell'eliminazione di un impianto";
    public static final String DESC_ERRORE_GENERICO_CONT_IMP = "Errore generico nell'aggiunta di un controllo su un impianto";
    public static final String DESC_ERRORE_GENERICO_DEL_ROW_IMP = "Errore generico nell'eliminazione di una riga provvisoria di un impianto";
    public static final String DESC_ERRORE_ERRORE_GENERICO_DEL_CONTROLLO_IMP = "Errore generico nell'eliminazione di un controllo impianto";
    public static final String DESC_ERRORE_GENERICO_CARTOG_IMP = "Errore generico nell'allineamento di un impianto con esito IDT.";
    public static final String DESC_ERRORE_ERRORE_GENERICO_EXIST_IMP = "Errore generico nella verifica di istanze attive per un impianto";

    //Export CSV
    public static final String DESC_ERRORE_EXPORT_CSV = "Errore nella creazione del file csv.";
    public static final String DESC_ERRORE_FORMATO_EXPORT = "Tipo di export non previsto.";

    //Nome files csv
    public static final String NOMEFILE_CSV_LISTA_IMPIANTI = "lista_impianti";
    public static final String NOMEFILE_CSV_LISTA_ARCHIVIO_MODIFICHE = "lista_archivio_modifiche";

    //Labels generiche
    public static final String LABEL_SI = "SI";
    public static final String LABEL_NO = "NO";
    public static final String LABEL_STATO_IMPIANTO = "Stato Impianto";
    public static final String LABEL_ELEMENTO_N = "Elemento N.";
    public static final String BLANK_CSV_VALUE = "-";

    
    
	
}

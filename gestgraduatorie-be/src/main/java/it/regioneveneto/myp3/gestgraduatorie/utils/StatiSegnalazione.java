package it.regioneveneto.myp3.gestgraduatorie.utils;

import java.util.HashMap;
import java.util.Map;

public enum StatiSegnalazione {
	IN_BOZZA("BOZZA", "In bozza"),
	ACCETTATA("ACCETTATA", "Completata"),
	INVIATA("INVIATA", "Inviata"),
//	RICEVUTA("RIC", "Ricevuta"),
//	CONFERMATA("CON", "Confermata"),
	RIFIUTATA("RIFIUTATA", "Rifiutata");
//	APERTA_PE("APP", "Apertura post emergenza"),
//	INVIATA_PE("INP", "Inviata post emergenza");

	private String id;
	private String descr;
	
	private StatiSegnalazione(String id, String descr) {
		this.id = id;
		this.descr = descr;
	}
	
    private static final Map<String, StatiSegnalazione> BY_ID = new HashMap<>();
    
    static {
        for (StatiSegnalazione e: values()) {
        	BY_ID.put(e.id, e);
        }
    }
 
    public static StatiSegnalazione valueOfId(String id) {
        return BY_ID.get(id);
    }

	public String getDescr() {
		return descr;
	}
	
	public String getId() {
		return id;
	}
}

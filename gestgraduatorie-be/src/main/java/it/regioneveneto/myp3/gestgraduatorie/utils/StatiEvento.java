package it.regioneveneto.myp3.gestgraduatorie.utils;

import java.util.HashMap;
import java.util.Map;

public enum StatiEvento {
	IN_BOZZA("BOZ", "In bozza"),
	RACCOLTA_DATI("RAC", "Raccolta dati"),
	ATTESA_EMERGENZA("ATT", "Attesa emergenza"),
	POST_EMERGENZA("POS", "Post emergenza");

	private String id;
	private String descr;
	
	private StatiEvento(String id, String descr) {
		this.id = id;
		this.descr = descr;
	}
	
    private static final Map<String, StatiEvento> BY_ID = new HashMap<>();
    
    static {
        for (StatiEvento e: values()) {
        	BY_ID.put(e.id, e);
        }
    }
 
    public static StatiEvento valueOfId(String id) {
        return BY_ID.get(id);
    }

	public String getDescr() {
		return descr;
	}
	
	public String getId() {
		return id;
	}
}

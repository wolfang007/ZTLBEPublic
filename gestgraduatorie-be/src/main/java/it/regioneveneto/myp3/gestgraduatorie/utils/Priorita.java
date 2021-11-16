package it.regioneveneto.myp3.gestgraduatorie.utils;

import java.util.HashMap;
import java.util.Map;

public enum Priorita {

	EMERGENZA("EMERGENZA", "Emergenza"),
	ORDINARIA("ORDINARIA", "Ordinaria");

	private String id;
	private String descr;
	
	private Priorita(String id, String descr) {
		this.id = id;
		this.descr = descr;
	}
	
    private static final Map<String, Priorita> BY_ID = new HashMap<>();
    
    static {
        for (Priorita e: values()) {
        	BY_ID.put(e.id, e);
        }
    }
 
    public static Priorita valueOfId(String id) {
        return BY_ID.get(id);
    }

	public String getDescr() {
		return descr;
	}
	
	public String getId() {
		return id;
	}
}

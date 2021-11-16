package it.regioneveneto.myp3.gestgraduatorie.utils;

import java.util.HashMap;
import java.util.Map;

public enum Fragilita {
	MOTORIA("MOTORIA", "Motoria"),
	PSICHICA("PSICHICA", "Psichica"),
	SENSORIALE("SENSORIALE", "Sensoriale");

	private String id;
	private String descr;
	
	private Fragilita(String id, String descr) {
		this.id = id;
		this.descr = descr;
	}
	
    private static final Map<String, Fragilita> BY_ID = new HashMap<>();
    
    static {
        for (Fragilita e: values()) {
        	BY_ID.put(e.id, e);
        }
    }
 
    public static Fragilita valueOfId(String id) {
        return BY_ID.get(id);
    }

	public String getDescr() {
		return descr;
	}
	
	public String getId() {
		return id;
	}
}
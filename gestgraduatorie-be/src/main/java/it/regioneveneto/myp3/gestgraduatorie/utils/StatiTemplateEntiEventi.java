package it.regioneveneto.myp3.gestgraduatorie.utils;

import java.util.HashMap;
import java.util.Map;

public enum StatiTemplateEntiEventi {
	CHIUSO("CHI", "Chiuso"),
	APERTO("APE", "Aperto");

	private String id;
	private String descr;
	
	private StatiTemplateEntiEventi(String id, String descr) {
		this.id = id;
		this.descr = descr;
	}
	
    private static final Map<String, StatiTemplateEntiEventi> BY_ID = new HashMap<>();
    
    static {
        for (StatiTemplateEntiEventi e: values()) {
        	BY_ID.put(e.id, e);
        }
    }
 
    public static StatiTemplateEntiEventi valueOfId(String id) {
        return BY_ID.get(id);
    }

	public String getDescr() {
		return descr;
	}
	
	public String getId() {
		return id;
	}
}

package it.regioneveneto.myp3.gestgraduatorie.utils;

import java.util.HashMap;
import java.util.Map;

public enum Misura {

	ALTA("LIEVE", "Lieve"),
	MEDIA("MEDIA", "Media"),
	GRAVE("GRAVE", "Grave");

	private String id;
	private String descr;
	
	private Misura(String id, String descr) {
		this.id = id;
		this.descr = descr;
	}
	
    private static final Map<String, Misura> BY_ID = new HashMap<>();
    
    static {
        for (Misura e: values()) {
        	BY_ID.put(e.id, e);
        }
    }
 
    public static Misura valueOfId(String id) {
        return BY_ID.get(id);
    }

	public String getDescr() {
		return descr;
	}
	
	public String getId() {
		return id;
	}
}

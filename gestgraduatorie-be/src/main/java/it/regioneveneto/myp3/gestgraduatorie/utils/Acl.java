package it.regioneveneto.myp3.gestgraduatorie.utils;

import java.util.HashMap;
import java.util.Map;

public enum Acl {
	
	OPE("ope", "OPERATORE");
	//OPAS("opas","OPERATORE ASSISTENTE SOCIALE");

	private String id;
	private String descr;
	
	private Acl(String id, String descr) {
		this.id = id;
		this.descr = descr;
	}
	
    private static final Map<String, Acl> BY_ID = new HashMap<>();
    
    static {
        for (Acl e: values()) {
        	BY_ID.put(e.id, e);
        }
    }
 
    public static Acl valueOfId(String id) {
        return BY_ID.get(id);
    }

	public String getDescr() {
		return descr;
	}
	
	public String getId() {
		return id;
	}
}

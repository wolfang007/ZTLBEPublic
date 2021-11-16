package it.regioneveneto.myp3.gestgraduatorie.utils;

import java.util.HashMap;
import java.util.Map;

public enum AnaTpCampoModuli {
	DATA(1l, "Data"),
	DESCRITTIVO(2l, "Descrittivo"),
	NUMERO(3l, "Numero"),
	TENDINA(4l, "Campo con menù a tendina");
	
	private Long id;
	private String descr;
	
	private AnaTpCampoModuli(Long id, String descr) {
		this.id = id;
		this.descr = descr;
	}
	
    private static final Map<Long, AnaTpCampoModuli> BY_ID = new HashMap<>();
    
    static {
        for (AnaTpCampoModuli e: values()) {
        	BY_ID.put(e.id, e);
        }
    }
 
    public static AnaTpCampoModuli valueOfId(Long id) {
        return BY_ID.get(id);
    }

	public String getDescr() {
		return descr;
	}
	
	public Long getId() {
		return id;
	}
}

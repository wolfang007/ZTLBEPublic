package it.regioneveneto.myp3.gestgraduatorie.idt.bean.request;

public class IdtDelRequest {
	private String layer;
    private Integer idFeature;
	

	public String getLayer() {
		return layer;
	}
	public void setLayer(String layer) {
		this.layer = layer;
	}
	public Integer getIdFeature() {
		return idFeature;
	}
	public void setIdFeature(Integer idFeature) {
		this.idFeature = idFeature;
	}
    
	@Override
	public String toString() {
		return "IdtDelRequest [layer=" + layer + ", idFeature=" + idFeature + "]";
	}

}

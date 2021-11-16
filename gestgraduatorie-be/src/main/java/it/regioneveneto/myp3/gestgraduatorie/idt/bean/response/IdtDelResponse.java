package it.regioneveneto.myp3.gestgraduatorie.idt.bean.response;

public class IdtDelResponse {


	private Boolean success;
    private String msg;
    private String result;
    private String code;
    private String detailMsg;
    
	
    public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDetailMsg() {
		return detailMsg;
	}
	public void setDetailMsg(String detailMsg) {
		this.detailMsg = detailMsg;
	}
	
	@Override
	public String toString() {
		return "IdtDelResponse [success=" + success + ", msg=" + msg + ", result=" + result + ", code=" + code
				+ ", detailMsg=" + detailMsg + "]";
	}
	
}

package it.regioneveneto.myp3.gestgraduatorie.idt.bean.response;

import java.io.Serializable;

public class IdtResponse implements Serializable {

    private Boolean success;
    private String msg;
    private Result result;
    private String code;
    private String detailMsg;

    public IdtResponse() {
    }

    public IdtResponse(Boolean success, String msg, Result result, String code, String detailMsg) {
        this.success = success;
        this.msg = msg;
        this.result = result;
        this.code = code;
        this.detailMsg = detailMsg;
    }

    //Getter e setter

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

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
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
        return "IdtResponse{" +
                "success=" + success +
                ", msg='" + msg + '\'' +
                ", result=" + result +
                ", code='" + code + '\'' +
                ", detailMsg='" + detailMsg + '\'' +
                '}';
    }
}

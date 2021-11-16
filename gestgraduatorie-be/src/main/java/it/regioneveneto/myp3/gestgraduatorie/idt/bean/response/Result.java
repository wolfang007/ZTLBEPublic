package it.regioneveneto.myp3.gestgraduatorie.idt.bean.response;

public class Result {
    private Integer idFeature;

    public Result() {
    }

    public Result(Integer idFeature) {
        this.idFeature = idFeature;
    }

    //Getter and setter

    public Integer getIdFeature() {
        return idFeature;
    }

    public void setIdFeature(Integer idFeature) {
        this.idFeature = idFeature;
    }

    @Override
    public String toString() {
        return "Result{" +
                "idFeature=" + idFeature +
                '}';
    }
}

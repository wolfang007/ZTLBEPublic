package it.regioneveneto.myp3.gestgraduatorie.idt.bean.request;

public class FeatureProperties {

    private String desc;
    private Double ele;
    private String ele_string;
    private Integer id;
    private String name;
    private String simbologia;
    private String time;

    //Getter and setter


    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Double getEle() {
        return ele;
    }

    public void setEle(Double ele) {
        this.ele = ele;
    }

    public String getEle_string() {
        return ele_string;
    }

    public void setEle_string(String ele_string) {
        this.ele_string = ele_string;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSimbologia() {
        return simbologia;
    }

    public void setSimbologia(String simbologia) {
        this.simbologia = simbologia;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Properties{" +
                "desc='" + desc + '\'' +
                ", ele=" + ele +
                ", ele_string='" + ele_string + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", simbologia='" + simbologia + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}

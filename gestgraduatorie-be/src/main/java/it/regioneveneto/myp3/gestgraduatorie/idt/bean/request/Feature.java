package it.regioneveneto.myp3.gestgraduatorie.idt.bean.request;

public class Feature {

    private Geometry geometry;
    private FeatureProperties properties;
    private String type;

    //Getter and setter


    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public FeatureProperties getProperties() {
        return properties;
    }

    public void setProperties(FeatureProperties properties) {
        this.properties = properties;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Feature{" +
                "geometry=" + geometry +
                ", properties=" + properties +
                ", type='" + type + '\'' +
                '}';
    }
}

package it.regioneveneto.myp3.gestgraduatorie.idt.bean.request;

import java.io.Serializable;

public class IdtRequest implements Serializable {

    private Feature feature;
    private String layer;

    //Getter and setter

    public Feature getFeature() {
        return feature;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }

    public String getLayer() {
        return layer;
    }

    public void setLayer(String layer) {
        this.layer = layer;
    }

    @Override
    public String toString() {
        return "IdtRequest{" +
                "feature=" + feature +
                ", layer='" + layer + '\'' +
                '}';
    }
}

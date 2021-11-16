package it.regioneveneto.myp3.gestgraduatorie.idt.bean.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Geometry {

    private Double[][]  coordinates;
    
    private String type;


    //Getter and setter
    public Double[][]  getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Double[][] coordinates) {
        this.coordinates = coordinates;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Geometry{" +
                "coordinates=" + coordinates +
                ", type='" + type + '\'' +
                '}';
    }
}

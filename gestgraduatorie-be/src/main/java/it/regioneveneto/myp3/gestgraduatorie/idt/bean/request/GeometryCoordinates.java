package it.regioneveneto.myp3.gestgraduatorie.idt.bean.request;

public class GeometryCoordinates {

    private String coordX;
    private String coordY;


    public GeometryCoordinates(String coordX, String coordY) {
        this.coordX = coordX;
        this.coordY = coordY;
    }

    public String getCoordX() {
        return coordX;
    }

    public void setCoordX(String coordX) {
        this.coordX = coordX;
    }

    public String getCoordY() {
        return coordY;
    }

    public void setCoordY(String coordY) {
        this.coordY = coordY;
    }
}

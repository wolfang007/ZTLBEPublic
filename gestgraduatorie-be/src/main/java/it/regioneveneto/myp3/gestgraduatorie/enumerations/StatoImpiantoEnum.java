package it.regioneveneto.myp3.gestgraduatorie.enumerations;

public enum StatoImpiantoEnum {

    PREINS("PRE-INS", "Pre-inserito"),
    INS("INS", "Inserito"),
    VAL("VAL", "Validato"),
    NVAL("NVAL", "Non Validato"),
    DIS("DIS", "Dismesso"),
    DEL("DEL", "Eliminato");

    private String code;
    private String description;

    StatoImpiantoEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}

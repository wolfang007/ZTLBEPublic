package it.regioneveneto.myp3.gestgraduatorie.enumerations;

public enum TipoOperazioneEnum {

    INS("INS", "Inserimento"),
    MOD("MOD", "Modifica"),
    DEL("DEL", "Eliminazione");

    private String code;
    private String description;

    TipoOperazioneEnum(String code, String description) {
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

package it.regioneveneto.myp3.gestgraduatorie.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bean {
	private String idBando;
	private String idRichiesta;
    private String bando;
    private String num_protocollo;
    private String nome;
    private String cognome;
    private String codice_fiscale;
    private String punteggio;
}

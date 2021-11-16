package it.regioneveneto.myp3.gestgraduatorie.web.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EsitoServizio implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String codiceEsito;
    private String descrizioneEsito;


}

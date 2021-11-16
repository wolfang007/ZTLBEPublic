package it.regioneveneto.myp3.gestgraduatorie.web.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComuneDTO implements Serializable {

	private static final long serialVersionUID = 709219073538674313L;
	
	private Long id;
	private String siglaProvincia;
	private String codiceIstat;
	private String codiceBelfiore;
	private String denominazione;
	private Long idProvincia;
	private String cap;
	

	@Override
	public String toString() {
		return String.format(
				"ComuneDTO [id=%s, siglaProvincia=%s, codiceIstat=%s, codiceBelfiore=%s, denominazione=%s, idProvincia=%s, cap=%s]",
				id, siglaProvincia, codiceIstat, codiceBelfiore, denominazione, idProvincia, cap);
	}

}

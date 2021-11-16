package it.regioneveneto.myp3.gestgraduatorie.web.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProvinciaDTO implements Serializable {

	private static final long serialVersionUID = 3967784894439224513L;

	private Long id;
	private String codiceIstat;
	private String sigla;
	private String denominazione;
	private Long idRegione;


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ProvinciaDTO [");
		if (id != null)
			builder.append("id=").append(id).append(", ");
		if (codiceIstat != null)
			builder.append("codiceIstat=").append(codiceIstat).append(", ");
		if (sigla != null)
			builder.append("sigla=").append(sigla).append(", ");
		if (denominazione != null)
			builder.append("denominazione=").append(denominazione).append(", ");
		if (idRegione != null)
			builder.append("idRegione=").append(idRegione);
		builder.append("]");
		return builder.toString();
	}
	
}

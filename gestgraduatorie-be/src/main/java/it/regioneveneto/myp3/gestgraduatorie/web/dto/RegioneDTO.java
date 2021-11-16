package it.regioneveneto.myp3.gestgraduatorie.web.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegioneDTO implements Serializable {
	
	private static final long serialVersionUID = -420087508248042878L;
	
	private Long id;
	private String denominazione;
	private String codiceIstat;
	private String codiceSsn;
	private String codiceBelfiore;
	
	

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RegioneDTO [");
		if (id != null)
			builder.append("id=").append(id).append(", ");
		if (denominazione != null)
			builder.append("denominazione=").append(denominazione).append(", ");
		if (codiceIstat != null)
			builder.append("codiceIstat=").append(codiceIstat).append(", ");
		if (codiceSsn != null)
			builder.append("codiceSsn=").append(codiceSsn).append(", ");
		if (codiceBelfiore != null)
			builder.append("codiceBelfiore=").append(codiceBelfiore);
		builder.append("]");
		return builder.toString();
	}

	
}

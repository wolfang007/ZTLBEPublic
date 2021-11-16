package it.regioneveneto.myp3.gestgraduatorie.web.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuditUploadDTO  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Long id;
	String nomeUtente;
	String idFile;
	Date dataCaricamento;
	Long idBando;
	String esito;
	
}

package it.regioneveneto.myp3.gestgraduatorie.web.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AclDto {
	private String acl;
	private String ente;
	private List<EnteDTO> enti;
	private String nome;
	private String cognome;
	private String codiceFiscale;
}

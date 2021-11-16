package it.regioneveneto.myp3.gestgraduatorie.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class JwtDto {
	String cf;
	AclDto acl;
	String nome;
	String cognome;
}

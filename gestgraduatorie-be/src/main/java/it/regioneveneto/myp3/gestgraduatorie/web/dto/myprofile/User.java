package it.regioneveneto.myp3.gestgraduatorie.web.dto.myprofile;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class User {

	private Long userId;
	private String userCode;
	private String userSurname;
	private String userName;
	private String userTaxCode;
	private String userDomain;
	private Set<UserAttribute> attributes = new HashSet<UserAttribute>(0);

	@JsonIgnore
	private Integer version;

}

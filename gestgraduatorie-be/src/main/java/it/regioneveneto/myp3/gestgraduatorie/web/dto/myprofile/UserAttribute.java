package it.regioneveneto.myp3.gestgraduatorie.web.dto.myprofile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class UserAttribute {

	private Long attributeId;
	private String attributeName;
	private String attributeValue;
	private User user;
	@JsonIgnore
	private Integer version;

}

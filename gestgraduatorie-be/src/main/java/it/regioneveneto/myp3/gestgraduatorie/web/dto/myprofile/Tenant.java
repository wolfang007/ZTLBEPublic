package it.regioneveneto.myp3.gestgraduatorie.web.dto.myprofile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class Tenant {

	private Long tenantId;
	private String tenantCode;
	private String description;
	@JsonIgnore
	private Integer version;
}

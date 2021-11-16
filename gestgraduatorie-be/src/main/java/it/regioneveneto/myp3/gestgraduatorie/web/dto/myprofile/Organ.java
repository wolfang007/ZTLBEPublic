package it.regioneveneto.myp3.gestgraduatorie.web.dto.myprofile;

import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class Organ {

	private Long organId;
	private String nodeCode;
	private String description;
	private Tenant tenant;
	private Set<OrganAttribute> attributes = new HashSet<OrganAttribute>(0);
	@JsonIgnore
	private Integer version;
}

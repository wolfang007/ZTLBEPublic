package it.regioneveneto.myp3.gestgraduatorie.web.dto.myprofile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class OrganAttribute {

	private Long attributeId;
	private String attributeName;
	private String attributeValue;
	private Organ organ;
	@JsonIgnore
	private Integer version;

}

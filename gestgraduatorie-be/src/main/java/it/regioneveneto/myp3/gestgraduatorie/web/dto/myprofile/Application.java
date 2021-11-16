package it.regioneveneto.myp3.gestgraduatorie.web.dto.myprofile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class Application {

	private Long applId;
	private String applCode;
	private String description;
	@JsonIgnore
	private Integer version;
}

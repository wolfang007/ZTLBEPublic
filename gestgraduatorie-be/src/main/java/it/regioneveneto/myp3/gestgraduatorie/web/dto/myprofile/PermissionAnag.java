package it.regioneveneto.myp3.gestgraduatorie.web.dto.myprofile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class PermissionAnag {

	private Long permissionAnagId;
	private String permission;
	private String permissionDesc;
	private Role role;
	@JsonIgnore
	private Integer version;

}

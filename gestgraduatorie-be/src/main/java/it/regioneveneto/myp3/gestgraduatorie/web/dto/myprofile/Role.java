package it.regioneveneto.myp3.gestgraduatorie.web.dto.myprofile;

import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class Role {

	private Long roleId;
	private String roleName;
	private String description;
	private String acl;
	private Application application;
	private Set<PermissionAnag> permissions = new HashSet<PermissionAnag>(0);

	@JsonIgnore
	private Integer version;

}

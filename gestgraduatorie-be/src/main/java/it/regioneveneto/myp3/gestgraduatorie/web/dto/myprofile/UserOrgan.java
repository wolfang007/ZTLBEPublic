package it.regioneveneto.myp3.gestgraduatorie.web.dto.myprofile;

import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class UserOrgan {

	private Long userOrganlId;
	private TenantUser tenantUser;
	private Organ organ;
	private Set<UserOrganRole> roles = new HashSet<UserOrganRole>(0);
	@JsonIgnore
	private Integer version;

}

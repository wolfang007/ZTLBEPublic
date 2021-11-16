package it.regioneveneto.myp3.gestgraduatorie.web.dto.myprofile;

import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class Permission implements CreateAndUpdateTimestamp {

	private Long permissionId;
	private PermissionAnag permission;
	private String resource;
	private TenantApplUserRole tenantappluserRole;
	private GroupRole groupRole;
	private UserOrganRole userorganRole;
	@JsonIgnore
	private Integer version;
	private Timestamp creationTimestamp;
	private Timestamp updateTimestamp;

}

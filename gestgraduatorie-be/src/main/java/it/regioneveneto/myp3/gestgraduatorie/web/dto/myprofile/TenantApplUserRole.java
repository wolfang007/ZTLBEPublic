package it.regioneveneto.myp3.gestgraduatorie.web.dto.myprofile;

import java.sql.Timestamp;
import java.util.LinkedHashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.Data;

@Data
public class TenantApplUserRole implements CreateAndUpdateTimestamp {
	private static final Logger log = LoggerFactory.getLogger(TenantApplUserRole.class);
	private Long tenantApplUserRoleId;
	private TenantapplUser tenantapplUser;
	private Role role;
	private Set<Permission> permissions = new LinkedHashSet<Permission>(0);
	private Timestamp creationTimestamp;
	private Timestamp updateTimestamp;

}

package it.regioneveneto.myp3.gestgraduatorie.web.dto.myprofile;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class TenantapplUser implements CreateAndUpdateTimestamp {
	private static final Logger log = LoggerFactory.getLogger(TenantapplUser.class);

	private Long tenantapplUserId;
	private TenantAppl tenantappl;
	private TenantUser tenantUser;
	private Set<TenantApplUserRole> roles = new HashSet<TenantApplUserRole>(0);

	@JsonIgnore
	private Integer version;
	private Timestamp creationTimestamp;
	private Timestamp updateTimestamp;

}

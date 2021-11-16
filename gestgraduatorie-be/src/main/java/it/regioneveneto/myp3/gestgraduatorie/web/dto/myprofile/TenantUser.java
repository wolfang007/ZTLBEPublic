package it.regioneveneto.myp3.gestgraduatorie.web.dto.myprofile;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class TenantUser {
	private static final Logger log = LoggerFactory.getLogger(TenantUser.class);
	private Long tenantUserId;
	private Tenant tenant;
	private User user;
	private Set<TenantapplUser> tenantAppls = new HashSet<TenantapplUser>(0);
	private Set<Group> groups = new HashSet<Group>(0);
	private Set<UserOrgan> userOrgans = new HashSet<UserOrgan>(0);
	@JsonIgnore
	private Integer version;

	protected TenantUser() {
	}

}

package it.regioneveneto.myp3.gestgraduatorie.web.dto.myprofile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class TenantAppl {
	private static final Logger log = LoggerFactory.getLogger(TenantAppl.class);
	private Long tenantApplId;
	private Tenant tenant;
	private Application appl;
	@JsonIgnore
	private Integer version;

}

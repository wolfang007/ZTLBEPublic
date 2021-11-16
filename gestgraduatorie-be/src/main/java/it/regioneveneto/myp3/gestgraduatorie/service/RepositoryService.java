package it.regioneveneto.myp3.gestgraduatorie.service;

import java.util.List;

import it.regioneveneto.myp3.gestgraduatorie.web.dto.AclDto;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.EnteDTO;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.myprofile.Organ;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.myprofile.TenantUser;

public interface RepositoryService {

	TenantUser getTenantUserFromRepository(String userCode, String tenantCode);

	List<Organ> getOrgansFromRepository(String tenantCode);

//	List<EnteDTO> getEnteFromRepository(String tenantCode);

	AclDto getAclForTenantUserFromRepository(String codiceFiscale, String tenantRegionale);
	
	List<EnteDTO> getEntiFromRepository(String codiceFiscale);
}

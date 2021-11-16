package it.regioneveneto.myp3.gestgraduatorie.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import it.regioneveneto.myp3.gestgraduatorie.service.RepositoryService;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.AclDto;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.EnteDTO;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.myprofile.Group;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.myprofile.GroupRole;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.myprofile.Organ;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.myprofile.TenantUser;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.myprofile.UserOrgan;

@Service
public class RepositoryServiceImpl implements RepositoryService {
    private static final Logger LOG = LoggerFactory.getLogger(RepositoryServiceImpl.class);

    @Value("${gestgraduatorie.myprofile.host}")
    private String myprofileHost;
        
    @Value("${gestgraduatorie.myprofile.port}")
    private String myprofilePort;
        
    @Value("${gestgraduatorie.myprofile.baseUri}")
    private String myprofileBaseuri;
        
    @Value("${gestgraduatorie.myprofile.apiOrgans}")
    private String myprofileApiOrgans;
    
    @Value("${gestgraduatorie.myprofile.apiProfile}")
    private String myprofileApiProfile;
        
    @Value("${gestgraduatorie.myprofile.filtroOrgans}")
    private String myprofileFiltroOrgans;
        
    @Value("${gestgraduatorie.myprofile.filtroAcl}")
    private String myprofileFiltroAcl;

    @Value("${gestgraduatorie.myprofile.apiTenants}")
    private String myprofileApiTenants;    
    
    @Autowired
    private RestTemplate restTemplate;

    private HttpHeaders header;
    private String baseUri;
    
	@PostConstruct
	public void initialize() {		
		header = new HttpHeaders();
		header.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		header.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		baseUri = new StringBuilder(myprofileHost).append(":").append(myprofilePort).append(myprofileBaseuri).toString();
	}

	@Override
	public TenantUser getTenantUserFromRepository(String userCode, String tenantCode) {
		HttpEntity<String> requestEntity = new HttpEntity<>(header);
		StringBuilder uri = new StringBuilder(baseUri).append(myprofileApiProfile).append("/{userCode}/{tenantCode}");
		
		ResponseEntity<TenantUser> ricercaTenant = restTemplate.exchange(uri.toString(), HttpMethod.GET, requestEntity, TenantUser.class, userCode, tenantCode);
		if( ricercaTenant.getStatusCode().is2xxSuccessful() ) {
			return ricercaTenant.getBody();
		} else {
			if(LOG.isWarnEnabled()) {
				LOG.warn("Nessun tenant trovato all'URI {}", uri.toString());
			}
			return null;
		}
	}
	
	private Optional<Set<Organ>> getOrgansFromRepositoryRemote(String tenantCode) {
		HttpEntity<String> requestEntity = new HttpEntity<>(header);
		StringBuilder uri = new StringBuilder(baseUri).append(myprofileApiOrgans).append("/{tenantCode}/");
		
	    ResponseEntity<Set<Organ>> ricercaOrgans = restTemplate.exchange(uri.toString(), HttpMethod.GET, requestEntity, 
	    	      new ParameterizedTypeReference<Set<Organ>>(){}, tenantCode);

		if( ricercaOrgans.getStatusCode().is2xxSuccessful() ) {
			Set<Organ> organs = new TreeSet<Organ>(Comparator.comparing(Organ::getNodeCode, Comparator.nullsFirst(Comparator.naturalOrder())));
			organs.addAll(ricercaOrgans.getBody());
			return Optional.of(organs);
		} else {
			if(LOG.isWarnEnabled()) {
				LOG.warn("Nessun organs trovato all'URI {}", uri.toString());
			}
			return Optional.empty();
		}
	}

	@Override
	public List<Organ> getOrgansFromRepository(String tenantCode) {
		Optional<Set<Organ>> organs = getOrgansFromRepositoryRemote(tenantCode);
		
		List<Organ> organList = organs.orElse(Collections.emptySet()).stream()
				.filter(organ -> organ.getNodeCode().startsWith(myprofileFiltroOrgans))
				.map(organ -> {
					organ.setNodeCode(organ.getNodeCode().substring(organ.getNodeCode().indexOf(myprofileFiltroOrgans) + myprofileFiltroOrgans.length()));
					return organ;
				})
				.collect(Collectors.toList());
		return organList;
	}
	/*
	@Override
	public List<EnteDTO> getEnteFromRepository(String tenantCode) {
		Optional<Set<Organ>> organs = getOrgansFromRepositoryRemote(tenantCode);
		
		List<EnteDTO> organList = organs.orElse(Collections.emptySet()).stream()
				.filter(organ -> organ.getNodeCode().startsWith(myprofileFiltroOrgans))
				.map(organ -> {
					EnteDTO enteDTO = new EnteDTO();
					enteDTO.setAbilitato("N");
					enteDTO.setDescrizione(organ.getDescription());
					enteDTO.setOrganId(organ.getOrganId());
					enteDTO.setNodeCode(organ.getNodeCode().substring(organ.getNodeCode().indexOf(myprofileFiltroOrgans) + myprofileFiltroOrgans.length()));
					enteDTO.setIdEnte(null);
					return enteDTO;
				})
				.collect(Collectors.toList());
		
		return organList;

	}
*/
	@Override
	public AclDto getAclForTenantUserFromRepository(String codiceFiscale, String tenantRegionale) {
		AclDto aclDto = new AclDto();
		TenantUser tenantUser = getTenantUserFromRepository(codiceFiscale, tenantRegionale);
		if (tenantUser == null) {
			return null;
		}
		
		if (tenantUser.getGroups() == null) {
			return null;
		}
		
		for (Group group : tenantUser.getGroups()) {
			if (group.getRoles() == null) {
				continue;
			}
			for (GroupRole groupRole : group.getRoles()) {
				if (groupRole.getRole() != null && groupRole.getRole().getAcl() != null) {
					if (groupRole.getRole().getAcl().startsWith(myprofileFiltroAcl)) {
						String acl = groupRole.getRole().getAcl().substring(groupRole.getRole().getAcl().indexOf(myprofileFiltroAcl) + myprofileFiltroAcl.length());
						aclDto.setAcl(acl);
						break;
					}
				}
			}
		}
		
		if (tenantUser.getUserOrgans() == null) {
			return null;
		}
		//FIXME per ora setto l'ente a mano, a regime ci sarà chiamata verso myprofile per recupero tenant
		aclDto.setEnte("A0001");
		
		for (UserOrgan userOrgan : tenantUser.getUserOrgans()) {
			if (userOrgan.getOrgan() == null) {
				continue;
			} else {
				String nodeCode = userOrgan.getOrgan().getNodeCode();
				if (nodeCode.startsWith(myprofileFiltroOrgans)) {
					aclDto.setEnte(nodeCode.substring(nodeCode.indexOf(myprofileFiltroOrgans) + myprofileFiltroOrgans.length()));
					break;
				}
			}
		}
		/*
		if (aclDto.getAcl() == null || ("eecl".equals(aclDto.getAcl()) && aclDto.getEnte() == null)) {
			return null;
		}
*/
		aclDto.setEnte(tenantRegionale);
		
		return aclDto;
	}

	@Override
	public List<EnteDTO> getEntiFromRepository(String codiceFiscale) {
		HttpEntity<String> requestEntity = new HttpEntity<>(header);
		StringBuilder uri = new StringBuilder(baseUri).append(myprofileApiTenants).append("/{application_code}").append("/{user_code}/");
		
		ResponseEntity<List<EnteDTO>> enti = restTemplate.exchange(uri.toString(), HttpMethod.GET, requestEntity, 
				new ParameterizedTypeReference<List<EnteDTO>>(){}, "MYP3",codiceFiscale);
		if( enti.getStatusCode().is2xxSuccessful() ) {
			return enti.getBody();
		} else {
			if(LOG.isWarnEnabled()) {
				LOG.warn("Nessun tenant trovato all'URI {}", uri.toString());
			}
			return null;
		}
	}

}

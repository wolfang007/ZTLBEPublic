package it.regioneveneto.myp3.gestgraduatorie.service;

import java.util.List;
import java.util.Optional;

import it.regioneveneto.myp3.gestgraduatorie.web.dto.ComuneDTO;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.ProvinciaDTO;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.RegioneDTO;

public interface ProvinceRegioniService {
	
	List<RegioneDTO> findAllRegioni( ) throws Exception;
	
	List<ProvinciaDTO> findProvinceByIdRegione( Long idRegione ) throws Exception;
	
	List<ProvinciaDTO> findProvinceByDenominazione( String denominazione ) throws Exception;

	List<ProvinciaDTO> searchProvince( Long idRegione, String denominazione ) throws Exception;
	
	List<ComuneDTO> findComuniByIdProvincia( Long idProvincia ) throws Exception;
	
	List<ComuneDTO> findComuniByDenominazione( String denominazione ) throws Exception;
	
	List<ComuneDTO> searchComuni( Long idProvincia, String denominazione ) throws Exception;
	
	Optional<ComuneDTO> findComuneByCAP( String cap ) throws Exception;
	
	boolean existsComuneByCap( String cap ) throws Exception;
	
	List<ComuneDTO> findComuniByDenominazioneProvincia(String denominazioneProvincia) throws Exception;

	List<ComuneDTO> findComuniByIdProvinciaOrIdRegione(Long idProvincia, Long idRegione) throws Exception;

}

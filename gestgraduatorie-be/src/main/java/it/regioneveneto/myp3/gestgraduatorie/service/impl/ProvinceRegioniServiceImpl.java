package it.regioneveneto.myp3.gestgraduatorie.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import it.regioneveneto.myp3.gestgraduatorie.converter.ComuneConverter;
import it.regioneveneto.myp3.gestgraduatorie.converter.ProvinciaConverter;
import it.regioneveneto.myp3.gestgraduatorie.converter.RegioneConverter;
import it.regioneveneto.myp3.gestgraduatorie.repository.ComuneRepository;
import it.regioneveneto.myp3.gestgraduatorie.repository.ProvinciaRepository;
import it.regioneveneto.myp3.gestgraduatorie.repository.RegioneRepository;
import it.regioneveneto.myp3.gestgraduatorie.service.ProvinceRegioniService;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.ComuneDTO;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.ProvinciaDTO;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.RegioneDTO;

@Service
public class ProvinceRegioniServiceImpl implements ProvinceRegioniService {
	
	@Autowired
	private RegioneRepository regioneRepository;
	
	@Autowired
	private ProvinciaRepository provinciaRepository;
	
	@Autowired
	private ComuneRepository comuneRepository;
	
	@Autowired
	private RegioneConverter regioneConverter;
	
	@Autowired
	private ProvinciaConverter provinciaConverter;

	@Autowired
	private ComuneConverter comuneConverter;
	
	private static final String REGIONI_DEFAULT_SORT_PROPERTY = "denominazione";
	private static final String PROVINCE_DEFAULT_SORT_PROPERTY = "denominazione";
	private static final String COMUNI_DEFAULT_SORT_PROPERTY = "denominazione";
	
	
	@Override
	@Transactional( rollbackFor = Exception.class, readOnly = true )
	@Cacheable( value = "findAllRegioni", sync = true )
	public List<RegioneDTO> findAllRegioni() throws Exception {
		return 
				this.regioneConverter.toDTO(
						this.regioneRepository.findAll( 
								Sort.by(
										Sort.Direction.ASC, 
										REGIONI_DEFAULT_SORT_PROPERTY)));
								
						
	}

	
	@Override
	@Transactional( rollbackFor = Exception.class, readOnly = true )
	@Cacheable( value = "findProvinceByIdRegione", key = "#idRegione", sync = true )
	public List<ProvinciaDTO> findProvinceByIdRegione(Long idRegione) throws Exception {
		return
				this.provinciaConverter.toDTO(
						this.provinciaRepository.findByRegione_Id(
								idRegione, 
								Sort.by(
										Sort.Direction.ASC,
										PROVINCE_DEFAULT_SORT_PROPERTY
										)
								)
						);
	}
	

	@Override
	@Transactional( rollbackFor = Exception.class, readOnly = true )
	@Cacheable( value = "findComuniByIdProvincia", key = "#idProvincia", sync = true )
	public List<ComuneDTO> findComuniByIdProvincia(Long idProvincia) throws Exception {
		return
				this.comuneConverter.toDTO(
						this.comuneRepository.findByProvincia_Id(
								idProvincia, 
								Sort.by(
										Sort.Direction.ASC,
										COMUNI_DEFAULT_SORT_PROPERTY
										)
								)
						);
	}

	
	@Override
	@Transactional( rollbackFor = Exception.class, readOnly = true )
	@Cacheable( value = "findProvinceByDenominazione", key = "#denominazione", sync = true )
	public List<ProvinciaDTO> findProvinceByDenominazione(String denominazione) throws Exception {
		return
				this.provinciaConverter.toDTO(
						this.provinciaRepository.findByDenominazioneContainingIgnoreCase(
								denominazione, 
								Sort.by(
										Sort.Direction.ASC,
										PROVINCE_DEFAULT_SORT_PROPERTY
										)
								)
						);
	}

	
	@Override
	@Transactional( rollbackFor = Exception.class, readOnly = true )
	@Cacheable( value = "findComuniByDenominazione", key = "#denominazione", sync = true )
	public List<ComuneDTO> findComuniByDenominazione(String denominazione) throws Exception {
		return
				this.comuneConverter.toDTO(
						this.comuneRepository.findByDenominazioneContainingIgnoreCase(
								denominazione, 
								Sort.by(
										Sort.Direction.ASC,
										COMUNI_DEFAULT_SORT_PROPERTY
										)
								)
						);
	}
	
	@Override
	@Transactional( rollbackFor = Exception.class, readOnly = true )
	@Cacheable( value = "findComuniByDenominazioneProvincia", key = "#denominazioneProvincia", sync = true )
	public List<ComuneDTO> findComuniByDenominazioneProvincia(String denominazioneProvincia) throws Exception {
		return
				this.comuneConverter.toDTO(
						this.comuneRepository.findByProvinciaDenominazioneEqualsIgnoreCase(
								denominazioneProvincia, 
								Sort.by(
										Sort.Direction.ASC,
										COMUNI_DEFAULT_SORT_PROPERTY
										)
								)
						);
	}

	
	@Override
	@Transactional( rollbackFor = Exception.class, readOnly = true )
	@Cacheable( value = "findComuneByCAP", key = "#cap", sync = true )
	public Optional<ComuneDTO> findComuneByCAP(String cap) throws Exception {
		return 
				this.comuneConverter.toDTO( 
						this.comuneRepository.findByListaCap_CapStartingWith( cap ) );
	}

	@Override
	@Transactional( rollbackFor = Exception.class, readOnly = true )
	public boolean existsComuneByCap(String cap) throws Exception {
		return comuneRepository.existsByListaCap_Cap( cap );
	}


	@Override
	@Transactional( rollbackFor = Exception.class, readOnly = true )
	@Cacheable( value = "searchProvince", sync = true )
	public List<ProvinciaDTO> searchProvince(final Long idRegione, final String denominazione) throws Exception {
		
		//tutte le province se senza filtro
		if( idRegione == null && !StringUtils.hasText(denominazione) ){
			return this.provinciaConverter.toDTO( this.provinciaRepository.findAll(Sort.by(Sort.Direction.ASC, PROVINCE_DEFAULT_SORT_PROPERTY) ) );
		}
		//altrimenti ricerco per entrambi o solo un filtro
		else if( idRegione != null && StringUtils.hasText( denominazione ) )
			return
					this.provinciaConverter.toDTO(
							this.provinciaRepository.findByRegione_IdAndDenominazioneContainingIgnoreCase(
									idRegione, 
									denominazione,
									Sort.by(
											Sort.Direction.ASC,
											PROVINCE_DEFAULT_SORT_PROPERTY
											)
									)
							);
		else if(idRegione != null)
			return
					this.provinciaConverter.toDTO(
							this.provinciaRepository.findByRegione_Id(
									idRegione, 
									Sort.by(
											Sort.Direction.ASC,
											PROVINCE_DEFAULT_SORT_PROPERTY
											)
									)
							);
		else
			return
					this.provinciaConverter.toDTO(
							this.provinciaRepository.findByDenominazioneContainingIgnoreCase(
									denominazione, 
									Sort.by(
											Sort.Direction.ASC,
											PROVINCE_DEFAULT_SORT_PROPERTY
											)
									)
							);
	}


	@Override
	@Transactional( rollbackFor = Exception.class, readOnly = true )
	@Cacheable( value = "searchComuni", sync = true )
	public List<ComuneDTO> searchComuni(Long idProvincia, String denominazione) throws Exception {
		
		//lista vuota se non ci stanno filtri
		if( idProvincia == null && !StringUtils.hasText( denominazione ) )
			return Collections.emptyList();
		//altrimenti ricerco per entrambi o solo un filtro
		else if( idProvincia != null && StringUtils.hasText( denominazione ) )
			return
					this.comuneConverter.toDTO(
							this.comuneRepository.findByProvincia_IdAndDenominazioneContainingIgnoreCase(
									idProvincia,
									denominazione, 
									Sort.by(
											Sort.Direction.ASC,
											COMUNI_DEFAULT_SORT_PROPERTY
											)
									)
							);
		else if( idProvincia != null )
			return
					this.comuneConverter.toDTO(
							this.comuneRepository.findByProvincia_Id(
									idProvincia, 
									Sort.by(
											Sort.Direction.ASC,
											COMUNI_DEFAULT_SORT_PROPERTY
											)
									)
							);
		else
			return
					this.comuneConverter.toDTO(
							this.comuneRepository.findByDenominazioneContainingIgnoreCase(
									denominazione, 
									Sort.by(
											Sort.Direction.ASC,
											COMUNI_DEFAULT_SORT_PROPERTY
											)
									)
							);
	}


	@Override
	@Transactional( rollbackFor = Exception.class, readOnly = true )
	@Cacheable( value = "findComuniByIdProvinciaOrIdRegione", key = "{#idProvincia, #idRegione}", sync = true )
	public List<ComuneDTO> findComuniByIdProvinciaOrIdRegione(Long idProvincia, Long idRegione) throws Exception {
		Sort sort = Sort.by(Sort.Direction.ASC, COMUNI_DEFAULT_SORT_PROPERTY);
		if(idProvincia == null && idRegione == null)
			return Collections.emptyList();
		else if(idProvincia != null && idRegione != null) {
			return 
					this.comuneConverter.toDTO( 
							this.comuneRepository.findByProvincia_Regione_IdAndProvincia_id(
									idRegione, idProvincia, sort
									)
							);
		} else if( idProvincia != null ) {
			return 
					this.comuneConverter.toDTO( 
							this.comuneRepository.findByProvincia_Id(
									idProvincia, sort
									)
							);
		} else if( idRegione != null ){
			return 
					this.comuneConverter.toDTO( 
							this.comuneRepository.findByProvincia_Regione_Id(
									idRegione, sort
									)
							);
		}
		return Collections.emptyList();
	}

}

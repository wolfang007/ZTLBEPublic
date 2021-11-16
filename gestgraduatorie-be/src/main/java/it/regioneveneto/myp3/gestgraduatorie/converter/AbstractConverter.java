package it.regioneveneto.myp3.gestgraduatorie.converter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import it.regioneveneto.myp3.gestgraduatorie.exception.ConverterException;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.PagedResult;




public abstract class AbstractConverter<MODEL, DTO> {
	
	private static final Logger LOG = LoggerFactory.getLogger(AbstractConverter.class);
	
//	@Autowired
//	protected TipologicheService tipologicheService;
	
	
	
	public abstract MODEL toModel(DTO dto, Map<String, Object> parameters) throws ConverterException;
	
	public abstract DTO toDTO(MODEL model, Map<String, Object> parameters) throws ConverterException;

	public DTO toDTO(MODEL model) throws ConverterException {
		return toDTO(model, Collections.emptyMap() );
	}
	
	public MODEL toModel(DTO dto) throws ConverterException {
		return toModel(dto, Collections.emptyMap() );
	}
	
	/**
	 * Converte un oggetto iterabile di MODEL nella corrispondente lista di DTO
	 * @param models � la lista iterabile di MODEL da convertire
	 * @return la lista dei DTO convertiti
	 * @throws ConverterException
	 */
	public List<DTO> toDTO( Iterable<MODEL> models ) {
		List<DTO> dtos = Collections.emptyList();
		if( models != null ){
			dtos = new ArrayList<>();
			for (MODEL model : models ) {
				dtos.add( this.toDTO( model ) );
			}
		}
		return dtos;
	}

	/**
	 * Converte un oggetto iterabile di DTO nella corrispondente lista di MODEL
	 * @param dtos � la lista iterabile di DTO da convertire
	 * @return la lista dei MODEL convertiti
	 * @throws ConverterException
	 */
	public List<MODEL> toModel( Iterable<DTO> dtos ) throws ConverterException{
		List<MODEL> models = Collections.emptyList();
		if( dtos != null ){
			models = new ArrayList<>( );
			for (DTO dto : dtos) {
				models.add( this.toModel(dto) );
			}
		}
		return models;
	}
	
	/**
	 * Converte una pagina di MODEL in una pagina di DTO.
	 * @param pageModels � la pagina di MODEL
	 * @return una pagina di DTO
	 * @throws ConverterException
	 */
	public Page<DTO> toDTO(Page<MODEL> pageModels) throws ConverterException { 
		Page<DTO> pageDTOs = new PageImpl<>( Collections.emptyList(), pageModels.getPageable(), 0);
		if(pageModels != null && pageModels.hasContent()){
			List<DTO> dtos = new ArrayList<>(pageModels.getContent().size());
			dtos = this.toDTO(pageModels.getContent());
			pageDTOs = new PageImpl<>(dtos, pageModels.getPageable(), pageModels.getTotalElements());
		}
		return pageDTOs;
	}
	
	/**
	 * Converte un {@link PagedResult} che contiene una pagina di MODEL in uno stesso oggetto ma con contenuto
	 * una pagina di DTO.
	 * @param pageModels � la pagina di MODEL
	 * @return una pagina di DTO
	 * @throws ConverterException
	 */
	public PagedResult<DTO> toDTO(PagedResult<MODEL> pageModels) throws ConverterException {
		PagedResult<DTO> pageDtos = new PagedResult<>();
		if(pageModels != null){
			pageDtos.setContent( pageModels.getContent() != null ? this.toDTO(pageModels.getContent()) : Collections.emptyList() );
			pageDtos.setFirst( pageModels.isFirst() );
			pageDtos.setLast( pageModels.isLast() );
			pageDtos.setNumber( pageModels.getNumber() );
			pageDtos.setNumberOfElements( pageModels.getNumberOfElements() );
			pageDtos.setSize( pageModels.getSize() );
			pageDtos.setTotalElements( pageModels.getTotalElements() );
			pageDtos.setTotalPages( pageModels.getTotalPages() );
		}
		return pageDtos;
	}
	
	/**
	 * Converter da optional model ad optional dto
	 * @param optional
	 * @return
	 */
	public Optional<DTO> toDTO( Optional<MODEL> optional ){
		return ( optional.isPresent() ) ? 
				Optional.of( this.toDTO( optional.get( ) ) )
					: Optional.empty();
	}
	
	/**
	 * Converte da optional dto ad optional model
	 * @param optional
	 * @return
	 */
	public Optional<MODEL> toModel( Optional<DTO> optional ){
		return ( optional.isPresent() ) ? 
				Optional.of( this.toModel( optional.get( ) ) )
					: Optional.empty();
	}
	
	/**
	 * Restituisce la label di una tipologica
	 */
//	protected String getLabel( String name, String category, String tenantCode ) {
//		String label = null;
//		try {
//			tenantCode = StringUtils.hasText(tenantCode) ? tenantCode : ConstantValues.C_DEFAULT;
//			return this.tipologicheService.getLabel(category, name, tenantCode);
//		} catch (Exception e) {
//			LOG.error("AbstractConverter --> Errore nel recupero della label.");
//			//do nothing
//		}
//		return label;
//	}

}
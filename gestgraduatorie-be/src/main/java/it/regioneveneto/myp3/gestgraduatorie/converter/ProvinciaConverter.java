package it.regioneveneto.myp3.gestgraduatorie.converter;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import it.regioneveneto.myp3.gestgraduatorie.exception.ConverterException;
import it.regioneveneto.myp3.gestgraduatorie.model.Provincia;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.ProvinciaDTO;


/**
 * Converter da {@link Provincia} a {@link ProvinciaDTO}.
 */
@Component
public class ProvinciaConverter extends AbstractConverter<Provincia, ProvinciaDTO>{
	
	private static final Logger LOG = LoggerFactory.getLogger(ProvinciaConverter.class);

	@Override
	public Provincia toModel(ProvinciaDTO dto,  Map<String, Object> parameters) throws ConverterException {
		
		LOG.error("ProvinciaConverter --> Conversione di una provincia da dto a model non supportata.");
		throw new UnsupportedOperationException( "Conversione di una provincia da dto a model non supportata." );
		
	}

	@Override
	public ProvinciaDTO toDTO(Provincia model,  Map<String, Object> parameters) throws ConverterException {
		
		ProvinciaDTO dto = new ProvinciaDTO();
		
		LOG.debug("ProvinciaConverter --> Conversione da dto a model.");
		if( model != null ){
			dto.setId( model.getId( ) );
			dto.setCodiceIstat( model.getCodiceIstat( ) );
			dto.setDenominazione( model.getDenominazione( ) );
			if(model.getRegione() != null)
				dto.setIdRegione( model.getRegione( ).getId( ) );
			dto.setSigla( model.getSigla( ) );
		}
		LOG.debug("ProvinciaConverter --> Conversione da dto a model avvenuta con successo. DTO: {}", dto.toString( ) );
		
		return dto;
		
	}

}

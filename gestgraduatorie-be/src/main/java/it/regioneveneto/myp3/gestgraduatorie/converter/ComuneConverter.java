package it.regioneveneto.myp3.gestgraduatorie.converter;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import it.regioneveneto.myp3.gestgraduatorie.exception.ConverterException;
import it.regioneveneto.myp3.gestgraduatorie.model.Comune;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.ComuneDTO;



/**
 * Converter da {@link Comune} a {@link ComuneDTO}.
 */
@Component
public class ComuneConverter extends AbstractConverter<Comune, ComuneDTO>{
	
	private static final Logger LOG = LoggerFactory.getLogger(ComuneConverter.class);

	@Override
	public Comune toModel(ComuneDTO dto, Map<String, Object> parameters) throws ConverterException {
		
		LOG.error("ComuneConverter --> Conversione di un comune da dto a model non supportata.");
		throw new UnsupportedOperationException( "Conversione di un comune da dto a model non supportata." );
		
	}

	@Override
	public ComuneDTO toDTO(Comune model, Map<String, Object> parameters) throws ConverterException {
		
		ComuneDTO dto = new ComuneDTO();
		
		LOG.debug("ComuneConverter --> Conversione da dto a model.");
		if( model != null ) {
			dto.setId( model.getId( ) );
			dto.setDenominazione( model.getDenominazione( ) );
			dto.setCodiceIstat( model.getCodiceIstat( ) );
			dto.setCodiceBelfiore( model.getCodiceBelfiore( ) );
			dto.setSiglaProvincia( model.getSiglaProvincia( ) );
			if( model.getProvincia( ) != null )
				dto.setIdProvincia( model.getProvincia( ).getId( ) );
//			if( model.getCap( ) != null )
//				dto.setCap( model.getCap().getCap( ) );
		}
		LOG.debug("ComuneConverter --> Conversione da dto a model avvenuta con successo. DTO: {}", dto.toString( ) );
		
		return dto;
		
	}
	
	

}

package it.regioneveneto.myp3.gestgraduatorie.converter;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import it.regioneveneto.myp3.gestgraduatorie.exception.ConverterException;
import it.regioneveneto.myp3.gestgraduatorie.model.Regione;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.RegioneDTO;


/**
 * Converter da {@link Regione} a {@link RegioneDTO}.
 */
@Component
public class RegioneConverter extends AbstractConverter<Regione, RegioneDTO>{

	private static final Logger LOG = LoggerFactory.getLogger(RegioneConverter.class);
	
	@Override
	public Regione toModel(RegioneDTO dto, Map<String, Object> parameters) throws ConverterException {
		
		LOG.error("RegioneConverter --> Conversione di una regione da dto a model non supportata.");
		throw new UnsupportedOperationException( "Conversione di una regione da dto a model non supportata." );
		
	}

	@Override
	public RegioneDTO toDTO(Regione model, Map<String, Object> parameters) throws ConverterException {
		
		RegioneDTO dto = new RegioneDTO();
		
		LOG.debug("RegioneConverter --> Conversione da dto a model.");
		if(model != null){
			dto.setId( model.getId() );
			dto.setCodiceBelfiore( model.getCodiceBelfiore( ) );
			dto.setCodiceIstat( model.getCodiceIstat( ) );
			dto.setCodiceSsn( model.getCodiceSsn( ) );
			dto.setDenominazione( model.getDenominazione( ) );
		}
		LOG.debug("RegioneConverter --> Conversione da dto a model avvenuta con successo. DTO: {}", dto.toString( ) );
		
		return dto;
		
	}


}

package it.regioneveneto.myp3.gestgraduatorie.service;

import java.util.Collection;

import it.regioneveneto.myp3.gestgraduatorie.web.dto.Bean;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.IstanzaFilterDTO;



public interface ReportService {


	byte[] getElencoIstanze(IstanzaFilterDTO filterRichiesta, String ente, String report) throws Exception;

	Collection<Bean> getElencoDtoIstanze(IstanzaFilterDTO filterRichiesta, String ente) throws Exception;

}

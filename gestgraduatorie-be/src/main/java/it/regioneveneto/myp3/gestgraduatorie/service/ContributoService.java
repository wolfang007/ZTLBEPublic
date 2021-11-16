package it.regioneveneto.myp3.gestgraduatorie.service;



import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.regioneveneto.myp3.gestgraduatorie.exception.ServiceException;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.AuditUploadDTO;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.BandoDTO;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.BandoFilterDTO;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.BeneficiarioDTO;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.IstanzaDTO;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.IstanzaFilterDTO;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.RichiestaDTO;

public interface ContributoService {

	RichiestaDTO createRichiestaContributo(String richiesta) throws ServiceException;

	Page<BandoDTO> getBandi(BandoFilterDTO filterBando, String ente, Pageable pageable) throws Exception;

	Page<IstanzaDTO> getRichieste(IstanzaFilterDTO filterRichiesta, String ente, Pageable pageable) throws Exception;	

	IstanzaDTO getRichiestaById(long id, String ente) throws Exception;
	

	
	int aggiornaRichiestaMassiva(String punteggio,Double idRichiesta,Double idBando) throws ServiceException;

	Boolean verificaBandoDaChiudere(long id,String ente) throws ServiceException;
	
	Boolean verificaBandoChiuso(long id,String ente) throws ServiceException;

	int chiudiBando(long id, String ente) throws ServiceException;
	//MS
	RichiestaDTO createRichiesta(RichiestaDTO richiestaDTO) throws ServiceException;
	
	Optional<Integer> getIdBandoByCodeIdentify(String codiceBando)throws ServiceException;
	
	BandoDTO insertBando(BandoDTO bandoDTO) throws ServiceException;
	
	BeneficiarioDTO insertBeneficiario(BeneficiarioDTO beneficiarioDTO) throws ServiceException;
	
	AuditUploadDTO insertAuditUpload(AuditUploadDTO auditUploadDTO) throws ServiceException;
	
	Optional<Integer> verificaBeneficiario(Long idBando,String codiceFiscale) throws ServiceException;
 
}

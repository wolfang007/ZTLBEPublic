package it.regioneveneto.myp3.gestgraduatorie.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import it.regioneveneto.myp3.gestgraduatorie.model.Comune;

public interface ComuneRepository extends PagingAndSortingRepository<Comune, Long>{
	
	List<Comune> findByProvincia_Id( Long idProvincia, Sort sort );
	
	List<Comune> findByProvincia_Regione_Id( Long idRegione, Sort sort );
	
	List<Comune> findByProvincia_Regione_IdAndProvincia_id( Long idRegione, Long idProvincia, Sort sort );
	
	List<Comune> findByDenominazioneContainingIgnoreCase( String denominazione, Sort sort );
	
	List<Comune> findByDenominazioneIgnoreCase( String denominazione );
	
	List<Comune> findByProvinciaDenominazioneEqualsIgnoreCase( String denominazione, Sort sort );
	
	List<Comune> findByProvincia_IdAndDenominazioneContainingIgnoreCase( Long idProvincia, String denominazione, Sort sort );
	
	Optional<Comune> findByListaCap_CapStartingWith( String cap );
	
	boolean existsByListaCap_Cap( String cap );
	
	Optional<Comune> findByCodiceBelfiore( String codiceBelfiore );
	
	Optional<Comune> findByCodiceIstat( String codiceIstat );
	
}
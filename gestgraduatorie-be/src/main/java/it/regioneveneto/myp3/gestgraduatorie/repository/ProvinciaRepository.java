package it.regioneveneto.myp3.gestgraduatorie.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import it.regioneveneto.myp3.gestgraduatorie.model.Provincia;

public interface ProvinciaRepository extends PagingAndSortingRepository<Provincia, Long>{
	
	List<Provincia> findByRegione_Id( Long idRegione, Sort sort );
	
	List<Provincia> findByRegione_IdAndDenominazioneContainingIgnoreCase( Long idRegione, String denominazione, Sort sort );

	List<Provincia> findByDenominazioneContainingIgnoreCase( String denominazione, Sort sort );
	
}
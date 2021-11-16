package it.regioneveneto.myp3.gestgraduatorie.configuration;

import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import it.regioneveneto.myp3.gestgraduatorie.repository.ServiceRepository;

@Component
public class JdbiDAOFactory {
	
	@Autowired
	Jdbi jdbi;

	@Bean
	public ServiceRepository serviceRepository(){        
	    return jdbi.onDemand(ServiceRepository.class);
	}
	
}

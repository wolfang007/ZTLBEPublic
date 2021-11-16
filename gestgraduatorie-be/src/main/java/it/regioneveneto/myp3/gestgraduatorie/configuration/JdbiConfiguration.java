package it.regioneveneto.myp3.gestgraduatorie.configuration;


import java.util.List;

import javax.sql.DataSource;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.spi.JdbiPlugin;
import org.jdbi.v3.core.statement.SqlLogger;
import org.jdbi.v3.core.statement.StatementContext;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

@Configuration
public class JdbiConfiguration {
	private static final Logger LOG = LoggerFactory.getLogger(JdbiConfiguration.class);
	
    @Bean
    public Jdbi jdbi(DataSource ds,List<JdbiPlugin> jdbiPlugins, List<RowMapper<?>> rowMappers) {        
        TransactionAwareDataSourceProxy proxy = new TransactionAwareDataSourceProxy(ds);        
        Jdbi jdbi = Jdbi.create(proxy);
        
        jdbiPlugins.forEach(plugin -> jdbi.installPlugin(plugin));
        rowMappers.forEach(mapper -> jdbi.registerRowMapper(mapper));
        
        //TODO: probabilmente occerrerà valutare una disabilitazione a seconda dell'ambiente di deploy
        jdbi.setSqlLogger(new SqlLogger() {
        	@Override
            public void logAfterExecution(StatementContext context) {
        		LOG.info("{}", context.getRenderedSql());
        	}
		});
       // jdbi.registerRowMapper(Evento.class, ConstructorMapper.of(Evento.class));
        
        return jdbi;
    }
    
    @Bean
    public JdbiPlugin sqlObjectPlugin() {
        return new SqlObjectPlugin();
    }
    
   
 }
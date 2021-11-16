package it.regioneveneto.myp3.gestgraduatorie.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import it.regioneveneto.myp3.gestgraduatorie.jwt.JwtAuthenticationFilter;
import it.regioneveneto.myp3.gestgraduatorie.jwt.JwtAuthenticationProvider;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	

    
    /**
     * Rest security configuration for /api/
     */
    @Configuration
    @Order(1)
    public static class RestApiSecurityConfig extends WebSecurityConfigurerAdapter {
    	
        @Value("${gestgraduatorie.auth.jwt-secret}")
    	String jwtSecret;

        
        @Value("${gestgraduatorie.auth.allowed-origin-host}")
        String[] allowedOriginHost;

        private static final String apiMatcher = "/api/**";
        
        	//TODO: Verificare se c'e' un metodo piu' organico
	        @Override
	        public void configure(WebSecurity web) throws Exception {
	        	web.ignoring().antMatchers(HttpMethod.OPTIONS, "/api/**");
	        	//web.ignoring().antMatchers("/api/**/*");
	        	web.ignoring().antMatchers("/open/**");
	        	
	        }
	        
            @Override
            protected void configure(HttpSecurity http) throws Exception {
            	
                http.addFilterBefore(new JwtAuthenticationFilter(apiMatcher, super.authenticationManager()), UsernamePasswordAuthenticationFilter.class);
                
                http	.cors().and().csrf()
		                .ignoringAntMatchers("/saml/**")
		                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());


                http.authorizeRequests()
		                .antMatchers("/hd/**")
		                .permitAll()
		                .antMatchers(apiMatcher)
		                .authenticated().and()
		                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            }


            @Override
            protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            	
                auth.authenticationProvider(new JwtAuthenticationProvider(jwtSecret));
            }
            
            @Bean
            public CorsFilter corsFilter() {
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowCredentials(true);
            List<String> allowedOrigins = new ArrayList<String>();
            allowedOrigins.addAll(Arrays.asList(allowedOriginHost));
            config.setAllowedOrigins(allowedOrigins);
            config.addAllowedHeader("*");
            config.addAllowedMethod("*");
            source.registerCorsConfiguration("/api/**", config);
            return new CorsFilter(source);
            }
            
            /*
        	@Bean
        	CorsConfigurationSource corsConfigurationSource() {

        		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    			CorsConfiguration configuration = new CorsConfiguration();
    			configuration.setAllowCredentials(true);
    			configuration.addAllowedOrigin("*");
    			configuration.addAllowedHeader("*");
    			configuration.addAllowedMethod("*");
    			source.registerCorsConfiguration("/**", configuration);
        		return source;
        	}
        	*/
    }

    
    
    
//    /**
//     * Rest security configuration for /auth/token
//     */
//    @Configuration
//    @Order(2)
//    public static class AuthSecurityConfig extends WebSecurityConfigurerAdapter {
//
//        private static final String apiMatcher = "/auth/login";
//
//        @Override
//        protected void configure(HttpSecurity http) throws Exception {
//
//            http
//                    .exceptionHandling()
//                    .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.FORBIDDEN));
//
//            http.antMatcher(apiMatcher).authorizeRequests()
//                    .anyRequest().authenticated();
//        }
//    }

    /**
     * Saml security config
     */
    @Configuration
    @Import(WebSecurityConfig.class)
    public static class SamlConfig {

    }

}

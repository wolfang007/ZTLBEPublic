package it.regioneveneto.myp3.gestgraduatorie.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    public final String HEADER_SECURITY_TOKEN = "x-auth-token";

    public JwtAuthenticationFilter(final String matcher, AuthenticationManager authenticationManager) {
        super(matcher);
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        //final String token = request.getHeader(HEADER_SECURITY_TOKEN);
    	String token = null;
    	if (request.getCookies() != null) {
	        for (Cookie cc : request.getCookies()) {
	        	if ("gestgraduatorie_access_token".equals(cc.getName())) {
	        		token = cc.getValue();
	        	}
	        }
    	}
        JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(token);
    	
        log.debug("attemptAuthentication");

        return getAuthenticationManager().authenticate(jwtAuthenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {

        SecurityContextHolder.getContext().setAuthentication(authResult);

         log.debug("Authentication Result" + authResult);
         log.debug("Authentication Result Name" + authResult.getName());
         log.debug("Authentication Result Autorithies size" + authResult.getAuthorities().size());
         log.debug("Authentication Result Details" + authResult.getDetails());
         log.debug("Authentication Result Principal" + authResult.getPrincipal());
         log.debug("Authentication Result is auth" + authResult.isAuthenticated());

        log.debug("successfulAuthentication");

        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {

        log.debug("unsuccessfulAuthentication");

        SecurityContextHolder.clearContext();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    }
}

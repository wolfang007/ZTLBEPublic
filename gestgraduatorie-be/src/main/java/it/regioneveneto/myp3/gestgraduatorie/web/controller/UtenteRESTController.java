package it.regioneveneto.myp3.gestgraduatorie.web.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import io.swagger.annotations.Api;
import it.regioneveneto.myp3.gestgraduatorie.service.RepositoryService;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.AclDto;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.EnteDTO;
import net.minidev.json.JSONObject;

@RestController
@RequestMapping("/api/utente")
@Api(value= "/utente", tags="tag_api_utente")
public class UtenteRESTController {

    @Autowired
    RepositoryService repositoryService;
    @Value("${gestgraduatorie.auth.jwt-secret}")
    private String jwtSecret;
    @Value("${gestgraduatorie.auth.access-token-secure}")
    private boolean accessTokenSecure;
    
    @GetMapping("/login")
    public ResponseEntity<Void> login() {
    	//Non fa nulla se non rispondere forbidden e triggerare l'interceptor a frontend
        return ResponseEntity.ok().body(null);
    }
    
    @GetMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest req, HttpServletResponse resp, 
    		@AuthenticationPrincipal SignedJWT signedJWT) {
    	
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
            	//TODO: cambiare il nome al cookie
            	if ("gestgraduatorie_access_token".equals(cookie.getName())) {
	            	cookie.setValue("");
	                cookie.setMaxAge(0); //DELETE
	                cookie.setPath("/");
	                cookie.setHttpOnly(true);
	                resp.addCookie(cookie);
            	}
            }
        }
        
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/acl-utente")
    public ResponseEntity<AclDto> profiloUtente(@AuthenticationPrincipal SignedJWT signedJWT) throws Exception {
    	JSONObject object = (JSONObject) signedJWT.getJWTClaimsSet().getClaim("acl");
//    	String codiceFiscale =  signedJWT.getJWTClaimsSet().getStringClaim("cf");
//    	String nome = signedJWT.getJWTClaimsSet().getStringClaim("nome");
//    	String cognome = signedJWT.getJWTClaimsSet().getStringClaim("cognome");
    	
    	ObjectMapper objectMapper = new ObjectMapper();
    	AclDto aclDto = objectMapper.readValue(object.toString(), AclDto.class); 
//    	aclDto.setCodiceFiscale(codiceFiscale);
//    	aclDto.setNome(nome);
//    	aclDto.setCognome(cognome);
        return ResponseEntity.ok(aclDto);
    }
    
    
    @GetMapping("/modificaProfiloEnte")
    public void modificaProfiloEnte(@AuthenticationPrincipal SignedJWT signedJWT,@ModelAttribute EnteDTO ente,
    		HttpServletRequest req, HttpServletResponse resp) throws Exception {
    	
    	
    	
        String cf = (String) signedJWT.getJWTClaimsSet().getClaim("cf");
        
        AclDto acl = repositoryService.getAclForTenantUserFromRepository(cf, ente.getTenantCode());
  	  	if (acl == null) {
  	  		throw new IOException("Impossibile recuperare dati tenant");
  	  	}

        JWTClaimsSet.Builder jwtClaimsSetBuilder = new JWTClaimsSet.Builder();
        Date dateexp = (Date) signedJWT.getJWTClaimsSet().getDateClaim("exp");
		jwtClaimsSetBuilder.expirationTime(dateexp);

		jwtClaimsSetBuilder.claim("cf", cf);
		jwtClaimsSetBuilder.claim("acl", acl);
	    jwtClaimsSetBuilder.claim("nome", (String) signedJWT.getJWTClaimsSet().getClaim("nome"));
	    jwtClaimsSetBuilder.claim("cognome", (String) signedJWT.getJWTClaimsSet().getClaim("cognome"));

	    
		//signature
		SignedJWT newSignedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), jwtClaimsSetBuilder.build());
		newSignedJWT.sign(new MACSigner(jwtSecret));

		String token = newSignedJWT.serialize();
    	Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
            	//TODO: cambiare il nome al cookie
            	if ("gestgraduatorie_access_token".equals(cookie.getName())) {
	            	cookie.setValue("");
	                cookie.setMaxAge(0); //DELETE
	                cookie.setPath("/");
	                cookie.setHttpOnly(true);
	                resp.addCookie(cookie);
            	}
            }
        }
        
        Cookie cookie = new Cookie("gestgraduatorie_access_token", URLEncoder.encode(token, StandardCharsets.UTF_8));
        cookie.setHttpOnly(true);
        cookie.setMaxAge(-1);
        cookie.setSecure(accessTokenSecure);
        cookie.setPath("/");
        resp.addCookie(cookie);
        
		
		
		//String token = "{ \"token\":\"" + signedJWT.serialize() + "\" }";
		
		//return token;
	    //AclDto aclDto = objectMapper.readValue(object.toString(), AclDto.class); 
//    	aclDto.setCodiceFiscale(codiceFiscale);
//    	aclDto.setNome(nome);
//    	aclDto.setCognome(cognome);
        //return ResponseEntity.ok(acl);
    }
    
}

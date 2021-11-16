package it.regioneveneto.myp3.gestgraduatorie.jwt;


import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.Assert;

import static it.regioneveneto.myp3.gestgraduatorie.saml.SAMLUserDetailsServiceImpl.GRANTED_AUTHORITY;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;

public class JwtAuthenticationProvider implements AuthenticationProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationProvider.class);

    private String jwtSecret = null;

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public JwtAuthenticationProvider(String jwtSecret) {
    	this.jwtSecret = jwtSecret;
    }
    
    @Override
    public Authentication authenticate(Authentication authentication) {

        Assert.notNull(authentication, "Authentication is missing");

        Assert.isInstanceOf(JwtAuthenticationToken.class, authentication,
                "This method only accepts JwtAuthenticationToken");

//        logger.debug("JWT {}", jwtSecret);

        String jwtToken = authentication.getName();

        if (authentication.getPrincipal() == null || jwtToken == null) {
            throw new AuthenticationCredentialsNotFoundException("Authentication token is missing");
        }

        final SignedJWT signedJWT;

        try {
            signedJWT = SignedJWT.parse(jwtToken);

            boolean isVerified = signedJWT.verify(new MACVerifier(jwtSecret));

            if (!isVerified) {
                throw new BadCredentialsException("Invalid token signature");
            }

            //is token expired ?
            LocalDateTime expirationTime = LocalDateTime.ofInstant(
                    signedJWT.getJWTClaimsSet().getExpirationTime().toInstant(), ZoneId.systemDefault());

            if (LocalDateTime.now(ZoneId.systemDefault()).isAfter(expirationTime)) {
                throw new CredentialsExpiredException("Token expired");
            }

            //TODO deve essere null null?
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

            authorities.add(new SimpleGrantedAuthority(GRANTED_AUTHORITY));

            return new JwtAuthenticationToken(signedJWT, signedJWT, authorities);

        } catch (ParseException e) {
            throw new InternalAuthenticationServiceException("Unreadable token");
        } catch (JOSEException e) {
            throw new InternalAuthenticationServiceException("Unreadable signature");
        }
    }
}

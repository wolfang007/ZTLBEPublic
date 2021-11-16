package it.regioneveneto.myp3.gestgraduatorie.saml;

import java.util.ArrayList;
import java.util.Collection;

import org.opensaml.xml.schema.impl.XSStringImpl;
import org.opensaml.xml.util.XMLHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.security.saml.userdetails.SAMLUserDetailsService;
import org.springframework.stereotype.Service;


@Service
public class SAMLUserDetailsServiceImpl implements SAMLUserDetailsService {

    static final Logger log = LoggerFactory.getLogger(SAMLUserDetailsServiceImpl.class);

    private static final String EMAIL_ADDRESS_PERSONALE = "emailAddressPersonale";
    private static final String CODICE_FISCALE_ATTRIBUTE = "codiceFiscale";
    private static final String NOME = "nome";
    private static final String COGNOME = "cognome";
    private static final String TELEFONO = "cellulare";
    private static final String USER_ID_FROM_SPID = "spidCode";
    public static final String GRANTED_AUTHORITY = "ROLE_USER";


    @Override
    public Object loadUserBySAML(SAMLCredential credential) throws UsernameNotFoundException {

        log.info("SAML authentication");
        log.debug(XMLHelper.nodeToString(credential.getAuthenticationAssertion().getParent().getDOM()));

        MyIDAuthenticationPrincipal principal;

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority(GRANTED_AUTHORITY));

        MyIdUserDetails myIdUserDetails = new MyIdUserDetails();

        String userAlias ="userAlias";

        // SPID CODE
        if( credential.getAttribute(USER_ID_FROM_SPID) != null && credential.getAttribute(USER_ID_FROM_SPID).getAttributeValues() != null && credential.getAttribute(CODICE_FISCALE_ATTRIBUTE).getAttributeValues() != null && credential.getAttribute(CODICE_FISCALE_ATTRIBUTE).getAttributeValues().size() > 0) {
            userAlias = ((XSStringImpl) credential.getAttribute(USER_ID_FROM_SPID).getAttributeValues().get(0)).getValue();
            log.debug("Spid Code {}", userAlias);
        } else {
            log.error("Parametro ["+USER_ID_FROM_SPID+"] non presente in MyID");
        }
        myIdUserDetails.setUser(new User(userAlias, "", authorities));

        // CODICE FISCALE
        if( credential.getAttribute(CODICE_FISCALE_ATTRIBUTE) != null && credential.getAttribute(CODICE_FISCALE_ATTRIBUTE).getAttributeValues() != null && credential.getAttribute(CODICE_FISCALE_ATTRIBUTE).getAttributeValues() != null && credential.getAttribute(CODICE_FISCALE_ATTRIBUTE).getAttributeValues().size() > 0) {
            String codiceFiscaleAttribute = ((XSStringImpl) credential.getAttribute(CODICE_FISCALE_ATTRIBUTE).getAttributeValues().get(0)).getValue();
            log.debug("Codice Fiscale {}", codiceFiscaleAttribute);
            myIdUserDetails.setCodiceFiscale(codiceFiscaleAttribute);
        } else {
            log.error("Parametro ["+CODICE_FISCALE_ATTRIBUTE+"] non presente in MyID");
        }

        // EMAIL PERSONALE
        if( credential.getAttribute(EMAIL_ADDRESS_PERSONALE) != null && credential.getAttribute(EMAIL_ADDRESS_PERSONALE).getAttributeValues() != null && credential.getAttribute(EMAIL_ADDRESS_PERSONALE).getAttributeValues() != null && credential.getAttribute(EMAIL_ADDRESS_PERSONALE).getAttributeValues().size() > 0) {
            String emailPersonale = ((XSStringImpl) credential.getAttribute(EMAIL_ADDRESS_PERSONALE).getAttributeValues().get(0)).getValue();
            log.debug("Email personale {}", emailPersonale);
            myIdUserDetails.setIndirizzoEmail(emailPersonale);
        } else {
            log.error("Parametro ["+EMAIL_ADDRESS_PERSONALE+"] non presente in MyID");
        }

        // NOME
        if( credential.getAttribute(NOME) != null && credential.getAttribute(NOME).getAttributeValues() != null && credential.getAttribute(NOME).getAttributeValues() != null && credential.getAttribute(NOME).getAttributeValues().size() > 0) {
            String nome = ((XSStringImpl) credential.getAttribute(NOME).getAttributeValues().get(0)).getValue();
            log.debug("Nome {}", nome);
            myIdUserDetails.setNome(nome);
        } else {
            log.error("Parametro ["+NOME+"] non presente in MyID");
        }

        // COGNOME
        if( credential.getAttribute(COGNOME) != null && credential.getAttribute(COGNOME).getAttributeValues() != null && credential.getAttribute(COGNOME).getAttributeValues() != null && credential.getAttribute(COGNOME).getAttributeValues().size() > 0) {
            String cognome = ((XSStringImpl) credential.getAttribute(COGNOME).getAttributeValues().get(0)).getValue();
            log.debug("Cognome {}", cognome);
            myIdUserDetails.setCognome(cognome);
        } else {
            log.error("Parametro ["+COGNOME+"] non presente in MyID");
        }

        // TELEFONO
        if( credential.getAttribute(TELEFONO) != null && credential.getAttribute(TELEFONO).getAttributeValues() != null && credential.getAttribute(TELEFONO).getAttributeValues() != null && credential.getAttribute(TELEFONO).getAttributeValues().size() > 0) {
            String telefono = ((XSStringImpl) credential.getAttribute(TELEFONO).getAttributeValues().get(0)).getValue();
            log.debug("Telefono personale {}", telefono);
            myIdUserDetails.setTelefono(telefono);
        } else {
            log.error("Parametro ["+TELEFONO+"] non presente in MyID");
        }

        log.debug("MyIdUserDetails {}", myIdUserDetails);

        principal = new MyIDAuthenticationPrincipal(myIdUserDetails, true, authorities);

        SecurityContextHolder.clearContext();
        SecurityContextHolder.getContext().setAuthentication(principal);

        log.info("Principal inserito nel contesto di sicurezza {}", principal);

        return principal;
    }
    /*
     * Parameters: username the username presented to the DaoAuthenticationProvider
     * password the password that should be presented to the
     * DaoAuthenticationProvider enabled set to true if the user is enabled
     * accountNonExpired set to true if the account has not expired
     * credentialsNonExpired set to true if the credentials have not expired
     * accountNonLocked set to true if the account is not locked authorities the
     * authorities that should be granted to the caller if they presented the
     * correct username and password and the user is enabled. Not null.
     */

}

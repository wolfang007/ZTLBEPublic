package it.regioneveneto.myp3.gestgraduatorie.configuration;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.velocity.app.VelocityEngine;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.core.AuthnContextComparisonTypeEnumeration;
import org.opensaml.saml2.core.NameIDType;
import org.opensaml.saml2.metadata.provider.AbstractMetadataProvider;
import org.opensaml.saml2.metadata.provider.MetadataProvider;
import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.opensaml.util.resource.ResourceException;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.Unmarshaller;
import org.opensaml.xml.parse.ParserPool;
import org.opensaml.xml.parse.StaticBasicParserPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.saml.SAMLAuthenticationProvider;
import org.springframework.security.saml.SAMLBootstrap;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.security.saml.SAMLDiscovery;
import org.springframework.security.saml.SAMLEntryPoint;
import org.springframework.security.saml.SAMLLogoutFilter;
import org.springframework.security.saml.SAMLLogoutProcessingFilter;
import org.springframework.security.saml.SAMLProcessingFilter;
import org.springframework.security.saml.SAMLWebSSOHoKProcessingFilter;
import org.springframework.security.saml.context.SAMLContextProviderLB;
import org.springframework.security.saml.key.JKSKeyManager;
import org.springframework.security.saml.key.KeyManager;
import org.springframework.security.saml.log.SAMLDefaultLogger;
import org.springframework.security.saml.metadata.CachingMetadataManager;
import org.springframework.security.saml.metadata.ExtendedMetadata;
import org.springframework.security.saml.metadata.ExtendedMetadataDelegate;
import org.springframework.security.saml.metadata.MetadataDisplayFilter;
import org.springframework.security.saml.metadata.MetadataGenerator;
import org.springframework.security.saml.metadata.MetadataGeneratorFilter;
import org.springframework.security.saml.parser.ParserPoolHolder;
import org.springframework.security.saml.processor.HTTPArtifactBinding;
import org.springframework.security.saml.processor.HTTPPAOS11Binding;
import org.springframework.security.saml.processor.HTTPPostBinding;
import org.springframework.security.saml.processor.HTTPRedirectDeflateBinding;
import org.springframework.security.saml.processor.HTTPSOAP11Binding;
import org.springframework.security.saml.processor.SAMLBinding;
import org.springframework.security.saml.processor.SAMLProcessorImpl;
import org.springframework.security.saml.storage.EmptyStorageFactory;
import org.springframework.security.saml.util.VelocityFactory;
import org.springframework.security.saml.websso.ArtifactResolutionProfile;
import org.springframework.security.saml.websso.ArtifactResolutionProfileImpl;
import org.springframework.security.saml.websso.SingleLogoutProfile;
import org.springframework.security.saml.websso.SingleLogoutProfileImpl;
import org.springframework.security.saml.websso.WebSSOProfile;
import org.springframework.security.saml.websso.WebSSOProfileConsumer;
import org.springframework.security.saml.websso.WebSSOProfileConsumerHoKImpl;
import org.springframework.security.saml.websso.WebSSOProfileConsumerImpl;
import org.springframework.security.saml.websso.WebSSOProfileECPImpl;
import org.springframework.security.saml.websso.WebSSOProfileImpl;
import org.springframework.security.saml.websso.WebSSOProfileOptions;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.w3c.dom.Document;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import it.regioneveneto.myp3.gestgraduatorie.saml.CustomSAMLBootstrap;
import it.regioneveneto.myp3.gestgraduatorie.saml.MyIDAuthenticationPrincipal;
import it.regioneveneto.myp3.gestgraduatorie.saml.MyIdUserDetails;
import it.regioneveneto.myp3.gestgraduatorie.saml.SAMLUserDetailsServiceImpl;
import it.regioneveneto.myp3.gestgraduatorie.service.RepositoryService;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.AclDto;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.EnteDTO;

@Configuration
@ComponentScan("it.regioneveneto.myp3.gestgraduatorie")
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, proxyTargetClass = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

//    private static final String SAML2_PASSWORD_PROTECTED_TRANSPORT = "https://www.spid.gov.it/SpidL2";
    //private static final String SAML2_PASSWORD_PROTECTED_TRANSPORT = "urn:oasis:names:tc:SAML:2.0:ac:classes:SecureRemotePassword";
	//Questo determina la tipologia di log-in che verrà richiesta dall'IdP (in questo caso secureremotepassword triggera una procedura OTP)
	private static final String SAML2_PASSWORD_PROTECTED_TRANSPORT = "urn:oasis:names:tc:SAML:2.0:ac:classes:PasswordProtectedTransport";

    @Value("${gestgraduatorie.saml.idp-app-id}")
    private String idpApplicationId;

    @Value("${gestgraduatorie.saml.idp-entity-id}")
    private String idpEntityId;

    @Value("${gestgraduatorie.saml.appBaseUrl}")
    private String samlAppBaseUrl;

    @Value("${gestgraduatorie.saml.appEntityId}")
    private String samlAppEntityId;

    @Value("${gestgraduatorie.saml.contextProviderServerScheme}")
    private String samlContextProviderServerScheme;
    @Value("${gestgraduatorie.saml.contextProviderServerName}")
    private String samlContextProviderServerName;
    @Value("${gestgraduatorie.saml.contextProviderServerPort}")
    private Integer samlContextProviderServerPort;

    @Value("${gestgraduatorie.saml.includeServerPortInRequestURL}")
    private Boolean samlIncludeServerPortInRequestURL;
    
    @Value("${gestgraduatorie.saml.successURLRedirect}")
    private String successURLRedirect;

    @Value("${server.servlet.context-path}")
    private String contextPath;
    
    @Value("${gestgraduatorie.auth.access-token-secure}")
    private boolean accessTokenSecure;

    @Autowired
    private SAMLUserDetailsServiceImpl samlUserDetailsServiceImpl;

    @Autowired
    RepositoryService repositoryService;
    
    @Value("${gestgraduatorie.tenantRegionale}")
    private String tenantRegionale;

    @Value("${gestgraduatorie.auth.jwt-secret}")
    private String jwtSecret;

    @Value("${gestgraduatorie.saml.keystore.alias-cert}")
    private String samlKeystoreAliasCert;
    
    @Value("${gestgraduatorie.saml.keystore.cert-pwd}")
    private String samlKeystoreCertPwd;

    @Value("${gestgraduatorie.saml.keystore.filepath}")
    private String samlKeystoreFilePath;
    @Value("${gestgraduatorie.saml.keystore.filename}")
    private String samlKeystoreFilename;

    @Value("${gestgraduatorie.saml.service-provider-metadata.filepath}")
    private String samlServiceProviderMetadataFilepath;
    @Value("${gestgraduatorie.saml.service-provider-metadata.filename}")
    private String samlServiceProviderMetadataFilename;
    
    @Value("${gestgraduatorie.saml.idp-metadata.filepath}")
    private String samlIdpMetadataFilePath;
    @Value("${gestgraduatorie.saml.idp-metadata.filename}")
    private String samlIdpMetadataFileName;


    private static final String[] AUTH_WHITELIST = {"/actuator/**", "/saml/**", "/api/**", "/hd/**"};

    private static final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http    .exceptionHandling()
                .authenticationEntryPoint(samlEntryPoint());

        http    .csrf().ignoringAntMatchers("/saml/**");

        http
        		.addFilterAfter(samlFilter(), BasicAuthenticationFilter.class);
        
		http
		        .authorizeRequests()
		        .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
		        .antMatchers("/error").permitAll()
		        .antMatchers("/saml/**").permitAll()
		        .anyRequest().authenticated();
    }
    
    /**
     * Configures multiple Authentication providers. AuthenticationManagerBuilder allows for easily building multiple authentication mechanisms in the
     * order they're declared. CasAuthenticationProvider is used here.
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(samlAuthenticationProvider());
    }

    @Bean
    public SecurityContextLogoutHandler securityContextLogoutHandler() {
        return new SecurityContextLogoutHandler();
    }

    //////////////////////////  SAML \\\\\\\\\\\\\\\\\\\\\\\

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    protected SessionRegistry sessionRegistryImpl() {
        return new SessionRegistryImpl();
    }

    // SAML being XML based protocol, XML parser pools should be initialized to read
    // metadata and assertions that are in XML format.
    // Initialization of the velocity engine
    @Bean
    public VelocityEngine velocityEngine() {
        return VelocityFactory.getEngine();
    }

    // XML parser pool needed for OpenSAML parsing
    @Bean(initMethod = "initialize")
    public StaticBasicParserPool parserPool() {
        return new StaticBasicParserPool();
    }

    @Bean(name = "parserPoolHolder")
    public ParserPoolHolder parserPoolHolder() {
        return new ParserPoolHolder();
    }

    // Bindings, encoders and decoders used for creating and parsing messages
    @Bean
    public MultiThreadedHttpConnectionManager multiThreadedHttpConnectionManager() {
        return new MultiThreadedHttpConnectionManager();
    }

    @Bean
    public HttpClient httpClient() {
        return new HttpClient(multiThreadedHttpConnectionManager());
    }

    // SAML Authentication Provider responsible for validating of received SAML
    // messages
    @Bean
    public SAMLAuthenticationProvider samlAuthenticationProvider() {
        SAMLAuthenticationProvider samlAuthenticationProvider = new SAMLAuthenticationProvider();
        samlAuthenticationProvider.setUserDetails(samlUserDetailsServiceImpl);
        samlAuthenticationProvider.setForcePrincipalAsString(false);
        return samlAuthenticationProvider;
    }

    /**
     * Provider of default SAML Context This configuration is for the application that is not behind a Reverse Proxy. Alternatively,
     * SAMLContextProviderLB can be used, which is a Context provider that overrides request attributes with values of the load-balancer or
     * reverse-proxy in front of the local application. The settings help to provide correct redirect URls and verify destination URLs during SAML
     * processing.
     */
    // @Bean
    // public SAMLContextProviderImpl contextProvider() {
    // return new SAMLContextProviderImpl();
    // }
    @Bean
    public SAMLContextProviderLB contextProvider() {
        SAMLContextProviderLB contextProvider = new SAMLContextProviderLB();
        contextProvider.setScheme(samlContextProviderServerScheme);
        contextProvider.setServerName(samlContextProviderServerName);
        contextProvider.setServerPort(samlContextProviderServerPort);
        contextProvider.setIncludeServerPortInRequestURL(samlIncludeServerPortInRequestURL);
        contextProvider.setContextPath(contextPath);
        contextProvider.setStorageFactory(new EmptyStorageFactory());
        return contextProvider;
    }

    // Initialization of OpenSAML library
    @Bean
    public static SAMLBootstrap sAMLBootstrap() {
        // return new SAMLBootstrap();
        return new CustomSAMLBootstrap();
    }

    // Logger for SAML messages and events
    @Bean
    public SAMLDefaultLogger samlLogger() {
        return new SAMLDefaultLogger();
    }

    // SAML 2.0 WebSSO Assertion Consumer
    @Bean
    public WebSSOProfileConsumer webSSOprofileConsumer() {
        WebSSOProfileConsumerImpl sso = new WebSSOProfileConsumerImpl();
        sso.setMaxAssertionTime(72000);
        sso.setResponseSkew(600);
        return new WebSSOProfileConsumerImpl();
    }

    // SAML 2.0 Holder-of-Key WebSSO Assertion Consumer
    @Bean
    public WebSSOProfileConsumerHoKImpl hokWebSSOprofileConsumer() {
        return new WebSSOProfileConsumerHoKImpl();
    }

    // SAML 2.0 Web SSO profile
    @Bean
    public WebSSOProfile webSSOprofile() {
        return new WebSSOProfileImpl();
    }

    // SAML 2.0 Holder-of-Key Web SSO profile
    @Bean
    public WebSSOProfileConsumerHoKImpl hokWebSSOProfile() {
        return new WebSSOProfileConsumerHoKImpl();
    }

    // SAML 2.0 ECP profile
    @Bean
    public WebSSOProfileECPImpl ecpprofile() {
        return new WebSSOProfileECPImpl();
    }

    @Bean
    public SingleLogoutProfile logoutprofile() {
        return new SingleLogoutProfileImpl();
    }

    /**
     * Metadata generation requires a keyManager, it is responsible to encrypt the saml assertion sent to IdP. A self-signed key and keystore can be
     * generated with the JRE keytool command: keytool -genkeypair -alias mykeyalias -keypass mykeypass -storepass samlstorepass -keystore
     * saml-keystore.jks
     */
    @Bean
    public KeyManager keyManager() {
        DefaultResourceLoader loader = new DefaultResourceLoader();
        Resource storeFile = loader.getResource("file:" + samlKeystoreFilePath + samlKeystoreFilename);
        String storePass = samlKeystoreCertPwd;
        Map<String, String> passwords = new HashMap<String, String>();
        passwords.put(samlKeystoreAliasCert, samlKeystoreCertPwd);
        String defaultKey = samlKeystoreAliasCert;
        return new JKSKeyManager(storeFile, storePass, passwords, defaultKey);
    }

    @Bean
    public WebSSOProfileOptions defaultWebSSOProfileOptions() {

        WebSSOProfileOptions webSSOProfileOptions = new WebSSOProfileOptions();
        webSSOProfileOptions.setIncludeScoping(false);
        webSSOProfileOptions.setProxyCount(0);
        webSSOProfileOptions.setForceAuthN(true);
        webSSOProfileOptions.setNameID(NameIDType.TRANSIENT);

        //TODO: capire che senso ha questo relay state
        //webSSOProfileOptions.setRelayState(UUID.randomUUID().toString());
        
        webSSOProfileOptions.setRelayState(successURLRedirect);
        //http://localhost:9081/eventicalamitosi/api/swagger-ui.html
        //webSSOProfileOptions.setRelayState("http://localhost:9081/eventicalamitosi/api/swagger-ui.html");

        webSSOProfileOptions.setAllowCreate(true);
        webSSOProfileOptions.setAuthnContextComparison(AuthnContextComparisonTypeEnumeration.MINIMUM);
        webSSOProfileOptions.setBinding(SAMLConstants.SAML2_REDIRECT_BINDING_URI);

        Collection<String> contexts = new ArrayList<>();
        contexts.add(SAML2_PASSWORD_PROTECTED_TRANSPORT);
        webSSOProfileOptions.setAuthnContexts(contexts);
        return webSSOProfileOptions;
    }

    @Bean
    public SAMLEntryPoint samlEntryPoint() {
        SAMLEntryPoint samlEntryPoint = new SAMLEntryPoint();
        samlEntryPoint.setDefaultProfileOptions(defaultWebSSOProfileOptions());
        return samlEntryPoint;
    }
    
    /*@Bean
    public SAMLEntryPoint samlEntryPoint() {
        SAMLEntryPoint samlEntryPoint = new SamlWithRelayStateEntryPoint();
        samlEntryPoint.setDefaultProfileOptions(defaultWebSSOProfileOptions());
        return samlEntryPoint;
    }*/


    @Bean
    public SAMLDiscovery samlIDPDiscovery() {
        SAMLDiscovery idpDiscovery = new SAMLDiscovery();
        idpDiscovery.setIdpSelectionPath("/saml/idpSelection");
        return idpDiscovery;
    }

    @Bean
    @Qualifier("idp-ssocircle")
    public ExtendedMetadataDelegate ssoCircleExtendedMetadataProvider() throws MetadataProviderException {

        AbstractMetadataProvider provider = new AbstractMetadataProvider() {
            @Override
            protected XMLObject doGetMetadata() throws MetadataProviderException {
                DefaultResourceLoader loader = new DefaultResourceLoader();
                Resource storeFile = loader.getResource("file:" + samlIdpMetadataFilePath + samlIdpMetadataFileName);

                ParserPool parser = parserPool();

                try {
                    Document mdDocument = parser.parse(storeFile.getInputStream());
                    Unmarshaller unmarshaller = unmarshallerFactory.getUnmarshaller(mdDocument.getDocumentElement());
                    return unmarshaller.unmarshall(mdDocument.getDocumentElement());
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new MetadataProviderException();
                }

            }
        };
        ExtendedMetadataDelegate extendedMetadataDelegate = new ExtendedMetadataDelegate(provider, extendedMetadata());
        extendedMetadataDelegate.setMetadataTrustCheck(false);
        extendedMetadataDelegate.setMetadataRequireSignature(false);
        return extendedMetadataDelegate;
    }

    // TODO scommentare quando andremo su NAM
    // @Bean
    // public ExtendedMetadataDelegate idpMetadata()
    // throws MetadataProviderException, ResourceException {
    //
    // Timer backgroundTaskTimer = new Timer(true);
    //
    //// HTTPMetadataProvider httpMetadataProvider = new
    // HTTPMetadataProvider(backgroundTaskTimer, new
    // HttpClient(),idpMetadataUrl.concat(applicationIdpOnIdp));
    // HTTPMetadataProvider httpMetadataProvider = new
    // HTTPMetadataProvider(backgroundTaskTimer, new HttpClient(),idpMetadataUrl);
    // httpMetadataProvider.setParserPool(parserPool());
    //
    // ExtendedMetadataDelegate extendedMetadataDelegate = new
    // ExtendedMetadataDelegate(httpMetadataProvider , extendedMetadata());
    // extendedMetadataDelegate.setMetadataRequireSignature(false);
    // return extendedMetadataDelegate;
    // }

    @Bean
    @Qualifier("metadata")
    public CachingMetadataManager metadata() throws MetadataProviderException, ResourceException {
        List<MetadataProvider> providers = new ArrayList<MetadataProvider>();

        providers.add(ssoCircleExtendedMetadataProvider());
        // TODO scommentare quando andremo su NAM
        // providers.add(idpMetadata());

        providers.add(extendedMetadataDelegate());

        System.out.println("*************** providers = " + providers);
        System.out.println("*************** providers = " + providers.size());

        return new CachingMetadataManager(providers);
    }

    @Bean
    public MetadataGenerator metadataGenerator() {
        MetadataGenerator metadataGenerator = new MetadataGenerator();

        // APP_ENTITY_ID – This is the name of the application/ audience field in the
        // application set-up for the IDP
        metadataGenerator.setEntityId(samlAppEntityId);
        // APP_BASE_URL –This is the application’s base url after deployment, it varies
        // according to the environment the application is deployed in.
        metadataGenerator.setEntityBaseURL(samlAppBaseUrl);
        metadataGenerator.setNameID(Arrays.asList("urn:oasis:names:tc:SAML:2.0:nameid-format:transient"));
        metadataGenerator.setExtendedMetadata(extendedMetadata());
        metadataGenerator.setIncludeDiscoveryExtension(false);
        metadataGenerator.setKeyManager(keyManager());
        return metadataGenerator;
    }

    @Bean
    public ExtendedMetadata extendedMetadata() {
        ExtendedMetadata extendedMetadata = new ExtendedMetadata();
        extendedMetadata.setIdpDiscoveryEnabled(false);

//		extendedMetadata.setSigningAlgorithm("http://www.w3.org/2001/04/xmldsig-more#rsa-sha256");
//		extendedMetadata.setDigestMethodAlgorithm("http://www.w3.org/2001/04/xmlenc#sha256 ");

		extendedMetadata.setDigestMethodAlgorithm("http://www.w3.org/2000/09/xmldsig#rsa-sha1");
        extendedMetadata.setSigningAlgorithm("http://www.w3.org/2000/09/xmldsig#rsa-sha1");
        extendedMetadata.setSignMetadata(false);
        extendedMetadata.setRequireLogoutRequestSigned(false);

        return extendedMetadata;
    }

    @Bean
    public MetadataDisplayFilter metadataDisplayFilter() {
        return new MetadataDisplayFilter();
    }

    @Bean
    public AuthenticationSuccessHandler successRedirectHandler() {
        /*SavedRequestAwareAuthenticationSuccessHandler successRedirectHandler = new SavedRequestAwareAuthenticationSuccessHandler();
//        successRedirectHandler.setDefaultTargetUrl("/#/richiesta-rimborso");
        successRedirectHandler.setDefaultTargetUrl(successURLRedirect);
        return successRedirectHandler;*/
        
        return new AuthenticationSuccessHandler() {
            private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
            
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
              //redirect to callback url specified when initiating login procedure, passing JWT token as query param
              final SAMLCredential credential = (SAMLCredential) authentication.getCredentials();
              //Authentication innerAuth = (Authentication)authentication.getPrincipal();
              /*UserWithAdditionalInfo userDetails = (UserWithAdditionalInfo) innerAuth.getPrincipal();
              log.info("principal: "+userDetails);
              Map<String, Object> claims = new HashMap<>();
              claims.put("cognome",userDetails.getCognome());
              claims.put("nome",userDetails.getNome());
              claims.put("codiceFiscale",userDetails.getCodiceFiscale());
              claims.put("email",userDetails.getEmail());*/
              //jwtTokenUtil.generateToken(userDetails.getUsername(), claims);
              try {
	              final String token = extracted(authentication);
	              //url encode token
	              String targetUrl = credential.getRelayState();
	              Cookie cookie = new Cookie("gestgraduatorie_access_token", URLEncoder.encode(token, StandardCharsets.UTF_8));
	              cookie.setHttpOnly(true);
	              cookie.setMaxAge(-1);
	              cookie.setSecure(accessTokenSecure);
	              cookie.setPath("/");
	              response.addCookie(cookie);
	              response.setStatus(HttpServletResponse.SC_SEE_OTHER);
	              redirectStrategy.sendRedirect(request, response, targetUrl);
              } catch (Exception ex) {
            	  throw new IOException(ex);
              }
            }
          };

    }

    @Bean
    public SimpleUrlAuthenticationFailureHandler authenticationFailureHandler() {
        SimpleUrlAuthenticationFailureHandler failureHandler = new SimpleUrlAuthenticationFailureHandler();
        failureHandler.setUseForward(false);
        failureHandler.setDefaultFailureUrl("/#/errore");
        return failureHandler;
    }
    
    //TODO: ripristinare l'handler precedente dopo aver verificato perché sta fallendo al momento
    public AuthenticationFailureHandler myFailureHandler() {
    	return new AuthenticationFailureHandler() {
    		 
    	    private ObjectMapper objectMapper = new ObjectMapper();
    	 
    	    @Override
    	    public void onAuthenticationFailure(
    	      HttpServletRequest request,
    	      HttpServletResponse response,
    	      AuthenticationException exception) 
    	      throws IOException, ServletException {
    	 
    	        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    	        Map<String, Object> data = new HashMap<>();
    	        data.put(
    	          "timestamp", 
    	          Calendar.getInstance().getTime());
    	        data.put(
    	          "exception", 
    	          exception.getMessage());
    	 
    	        response.getOutputStream()
    	          .println(objectMapper.writeValueAsString(data));
    	    }
    	};
    }

    @Bean
    public SAMLWebSSOHoKProcessingFilter samlWebSSOHoKProcessingFilter() throws Exception {
        SAMLWebSSOHoKProcessingFilter samlWebSSOHoKProcessingFilter = new SAMLWebSSOHoKProcessingFilter();
        samlWebSSOHoKProcessingFilter.setAuthenticationSuccessHandler(successRedirectHandler());
        samlWebSSOHoKProcessingFilter.setAuthenticationManager(authenticationManager());
        samlWebSSOHoKProcessingFilter.setAuthenticationFailureHandler(authenticationFailureHandler());
        return samlWebSSOHoKProcessingFilter;
    }

    @Bean
    public SAMLProcessingFilter samlWebSSOProcessingFilter() throws Exception {

        SAMLProcessingFilter samlWebSSOProcessingFilter = new SAMLProcessingFilter();
        samlWebSSOProcessingFilter.setAuthenticationManager(authenticationManager());
        samlWebSSOProcessingFilter.setAuthenticationSuccessHandler(successRedirectHandler());
        //samlWebSSOProcessingFilter.setAuthenticationFailureHandler(authenticationFailureHandler());
        samlWebSSOProcessingFilter.setAuthenticationFailureHandler(myFailureHandler());

        return samlWebSSOProcessingFilter;
    }

    @Bean
    public MetadataGeneratorFilter metadataGeneratorFilter() {
        return new MetadataGeneratorFilter(metadataGenerator());
    }

    @Bean
    public SimpleUrlLogoutSuccessHandler successLogoutHandler() {
        SimpleUrlLogoutSuccessHandler successLogoutHandler = new SimpleUrlLogoutSuccessHandler();
        successLogoutHandler.setDefaultTargetUrl("/index.html");
        return successLogoutHandler;
    }

    @Bean
    public SecurityContextLogoutHandler logoutHandler() {
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.setInvalidateHttpSession(true);
        logoutHandler.setClearAuthentication(true);
        return logoutHandler;
    }

    @Bean
    public SAMLLogoutProcessingFilter samlLogoutProcessingFilter() {
        return new SAMLLogoutProcessingFilter(successLogoutHandler(), logoutHandler());
    }

    @Bean
    public SAMLLogoutFilter samlLogoutFilter() {
        return new SAMLLogoutFilter(successLogoutHandler(), new LogoutHandler[]{logoutHandler()},
                new LogoutHandler[]{logoutHandler()});
    }

    private ArtifactResolutionProfile artifactResolutionProfile() {
        final ArtifactResolutionProfileImpl artifactResolutionProfile = new ArtifactResolutionProfileImpl(httpClient());
        artifactResolutionProfile.setProcessor(new SAMLProcessorImpl(soapBinding()));
        return artifactResolutionProfile;
    }

    @Bean
    public HTTPArtifactBinding artifactBinding(ParserPool parserPool, VelocityEngine velocityEngine) {
        return new HTTPArtifactBinding(parserPool, velocityEngine, artifactResolutionProfile());
    }

    @Bean
    public HTTPSOAP11Binding soapBinding() {
        return new HTTPSOAP11Binding(parserPool());
    }

    @Bean
    public HTTPPostBinding httpPostBinding() {
        return new HTTPPostBinding(parserPool(), velocityEngine());
    }

    @Bean
    public HTTPRedirectDeflateBinding httpRedirectDeflateBinding() {
        return new HTTPRedirectDeflateBinding(parserPool());
    }

    @Bean
    public HTTPSOAP11Binding httpSOAP11Binding() {
        return new HTTPSOAP11Binding(parserPool());
    }

    @Bean
    public HTTPPAOS11Binding httpPAOS11Binding() {
        return new HTTPPAOS11Binding(parserPool());
    }

    @Bean
    public SAMLProcessorImpl processor() {
        Collection<SAMLBinding> bindings = new ArrayList<SAMLBinding>();
        bindings.add(httpRedirectDeflateBinding());
        bindings.add(httpPostBinding());
        bindings.add(artifactBinding(parserPool(), velocityEngine()));
        bindings.add(httpSOAP11Binding());
        bindings.add(httpPAOS11Binding());
        return new SAMLProcessorImpl(bindings);
    }

    @Bean
    public FilterChainProxy samlFilter() throws Exception {

        List<SecurityFilterChain> chains = new ArrayList<SecurityFilterChain>();

        chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher("/saml/login/**"), samlEntryPoint()));
        chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher("/saml/logout/**"), samlLogoutFilter()));
        chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher("/saml/metadata/**"), metadataDisplayFilter()));
        chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher("/saml/SSO/**"), samlWebSSOProcessingFilter()));
        chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher("/saml/SSOHoK/**"), samlWebSSOHoKProcessingFilter()));
        chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher("/saml/SingleLogout/**"), samlLogoutProcessingFilter()));
        chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher("/saml/discovery/**"), samlIDPDiscovery()));

        return new FilterChainProxy(chains);
    }

    // @Bean
    // @Override
    // public AuthenticationManager authenticationManagerBean() throws Exception {
    // return super.authenticationManagerBean();
    // }

    private static AuthenticationEntryPoint getAuthEntryPoint() {
        return new AuthenticationEntryPoint() {
            @Override
            public void commence(HttpServletRequest request, HttpServletResponse response,
                    AuthenticationException authException) throws IOException, ServletException {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access Denied");
            }
        };
    }

    @Bean
    public ExtendedMetadataDelegate extendedMetadataDelegate() throws MetadataProviderException {

        AbstractMetadataProvider provider = new AbstractMetadataProvider() {

            @Override
            protected XMLObject doGetMetadata() throws MetadataProviderException {

                DefaultResourceLoader loader = new DefaultResourceLoader();

                Resource storeFile = loader.getResource("file:" + samlServiceProviderMetadataFilepath + samlServiceProviderMetadataFilename);

                ParserPool parser = parserPool();

                try {

                    Document mdDocument = parser.parse(storeFile.getInputStream());

                    Unmarshaller unmarshaller = unmarshallerFactory.getUnmarshaller(mdDocument.getDocumentElement());

                    return unmarshaller.unmarshall(mdDocument.getDocumentElement());

                } catch (Exception e) {

                    e.printStackTrace();

                    throw new MetadataProviderException();

                }

            }

        };

        ExtendedMetadataDelegate extendedMetadataDelegate =

                new ExtendedMetadataDelegate(provider, spExtendedMetadata());

        extendedMetadataDelegate.setMetadataTrustCheck(false);

        extendedMetadataDelegate.setMetadataRequireSignature(false);
        return extendedMetadataDelegate;

    }

    @Bean
    public ExtendedMetadata spExtendedMetadata() {
        ExtendedMetadata extendedMetadata = new ExtendedMetadata();
        extendedMetadata.setLocal(true);
        extendedMetadata.setSecurityProfile("metaiop");
        extendedMetadata.setSslSecurityProfile("pkix");
        extendedMetadata.setSignMetadata(false);
        extendedMetadata.setSigningKey("evecal");
        extendedMetadata.setRequireArtifactResolveSigned(false);
        extendedMetadata.setRequireLogoutRequestSigned(false);
        extendedMetadata.setRequireLogoutResponseSigned(false);
        extendedMetadata.setIdpDiscoveryEnabled(false);
        extendedMetadata.setSigningAlgorithm("http://www.w3.org/2000/09/xmldsig#rsa-sha1");
        return extendedMetadata;

    }
    
    



    
    
	private String extracted(final Authentication authentication) throws JOSEException, KeyLengthException, IOException {
		final SAMLCredential credential = (SAMLCredential) authentication.getCredentials();

		final DateTime dateTime = credential.getAuthenticationAssertion()
		        .getIssueInstant()
		        .toDateTime(DateTimeZone.forTimeZone(TimeZone.getDefault()));

		JWTClaimsSet.Builder jwtClaimsSetBuilder = new JWTClaimsSet.Builder();
		jwtClaimsSetBuilder.expirationTime(dateTime.plusMinutes(120).toDate());

		try {

		    MyIDAuthenticationPrincipal myIdPrincipal = (MyIDAuthenticationPrincipal) authentication.getDetails();
		    MyIdUserDetails userDetails = (MyIdUserDetails) myIdPrincipal.getDetails();

		    logger.info("*#*#*#*# Utente loggato {}", userDetails);

		    //chiamata http://myprofile-rw-private.myplace.tz.eng.it:8080/myprofile-server/api/tenants/MYP3/MLONNN59H29Z125G/
		    List<EnteDTO> enti = repositoryService.getEntiFromRepository(userDetails.getCodiceFiscale());
		    
		    //build claims
      	  	AclDto acl = repositoryService.getAclForTenantUserFromRepository(userDetails.getCodiceFiscale(), enti.get(0).getTenantCode());
      	  	if (acl == null) {
      	  		throw new IOException("Impossibile recuperare dati tenant");
      	  	}
      	  	jwtClaimsSetBuilder.claim("cf", userDetails.getCodiceFiscale());
      	  	
      	  	acl.setEnti(enti);
      	  	acl.setCognome(userDetails.getCognome());
      	  	acl.setNome(userDetails.getNome());
      	  	acl.setCodiceFiscale(userDetails.getCodiceFiscale());
      	  	
      	  	jwtClaimsSetBuilder.claim("acl", acl);
      	  	
		    /*jwtClaimsSetBuilder.claim("indirizzoEmail", userDetails.getIndirizzoEmail());
		    jwtClaimsSetBuilder.claim("telefono", userDetails.getTelefono());*/
		    jwtClaimsSetBuilder.claim("nome", userDetails.getNome());
		    jwtClaimsSetBuilder.claim("cognome", userDetails.getCognome());

		} catch (ClassCastException classCastException) {

		    classCastException.printStackTrace();
		    logger.error("ERRORE Conversione classe ");

		} catch (Exception e) {
		    e.printStackTrace();
		    logger.error("ERRORE estrazione dati Principal");
		}

		//signature
		SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), jwtClaimsSetBuilder.build());
		signedJWT.sign(new MACSigner(jwtSecret));

		//String token = "{ \"token\":\"" + signedJWT.serialize() + "\" }";
		return signedJWT.serialize();
		//return token;
	}
	
	
}

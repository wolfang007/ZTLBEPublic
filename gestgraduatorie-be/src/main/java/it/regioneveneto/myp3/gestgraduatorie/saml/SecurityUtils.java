package it.regioneveneto.myp3.gestgraduatorie.saml;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.xml.Configuration;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.util.XMLHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;

@Service
public class SecurityUtils {

    static final Logger logger = LoggerFactory.getLogger(SecurityUtils.class);


    Set<SimpleGrantedAuthority> listaFunzioni = null;

    public MyIdUserDetails buildUserAuthority(Object utente) {
        MyIdUserDetails silUserDetails = null;

        listaFunzioni = new HashSet<SimpleGrantedAuthority>();
//		List<ProfRuoloDto> listaProfRuoloDto = null;
//		List<UteGruppoDto> listaUteGruppoDto = null;
//		try {
//
//			if (utente.getUsername() != null) {
//				silUserDetails = new SilUserDetails();
//				// RUOLI FUNZ
//				ProfRuoloBeanFilter roleBeanFilter = new ProfRuoloBeanFilter();
//				roleBeanFilter.setUsername(utente.getUsername());
//				roleBeanFilter.setCodApplicazione("PLL");
//				roleBeanFilter.setFlagSelectFunzSegrs(Boolean.TRUE);
//				List<ProfRuolo> listaRuoli = ruoliDao.searchAllRoles(roleBeanFilter);
//				listaProfRuoloDto = new ArrayList<ProfRuoloDto>();
//
//				for (ProfRuolo ruolo : listaRuoli) {
//					boolean checkPresenza = false;
//					for (ProfRuoloDto ruoDto : listaProfRuoloDto) {
//						if (ruolo.getCodRuolo().equals(ruoDto.getCodRuolo()))
//							checkPresenza = true;
//					}
//
//					if (!checkPresenza) {
//						ProfRuoloDto profRuoloDto = DomainEntityConverter.map(ruolo, ProfRuoloDto.class);
//
//						// set codTipoUtente nel ruolo
//						CodTipoUtenteBean tipoUtente = DomainEntityConverter.map(ruolo.getCodTipoUtente(),
//								CodTipoUtenteBean.class);
//						profRuoloDto.setCodTipoUtente(tipoUtente);
//						profRuoloDto.setCodApplicazione(ruolo.getProfApplicazione().getCodApplicazione());
//
//						List<ProfRuoFunzSegr> profRuoFunzSegList = ruolo.getProfRuoFunzSegrs();
//						if (profRuoFunzSegList != null && !profRuoFunzSegList.isEmpty()) {
//
//							List<ProfRuoFunzioneDto> profRuoFunzioneList = new ArrayList<>();
//
//							for (ProfRuoFunzSegr profRuoFunzSegr : profRuoFunzSegList) {
//
//								boolean getConvalidaIdentita = profRuoFunzSegr.getConvalidaIdentita() != null
//										? profRuoFunzSegr.getConvalidaIdentita()
//										: false;
//								boolean getLivSicurCredenziali = profRuoFunzSegr.getLivSicurCredenziali() != null
//										? profRuoFunzSegr.getLivSicurCredenziali()
//										: false;
//
//								ProfFunzione entityFunzioneLivello1 = profRuoFunzSegr.getProfFunzione();
//
//								if (entityFunzioneLivello1 != null) {
//									listaFunzioni
//											.add(new SimpleGrantedAuthority(entityFunzioneLivello1.getCodFunzione()));
//
//									/* Creo la funzione di 1° livello */
//									ProfRuoFunzioneDto profRuoFunzioneDtoLiv1 = new ProfRuoFunzioneDto();
//									profRuoFunzioneDtoLiv1.setCodiceFunzione(entityFunzioneLivello1.getCodFunzione());
//									profRuoFunzioneDtoLiv1.setConvalidaIdentita(getConvalidaIdentita);
//									profRuoFunzioneDtoLiv1.setLivSicurCredenziali(getLivSicurCredenziali);
//
//									/* aggiungo configurazione segregazione dati alla funzione di 1° livello */
//									ProfSegregazioneFunzioneDto profSegregazioneFunzioneDto = null;
//
//									ProfSegregazioneDati profSegregazioneDatiEntity = profRuoFunzSegr
//											.getProfSegregazioneDati();
//									if (profSegregazioneDatiEntity != null) {
//										profSegregazioneFunzioneDto = new ProfSegregazioneFunzioneDto();
//										profSegregazioneFunzioneDto.setCodSegregazioneDati(
//												profSegregazioneDatiEntity.getCodSegregazioneDati());
//										profSegregazioneFunzioneDto
//												.setParametro1(profSegregazioneDatiEntity.getParametro1());
//										profSegregazioneFunzioneDto
//												.setParametro2(profSegregazioneDatiEntity.getParametro2());
//										profSegregazioneFunzioneDto
//												.setParametro3(profSegregazioneDatiEntity.getParametro3());
//										profSegregazioneFunzioneDto
//												.setParametro4(profSegregazioneDatiEntity.getParametro4());
//										profRuoFunzioneDtoLiv1.setSegregazione(profSegregazioneFunzioneDto);
//									}
//
//									/* Aggiungo la funzione di 1° livello alla lista di output */
//									profRuoFunzioneList.add(profRuoFunzioneDtoLiv1);
//
//									/* creo eventuali funzioni di 2° livello */
//									List<ProfFunzione> listaFunzioniLivello2 = entityFunzioneLivello1
//											.getProfFunziones();
//									if (listaFunzioniLivello2 != null) {
//
//										for (ProfFunzione entityFunzioneLivello2 : listaFunzioniLivello2) {
//											listaFunzioni.add(new SimpleGrantedAuthority(
//													entityFunzioneLivello2.getCodFunzione()));
//
//											ProfRuoFunzioneDto profRuoFunzioneDtoLiv2 = new ProfRuoFunzioneDto();
//											profRuoFunzioneDtoLiv2
//													.setCodiceFunzione(entityFunzioneLivello2.getCodFunzione());
//											profRuoFunzioneDtoLiv2.setConvalidaIdentita(getConvalidaIdentita);
//											profRuoFunzioneDtoLiv2.setLivSicurCredenziali(getLivSicurCredenziali);
//
//											if (profSegregazioneDatiEntity != null)
//												profRuoFunzioneDtoLiv2.setSegregazione(profSegregazioneFunzioneDto);
//
//											profRuoFunzioneList.add(profRuoFunzioneDtoLiv2);
//
//											/* creo eventuali funzioni di 3° livello */
//											List<ProfFunzione> listaFunzioniLivello3 = entityFunzioneLivello2
//													.getProfFunziones();
//
//											if (listaFunzioniLivello3 != null && !listaFunzioniLivello3.isEmpty()) {
//
//												for (ProfFunzione entityFunzioneLivello3 : listaFunzioniLivello3) {
//													listaFunzioni.add(new SimpleGrantedAuthority(
//															entityFunzioneLivello3.getCodFunzione()));
//
//													ProfRuoFunzioneDto profRuoFunzioneDtoLiv3 = new ProfRuoFunzioneDto();
//													profRuoFunzioneDtoLiv3
//															.setCodiceFunzione(entityFunzioneLivello3.getCodFunzione());
//													profRuoFunzioneDtoLiv3.setConvalidaIdentita(getConvalidaIdentita);
//													profRuoFunzioneDtoLiv3
//															.setLivSicurCredenziali(getLivSicurCredenziali);
//													if (profSegregazioneDatiEntity != null)
//														profRuoFunzioneDtoLiv3
//																.setSegregazione(profSegregazioneFunzioneDto);
//
//													profRuoFunzioneList.add(profRuoFunzioneDtoLiv3);
//
//												}
//											}
//										}
//									}
//								}
//								profRuoloDto.setProfRuoFunzione(profRuoFunzioneList);
//							}
//
//							/// ADD GRUPPI
//							List<ProfUtenteRuolo> listaProfUtenteRuolo = profUtenteRuoloRepository
//									.findProfUtenteRuoloByUteUtenteAndProfRuolo(utente, ruolo);
//							listaUteGruppoDto = new ArrayList<UteGruppoDto>();
//
//							for (ProfUtenteRuolo uteRuo : listaProfUtenteRuolo) {
//								if (uteRuo != null) {
//									UteGruppo gruppo = uteRuo.getUteGruppo();
//									if (gruppo != null) {
//										UteGruppoDto gruppoDto = DomainEntityConverter.map(gruppo, UteGruppoDto.class);
//										gruppoDto.setFkIdGruppoPadre(gruppo.getUteGruppo().getIdGruppo());
//
//										UteAziendaDto uteAziendaDto = DomainEntityConverter.map(gruppo.getUteAzienda(),
//												UteAziendaDto.class);
//
//										Optional<UteAzienda> uteAziendaEntityOpt = null;
//										if (uteAziendaDto != null && uteAziendaDto.getIdUtenteAzienda() != null) {
//											uteAziendaEntityOpt = uteAziendaRepository
//													.findById(uteAziendaDto.getIdUtenteAzienda());
//										}
//
//										if (uteAziendaEntityOpt != null && uteAziendaEntityOpt.isPresent()) {
//											UteAzienda uteAziendaEntity = uteAziendaEntityOpt.get();
//											CodStatoUtente codStatoUtente = uteAziendaEntity.getCodStatoUtente();
//											uteAziendaDto.setCodStatoUtente(
//													codStatoUtente != null ? codStatoUtente.getCodStatoUtente() : null);
//										}
//
//										gruppoDto.setAzienda(uteAziendaDto);
//
//										gruppoDto
//												.setCpi(DomainEntityConverter.map(gruppo.getStdCpi(), StdCpiDto.class));
//										gruppoDto.setIsRuoloAbilitato(uteRuo.getIsAbilitato());
//
//										StdOperatoreGgBean operatoreGgDto = DomainEntityConverter
//												.map(gruppo.getStdOperatoreGg(), StdOperatoreGgBean.class);
//										if (operatoreGgDto != null && operatoreGgDto.getCodiceIntermediario() != null) {
//											gruppoDto.setFkCodEntePromotoreGg(operatoreGgDto.getCodiceIntermediario());
//										}
//
//										if(gruppo.getFkCodEntePromotoreRegionale() != null) {
//											gruppoDto.setFkCodEntePromotoreRegionale(gruppo.getFkCodEntePromotoreRegionale());
//										}
//
//										listaUteGruppoDto.add(gruppoDto);
//									}
//
//									profRuoloDto.setIsRuoloAbilitato(uteRuo.getIsAbilitato());
//
//								}
//							}
//							profRuoloDto.setUteGruppi(listaUteGruppoDto);
//						}
//						listaProfRuoloDto.add(profRuoloDto);
//					}
//				}
//
//				utente.setDtLastCheckin(new Date());
//				utente = utenteRepository.save(utente);
//
//				silUserDetails = new SilUserDetails();
//				silUserDetails.setCodTipoAccesso(utente.getCodTipoAccesso().getCodTipoAccesso());
//				silUserDetails.setListaRuoli(listaProfRuoloDto);
//				silUserDetails.setUsername(utente.getUsername());
//				silUserDetails.setNome(utente.getUteCittadino().getNome());
//				silUserDetails.setCognome(utente.getUteCittadino().getCognome());
//				silUserDetails.setIdUtente(utente.getIdUtente());
//				silUserDetails.setCodiceFiscale(utente.getUteCittadino().getCodiceFiscale());
//				silUserDetails.setLastLoginTimestamp(utente.getDtLastCheckin());
//
//				if (listaFunzioni.isEmpty()) // primo accesso, autorizziamo le funzioni di registrazione
//					listaFunzioni.add(new SimpleGrantedAuthority("REGISTR"));
//
//			} else {
//				throw new Exception("Errore di autenticazione ... utente non presente");
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error(e.getMessage());
//		}
        return silUserDetails;
    }

    public Set<SimpleGrantedAuthority> getAutorities() {
        return listaFunzioni;
    }

    public static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }


    public UteUtenteBean createUtenteBySAML(List<Attribute> attributeList) {

        UteUtenteBean utente = new UteUtenteBean();
        try {

            if (attributeList != null) {
                for (Attribute attribute : attributeList) {
                    // List<String> valueList = new ArrayList<>();

                    List<XMLObject> values = attribute.getAttributeValues();
                    String attributesValue = null;

                    if (values != null) {

                        // if (!envProfile.equals("production")) {
                        //
                        // for (int i = 0; i < values.size(); i++) {
                        // XSAnyImpl value = (XSAnyImpl) attribute.getAttributeValues().get(i);
                        // String attributeValue = value.getTextContent();
                        // if (StringUtils.isBlank(attributesValue)) {
                        // attributesValue = attributeValue;
                        // } else {
                        // attributesValue = attributesValue + ", " + attributeValue;
                        // }
                        // }
                        //
                        // switch (attribute.getFriendlyName()) {
                        // case "uid":
                        // utente.setUsername(attributesValue);
                        // break;
                        // case "mail":
                        // utente.setEmail(attributesValue);
                        // break;
                        // default:
                        // break;
                        // }
                        //
                        // } else {

                        for (int i = 0; i < values.size(); i++) {
                            Element value = marshall(attribute.getAttributeValues().get(i));
                            String attributeValue = value.getTextContent();
                            if (StringUtils.isBlank(attributesValue)) {
                                attributesValue = attributeValue;
                            } else {
                                attributesValue = attributesValue + ", " + attributeValue;
                            }
                        }
                        // results.put(attribute.getName(), value);

                        if (!attributesValue.equals("")) {

                            logger.info("Attributo prelevato dalla Response SAML : Attributo: " + attribute.getName()
                                    + " --- Valore : " + attributesValue);

                            /*
                             * codici SPID aggiuntivi
                             *
                             *
                             * Numero di telefono mobile mobilePhone Indirizzo di posta elettronica email
                             * Domicilio fisico address Data di scadenza identit expirationDate Domicilio
                             * digitale digitalAddress
                             *
                             */
                            switch (attribute.getName()) {
                                case "name": // Nome name
                                    // utente.get(attributesValue);
                                    utente.setNameIam(attributesValue);
                                    break;
                                case "familyName": // Cognome familyName
                                    utente.setFamilyNameIam(attributesValue);
                                    break;
                                case "email": // Cognome familyName
                                    utente.setEmail(attributesValue);
                                    break;
                                case "placeOfBirth": // Luogo di nascita placeOfBirth
                                    utente.setPlaceOfBirthIam(attributesValue);
                                    break;
                                case "countyOfBirth": // Provincia di nascita countyOfBirth
                                    utente.setCountyOfBirthIam(attributesValue);
                                    break;
                                case "dateOfBirth": // Data di nascita dateOfBirth
                                    if (attributesValue.contains("-")) {
                                        String inPattern = "yyyy-MM-dd";
                                        String outPattern = "dd/MM/yyyy";
                                        SimpleDateFormat inDateFormat = new SimpleDateFormat(inPattern);
                                        SimpleDateFormat outDateFormat = new SimpleDateFormat(outPattern);
                                        Date date = inDateFormat.parse(attributesValue);
                                        utente.setDateOfBirthIam(outDateFormat.format(date));
                                    } else {
                                        utente.setDateOfBirthIam(attributesValue);
                                    }
                                    break;
                                case "gender": // Sesso gender
                                    utente.setGenderIam(attributesValue.equals("M") ? "M" : "F");
                                    break;
                                case "companyName": // Ragione o denominazione sociale
                                    utente.setCompanyNameIam(attributesValue);
                                    break;
                                case "registeredOffice": // Sede legale registeredOffice
                                    utente.setRegisteredOfficeIam(attributesValue);
                                    break;
                                case "parseFiscalNumber": // Codice fiscale fiscalNumber - prendiamo parseFiscalnumber
                                    /**
                                     * @MirkoManganiello Che attributo generato dal nostro impianto utilissimo in
                                     *                   vece del fiscal number poich, mentre su quest ultimo le
                                     *                   autenticazioni SPID federate (Poste ecc.) rispondono con
                                     *                   TINIT- + codice fiscale utente, nel campo che ho aggiunto
                                     *                   il codice fiscale sempre epurato della stringa codice id
                                     *                   italiano del fiscal number (TINIT- viene rimosso)
                                     */
                                    utente.setUsername(
                                            attributesValue != null ? attributesValue.trim().toUpperCase() : null);
                                    utente.setFiscalNumberIam(attributesValue != null ? attributesValue.trim().toUpperCase() : null);
                                    break;
                                case "ivaCode": // Partita IVA ivaCode
                                    utente.setIvaCodeIam(attributesValue);
                                    break;
                                case "idCard": // Documento d'identit idCard
                                    utente.setIdCardIam(attributesValue);
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return utente;
    }

    /**
     * Marshall an XMLObject. If the XMLObject already has a cached DOM via {@link XMLObject#getDOM()}, that Element will be returned. Otherwise the
     * object will be fully marshalled and that Element returned.
     *
     * @param xmlObject the XMLObject to marshall
     * @return the marshalled Element
     * @throws MarshallingException if there is a problem marshalling the XMLObject
     */
    public static Element marshall(XMLObject xmlObject) throws MarshallingException {

        logger.debug("Marshalling XMLObject");

        if (xmlObject.getDOM() != null) {
            logger.debug("XMLObject already had cached DOM, returning that element");
            return xmlObject.getDOM();
        }

        Marshaller marshaller = Configuration.getMarshallerFactory().getMarshaller(xmlObject);
        if (marshaller == null) {
            logger.error("Unable to marshall XMLOBject, no marshaller registered for object: "
                    + xmlObject.getElementQName());
        }

        Element messageElem = marshaller.marshall(xmlObject);

        if (logger.isTraceEnabled()) {
            logger.trace("Marshalled XMLObject into DOM:");
            logger.trace(XMLHelper.nodeToString(messageElem));
        }

        return messageElem;
    }

}

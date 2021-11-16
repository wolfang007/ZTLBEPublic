delete from public.gest_beneficiario;
commit;
-- cancella le richieste 
delete from public.gest_richiesta;
commit;
-- cancella bandi
delete from public.gest_bando;
commit;
-- cancella la tipologica tippologiabando
delete from public.gest_tp_tipologiabando;
commit;
-- cancella la tipologica statobando
delete from public.gest_tp_statobando;
commit;
delete from public.gest_allegato;
commit;

 

ALTER SEQUENCE public.gest_bando_seq RESTART WITH 1;
ALTER SEQUENCE public.gest_richiesta_seq RESTART WITH 1;
ALTER SEQUENCE public.gest_beneficiario_seq RESTART WITH 1;
ALTER SEQUENCE public.gest_allegato_seq RESTART WITH 1;
ALTER SEQUENCE public.gest_audit_uploadfile_seq RESTART WITH 1;
commit;

 

--POPOLAMENTO DATI

 INSERT INTO public.gest_tp_statobando (id,codice_statobando,descrizione) VALUES
     (1,'codice1','desc1'),
     (2,'codice2','desc2');

 

INSERT INTO public.gest_tp_tipologiabando (id,codice_tipologiabando,descrizione) VALUES
     (1,'codice1','desc1'),
     (2,'codice2','desc2');

 

INSERT INTO public.gest_bando (id_procedimento,codiceidentificativobando,dataattograduatoriadefinitiva,dataattograduatoriapreliminare,datadeliberabando,datafinepresentazionedomande,datainiziopresentazionedomande,estremiattograduatoriadefinitiva,estremiattograduatoriapreliminare,estremideliberabando,oggetto,id_stato_bando,datascadenzagraduatoriapreliminare,importofinanziatototale,id_tipologia_bando,note,data_inizio_validita,data_fine_validita,data_inserimento,data_modifica,ente) VALUES
     (NULL,'324234',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'ewfwe',2,NULL,'100',1,NULL,NULL,NULL,NULL,NULL,'C_DEMO'),
     (NULL,'at13423b',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'bando1',NULL,NULL,NULL,NULL,NULL,'2021-03-26',NULL,'2021-03-26 20:42:57.741807','2021-03-26 20:42:57.741807','C_DEMO'),
     (NULL,'at13425b',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'bando1',NULL,NULL,NULL,NULL,NULL,'2021-03-26',NULL,'2021-03-26 20:42:57.776508','2021-03-26 20:42:57.776508','C_DEMO'),
     (NULL,'AB1726IT',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'INTEGRAZIONE SOCIALE',NULL,NULL,NULL,NULL,NULL,'2021-03-29',NULL,'2021-03-29 09:59:47.834993','2021-03-29 09:59:47.834993','C_DEMO'),
     (NULL,'AB1726ITR',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'INTEGRAZIONE SOCIALE',NULL,NULL,NULL,NULL,NULL,'2021-03-29',NULL,'2021-03-29 10:21:34.881968','2021-03-29 10:21:34.881968','C_DEMO');

 


INSERT INTO public.gest_richiesta (id_istanza,cf_richiedente,id_bando,punteggiograduatoriapreliminare,punteggiograduatoriadefinitiva,importofinanziatorichiesto,importofinanziato,accolto,numprotocolloentrata,numprotocollouscita,data_prot_entrata,data_prot_uscita,nome,cognome,idoneo,note,motivodiniego,numeromandatopagamento,datirichiesta,id_fascicolo,data_inizio_validita,data_fine_validita,data_inserimento,data_modifica,ente) VALUES
     (NULL,'CFTESTEST',1,NULL,'40.0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'{"cognome_richiedente":"Mola","numero_di_telefono_beneficiario":"00000","cognome_beneficiario":"chiamata ad ANPR o APR","processIstanceId":"64848c16-8bbd-11eb-8d5a-0242ac120017","titolo_richiedente":"coniuge","userRegistryId":"5fce1d0d988a12f444636804","cittadinanza_beneficiario":"chiamata ad ANPR o APR","numero_di_telefono_richiedente":"00000","codice_fiscale_beneficiario":"MLONNN59H29Z125G","codice_fiscale_richiedente":"MLONNN59H29Z125G","indirizzo_posta_elettronica_richiedente":"antonino.mola@regione.veneto.it","indirizzo_posta_elettronica_beneficiario":"antonino.mola@regione.veneto.it","cap_residenza_beneficiario":"chiamata ad ANPR o APR","nome_richiedente":"Antonino","nome_beneficiario":"chiamata ad ANPR o APR","tenant":"C_DEMO","comune_residenza_beneficiario":"chiamata ad ANPR o APR","indirizzo_residenza_beneficiario":"chiamata ad ANPR o APR"}',NULL,'2021-03-23',NULL,'2021-03-23 10:54:56','2021-03-23 10:54:56','C_DEMO'),
     (NULL,'CFTESTEST',1,NULL,'82.0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'Antonino','Mola',NULL,NULL,NULL,NULL,'{"cognome_richiedente":"Mola","numero_di_telefono_beneficiario":"00000","cognome_beneficiario":"chiamata ad ANPR o APR","processIstanceId":"64848c16-8bbd-11eb-8d5a-0242ac120017","titolo_richiedente":"coniuge","userRegistryId":"5fce1d0d988a12f444636804","cittadinanza_beneficiario":"chiamata ad ANPR o APR","numero_di_telefono_richiedente":"00000","codice_fiscale_beneficiario":"MLONNN59H29Z125G","codice_fiscale_richiedente":"MLONNN59H29Z125G","indirizzo_posta_elettronica_richiedente":"antonino.mola@regione.veneto.it","indirizzo_posta_elettronica_beneficiario":"antonino.mola@regione.veneto.it","cap_residenza_beneficiario":"chiamata ad ANPR o APR","nome_richiedente":"Antonino","nome_beneficiario":"chiamata ad ANPR o APR","tenant":"C_DEMO","comune_residenza_beneficiario":"chiamata ad ANPR o APR","indirizzo_residenza_beneficiario":"chiamata ad ANPR o APR"}',NULL,'2021-03-23',NULL,'2021-03-23 10:54:56.064','2021-03-23 10:54:56.064','C_DEMO'),
     (NULL,'CFTESTEST',1,NULL,'98.0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'Antonino','Mola',NULL,NULL,NULL,NULL,'{"cognome_richiedente":"Mola","numero_di_telefono_beneficiario":"00000","cognome_beneficiario":"chiamata ad ANPR o APR","processIstanceId":"86b95a46-8bce-11eb-b902-0242ac120016","titolo_richiedente":"coniuge","userRegistryId":"5fce1d0d988a12f444636804","cittadinanza_beneficiario":"chiamata ad ANPR o APR","numero_di_telefono_richiedente":"00000","codice_fiscale_beneficiario":"MLONNN59H29Z125G","codice_fiscale_richiedente":"MLONNN59H29Z125G","indirizzo_posta_elettronica_richiedente":"antonino.mola@regione.veneto.it","indirizzo_posta_elettronica_beneficiario":"antonino.mola@regione.veneto.it","cap_residenza_beneficiario":"chiamata ad ANPR o APR","nome_richiedente":"Antonino","nome_beneficiario":"chiamata ad ANPR o APR","tenant":"C_DEMO","comune_residenza_beneficiario":"chiamata ad ANPR o APR","indirizzo_residenza_beneficiario":"chiamata ad ANPR o APR"}',NULL,'2021-03-23',NULL,'2021-03-23 12:57:28.019','2021-03-23 12:57:28.019','C_DEMO'),
     (NULL,'CFTESTEST',4,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'Antonio','Mola',NULL,NULL,NULL,NULL,'{"codice_fiscale_richiedente":"TYT675UIYIT878UH"}',NULL,'2021-03-29',NULL,'2021-03-29 10:21:34.952745','2021-03-29 10:21:34.952745','C_DEMO'),
     (NULL,'CFTESTEST',4,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'Antonio','Mola',NULL,NULL,NULL,NULL,'{"codice_fiscale_richiedente":"TYT675UIYT878UHE"}',NULL,'2021-03-29',NULL,'2021-03-29 10:21:34.99187','2021-03-29 10:21:34.99187','C_DEMO'),
     (NULL,'CFTESTEST',4,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'Antonio','Mola',NULL,NULL,NULL,NULL,'{"codice_fiscale_richiedente":"TYT675UIYT87D8UH"}',NULL,'2021-03-29',NULL,'2021-03-29 10:21:35.008377','2021-03-29 10:21:35.008377','C_DEMO'),
     (NULL,'CFTESTEST',4,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'Antonio','Mola',NULL,NULL,NULL,NULL,'{"codice_fiscale_richiedente":"TYT675UIYT87A8UH"}',NULL,'2021-03-29',NULL,'2021-03-29 10:21:35.024857','2021-03-29 10:21:35.024857','C_DEMO'),
     (NULL,'CFTESTEST',3,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'maurizio','lamberti',NULL,NULL,NULL,NULL,'{"codice_fiscale_richiedente":"FJLKHNJH"}',NULL,'2021-03-26',NULL,'2021-03-26 20:42:57.748386','2021-03-26 20:42:57.748386','C_DEMO'),
     (NULL,'CFTESTEST',3,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'mario','rossi',NULL,NULL,NULL,NULL,'{"codice_fiscale_richiedente":"NJFHYTU"}',NULL,'2021-03-26',NULL,'2021-03-26 20:42:57.762986','2021-03-26 20:42:57.762986','C_DEMO'),
     (NULL,'CFTESTEST',2,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'giovanni','tozzi',NULL,NULL,NULL,NULL,'{"codice_fiscale_richiedente":"TUYITFYU"}',NULL,'2021-03-26',NULL,'2021-03-26 20:42:57.781987','2021-03-26 20:42:57.781987','C_DEMO');
INSERT INTO public.gest_richiesta (id_istanza,cf_richiedente,id_bando,punteggiograduatoriapreliminare,punteggiograduatoriadefinitiva,importofinanziatorichiesto,importofinanziato,accolto,numprotocolloentrata,numprotocollouscita,data_prot_entrata,data_prot_uscita,nome,cognome,idoneo,note,motivodiniego,numeromandatopagamento,datirichiesta,id_fascicolo,data_inizio_validita,data_fine_validita,data_inserimento,data_modifica,ente) VALUES
     (NULL,'CFTESTEST',2,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'michele','bindi',NULL,NULL,NULL,NULL,'{"codice_fiscale_richiedente":"MFGFDUYT"}',NULL,'2021-03-26',NULL,'2021-03-26 20:42:57.792114','2021-03-26 20:42:57.792114','C_DEMO'),
     (NULL,'CFTESTEST',3,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'maurizio','lamberti',NULL,NULL,NULL,NULL,'{"codice_fiscale_richiedente":"FJLKHNJH"}',NULL,'2021-03-26',NULL,'2021-03-26 20:43:01.550663','2021-03-26 20:43:01.550663','C_DEMO'),
     (NULL,'CFTESTEST',3,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'mario','rossi',NULL,NULL,NULL,NULL,'{"codice_fiscale_richiedente":"NJFHYTU"}',NULL,'2021-03-26',NULL,'2021-03-26 20:43:01.561795','2021-03-26 20:43:01.561795','C_DEMO'),
     (NULL,'CFTESTEST',2,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'giovanni','tozzi',NULL,NULL,NULL,NULL,'{"codice_fiscale_richiedente":"TUYITFYU"}',NULL,'2021-03-26',NULL,'2021-03-26 20:43:01.57598','2021-03-26 20:43:01.57598','C_DEMO'),
     (NULL,'CFTESTEST',2,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'michele','bindi',NULL,NULL,NULL,NULL,'{"codice_fiscale_richiedente":"MFGFDUYT"}',NULL,'2021-03-26',NULL,'2021-03-26 20:43:01.587524','2021-03-26 20:43:01.587524','C_DEMO'),
     (NULL,'CFTESTEST',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'Antonio','Mola',NULL,NULL,NULL,NULL,'{"codice_fiscale_richiedente":"TYT675UIYIT878UH"}',NULL,'2021-03-29',NULL,'2021-03-29 09:59:47.946042','2021-03-29 09:59:47.946042','C_DEMO'),
     (NULL,'CFTESTEST',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'Antonio','Mola',NULL,NULL,NULL,NULL,'{"codice_fiscale_richiedente":"TYT675UIYT878UHE"}',NULL,'2021-03-29',NULL,'2021-03-29 09:59:47.993567','2021-03-29 09:59:47.993567','C_DEMO'),
     (NULL,'CFTESTEST',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'Antonio','Mola',NULL,NULL,NULL,NULL,'{"codice_fiscale_richiedente":"TYT675UIYT87D8UH"}',NULL,'2021-03-29',NULL,'2021-03-29 09:59:48.010345','2021-03-29 09:59:48.010345','C_DEMO'),
     (NULL,'CFTESTEST',5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'Antonio','Mola',NULL,NULL,NULL,NULL,'{"codice_fiscale_richiedente":"TYT675UIYT87A8UH"}',NULL,'2021-03-29',NULL,'2021-03-29 09:59:48.025467','2021-03-29 09:59:48.025467','C_DEMO');

 

 

INSERT INTO public.gest_beneficiario (id_richiesta,id_bando,nome,cognome,codice_fiscale,telefono,cellulare,email,residende_comune,cittadino_ue,data_inizio_validita,data_fine_validita,data_inserimento,data_modifica,ente) VALUES
     (1,1,'ccc','vvv','ddd',NULL,'34344',NULL,true,true,NULL,NULL,NULL,NULL,'C_DEMO'),
     (2,1,'maurizio','lamberti','FJLKHNJH',NULL,NULL,NULL,NULL,NULL,'2021-03-26',NULL,'2021-03-26 20:42:57.75804','2021-03-26 20:42:57.75804','C_DEMO'),
     (3,1,'mario','rossi','NJFHYTU',NULL,NULL,NULL,NULL,NULL,'2021-03-26',NULL,'2021-03-26 20:42:57.769068','2021-03-26 20:42:57.769068','C_DEMO'),
     (4,2,'giovanni','tozzi','TUYITFYU',NULL,NULL,NULL,NULL,NULL,'2021-03-26',NULL,'2021-03-26 20:42:57.78718','2021-03-26 20:42:57.78718','C_DEMO'),
     (5,2,'michele','bindi','MFGFDUYT',NULL,NULL,NULL,NULL,NULL,'2021-03-26',NULL,'2021-03-26 20:42:57.797785','2021-03-26 20:42:57.797785','C_DEMO'),
     (6,2,'maurizio','lamberti','FJLKHNJH',NULL,NULL,NULL,NULL,NULL,'2021-03-26',NULL,'2021-03-26 20:43:01.556322','2021-03-26 20:43:01.556322','C_DEMO'),
     (7,2,'mario','rossi','NJFHYTU',NULL,NULL,NULL,NULL,NULL,'2021-03-26',NULL,'2021-03-26 20:43:01.566648','2021-03-26 20:43:01.566648','C_DEMO'),
     (8,3,'giovanni','tozzi','TUYITFYU',NULL,NULL,NULL,NULL,NULL,'2021-03-26',NULL,'2021-03-26 20:43:01.582601','2021-03-26 20:43:01.582601','C_DEMO'),
     (9,3,'michele','bindi','MFGFDUYT',NULL,NULL,NULL,NULL,NULL,'2021-03-26',NULL,'2021-03-26 20:43:01.60006','2021-03-26 20:43:01.60006','C_DEMO'),
     (10,3,'FRANCESCO','ROSSI','TYT675UIYIT878UH',NULL,NULL,NULL,NULL,NULL,'2021-03-29',NULL,'2021-03-29 09:59:47.976511','2021-03-29 09:59:47.976511','C_DEMO');
INSERT INTO public.gest_beneficiario (id_richiesta,id_bando,nome,cognome,codice_fiscale,telefono,cellulare,email,residende_comune,cittadino_ue,data_inizio_validita,data_fine_validita,data_inserimento,data_modifica,ente) VALUES
     (11,3,'LUCA','GRIMALDI','TYT675UIYT878UHE',NULL,NULL,NULL,NULL,NULL,'2021-03-29',NULL,'2021-03-29 09:59:48.003061','2021-03-29 09:59:48.003061','C_DEMO'),
     (12,4,'MIRCO','MAGRINI','TYT675UIYT87D8UH',NULL,NULL,NULL,NULL,NULL,'2021-03-29',NULL,'2021-03-29 09:59:48.018209','2021-03-29 09:59:48.018209','C_DEMO'),
     (13,4,'GIONANNI','LUPPAZZO','TYT675UIYT87A8UH',NULL,NULL,NULL,NULL,NULL,'2021-03-29',NULL,'2021-03-29 09:59:48.033185','2021-03-29 09:59:48.033185','C_DEMO'),
     (14,4,'FRANCESCO2','ROSSI2','TYT675UIYIT878UH',NULL,NULL,NULL,NULL,NULL,'2021-03-29',NULL,'2021-03-29 10:21:34.977821','2021-03-29 10:21:34.977821','C_DEMO'),
     (15,5,'LUCA2','GRIMALDI2','TYT675UIYT878UHE',NULL,NULL,NULL,NULL,NULL,'2021-03-29',NULL,'2021-03-29 10:21:35.001342','2021-03-29 10:21:35.001342','C_DEMO'),
     (16,5,'MIRCO2','MAGRINI2','TYT675UIYT87D8UH',NULL,NULL,NULL,NULL,NULL,'2021-03-29',NULL,'2021-03-29 10:21:35.01811','2021-03-29 10:21:35.01811','C_DEMO'),
     (17,5,'GIONANNI2','LUPPAZZO2','TYT675UIYT87A8UH',NULL,NULL,NULL,NULL,NULL,'2021-03-29',NULL,'2021-03-29 10:21:35.033722','2021-03-29 10:21:35.033722','C_DEMO');

 

 

INSERT INTO public.gest_allegato (id_richiesta,user_id,descrizione_allegato,nr_prot,data_prot,procid,idmybox,fieldname,nomefile,mimetype,length,id_file,tipologia,note,data_inizio_validita,data_fine_validita,data_inserimento,data_modifica,ente) VALUES
     (1,'USER','allegato1 desc','12312',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
     (1,'USER','allegato2 desc','rwerr',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
    
    
ALTER SEQUENCE public.gest_bando_seq RESTART WITH 20;
ALTER SEQUENCE public.gest_richiesta_seq RESTART WITH 20;
ALTER SEQUENCE public.gest_beneficiario_seq RESTART WITH 20;
ALTER SEQUENCE public.gest_allegato_seq RESTART WITH 20;
ALTER SEQUENCE public.gest_audit_uploadfile_seq RESTART WITH 20;

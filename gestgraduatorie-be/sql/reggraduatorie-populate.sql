
--     _____ __________  __  _________   ___________________
--    / ___// ____/ __ \/ / / / ____/ | / / ____/ ____/ ___/
--    \__ \/ __/ / / / / / / / __/ /  |/ / /   / __/  \__ \ 
--   ___/ / /___/ /_/ / /_/ / /___/ /|  / /___/ /___ ___/ / 
--  /____/_____/\___\_\____/_____/_/ |_/\____/_____//____/  
--  


-- SEQUENCE: public.gest_bando_seq

-- DROP SEQUENCE public.gest_bando_seq;

CREATE SEQUENCE public.gest_bando_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1;

ALTER SEQUENCE public.gest_bando_seq
    OWNER TO reggraduatorie;
	
-- SEQUENCE: public.gest_richiesta_seq

-- DROP SEQUENCE public.gest_richiesta_seq;

CREATE SEQUENCE public.gest_richiesta_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1;

ALTER SEQUENCE public.gest_richiesta_seq
    OWNER TO reggraduatorie;
	
-- SEQUENCE: public.gest_beneficiario_seq

-- DROP SEQUENCE public.gest_beneficiario_seq;

CREATE SEQUENCE public.gest_beneficiario_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1;

ALTER SEQUENCE public.gest_beneficiario_seq
    OWNER TO reggraduatorie;
	
-- SEQUENCE: public.gest_allegato_seq

-- DROP SEQUENCE public.gest_allegato_seq;

CREATE SEQUENCE public.gest_allegato_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1;

ALTER SEQUENCE public.gest_allegato_seq
    OWNER TO reggraduatorie;
	
-- SEQUENCE: public.gest_audit_uploadfile_seq

-- DROP SEQUENCE public.gest_audit_uploadfile_seq;    
    
CREATE SEQUENCE public.gest_audit_uploadfile_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1;

ALTER SEQUENCE public.gest_audit_uploadfile_seq
    OWNER TO reggraduatorie; 
    
-- Table: public.gest_bando

-- DROP TABLE public.gest_bando;

CREATE TABLE public.gest_bando
(
    id bigint NOT NULL DEFAULT nextval('gest_bando_seq'::regclass),
    id_procedimento character varying(30) COLLATE pg_catalog."default",
    codiceidentificativobando character varying(30) COLLATE pg_catalog."default",
    dataattograduatoriadefinitiva date,
	dataattograduatoriapreliminare date,
	datadeliberabando date,
	datafinepresentazionedomande timestamp without time zone,
	datainiziopresentazionedomande timestamp without time zone,
    estremiattograduatoriadefinitiva character varying(100) COLLATE pg_catalog."default",
	estremiattograduatoriapreliminare character varying(100) COLLATE pg_catalog."default",
	estremideliberabando character varying(100) COLLATE pg_catalog."default",
	oggetto character varying(250) COLLATE pg_catalog."default",
	id_stato_bando bigint,
	datascadenzagraduatoriapreliminare timestamp without time zone,
	importofinanziatototale character varying(6) COLLATE pg_catalog."default",
	id_tipologia_bando bigint,
    note character varying(4000) COLLATE pg_catalog."default",
    data_inizio_validita date,
    data_fine_validita date,
    data_inserimento timestamp without time zone,
    data_modifica timestamp without time zone,
    ente character varying(6) COLLATE pg_catalog."default",
    CONSTRAINT gest_bando_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.gest_bando
    OWNER to reggraduatorie;
	


-- Table: public.gest_richiesta

-- DROP TABLE public.gest_richiesta;

CREATE TABLE public.gest_richiesta
(
    id bigint NOT NULL DEFAULT nextval('gest_richiesta_seq'::regclass),
    id_istanza character varying(16) COLLATE pg_catalog."default",
    cf_richiedente character varying(16) COLLATE pg_catalog."default",
	id_bando bigint,
	punteggiograduatoriapreliminare character varying(6) COLLATE pg_catalog."default",
	punteggiograduatoriadefinitiva character varying(6) COLLATE pg_catalog."default",
	importofinanziatorichiesto character varying(6) COLLATE pg_catalog."default",
	importoFinanziato character varying(6) COLLATE pg_catalog."default",
	accolto	boolean,
	numprotocolloentrata character varying(16) COLLATE pg_catalog."default",
	numprotocollouscita character varying(16) COLLATE pg_catalog."default",
	data_prot_entrata timestamp without time zone,
	data_prot_uscita timestamp without time zone,
    nome character varying(16) COLLATE pg_catalog."default",
	cognome character varying(16) COLLATE pg_catalog."default",
	idoneo	boolean,
	note character varying(4000) COLLATE pg_catalog."default",
	motivodiniego character varying(4000) COLLATE pg_catalog."default",
	numeromandatopagamento character varying(16) COLLATE pg_catalog."default",
	datiRichiesta 	json,
	id_fascicolo	character  varying(16) COLLATE pg_catalog."default",
    data_inizio_validita date,
    data_fine_validita date,
    data_inserimento timestamp without time zone,
    data_modifica timestamp without time zone,
    ente character varying(6) COLLATE pg_catalog."default",
    CONSTRAINT gest_richiesta_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.gest_richiesta
    OWNER to reggraduatorie;
	

-- Table: public.gest_beneficiario

-- DROP TABLE public.gest_beneficiario;

CREATE TABLE public.gest_beneficiario
(
    id bigint NOT NULL DEFAULT nextval('gest_beneficiario_seq'::regclass),
	id_richiesta bigint,
	id_bando bigint,
    nome character varying(16) COLLATE pg_catalog."default",
	cognome character varying(16) COLLATE pg_catalog."default",
	codice_fiscale character varying(16) COLLATE pg_catalog."default",
	telefono character varying(16) COLLATE pg_catalog."default",
	cellulare character varying(16) COLLATE pg_catalog."default",
	email character varying(30) COLLATE pg_catalog."default",
	residende_comune boolean,
	cittadino_ue boolean,
    data_inizio_validita date,
    data_fine_validita date,
    data_inserimento timestamp without time zone,
    data_modifica timestamp without time zone,
    ente character varying(6) COLLATE pg_catalog."default",
    CONSTRAINT gest_beneficiario_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.gest_beneficiario
    OWNER to reggraduatorie;
	

-- Table: public.gest_allegato

-- DROP TABLE public.gest_allegato;


CREATE TABLE public.gest_allegato
(

    id bigint NOT NULL DEFAULT nextval('gest_allegato_seq'::regclass),
    id_richiesta bigint,
    user_id character varying(16) COLLATE pg_catalog."default",
	descrizione_allegato character varying(100) COLLATE pg_catalog."default",
	nr_prot character varying(16) COLLATE pg_catalog."default",
	data_prot timestamp without time zone,
	procid character varying(255) COLLATE pg_catalog."default",
	idmybox character varying(255) COLLATE pg_catalog."default",
	fieldname character varying(100) COLLATE pg_catalog."default",
	nomefile character varying(100) COLLATE pg_catalog."default",
	mimetype character varying(100) COLLATE pg_catalog."default",
	length integer,
	id_file character varying(100) COLLATE pg_catalog."default",
	tipologia character varying(100) COLLATE pg_catalog."default",
	note character varying(4000) COLLATE pg_catalog."default",
    data_inizio_validita date,
    data_fine_validita date,
    data_inserimento timestamp without time zone,
    data_modifica timestamp without time zone,
    ente character varying(6) COLLATE pg_catalog."default",
    CONSTRAINT gest_allegato_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.gest_allegato
    OWNER to reggraduatorie;


-- Table: public.gest_tp_tipologiabando

-- DROP TABLE public.gest_tp_tipologiabando;

CREATE TABLE public.gest_tp_tipologiabando
(
    id bigint NOT NULL,
	codice_tipologiabando character varying(100),
    descrizione character varying(100) COLLATE pg_catalog."default",
    CONSTRAINT gest_tp_tipologiabando_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.gest_tp_tipologiabando
    OWNER to reggraduatorie;


-- Table: public.gest_audit_uploadfile

-- DROP TABLE public.gest_audit_uploadfile;

CREATE TABLE public.gest_audit_uploadfile
(
    id bigint NOT NULL DEFAULT nextval('gest_audit_uploadfile_seq'::regclass),
    nome_utente character varying(50) COLLATE pg_catalog."default",
    id_file character varying COLLATE pg_catalog."default",
    data_caricamento timestamp without time zone,
    id_bando bigint,
    esito character varying COLLATE pg_catalog."default",
    CONSTRAINT gest_audit_uploadfile_pkey PRIMARY KEY (id),
    CONSTRAINT idbando_fk FOREIGN KEY (id_bando)
        REFERENCES public.gest_bando (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;
ALTER TABLE public.gest_audit_uploadfile
    OWNER to reggraduatorie;    

-- Table: public.gest_tp_statibando

-- DROP TABLE public.gest_tp_statibando;

CREATE TABLE public.gest_tp_statobando
(
    id bigint NOT NULL,
	codice_statobando character varying(100),
    descrizione character varying(100) COLLATE pg_catalog."default",
    CONSTRAINT gest_tp_statobando_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.gest_tp_statobando
    OWNER to reggraduatorie;
	
ALTER TABLE ONLY public.gest_richiesta
    ADD CONSTRAINT fk_gest_richiesta_id_bando FOREIGN KEY (id_bando) REFERENCES public.gest_bando(id);
	
ALTER TABLE ONLY public.gest_allegato
    ADD CONSTRAINT fk_gest_allegato_id_richiesta FOREIGN KEY (id_richiesta) REFERENCES public.gest_richiesta(id);
	
ALTER TABLE ONLY public.gest_beneficiario
    ADD CONSTRAINT fk_gest_beneficiario_id_bando FOREIGN KEY (id_bando) REFERENCES public.gest_bando(id);

ALTER TABLE ONLY public.gest_beneficiario
    ADD CONSTRAINT fk_gest_beneficiario_id_richiesta FOREIGN KEY (id_richiesta) REFERENCES public.gest_richiesta(id);
	


ALTER TABLE ONLY public.gest_bando
    ADD CONSTRAINT fk_gest_bando_id_stato_bando FOREIGN KEY (id_stato_bando) REFERENCES public.gest_tp_statobando(id);
	
ALTER TABLE ONLY public.gest_bando
    ADD CONSTRAINT fk_gest_bando_id_tipologia_bando FOREIGN KEY (id_tipologia_bando) REFERENCES public.gest_tp_tipologiabando(id);
    
    
INSERT INTO public.gest_tp_statobando ( id, codice_statobando, descrizione ) VALUES (1, 'APERTO', 'Aperto');
INSERT INTO public.gest_tp_statobando ( id, codice_statobando, descrizione ) VALUES (2, 'CHIUSO', 'Chiuso');

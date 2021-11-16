-- cancella allegati     --
delete from public.gest_allegato;

-- cancella beneficiario
delete from public.gest_beneficiario;

-- cancella le richieste 
delete from public.gest_richiesta;

-- cancella bandi
delete from public.gest_bando;

-- cancella la tipologica tippologiabando
delete from public.gest_tp_tipologiabando;

-- cancella la tipologica statobando
delete from public.gest_tp_statobando;

-- cancella la gest_audit_uploadfile
delete from public.gest_audit_uploadfile;


ALTER SEQUENCE public.gest_bando_seq RESTART WITH 1;
ALTER SEQUENCE public.gest_richiesta_seq RESTART WITH 1;
ALTER SEQUENCE public.gest_beneficiario_seq RESTART WITH 1;
ALTER SEQUENCE public.gest_allegato_seq RESTART WITH 1;
ALTER SEQUENCE public.gest_audit_uploadfile_seq RESTART WITH 1;



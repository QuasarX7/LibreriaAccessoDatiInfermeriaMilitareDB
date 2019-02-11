package it.quasar_x7.infermeria.DaseDati;

/**
 * Classe che raccoglie gli indice dei nomi di ogni tabella.
 * 
 * @author Dott. Domenico della PERUTA
 */
public class BASE_DATI {

    public static class TABELLA{
        public static final String MILITARE             = "militare"; 
        public static final String ANAMNESI_VACCINALE   = "anamnesi_seduta_vaccinale"; 
        public static final String VISITE               = "visita"; 
        public static final String RICOVERO             = "ricovero"; 
    }
    
    public static class MILITARE {

        public static final int COGNOME = 0;
        public static final int NOME = 1;
        public static final int DATA_NASCITA = 2;
        public static final int LUOGO_NASCITA = 3;
        public static final int GRADO = 4;
        public static final int SCUOLA = 5;
        public static final int COMPAGNIA = 6;
        public static final int ANAMNESI_FAMILIARE = 7;
        public static final int ANAMNESI_REMOTA = 8;
        public static final int ANAMNESI_PROSSIMA = 9;
        public static final int ALTEZZA = 10;
        public static final int PESO = 11;
        public static final int TORACE = 12;
        public static final int VISUS_OD = 13;
        public static final int VISUS_OS = 14;
        public static final int VISUS_OD_CORRETTO = 15;
        public static final int VISUS_OS_CORRETTO = 16;
        public static final int PRESSIONE_MAX = 17;
        public static final int PRESSIONE_MIN = 18;
        public static final int FREQUENZA = 19;
        public static final int AUDIOMETRIA_DX = 20;
        public static final int AUDIOMETRIA_SX = 21;
        public static final int DATA_GML = 22;
        public static final int GML = 23;
        public static final int CORSO = 24;
        public static final int UTENTE = 25;
        public static final int FAVISMO = 26;
        public static final int ASSENZA_DOC_FAVISMO = 27;
        public static final int MODIFICA = 28;
        public static final int MEDICO = 29;
        public static final int SESSO = 30;
        public static final int DOCUMENTO = 31;
        public static final int NR_DOCUMENTO = 32;
        public static final int RESIDENZA = 33;
        public static final int INDIRIZZO = 34;
        public static final int INCARICO = 35;
        public static final int ASL = 36;
    }

    public static class ANAMNESI_VACCINALE {

        public static final int COGNOME = 0;
        public static final int NOME = 1;
        public static final int DATA_NASCITA = 2;
        public static final int DATA_VACCINAZIONE = 3;
        public static final int ALLERGIA_POLLO = 4;
        public static final int ALLERGIA_ANATRA = 5;
        public static final int ALLERGIA_BOVINO = 6;
        public static final int ALLERGIA_FOLMALDEIDE = 7;
        public static final int ALLERGIA_NEOMICINA = 8;
        public static final int ALLERGIA_STRETTOMICINA = 9;
        public static final int ALLERGIA_KANAMICINA = 10;
        public static final int ALLERGIA_POLIMIXIN_B = 11;
        public static final int ALLERGIA_MERCURIALI = 12;
        public static final int ALLERGIA_ALTRO = 13;
        public static final int CONVINVENTI_IMMUNODEPRESSIVI = 14;
        public static final int CONVINVENTI_TUMORI_SANGUE = 15;
        public static final int CONVINVENTI_GRAVIDANZA = 16;
        public static final int FEBBRE = 17;
        public static final int DISTURBI_VIE_AEREE = 18;
        public static final int DIARREA = 19;
        public static final int TERAPIA_RECENTE = 20;
        public static final int TRASFUSIONE = 21;
        public static final int PERIODICITA_MESTRUALE = 22;
        public static final int ULTIMA_MESTRUAZIONE = 23;
        public static final int STATO_GRAVIDANZA = 24;
        public static final int TEST_GRAVIDANZA = 25;
        public static final int DATA_TEST_GRAVIDANZA = 26;
        public static final int MEDICO = 27;
        public static final int UTENTE = 28;
    }

    
    public static class MODELLO_GL {

        public static final int COGNOME = 0;
        public static final int NOME = 1;
        public static final int DATA_NASCITA = 2;
        public static final int DATA_VERBALE = 3;
        public static final int ELEMENTI_INFO = 4;
        public static final int RELAZIONE_ALLEGATA = 5;
        public static final int DATA_FRUIZIONE_GIORNI_ASS = 6;
        public static final int GIORNI_ASSENZA = 7;
        public static final int PROSPETTO_ASSENZA = 8;
        public static final int NR_ALL_PROSPETTO_ASSENZA = 9;
        public static final int DOC_MATRICOLARE = 10;
        public static final int NR_ALL_DOC_MATRICOLARE = 11;
        public static final int COPIA_CERTIF = 12;
        public static final int NR_ALLEGATI_CERTIF = 13;
        public static final int SUPERAMENTO_ASS_CONV = 14;
        public static final int TERMINE_ASSENZA = 15;
        public static final int GIORNI_PERIODO_ASSENZA = 16;
        public static final int PRESUNTA_INABILITA = 17;
        public static final int ALTRA_FOR_INV = 18;
        public static final int NOME_ALTRA_FOR_INV = 19;
        public static final int ACCERTAMENTO_CAUSA_SERV = 20;
        public static final int SU_RICHIESTA = 21;
        public static final int ALTRO = 22;
        public static final int NOTE = 23;
        public static final int TEMP_NON_IDONEO = 24;
        public static final int ALTRI_ELEMENTI_INFO = 25;
        public static final int ANAMNESI = 26;
        public static final int ESAME_OBIETTIVO = 27;
        public static final int DIAGNOSI = 28;
        public static final int MEDICO = 29;
        public static final int DOBBI_IDONEITA = 30;
    }

    
    public static class MODELLO_ML {

        public static final int COGNOME = 0;
        public static final int NOME = 1;
        public static final int DATA_NASCITA = 2;
        public static final int DATA_VERBALE = 3;
        public static final int PROTOCOLLO = 4;
        public static final int RICHIESTA = 5;
        public static final int VISITA_MEDICA = 6;
        public static final int ESAME_CERTIFICATO = 7;
        public static final int INCARICO = 8;
        public static final int REPARTO = 9;
        public static final int IDONEO_SERVIZIO = 10;
        public static final int TO = 11;
        public static final int TO_IDONEO = 12;
        public static final int TO_NON_IDONEO = 13;
        public static final int TO_TEMP_NON_IDONEO = 14;
        public static final int TO_TEMP_NON_IDONEO_MESI = 15;
        public static final int CONTR_PROVE_OPERATIVE_ASSENTI = 16;
        public static final int CONTR_PROVE_OPERATIVE_PRESENTI = 17;
        public static final int CONTR_PROVE_OPERATIVE_TEMP = 18;
        public static final int CONTR_PROVE_OPERATIVE_TEMP_PERIODO = 19;
        public static final int SOTTOPORRE_VISITA_FISCALE = 20;
        public static final int AMMALATO = 21;
        public static final int AMMALATO_FINE = 22;
        public static final int CONVALESCENZA = 23;
        public static final int CONVALESCENZA_FINO = 24;
        public static final int TEMP_NON_IDONO = 25;
        public static final int TEMP_NON_IDONEO_INVIO_CMO = 26;
        public static final int TEMP_NON_IDONEO_INVIO_OSSERVAZIONE = 27;
        public static final int IMPIEGO_TECNICO_AMM = 28;
        public static final int IMPIEGO_TECNICO_AMM_INV_CMO = 29;
        public static final int IMPIEGO_TECNICO_AMM_INV_OSSERVAZIONE = 30;
        public static final int INFERMITA_LESIONI = 31;
        public static final int INFERMITA_LESIONE_ESCLUSIVANTE = 32;
        public static final int INFERMITA_LESIONE_MISURA_PREV = 33;
        public static final int INFERMITA_LESIONE_CAUSA_SERV = 34;
        public static final int INFERMITA_LESIONE_NON_CAUSA_SERV = 35;
        public static final int INFERMITA_LESIONE_IN_ACCERTAMENTO = 36;
        public static final int INFERMITA_LESIONE_NON_OGGETTO_ACCERTAMENTO = 37;
        public static final int FERITE_LESIONE_TRAUMATICHE = 38;
        public static final int INFERMITA_MISSIONE = 39;
        public static final int NOTE_CONTR_INDICAZIONI = 40;
        public static final int VISITA_PERIODICA = 41;
        public static final int CONTROLLO_CERTIFICATO_MEDICO = 42;
        public static final int VISITA_PREIMPIEGO = 43;
        public static final int VISITA_RIEMTRO_TO = 44;
        public static final int VISITA_RIENTRO_MALATTIA = 45;
        public static final int VISITA_FISCALE = 46;
        public static final int VISITA_RICHIESTA = 47;
        public static final int ALTRA_VISITA = 48;
        public static final int TIPO_ALTRA_VISITA = 49;
        public static final int IDONEITA_INCOND = 50;
        public static final int IDONEITA_PARZIALE = 51;
        public static final int FUMO_SI = 52;
        public static final int FUMO_NO = 53;
        public static final int NR_SIGARETTE = 54;
        public static final int NR_SIGARETTE_DAL = 55;
        public static final int EX_FUMATORE = 56;
        public static final int EX_FUMATORE_DAL = 57;
        public static final int ALCOOL_SI = 58;
        public static final int ALCOOL_NO = 59;
        public static final int ALVO = 60;
        public static final int DIURESI = 61;
        public static final int RITMO_SOGNO_VEGLIA = 62;
        public static final int VACCINAZIONE_COMPLETA = 63;
        public static final int TIPO_VACC_DA_COMPLETARE = 64;
        public static final int VACC_NON_DESUMIBILE = 65;
        public static final int ANAMNESI_PATOLOGICA_REMOTA = 66;
        public static final int ANAMNESI_PATOLOGICA_PROSSIMA = 67;
        public static final int INTOLLERANZA_ALIMENTARE = 68;
        public static final int INTOLLERANZE_ALLERGIE = 69;
        public static final int TERAPIA_IN_ATTO = 70;
        public static final int PESO = 71;
        public static final int STATURA = 72;
        public static final int COND_GENERALI = 73;
        public static final int VISTA = 74;
        public static final int UDITO = 75;
        public static final int AZIONE_CARDIACA = 76;
        public static final int FREQUENZA = 77;
        public static final int PRESSIONE_MAX = 78;
        public static final int PRESSIONE_MIN = 79;
        public static final int ESAME_OBIETTIVO_LOC = 80;
        public static final int EO_NULLA_RILEVANTE = 81;
        public static final int ACCERTAMENTI = 82;
        public static final int DIAGNOSI = 83;
        public static final int NOTE = 84;
        public static final int MEDICO = 85;
        public static final int ECCESSO_PONDERALE = 86;
        public static final int GIORNI_ECCESSO_PONTERALE = 87;
    }

    
    public static class REAZIONE_VACCINALE {

        public static final int DIMENSIONE_TABELLA = 9;
        public static final int COGNOME = 0;
        public static final int NOME = 1;
        public static final int DATA_NASCITA = 2;
        public static final int DATA_VACCINAZIONE = 3;
        public static final int VACCINO = 4;
        public static final int LOCALI_GRAVI = 5;
        public static final int GENERALI_LIEVI = 6;
        public static final int GENERALI_GRAVI = 7;
        public static final int DATA_EVENTO = 8;
    }

    /**
     *
     * @author Ninja
     */
    public static class RICOVERO {

        public static final int COGNOME = 0;
        public static final int NOME = 1;
        public static final int DATA_NASCITA = 2;
        public static final int DATA_INGRESSO = 3;
        public static final int DATA_USCITA = 4;
        public static final int ANAMNESI = 5;
        public static final int DATI_CLINICI = 6;
        public static final int ESAME_OBBIETTIVO = 7;
        public static final int DIAGNOSI = 8;
        public static final int TERAPIA = 9;
        public static final int PML = 10;
        public static final int UTENTE = 11;
        public static final int MODIFICA = 12;
    }

    
    public static class STORIA_VACCINALE {

        public static final int COGNOME = 0;
        public static final int NOME = 1;
        public static final int DATA_NASCITA = 2;
        public static final int PROFILASSI = 3;
        public static final int PREGRESSA = 4;
        public static final int PREGRESSA_DOC = 5;
        public static final int VACCINAZIONE_CIVILE = 6;
        public static final int VACCINAZIONE_MILITARE = 7;
        public static final int DOSE = 8;
        public static final int SIGLA_TIPO_VACCINO = 9;
        public static final int DATA_VACCINAZIONE = 10;
        public static final int MEDICO = 11;
        public static final int UTENTE = 12;
        public static final int MODIFICA = 13;
    }

    
    public static class VACCINAZIONI {

        public static final int DATA_SEDUTA = 0;
        public static final int TIPO_PROFILASSI = 1;
        public static final int COGNOME = 2;
        public static final int NOME = 3;
        public static final int DATA_NASCITA = 4;
        public static final int DOSE = 5;
        public static final int VIA_SOMMINISTRAZIONE = 6;
        public static final int NOME_VACCINO = 7;
        public static final int DITTA = 8;
        public static final int LOTTO = 9;
        public static final int SERIE = 10;
        public static final int SCADENZA = 11;
        public static final int INADEMPIENZA = 12;
    }

    
    public static class VISITA {

        public static final int TIPO = 0;
        public static final int DATA = 1;
        public static final int COGNOME = 2;
        public static final int NOME = 3;
        public static final int DATA_NASCITA = 4;
        public static final int ANAMNESI = 5;
        public static final int DATI_CLINICI = 6;
        public static final int ESAME_OBBIETTIVO = 7;
        public static final int DIAGNOSI = 8;
        public static final int TERAPIA = 9;
        public static final int TRASFERIMENTO = 10;
        public static final int PML = 11;
        public static final int DLT = 12;
        public static final int RICOVERO = 13;
        public static final int OPERATORE = 14;
        public static final int MEDICO = 15;
        public static final int MODIFICA = 16;
    }
    
    public static class RIFIUTI_SANITARI {
        public static final int PROTOCOLLO          = 0;
        public static final int DATA_PRODUZIONE     = 1;
        public static final int CODICE              = 2;
        public static final int QUANTITA            = 3;
        public static final int CONTENITORI         = 4;
        public static final int VOLUME              = 5;
        public static final int MEDICO              = 6;
        public static final int RESPONSABILE        = 7;
        public static final int DATA_VERSAMENTO     = 8;
        public static final int VERBALE_VERSAMENTO  = 9;
    }
    
}

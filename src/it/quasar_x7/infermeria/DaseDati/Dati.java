package it.quasar_x7.infermeria.DaseDati;

import it.quasar_x7.java.BaseDati.*;
import it.quasar_x7.java.utile.DataOraria;
import it.quasar_x7.java.utile.Errore;
import it.quasar_x7.java.utile.Testo;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/*******************************************************************************
 * 
 * @author Domenico della Peruta
 ******************************************************************************/
public class Dati {
    
    

    static private String IP_MySQL         = null;
    
    final protected boolean CHIAVE         = true;
    final protected boolean NON_CHIAVE     = false;
    
    final protected Dominio STRINGA_30     = new DatoStringa(30);        
    final protected Dominio STRINGA_250    = new DatoStringa(250); 
    final protected Dominio STRINGA_500    = new DatoStringa(250); 
    final protected Dominio STRINGA_2      = new DatoStringa(2);
    final protected Dominio STRINGA_50     = new DatoStringa(50);
    final protected Dominio STRINGA_1      = new DatoStringa(1);
    final protected Dominio STRINGA_20     = new DatoStringa(20);
    final protected Dominio STRINGA_40     = new DatoStringa(40);
    final protected Dominio STRINGA_45     = new DatoStringa(45);
    final protected Dominio STRINGA_3      = new DatoStringa(3);
    final protected Dominio STRINGA_25     = new DatoStringa(25);
    final protected Dominio STRINGA_10     = new DatoStringa(10);
    final protected Dominio STRINGA_100    = new DatoStringa(100);
    
    final protected Dominio NUMERO_INTERO  = new DatoInteroLungo();        
    final protected Dominio BOOLEANO       = new DatoBooleano();
    final protected Dominio DATA_ORA       = new DatoDataOraria();
    final protected Dominio DATA           = new DatoData();

    protected DataOraria _evento = new DataOraria();
    
 //---------------------------- proprietà --------------------------------------

    protected BaseDati DB;
    
    protected Relazione militare;
    protected Relazione medico;
    protected Relazione storiaVaccinale;
    protected Relazione anamnesiSedutaVaccinale;
    protected Relazione anamnesiReazioneVaccinale;
    protected Relazione vaccinazioni;
    protected Relazione visita;
    protected Relazione ricovero;
    protected Relazione utente;
    protected Relazione modelloML;
    protected Relazione DLT;
    protected Relazione modelloGL;
    
    protected Relazione impostazioni;
    
    protected Relazione comune;
    
    protected Relazione patologie; 
    protected Relazione terapia; 
    protected Relazione attivitàClinica; 
    protected Relazione trasferimentoOspedale;
    protected Relazione attivitàEsterna;
    protected Relazione attivitàMedicoLegali;
    protected Relazione causaServizio;
    
    protected Relazione presenza;
    protected Relazione posizione;
    
    protected Relazione rifiutiSanitari;
    
    

    public Dati() {
        //creazione della connessione col database "infermeria.mdb"
        //DB = new AccessoACCESS("infermeria");
        DB= new AccessoMySQL(apriFileIP(),"infermeria","root","Orione",false);
        //DB= new AccessoMySQL("infermeriaMySQL","root","Orione",true);
        creaTabellaMilitare();
        creaTabellaStoriaVaccinaei();
        creaTabellaMedici();
        creaTabellaRicovero();
        creaTabellaVaccinazioni();
        creaTabellaAnamnesiSedutaVaccinale();
        creaTabellaAnamnesiReazioneVaccinale();
        creaTabellaVisite();
        creaTabellaUtente();
        creaTabellaComune();
        creaTabellaPatologia();
        creaTabellaAttivitàClinica();
        creaTabellaAttivitàTrasferimento();
        creaTabellaAttivitàEsterna();
        creaTabellaTerapia();
        creaTabellaAttivitàMedicoLegali();
        creaTabellaVerbale5000();
        creaTabellaDLT();
        creaTabellaCausaServizio();
        creaTabellaModelloGL();
        creaTabellaPresenza();
        creaTabellaPosizione();
        creaTabellaImpostazioni();
        creaTabellaRifiuti();
        
    }

    
    private void creaTabellaPosizione(){
        try {
            posizione = new Relazione("posizione");
            posizione.creaAttributo("posizione",STRINGA_25, "", CHIAVE);
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(Dati.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void creaTabellaImpostazioni(){
        try {
            impostazioni = new Relazione("impostazioni");
            
            impostazioni.creaAttributo("nome",STRINGA_25, "", CHIAVE);
            impostazioni.creaAttributo("tipo",STRINGA_25, null, NON_CHIAVE);
            impostazioni.creaAttributo("valore",STRINGA_50, null, NON_CHIAVE);
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(Dati.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    private void creaTabellaVerbale5000(){
        try {
            modelloML = new Relazione("verbale5000");
            modelloML.creaAttributo("cognome",STRINGA_50, "", CHIAVE);//0
            modelloML.creaAttributo("nome", STRINGA_50, "", CHIAVE);//1
            modelloML.creaAttributo("data di nascita", DATA_ORA, "", CHIAVE);//2
            modelloML.creaAttributo("data", DATA_ORA, "", CHIAVE);//3
            
            modelloML.creaAttributo("protocollo", STRINGA_10, null, NON_CHIAVE);//4
            modelloML.creaAttributo("richiesta", STRINGA_100, null, NON_CHIAVE);//5
            modelloML.creaAttributo("visita medica", BOOLEANO, null, NON_CHIAVE);//6
            modelloML.creaAttributo("esame certificato medico", BOOLEANO, null, NON_CHIAVE);//7
            modelloML.creaAttributo("incarico", STRINGA_50, null, NON_CHIAVE);//8
            modelloML.creaAttributo("reparto", STRINGA_50, null, NON_CHIAVE);//9
            modelloML.creaAttributo("idoneo servizio", BOOLEANO, null, NON_CHIAVE);//10
            modelloML.creaAttributo("T.O.", STRINGA_50, null, NON_CHIAVE);//11
            modelloML.creaAttributo("T.O. - idoneo", BOOLEANO, null, NON_CHIAVE);//12
            modelloML.creaAttributo("T.O. - non idoneo", BOOLEANO, null, NON_CHIAVE);//13
            modelloML.creaAttributo("T.O. - temp non idoneo", BOOLEANO, null, NON_CHIAVE);//14
            modelloML.creaAttributo("T.O. - temp non idoneo mesi", NUMERO_INTERO, null, NON_CHIAVE);//15
            modelloML.creaAttributo("contr prove operative - assenti", BOOLEANO, null, NON_CHIAVE);//16
            modelloML.creaAttributo("contr prove operative - permanenti", BOOLEANO, null, NON_CHIAVE);//17
            modelloML.creaAttributo("contr prove operative - temp", BOOLEANO, null, NON_CHIAVE);//18
            modelloML.creaAttributo("contr prove operative - temp periodo", STRINGA_10, null, NON_CHIAVE);//19
            modelloML.creaAttributo("sottoporre visita fiscale", BOOLEANO, null, NON_CHIAVE);//20
            modelloML.creaAttributo("ammalato", BOOLEANO, null, NON_CHIAVE);//21
            modelloML.creaAttributo("ammalato fino", DATA_ORA, null, NON_CHIAVE);//22
            modelloML.creaAttributo("convalescente", BOOLEANO, null, NON_CHIAVE);//23
            modelloML.creaAttributo("convalescente fino", DATA_ORA, null, NON_CHIAVE);//24
            modelloML.creaAttributo("temp non idoneo", BOOLEANO, null, NON_CHIAVE);//25
            modelloML.creaAttributo("temp non idoneo - invio CMO", BOOLEANO, null, NON_CHIAVE);//26
            modelloML.creaAttributo("temp non idoneo - invio Osservazione", BOOLEANO, null, NON_CHIAVE);//27
            modelloML.creaAttributo("impiego tecnico amm.", BOOLEANO, null, NON_CHIAVE);//28
            modelloML.creaAttributo("impiego tecnico amm. - invio CMO", BOOLEANO, null, NON_CHIAVE);//29
            modelloML.creaAttributo("impiego tecnico amm. - invio Osservazione", BOOLEANO, null, NON_CHIAVE);//30
            modelloML.creaAttributo("infermita lesioni", BOOLEANO, null, NON_CHIAVE);//31
            modelloML.creaAttributo("infermita lesioni - esclusivamente", BOOLEANO, null, NON_CHIAVE);//32
            modelloML.creaAttributo("infermita lesioni - in misura prev", BOOLEANO, null, NON_CHIAVE);//33
            modelloML.creaAttributo("infermita lesioni - causa di serv", BOOLEANO, null, NON_CHIAVE);//34
            modelloML.creaAttributo("infermita lesioni - non causa di serv", BOOLEANO, null, NON_CHIAVE);//35
            modelloML.creaAttributo("infermita lesioni - in accertamento", BOOLEANO, null, NON_CHIAVE);//36
            modelloML.creaAttributo("infermita lesioni - non oggetto acc", BOOLEANO, null, NON_CHIAVE);//37
            modelloML.creaAttributo("ferite o lesioni traumatiche", BOOLEANO, null, NON_CHIAVE);//38
            modelloML.creaAttributo("infermita seguito miss. attivita op. add", BOOLEANO, null, NON_CHIAVE);//39
            modelloML.creaAttributo("note controindicazioni motivazioni", STRINGA_250, null, NON_CHIAVE);//40
            
            modelloML.creaAttributo("visita periodica", BOOLEANO, null, NON_CHIAVE);//41
            modelloML.creaAttributo("controllo certificato medico", BOOLEANO, null, NON_CHIAVE);//42
            modelloML.creaAttributo("visita preimpiego TO", BOOLEANO, null, NON_CHIAVE);//43
            modelloML.creaAttributo("visita rientro TO", BOOLEANO, null, NON_CHIAVE);//44
            modelloML.creaAttributo("visita rientro malattia", BOOLEANO, null, NON_CHIAVE);//45
            modelloML.creaAttributo("visita fiscale", BOOLEANO, null, NON_CHIAVE);//46
            modelloML.creaAttributo("visita richiesta", BOOLEANO, null, NON_CHIAVE);//47
            modelloML.creaAttributo("altra visita", BOOLEANO, null, NON_CHIAVE);//48
            modelloML.creaAttributo("tipo altra visita", STRINGA_50, null, NON_CHIAVE);//49
            
            modelloML.creaAttributo("idoneita incondizionata", BOOLEANO, null, NON_CHIAVE);//50
            modelloML.creaAttributo("idoneita parziale", BOOLEANO, null, NON_CHIAVE);//51
            modelloML.creaAttributo("fumo SI", BOOLEANO, null, NON_CHIAVE);//52
            modelloML.creaAttributo("fumo NO", BOOLEANO, null, NON_CHIAVE);//53
            modelloML.creaAttributo("n. sigarette", NUMERO_INTERO, null, NON_CHIAVE);//54
            modelloML.creaAttributo("n. sigarette dal", DATA_ORA, null, NON_CHIAVE);//55
            modelloML.creaAttributo("ex fumatore", BOOLEANO, null, NON_CHIAVE);//56
            modelloML.creaAttributo("ex fumatore dal", DATA_ORA, null, NON_CHIAVE);//57
            modelloML.creaAttributo("alcool SI", BOOLEANO, null, NON_CHIAVE);//58
            modelloML.creaAttributo("alcool NO", BOOLEANO, null, NON_CHIAVE);//59
            modelloML.creaAttributo("alvo", STRINGA_50, null, NON_CHIAVE);//60
            modelloML.creaAttributo("diuresi", STRINGA_50, null, NON_CHIAVE);//61
            modelloML.creaAttributo("ritmo sonno-veglia", STRINGA_50, null, NON_CHIAVE);//62
            modelloML.creaAttributo("vaccinazione completa", BOOLEANO, null, NON_CHIAVE);//63
            modelloML.creaAttributo("vaccinazione da completare", BOOLEANO, null, NON_CHIAVE);//64
            modelloML.creaAttributo("tipo vacc da completare", STRINGA_100, null, NON_CHIAVE);//65
            modelloML.creaAttributo("vaccinazione non desumibile", BOOLEANO, null, NON_CHIAVE);//66
            modelloML.creaAttributo("anamnesi patologica remota", STRINGA_500, null, NON_CHIAVE);//67
            modelloML.creaAttributo("anamnesi patologica prossima", STRINGA_500, null, NON_CHIAVE);//68
            modelloML.creaAttributo("intolleranze allergie", STRINGA_100, null, NON_CHIAVE);//69
            modelloML.creaAttributo("terapia in atto", STRINGA_100, null, NON_CHIAVE);//70
            
            modelloML.creaAttributo("peso", NUMERO_INTERO, null, NON_CHIAVE);//71
            modelloML.creaAttributo("statura", NUMERO_INTERO, null, NON_CHIAVE);//72
            modelloML.creaAttributo("condizioni generali", STRINGA_50, null, NON_CHIAVE);//73
            modelloML.creaAttributo("vista", STRINGA_50, null, NON_CHIAVE);//74
            modelloML.creaAttributo("udito", STRINGA_50, null, NON_CHIAVE);//75
            modelloML.creaAttributo("azione cardiaca", STRINGA_50, null, NON_CHIAVE);//76
            modelloML.creaAttributo("frequenza", NUMERO_INTERO, null, NON_CHIAVE);//77
            modelloML.creaAttributo("pressione max", NUMERO_INTERO, null, NON_CHIAVE);//78
            modelloML.creaAttributo("pressione min", NUMERO_INTERO, null, NON_CHIAVE);//79
            modelloML.creaAttributo("esame obiettivo locale", STRINGA_500, null, NON_CHIAVE);//80
            modelloML.creaAttributo("E.O. nulla di rilevante", BOOLEANO, null, NON_CHIAVE);//81
            
            modelloML.creaAttributo("accertamenti", STRINGA_500, null, NON_CHIAVE);//82
            modelloML.creaAttributo("diagnosi", STRINGA_500, null, NON_CHIAVE);//83
            modelloML.creaAttributo("note", STRINGA_250, null, NON_CHIAVE);//84
            modelloML.creaAttributo("medico", STRINGA_50, null, NON_CHIAVE);//85
            
            modelloML.creaAttributo("monitoraggio eccesso pond", BOOLEANO, null, NON_CHIAVE);//86
            modelloML.creaAttributo("giorni monitoraggio eccesso pond", NUMERO_INTERO, null, NON_CHIAVE);//87
            
            
            /*
            try {
            DB.connetti();
            
            DB.generaTabella(causaServizio);
            javax.swing.JOptionPane.showMessageDialog(null, ":)");
            } catch (EccezioneBaseDati ex) {
            javax.swing.JOptionPane.showMessageDialog(null, ":(\n"+ex.getMessage());
            Logger.getLogger(Dati.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            */
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(Dati.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
    private void creaTabellaCausaServizio(){
        try {
            causaServizio = new Relazione("cause_di_servizio");
            causaServizio.creaAttributo("cognome",STRINGA_50, "", CHIAVE);//0
            causaServizio.creaAttributo("nome", STRINGA_50, "", CHIAVE);//1
            causaServizio.creaAttributo("data di nascita", DATA_ORA, "", CHIAVE);//2
            causaServizio.creaAttributo("infermita", STRINGA_100, "", CHIAVE);//3
            
            causaServizio.creaAttributo("dipendenza", STRINGA_50, null, NON_CHIAVE);//4
            causaServizio.creaAttributo("temp domanda", STRINGA_50, null, NON_CHIAVE);//5
            causaServizio.creaAttributo("provvedimento", STRINGA_100, null, NON_CHIAVE);//6
            causaServizio.creaAttributo("data provv", DATA_ORA, null, NON_CHIAVE);//7
            causaServizio.creaAttributo("ente", STRINGA_100, null, NON_CHIAVE);//8
            
            
            
            /*
            try {
            DB.connetti();
            
            DB.generaTabella(causaServizio);
            javax.swing.JOptionPane.showMessageDialog(null, ":)");
            } catch (EccezioneBaseDati ex) {
            javax.swing.JOptionPane.showMessageDialog(null, ":(\n"+ex.getMessage());
            Logger.getLogger(Dati.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            */
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(Dati.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
      
        
    private void creaTabellaModelloGL(){
        try {
            modelloGL = new Relazione("modello_gl");
            modelloGL.creaAttributo("cognome",STRINGA_50, "", CHIAVE);//0
            modelloGL.creaAttributo("nome", STRINGA_50, "", CHIAVE);//1
            modelloGL.creaAttributo("data di nascita", DATA_ORA, "", CHIAVE);//2
            modelloGL.creaAttributo("data", DATA_ORA, "", CHIAVE);//3
            
            modelloGL.creaAttributo("elementi informativi non sanitari", STRINGA_250, null, NON_CHIAVE);//4
            modelloGL.creaAttributo("relazione allegata", BOOLEANO, null, NON_CHIAVE);
            modelloGL.creaAttributo("data fruizione giorni assenza", DATA_ORA, null, NON_CHIAVE);
            modelloGL.creaAttributo("giorni assenza", NUMERO_INTERO, null, NON_CHIAVE);
            
            modelloGL.creaAttributo("prospetto assenza", BOOLEANO, null, NON_CHIAVE);//8
            modelloGL.creaAttributo("n allegato prospetto assenza", NUMERO_INTERO, null, NON_CHIAVE);
            modelloGL.creaAttributo("documento matricolare", BOOLEANO, null, NON_CHIAVE);//10
            modelloGL.creaAttributo("n allegato documento matricolare", NUMERO_INTERO, null, NON_CHIAVE);
            modelloGL.creaAttributo("copia certif sanitario", BOOLEANO, null, NON_CHIAVE);
            modelloGL.creaAttributo("n allegato copia certif sanitario", NUMERO_INTERO, null, NON_CHIAVE);
            
            modelloGL.creaAttributo("superamento assenze conv", BOOLEANO, null, NON_CHIAVE);//14
            modelloGL.creaAttributo("termine periodo assenza", BOOLEANO, null, NON_CHIAVE);
            modelloGL.creaAttributo("giorni periodo assenza", STRINGA_50, null, NON_CHIAVE);
            modelloGL.creaAttributo("presunta inabilita", BOOLEANO, null, NON_CHIAVE);
            modelloGL.creaAttributo("altre forme inabilita", BOOLEANO, null, NON_CHIAVE);
            modelloGL.creaAttributo("nome altre forme inabilita", STRINGA_250, null, NON_CHIAVE);
            modelloGL.creaAttributo("accertamento causa di servizio", BOOLEANO, null, NON_CHIAVE);//20
            modelloGL.creaAttributo("su richiesta", BOOLEANO, null, NON_CHIAVE);
            modelloGL.creaAttributo("altro", BOOLEANO, null, NON_CHIAVE);
            modelloGL.creaAttributo("note altro", STRINGA_250, null, NON_CHIAVE);
            modelloGL.creaAttributo("temp non idoneo", BOOLEANO, null, NON_CHIAVE);//24
            
            modelloGL.creaAttributo("altri elementi informativi",STRINGA_250, null, NON_CHIAVE);//25
            modelloGL.creaAttributo("anamnesi",STRINGA_500, null, NON_CHIAVE);
            modelloGL.creaAttributo("esame obiettivo",STRINGA_500, null, NON_CHIAVE);
            modelloGL.creaAttributo("diagnosi",STRINGA_500, null, NON_CHIAVE);
            modelloGL.creaAttributo("medico", STRINGA_50, null, NON_CHIAVE);//29
            
            modelloGL.creaAttributo("dubbi di idoneita al servizio", BOOLEANO, null, NON_CHIAVE);//30
            /*
            try {
            DB.connetti();
            
            DB.generaTabella(modelloGL);
            javax.swing.JOptionPane.showMessageDialog(null, ":)");
            } catch (EccezioneBaseDati ex) {
            javax.swing.JOptionPane.showMessageDialog(null, ":(\n"+ex.getMessage());
            Logger.getLogger(Dati.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            */
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(Dati.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void creaTabellaRifiuti() {
        try {
            rifiutiSanitari = new Relazione("rifiuti_sanitari");
            rifiutiSanitari.creaAttributo("protocollo", STRINGA_10, "", CHIAVE);
            rifiutiSanitari.creaAttributo("data_produzione", DATA_ORA, null, NON_CHIAVE);
            rifiutiSanitari.creaAttributo("codice", STRINGA_10, null, NON_CHIAVE);
            rifiutiSanitari.creaAttributo("quantita", NUMERO_INTERO, null, NON_CHIAVE);
            rifiutiSanitari.creaAttributo("contenitori", NUMERO_INTERO, null, NON_CHIAVE);
            rifiutiSanitari.creaAttributo("volume", NUMERO_INTERO, null, NON_CHIAVE);
            rifiutiSanitari.creaAttributo("medico", STRINGA_50, null, NON_CHIAVE);
            rifiutiSanitari.creaAttributo("responsabile", STRINGA_50, null, NON_CHIAVE);
            rifiutiSanitari.creaAttributo("data_versamento", DATA_ORA, null, NON_CHIAVE);
            rifiutiSanitari.creaAttributo("verbale_versamento", STRINGA_20, null, NON_CHIAVE);
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(Dati.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void creaTabellaAttivitàMedicoLegali(){
        try {
            attivitàMedicoLegali = new Relazione("attivita_medico_legali");
            attivitàMedicoLegali.creaAttributo("data", DATA_ORA, "", CHIAVE);
            attivitàMedicoLegali.creaAttributo("pratica", STRINGA_40, "", CHIAVE);
            attivitàMedicoLegali.creaAttributo("attivita", STRINGA_40, null, NON_CHIAVE);
            attivitàMedicoLegali.creaAttributo("numero",NUMERO_INTERO, null, NON_CHIAVE);
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(Dati.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    private void creaTabellaAnamnesiReazioneVaccinale(){

        try {
            anamnesiReazioneVaccinale = new Relazione("reazione_precedente_vaccinazione");
            anamnesiReazioneVaccinale.creaAttributo("cognome",STRINGA_50, "", CHIAVE);
            anamnesiReazioneVaccinale.creaAttributo("nome", STRINGA_50, "", CHIAVE);
            anamnesiReazioneVaccinale.creaAttributo("data di nascita", DATA_ORA, "", CHIAVE);
            anamnesiReazioneVaccinale.creaAttributo("data vaccinazione", DATA_ORA,"", CHIAVE);
            anamnesiReazioneVaccinale.creaAttributo("tipo profilassi", STRINGA_50, "", CHIAVE);
            anamnesiReazioneVaccinale.creaAttributo("locali gravi", BOOLEANO, null, NON_CHIAVE);
            anamnesiReazioneVaccinale.creaAttributo("generali lievi", BOOLEANO, null, NON_CHIAVE);
            anamnesiReazioneVaccinale.creaAttributo("generali gravi", BOOLEANO, null, NON_CHIAVE);
            anamnesiReazioneVaccinale.creaAttributo("data evento", DATA_ORA, null, NON_CHIAVE);
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(Dati.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


   private void creaTabellaPatologia() {      
        try {
            patologie = new Relazione("patologia");
            patologie.creaAttributo("patologia",STRINGA_100,"", CHIAVE);
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(Dati.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
   
   private void creaTabellaTerapia() {   
       
        try {
            terapia = new Relazione("terapia");
            terapia.creaAttributo("terapia",STRINGA_100,"", CHIAVE);
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(Dati.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
   
   private void creaTabellaComune(){        
        try {
            this.comune = new Relazione("comune");
            
            this.comune.creaAttributo("comune", STRINGA_50, "", CHIAVE);
            this.comune.creaAttributo("provincia", STRINGA_2, null, NON_CHIAVE);
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(Dati.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
   
   private void creaTabellaVisite(){
        
        try {
            visita = new Relazione("visita");
            
            visita.creaAttributo("tipo", STRINGA_50, "", NON_CHIAVE);//0
            
            visita.creaAttributo("visita", DATA_ORA,"", CHIAVE);
            visita.creaAttributo("cognome",STRINGA_50, "", CHIAVE);
            visita.creaAttributo("nome", STRINGA_50, "", CHIAVE);
            visita.creaAttributo("data di nascita", DATA_ORA, "", CHIAVE);//4
            
            visita.creaAttributo("motivo",    STRINGA_250, null, NON_CHIAVE);
            visita.creaAttributo("dati clinici", STRINGA_250, null, NON_CHIAVE);
            visita.creaAttributo("esame obbiettivo", STRINGA_250, null, NON_CHIAVE);
            visita.creaAttributo("diagnosi",  STRINGA_250, null, NON_CHIAVE);
            visita.creaAttributo("terapia",   STRINGA_250, null, NON_CHIAVE);
            visita.creaAttributo("trasferimento", STRINGA_250, null, NON_CHIAVE);//10
            visita.creaAttributo("PML",       STRINGA_250, null, NON_CHIAVE);
            visita.creaAttributo("DLT",       BOOLEANO, null, NON_CHIAVE);
            visita.creaAttributo("ricovero",  BOOLEANO, null, NON_CHIAVE);
            visita.creaAttributo("operatore", STRINGA_50, null, NON_CHIAVE);//14
            visita.creaAttributo("medico",    STRINGA_50, null, NON_CHIAVE);
            visita.creaAttributo("modifica",  DATA_ORA, null, NON_CHIAVE);
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(Dati.class.getName()).log(Level.SEVERE, null, ex);
        }

   }
   
   private void creaTabellaDLT(){
        
        try {
            DLT = new Relazione("DLT");
            
            DLT.creaAttributo("visita", DATA_ORA,"", CHIAVE);
            DLT.creaAttributo("cognome",STRINGA_50, "", CHIAVE);
            DLT.creaAttributo("nome", STRINGA_50, "", CHIAVE);
            DLT.creaAttributo("data di nascita", DATA_ORA, "", CHIAVE);
            
            DLT.creaAttributo("dichiarazione", STRINGA_250, null, NON_CHIAVE);
            DLT.creaAttributo("esame obbiettivo", STRINGA_250, null, NON_CHIAVE);
            DLT.creaAttributo("diagnosi", STRINGA_250, null, NON_CHIAVE);
            DLT.creaAttributo("terapia", STRINGA_250, null, NON_CHIAVE);
            
            DLT.creaAttributo("testimone",STRINGA_50, null, NON_CHIAVE);
            DLT.creaAttributo("guarigione",NUMERO_INTERO, null, NON_CHIAVE);
            DLT.creaAttributo("vacc. antitetanica",BOOLEANO, null, NON_CHIAVE);
            DLT.creaAttributo("motivo vacc. antitetanica",STRINGA_50, null, NON_CHIAVE);
            DLT.creaAttributo("trasporto O.C.",BOOLEANO, null, NON_CHIAVE);
            DLT.creaAttributo("motivo trasporto O.C.",STRINGA_250, null, NON_CHIAVE);
            DLT.creaAttributo("verosomiglianza",BOOLEANO, null, NON_CHIAVE);
            
            DLT.creaAttributo("operatore", STRINGA_50, null, NON_CHIAVE);
            DLT.creaAttributo("medico", STRINGA_50, null, NON_CHIAVE);
            DLT.creaAttributo("modifica", DATA_ORA, null, NON_CHIAVE);
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(Dati.class.getName()).log(Level.SEVERE, null, ex);
        }

   }

   private void creaTabellaAttivitàClinica(){
        
        try {
            attivitàClinica = new Relazione("attivita_clinica");
            
            attivitàClinica.creaAttributo("tipo", STRINGA_50, "", CHIAVE);
            attivitàClinica.creaAttributo("data", DATA_ORA,"", CHIAVE);
            attivitàClinica.creaAttributo("motivo",STRINGA_100, "", CHIAVE);
            attivitàClinica.creaAttributo("numero", NUMERO_INTERO, null, NON_CHIAVE);
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(Dati.class.getName()).log(Level.SEVERE, null, ex);
        }

   }

   private void creaTabellaAttivitàTrasferimento(){
        
        try {
            trasferimentoOspedale = new Relazione("attivita_trasferimento_ospedale");
            
            trasferimentoOspedale.creaAttributo("motivo", STRINGA_100, "", CHIAVE);
            trasferimentoOspedale.creaAttributo("data", DATA_ORA,"", CHIAVE);
            trasferimentoOspedale.creaAttributo("ospedale",STRINGA_100, "", CHIAVE);
            trasferimentoOspedale.creaAttributo("numero", NUMERO_INTERO, null, NON_CHIAVE);
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(Dati.class.getName()).log(Level.SEVERE, null, ex);
        }

   }
   
   private void creaTabellaAttivitàEsterna(){
        
        try {
            attivitàEsterna = new Relazione("attivita_esterna");
            
            attivitàEsterna.creaAttributo("tipo", STRINGA_100, "", CHIAVE);
            attivitàEsterna.creaAttributo("luogo", STRINGA_50, "", CHIAVE);
            attivitàEsterna.creaAttributo("data", DATA_ORA,"", CHIAVE);
            attivitàEsterna.creaAttributo("durata",NUMERO_INTERO, null, NON_CHIAVE);
            attivitàEsterna.creaAttributo("medici",NUMERO_INTERO, null, NON_CHIAVE);
            attivitàEsterna.creaAttributo("infermieri",NUMERO_INTERO, null, NON_CHIAVE);
            attivitàEsterna.creaAttributo("asa",NUMERO_INTERO, null, NON_CHIAVE);
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(Dati.class.getName()).log(Level.SEVERE, null, ex);
        }

   }
   
   
   private void creaTabellaRicovero(){

        try {
            ricovero = new Relazione("ricovero");
            
            ricovero.creaAttributo("cognome",STRINGA_50, "", CHIAVE);
            ricovero.creaAttributo("nome", STRINGA_50, "", CHIAVE);
            ricovero.creaAttributo("data di nascita", DATA_ORA, "", CHIAVE);
            ricovero.creaAttributo("data ingresso", DATA_ORA,"", CHIAVE);
            
            ricovero.creaAttributo("data uscita", DATA_ORA,null, NON_CHIAVE);
            ricovero.creaAttributo("anamnesi", STRINGA_250, null, NON_CHIAVE);
            ricovero.creaAttributo("dati clinici", STRINGA_250, null, NON_CHIAVE);
            ricovero.creaAttributo("esame obbiettivo", STRINGA_250, null, NON_CHIAVE);
            ricovero.creaAttributo("diagnosi", STRINGA_250, null, NON_CHIAVE);
            ricovero.creaAttributo("terapia", STRINGA_250, null, NON_CHIAVE);
            ricovero.creaAttributo("PML",STRINGA_250, null, NON_CHIAVE);
            ricovero.creaAttributo("utente",STRINGA_50, null, NON_CHIAVE);
            ricovero.creaAttributo("modifica", DATA_ORA, null, NON_CHIAVE);
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(Dati.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


   private void creaTabellaUtente(){
        
        try {
            utente =new Relazione("utente");
            
            utente.creaAttributo("nome",STRINGA_30, "", CHIAVE);
            utente.creaAttributo("password",STRINGA_30, null, NON_CHIAVE);
            utente.creaAttributo("livello",STRINGA_30, null, NON_CHIAVE);
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(Dati.class.getName()).log(Level.SEVERE, null, ex);
        }

   }


    private void creaTabellaAnamnesiSedutaVaccinale(){
    
        try {
            anamnesiSedutaVaccinale =new Relazione(BASE_DATI.TABELLA.ANAMNESI_VACCINALE);
            
            anamnesiSedutaVaccinale.creaAttributo("cognome",STRINGA_50, "", CHIAVE);//0
            anamnesiSedutaVaccinale.creaAttributo("nome", STRINGA_50, "", CHIAVE);
            anamnesiSedutaVaccinale.creaAttributo("data di nascita", DATA_ORA, "", CHIAVE);
            
            anamnesiSedutaVaccinale.creaAttributo("data vaccinazione", DATA_ORA,"", CHIAVE);//3
            
            
            
            anamnesiSedutaVaccinale.creaAttributo(
                    "allergie - uova penne piume carne pollo",STRINGA_1, null, NON_CHIAVE);//4
            anamnesiSedutaVaccinale.creaAttributo(
                    "allergie - uova penne piume carne anatra",STRINGA_1, null, NON_CHIAVE);
            anamnesiSedutaVaccinale.creaAttributo(
                    "allergie - carne bovina gelatina",STRINGA_1, null, NON_CHIAVE);
            anamnesiSedutaVaccinale.creaAttributo(
                    "allergie - formaldeide",STRINGA_1, null, NON_CHIAVE);
            anamnesiSedutaVaccinale.creaAttributo(
                    "allergie - antibiotici neomicina",STRINGA_1, null, NON_CHIAVE);
            anamnesiSedutaVaccinale.creaAttributo(
                    "allergie - antibiotici streptomicina",STRINGA_1, null, NON_CHIAVE);
            anamnesiSedutaVaccinale.creaAttributo(
                    "allergie - kanamicina",STRINGA_1, null, NON_CHIAVE);
            anamnesiSedutaVaccinale.creaAttributo(
                    "allergie - antibiotici polimixin b",STRINGA_1, null, NON_CHIAVE);
            anamnesiSedutaVaccinale.creaAttributo(
                    "allergie - composti mercuriali",STRINGA_1,null, NON_CHIAVE);
            anamnesiSedutaVaccinale.creaAttributo(
                    "allergie - altro",STRINGA_1, null, NON_CHIAVE);//13
            
            anamnesiSedutaVaccinale.creaAttributo(
                    "conviventi - contatti con - immunodepressione grave", BOOLEANO, null, NON_CHIAVE);
            anamnesiSedutaVaccinale.creaAttributo(
                    "conviventi - contatti con - tumori solidi o del sangue", BOOLEANO, null, NON_CHIAVE);
            anamnesiSedutaVaccinale.creaAttributo(
                    "conviventi - contatti con - gravidanza in corso", BOOLEANO, null, NON_CHIAVE);//16
            
            anamnesiSedutaVaccinale.creaAttributo(
                    "anamnesi recente - febbre in atto", BOOLEANO, null, NON_CHIAVE);
            anamnesiSedutaVaccinale.creaAttributo(
                    "anamnesi recente - disturbi vie aeree", BOOLEANO, null, NON_CHIAVE);
            anamnesiSedutaVaccinale.creaAttributo(
                    "anamnesi recente - diarrea in atto", BOOLEANO, null, NON_CHIAVE);
            anamnesiSedutaVaccinale.creaAttributo(
                    "anamnesi recente - terapie recenti", BOOLEANO, null, NON_CHIAVE);
            anamnesiSedutaVaccinale.creaAttributo(
                    "anamnesi recente - emotrasfusioni recenti", BOOLEANO, null, NON_CHIAVE);//21
            
            anamnesiSedutaVaccinale.creaAttributo(
                    "anamnesi ginecologica - periodicita mestruale",STRINGA_20, null, NON_CHIAVE);
            anamnesiSedutaVaccinale.creaAttributo("anamnesi ginecologica - data ultima mestruazione", DATA_ORA, null, NON_CHIAVE);
            anamnesiSedutaVaccinale.creaAttributo(
                    "anamnesi ginecologica - stato gravidico", STRINGA_20, null, NON_CHIAVE);
            anamnesiSedutaVaccinale.creaAttributo(
                    "anamnesi ginecologica - test di gravidanza",STRINGA_20, null, NON_CHIAVE);
            anamnesiSedutaVaccinale.creaAttributo("anamnesi ginecologica - data esecuzione test gravidanza", DATA_ORA, null, NON_CHIAVE);//26
            
            anamnesiSedutaVaccinale.creaAttributo(
                    "medico", STRINGA_40, null, NON_CHIAVE);//27
            anamnesiSedutaVaccinale.creaAttributo("utente",STRINGA_50, null, NON_CHIAVE);
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(Dati.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }

    private void creaTabellaMedici() {
        
        try {
            medico = new Relazione("medico");
            medico.creaAttributo("nome", STRINGA_40, "", CHIAVE);
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(Dati.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void creaTabellaVaccinazioni(){

        try {
            vaccinazioni = new Relazione("vaccinazione");
            vaccinazioni.creaAttributo("seduta vaccinale", DATA_ORA, "", CHIAVE);//0
            vaccinazioni.creaAttributo("tipo profilassi", STRINGA_25, "", CHIAVE);//1
            vaccinazioni.creaAttributo("cognome",STRINGA_50, "", CHIAVE);//2
            vaccinazioni.creaAttributo("nome", STRINGA_50, "", CHIAVE);//3
            vaccinazioni.creaAttributo("data di nascita", DATA_ORA, "", CHIAVE);//4
            
            vaccinazioni.creaAttributo("tipo dose", STRINGA_3, null, NON_CHIAVE);//5
            vaccinazioni.creaAttributo("via somministrazione", STRINGA_2, null, NON_CHIAVE);
            vaccinazioni.creaAttributo("nome vaccino", STRINGA_30, null, NON_CHIAVE);
            vaccinazioni.creaAttributo("ditta produttrice", STRINGA_40, null, NON_CHIAVE);
            vaccinazioni.creaAttributo("lotto", STRINGA_30, null, NON_CHIAVE);
            vaccinazioni.creaAttributo("serie", STRINGA_30, null, NON_CHIAVE);//10
            vaccinazioni.creaAttributo("scadenza", DATA_ORA, null, NON_CHIAVE);
            vaccinazioni.creaAttributo("cause inadempienza", STRINGA_50, null, NON_CHIAVE);
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(Dati.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    

    private void creaTabellaMilitare(){
                 
        try {
            militare = new Relazione(BASE_DATI.TABELLA.MILITARE);
            
            militare.creaAttributo("cognome",STRINGA_50, "", CHIAVE);//0
            militare.creaAttributo("nome", STRINGA_50, "", CHIAVE);
            militare.creaAttributo("data di nascita", DATA_ORA, "", CHIAVE);
            
            militare.creaAttributo("luogo di nascita", STRINGA_50, null,NON_CHIAVE);//3
            militare.creaAttributo("grado", STRINGA_50, null,NON_CHIAVE);
            militare.creaAttributo("scolarita", STRINGA_50, null, NON_CHIAVE);//5
            militare.creaAttributo("compagnia", STRINGA_50, null, NON_CHIAVE);
            militare.creaAttributo("anamnesi famigliare", STRINGA_100,null,NON_CHIAVE);
            militare.creaAttributo("anamnesi patologica remota", STRINGA_100,null,NON_CHIAVE);//8
            militare.creaAttributo("anamnesi patologica prossima", STRINGA_100,null,NON_CHIAVE);//9
            
            militare.creaAttributo("altezza", NUMERO_INTERO,null,NON_CHIAVE);//10
            militare.creaAttributo("peso", NUMERO_INTERO,null,NON_CHIAVE);
            militare.creaAttributo("perimetro toracico", NUMERO_INTERO,null,NON_CHIAVE);
            militare.creaAttributo("visus OD", NUMERO_INTERO,null,NON_CHIAVE);//13
            militare.creaAttributo("visus OS", NUMERO_INTERO,null,NON_CHIAVE);//14
            militare.creaAttributo("visus OD corretto", NUMERO_INTERO,null,NON_CHIAVE);//15
            militare.creaAttributo("visus OS corretto", NUMERO_INTERO,null,NON_CHIAVE);
            militare.creaAttributo("pressione arteriosa max", NUMERO_INTERO,null,NON_CHIAVE);//17
            militare.creaAttributo("pressione arteriosa min", NUMERO_INTERO,null,NON_CHIAVE);//18
            militare.creaAttributo("frequenza cardiaca", NUMERO_INTERO,null,NON_CHIAVE);//19
            militare.creaAttributo("audiometria destra", NUMERO_INTERO,null,NON_CHIAVE);//20
            militare.creaAttributo("audiometria sinistra", NUMERO_INTERO,null,NON_CHIAVE);
            militare.creaAttributo("data GML", DATA_ORA, null, NON_CHIAVE);//22
            militare.creaAttributo("GML", STRINGA_50, null, NON_CHIAVE);
            militare.creaAttributo("corso", STRINGA_10, null, NON_CHIAVE);
            militare.creaAttributo("utente", STRINGA_30, null, NON_CHIAVE);
            militare.creaAttributo("favismo",BOOLEANO,null,NON_CHIAVE);
            militare.creaAttributo("assenza documentazione favismo",BOOLEANO,null,NON_CHIAVE);
            militare.creaAttributo("modifica", DATA_ORA, null, NON_CHIAVE);
            militare.creaAttributo("medico",STRINGA_50,null, NON_CHIAVE);
            
            militare.creaAttributo("sesso", STRINGA_1, null,NON_CHIAVE);//30
            
            militare.creaAttributo("tipo documento",STRINGA_50,null, NON_CHIAVE);//31
            militare.creaAttributo("numero documento",STRINGA_30,null, NON_CHIAVE);
            
            militare.creaAttributo("residenza", STRINGA_50, null,NON_CHIAVE);//33
            militare.creaAttributo("indirizzo", STRINGA_50, null,NON_CHIAVE);//34
            militare.creaAttributo("incarico", STRINGA_50, null,NON_CHIAVE);//35
            militare.creaAttributo("ASL", STRINGA_50, null, NON_CHIAVE);//36
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(Dati.class.getName()).log(Level.SEVERE, null, ex);
        }

     }

     
    private void creaTabellaStoriaVaccinaei(){
        
        try {
            storiaVaccinale = new Relazione("storia_vaccinale");
            
            storiaVaccinale.creaAttributo("cognome",STRINGA_50, "", CHIAVE);//0
            storiaVaccinale.creaAttributo("nome", STRINGA_50, "", CHIAVE);
            storiaVaccinale.creaAttributo("data di nascita", DATA_ORA, "", CHIAVE);
            storiaVaccinale.creaAttributo("tipo profilassi", STRINGA_20, "", CHIAVE);
            
            storiaVaccinale.creaAttributo("pregressa", BOOLEANO,null, NON_CHIAVE);//4
            storiaVaccinale.creaAttributo("pregressa documentata", BOOLEANO,null, NON_CHIAVE);
            storiaVaccinale.creaAttributo("ultima vaccinazione civile", STRINGA_1,null, NON_CHIAVE);
            storiaVaccinale.creaAttributo("ultima vaccinazione militare",STRINGA_1,null, NON_CHIAVE);
            storiaVaccinale.creaAttributo("tipo dose",STRINGA_3,null, NON_CHIAVE);
            storiaVaccinale.creaAttributo("tipo vaccino", STRINGA_3,null, NON_CHIAVE);
            storiaVaccinale.creaAttributo("data vaccinazione", DATA_ORA, null, NON_CHIAVE);//10
            
            storiaVaccinale.creaAttributo("medico",STRINGA_50,null, NON_CHIAVE);
            storiaVaccinale.creaAttributo("utente",STRINGA_30,null, NON_CHIAVE);
            storiaVaccinale.creaAttributo("modifica", DATA_ORA, null, NON_CHIAVE);
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(Dati.class.getName()).log(Level.SEVERE, null, ex);
        }

     }

     
    private void creaTabellaPresenza() {
        try {
            presenza = new Relazione("presenza");
            presenza.creaAttributo("data", DATA_ORA, "", CHIAVE);
            presenza.creaAttributo("militare", STRINGA_45, "", CHIAVE);
            presenza.creaAttributo("posizione", STRINGA_50,null, NON_CHIAVE);
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(Dati.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    private String apriFileIP(){

        if(IP_MySQL == null){

           Properties lista = new Properties();

            try {
                FileInputStream file = new FileInputStream("serverIP.xml");
                lista.loadFromXML(file);
                file.close();
                IP_MySQL = lista.getProperty("IP");

                if(IP_MySQL==null){
                    IP_MySQL = this.creaFileIP();
                }

            } catch (IOException ex) {
                System.out.println("errore apertura file serverIP.xml");
                IP_MySQL = this.creaFileIP();
            }            
            return IP_MySQL;

        }else{
            return IP_MySQL;
        }
    }
        


    public ArrayList<String> attributi(String tabella) {
        Relazione relazione = this.tabella(tabella);
        if (relazione == null) {
            return null;
        }
        
        ArrayList<Attributo> attributi = relazione.vediTuttiAttributi();
        ArrayList<String> tab = new ArrayList<String>();
        for (Attributo x : attributi) {
            tab.add(x.nome());
        }
        return tab;
    }
    
    public Relazione tabella(String nomeTabella){

         Relazione tabella = null;
         if(nomeTabella.compareTo(militare.nome())==0){
             tabella=militare;
         }else if(nomeTabella.compareTo(anamnesiReazioneVaccinale.nome())==0){
             tabella=anamnesiReazioneVaccinale;
         }else if(nomeTabella.compareTo(anamnesiSedutaVaccinale.nome())==0){
             tabella=anamnesiSedutaVaccinale;
         }else if(nomeTabella.compareTo(medico.nome())==0){
             tabella=medico;
         }else if(nomeTabella.compareTo(storiaVaccinale.nome())==0){
             tabella=storiaVaccinale;
         }else if(nomeTabella.compareTo(vaccinazioni.nome())==0){
             tabella=vaccinazioni;
         }else if(nomeTabella.compareTo(visita.nome())==0){
             tabella=visita;
         }else if(nomeTabella.compareTo(ricovero.nome())==0){
             tabella=ricovero;
         }else if(nomeTabella.compareTo(modelloML.nome())==0){
             tabella = modelloML;
         }else if(nomeTabella.compareTo(modelloGL.nome())==0){
             tabella = modelloGL;
         }
         return tabella;
     }


    public String creaFileIP() {
        Properties lista = new Properties();
        String ip = JOptionPane.showInputDialog("IP o nome host server");
        if (ip != null) {
            lista.setProperty("IP", ip);
        }
        try {
            FileOutputStream file = new FileOutputStream("serverIP.xml");
            lista.storeToXML(file, "ip o nome server host MySQL");
            file.close();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "errore creazione file temp", "errore", JOptionPane.ERROR_MESSAGE);
        }
        return ip;
    }

    public static String getHost() {
        return IP_MySQL;
    }
    
    public static void setHost(String IP) {
        IP_MySQL = IP;
    }
    
    /**
     * Metodo che effettua una ricerca nominativa dei record di una data tabella.
     * 
     * @param cognome
     * @param nome
     * @param nato
     * @param tabella
     * @return 
     */
    protected ArrayList<Object[]> trovaPerNominativo(String cognome, String nome, DataOraria nato,Relazione tabella) {
        ArrayList<Object[]> query =null;
        try {
            DB.connetti();
            String sqlWhere = String.format(
                        " `cognome` = '%s' AND `nome` = '%s' AND `data di nascita` = #%s# ",
                        correggi(cognome),
                        correggi(nome), 
                        nato.stampaGiornoInverso()
                    );
            query = DB.interrogazioneSempliceTabella(tabella,sqlWhere); 
        
        DB.chiudi();
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(Dati.class.getName()).log(Level.SEVERE, null, ex);
        }
        return query;
    }
    
    /*******************************************************
      * Funzione che restituisce tutti i records di una tabella
      * ordinati per cognome del militare
      * 
      * @param nomeTabella
      * @return
      ******************************************************/
     public ArrayList<Object[]> trovaTabella(String nomeTabella){
        ArrayList<Object[]> x =null;
        try {
            DB.connetti();
            Relazione tabella = tabella(nomeTabella);
            if(tabella == null) return null;
            x =
                   DB.interrogazioneSempliceTabella(
                   tabella,
                   " 1=1 ORDER BY ["+militare.nomeAttributo(0)+"]");
            DB.chiudi();
           
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(Dati.class.getName()).log(Level.SEVERE, null, ex);
        }
         return x;
     }

     /*******************************************************
      * Funzione che restituisce tutti i records di una tabella
      * ordinati e ne permette l'ordinamento
     * @param nomeTabella
      * @param ordina
      * @return
      ******************************************************/
     public ArrayList<Object[]> trovaTabella(String nomeTabella,String ordina){
        ArrayList<Object[]> x =null;
        try {
            DB.connetti();
            Relazione tabella = this.tabella(nomeTabella);
            if(tabella == null) {
                return null;
            }
            x =
                   DB.interrogazioneSempliceTabella(
                   tabella,
                   " 1=1 ORDER BY ["+ordina+"]");
            DB.chiudi();
           
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(Dati.class.getName()).log(Level.SEVERE, null, ex);
        }
         return x;
     }


     
    

    
     /***********************************************
     * eliminaMilitare tutti qui caratteri che possono
     * alterare la stringa SQL
     *
     * @param s
     * @return
     ***********************************************/
    protected String correggi(String s){
        Testo d = new Testo(s);
        d.sostituisciStringa("'", "€@h19vç");
        d.sostituisciStringa("€@h19vç","''");
        return d.getTesto();
    }
    
    /***
     * Metodo che permette di aggiungere un record a una tabella che abbia come
     * elementi chiave iniziali 'cognome', 'nome' e 'data di nascita'.
     * 
     * @param cognome
     * @param nome
     * @param dataNascita
     * @param record
     * @param tabella
     * @return 
     */
    protected boolean aggiungi(String cognome,String nome,DataOraria dataNascita,
             Object[]record,Relazione tabella){
        try {
            record[0]=cognome;
            record[1]=nome;
            record[2]=dataNascita;
            DB.connetti();
            try{
                    
                DB.aggiungiTupla(tabella,record);
                
                _evento.adesso();
                System.out.println(
                        _evento+
                        " -> aggiunto record ["+ " "+
                        cognome+" "+nome+" "+dataNascita
                        +"...] alla tabella '"+tabella.nome()+"'."
                        );
                
            }catch(EccezioneBaseDati e){
                _evento.adesso();
                System.out.println(
                        _evento+
                        " -> ERRORE impossibile aggiungere il record ["+ " "+
                        cognome+" "+nome+" "+dataNascita
                        +"] alla tabella '"+tabella.nome()+"'!"
                        );

                Logger.getLogger(Dati.class.getName()).log(Level.SEVERE, null, e);
                DB.chiudi();
                return false;
            }
            DB.chiudi();
            
                
        } catch (EccezioneBaseDati ex) {
            _evento.adesso();
                System.out.println(
                        _evento+
                        " -> ERRORE di connessione alla tabella '"+tabella.nome()+"': "+ex.getMessage()
                        );
            Logger.getLogger(Dati.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
     
    }
    
    protected void elimina(String cognome, String nome, String dataNascita,Relazione tabella) throws Errore{
        try {
            DB.connetti();
            try {
                
                DB.eliminaTupla(
                        tabella, new Object[]{cognome, nome, new DataOraria(dataNascita)});
                _evento.adesso();
                System.out.println(
                            _evento+
                            " -> eliminato record ["+ " "+
                            cognome+" "+nome+" "+dataNascita
                            +"...] della tabella '"+tabella.nome()+"'."
                            );
            
            
                
                
            } catch (EccezioneBaseDati ex) {
                _evento.adesso();
                System.out.println(
                            _evento+
                            " -> ERRORE impossibile eliminare il record ["+ " "+
                            cognome+" "+nome+" "+dataNascita
                            +"] dalla tabella '"+tabella.nome()+"'!"
                            );
                Logger.getLogger(Dati.class.getName()).log(Level.SEVERE, null, ex);
                
            }
            DB.chiudi();
        } catch (EccezioneBaseDati ex) {
            _evento.adesso();
            System.out.println(
                        _evento+
                        " -> ERRORE di connessione alla tabella '"+tabella.nome()+"': "+ex.getMessage()
                        );
            Logger.getLogger(Dati.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
        
        
   
    protected boolean modifica(Object[] chiave, Object[]record, Relazione tabella){
        try {
            DB.connetti();
            try{
                DB.modificaTupla(
                        tabella,
                        chiave,
                        record);
                
                _evento.adesso();
                System.out.println(
                        _evento+
                        " -> modofica al record ["+ " "+
                        chiave[0]+" "+chiave[1]+" "+chiave[2]
                        +"...] della tabella '"+tabella.nome()+"'."
                        );
                
            }catch(EccezioneBaseDati e){
                _evento.adesso();
                System.out.println(
                        _evento+
                        " -> ERRORE impossibile modificare il record ["+ " "+
                        chiave[0]+" "+chiave[1]+" "+chiave[2]
                        +"] dalla tabella '"+tabella.nome()+"'!"
                        );
                Logger.getLogger(Dati.class.getName()).log(Level.SEVERE, null, e);
                DB.chiudi();
                return false;
               
            }
            DB.chiudi();
            return true;
            
                
        } catch (EccezioneBaseDati ex) {
            _evento.adesso();
                System.out.println(
                        _evento+
                        " -> ERRORE di connessione alla tabella '"+tabella.nome()+"': "+ex.getMessage()
                        );
            Logger.getLogger(Dati.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
         

     }

         
    protected Object[] trova(String cognome,String nome,DataOraria dataNascita, Relazione tabella){
        
         Object[] x = null;
         try {
            DB.connetti();
             // campo del dati
            x = DB.vediTupla(
                    tabella, new Object[]{cognome, nome, dataNascita});
            DB.chiudi();            
            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(Dati.class.getName()).log(Level.SEVERE, null, ex);
            
        }
         return x;
     }
    
    
       
    /************************************************
     * Cerca tutti i militari in base alla compagnia
     * 
     * @param compagnia
     * @return
     ************************************************/
    public ArrayList<Object[]> tuttaLaCompagnia(String compagnia,String corso) {
        ArrayList<Object[]> x =null;
        try {
            this.DB.connetti();
            x =
                    this.DB.interrogazioneSempliceTabella(
                        militare,
                        String.format(
                            "[%s] = '%s' AND [%s] = '%s'"
                            + "ORDER BY [%s], [%s]",
                            militare.nomeAttributo(24),
                            corso,
                            militare.nomeAttributo(6),
                            compagnia,
                            militare.nomeAttributo(0),
                            militare.nomeAttributo(1)
                        )
                    );

            this.DB.chiudi();
           
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiGenerici.class.getName()).log(Level.SEVERE, null, ex);
           
        }
         return x;
         
    }
    
    static public boolean alfanumerico(String s){
        Testo x = new Testo(s);
        return x.controllaStringa("qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890°^-'*. ");
    }

    

    

    
}

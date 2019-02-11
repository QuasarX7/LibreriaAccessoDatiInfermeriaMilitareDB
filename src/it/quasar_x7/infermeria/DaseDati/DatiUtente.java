package it.quasar_x7.infermeria.DaseDati;


import it.quasar_x7.java.BaseDati.Attributo;
import it.quasar_x7.java.BaseDati.EccezioneBaseDati;
import it.quasar_x7.java.BaseDati.FunzioneSQL;
import it.quasar_x7.java.utile.DataOraria;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;


/*******************************************************************************
 *Implementa la struttura della base di dati infermeria MySQL
 *
 * @author Domenico della Peruta
 * @version 1.1.0 ultima modifica 25/04/2015
 ******************************************************************************/
public class DatiUtente extends Dati {

    /*ATTRIBUTI RELAZIONE UTENE*/
    public static final int _NOME       = 0;
    public static final int _PASSWORD   = 1;
    public static final int _LIVELLO    = 2;
       
    
    
    public static final String MEDICO  		= "1 - MEDICO";
    public static final String INFERMIERE    	= "2 - INFERMIERE";
    public static final String ASA_VSP    	= "3 - A.SA VSP";
    public static final String ASA_VFP    	= "4 - A.SA VFP";
    public static final String OPERATORE  	= "5 - OPERATORE";
    public static final String AMMINISTRATORE 	= "6 - AMMINISTRATORE";


//---------------------------- costruttore ------------------------------------
    /******************************************************
     * Il costruttore crea le varie entita tabelle con i
     * vari campi e domini e si connette al database
     *****************************************************/
    public DatiUtente() {
        super();
        
    }

//------------------------------ metodi ----------------------------------------

      
    /***************************************************************************
     * Restituisce l'elenco dei militari ordinato per livello.
     * 
     * @since 1.1.0 ultima modifica 28/01/2015
     * @return 
     **************************************************************************/
     public ArrayList<String> tuttiUtenti(){        
         ArrayList<String> utenti = new ArrayList<String>();
         try {           
             DB.connetti();
             ArrayList<Object[]> lista =
                    DB.interrogazioneSempliceTabella(
                            this.utente, String.format(
                                    " 1 = 1 ORDER BY `%s`, `%s` ",
                                    this.utente.nomeAttributo(_LIVELLO),
                                    this.utente.nomeAttributo(_NOME)
                            )
                    );
             DB.chiudi();
             if(lista != null){
                for(Object[] record: lista){
                    utenti.add((String) record[0]);
                }
                 return utenti;
             }
             
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiUtente.class.getName()).log(Level.SEVERE, null, ex);
            
        }
       return null;

     }


     /**************************************************************************
      * Metodo che restituisce il livello associato all'utente.
      * 
      * @since 1.0.0
      * @param utente
      * @return ritorna nullo o una stringa contenente il nome del livello
      *************************************************************************/
     public String livello(String utente){
    	 try {
             DB.connetti();
             ArrayList<Object[]> _utente=
                     DB.interrogazioneSempliceTabella(
                     this.utente,
                     String.format(
                     " [nome] = '%s' ",
                     correggi(utente)));

             DB.chiudi();
             if(_utente != null)
                 if(_utente.size() > 0)
                    if (_utente.get(0)[2] != null) 
                        return _utente.get(0)[2].toString();
                 
         } catch (EccezioneBaseDati ex) {
             Logger.getLogger(DatiUtente.class.getName()).log(Level.SEVERE, null, ex);
             
         }
         return null;
     }
     
     /**************************************************************************
      * Verifica che l'utente abbia quel livello.
      * 
      * @since 1.1.0
      * @param utente
      * @param livello
      * @return 
      *************************************************************************/
     public boolean verifica(String utente, String livello){
        String[] lista = utentiPerLivello(livello);
        if(lista != null){
            for(String voce: lista){
                if(voce != null)
                    if(voce.compareTo(utente)==0)
                        return true;
            }
        }
        return false;
     }
     /**************************************************************************
      * Trova una lista di utenti con il livello indicato.
      * 
      * @since 1.1.0 
      * @param livello
      * @return
      *************************************************************************/
     public String[] utentiPerLivello(String livello){
         try {
             DB.connetti();
             ArrayList<Object[]> record=
                     DB.interrogazioneSempliceTabella(
                        this.utente,
                        String.format(
                            " `%s` = '%s' ",
                            utente.nomeAttributo(_LIVELLO),
                            livello
                        )
                     );

             DB.chiudi();
             if(record != null){
                 ArrayList<String> utenti = new ArrayList<String>();
                 for(Object[] _utente: record){
                     if(_utente[_LIVELLO] != null){
                        if(AMMINISTRATORE.compareTo(_utente[_LIVELLO].toString())==0){
                            utenti.add(_utente[_NOME].toString());
                        }
                     }
                 }
                 if(utenti.size() > 0)
                     return utenti.toArray(new String[utenti.size()]);
             }
         } catch (EccezioneBaseDati ex) {
             Logger.getLogger(DatiUtente.class.getName()).log(Level.SEVERE, null, ex);
             
         }
         return null;
     }
     
     
     public void modificaLivello(String utente,String livello){
    	 try {
            Object[] record= new Object[]{utente,null,livello};
            Object[] chiave= new Object[]{utente};
            DB.connetti();
           try{
                    DB.modificaTupla(
                        this.utente,
                        chiave,
                        record);
           }catch(EccezioneBaseDati e){
               JOptionPane.showMessageDialog(
                               null,
                               "Nessun valore modificato nella tabella '"+
                               this.utente.nome()+"' : "+e.getMessage(),
                               "avviso",
                               JOptionPane.WARNING_MESSAGE);
               Logger.getLogger(DatiUtente.class.getName()).log(Level.SEVERE, null, e);
                
           }
            DB.chiudi();
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiUtente.class.getName()).log(Level.SEVERE, null, ex);
            
        }
     }
     
     public void aggiungiUtente(String nome,String password){
    	 this.aggiungiUtente(nome, password, OPERATORE);
     }
     
     /**
      * @since 1.0.0
      * @param nome
      * @param password
      * @param livello
      */
     public void aggiungiUtente(String nome,String password,String livello){
        try {
            Object[] record= new Object[]{nome,password,livello};
            DB.connetti();
           try{
                    DB.aggiungiTupla(
                        utente,
                        record);
           }catch(EccezioneBaseDati e){
               JOptionPane.showMessageDialog(
                               null,
                               "Nessun valore aggiunto alla tabella '"+
                               utente.nome()+"' : "+e.getMessage(),
                               "avviso",
                               JOptionPane.WARNING_MESSAGE);
               Logger.getLogger(DatiUtente.class.getName()).log(Level.SEVERE, null, e);
                
           }
            DB.chiudi();
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiUtente.class.getName()).log(Level.SEVERE, null, ex);
            
        }
     }

    public void eliminaUtente(String nome){
        try {
            DB.connetti();
            try{
                DB.eliminaTupla(
                    this.utente, new Object[]{nome});
                
            }catch(EccezioneBaseDati e){
                    javax.swing.JOptionPane.showMessageDialog(
                        null, "errore eliminazione record tabella utente",
                        "errore", javax.swing.JOptionPane.ERROR_MESSAGE);
                Logger.getLogger(DatiUtente.class.getName()).log(Level.SEVERE, null, e);
                
            }
            
            
            DB.chiudi();
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiUtente.class.getName()).log(Level.SEVERE, null, ex);
            
        }
    }

    public Object[] trovaUtene(String nome){
        try {
            DB.connetti();
            ArrayList<Object[]> _utente=
                    DB.interrogazioneSempliceTabella(
                    this.utente,
                    String.format(
                    " [nome] = '%s' ",
                    correggi(nome)));

            DB.chiudi();
            return _utente.get(0);
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiUtente.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return null;
    }


    

    public int accessoUtenteMilitare(String utente,DataOraria data) {        
        int n=0;
        try {
            DB.connetti();                
            String SQL = String.format(
                        "SELECT COUNT(*) "
                        + "FROM %s "
                        + "WHERE [%s] >= #%s 00:00:00# AND [%s] <= #%s 23:59:59# AND [%s] = '%s' ",
                        militare.nome(),//from
                        militare.nomeAttributo(28),//modifica
                        data.stampaGiornoInverso(),
                        militare.nomeAttributo(28),//modifica
                        data.stampaGiornoInverso(),
                        militare.nomeAttributo(25),//utente
                        utente                                                      
                        );
                Attributo[] dati = {
                    new Attributo("count(*)",new FunzioneSQL(1),false)
                };        
                ArrayList<Object[]> valori = DB.interrogazioneSQL(SQL, dati);
                if(valori != null){
                    if(valori.size()>0){
                        Object[] _valori = valori.get(0);
                        if(_valori != null) {
                            n = ((Long)_valori[0]).intValue();
                        } 
                    }
                }
                DB.chiudi();

            } catch (EccezioneBaseDati ex) {
                Logger.getLogger(DatiUtente.class.getName()).log(Level.SEVERE, null, ex);
                
            }
        return n;
    }
    


public int accessoUtenteVisita(String utente,DataOraria data) {        
        int n=0;
        try {
            DB.connetti();                
            String SQL = String.format(
                        "SELECT COUNT(*) "
                        + "FROM %s "
                        + "WHERE [%s] >= #%s 00:00:00# AND [%s] <= #%s 23:59:59# AND [%s] = '%s' ",
                        visita.nome(),//from
                        visita.nomeAttributo(16),//modifica
                        data.stampaGiornoInverso(),
                        visita.nomeAttributo(16),//modifica
                        data.stampaGiornoInverso(),
                        visita.nomeAttributo(14),//utente
                        utente                                                      
                        );
                Attributo[] dati = {
                    new Attributo("count(*)",new FunzioneSQL(1),false)
                };        
                ArrayList<Object[]> valori = DB.interrogazioneSQL(SQL, dati);
                if(valori != null){
                    if(valori.size()>0){
                        Object[] _valori = valori.get(0);
                        if(_valori != null) {
                            n = ((Long)_valori[0]).intValue();
                        } 
                    }
                }
                DB.chiudi();

            } catch (EccezioneBaseDati ex) {
                Logger.getLogger(DatiUtente.class.getName()).log(Level.SEVERE, null, ex);
                
            }
        return n;
    }

    public int accessoUtenteRicovero(String utente, DataOraria data) {
        int n=0;
        try {
            DB.connetti();                
            String SQL = String.format(
                        "SELECT COUNT(*) "
                        + "FROM %s "
                        + "WHERE [%s] >= #%s 00:00:00# AND [%s] <= #%s 23:59:59# AND [%s] = '%s' ",
                        ricovero.nome(),//from
                        ricovero.nomeAttributo(12),//modifica
                        data.stampaGiornoInverso(),
                        ricovero.nomeAttributo(12),//modifica
                        data.stampaGiornoInverso(),
                        ricovero.nomeAttributo(11),//utente
                        utente                                                      
                        );
                Attributo[] dati = {
                    new Attributo("count(*)",new FunzioneSQL(1),false)
                };        
                ArrayList<Object[]> valori = DB.interrogazioneSQL(SQL, dati);
                if(valori != null){
                    if(valori.size()>0){
                        Object[] _valori = valori.get(0);
                        if(_valori != null) {
                            n = ((Long)_valori[0]).intValue();
                        } 
                    }
                }
                DB.chiudi();

            } catch (EccezioneBaseDati ex) {
                Logger.getLogger(DatiUtente.class.getName()).log(Level.SEVERE, null, ex);
                
            }
        return n;
    }

    public int accessoUtenteSedVaccinale(String utente, DataOraria data) {
        int n=0;
        try {
            DB.connetti();                
            String SQL = String.format(
                        "SELECT COUNT(*) "
                        + "FROM %s "
                        + "WHERE [%s] = #%s# AND [%s] = '%s' ",
                        anamnesiSedutaVaccinale.nome(),//from
                        anamnesiSedutaVaccinale.nomeAttributo(3),//modifica
                        data.stampaGiornoInverso(),
                        anamnesiSedutaVaccinale.nomeAttributo(28),//utente
                        utente                                                      
                        );
                Attributo[] dati = {
                    new Attributo("count(*)",new FunzioneSQL(1),false)
                };        
                ArrayList<Object[]> valori = DB.interrogazioneSQL(SQL, dati);
                if(valori != null){
                    if(valori.size()>0){
                        Object[] _valori = valori.get(0);
                        if(_valori != null) {
                            n = ((Long)_valori[0]).intValue();
                        } 
                    }
                }
                DB.chiudi();

            } catch (EccezioneBaseDati ex) {
                Logger.getLogger(DatiUtente.class.getName()).log(Level.SEVERE, null, ex);
               
            }
        return n;
    }

    public int accessoUtenteStoriaVaccinale(String utente, DataOraria data) {
        int n=0;
        try {
            DB.connetti();                
            String SQL = String.format(
                        "SELECT COUNT(*) "
                        + "FROM %s "
                        + "WHERE [%s] >= #%s 00:00:00# AND [%s] <= #%s 23:59:59# AND [%s] = '%s' ",
                        storiaVaccinale.nome(),//from
                        storiaVaccinale.nomeAttributo(13),//modifica
                        data.stampaGiornoInverso(),
                        storiaVaccinale.nomeAttributo(13),//modifica
                        data.stampaGiornoInverso(),
                        storiaVaccinale.nomeAttributo(12),//utente
                        utente                                                      
                        );
                Attributo[] dati = {
                    new Attributo("count(*)",new FunzioneSQL(1),false)
                }; 
                ArrayList<Object[]> valori = DB.interrogazioneSQL(SQL, dati);
                if(valori != null){
                    if(valori.size()>0){
                        Object[] _valori = valori.get(0);
                        if(_valori != null) {
                            n = ((Long)_valori[0]).intValue();
                        } 
                    }
                }
                
                DB.chiudi();

            } catch (EccezioneBaseDati ex) {
                Logger.getLogger(DatiUtente.class.getName()).log(Level.SEVERE, null, ex);
               
            }
        return n;
    }
    
        

    public int attivitaUtente(String utente, DataOraria inizio, DataOraria fine){
        int n=0;
        try {
            DB.connetti();                
            String SQL = String.format(
                        "SELECT COUNT(*) "
                        + "FROM %s "
                        + "WHERE [%s] >= #%s 00:00:00# AND [%s] <= #%s 23:59:59# AND [%s] = '%s' ",
                        storiaVaccinale.nome(),//from
                        storiaVaccinale.nomeAttributo(13),//modifica
                        inizio.stampaGiornoInverso(),
                        storiaVaccinale.nomeAttributo(13),//modifica
                        fine.stampaGiornoInverso(),
                        storiaVaccinale.nomeAttributo(12),//utente
                        utente                                                      
                        );
                Attributo[] dati = {
                    new Attributo("count(*)",new FunzioneSQL(1),false)
                }; 
                ArrayList<Object[]> valori = DB.interrogazioneSQL(SQL, dati);
                if(valori != null){
                    if(valori.size()>0){
                        Object[] _valori = valori.get(0);
                        if(_valori != null) {
                            n += ((Long)_valori[0]).intValue();
                        } 
                    }
                }
                
                
                SQL = String.format(
                        "SELECT COUNT(*) "
                        + "FROM %s "
                        + "WHERE [%s] >= #%s 00:00:00# AND [%s] <= #%s 23:59:59# AND [%s] = '%s' ",
                        ricovero.nome(),//from
                        ricovero.nomeAttributo(12),//modifica
                        inizio.stampaGiornoInverso(),
                        ricovero.nomeAttributo(12),//modifica
                        fine.stampaGiornoInverso(),
                        ricovero.nomeAttributo(11),//utente
                        utente                                                      
                        );
                Attributo[] dati2 = {
                    new Attributo("count(*)",new FunzioneSQL(1),false)
                };        
                ArrayList<Object[]> valori2 = DB.interrogazioneSQL(SQL, dati2);
                if(valori2 != null){
                    if(valori2.size()>0){
                        Object[] _valori = valori2.get(0);
                        if(_valori != null) {
                            n += ((Long)_valori[0]).intValue();
                        } 
                    }
                }
                SQL = String.format(
                        "SELECT COUNT(*) "
                        + "FROM %s "
                        + "WHERE [%s] >= #%s 00:00:00# AND [%s] <= #%s 23:59:59# AND [%s] = '%s' ",
                        visita.nome(),//from
                        visita.nomeAttributo(16),//modifica
                        inizio.stampaGiornoInverso(),
                        visita.nomeAttributo(16),//modifica
                        fine.stampaGiornoInverso(),
                        visita.nomeAttributo(14),//utente
                        utente                                                      
                        );
                Attributo[] dati3 = {
                    new Attributo("count(*)",new FunzioneSQL(1),false)
                };        
                ArrayList<Object[]> valori3 = DB.interrogazioneSQL(SQL, dati3);
                if(valori3 != null){
                    if(valori3.size()>0){
                        Object[] _valori = valori3.get(0);
                        if(_valori != null) {
                            n += ((Long)_valori[0]).intValue();
                        } 
                    }
                }
                SQL = String.format(
                        "SELECT COUNT(*) "
                        + "FROM %s "
                        + "WHERE [%s] >= #%s 00:00:00# AND [%s] <= #%s 23:59:59# AND [%s] = '%s' ",
                        militare.nome(),//from
                        militare.nomeAttributo(28),//modifica
                        inizio.stampaGiornoInverso(),
                        militare.nomeAttributo(28),//modifica
                        fine.stampaGiornoInverso(),
                        militare.nomeAttributo(25),//utente
                        utente                                                      
                        );
                Attributo[] dati4 = {
                    new Attributo("count(*)",new FunzioneSQL(1),false)
                };        
                ArrayList<Object[]> valori4 = DB.interrogazioneSQL(SQL, dati4);
                if(valori4 != null){
                    if(valori4.size()>0){
                        Object[] _valori = valori4.get(0);
                        if(_valori != null) {
                            n += ((Long)_valori[0]).intValue();
                        } 
                    }
                }
                SQL = String.format(
                        "SELECT COUNT(*) "
                        + "FROM %s "
                        + "WHERE [%s] >= #%s 00:00:00# AND [%s] <= #%s 23:59:59# AND [%s] = '%s' ",
                        anamnesiSedutaVaccinale.nome(),//from
                        anamnesiSedutaVaccinale.nomeAttributo(3),//modifica
                        inizio.stampaGiornoInverso(),
                        anamnesiSedutaVaccinale.nomeAttributo(3),//modifica
                        fine.stampaGiornoInverso(),
                        anamnesiSedutaVaccinale.nomeAttributo(28),//utente
                        utente                                                      
                        );
                Attributo[] dati5 = {
                    new Attributo("count(*)",new FunzioneSQL(1),false)
                };        
                ArrayList<Object[]> valori5 = DB.interrogazioneSQL(SQL, dati5);
                if(valori5 != null){
                    if(valori5.size()>0){
                        Object[] _valori = valori5.get(0);
                        if(_valori != null) {
                            n += ((Long)_valori[0]).intValue();
                        } 
                    }
                }
                DB.chiudi();

            } catch (EccezioneBaseDati ex) {
                Logger.getLogger(DatiUtente.class.getName()).log(Level.SEVERE, null, ex);
                
            }
        return n;
    }

}

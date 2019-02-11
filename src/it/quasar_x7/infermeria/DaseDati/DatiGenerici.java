package it.quasar_x7.infermeria.DaseDati;


import it.quasar_x7.java.BaseDati.Attributo;
import it.quasar_x7.java.BaseDati.EccezioneBaseDati;
import it.quasar_x7.java.BaseDati.Relazione;
import it.quasar_x7.java.utile.DataOraria;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


/*******************************************************************************
 *Implementa la struttura della base di dati infermeria MySQL
 *
 * @author Domenico della Peruta
 ******************************************************************************/
public class DatiGenerici extends Dati {

//---------------------------- costruttore ------------------------------------
    /******************************************************
     * Il costruttore crea le varie entita tabelle con i
     * vari campi e domini e si connette al database
     *****************************************************/
    public DatiGenerici() {
        super();
        
    }

     /*******************************************************
      * Funzione che restituisce tutti i records di una tabella
      * ordinati per cognome del militare
      * 
     * @param nomeTabella
      * @return
      ******************************************************/
    @Override
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
            Logger.getLogger(DatiGenerici.class.getName()).log(Level.SEVERE, null, ex);
        
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
    @Override
     public ArrayList<Object[]> trovaTabella(String nomeTabella,String ordina){
        ArrayList<Object[]> x =null;
        try {
            DB.connetti();
            Relazione tabella = this.tabella(nomeTabella);
            if(tabella == null) return null;
            x =
                   DB.interrogazioneSempliceTabella(
                   tabella,
                   " 1=1 ORDER BY ["+ordina+"]");
            DB.chiudi();
           
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiGenerici.class.getName()).log(Level.SEVERE, null, ex);
            
        }
         return x;
     }

     /********************************************************
      * Funzione che trova i record di una tabella per corso
      * ed effettua un ordinamento
      * 
      * @param nomeTabella
      * @param ordina nome della colonna
      * @param corso
      * @return 
      *********************************************************/
     public ArrayList<Object[]> filtraTabellaCorso(String nomeTabella,String ordina,String corso){
         ArrayList<Object[]> records = null;
         try {
            DB.connetti();
            Relazione tabella = this.tabella(nomeTabella);
            if(tabella == null) return null;
           
  
            
            if(tabella.nome().compareTo(militare.nome())==0){   
                String condizione = String.format(
                        " [%s] = '%s' ORDER BY [%s] ",
                        militare.nomeAttributo(24),corso,ordina
                        );                    
                records = DB.interrogazioneSempliceTabella(tabella,condizione);

            
            }else{//altre tabelle
                String condizione = String.format(
                        " %s.[%s] =  %s.[%s] AND %s.[%s] =  %s.[%s] AND  %s.[%s] =  %s.[%s] "+//cofronto chiavi
                        "AND [%s] = '%s' ORDER BY %s.[%s] ",
                        //confronto chiavi
                        militare.nome(),
                        militare.nomeAttributo(0),
                        tabella.nome(),
                        militare.nomeAttributo(0),                 
                        militare.nome(),
                        militare.nomeAttributo(1),
                        tabella.nome(),
                        militare.nomeAttributo(1),                
                        militare.nome(),
                        militare.nomeAttributo(2),
                        tabella.nome(),
                        militare.nomeAttributo(2),
                        
                        militare.nomeAttributo(24),corso,
                        tabella.nome(),ordina
                        ); 
                
                   
                    
                Relazione[] tabelle = new Relazione[]{militare,tabella};
                Attributo[] colonne =tabella.vediTuttiAttributi()
                        .toArray(new Attributo[tabella.vediTuttiAttributi().size()]);
                
                records = DB.interrogazioneJoin(tabelle,colonne,condizione);

            }
            DB.chiudi();         
            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiGenerici.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return records;
     }
     /*******************************************************
      * Funzione che trova i record di una tabella per corso
      * e compagnia, effettuando un ordinamento
      * 
      * @param nomeTabella
      * @param ordina
      * @param corso
      * @param compagnia
      * @return 
      ********************************************************/
     public ArrayList<Object[]> filtraTabellaCorsoCompagnia(String nomeTabella,String ordina,String corso,String compagnia){
         ArrayList<Object[]> records = null;
         try {
            DB.connetti();

            Relazione tabella = this.tabella(nomeTabella);
            if(tabella == null) return null;
                              
            
            if(tabella.nome().compareTo(militare.nome())==0){   
                String condizione = String.format(
                        " [%s] = '%s' AND [%s] = '%s' ORDER BY [%s] ",
                        militare.nomeAttributo(24),corso,
                        militare.nomeAttributo(6),compagnia,
                        ordina
                        );                    
                records = DB.interrogazioneSempliceTabella(tabella,condizione);             

            }else{//altre tabelle
                String condizione = String.format(
                        " %s.[%s] =  %s.[%s] AND %s.[%s] =  %s.[%s] AND  %s.[%s] =  %s.[%s] "+//cofronto chiavi
                        "AND [%s] = '%s' AND [%s] = '%s' ORDER BY %s.[%s] ",
                        //confronto chiavi
                        militare.nome(),
                        militare.nomeAttributo(0),
                        tabella.nome(),
                        militare.nomeAttributo(0),                 
                        militare.nome(),
                        militare.nomeAttributo(1),
                        tabella.nome(),
                        militare.nomeAttributo(1),                
                        militare.nome(),
                        militare.nomeAttributo(2),
                        tabella.nome(),
                        militare.nomeAttributo(2),
                        
                        militare.nomeAttributo(24),corso,
                        militare.nomeAttributo(6),compagnia,
                        tabella.nome(),ordina
                        ); 
                
                   
                    
                Relazione[] tabelle = new Relazione[]{militare,tabella};
                Attributo[] colonne =tabella.vediTuttiAttributi()
                        .toArray(new Attributo[tabella.vediTuttiAttributi().size()]);
                
                records = DB.interrogazioneJoin(tabelle,colonne,condizione);
            }
            DB.chiudi();
            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiGenerici.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return records;
     }
     
     /********************************************************
      * Funzione che trova i record di una tabella per compagnia
      * ed effettua un ordinamento
      * 
      * @param nomeTabella
      * @param ordina nome della colonna
      * @param compagnia
      * @return 
      *********************************************************/
     public ArrayList<Object[]> filtraTabellaCompagnia(String nomeTabella,String ordina,String compagnia){         
                    
         ArrayList<Object[]> records = null;
         try {
                
            DB.connetti();
            Relazione tabella = this.tabella(nomeTabella);
            if(tabella == null) return null;
            
            if(tabella.nome().compareTo(militare.nome())==0){   
                String condizione = String.format(
                        " [%s] = '%s' ORDER BY [%s] ",
                        militare.nomeAttributo(6),compagnia,ordina
                        );                    
                records = DB.interrogazioneSempliceTabella(tabella,condizione);
            
            }else{//altre tabelle
                String condizione = String.format(
                        " %s.[%s] =  %s.[%s] AND %s.[%s] =  %s.[%s] AND  %s.[%s] =  %s.[%s] "+//cofronto chiavi
                        "AND [%s] = '%s' ORDER BY %s.[%s] ",
                        //confronto chiavi
                        militare.nome(),
                        militare.nomeAttributo(0),
                        tabella.nome(),
                        militare.nomeAttributo(0),                 
                        militare.nome(),
                        militare.nomeAttributo(1),
                        tabella.nome(),
                        militare.nomeAttributo(1),                
                        militare.nome(),
                        militare.nomeAttributo(2),
                        tabella.nome(),
                        militare.nomeAttributo(2),
                        
                        militare.nomeAttributo(6),compagnia,
                        tabella.nome(),ordina
                        ); 
                      
                    
                Relazione[] tabelle = new Relazione[]{militare,tabella};
                Attributo[] colonne =tabella.vediTuttiAttributi()
                        .toArray(new Attributo[tabella.vediTuttiAttributi().size()]);
                
                records = DB.interrogazioneJoin(tabelle,colonne,condizione);
            }
            DB.chiudi();
            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiGenerici.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return records;
     }
     
     /*************************************************************
      * Funzione che trova tutti i records di una tabella relativi
      * a un dato militare
      * 
      * @param nomeTabella
      * @param cognome
      * @param nome
      * @param nascita
      * @return 
      **************************************************************/
     public ArrayList<Object[]> filtraTabellaPerNominativo(String nomeTabella,String cognome,String nome,DataOraria nascita){
        ArrayList<Object[]> records = null;
        try {
            DB.connetti();
            Relazione tabella = this.tabella(nomeTabella);
            if(tabella == null) return null;
            
            
            String condizione = String.format(
                    " %s.[%s] = '%s' AND %s.[%s] = '%s' AND  %s.[%s] =  #%s# ",
                    //confronto chiavi                 
                    tabella.nome(),
                    militare.nomeAttributo(0),
                    cognome,
                    tabella.nome(),
                    militare.nomeAttributo(1),
                    nome,
                    tabella.nome(),
                    militare.nomeAttributo(2),
                    nascita.stampaGiornoInverso()
                    ); 
                  
            records = DB.interrogazioneSempliceTabella(tabella, condizione);
            DB.chiudi();
            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiGenerici.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return records;
     }


     public ArrayList<String> tuttiComuni(){
        try {
            ArrayList<String> comuni = new ArrayList<String>();
            DB.connetti();
            ArrayList<Object[]> lista =
                   DB.interrogazioneSempliceTabella(this.comune, " 1 = 1 ORDER BY [comune]");
            DB.chiudi();
            if(lista != null){
               for(Object[] record: lista){
                   comuni.add((String) record[0]);
               }
                return comuni;
            }            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiGenerici.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return null;
     }

    
    
    

}

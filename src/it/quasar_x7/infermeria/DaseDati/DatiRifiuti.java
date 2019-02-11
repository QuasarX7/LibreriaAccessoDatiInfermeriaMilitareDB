package it.quasar_x7.infermeria.DaseDati;


import it.quasar_x7.java.BaseDati.Attributo;
import it.quasar_x7.java.BaseDati.EccezioneBaseDati;
import it.quasar_x7.java.BaseDati.FunzioneSQL;
import it.quasar_x7.java.utile.DataOraria;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dr Domenico della Peruta
 */
public class DatiRifiuti extends Dati {
    public DatiRifiuti() {
        super();
    }
    
    public ArrayList<Object[]> registroRifiuti(){        
        ArrayList<Object[]> lista = null;
        try {
            DB.connetti();
        
            lista = DB.interrogazioneSempliceTabella(
                    rifiutiSanitari,
                    String.format(
                            " 1=1 ORDER BY `%s` DESC ",
                            rifiutiSanitari.nomeAttributo(
                                    BASE_DATI.RIFIUTI_SANITARI.DATA_PRODUZIONE
                            )
                    )
            );
             DB.chiudi();
             
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiRifiuti.class.getName()).log(Level.SEVERE, null, ex);
            
        }
       return lista;
    }
    
    public boolean aggiungi(String protocollo,DataOraria produzione,String codice,Long quantità,Long contenitore,Long volume,
            String medico, String responsabile,DataOraria versamento,String verbale){
        try {
            DB.connetti();
            DB.aggiungiTupla(
                    rifiutiSanitari, 
                    new Object[]{
                        protocollo,
                        produzione,
                        codice,
                        quantità,
                        contenitore,
                        volume,
                        medico,
                        responsabile,
                        versamento,
                        verbale
                    }
            );  
            DB.chiudi();
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiRifiuti.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    
    public void elimina(String protocollo){
        try {
            DB.connetti();
            DB.eliminaTupla(
                    rifiutiSanitari, 
                    new Object[]{
                        protocollo
                    }
            );  
            DB.chiudi();
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiRifiuti.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
   /**
    * Restituisce la giaccenza dei rifiuti sanitari presenti in magazzino in un dato momento
    * @param codice     codice rifiuti
    * @param data       di riferimento
    * @return ennupla composta dai campi: numero contenitori, quantità, volume
    */ 
   public ArrayList<Object[]> giacenzaMagazzino(String codice,DataOraria data){ 
        ArrayList<Object[]> lista = null;
        
        try {
            DB.connetti();
            lista = DB.interrogazioneSQL(
                    String.format(
                            " SELECT  sum(`%s`), sum(`%s`), sum(`%s`) "
                          + " FROM infermeria.`%s` "
                          + " WHERE ((`%s` is null) or (`%s` > '%s')) and `%s` <= '%s' and `%s` = '%s' ;",
                            rifiutiSanitari.nomeAttributo(BASE_DATI.RIFIUTI_SANITARI.CONTENITORI),
                            rifiutiSanitari.nomeAttributo(BASE_DATI.RIFIUTI_SANITARI.QUANTITA),
                            rifiutiSanitari.nomeAttributo(BASE_DATI.RIFIUTI_SANITARI.VOLUME),
                            rifiutiSanitari.nome(),
                            rifiutiSanitari.nomeAttributo(BASE_DATI.RIFIUTI_SANITARI.DATA_VERSAMENTO),
                            rifiutiSanitari.nomeAttributo(BASE_DATI.RIFIUTI_SANITARI.DATA_VERSAMENTO),
                            data.stampaGiornoInverso(),
                            rifiutiSanitari.nomeAttributo(BASE_DATI.RIFIUTI_SANITARI.DATA_PRODUZIONE),
                            data.stampaGiornoInverso(),
                            rifiutiSanitari.nomeAttributo(BASE_DATI.RIFIUTI_SANITARI.CODICE),
                            codice
                    ), 
                    new Attributo[]{
                        new Attributo(String.format(
                                "sum(`%s`)",
                                rifiutiSanitari.nomeAttributo(BASE_DATI.RIFIUTI_SANITARI.CONTENITORI)),
                                new FunzioneSQL(1),false
                        ),
                        new Attributo(String.format(
                                "sum(`%s`)",
                                rifiutiSanitari.nomeAttributo(BASE_DATI.RIFIUTI_SANITARI.QUANTITA)),
                                new FunzioneSQL(2),false
                        ),
                        new Attributo(String.format(
                                "sum(`%s`)",
                                rifiutiSanitari.nomeAttributo(BASE_DATI.RIFIUTI_SANITARI.VOLUME)),
                                new FunzioneSQL(3),false
                        )
                    }
            );
            DB.chiudi();
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiRifiuti.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
   } 
    
   /**
    * Restituisce il quantitativo di rifiuti sanitari prodotto nell'intervallo specificato.
    * 
    * @param codice
    * @param inizio
    * @param fine
    * @return ennupla composta dai campi: numero contenitori, quantità, volume
    */
   public ArrayList<Object[]> versamentoRifiuti(String codice,DataOraria inizio,DataOraria fine){
       ArrayList<Object[]> lista = null;
        
        try {
            DB.connetti();
            lista = DB.interrogazioneSQL(
                    String.format(
                            " SELECT  sum(`%s`), sum(`%s`), sum(`%s`) "
                          + " FROM infermeria.`%s` "
                          + " WHERE  `%s`  >= '%s' and `%s`  <= '%s' and `codice` = '%s' ;",
                            rifiutiSanitari.nomeAttributo(BASE_DATI.RIFIUTI_SANITARI.CONTENITORI),
                            rifiutiSanitari.nomeAttributo(BASE_DATI.RIFIUTI_SANITARI.QUANTITA),
                            rifiutiSanitari.nomeAttributo(BASE_DATI.RIFIUTI_SANITARI.VOLUME),
                            rifiutiSanitari.nome(),
                            rifiutiSanitari.nomeAttributo(BASE_DATI.RIFIUTI_SANITARI.DATA_VERSAMENTO),
                            inizio.stampaGiornoInverso(),
                            rifiutiSanitari.nomeAttributo(BASE_DATI.RIFIUTI_SANITARI.DATA_VERSAMENTO),
                            fine.stampaGiornoInverso(),
                            codice
                    ), 
                    new Attributo[]{
                        new Attributo(String.format(
                                "sum(`%s`)",
                                rifiutiSanitari.nomeAttributo(BASE_DATI.RIFIUTI_SANITARI.CONTENITORI)),
                                new FunzioneSQL(1),false
                        ),
                        new Attributo(String.format(
                                "sum(`%s`)",
                                rifiutiSanitari.nomeAttributo(BASE_DATI.RIFIUTI_SANITARI.QUANTITA)),
                                new FunzioneSQL(2),false
                        ),
                        new Attributo(String.format(
                                "sum(`%s`)",
                                rifiutiSanitari.nomeAttributo(BASE_DATI.RIFIUTI_SANITARI.VOLUME)),
                                new FunzioneSQL(3),false
                        )
                    }
            );
            DB.chiudi();
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiRifiuti.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
   }
    
}

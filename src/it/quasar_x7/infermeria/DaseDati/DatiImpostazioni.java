package it.quasar_x7.infermeria.DaseDati;

import it.quasar_x7.java.BaseDati.Attributo;
import it.quasar_x7.java.BaseDati.EccezioneBaseDati;
import it.quasar_x7.java.BaseDati.FunzioneSQL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/** ****************************************************************************
 *
 * @author Ing. Domenico della Peruta
 ******************************************************************************/
public class DatiImpostazioni extends Dati{

    public static final String TIPO_DATI_VACCINO    = "vaccinazioni";
    public static final String TIPO_DATI_REPORT     = "report";
    
    public static final String REPORT_INTESTAZIONE_ENTE  = "intestrazione ente";
    
    public DatiImpostazioni() {
        super();
    }
    
    /** ************************************************************************
     * Metodo che restituisce il valore associato alla 'chiave', oppure
     * null in caso di errore.
     * 
     * @param chiave
     * @return 
     **************************************************************************/
    public String valore(String chiave){
        ArrayList<Object[]> record = null;
        try {
            DB.connetti();        
            record = DB.interrogazioneSempliceTabella(
                    impostazioni,
                    String.format(
                        " `%s` = '%s' ",
                        impostazioni.nomeAttributo(0),
                        chiave
                    )        
            );
            DB.chiudi();
            if(record != null)
                if(record.size() == 1)
                    if(record.get(0)[2] != null)
                        return record.get(0)[2].toString();
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiImpostazioni.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return null;
    }
    
    /**
     * Metodo che crea un nuovo record.
     * @param chiave
     * @param tipo
     * @param valore 
     */
    public void valore(String chiave, String tipo, String valore){
        try {
            DB.connetti();        
            DB.aggiungiTupla(this.impostazioni, new Object[]{chiave,tipo,valore});
            DB.chiudi();
            
        } catch (EccezioneBaseDati ex) {
            if(ex.getMessage().contains("Duplicate")){
                try {
                    DB.modificaTupla(this.impostazioni, new Object[]{chiave}, new Object[]{chiave,tipo,valore});
                } catch (EccezioneBaseDati ex1) {
                    Logger.getLogger(DatiImpostazioni.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }else{
                Logger.getLogger(DatiImpostazioni.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }

    /**
     * Metodo conta il numero di record di quel tipo.
     * 
     * @param tipoKey
     * @return 
     */
    public int tipo(String tipoKey) {
    
        ArrayList<Object[]> record = null;
        try {
            this.DB.connetti();
            String sql = String.format(
                    " `%s` = '%s' ",
                    impostazioni.nomeAttributo(1),
                    tipoKey
            );
            
            record = DB.interrogazioneSempliceTabella(
                    impostazioni,
                    new Attributo[]{
                        new Attributo("count(*)",new FunzioneSQL(1),false)
                    },
                    sql
            );
            this.DB.chiudi();
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiImpostazioni.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(record != null){
            if(record.size() == 1){
                return new Integer(record.get(0)[0].toString());
            }
        }
        return 0;
    }
    
    
    public void elimina(String chiave){
        try {
            DB.connetti();
            DB.eliminaTupla(impostazioni, new Object[]{chiave});
            DB.chiudi();
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiImpostazioni.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}

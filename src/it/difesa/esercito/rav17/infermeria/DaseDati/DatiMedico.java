package it.difesa.esercito.rav17.infermeria.DaseDati;


import it.quasar_x7.java.BaseDati.EccezioneBaseDati;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;


/*******************************************************************************
 *Implementa la struttura della base di dati infermeria MySQL
 *
 * @author Domenico della Peruta
 ******************************************************************************/
public class DatiMedico extends Dati {

//---------------------------- costruttore ------------------------------------
    /******************************************************
     * Il costruttore crea le varie entita tabelle con i
     * vari campi e domini e si connette al database
     *****************************************************/
    public DatiMedico() {
        super();
        
    }

     /*******************************************************
      * Funzione che restituisce tutti i records di una tabella
     

     /**************************************************
      * Trova tutti i medici registrati nella BD
      * @return
      *************************************************/
     public ArrayList<String> tuttiMedici(){
        try {
            ArrayList<String> medici = new ArrayList<String>();
            DB.connetti();
            ArrayList<Object[]> lista =
                   DB.interrogazioneSempliceTabella(this.medico, " 1 = 1 ");
            DB.chiudi();
            if(lista != null){
               for(Object[] record: lista){
                   medici.add((String) record[0]);
               }
                return medici;
            }
            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiMedico.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return null;
     }

     public void aggiungiMedico(String medico) {
        try {
            DB.connetti();
            try{
                    DB.aggiungiTupla(
                        this.medico,
                        new Object[]{medico});
                    
            }catch(EccezioneBaseDati e){
                JOptionPane.showMessageDialog(
                               null,
                               "Nessun valore aggiunto alla tabella "+
                               this.medico.nome()+"!",
                               "avviso",
                               JOptionPane.WARNING_MESSAGE);
                Logger.getLogger(DatiMedico.class.getName()).log(Level.SEVERE, null, e);
                
            }
            DB.chiudi();
                
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiMedico.class.getName()).log(Level.SEVERE, null, ex);
            
        }
    }

    public void eliminaMedico(String medico) {
        try {
            DB.connetti();
            DB.eliminaTupla(this.medico, new Object[]{medico});
            DB.chiudi();
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiMedico.class.getName()).log(Level.SEVERE, null, ex);
            
        }
    }


    
    
    

}

package it.quasar_x7.infermeria.DaseDati;

import it.quasar_x7.java.BaseDati.Attributo;
import it.quasar_x7.java.BaseDati.EccezioneBaseDati;
import it.quasar_x7.java.BaseDati.FunzioneSQL;
import it.quasar_x7.java.utile.DataOraria;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Ing. Domenico della PERUTA
 */
public class DatiPresenza extends Dati{
    
    public DatiPresenza() {
        super();
    }

    public boolean aggiungiPosizione(String posizione) {
        boolean aggiunto = false;
        try {
            Object[] record= new Object[]{posizione};
            DB.connetti();
           try{
                    DB.aggiungiTupla(this.posizione,record);
                    aggiunto = true;
                    _evento.adesso();
                    System.out.println(
                                _evento+
                                " -> aggiunto record ["+ " "+
                                posizione+" "
                                +"...] alla tabella '"+this.posizione.nome()+"'"
                                );
           }catch(EccezioneBaseDati e){
               Logger.getLogger(DatiPresenza.class.getName()).log(Level.SEVERE, null, e);
           }
            DB.chiudi();
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiPresenza.class.getName()).log(Level.SEVERE, null, ex);
        }
        return aggiunto;
    }
    
    public boolean eliminaPosizione(String posizione) {
        boolean eliminata = false;
        try {
            Object[] record= new Object[]{posizione};
            DB.connetti();
           try{
                    DB.eliminaTupla(this.posizione,record);
                    eliminata = true;
                    _evento.adesso();
                    System.out.println(
                                _evento+
                                " -> eliminato record ["+ " "+
                                posizione+" "
                                +"...] dalla tabella '"+this.posizione.nome()+"'"
                                );
           }catch(EccezioneBaseDati e){
               Logger.getLogger(DatiPresenza.class.getName()).log(Level.SEVERE, null, e);
           }
            DB.chiudi();
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiPresenza.class.getName()).log(Level.SEVERE, null, ex);
        }
        return eliminata;
    }

    
    public boolean aggiungiPresenza(DataOraria data,String militare, String posizione) {
        boolean aggiunto = false;
        try {
            Object[] record= new Object[]{data,militare,posizione};
            DB.connetti();
           try{
                    DB.aggiungiTupla(presenza,record);
                    aggiunto = true;
                    _evento.adesso();
                    System.out.println(
                                _evento+
                                " -> aggiunto record ["+ " "+
                                data+" "+militare+" "
                                +"...] alla tabella '"+presenza.nome()+"'"
                                );
           }catch(EccezioneBaseDati e){
               Logger.getLogger(DatiPresenza.class.getName()).log(Level.SEVERE, null, e);
           }
            DB.chiudi();
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiPresenza.class.getName()).log(Level.SEVERE, null, ex);
        }
        return aggiunto;
     }

    public boolean  modificaPresenza(DataOraria data, String militare, String posizione) {
        boolean modificato = false;
        try {
            DB.connetti();
            try {
                DB.modificaTupla(presenza, new Object[]{data,militare}, new Object[]{data,militare,posizione});
                modificato = true;
                _evento.adesso();
                System.out.println(
                                _evento+
                                " -> modifica al record ["+ " "+data+" "+militare+" "+"...] della tabella '"+presenza.nome()+"'"
                                );
                
            } catch (EccezioneBaseDati ex) {
                Logger.getLogger(DatiPresenza.class.getName()).log(Level.SEVERE, null, ex);
            }
            DB.chiudi();
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiPresenza.class.getName()).log(Level.SEVERE, null, ex);
        }
        return modificato;    
    }


    public Object[] trovaPresenza(DataOraria data, String militare) {
        Object[] record = null;
        try {
            DB.connetti();
            try {
                record = DB.vediTupla(presenza, new Object[]{data,militare});
              
            } catch (EccezioneBaseDati ex) {
                Logger.getLogger(DatiPresenza.class.getName()).log(Level.SEVERE, null, ex);
            }
            DB.chiudi();
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiPresenza.class.getName()).log(Level.SEVERE, null, ex);
        }
        return record;
    }

    public ArrayList<String> tutteLePosizione() {
        try {
            ArrayList<String> voce = new ArrayList<String>();
            DB.connetti();
            ArrayList<Object[]> lista =
                   DB.interrogazioneSempliceTabella(this.posizione, " 1 = 1 ");
            DB.chiudi();
            if(lista != null){
               for(Object[] record: lista){
                   voce.add((String) record[0]);
               }
                return voce;
            }
            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiPresenza.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return null;
    }

    private String fineSettimana(boolean v){
        return v==true ? 
                " AND ( DATE_FORMAT( data ,'%w') = 0 OR DATE_FORMAT( data ,'%w') = 6 ) " 
                : " AND ( DATE_FORMAT( data ,'%w') > 0 AND DATE_FORMAT( data ,'%w') < 6 ) " ;
    }
    public Long totalePresenza(String posizione, String militare,int intervallo,boolean festivo) {
         ArrayList<Object[]> numero =null;
         DataOraria prima = new DataOraria();
         prima.decrementaGiorno(intervallo);
         
         try {
            this.DB.connetti();
           String sql = String.format(
                   " militare = '%s' AND posizione = '%s' AND data <= '%s' AND data >= '%s' ", 
                   militare,
                   posizione,
                   _evento.stampaGiornoInverso(),
                   prima.stampaGiornoInverso()
                   );
           
            numero =
                   DB.interrogazioneSempliceTabella(
                   presenza,
                   new Attributo[]{                       
                        new Attributo("count(*)",new FunzioneSQL(1),false)
                    },
                   sql+fineSettimana(festivo));
            this.DB.chiudi();

            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiPresenza.class.getName()).log(Level.SEVERE, null, ex);
            
        }
         if(numero != null)
            return (Long) numero.get(0)[0];
         return -1L;
    }

    public Long totalePresenza(String posizione, String militare,boolean festivo) {
        ArrayList<Object[]> numero =null;
         DataOraria prima = new DataOraria();
         try {
            this.DB.connetti();
           String sql = String.format(
                   " militare = '%s' AND posizione = '%s'  ", 
                   militare,
                   posizione
                   );
           
            numero =
                   DB.interrogazioneSempliceTabella(
                   presenza,
                   new Attributo[]{                       
                        new Attributo("count(*)",new FunzioneSQL(1),false)
                    },
                   sql+fineSettimana(festivo));
            this.DB.chiudi();

            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiPresenza.class.getName()).log(Level.SEVERE, null, ex);
            
        }
         if(numero != null)
            return (Long) numero.get(0)[0];
         return -1L;
    }

   
}

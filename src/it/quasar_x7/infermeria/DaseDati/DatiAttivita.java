package it.quasar_x7.infermeria.DaseDati;


import it.quasar_x7.java.BaseDati.EccezioneBaseDati;
import it.quasar_x7.java.utile.DataOraria;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


/*******************************************************************************
 *
 *
 * @author Domenico della Peruta
 ******************************************************************************/
public class DatiAttivita extends Dati {

//---------------------------- costruttore ------------------------------------
    /******************************************************
     * 
     *****************************************************/
    public DatiAttivita() {
        super();
        
    }

    
    
    
    public boolean aggiungiPatologia(String patologie){
        try {
            DB.connetti();

            
            DB.aggiungiTupla(
                    this.patologie,
                    new Object[]{patologie});
                
            

            DB.chiudi();
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiAttivita.class.getName()).log(Level.SEVERE, null, ex);
            
            return false;
        }
        return true;
    }

    public boolean eliminaPatologia(String patologia){
        try {
            DB.connetti();

            DB.eliminaTupla(
                    this.patologie,
                    new Object[]{patologia});

            DB.chiudi();

            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiAttivita.class.getName()).log(Level.SEVERE, null, ex);
            
            return false;
        }
        return true;
    }
          
    
    public Object[] vediPatologie() {
        try {
            DB.connetti();
            
            ArrayList<Object[]> sql =
                    DB.vediTutteLeTuple(patologie);           
                      
            DB.chiudi();
            if (sql != null){
                int i=0;
                Object[] elenco = new Object[sql.size()];
                for(Object[] record : sql){
                    elenco[i++]= record[0];
                } 
                return elenco;
            }
            return null;
            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiAttivita.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return null;
    }

    public boolean aggiungiTerapia(String terapia){
        try {
            DB.connetti();

            DB.aggiungiTupla(
                    this.terapia,
                    new Object[]{terapia});

            DB.chiudi();

            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiAttivita.class.getName()).log(Level.SEVERE, null, ex);
            
            return false;
        }
        return true;
    }

    public boolean eliminaTerapia(String terapia){
        try {
            DB.connetti();

            DB.eliminaTupla(
                    this.terapia,
                    new Object[]{terapia});

            DB.chiudi();

            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiAttivita.class.getName()).log(Level.SEVERE, null, ex);
            
            return false;
        }
        return true;
    }

          
    
    public Object[] vediTerapia() {
        try {
            DB.connetti();
            
            ArrayList<Object[]> sql =
                    DB.vediTutteLeTuple(terapia);           
                      
            DB.chiudi();
            if (sql != null){
                int i=0;
                Object[] elenco = new Object[sql.size()];
                for(Object[] record : sql){
                    elenco[i++]= record[0];
                } 
                return elenco;
            }
            return null;
            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiAttivita.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return null;
    }

    
    public boolean aggiungiAttivitàClinica(String tipo,DataOraria data,String patologia,int numero) {
         
        try {
            DB.connetti();
            DB.aggiungiTupla(attivitàClinica, new Object[]{tipo,data,patologia,numero});
            DB.chiudi();
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiAttivita.class.getName()).log(Level.SEVERE, null, ex);
            
            return false;
        }
        return true;
    }
    
    
    public ArrayList<Object[]> vediAttivitàClinica(String tipo,DataOraria data) {
         ArrayList<Object[]> lista = new ArrayList<Object[]>();
        try {
            DB.connetti();
            String condizione=" [tipo] = '"+tipo+"' AND [data] = #"+data.stampaGiornoInverso()+"# ";
            lista=DB.interrogazioneSempliceTabella(attivitàClinica, condizione);
            DB.chiudi();
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiAttivita.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return lista;
    }
    
    public ArrayList<Object[]> vediAttivitàClinica(String tipo,DataOraria inizio,DataOraria fine) {
         ArrayList<Object[]> lista = new ArrayList<Object[]>();
        try {
            DB.connetti();
            String condizione=" [tipo] = '"+tipo+
                    "' AND [data] >= #"+inizio.stampaGiornoInverso()+"# "+
                    " AND [data] <= #"+fine.stampaGiornoInverso()+"# ";
            
            
            lista=DB.interrogazioneSempliceTabella(attivitàClinica, condizione);
            DB.chiudi();
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiAttivita.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return lista;
    }


    public void eliminaAttivitàClinica(String attività, DataOraria oggi, String patologia) {
        try {
            DB.connetti();
            DB.eliminaTupla(attivitàClinica, new Object[]{attività,oggi,patologia});
            DB.chiudi();
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiAttivita.class.getName()).log(Level.SEVERE, null, ex);
            
        }
    }
    
    public boolean aggiungiAttivitàTrasferimentoOspedale(String motivo,DataOraria data,String luogo,int numero) {
         
        try {
            DB.connetti();
            DB.aggiungiTupla(trasferimentoOspedale, new Object[]{motivo,data,luogo,numero});
            DB.chiudi();
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiAttivita.class.getName()).log(Level.SEVERE, null, ex);
            
            return false;
        }
        return true;
    }
    
    
    public ArrayList<Object[]> vediAttivitàTrasferimentoOspedale(DataOraria data) {
         ArrayList<Object[]> lista = new ArrayList<Object[]>();
        try {
            DB.connetti();
            String condizione=" [data] = #"+data.stampaGiornoInverso()+"# ";
            lista=DB.interrogazioneSempliceTabella(trasferimentoOspedale, condizione);
            DB.chiudi();
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiAttivita.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return lista;
    }

    public ArrayList<Object[]> vediAttivitàTrasferimentoOspedale(DataOraria inizio,DataOraria fine) {
         ArrayList<Object[]> lista = new ArrayList<Object[]>();
        try {
            DB.connetti();
            String condizione=" [data] >= #"+inizio.stampaGiornoInverso()+"# AND [data] <= #"+fine.stampaGiornoInverso()+"# ";
            lista=DB.interrogazioneSempliceTabella(trasferimentoOspedale, condizione);
            DB.chiudi();
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiAttivita.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return lista;
    }

    
    public void eliminaAttivitàTrasferimentoOspedale(String motivo, DataOraria oggi, String luogo) {
        try {
            DB.connetti();
            DB.eliminaTupla(trasferimentoOspedale, new Object[]{motivo,oggi,luogo});
            DB.chiudi();
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiAttivita.class.getName()).log(Level.SEVERE, null, ex);
            
        }
    }
        
    public boolean aggiungiAttivitàEsterna(String tipo,String luogo,DataOraria data,int durata,int medici,int infermieri,int asa) {
         
        try {
            DB.connetti();
            DB.aggiungiTupla(attivitàEsterna, new Object[]{tipo,luogo,data,durata,medici,infermieri,asa});
            DB.chiudi();
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiAttivita.class.getName()).log(Level.SEVERE, null, ex);
            
            return false;
        }
        return true;
    }
    
    
    public ArrayList<Object[]> vediAttivitàEsterna(String tipo,DataOraria data) {
         ArrayList<Object[]> lista = new ArrayList<Object[]>();
        try {
            DB.connetti();
            String condizione=" [tipo] = '"+tipo+"' AND [data] = #"+data.stampaGiornoInverso()+"# ";
            lista=DB.interrogazioneSempliceTabella(attivitàEsterna, condizione);
            DB.chiudi();
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiAttivita.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return lista;
    }

    
    public ArrayList<Object[]> vediAttivitàEsterna(String tipo,DataOraria inizio,DataOraria fine) {
         ArrayList<Object[]> lista = new ArrayList<Object[]>();
        try {
            DB.connetti();
            String condizione=" [tipo] = '"+tipo+"' AND [data] >= #"+inizio.stampaGiornoInverso()+"#  AND [data] <= #"+fine.stampaGiornoInverso()+"# ";
            lista=DB.interrogazioneSempliceTabella(attivitàEsterna, condizione);
            DB.chiudi();
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiAttivita.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return lista;
    }
    
    public void eliminaAttivitàEsterna(String tipo,String luogo,DataOraria data) {
        try {
            DB.connetti();
            DB.eliminaTupla(attivitàEsterna, new Object[]{tipo,luogo,data});
            DB.chiudi();
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiAttivita.class.getName()).log(Level.SEVERE, null, ex);
            
        }
    }
    public boolean aggiungiAttivitàMedicoLegali(String pratica,String attività,DataOraria data,int numero) {
         
        try {
            DB.connetti();
            DB.aggiungiTupla(attivitàMedicoLegali, new Object[]{data,pratica,attività,numero});
            DB.chiudi();
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiAttivita.class.getName()).log(Level.SEVERE, null, ex);
            
            return false;
        }
        return true;
    }
    
    
    public ArrayList<Object[]> vediAttivitàMedicoLegali(String attività,DataOraria data) {
         ArrayList<Object[]> lista = new ArrayList<Object[]>();
        try {
            DB.connetti();
            String condizione=" [attivita] = '"+attività+"' AND [data] = #"+data.stampaGiornoInverso()+"# ";
            lista=DB.interrogazioneSempliceTabella(attivitàMedicoLegali, condizione);
            DB.chiudi();
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiAttivita.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return lista;
    }
    
    
    public ArrayList<Object[]> vediAttivitàMedicoLegali(String attività,DataOraria inizio,DataOraria fine) {
         ArrayList<Object[]> lista = new ArrayList<Object[]>();
        try {
            DB.connetti();
            String condizione=" [attivita] = '"+attività+"' AND [data] >= #"
                    +inizio.stampaGiornoInverso()+"# "+" AND [data] <= #"+fine.stampaGiornoInverso()+"# ";
            lista=DB.interrogazioneSempliceTabella(attivitàMedicoLegali, condizione);
            DB.chiudi();
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiAttivita.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return lista;
    }

    public void eliminaAttivitàMedicoLegali(String pratica,DataOraria data) {
        try{
            DB.connetti();
            DB.eliminaTupla(attivitàMedicoLegali, new Object[]{data,pratica});
            DB.chiudi();
        }catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiAttivita.class.getName()).log(Level.SEVERE, null, ex);
            
        }
    }

}

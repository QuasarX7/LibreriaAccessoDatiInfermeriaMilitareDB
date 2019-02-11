
package it.quasar_x7.infermeria.DaseDati;

import it.quasar_x7.java.BaseDati.Attributo;
import it.quasar_x7.java.BaseDati.DatoDataOraria;
import it.quasar_x7.java.BaseDati.DatoStringa;
import it.quasar_x7.java.BaseDati.EccezioneBaseDati;
import it.quasar_x7.java.BaseDati.FunzioneSQL;
import it.quasar_x7.java.BaseDati.Relazione;
import it.quasar_x7.java.utile.DataOraria;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/*******************************************************************************
 *
 * @author Domenico della Peruta
 * @version 1.1.0 ultima versione 03/02/15
 ******************************************************************************/
public class DatiVerbaleVisita extends Dati{

    public DatiVerbaleVisita() {
        super();
    }
    
    public int dimensioneVerbaleVisite(){
        return this.modelloML.vediTuttiAttributi().size();
    }
    
    public boolean modificaVerbale(DataOraria data, String cognome, String nome, DataOraria dataNascita, Object[] record){
        try {
            record[0]=cognome;
            record[1]=nome;
            record[2]=dataNascita;
            record[3]=data;

            DB.connetti();
            DB.modificaTupla(modelloML,new Object[]{cognome,nome,dataNascita,data}, record);
            DB.chiudi();
            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiVerbaleVisita.class.getName()).log(Level.SEVERE, null, ex);
           return false;
        }
        return true;
    }
    
    /***************************************************************************
     * Metodo che aggiunge il verbale ML della cir. 5000.
     * 
     * @since 1.1.0
     * @param data
     * @param cognome
     * @param nome
     * @param dataNascita
     * @param record
     * @return true se il verbale è aggiunto correttamente.
     **************************************************************************/
    public boolean aggiungiVerbale(DataOraria data, String cognome, String nome, DataOraria dataNascita, Object[] record){
                
        try {
            record[0]=cognome;
            record[1]=nome;
            record[2]=dataNascita;
            record[3]=data;

            DB.connetti();
            /*try{
                DB.eliminaTupla(modelloML, new Object[]{cognome,nome,dataNascita,data});
            }catch(EccezioneBaseDati e){
            }*/
            DB.aggiungiTupla(modelloML,record);
            DB.chiudi();
            
        } catch (EccezioneBaseDati ex) {
            if(ex.getMessage().contains("Duplicate entry"))
                return true;
            Logger.getLogger(DatiVerbaleVisita.class.getName()).log(Level.SEVERE, null, ex);
           return false;
        }
        return true;
    }
    
    
    /**
     * 
     * @param data
     * @param cognome
     * @param nome
     * @param dataNascita
     * @return 
     */
    public boolean eliminaVerbale(DataOraria data, String cognome, String nome, DataOraria dataNascita){
        boolean errore = false;
        try {
            DB.connetti();
           DB.eliminaTupla(modelloML,
                        new Object[]{cognome,nome,dataNascita,data});
           
           DB.chiudi();
          
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiVerbaleVisita.class.getName()).log(Level.SEVERE, null, ex);
            errore = true;
        }
        return !errore;
    }
    
    public Object[] vediVerbale(DataOraria data, String cognome, String nome, DataOraria dataNascita){
        Object[] x =null;
        try {
            DB.connetti();
            // campo del dati
            x = DB.vediTupla(modelloML, 
                    new Object[]{cognome, nome, dataNascita,data}
            );
            DB.chiudi();
            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiVerbaleVisita.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        
        return x;
    }
    
    public ArrayList<Object[]> trovaVerbali(String cognome, String nome, DataOraria nato) {
        ArrayList<Object[]> _verbale =null;
        try {
            DB.connetti();
            
            _verbale =
                    DB.interrogazioneSempliceTabella(modelloML,
                    String.format(
                    " [cognome] = '%s' AND [nome] = '%s' "
                    + "AND [data di nascita] = #%s# ",
                    correggi(cognome), correggi(nome), nato.stampaGiornoInverso()));

            DB.chiudi();
            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiVerbaleVisita.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return _verbale;
        
        
    }
    
    /**
     * 
     * @param data
     * @return Lista di record composto da:
      * <ul>
      *     <li>`seduta vaccinale`</li>
      *     <li>`grado`</li>
      *     <li>`cognome`</li>
      *     <li>`nome`</li>
      *     <li>`data di nascita`</li>
      *     <li>`compagnia`</li>
      *     <li>`diagnosi`</li>
      * </ul>
     */
    public ArrayList<Object[]> trovaVerbaliML(DataOraria data) {
        ArrayList<Object[]> _verbale =null;
        try {
            DB.connetti();
            
            _verbale =
                    DB.interrogazioneJoin(
                        new Relazione[]{militare,modelloML},
                        new Attributo[]{
                            militare.vediAttributo(BASE_DATI.MILITARE.GRADO),
                            militare.vediAttributo(BASE_DATI.MILITARE.COGNOME),
                            militare.vediAttributo(BASE_DATI.MILITARE.NOME),
                            militare.vediAttributo(BASE_DATI.MILITARE.DATA_NASCITA),
                            militare.vediAttributo(BASE_DATI.MILITARE.COMPAGNIA),
                            modelloML.vediAttributo(BASE_DATI.MODELLO_ML.DIAGNOSI)
                        },
                        String.format(
                                  " `%s`.`%s` = `%s`.`%s` AND `%s`.`%s` = `%s`.`%s` AND `%s`.`%s` = `%s`.`%s` "
                                + " AND `%s` = '%s' ",
                                modelloML.nome(), modelloML.nomeAttributo(0),  militare.nome(), militare.nomeAttributo(0),
                                modelloML.nome(), modelloML.nomeAttributo(1),  militare.nome(), militare.nomeAttributo(1),
                                modelloML.nome(), modelloML.nomeAttributo(2),  militare.nome(), militare.nomeAttributo(2),
                                modelloML.nomeAttributo(3),data.stampaGiornoInverso()
                        )
                    );

            DB.chiudi();
            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiVerbaleVisita.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return _verbale;
    }
    
    public ArrayList<Object[]> trovaVerbali(DataOraria inizio,DataOraria fine) {
        ArrayList<Object[]> _verbale =null;
        try {
            DB.connetti();
            
            _verbale =
                    DB.interrogazioneJoin(
                    new Relazione[]{militare,modelloML},
                    new Attributo[]{
                        militare.vediAttributo("grado"),
                        militare.vediAttributo("cognome"),
                        militare.vediAttributo("nome"),
                        militare.vediAttributo("data di nascita"),
                        modelloML.vediAttributo("data"),
                        modelloML.vediAttributo("statura"),
                        modelloML.vediAttributo("peso")
                    },
                    String.format(
                              " `%s`.`%s` = `%s`.`%s` AND `%s`.`%s` = `%s`.`%s` AND `%s`.`%s` = `%s`.`%s` "
                            + " AND `%s` >= '%s' AND `%s` <= '%s' ",
                    modelloML.nome(), modelloML.nomeAttributo(0),  militare.nome(), militare.nomeAttributo(0),
                    modelloML.nome(), modelloML.nomeAttributo(1),  militare.nome(), militare.nomeAttributo(1),
                    modelloML.nome(), modelloML.nomeAttributo(2),  militare.nome(), militare.nomeAttributo(2),
                    modelloML.nomeAttributo(3),inizio.stampaGiornoInverso(),
                    modelloML.nomeAttributo(3), fine.stampaGiornoInverso()));

            DB.chiudi();
            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiVerbaleVisita.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return _verbale;
        
    }

    public int numeroVerbali(String reggimento, DataOraria d) {
        int n=0;
        try {
            DB.connetti();
            String SQL = String.format(//26-30
                    "SELECT COUNT(*) "
                            + "FROM %s, %s "
                            + "WHERE `%s`.`%s` = #%s#  AND "
                            + " `%s`.`%s` = `%s`.`%s` AND `%s`.`%s` = `%s`.`%s` AND `%s`.`%s` = `%s`.`%s` "
                            + "  AND  `%s`.`%s` LIKE '%s' ",
                    modelloML.nome(),militare.nome(),//from
                    
                    modelloML.nome(),modelloML.nomeAttributo(3),
                    d.stampaGiornoInverso(), // #data_di_oggi#
                    modelloML.nome(), modelloML.nomeAttributo(0),  militare.nome(), militare.nomeAttributo(0),
                    modelloML.nome(), modelloML.nomeAttributo(1),  militare.nome(), militare.nomeAttributo(1),
                    modelloML.nome(), modelloML.nomeAttributo(2),  militare.nome(), militare.nomeAttributo(2),
                    
                    militare.nome(),militare.nomeAttributo(6),"%"+reggimento+"%"
            );
            Attributo[] dati = {
                new Attributo("count(*)",new FunzioneSQL(1),false)
            };
            n = ((Long)DB.interrogazioneSQL(SQL, dati).get(0)[0]).intValue();
            DB.chiudi();
            
            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiVerbaleVisita.class.getName()).log(Level.SEVERE, null, ex);
        }
        return n;
        
    }
    
    public int numeroInviiConvalesenza(DataOraria d) {
        int n=0;
        try {
            DB.connetti();
            String SQL = String.format("SELECT COUNT(*) "
                    + "FROM %s, %s "
                    + "WHERE `%s`.`%s` = #%s#  AND "
                    + " `%s`.`%s` = TRUE   AND "
                    + " `%s`.`%s` = `%s`.`%s` AND `%s`.`%s` = `%s`.`%s` AND `%s`.`%s` = `%s`.`%s` ",
                    modelloML.nome(),militare.nome(),//from
                    modelloML.nome(),modelloML.nomeAttributo(3),
                    d.stampaGiornoInverso(), // #data_di_oggi#
                    modelloML.nome(),modelloML.nomeAttributo(it.quasar_x7.infermeria.DaseDati.BASE_DATI.MODELLO_ML.CONVALESCENZA),
                    modelloML.nome(), modelloML.nomeAttributo(0),  militare.nome(), militare.nomeAttributo(0),
                    modelloML.nome(), modelloML.nomeAttributo(1),  militare.nome(), militare.nomeAttributo(1),
                    modelloML.nome(), modelloML.nomeAttributo(2),  militare.nome(), militare.nomeAttributo(2)
            );
            Attributo[] dati = {
                new Attributo("count(*)",new FunzioneSQL(1),false)
            };
            n = ((Long)DB.interrogazioneSQL(SQL, dati).get(0)[0]).intValue();
            DB.chiudi();
            
            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiVerbaleVisita.class.getName()).log(Level.SEVERE, null, ex);
        }
        return n;
    }
    
    public int numeroInviiMalattia(DataOraria d) {
        int n=0;
        try {
            DB.connetti();
            String SQL = String.format("SELECT COUNT(*) "
                    + "FROM %s, %s "
                    + "WHERE `%s`.`%s` = #%s#  AND "
                    + " `%s`.`%s` = TRUE   AND "
                    + " `%s`.`%s` = `%s`.`%s` AND `%s`.`%s` = `%s`.`%s` AND `%s`.`%s` = `%s`.`%s` ",
                    modelloML.nome(),militare.nome(),//from
                    modelloML.nome(),modelloML.nomeAttributo(3),
                    d.stampaGiornoInverso(), // #data_di_oggi#
                    modelloML.nome(),modelloML.nomeAttributo(it.quasar_x7.infermeria.DaseDati.BASE_DATI.MODELLO_ML.AMMALATO),
                    modelloML.nome(), modelloML.nomeAttributo(0),  militare.nome(), militare.nomeAttributo(0),
                    modelloML.nome(), modelloML.nomeAttributo(1),  militare.nome(), militare.nomeAttributo(1),
                    modelloML.nome(), modelloML.nomeAttributo(2),  militare.nome(), militare.nomeAttributo(2)
            );
            Attributo[] dati = {
                new Attributo("count(*)",new FunzioneSQL(1),false)
            };
            n = ((Long)DB.interrogazioneSQL(SQL, dati).get(0)[0]).intValue();
            DB.chiudi();
            
            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiVerbaleVisita.class.getName()).log(Level.SEVERE, null, ex);
        }
        return n;
    }
    
    public int numeroInviiCMO(DataOraria d) {
        int n=0;
        try {
            DB.connetti();
            String SQL = String.format(//26-30
                    "SELECT COUNT(*) "
                    + "FROM %s, %s "
                    + "WHERE `%s`.`%s` = #%s#  AND "
                    + " ( `%s`.`%s` = TRUE  OR `%s`.`%s` = TRUE ) AND "
                    + " `%s`.`%s` = `%s`.`%s` AND `%s`.`%s` = `%s`.`%s` AND `%s`.`%s` = `%s`.`%s` ",
                    modelloML.nome(),militare.nome(),//from
                    modelloML.nome(),modelloML.nomeAttributo(3),
                    d.stampaGiornoInverso(), // #data_di_oggi#
                    modelloML.nome(),modelloML.nomeAttributo(26),
                    modelloML.nome(),modelloML.nomeAttributo(29),
                    modelloML.nome(), modelloML.nomeAttributo(0),  militare.nome(), militare.nomeAttributo(0),
                    modelloML.nome(), modelloML.nomeAttributo(1),  militare.nome(), militare.nomeAttributo(1),
                    modelloML.nome(), modelloML.nomeAttributo(2),  militare.nome(), militare.nomeAttributo(2)
            );
            Attributo[] dati = {
                new Attributo("count(*)",new FunzioneSQL(1),false)
            };
            n = ((Long)DB.interrogazioneSQL(SQL, dati).get(0)[0]).intValue();
            DB.chiudi();
            
            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiVerbaleVisita.class.getName()).log(Level.SEVERE, null, ex);
        }
        return n;
        
    }
    
    public int numeroVisitePeriodiche(DataOraria d) {
        int n=0;
        try {
            DB.connetti();
            String SQL = String.format("SELECT COUNT(*) "
                    + "FROM %s, %s "
                    + "WHERE `%s`.`%s` = #%s#  AND "
                    + " `%s`.`%s` = TRUE   AND "
                    + " `%s`.`%s` = `%s`.`%s` AND `%s`.`%s` = `%s`.`%s` AND `%s`.`%s` = `%s`.`%s` ",
                    modelloML.nome(),militare.nome(),//from
                    modelloML.nome(),modelloML.nomeAttributo(3),
                    d.stampaGiornoInverso(), // #data_di_oggi#
                    modelloML.nome(),modelloML.nomeAttributo(it.quasar_x7.infermeria.DaseDati.BASE_DATI.MODELLO_ML.VISITA_PERIODICA),
                    
                    modelloML.nome(), modelloML.nomeAttributo(0),  militare.nome(), militare.nomeAttributo(0),
                    modelloML.nome(), modelloML.nomeAttributo(1),  militare.nome(), militare.nomeAttributo(1),
                    modelloML.nome(), modelloML.nomeAttributo(2),  militare.nome(), militare.nomeAttributo(2)
            );
            Attributo[] dati = {
                new Attributo("count(*)",new FunzioneSQL(1),false)
            };
            n = ((Long)DB.interrogazioneSQL(SQL, dati).get(0)[0]).intValue();
            DB.chiudi();
            
            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiVerbaleVisita.class.getName()).log(Level.SEVERE, null, ex);
        }
        return n;
        
    }
    
    public int numeroInviiOsservazione(DataOraria d) {
        int n=0;
        try {
            DB.connetti();
            String SQL = String.format(//26-30
                    "SELECT COUNT(*) "
                    + "FROM %s, %s "
                    + "WHERE `%s`.`%s` = #%s#  AND "
                    + " ( `%s`.`%s` = TRUE  OR `%s`.`%s` = TRUE ) AND "
                    + " `%s`.`%s` = `%s`.`%s` AND `%s`.`%s` = `%s`.`%s` AND `%s`.`%s` = `%s`.`%s` ",
                    modelloML.nome(),militare.nome(),//from
                    modelloML.nome(),modelloML.nomeAttributo(3),
                    d.stampaGiornoInverso(), // #data_di_oggi#
                    modelloML.nome(),modelloML.nomeAttributo(27),
                    modelloML.nome(),modelloML.nomeAttributo(30),
                    modelloML.nome(), modelloML.nomeAttributo(0),  militare.nome(), militare.nomeAttributo(0),
                    modelloML.nome(), modelloML.nomeAttributo(1),  militare.nome(), militare.nomeAttributo(1),
                    modelloML.nome(), modelloML.nomeAttributo(2),  militare.nome(), militare.nomeAttributo(2)
            );
            Attributo[] dati = {
                new Attributo("count(*)",new FunzioneSQL(1),false)
            };
            n = ((Long)DB.interrogazioneSQL(SQL, dati).get(0)[0]).intValue();
            DB.chiudi();
            
            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiVerbaleVisita.class.getName()).log(Level.SEVERE, null, ex);
        }
        return n;
        
    }
    
    
    public int numeroInviiCMOeOsservazione(String reggimento, DataOraria d) {
        int n=0;
        try {
            DB.connetti();
            String SQL = String.format(//26-30
                    "SELECT COUNT(*) "
                            + "FROM %s, %s "
                            + "WHERE `%s`.`%s` = #%s#  AND "
                            + " ( `%s`.`%s` = TRUE  OR `%s`.`%s` = TRUE OR `%s`.`%s` = TRUE  OR `%s`.`%s` = TRUE ) AND "
                            + " `%s`.`%s` = `%s`.`%s` AND `%s`.`%s` = `%s`.`%s` AND `%s`.`%s` = `%s`.`%s` "
                            + "  AND  `%s`.`%s` LIKE '%s' ",
                    modelloML.nome(),militare.nome(),//from
                    
                    modelloML.nome(),modelloML.nomeAttributo(3),
                    d.stampaGiornoInverso(), // #data_di_oggi#
                    
                    modelloML.nome(),modelloML.nomeAttributo(26),
                    modelloML.nome(),modelloML.nomeAttributo(27),
                    modelloML.nome(),modelloML.nomeAttributo(29),
                    modelloML.nome(),modelloML.nomeAttributo(30),
                    
                    modelloML.nome(), modelloML.nomeAttributo(0),  militare.nome(), militare.nomeAttributo(0),
                    modelloML.nome(), modelloML.nomeAttributo(1),  militare.nome(), militare.nomeAttributo(1),
                    modelloML.nome(), modelloML.nomeAttributo(2),  militare.nome(), militare.nomeAttributo(2),
                    
                    militare.nome(),militare.nomeAttributo(6),"%"+reggimento+"%"
            );
            Attributo[] dati = {
                new Attributo("count(*)",new FunzioneSQL(1),false)
            };
            n = ((Long)DB.interrogazioneSQL(SQL, dati).get(0)[0]).intValue();
            DB.chiudi();
            
            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiVerbaleVisita.class.getName()).log(Level.SEVERE, null, ex);
        }
        return n;
        
    }

    
    public int numeroRientriMalatticaConv(String reggimento, DataOraria d) {
        int n=0;
        try {
            DB.connetti();
            String SQL = String.format(
                    "SELECT COUNT(*) "
                            + "FROM %s, %s "
                            + "WHERE `%s`.`%s` = #%s#  AND "
                            + " `%s`.`%s` = TRUE AND "
                            + " `%s`.`%s` = `%s`.`%s` AND `%s`.`%s` = `%s`.`%s` AND `%s`.`%s` = `%s`.`%s` "
                            + "  AND  `%s`.`%s` LIKE '%s' ",
                    modelloML.nome(),militare.nome(),//from
                    
                    modelloML.nome(),modelloML.nomeAttributo(3),
                    d.stampaGiornoInverso(), // #data_di_oggi#
                    
                    modelloML.nome(),modelloML.nomeAttributo(45),
                    
                    modelloML.nome(), modelloML.nomeAttributo(0),  militare.nome(), militare.nomeAttributo(0),
                    modelloML.nome(), modelloML.nomeAttributo(1),  militare.nome(), militare.nomeAttributo(1),
                    modelloML.nome(), modelloML.nomeAttributo(2),  militare.nome(), militare.nomeAttributo(2),
                    
                    militare.nome(),militare.nomeAttributo(6),"%"+reggimento+"%"
            );
            Attributo[] dati = {
                new Attributo("count(*)",new FunzioneSQL(1),false)
            };
            n = ((Long)DB.interrogazioneSQL(SQL, dati).get(0)[0]).intValue();
            DB.chiudi();
            
            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiVerbaleVisita.class.getName()).log(Level.SEVERE, null, ex);
        }
        return n;
        
    }

    
}

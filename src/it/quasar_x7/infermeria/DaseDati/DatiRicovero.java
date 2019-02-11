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
 ******************************************************************************/
public class DatiRicovero extends Dati {

//---------------------------- costruttore ------------------------------------
    /******************************************************
     * Il costruttore crea le varie entita tabelle con i
     * vari campi e domini e si connette al database
     *****************************************************/
    public DatiRicovero() {
        super();
        
    }


/********************************************************
 * Funzione che determina il numero di militari ricoverati
 * 
 * 
 * @return 
 ********************************************************/
public int numeroRicoverati(DataOraria d) throws EccezioneBaseDati {
      
    int n=0;
    
    DB.connetti();
     String SQL = String.format(
              "SELECT COUNT(*) "
            + "FROM %s "
            + "WHERE ( `%s` >= #%s 00:00:00# OR `%s` IS NULL ) AND `%s` <= #%s 23:59:59# "
           ,
            ricovero.nome(),//from
            
            ricovero.nomeAttributo(4), d.stampaGiornoInverso(),ricovero.nomeAttributo(4),
            ricovero.nomeAttributo(3), d.stampaGiornoInverso()
            
            ); 
    Attributo[] dati = {
        new Attributo("count(*)",new FunzioneSQL(1),false)
    };        
    n = ((Long)DB.interrogazioneSQL(SQL, dati).get(0)[0]).intValue(); 
    DB.chiudi();
            
        
    return n;
}

public int numeroRicoverati(DataOraria d, String reggimento) throws EccezioneBaseDati {
        
    int n=0;
    
    DB.connetti();
     String SQL = String.format(
              "SELECT COUNT(*) "
            + "FROM %s, %s "
            + "WHERE ( `%s`.`%s` >= #%s 00:00:00# OR `%s`.`%s` IS NULL ) AND `%s`.`%s` <= #%s 23:59:59# AND "
            + " `%s`.`%s` = `%s`.`%s` AND `%s`.`%s` = `%s`.`%s` AND `%s`.`%s` = `%s`.`%s` "
            + "AND  `%s`.`%s` LIKE '%s' ",
            ricovero.nome(),  militare.nome(),//from
            
            ricovero.nome(),ricovero.nomeAttributo(4), d.stampaGiornoInverso(),ricovero.nome(),ricovero.nomeAttributo(4),
            ricovero.nome(),ricovero.nomeAttributo(3), d.stampaGiornoInverso(), 
            
            ricovero.nome(), ricovero.nomeAttributo(0),  militare.nome(), militare.nomeAttributo(0),
            ricovero.nome(), ricovero.nomeAttributo(1),  militare.nome(), militare.nomeAttributo(1),
            ricovero.nome(), ricovero.nomeAttributo(2),  militare.nome(), militare.nomeAttributo(2),
             militare.nome(),militare.nomeAttributo(6),"%"+reggimento+"%"
            
            ); 
    Attributo[] dati = {
        new Attributo("count(*)",new FunzioneSQL(1),false)
    };        
    n = ((Long)DB.interrogazioneSQL(SQL, dati).get(0)[0]).intValue(); 
    DB.chiudi();
            
        
    return n;
}

public int numeroRicoverati() throws EccezioneBaseDati {
        
    int n=0;
    DataOraria d = new DataOraria();d.adesso();
    DB.connetti();
     String SQL = String.format(
              "SELECT COUNT(*) "
            + "FROM %s "
            + "WHERE [%s] >= #%s# OR [%s] IS NULL ",
            ricovero.nome(),//from
            ricovero.nomeAttributo(4),//campo [DATA_ORA uscita]
            d.stampaGiornoOraInverso(), // #data_e_ora_interessata#
            ricovero.nomeAttributo(4)//campo [DATA_ORA uscita]                              
            );
    Attributo[] dati = {
        new Attributo("count(*)",new FunzioneSQL(1),false)
    };        
    n = ((Long)DB.interrogazioneSQL(SQL, dati).get(0)[0]).intValue(); 
    DB.chiudi();
            
        
    return n;
}

    public ArrayList<Object[]> trovaRicoveriMilitare(String cognome, String nome, DataOraria nato) {
        ArrayList<Object[]> _ricovero =null;
        try {
            DB.connetti();
            _ricovero =
                    DB.interrogazioneSempliceTabella(
                    this.ricovero,
                    String.format(
                    " [cognome] = '%s' AND [nome] = '%s' "
                    + "AND [data di nascita] = #%s# ",
                    correggi(cognome), correggi(nome), nato.stampaGiornoInverso()));

            DB.chiudi();
            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiRicovero.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return _ricovero;
    }

    /**
     * 
     * @param dataIngresso
     * @param cognome
     * @param nome
     * @param dataNascita
     * @return 
     */
    public boolean eliminaRicovero(DataOraria dataIngresso,String cognome,
             String nome,DataOraria dataNascita) {
        boolean errore = false;
        try {
            
            DB.connetti();
            try {
                
               DB.eliminaTupla(
                           this.ricovero,
                            new Object[]{cognome,nome,dataNascita,dataIngresso});
               _evento.adesso();
               System.out.println(
                                _evento+
                                " -> eliminazione del record ["+ " "+
                                cognome+" "+nome+" "+dataNascita+" "+dataIngresso
                                +"...] della tabella 'ricovero'."
                                );
               
              
              
            } catch (EccezioneBaseDati ex) {
                _evento.adesso();
                System.out.println(
                                _evento+
                                " -> ERRORE nell'eliminazione del record ["+ " "+
                                cognome+" "+nome+" "+dataNascita+" "+dataIngresso
                                +"...] della tabella 'ricovero'."
                                );
                Logger.getLogger(DatiRicovero.class.getName()).log(Level.SEVERE, null, ex);
                errore = true;
            }
            
             DB.chiudi();
           
          
          
        } catch (EccezioneBaseDati ex) {
            _evento.adesso();
                System.out.println(
                                _evento+
                                " -> ERRORE connessione alla tabella 'ricovero'."
                                );
            Logger.getLogger(DatiRicovero.class.getName()).log(Level.SEVERE, null, ex);
            errore = true;
            
        }
        return !errore;
     }

    public void aggiungiRicovero(DataOraria dataIngresso,
             String cognome,String nome,DataOraria dataNascita,Object[]record){
        try {
            record[0]=cognome;
            record[1]=nome;
            record[2]=dataNascita;
            record[3]=dataIngresso;


            this.DB.connetti();
            try{
                DB.aggiungiTupla(
                        ricovero,
                        record);
                _evento.adesso();
                System.out.println(
                            _evento+
                            " -> aggiunto record ["+ " "+
                            cognome+" "+nome+" "+dataNascita+" "+dataIngresso
                            +"...] nella tabella 'ricovero'."
                            );
                
            }catch(EccezioneBaseDati e){
                JOptionPane.showMessageDialog(
                               null,
                               "Nessun valore aggiunto alla tabella "+
                               ricovero.nome()+"!",
                               "avviso",
                               JOptionPane.WARNING_MESSAGE);
                _evento.adesso();
                System.out.println(
                            _evento+
                            " -> ERRORE nell'aggiungere il record ["+ " "+
                            cognome+" "+nome+" "+dataNascita+" "+dataIngresso
                            +"...] nella tabella 'ricovero'."
                            );
                Logger.getLogger(DatiRicovero.class.getName()).log(Level.SEVERE, null, e);
                
            }
            this.DB.chiudi();
            
            
        } catch (EccezioneBaseDati ex) {
            _evento.adesso();
                System.out.println(
                            _evento+
                            " -> ERRORE connessione alla tabella 'ricovero'."
                            );
            Logger.getLogger(DatiRicovero.class.getName()).log(Level.SEVERE, null, ex);
            
        }
    }

    public Object[] trovaRicovero(DataOraria dataIngresso,String cognome,String nome,
             DataOraria dataNascita){
        Object[] x =null;
        try {
            DB.connetti();
            // campo del dati
            x = DB.vediTupla(
                    ricovero, new Object[]{
                cognome, nome, dataNascita,dataIngresso});
            DB.chiudi();
            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiRicovero.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        
        return x;
     }
    
    /**
     * Trova tutti i ricoverati in quel giorno.
     * 
     * @param data
     * @return  Lista di record composto da:
      * <ul>
      *     <li>`grado`</li>
      *     <li>`cognome`</li>
      *     <li>`nome`</li>
      *     <li>`data di nascita`</li>
      *     <li>`compagnia`</li>
      *     <li>`ingresso`</li>
      *     <li>`uscita`</li>
      *     <li>`diagnosi`</li>
      * </ul>
     */
    public ArrayList<Object[]> trovaRicoveri(DataOraria data){
        ArrayList<Object[]> ricoveri =null;
        try {
            DB.connetti();
            
            Attributo[] colonne = {
                militare.vediAttributo(BASE_DATI.MILITARE.GRADO),
                ricovero.vediAttributo(BASE_DATI.RICOVERO.COGNOME),
                ricovero.vediAttributo(BASE_DATI.RICOVERO.NOME),
                ricovero.vediAttributo(BASE_DATI.RICOVERO.DATA_NASCITA),
                militare.vediAttributo(BASE_DATI.MILITARE.COMPAGNIA),
                ricovero.vediAttributo(BASE_DATI.RICOVERO.DATA_INGRESSO),
                ricovero.vediAttributo(BASE_DATI.RICOVERO.DATA_USCITA),
                ricovero.vediAttributo(BASE_DATI.RICOVERO.DIAGNOSI)
            };
            
            String query = String.format(
                      "SELECT m.`%s`,r.`%s`,r.`%s`,r.`%s`,m.`%s`,r.`%s`,r.`%s`,r.`%s`"
                    + "FROM %s r,%s m "
                    + "WHERE  r.`cognome` = m.`cognome` AND r.`nome` = m.`nome` AND r.`data di nascita` = m.`data di nascita` AND "
                    + "( `%s` >= #%s 00:00:00# OR `%s` IS NULL ) AND `%s` <= #%s 23:59:59# ",
                    // select
                    colonne[0].nome(),//grado
                    colonne[1].nome(),//cognome
                    colonne[2].nome(),//nome
                    colonne[3].nome(),//nascita
                    colonne[4].nome(),//cp
                    colonne[5].nome(),//ingresso
                    colonne[6].nome(),//uscita
                    colonne[7].nome(),//diagnosi
                    //from
                    BASE_DATI.TABELLA.RICOVERO,BASE_DATI.TABELLA.MILITARE,
                    //where
                    colonne[6].nome(),//uscita,
                    data.stampaGiornoInverso(),
                    colonne[6].nome(),//uscita,
                    colonne[5].nome(),//ingresso
                    data.stampaGiornoInverso()
                    
                    
            );
            
            ricoveri = DB.interrogazioneSQL(query, colonne);
            DB.chiudi();
            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiRicovero.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return ricoveri;
    }
    
    /**
     * @deprecated 
     * @return 
     */
    public ArrayList<Object[]> trovaRicoveri(){
        ArrayList<Object[]> ricoveri =null;
        try {
            DataOraria oggi = new DataOraria();oggi.adesso();
             DB.connetti();
            ricoveri =
                    DB.interrogazioneSempliceTabella(
                    this.ricovero,
                    String.format(
                        "[data uscita] >= #%s# OR [data uscita] IS NULL ",
                        oggi.stampaGiornoOraInverso()));
            

            DB.chiudi();
            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiRicovero.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return ricoveri;
     }
      
      
    public int dimensioneRicovero() {
     return this.ricovero.vediTuttiAttributi().size();
    }
    
    
    public void modificaRicoveri(String cognome, String nome, DataOraria dataNascita, Object[] ricoveri) {        
        try {
            DB.connetti();
            ArrayList<Object[]> listaRicoveri = this.trovaRicoveri(cognome, nome, dataNascita);
                
                if(listaRicoveri != null)
                    if(!listaRicoveri.isEmpty())
                        for(Object[] record: listaRicoveri){
                            try {
                                DB.connetti();
                                try {
                                    DB.modificaTupla(
                                            ricovero,
                                            new Object[]{cognome,nome,dataNascita,record[3]},
                                            ricoveri);  
                                    _evento.adesso();
                                    System.out.println(
                                        _evento+
                                        " -> modifica del record ["+ " "+
                                        cognome+" "+nome+" "+dataNascita+" "+record[3]
                                        +"...] nella tabella 'ricovero'."
                                        );
                                } catch (EccezioneBaseDati ex) {
                                    _evento.adesso();
                                    System.out.println(
                                    _evento+" -> ERRORE nella modifica del record ["+ " "+
                                                    cognome+" "+nome+" "+dataNascita+
                                            "...] nella tabella 'ricovero'."
                                                    );
                                    Logger.getLogger(DatiRicovero.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                DB.chiudi();
                            } catch (EccezioneBaseDati ex) {
                                _evento.adesso();
                                    System.out.println(
                                    _evento+" -> ERRORE nella connessione alla tabella 'ricovero'."
                                                    );
                                Logger.getLogger(DatiRicovero.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                 
            
            DB.chiudi();
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiRicovero.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ArrayList<Object[]> trovaRicoveri(String cognome, String nome, DataOraria nato) {
        return this.trovaPerNominativo(cognome, nome, nato, ricovero);
        
    }

}

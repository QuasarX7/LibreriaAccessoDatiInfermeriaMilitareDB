package it.difesa.esercito.rav17.infermeria.DaseDati;


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
 * @version 2.0.1 ultima modifica 23/04/2016
 ******************************************************************************/
public class DatiVisita extends Dati {

//---------------------------- costruttore ------------------------------------
    /******************************************************
     * Il costruttore crea le varie entita tabelle con i
     * vari campi e domini e si connette al database
     *****************************************************/
    public DatiVisita() {
        super();
        
    }

     /*****************************************************
      *
      * @param dataVisita
      * @param cognome
      * @param nome
      * @param dataNascita
      * @return
      ******************************************************/
     public Object[] trovaVisita(DataOraria dataVisita,String cognome,String nome,
             DataOraria dataNascita){
         Object[] x = null;
        try {
            DB.connetti();
            // campo del dati
            x = DB.vediTupla(
                    visita, new Object[]{
                dataVisita, cognome, nome, dataNascita}
            );
            DB.chiudi();
            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiVisita.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return x;
     }

     /************************************************
      * 
      * @deprecated 
      * @param data
      * @param tipo
      * @return
      *************************************************/
     public ArrayList<Object[]> trovaVisite(DataOraria data,String tipo){        
         ArrayList<Object[]> visite = null;
         try {
            DB.connetti();
           visite = DB.interrogazioneSempliceTabella(
                   visita,
                   String.format(
                   " [%s] = '%s' AND [%s] >= #%s 00:00:00# AND " +
                   "[%s] <= #%s 23:59:59#",
                   visita.nomeAttributo(0),
                   tipo,
                   visita.nomeAttributo(1),
                   data.stampaGiornoInverso(),
                   visita.nomeAttributo(1),
                   data.stampaGiornoInverso()));

           DB.chiudi();
           
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiVisita.class.getName()).log(Level.SEVERE, null, ex);
           
        }
         return visite;
     }
     
     
     /**
      * 
      * @param data
      * @param tipo
      * @return Lista di record composto da:
      * <ul>
      *     <li>`grado`</li>
      *     <li>`cognome`</li>
      *     <li>`nome`</li>
      *     <li>`data di nascita`</li>
      *     <li>`compagnia`</li>
      *     <li>`diagnosi`</li>
      *     <li>`PML`</li>
      *     <li>`medico`</li>
      *     <li>`data (e ora)`</li>
      * </ul>
      */
    public ArrayList<Object[]> trovaVisiteMediche(DataOraria data,String tipo){  
        ArrayList<Object[]> visite = null;
         try {
            DB.connetti();
            
            Attributo[] colonne = {
                militare.vediAttributo(BASE_DATI.MILITARE.GRADO),     // grado
                visita.vediAttributo(BASE_DATI.VISITA.COGNOME),       // cognome
                visita.vediAttributo(BASE_DATI.VISITA.NOME),          // nome
                visita.vediAttributo(BASE_DATI.VISITA.DATA_NASCITA),  // data di nascita
                militare.vediAttributo(BASE_DATI.MILITARE.COMPAGNIA), // compagnia
                
                visita.vediAttributo(BASE_DATI.VISITA.DIAGNOSI),
                visita.vediAttributo(BASE_DATI.VISITA.PML),
                visita.vediAttributo(BASE_DATI.VISITA.MEDICO),
                visita.vediAttributo(BASE_DATI.VISITA.DATA), // data ora visita
            };
                    
            String query = String.format(
                      "SELECT m.`%s`, v.`%s`, v.`%s`, v.`%s`, m.`%s`, v.`%s`, v.`%s`, v.`%s`, v.`%s` "
                    + "FROM %s v, %s m "
                    + "WHERE  v.`cognome` = m.`cognome` AND v.`nome` = m.`nome` AND v.`data di nascita` = m.`data di nascita` AND "
                    + "v.`%s` >= #%s 00:00:00# AND v.`%s` <= #%s 23:59:59# AND "
                    + "v.`%s` = '%s' ",
                    colonne[0].nome(), 
                    colonne[1].nome(), 
                    colonne[2].nome(), 
                    colonne[3].nome(), 
                    colonne[4].nome(), 
                    colonne[5].nome(), 
                    colonne[6].nome(), 
                    colonne[7].nome(),
                    colonne[8].nome(),
                    BASE_DATI.TABELLA.VISITE, BASE_DATI.TABELLA.MILITARE,
                    visita.vediAttributo(BASE_DATI.VISITA.DATA).nome(),
                    data.stampaGiornoInverso(),
                    visita.vediAttributo(BASE_DATI.VISITA.DATA).nome(),
                    data.stampaGiornoInverso(),
                    visita.vediAttributo(BASE_DATI.VISITA.TIPO).nome(),
                    tipo
                    
            );
           
            
            
            visite =
                   DB.interrogazioneSQL(query, colonne);
           DB.chiudi();
           
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiVisita.class.getName()).log(Level.SEVERE, null, ex);
           
        }
         return visite;
    }

     /*******************************************************
      * Funzione che restituisce tutti i records di una tabella
     


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
            Logger.getLogger(DataVisita.class.getName()).log(Level.SEVERE, null, ex);
            registra("ERRORE "+ex.getMessage());
        }
        return null;
     }

     /*********************************************************
      * 
      * @param TIPO
      * @param dataVisita
      * @param vaccino
      * @param cognome
      * @param nome
      * @param dataNascita
      * @param vaccinato
      **********************************************************/
     public void aggiungiVisita(String TIPO,DataOraria dataVisita,
             String cognome,String nome,DataOraria dataNascita,Object[]record){
        try {
            record[0]=TIPO;
            record[1]=dataVisita;
            record[2]=cognome;
            record[3]=nome;
            record[4]=dataNascita;

            
            this.DB.connetti();
            try{
                    DB.aggiungiTupla(
                        visita,
                        record);
                    _evento.adesso();
                    System.out.println(
                        _evento+
                        " -> aggiunto record ["+ " "+
                        cognome+" "+nome+" "+dataNascita+" "+TIPO+" "+dataVisita
                        +"...] alla tabella '"+visita.nome()+"'."
                        );
            }catch(EccezioneBaseDati e){
                _evento.adesso();
                System.out.println(
                        _evento+
                        " -> ERRORE nell'aggiungere il record ["+ " "+
                        cognome+" "+nome+" "+dataNascita+" "+TIPO+" "+dataVisita
                        +"...] alla tabella '"+visita.nome()+"'."
                        );
                JOptionPane.showMessageDialog(
                               null,
                               "Nessun valore aggiunto alla tabella "+
                               visita.nome()+"!",
                               "avviso",
                               JOptionPane.WARNING_MESSAGE);
                Logger.getLogger(DatiVisita.class.getName()).log(Level.SEVERE, null, e);
                
            }
            this.DB.chiudi();
            
                
        } catch (EccezioneBaseDati ex) {
            _evento.adesso();
            System.out.println(
                        _evento+
                        " -> ERRORE connessione alla tabella '"+visita.nome()+"'."
                        );
            Logger.getLogger(DatiVisita.class.getName()).log(Level.SEVERE, null, ex);
            
        }
     }

     /*****************************************************
      * 
      * @param dataVisita
      * @param cognome
      * @param nome
      * @param dataNascita
     * @return 
      *****************************************************/
     public boolean eliminaVisita(DataOraria dataVisita,String cognome,
             String nome,DataOraria dataNascita) {
         boolean errore = false;
        try {
            DB.connetti();
             try {
                
               DB.eliminaTupla(
                           this.visita,
                            new Object[]{dataVisita,cognome,nome,dataNascita}
               );
               _evento.adesso();
                        System.out.println(
                            _evento+
                            " -> eliminato record ["+ " "+
                            cognome+" "+nome+" "+dataNascita+" "+dataVisita
                            +"...] dalla tabella '"+visita.nome()+"'."
                            );
           
               
            } catch (EccezioneBaseDati ex) {
                _evento.adesso();
                System.out.println(
                            _evento+
                            " -> ERRORE nell'eliminazione del record ["+ " "+
                            cognome+" "+nome+" "+dataNascita+" "+dataVisita
                            +"...] dalla tabella '"+visita.nome()+"'."
                            );
                Logger.getLogger(DatiVisita.class.getName()).log(Level.SEVERE, null, ex);
                errore = true;
            }
            DB.chiudi();
            
        } catch (EccezioneBaseDati ex) {
            _evento.adesso();
                System.out.println(
                            _evento+
                            " -> ERRORE connessione alla tabella '"+visita.nome()+"'."
                            );
            Logger.getLogger(DatiVisita.class.getName()).log(Level.SEVERE, null, ex);
            errore = true;
        }
        return !errore;
     }

 
    



    public ArrayList<Object[]> trovaVisiteMilitare(String cognome, String nome, DataOraria nato) {
        ArrayList<Object[]> visite =null;
        try {
            DB.connetti();
            visite =
                    DB.interrogazioneSempliceTabella(
                    this.visita,
                    String.format(
                    " [cognome] = '%s' AND [nome] = '%s' "
                    + "AND [data di nascita] = #%s# ",
                    correggi(cognome), correggi(nome), nato.stampaGiornoInverso()));
                   
            DB.chiudi();
            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiVisita.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return visite;
    }

    /*********************************************
     * restituisce il numero di visite mediche
     * fatte (compreso quelle di pronto soccorso)
     *
     * @return intero
     *********************************************/
    public int numeroVisite() {
        int n=0;
        try {
            DataOraria d = new DataOraria(); d.oggi(); 
            DB.connetti();
             String SQL = String.format(
                      "SELECT COUNT(*) "
                    + "FROM %s "
                    + "WHERE [%s] >= #%s 00:00:00# AND [%s] <= #%s 23:59:59#",
                    visita.nome(),//from
                    visita.nomeAttributo(1),//campo [visita]
                    d.stampaGiornoInverso(), // #data_di_oggi#
                    visita.nomeAttributo(1),//campo [visita]
                    d.stampaGiornoInverso() // #data_di_oggi#                
                    );
            Attributo[] dati = {
                new Attributo("count(*)",new FunzioneSQL(1),false)
            };        
            n = ((Long)DB.interrogazioneSQL(SQL, dati).get(0)[0]).intValue(); 
            DB.chiudi();
           
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiVisita.class.getName()).log(Level.SEVERE, null, ex);
            
        }
         return n;
    }
    
    
    

public int numeroVisite(String tipoVisita,DataOraria d) throws EccezioneBaseDati {
     
    int n=0;
    
            
    DB.connetti();
     String SQL = String.format(
              "SELECT COUNT(*) "
            + "FROM %s "
            + "WHERE [%s] >= #%s 00:00:00# AND [%s] <= #%s 23:59:59#"
            + " AND tipo = '%s' ",
            visita.nome(),//from
            visita.nomeAttributo(1),//campo [visita]
            d.stampaGiornoInverso(), // #data_di_oggi#
            visita.nomeAttributo(1),//campo [visita]
            d.stampaGiornoInverso(), // #data_di_oggi#   
             tipoVisita
            );
    Attributo[] dati = {
        new Attributo("count(*)",new FunzioneSQL(1),false)
    };        
    n = ((Long)DB.interrogazioneSQL(SQL, dati).get(0)[0]).intValue(); 
    DB.chiudi();

        
        return n;
    }

public int numeroVisite(String tipoVisita,String reggimento,DataOraria d) throws EccezioneBaseDati {
     
    int n=0;
    
            
    DB.connetti();
     String SQL = String.format(
              "SELECT COUNT(*) "
            + "FROM %s, %s "
            + "WHERE `%s`.`%s` >= #%s 00:00:00# AND `%s`.`%s` <= #%s 23:59:59# AND "
            + " `%s`.`%s` = `%s`.`%s` AND `%s`.`%s` = `%s`.`%s` AND `%s`.`%s` = `%s`.`%s` "
            + " AND `%s`.tipo = '%s' AND  `%s`.`%s` LIKE '%s' ",
            visita.nome(),militare.nome(),//from
            
            visita.nome(),visita.nomeAttributo(1),//campo [visita]
            d.stampaGiornoInverso(), // #data_di_oggi#
            visita.nome(),visita.nomeAttributo(1),//campo [visita]
            d.stampaGiornoInverso(), // #data_di_oggi#  
            
            visita.nome(), visita.nomeAttributo(2),  militare.nome(), militare.nomeAttributo(0),
            visita.nome(), visita.nomeAttributo(3),  militare.nome(), militare.nomeAttributo(1),
            visita.nome(), visita.nomeAttributo(4),  militare.nome(), militare.nomeAttributo(2),
            
            visita.nome(), tipoVisita,  militare.nome(),militare.nomeAttributo(6),"%"+reggimento+"%"
            );
    Attributo[] dati = {
        new Attributo("count(*)",new FunzioneSQL(1),false)
    };        
    n = ((Long)DB.interrogazioneSQL(SQL, dati).get(0)[0]).intValue(); 
    DB.chiudi();

        
        return n;
    }

public int numeroRicoveratiOspedale(String reggimento,DataOraria d) throws EccezioneBaseDati {
     
    int n=0;
    
            
    DB.connetti();
     String SQL = String.format(
              "SELECT COUNT(*) "
            + "FROM %s, %s "
            + "WHERE ( `%s`.`%s` <> '' OR `%s`.`%s` IS NOT NULL ) AND "
            + "`%s`.`%s` >= #%s 00:00:00# AND `%s`.`%s` <= #%s 23:59:59# AND "
            + " `%s`.`%s` = `%s`.`%s` AND `%s`.`%s` = `%s`.`%s` AND `%s`.`%s` = `%s`.`%s` "
            + "  AND  `%s`.`%s` LIKE '%s' ",
            visita.nome(),militare.nome(),//from
            
            visita.nome(),visita.nomeAttributo(14),visita.nome(),visita.nomeAttributo(14),
            
            visita.nome(),visita.nomeAttributo(1),//campo [visita]
            d.stampaGiornoInverso(), // #data_di_oggi#
            visita.nome(),visita.nomeAttributo(1),//campo [visita]
            d.stampaGiornoInverso(), // #data_di_oggi#  
            
            visita.nome(), visita.nomeAttributo(2),  militare.nome(), militare.nomeAttributo(0),
            visita.nome(), visita.nomeAttributo(3),  militare.nome(), militare.nomeAttributo(1),
            visita.nome(), visita.nomeAttributo(4),  militare.nome(), militare.nomeAttributo(2),
            
            militare.nome(),militare.nomeAttributo(6),"%"+reggimento+"%"
            );
    Attributo[] dati = {
        new Attributo("count(*)",new FunzioneSQL(1),false)
    };        
    n = ((Long)DB.interrogazioneSQL(SQL, dati).get(0)[0]).intValue(); 
    DB.chiudi();

        
        return n;
    }

     
      public int dimensioneVisita() {
        return this.visita.vediTuttiAttributi().size();
    }

    public void modificaVisitaMedica(String cognome, String nome, DataOraria dataNascita, Object[] visita) {        
        try {
            DB.connetti();
            
            ArrayList<Object[]> listaVisite = this.trovaVisiteMediche(cognome, nome, dataNascita);
                
                if(listaVisite != null)
                    if(!listaVisite.isEmpty())
                        for(Object[] record: listaVisite){ 
                            try {
                                DB.connetti();
                                try {
                                    DB.modificaTupla(
                                            this.visita,
                                            new Object[]{record[1],cognome,nome,dataNascita},
                                            visita);
                                    _evento.adesso();
                                    System.out.println(
                                        _evento+
                                        " -> modifica del record ["+ " "+
                                        cognome+" "+nome+" "+dataNascita+" "+record[1] 
                                        +"...] dalla tabella '"+this.visita.nome()+"'."
                                        );
                                    } catch (EccezioneBaseDati ex) {
                                        _evento.adesso();
                                        System.out.println(
                                            _evento+
                                            " -> ERRORE nella modifica del record ["+ " "+
                                            cognome+" "+nome+" "+dataNascita+"...] dalla tabella '"+this.visita.nome()+"'."
                                            );  
                                        Logger.getLogger(DatiVisita.class.getName()).log(Level.SEVERE, null, ex);

                                    }   
                                    DB.chiudi();
                            } catch (EccezioneBaseDati ex) {
                                _evento.adesso();
                                    System.out.println(
                                        _evento+
                                        " -> ERRORE  connessione alla tabella '"+this.visita.nome()+"'."
                                        ); 
                                Logger.getLogger(DatiVisita.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        }
                 
            
            DB.chiudi();
        } catch (EccezioneBaseDati ex) { 
            Logger.getLogger(DatiVisita.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ArrayList<Object[]> trovaVisiteMediche(String cognome, String nome, DataOraria nato) {
        return this.trovaPerNominativo(cognome, nome, nato, visita);
        
    }

    public ArrayList<Object[]> situazioneMedici(DataOraria inizio, DataOraria fine) {
        ArrayList<Object[]> conteggioVisite=null;
        try {
            String tabella = visita.nome();
            String _medico = visita.nomeAttributo(15);
            String _tipo_visita = visita.nomeAttributo(0);
            String _data = visita.nomeAttributo(1);
            
            DB.connetti();
            String query =String.format(
                      "SELECT `%s`, `%s`,count(*) "
                    + "FROM `%s` "
                    + "WHERE `%s` >= #%s 00:00:00# AND `%s` <= #%s 23:59:59# "
                    + "GROUP BY  `%s`, `%s` "
                    + "ORDER BY  `%s`, `%s` ",
                    //SELECT
                    _medico,
                    _tipo_visita,
                    //FROM
                    tabella,
                    //condizione WHERE
                    _data,
                    inizio.stampaGiornoInverso(),
                    _data,
                    fine.stampaGiornoInverso(),
                    //GROUP BY
                    _medico,
                    _tipo_visita,
                    //ORDER BY
                    _medico,
                    _tipo_visita
                    );
            
           Attributo[] colonne= {
               new Attributo(_medico,STRINGA_50,false),
               new Attributo(_tipo_visita,STRINGA_50,false),
               new Attributo("count(*)",new FunzioneSQL(3),false),
           };
           conteggioVisite = DB.interrogazioneSQL(query, colonne);
            DB.chiudi();
                 
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiVisita.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return conteggioVisite;
    }

    public ArrayList<Object[]> situazioneOperatore(DataOraria inizio, DataOraria fine) {
        ArrayList<Object[]> conteggioVisite=null;
        try {
            String tabella = visita.nome();
            String _operatore = visita.nomeAttributo(14);
            String _tipo_visita = visita.nomeAttributo(0);
            String _data = visita.nomeAttributo(1);
            
            DB.connetti();
            String query =String.format(
                      "SELECT `%s`, `%s`,count(*) "
                    + "FROM `%s` "
                    + "WHERE `%s` >= #%s 00:00:00# AND `%s` <= #%s 23:59:59# "
                    + "GROUP BY  `%s`, `%s` "
                    + "ORDER BY  `%s`, `%s` ",
                    //SELECT
                    _operatore,
                    _tipo_visita,
                    //FROM
                    tabella,
                    //condizione WHERE
                    _data,
                    inizio.stampaGiornoInverso(),
                    _data,
                    fine.stampaGiornoInverso(),
                    //GROUP BY
                    _operatore,
                    _tipo_visita,
                    //ORDER BY
                    _operatore,
                    _tipo_visita
                    );
            
           Attributo[] colonne= {
               new Attributo(_operatore,STRINGA_50,false),
               new Attributo(_tipo_visita,STRINGA_50,false),
               new Attributo("count(*)",new FunzioneSQL(3),false),
           };
           conteggioVisite = DB.interrogazioneSQL(query, colonne);
            DB.chiudi();
                 
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiVisita.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return conteggioVisite;
    }

    public ArrayList<Object[]> situazioneVisitaRicoveri(DataOraria inizio, DataOraria fine) {
        ArrayList<Object[]> conteggioVisite=null;
        try {
            String tabella = visita.nome();
            String _ricovero = visita.nomeAttributo(13);
            String _tipo_visita = visita.nomeAttributo(0);
            String _data = visita.nomeAttributo(1);
            
            DB.connetti();
            String query =String.format(
                      "SELECT  `%s`,count(*) "
                    + "FROM `%s` "
                    + "WHERE `%s` >= #%s 00:00:00# AND `%s` <= #%s 23:59:59# AND `%s` = TRUE "
                    + "GROUP BY  `%s` "
                    + "ORDER BY  `%s` ",
                    //SELECT
                    _tipo_visita,
                    //FROM
                    tabella,
                    //condizione WHERE
                    _data,
                    inizio.stampaGiornoInverso(),
                    _data,
                    fine.stampaGiornoInverso(),
                    _ricovero,
                    //GROUP BY
                    _tipo_visita,
                    //ORDER BY
                    _tipo_visita
                    );
            
           Attributo[] colonne= {
               new Attributo(_tipo_visita,STRINGA_50,false),
               new Attributo("count(*)",new FunzioneSQL(2),false),
           };
           conteggioVisite = DB.interrogazioneSQL(query, colonne);
            DB.chiudi();
                 
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiVisita.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return conteggioVisite;
    }
    
    /**
     * Metodo che ci permette di conoscere il numero di DLT (dichiarazione di lesioni traumatiche)
     * relative a un dato giorno.
     * 
     * @param data
     * @return numero di DLT
     */
    public int numeroVisitaDLT(DataOraria data) {
        ArrayList<Object[]> conteggioVisite=null;
        try {
            
            DB.connetti();
            String query =String.format("SELECT  count(*) "
                    + "FROM `%s` "
                    + "WHERE `%s` >= #%s 00:00:00# AND `%s` <= #%s 23:59:59# AND `%s` = TRUE ",
                    //FROM
                    visita.nome(),
                    //condizione WHERE
                    visita.nomeAttributo(BASE_DATI.VISITA.DATA),
                    data.stampaGiornoInverso(),
                    visita.nomeAttributo(BASE_DATI.VISITA.DATA),
                    data.stampaGiornoInverso(),
                    visita.nomeAttributo(BASE_DATI.VISITA.DLT)
                    
                    );
            
           Attributo[] colonne= {
               new Attributo("count(*)",new FunzioneSQL(1),false)
           };
           conteggioVisite = DB.interrogazioneSQL(query, colonne);
            DB.chiudi();
                 
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiVisita.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return conteggioVisite != null ? ((Long)conteggioVisite.get(0)[0]).intValue() : 0;
    }
    
    /**
     * 
     * @param inizio
     * @param fine
     * @return 
     */
    public ArrayList<Object[]> situazioneVisitaDLT(DataOraria inizio, DataOraria fine) {
        ArrayList<Object[]> conteggioVisite=null;
        try {
            String tabella = visita.nome();
            String _DLT = visita.nomeAttributo(12);
            String _tipo_visita = visita.nomeAttributo(0);
            String _data = visita.nomeAttributo(1);
            
            DB.connetti();
            String query =String.format(
                      "SELECT  `%s`,count(*) "
                    + "FROM `%s` "
                    + "WHERE `%s` >= #%s 00:00:00# AND `%s` <= #%s 23:59:59# AND `%s` = TRUE "
                    + "GROUP BY  `%s` "
                    + "ORDER BY  `%s` ",
                    //SELECT
                    _tipo_visita,
                    //FROM
                    tabella,
                    //condizione WHERE
                    _data,
                    inizio.stampaGiornoInverso(),
                    _data,
                    fine.stampaGiornoInverso(),
                    _DLT,
                    //GROUP BY
                    _tipo_visita,
                    //ORDER BY
                    _tipo_visita
                    );
            
           Attributo[] colonne= {
               new Attributo(_tipo_visita,STRINGA_50,false),
               new Attributo("count(*)",new FunzioneSQL(2),false),
           };
           conteggioVisite = DB.interrogazioneSQL(query, colonne);
            DB.chiudi();
                 
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiVisita.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return conteggioVisite;
    }
    
    
    public ArrayList<Object[]> situazioneVisitaOspedale(DataOraria inizio, DataOraria fine) {
        ArrayList<Object[]> conteggioVisite=null;
        try {
            String tabella = visita.nome();
            String _ospedale = visita.nomeAttributo(10);
            String _tipo_visita = visita.nomeAttributo(0);
            String _data = visita.nomeAttributo(1);
            
            DB.connetti();
            String query =String.format(
                      "SELECT  `%s`,count(*) "
                    + "FROM `%s` "
                    + "WHERE `%s` >= #%s 00:00:00# AND `%s` <= #%s 23:59:59# AND `%s` <> '' "
                    + "GROUP BY  `%s` "
                    + "ORDER BY  `%s` ",
                    //SELECT
                    _tipo_visita,
                    //FROM
                    tabella,
                    //condizione WHERE
                    _data,
                    inizio.stampaGiornoInverso(),
                    _data,
                    fine.stampaGiornoInverso(),
                    _ospedale,
                    //GROUP BY
                    _tipo_visita,
                    //ORDER BY
                    _tipo_visita
                    );
            
           Attributo[] colonne= {
               new Attributo(_tipo_visita,STRINGA_50,false),
               new Attributo("count(*)",new FunzioneSQL(2),false),
           };
           conteggioVisite = DB.interrogazioneSQL(query, colonne);
            DB.chiudi();
                 
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiVisita.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return conteggioVisite;
    }
}

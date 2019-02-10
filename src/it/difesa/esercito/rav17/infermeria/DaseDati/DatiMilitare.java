package it.difesa.esercito.rav17.infermeria.DaseDati;


import it.quasar_x7.java.BaseDati.Attributo;
import it.quasar_x7.java.BaseDati.EccezioneBaseDati;
import it.quasar_x7.java.BaseDati.FunzioneSQL;
import it.quasar_x7.java.utile.DataOraria;
import it.quasar_x7.java.utile.Errore;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


/*******************************************************************************
 *Implementa la struttura della base di dati infermeria MySQL
 *
 * @author Domenico della Peruta
 ******************************************************************************/
public class DatiMilitare extends Dati {
    
    class GML{
        public String giudizio=null;
        public String note=null; 
        
        static final String SEPARATORE = "\n\r";
        
        public GML(String testo){
            if(testo != null){
                giudizio="";
                note="";
                int id = testo.indexOf(SEPARATORE);
                if(id > 0){
                    if(id <= testo.length()){
                        giudizio = testo.substring(0,id);
                    }
                    if(id+2 < testo.length()){
                        note = testo.substring(id+2);
                    }
                }else{
                    note = testo;
                }
                 
            }
        }
    }

//---------------------------- costruttore ------------------------------------
    /******************************************************
     * Il costruttore crea le varie entita tabelle con i
     * vari campi e domini e si connette al database
     *****************************************************/
    public DatiMilitare() {
        super();
    }


     /***************************************************
      * Trova i vari attributi che compongono il dati
      * della tabella Militare relativa alle vaccinazioni
      * già fatte
      *
      * @param cognome
      * @param nome
      * @param dataNascita
      * @return un oggetto generico rappresentante il dato
      ***************************************************/
     public Object[] trovaMilitare(String cognome,String nome,DataOraria dataNascita){
         return this.trova(cognome, nome, dataNascita, militare);
     }
     


     /***************************************************
      * Trova i records della tabella militare
      *
      * @param cognome
      * @param corso
      * @return lista di reazioniVaccOggi o null
      ***************************************************/
     public ArrayList<Object[]> trovaMilitare(String cognome,String corso){        
       
         ArrayList<Object[]> x = null;
         try {
            DB.connetti();        
            x = DB.interrogazioneSempliceTabella(
                    militare,
                    String.format(
                        " [%s] = '%s' AND [%s] = '%s' ",
                        militare.nomeAttributo(0),
                        correggi(cognome),
                        militare.nomeAttributo(24),
                        corso)
                    );
            
            DB.chiudi();
            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiMilitare.class.getName()).log(Level.SEVERE, null, ex);
            
        }
         return x;
     }



    /*******************************************************
     * Elenco delle corsi presenti
     *
     * @return
     *******************************************************/
     public Object[] compagniePresenti(){        
         Object[] compagnie = null;
         try {
            DB.connetti();
            String query = String.format(
                      "SELECT DISTINCT `%s` "
                    + "FROM %s "
                    + "ORDER BY `%s`",
                    militare.nomeAttributo(6),//compagnia
                    militare.nome(),
                    militare.nomeAttributo(6));
            
           Attributo[] colonna = {
               new Attributo("compagnia",STRINGA_50,false)
           };
           ArrayList<Object[]> cp = DB.interrogazioneSQL(query, colonna);
           DB.chiudi();
           compagnie = new Object[cp.size()];
           int i=0;
           for(Object[] record: cp){
               compagnie[i++]=record[0];
           }
           
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiMilitare.class.getName()).log(Level.SEVERE, null, ex);
            
        }
         return compagnie;
         
     }
     
     /**
      * Metodo che restituisce l'elenco delle compagnie di un determinato corso.
      * 
      * @param corso
      * @return 
      */
     public Object[] compagniePresenti(String corso){        
         Object[] compagnie = null;
         try {
            DB.connetti();
            String query = String.format("SELECT DISTINCT `%s` "
                    + "FROM %s "
                    + "WHERE `%s` = '%s' "
                    + "ORDER BY `%s`",
                    //SELECT
                    militare.nomeAttributo(it.difesa.esercito.rav17.infermeria.DaseDati.BASE_DATI.MILITARE.COMPAGNIA),
                    //FROM
                    militare.nome(),
                    //WHERE
                    militare.nomeAttributo(it.difesa.esercito.rav17.infermeria.DaseDati.BASE_DATI.MILITARE.CORSO),corso,
                    //OLDER BY
                    militare.nomeAttributo(it.difesa.esercito.rav17.infermeria.DaseDati.BASE_DATI.MILITARE.COMPAGNIA));
            
           Attributo[] colonna = {
               new Attributo("compagnia",STRINGA_50,false)
           };
           ArrayList<Object[]> cp = DB.interrogazioneSQL(query, colonna);
           DB.chiudi();
           compagnie = new Object[cp.size()];
           int i=0;
           for(Object[] record: cp){
               compagnie[i++]=record[0];
           }
           
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiMilitare.class.getName()).log(Level.SEVERE, null, ex);
            
        }
         return compagnie;
         
     }

     
     
     /*****************************************************
      * Elenco dei corsi presenti nella base dati
      *
      * @return
      *****************************************************/
     public Object[] corsiPresenti(){
         Object[] corsi = null;
        try {
            DB.connetti();
            String query = String.format(
                      "SELECT DISTINCT `%s` "
                    + "FROM %s "
                    + "ORDER BY `%s`",
                    militare.nomeAttributo(24),//corso
                    militare.nome(),
                    militare.nomeAttributo(24));
            
           Attributo[] colonna = {
               new Attributo("corso",STRINGA_50,false)
           };
           ArrayList<Object[]> bl = DB.interrogazioneSQL(query, colonna);
           DB.chiudi();
           if(bl != null) {
	           corsi = new Object[bl.size()];
	           int i=0;
	           for(Object[] record: bl){
	               corsi[i++]=record[0];
	           }
	
	           return corsi;
           }
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiMilitare.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return null;
     }

     
     /***************************************************
      * Trova tutti i dati della tabella Militare
      *
      * @return un oggetto generico rappresentante il dato
      ***************************************************/
     public ArrayList<Object[]> tuttiMilitari(){
         return tuttiMilitariSimili("");
     }
     
    /**
     * 
     * @param cognome
     * @return 
     */
    public ArrayList<Object[]> tuttiMilitariSimili(String cognome){
         return tuttiMilitariSimili(cognome, "");
    }

    /**
     * 
     * @param cognome
     * @param corso
     * @return 
     */
    public ArrayList<Object[]> tuttiMilitariSimili(String cognome, String corso){
         ArrayList<Object[]> x =null;
        try {
            if(corso == null)corso="";
            String interogazioneCorso = (corso.length() > 0) ? militare.nomeAttributo(24)+" = '"+corso+"' AND " :"";
            DB.connetti();
           x =DB.interrogazioneSempliceTabella(
                   militare, 
                   String.format(
                           " %s `%s` LIKE '%s%s' ORDER BY `%s`, `%s` LIMIT 1000",
                           interogazioneCorso,
                           militare.nomeAttributo(0),
                           correggi(cognome),
                           "%",
                           militare.nomeAttributo(0),
                           militare.nomeAttributo(1)
                   )
           );
            DB.chiudi();
            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiMilitare.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return x;
     }

     /**************************************************
      * Funzione che restituisce una lista 
      * di militari appartenenti a un determinato corso
      *
      * @param corso
      * @return
      **************************************************/
     public ArrayList<Object[]> tuttiMilitari(String corso){
        
         ArrayList<Object[]> x =null;
         try {
            DB.connetti();
           x =
                   DB.interrogazioneSempliceTabella(
                   militare,
                   String.format(
                       " [%s] = '%s' " +
                       "ORDER BY [%s]",
                       militare.nomeAttributo(24),
                       corso,
                       militare.nomeAttributo(0)));
            DB.chiudi();

            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiMilitare.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return x;
     }
     
     /**************************************************
      * Funzione che restituisce una lista 
      * di militari appartenenti a un determinato corso
      * e compagnia
      *
      * @param corso
      * @param compagnia
      * @return
      **************************************************/
     public ArrayList<Object[]> tuttiMilitari(String corso,String compagnia){
        
         ArrayList<Object[]> x =null;
         try {
            DB.connetti();
           x =
                   DB.interrogazioneSempliceTabella(
                   militare,
                   String.format(
                       " [%s] = '%s' AND " +
                       " [%s] = '%s' " +
                       "ORDER BY [%s]",
                       militare.nomeAttributo(24),
                       corso,
                       militare.nomeAttributo(6),
                       compagnia,
                       militare.nomeAttributo(0)));
            DB.chiudi();

            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiMilitare.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return x;
     }

     
     public ArrayList<Object[]> situazioneIncorporamento(DataOraria data){
        
          ArrayList<Object[]> attivitaIncorporamento =null;
         try {
            this.DB.connetti();
           String sql = String.format(
                   " [%s] = #%s# "
                   + " GROUP BY [%s]", 
                   militare.nomeAttributo(22),//data GML
                   data.stampaGiornoInverso(),
                   militare.nomeAttributo(6)//compagnia
                   );
           
            attivitaIncorporamento =
                   DB.interrogazioneSempliceTabella(
                   militare,
                   new Attributo[]{                       
                        new Attributo("compagnia",STRINGA_40,false),
                        new Attributo("count(*)",new FunzioneSQL(2),false)
                    },
                   sql);
            this.DB.chiudi();

            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiMilitare.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return attivitaIncorporamento;

     }

     /**********************************************
      * Restituisce l'elenco dei militari di un corso
      * giudicati idonei
      * 
      * @param corso
      * @return
      **********************************************/
     public ArrayList<Object[]> situazioneIncorporamentoIdoneo(String corso){
        
          ArrayList<Object[]> attivitaIncorporamento =null;
         try {
            this.DB.connetti();
           String sql = String.format(
                   "[%s] = '%s' AND [%s] = 'idoneo' "
                   + " GROUP BY [%s], [%s]",
                   militare.nomeAttributo(24),//corso                
                   corso,
                   militare.nomeAttributo(23),//GML
                   militare.nomeAttributo(22),//data GML
                   militare.nomeAttributo(6)//compagnia
                   );
           
            attivitaIncorporamento =
                   DB.interrogazioneSempliceTabella(
                   militare,
                   new Attributo[]{
                       new Attributo("data GML",DATA_ORA,false),
                        new Attributo("compagnia",STRINGA_40,false),
                        new Attributo("count(*)",new FunzioneSQL(3),false)
                    },
                   sql);
            this.DB.chiudi();
            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiMilitare.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return attivitaIncorporamento;

     }
     
     /*******************************************
      * Tutti i militari di un corso visitati
      * 
      * @param corso
      * @return 
      *******************************************/
     public ArrayList<Object[]> situazioneIncorporamento(String corso){
        
          ArrayList<Object[]> attivitaIncorporamento =null;
         try {
            this.DB.connetti();
           String sql = String.format(
                   "[%s] = '%s' AND [%s] IS NOT NULL "
                   + " GROUP BY [%s], [%s]",
                   militare.nomeAttributo(24),//corso                
                   corso,
                   militare.nomeAttributo(23),//GML
                   militare.nomeAttributo(22),//data GML
                   militare.nomeAttributo(6)//compagnia
                   );
           
            attivitaIncorporamento =
                   DB.interrogazioneSempliceTabella(
                   militare,
                   new Attributo[]{
                       new Attributo("data GML",DATA_ORA,false),
                        new Attributo("compagnia",STRINGA_40,false),
                        new Attributo("count(*)",new FunzioneSQL(3),false)
                    },
                   sql);
            this.DB.chiudi();

            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiMilitare.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return attivitaIncorporamento;

     }
     
     /****************************************
      * 
      * @param corso
      * @return 
      ***************************************/
     public ArrayList<Object[]> situazioneIncorporamentoNoIdoneo(String corso){
        
          ArrayList<Object[]> attivitaIncorporamento =null;
         try {
            this.DB.connetti();
           String sql = String.format(
                   "[%s] = '%s' AND [%s] <> 'idoneo' "
                   + " GROUP BY [%s], [%s]",
                   militare.nomeAttributo(24),//corso                
                   corso,
                   militare.nomeAttributo(23),//GML
                   militare.nomeAttributo(22),//data GML
                   militare.nomeAttributo(6)//compagnia
                   );
           
            attivitaIncorporamento =
                   DB.interrogazioneSempliceTabella(
                   militare,
                   new Attributo[]{
                       new Attributo("data GML",DATA_ORA,false),
                        new Attributo("compagnia",STRINGA_40,false),
                        new Attributo("count(*)",new FunzioneSQL(3),false)
                    },
                   sql);
            this.DB.chiudi();

            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiMilitare.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return attivitaIncorporamento;

     }

     /**************************************************
      * Determina il numero di militari giudicati idonei
      * alla visita d'incorporamento.
      *
      * @param d
      * @return
      * @throws it.quasar_x7.java.BaseDati.EccezioneBaseDati
      **************************************************/
     public int numeroIdonieta(DataOraria d) throws EccezioneBaseDati{
        int n=0;
        
            DB.connetti();
            String SQL = String.format(
                      "SELECT COUNT(*) "
                    + "FROM %s "
                    + "WHERE [%s] = #%s# AND [%s] = 'IDONEO' ",
                    militare.nome(),//from
                    militare.nomeAttributo(22),//campo [DATA_ORA GML]
                    d.stampaGiornoInverso(), // #data#
                    militare.nomeAttributo(23)//campo [GML]
                    );
            Attributo[] dati = {
                new Attributo("count(*)",new FunzioneSQL(1),false)
            };
            n = ((Long)DB.interrogazioneSQL(SQL, dati).get(0)[0]).intValue();
            DB.connetti();
        
        return n;
        
     }
     
     /**
      * Metodo che determina il numero di visite di incorporamento.
      * 
      * @param giorno data della visita
      * @return
      * @throws EccezioneBaseDati 
      */
     public int numeroVisiteIncorporati(DataOraria giorno) throws EccezioneBaseDati{
        int n=0;
        
            DB.connetti();
            String SQL = String.format("SELECT COUNT(*) "
                    + "FROM `%s` "
                    + "WHERE `%s` = '%s' AND `%s` IS NOT NULL AND `%s` <> '' ",
                    militare.nome(),//from
                    militare.nomeAttributo(it.difesa.esercito.rav17.infermeria.DaseDati.BASE_DATI.MILITARE.DATA_GML),
                    giorno.stampaGiornoInverso(),
                    militare.nomeAttributo(it.difesa.esercito.rav17.infermeria.DaseDati.BASE_DATI.MILITARE.GML),
                    militare.nomeAttributo(it.difesa.esercito.rav17.infermeria.DaseDati.BASE_DATI.MILITARE.GML)
                    );
            Attributo[] dati = {
                new Attributo("count(*)",new FunzioneSQL(1),false)
            };
            n = ((Long)DB.interrogazioneSQL(SQL, dati).get(0)[0]).intValue();
            DB.connetti();
        
        return n;
        
     }
     
      public int numeroVisiteIncorporo(String reggimento,DataOraria d) throws EccezioneBaseDati{
        int n=0;
        
            DB.connetti();
            String SQL = String.format(
                      "SELECT COUNT(*) "
                    + "FROM %s "
                    + "WHERE [%s] = #%s# AND ( [%s] <> '' OR [%s] IS NOT NULL ) "
                    + "AND `%s` LIKE '%s' ",
                    militare.nome(),//from
                    militare.nomeAttributo(22),//campo [DATA_ORA GML]
                    d.stampaGiornoInverso(), // #data#
                    militare.nomeAttributo(23),militare.nomeAttributo(23),//campo [GML]
                    militare.nomeAttributo(6),"%"+reggimento+"%"
                    );
            Attributo[] dati = {
                new Attributo("count(*)",new FunzioneSQL(1),false)
            };
            n = ((Long)DB.interrogazioneSQL(SQL, dati).get(0)[0]).intValue();
            DB.connetti();
        
        return n;
        
     }
     
     
     /*****************************************************
      * numero di corsisti 
      * 
      * @param corso
      * @param cp compagnia/reggimento
      * @return 
      *****************************************************/
     public int numeroCorso(String corso,String cp){        
        int n=0;
        try {
            DB.connetti();
            String SQL = String.format(
                      "SELECT COUNT(*) "
                    + "FROM %s "
                    + "WHERE [%s] = '%s' AND [%s] = '%s' ",
                    militare.nome(),//from
                    militare.nomeAttributo(24),//corso
                    corso,
                    militare.nomeAttributo(6),
                    cp
                    );
            Attributo[] dati = {
                new Attributo("count(*)",new FunzioneSQL(1),false)
            };
            n = ((Long)DB.interrogazioneSQL(SQL, dati).get(0)[0]).intValue();
            DB.connetti();
            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiMilitare.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return n;
        
     }

     /*****************************************************
      * Modifica i campi del dati della tabella Militare
      *
      * @param cognome
      * @param nome
      * @param dataNascita
      * @param record
     * @return 
      *****************************************************/
     public boolean modificaMilitare(String cognome,String nome,DataOraria dataNascita,Object[]record){

         return modifica(new Object[]{cognome, nome, dataNascita}, record, militare);
     }


     


     /*****************************************************
      * Crea un nuovo dati della tabella Militare
      * 
      * @param cognome
      * @param nome
      * @param dataNascita
      * @param record
      * @return 
      *****************************************************/
     public boolean aggiungiMilitare(String cognome,String nome,DataOraria dataNascita, Object[]record){
         return aggiungi(cognome, nome, dataNascita, record, militare);
     }




     
     /*****************************************************
      * Numero dei campi della tabella Militari
      * @return
      *****************************************************/
     public int dimensioneMilitate(){
         return this.militare.vediTuttiAttributi().size();
     }

     /*****************************************************
      * Elimina il dati militare
      *
      * @param cognome
      * @param nome
      * @param dataNascita es.: 03/07/2011
      *****************************************************/
    public void eliminaMilitare(String cognome, String nome, String dataNascita) {
        try {
            elimina(cognome, nome, dataNascita, militare);
        } catch (Errore ex) {
            javax.swing.JOptionPane.showMessageDialog(
                    null, "Militare non eliminato: "+ex.getMessage(), 
                    "aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
            Logger.getLogger(DatiMilitare.class.getName()).log(Level.SEVERE, null, ex);
            
        }
    }
 

    public ArrayList<Object[]> elencoFavici(String corso) {
        ArrayList<Object[]> x = null;
         try {
            DB.connetti();        
            x = DB.interrogazioneSempliceTabella(
                    militare,
                    String.format(
                        " [%s] = true AND [%s] = '%s' ",
                        militare.nomeAttributo(26),
                        militare.nomeAttributo(24),
                        corso)
                    );
            DB.chiudi();
            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiMilitare.class.getName()).log(Level.SEVERE, null, ex);
            
        }
         return x;
    }

   public ArrayList<Object[]> elencoMilitariAssenzaDocFavismo(String corso, String compagnia) {
        ArrayList<Object[]> x = null;
         try {
            DB.connetti();        
            x = DB.interrogazioneSempliceTabella(
                    militare,
                    String.format(
                        " [%s] = true AND [%s] = '%s' AND [%s] = '%s' ",
                        militare.nomeAttributo(27),
                        militare.nomeAttributo(24),
                        corso,
                        militare.nomeAttributo(6),
                        compagnia)
                    );
            DB.chiudi();
            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiMilitare.class.getName()).log(Level.SEVERE, null, ex);
            
        }
         return x;
    }

    public ArrayList<Object[]> situazioneMedici(DataOraria inizio, DataOraria fine) {
        ArrayList<Object[]> conteggioVisiteIncor=null;
        try {
            String tabella = militare.nome();
            String _medico = militare.nomeAttributo(29);
            String _data = militare.nomeAttributo(22);
            
            DB.connetti();
            String query =String.format(
                      "SELECT  `%s`,count(*) "
                    + "FROM `%s` "
                    + "WHERE `%s` >= #%s 00:00:00# AND `%s` <= #%s 23:59:59# AND `%s` IS NOT NULL "
                    + "GROUP BY  `%s` ",
                    //SELECT
                    _medico,
                    //FROM
                    tabella,
                    //condizione WHERE
                    _data,
                    inizio.stampaGiornoInverso(),
                    _data,
                    fine.stampaGiornoInverso(),
                    _medico,
                    _medico
                    );
            
           Attributo[] colonne= {
               new Attributo(_medico,STRINGA_50,false),
               new Attributo("count(*)",new FunzioneSQL(2),false),
           };
           conteggioVisiteIncor = DB.interrogazioneSQL(query, colonne);
            DB.chiudi();
                 
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiMilitare.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return conteggioVisiteIncor;
    }

    public ArrayList<Object[]> elencoNonIdonei(String corso,String compagnia) {
        ArrayList<Object[]> x = null;
         try {
            DB.connetti();        
            x = DB.interrogazioneSempliceTabella(
                    militare,
                    String.format(
                        " ( [%s] <> 'IDONEO' AND [%s] <> '' ) AND [%s] = '%s' AND [%s] = '%s' ",
                        militare.nomeAttributo(23),
                        militare.nomeAttributo(23),
                        militare.nomeAttributo(24),
                        corso,
                        militare.nomeAttributo(6),
                        compagnia)
                    );
            DB.chiudi();
            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiMilitare.class.getName()).log(Level.SEVERE, null, ex);
            
        }
         return x;
    }

   /**
    * 
    * @param data
    * @return Lista di record composto da:
    * <ul>
    *     <li>`grado`</li>
    *     <li>`cognome`</li>
    *     <li>`nome`</li>
    *     <li>`data di nascita`</li>
    *     <li>`compagnia`</li>
    *     <li>`data GML`</li>
    *     <li>`GML`</li>
    * </ul> 
    */
    public ArrayList<Object[]> elencoIncorporati(DataOraria data) {
        ArrayList<Object[]> incorporati=null;
        try{
            DB.connetti(); 
            String SQL = String.format("SELECT  * "
                    + "FROM `%s` "
                    + "WHERE `%s` >= #%s 00:00:00# AND `%s` <= #%s 23:59:59# AND `%s` IS NOT NULL  ",
                        militare.nome(),
                        militare.nomeAttributo(BASE_DATI.MILITARE.DATA_GML),//data GML
                        data.stampaGiornoInverso(),
                        militare.nomeAttributo(BASE_DATI.MILITARE.DATA_GML),
                        data.stampaGiornoInverso(),
                        militare.nomeAttributo(BASE_DATI.MILITARE.DATA_GML)
                        );
            Attributo[] colonne={
                militare.vediAttributo(BASE_DATI.MILITARE.GRADO),//grado
                militare.vediAttributo(BASE_DATI.MILITARE.COGNOME),//cognome
                militare.vediAttributo(BASE_DATI.MILITARE.NOME),//nome
                militare.vediAttributo(BASE_DATI.MILITARE.DATA_NASCITA),//data di nascita
                militare.vediAttributo(BASE_DATI.MILITARE.COMPAGNIA),//reparto/cp
                //militare.vediAttributo(31),//documento
                //militare.vediAttributo(32),//n° documento
                militare.vediAttributo(BASE_DATI.MILITARE.DATA_GML),//data GML
                militare.vediAttributo(BASE_DATI.MILITARE.GML),//GML
                //militare.vediAttributo(29)//medico
            };
            
            incorporati= DB.interrogazioneSQL(SQL, colonne);
            DB.chiudi();
        }catch(EccezioneBaseDati e){
            Logger.getLogger(DatiMilitare.class.getName()).log(Level.SEVERE, null, e);
        }
        return incorporati;
    }
   
    
    public ArrayList<Object[]> elencoGiudizioIncorporati(DataOraria data) {
        ArrayList<Object[]> incorporati=null;
        try{
            DB.connetti(); 
            String SQL = String.format(
                      "SELECT  * "
                    + "FROM `%s` "
                    + "WHERE `%s` >= #%s 00:00:00# AND `%s` <= #%s 23:59:59# AND `%s` IS NOT NULL  ",
                        militare.nome(),
                        militare.nomeAttributo(22),//data GML
                        data.stampaGiornoInverso(),
                        militare.nomeAttributo(22),
                        data.stampaGiornoInverso(),
                        militare.nomeAttributo(22)
                        );
            Attributo[] colonne={
                militare.vediAttributo(4),//grado
                militare.vediAttributo(0),//cognome
                militare.vediAttributo(1),//nome
                militare.vediAttributo(2),//data di nascita
                militare.vediAttributo(23)//GML
            };
            
            incorporati= DB.interrogazioneSQL(SQL, colonne);
            DB.chiudi();
        }catch(EccezioneBaseDati e){
            Logger.getLogger(DatiMilitare.class.getName()).log(Level.SEVERE, null, e);
        }
        return incorporati;
    }


}


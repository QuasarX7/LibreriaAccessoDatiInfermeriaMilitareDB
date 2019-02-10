package it.difesa.esercito.rav17.infermeria.DaseDati;


import it.quasar_x7.java.BaseDati.Attributo;
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
 *Implementa la struttura della base di dati infermeria MySQL
 *
 * @author Domenico della Peruta
 * @version 1.1.0 ultima modifica 29/01/2015
 ******************************************************************************/
public class DatiVaccinazioni extends Dati {

//---------------------------- costruttore ------------------------------------
    /******************************************************
     * Il costruttore crea le varie entita tabelle con i
     * vari campi e domini e si connette al database
     *****************************************************/
    public DatiVaccinazioni() {
        super();
        
    }

//------------------------------ metodi ----------------------------------------

    

    
     /***************************************************
      * Trova i vari attributi che compongono il dati
      * della tabella anamnesi controindicazioni vaccinale
      * del militare specificato e riferiti alla giornata 
      * di oggi.
      *
      * @deprecated 
      * @param cognome
      * @param nome
      * @param dataNascita
      * @return un oggetto generico rappresentante il dato
      ***************************************************/
     public Object[] trovaControindicazioniOggi(String cognome,String nome,DataOraria dataNascita){
        DataOraria oggi = new DataOraria();
        oggi.oggi();
        return trovaControindicazioni(cognome, nome, dataNascita, oggi);
     }
     
     /*********************************************************
      * Funzione che interroga le tabelle anamnesi_reazione_vaccinale e
      * anamnesi_seduta_vaccinale, per ottenere le informazioni relative
      * alle controindicazioni vaccinali all'atto della seduta vaccinale.
      * 
      * @deprecated 
      * @param cognome
      * @param nome
      * @param dataNascita
      * @param data
      * @return reazioniVacc di dati utili per la visualizzazione e la
      * stampa delle controindicazioni.
      **********************************************************/
     public Object[] trovaControindicazioni(String cognome,String nome,DataOraria dataNascita,DataOraria data){        
         Object[] controindicazioni = null;
         try {
            
            DB.connetti();
            ArrayList<Object[]> recordReazioniVaccinali =
                    DB.interrogazioneSempliceTabella(
                            anamnesiReazioneVaccinale,
                            String.format(
                                    "[%s] = '%s' AND [%s] = '%s' AND [%s] = #%s# " +
                                    "AND " + "[%s] = #%s#",
                                    anamnesiReazioneVaccinale.nomeAttributo(BASE_DATI.ANAMNESI_VACCINALE.COGNOME),//campo [cognome]
                                    correggi(cognome), //valore cognome
                                    anamnesiReazioneVaccinale.nomeAttributo(BASE_DATI.ANAMNESI_VACCINALE.NOME),
                                    correggi(nome), 
                                    anamnesiReazioneVaccinale.nomeAttributo(BASE_DATI.ANAMNESI_VACCINALE.DATA_NASCITA),
                                    dataNascita.stampaGiornoInverso(), 
                                    anamnesiReazioneVaccinale.nomeAttributo(BASE_DATI.ANAMNESI_VACCINALE.DATA_VACCINAZIONE),
                                    data.stampaGiornoInverso()
                            )
                    );
             
            
            Object[] reazioni = new Object[20];
            int j=0;
            if(recordReazioniVaccinali !=null){
                for(Object[] x: recordReazioniVaccinali){
                    reazioni[0+(5*j)] = x[4];//nome
                    for(int i=0;i<3;i++) {
                        if(x[5] != null){
                            if(((Boolean)x[5+i]).booleanValue()){
                                reazioni[i+1+(5*j)]="SI";
                            }else {
                                reazioni[i+1+(5*j)]="NO";
                            }
                        }
                    }
                    reazioni[4+(5*j)] = x[8];//data
                    j++;
                }
             }

            Object[] anamnesi = DB.vediTupla(
                    anamnesiSedutaVaccinale,
                    new Object[]{cognome, nome, dataNascita, data});
            
            controindicazioni=new Object[53];

            controindicazioni[0]=cognome;
            controindicazioni[1]=nome;
            controindicazioni[2]=dataNascita;
            controindicazioni[3]=data;
            System.arraycopy(reazioni, 0, controindicazioni, 4, reazioni.length);

            int k =0;
            if(anamnesi != null){
                for(int i=0;i<anamnesi.length-4;i++){
                    if(anamnesi[i+4] instanceof Boolean)
                        if(((Boolean)anamnesi[i+4]).booleanValue()){
                            controindicazioni[reazioni.length+i+4+k]="X";
                        }else{
                           controindicazioni[reazioni.length+i+4+k]="";
                        }
                    else{//se non BOOLEANO
                        if(((i+4)==25) || ((i+4)==22)){//anamnesi ginecologica
                            if(anamnesi[i+4] !=null)
                                if(((String)anamnesi[i+4]).compareTo("regolare")==0 ||
                                        ((String)anamnesi[i+4]).compareTo("positivo")==0){
                                    controindicazioni[reazioni.length+i+4+k]="X";
                                }else {
                                    controindicazioni[reazioni.length+i+5+k]="X";
                                }
                            k++;
                        }else if ((i+4)== 24){
                            if(anamnesi[i+4]!=null)
                                if(((String)anamnesi[i+4]).compareTo("non in corso")==0){
                                    controindicazioni[reazioni.length+i+4+k]="X";
                                }else if(((String)anamnesi[i+4]).compareTo("in corso")==0){
                                    controindicazioni[reazioni.length+i+5+k]="X";
                                }else if(((String)anamnesi[i+4]).compareTo("non noto")==0){
                                    controindicazioni[reazioni.length+i+6+k]="X";
                                }
                            k+=2;

                        }else
                            controindicazioni[reazioni.length+i+4+k]=anamnesi[i+4];
                    }
                }
             }else{
                DB.chiudi();
                return null;
             }

            DB.chiudi();
            
        } catch (EccezioneBaseDati ex) {            
            Logger.getLogger(DatiVaccinazioni.class.getName()).log(Level.SEVERE, null, ex);
           
        }         
        return controindicazioni;
     }

     /*****************************************************
      * Cerca il reazioniVacc della tabella vaccinazioni 
      *
      * @param vaccino
      * @param cognome
      * @param nome
      * @param dataNascita
      * @return reazioniVacc
      ****************************************************/
     public Object[] trovaVaccinazioneOggi(String vaccino,String cognome,String nome,DataOraria dataNascita){
        _evento.oggi();
        return this.trovaVaccinazione(vaccino, _evento, cognome, nome, dataNascita);
     }
     
     public Object[] trovaVaccinazione(String vaccino,DataOraria seduta,String cognome,String nome,DataOraria dataNascita){
        Object[] x = null;
         try {

            DB.connetti();
               // campo del dati
            x = DB.vediTupla(
                   vaccinazioni, new Object[]{
               seduta, vaccino, cognome, nome, dataNascita});
            DB.chiudi();
            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiVaccinazioni.class.getName()).log(Level.SEVERE, null, ex);
            
        }
         return x;
     }

     /*************************************************
      * Restituisce l'elenco dei militari vaccinati
      *  
      * @param vaccino
      * @param dose "CB1" o "CB2"
      * @param corso
      * @param compagnia
      * @return
      *************************************************/
     public ArrayList<Object[]> elencoVaccinati(String vaccino,String dose,String corso,String compagnia){
        
         ArrayList<Object[]> query = null;
         try {
            DB.connetti();
            
            Attributo[] attributi = vaccinazioni.vediTuttiAttributi().toArray(
                       new Attributo[vaccinazioni.vediTuttiAttributi().size()]);
            
            String condizione = String.format(
                    " %s.[%s] =  %s.[%s] AND %s.[%s] =  %s.[%s] AND  %s.[%s] =  %s.[%s] "//cofronto chiavi
                    + "AND %s.[%s] = '%s' AND %s.[%s] = '%s' " 
                    + "AND %s.[%s] = '%s' AND %s.[%s] = '%s' ",
                    //confronto chiavi
                    militare.nome(),
                    militare.nomeAttributo(0),
                    vaccinazioni.nome(),
                    vaccinazioni.nomeAttributo(2),                 
                    militare.nome(),
                    militare.nomeAttributo(1),
                    vaccinazioni.nome(),
                    vaccinazioni.nomeAttributo(3),                 
                    militare.nome(),
                    militare.nomeAttributo(2),
                    vaccinazioni.nome(),
                    vaccinazioni.nomeAttributo(4),
                    
                    vaccinazioni.nome(),
                    vaccinazioni.nomeAttributo(1),//tipo profilassi
                    vaccino,
                    vaccinazioni.nome(),
                    vaccinazioni.nomeAttributo(5),//tipo dose
                    dose,
                    militare.nome(),
                    militare.nomeAttributo(24),//corso
                    corso,
                    militare.nome(),
                    militare.nomeAttributo(6),//compagnia
                    compagnia);
           query = DB.interrogazioneJoin(
                                        new Relazione[]{militare,vaccinazioni}, 
                                        attributi,
                                        condizione
                                        );
            DB.chiudi();
            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiVaccinazioni.class.getName()).log(Level.SEVERE, null, ex);
            
        }
         return query;
   
     }

     


     /***************************************************
      * Permette di trovare tutti i records della tabella
      * vaccinazioni relativi a militare vaccinato in data
      * odierna
      * 
      * @param cognome
      * @param nome
      * @param nato
      * @return recods delle vaccinazioni effettuate oggi
      * dal militare interessato
      ***************************************************/
     public ArrayList<Object[]> trovaVaccinazioniOggi(String cognome,String nome,DataOraria nato){
        
         ArrayList<Object[]> vaccinazioniOggi=null;
         try {
            DataOraria dataOggi = new DataOraria();
            dataOggi.oggi();
            DB.connetti();
           vaccinazioniOggi =
                   DB.interrogazioneSempliceTabella(
                   vaccinazioni,
                   String.format(
                   " [%s] = '%s' AND [%s] = '%s' AND [%s] = #%s# AND [%s] = #%s# ",
                   vaccinazioni.nomeAttributo(2),
                   correggi(cognome), 
                   vaccinazioni.nomeAttributo(3),
                   correggi(nome), 
                   vaccinazioni.nomeAttributo(4),
                   nato.stampaGiornoInverso(),
                   vaccinazioni.nomeAttributo(0),
                   dataOggi.stampaGiornoInverso()));        
           
           DB.chiudi();
         
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiVaccinazioni.class.getName()).log(Level.SEVERE, null, ex);
            
        }
         return vaccinazioniOggi;
     }

         /********************************************************
      * Trova vaccinazioni di un militare
      * 
      * @param cognome
      * @param nome
      * @param nato
      * @return
      ********************************************************/
     public ArrayList<Object[]> trovaVaccinazioni(String cognome,String nome,DataOraria nato) {
        
         ArrayList<Object[]> _vaccinazioni =null;
         try {
            DB.connetti();
            _vaccinazioni =
                    DB.interrogazioneSempliceTabella(
                    vaccinazioni,
                    String.format(
                   " [%s] = '%s' AND [%s] = '%s' AND [%s] = #%s# ",
                    vaccinazioni.nomeAttributo(2),
                    correggi(cognome), 
                    vaccinazioni.nomeAttributo(3),
                    correggi(nome), 
                    vaccinazioni.nomeAttributo(4),
                    nato.stampaGiornoInverso()));
     
            DB.chiudi();

            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiVaccinazioni.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return _vaccinazioni;
    }
     
     /********************************************************
      * Trova vaccinaziono del un militare
      * 
      * @param cognome
      * @param nome
      * @param nato
      * @param vaccino
      * @return
      ********************************************************/
     public ArrayList<Object[]> trovaVaccinazione(String cognome,String nome,DataOraria nato,String vaccino) {
        
         ArrayList<Object[]> _vaccinazioni =null;
         try {
            DB.connetti();
            _vaccinazioni =
                    DB.interrogazioneSempliceTabella(
                    vaccinazioni,
                    String.format(
                   " [%s] = '%s' AND [%s] = '%s' AND [%s] = #%s# AND [%s] = '%s' ",
                    vaccinazioni.nomeAttributo(2),
                    correggi(cognome), 
                    vaccinazioni.nomeAttributo(3),
                    correggi(nome), 
                    vaccinazioni.nomeAttributo(4),
                    nato.stampaGiornoInverso(),
                    vaccinazioni.nomeAttributo(1),
                    vaccino));
     
            DB.chiudi();

            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiVaccinazioni.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return _vaccinazioni;
    }
     
     /********************************************************
      * Trova i records della tabella vaccinazione
      * @param cognome
      * @param nome
      * @param nato
      * @param seduta data della vaccinazione
      * @return 
      ********************************************************/
     public ArrayList<Object[]> trovaVaccinazioni(String cognome,String nome,DataOraria nato,DataOraria seduta) {
       
         ArrayList<Object[]> _vaccinazioni =null;
         try {
            DB.connetti();
            _vaccinazioni =
                    DB.interrogazioneSempliceTabella(
                    vaccinazioni,
                    String.format(
                    " [%s] = '%s' AND [%s] = '%s' AND [%s] = #%s# AND [%s] = #%s# ",
                    vaccinazioni.nomeAttributo(2),
                    correggi(cognome), 
                    vaccinazioni.nomeAttributo(3),
                    correggi(nome), 
                    vaccinazioni.nomeAttributo(4),
                    nato.stampaGiornoInverso(),
                    vaccinazioni.nomeAttributo(0),
                    seduta.stampaGiornoInverso()));
     
            DB.chiudi();
            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiVaccinazioni.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return _vaccinazioni;
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
      *     <li>`nome vaccino`</li>
      *     <li>`lotto`</li>
      *     <li>`scadenza`</li>
      *     <li>`tipo dose`</li>
      *     <li>`medico`</li>
      * </ul>
      */
     public ArrayList<Object[]> trovaVaccinazioni(DataOraria data){
        
         ArrayList<Object[]> _vaccinazioni =null;
         try {
            DB.connetti();
            String query = String.format(
                      "SELECT v.`seduta vaccinale`,m.`grado`,v.`cognome`,v.`nome`,v.`data di nascita`,m.`compagnia`,v.`nome vaccino`,v.`lotto`,v.`scadenza`,v.`tipo dose`,a.`medico` "
                    + "FROM vaccinazione v, militare m, anamnesi_seduta_vaccinale a "
                    + "WHERE  v.`cognome` = m.`cognome` AND v.`nome` = m.`nome` AND v.`data di nascita` = m.`data di nascita` AND "
                    +        "a.`cognome` = m.`cognome` AND a.`nome` = m.`nome` AND a.`data di nascita` = m.`data di nascita` AND "
                    +        "a.`data vaccinazione` = v.`seduta vaccinale`"
                    +        " AND v.`seduta vaccinale` = '%s'",
                    data.stampaGiornoInverso()
                    );
           
            Attributo[] colonne = {
                vaccinazioni.vediTuttiAttributi().get(0), // seduta vaccinale
                militare.vediTuttiAttributi().get(4),     // grado
                vaccinazioni.vediTuttiAttributi().get(2), // cognome
                vaccinazioni.vediTuttiAttributi().get(3), // nome
                vaccinazioni.vediTuttiAttributi().get(4), // data di nascita
                militare.vediTuttiAttributi().get(6),     // compagnia
                
                vaccinazioni.vediTuttiAttributi().get(7), // nome vaccino
                vaccinazioni.vediTuttiAttributi().get(9), // lotto
                vaccinazioni.vediTuttiAttributi().get(11),// scadenza
                vaccinazioni.vediTuttiAttributi().get(5), // tipo dose
                anamnesiSedutaVaccinale.vediTuttiAttributi().get(27)// medico
            };
            
            _vaccinazioni =
                   DB.interrogazioneSQL(query, colonne);
            DB.chiudi();    

            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiVaccinazioni.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return _vaccinazioni;
     }

     
     
     /***************************************************
      * Conta le vaccinazioni fatte dal periodo indicato
      * 
      * @param inizio
      * @param fine
      * @return un records composto da: tipo profilassi(String),
      * tipo dose (String),sesso (String) ,numerico (Long)
      ***************************************************/
     public ArrayList<Object[]> situazioneVaccinale(DataOraria inizio , DataOraria fine){
        ArrayList<Object[]> conteggioVaccinazioni=null;
        try {
            DB.connetti();
            String query =String.format(
                      "SELECT v.`%s`, v.`%s`, m.`%s`, count(*) "
                    + "FROM `%s` v, `%s` m "
                    + "WHERE v.`%s` >= '%s' AND v.`%s` <= '%s' AND "
                    + " v.`cognome` = m.`cognome` AND v.`nome` = m.`nome` AND v.`data di nascita` = m.`data di nascita` "
                    + "GROUP BY  `%s`, `%s`, `%s` "
                    + "ORDER BY  `%s`, `%s`, `%s` ",
                    //SELECT
                    vaccinazioni.nomeAttributo(1),//tipo profilassi
                    vaccinazioni.nomeAttributo(5),//tipo dose
                    militare.nomeAttributo(30),//sesso
                    //FROM
                    vaccinazioni.nome(),
                    militare.nome(),
                    //condizione WHERE
                    vaccinazioni.nomeAttributo(0),//seduta vaccinale
                    inizio.stampaGiornoInverso(),
                    vaccinazioni.nomeAttributo(0),//seduta vaccinale
                    fine.stampaGiornoInverso(),
                    
                    //GROUP BY
                    vaccinazioni.nomeAttributo(1),//tipo profilassi
                    vaccinazioni.nomeAttributo(5),//tipo dose
                    militare.nomeAttributo(30),//sesso
                    //ORDER BY
                    vaccinazioni.nomeAttributo(1),//tipo profilassi
                    vaccinazioni.nomeAttributo(5),//tipo dose
                    militare.nomeAttributo(30)//sesso
                    );
            
           Attributo[] colonne= {
               new Attributo("tipo profilassi",STRINGA_30,false),
               new Attributo("tipo dose",STRINGA_3,false),
               new Attributo("sesso",STRINGA_1,false),
               new Attributo("count(*)",new FunzioneSQL(4),false),
           };
           conteggioVaccinazioni = DB.interrogazioneSQL(query, colonne);
            DB.chiudi();
                 
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiVaccinazioni.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return conteggioVaccinazioni;
         
     }

    /**
     * Metodo che fà una interrogazione alla base di dati per determinare
     * la copertura vaccinale di ogni militare di un dato corso e compagnia. 
     * Si ottiene una query con informazioni parziali per militare, 
     * che vanno aggregate ed elaborate dal metodo chiamante.
     * 
     * 
     * @param compagnia
     * @param corso
     * @return 
     * <br>
     *  +---------+-------+-----------+-------+-------+-----+-----+-----+------+------+  <br>
     *  | cognome | nome  | data di n.| POLIO |TETANO | MEN | VAR | MPR | Ep.A | Ep. B|  <br>
     *  +---------+-------+-----------+-------+-------+-----+-----+-----+------+------+  <br>
     *    ROSSI    MARIO   10/10/1999             X           ok                         <br>
     *    ROSSI    MARIO   10/10/1999             X                 ok                   <br>
     *    ROSSI    MARIO   10/10/1999    ok       X                                      <br>
     *    BIANCHI  LUCA    11/11/2000     X       X     ok                               <br>
     *                                                                                   <br>
     * N.B.                                                                              <br>
     * ok = già vaccinato o immunizzato                                                  <br>
     * X  = vaccinato                                                                    <br>
     *                         
     */ 
    public ArrayList<Object[]> coperturaVaccinale(String compagnia,String corso){
        ArrayList<Object[]> copertura = null;
        try {
            DB.connetti();
            String mCognome = militare.nomeAttributo(BASE_DATI.MILITARE.COGNOME);
            String mNome = militare.nomeAttributo(BASE_DATI.MILITARE.NOME);
            String mNascita = militare.nomeAttributo(BASE_DATI.MILITARE.DATA_NASCITA);
                    
            String vProfilassi = vaccinazioni.nomeAttributo(BASE_DATI.VACCINAZIONI.TIPO_PROFILASSI);
            String sData = storiaVaccinale.nomeAttributo(BASE_DATI.STORIA_VACCINALE.DATA_VACCINAZIONE);
            String sProfilassi = storiaVaccinale.nomeAttributo(BASE_DATI.STORIA_VACCINALE.PROFILASSI);
            String sDose = storiaVaccinale.nomeAttributo(BASE_DATI.STORIA_VACCINALE.DOSE);
                    
            String query = String.format(
                    "SELECT DISTINCT "+
                    "m.`%s`, m.`%s`, m.`%s`, " + //[1]
                            
                    " IF(v.`%s` LIKE '%s','X'," + // [2]
                        " IF((DATEDIFF(NOW(), s.`%s`) / 365.25 < 10) AND s.`%s` = 'POLIO', 'ok', '')" //[3]
                   + ") AS POLIO," +
                            
                    " IF(v.`%s` LIKE '%s', 'X'," + //[4]
                        " IF((DATEDIFF(NOW(), s.`%s`) / 365.25 < 10) AND s.`%s` = 'TETANO', 'ok', '')" //[5]
                   + ") AS TETANO," +
                            
                    " IF(v.`%s` LIKE '%s', 'X'," + //[6]
                        " IF((DATEDIFF(NOW(), s.`%s`) / 365.25 < 5) AND s.`%s` = 'MENINGITE', 'ok', '')" //[7]
                   + ") AS MEN," +
                            
                    " IF(v.`%s` LIKE '%s', 'X'," + //[8]
                        " IF(s.`%s` = 'CB2' AND s.`%s` = 'VARICELLA', 'ok'," //[9]
                            + " IF((s.`%s` = true OR s.`%s` = true)  AND s.`%s` = 'VARICELLA', 'ok', '')" //[10]
                       + ")"
                   + ") AS VARICELLA," +
                            
                    " IF(v.`%s` LIKE '%s', 'X'," + //[11]
                        " IF((s.`%s` = 'CB2' OR s.`%s` = '1D') AND s.`%s` = 'ROSOLIA', 'ok', '')" //[12]
                   + ") AS MorParRos," +
                            
                    " IF(v.`%s` LIKE '%s', 'X'," + //[13]
                        "  IF(s.`%s` = 'CB2' AND s.`%s` = 'EPATITE A', 'ok', '')" //[14]
                   + ") AS EpA," +
                            
                    " IF(v.`%s` LIKE '%s', 'X'," + //[15]
                        " IF((s.`%s` = 'CB3' OR s.`%s` = 'B') AND s.`%s` = 'EPATITE B', 'ok', '')" //[16]
                   + ") AS EpB" +
                            
                    " FROM " +
                    " %s m LEFT OUTER JOIN %s v "+ // [17]
                            
                    " ON "+ 
                    "  m.`%s` = v.`%s` AND m.`%s` = v.`%s` AND m.`%s` = v.`%s` "+ //[18]
                    
                    " LEFT OUTER JOIN %s s "+ //[19]
                    " ON "+ 
                    "  m.`%s` = s.`%s` AND m.`%s` = s.`%s` AND m.`%s` = s.`%s` "+ //[20]
                    " WHERE" +
                    " m.`%s` = '%s' AND m.`%s` = '%s'",//[21]
                    //SELECT
                    mCognome, mNome, mNascita,  //[1]
                    vProfilassi,"%polio%",      //[2]
                    sData, sProfilassi,         //[3]
                    vProfilassi,"%tetano%",      //[4]
                    sData, sProfilassi,         //[5]
                    vProfilassi,"%meningite%",  //[6]
                    sData, sProfilassi,         //[7]
                    vProfilassi,"%varicella%",  //[8]
                    sDose, sProfilassi,         //[9]
                    //[10]
                    storiaVaccinale.nomeAttributo(BASE_DATI.STORIA_VACCINALE.PREGRESSA),
                    storiaVaccinale.nomeAttributo(BASE_DATI.STORIA_VACCINALE.PREGRESSA_DOC),
                    sProfilassi,
                    //[10]
                    vProfilassi,"%rosolia%",    //[11]
                    sDose,sDose, sProfilassi,   //[12]
                    vProfilassi,"%epatite a%",  //[13]
                    sDose,sProfilassi,          //[14]
                    vProfilassi,"%epatite b%",  //[15]
                    sDose,sDose,sProfilassi,    //[16]
                    //FROM
                    //[17]
                    militare.nome(),
                    vaccinazioni.nome(),
                    //storiaVaccinale.nome(),
                    //[17]
                    
                    //WHERE
                    //[18]
                    mCognome,vaccinazioni.nomeAttributo(BASE_DATI.VACCINAZIONI.COGNOME),
                    mNome,vaccinazioni.nomeAttributo(BASE_DATI.VACCINAZIONI.NOME),
                    mNascita,vaccinazioni.nomeAttributo(BASE_DATI.VACCINAZIONI.DATA_NASCITA),
                    //[18]
                    
                    //[19]
                    storiaVaccinale.nome(),
                    //[19]
                    
                    //[20]
                    mCognome,storiaVaccinale.nomeAttributo(BASE_DATI.STORIA_VACCINALE.COGNOME),
                    mNome,storiaVaccinale.nomeAttributo(BASE_DATI.STORIA_VACCINALE.NOME),
                    mNascita,storiaVaccinale.nomeAttributo(BASE_DATI.STORIA_VACCINALE.DATA_NASCITA),
                    //[20]
                    
                    //[21]
                    militare.nomeAttributo(BASE_DATI.MILITARE.CORSO),
                    corso,
                    militare.nomeAttributo(BASE_DATI.MILITARE.COMPAGNIA),
                    compagnia
                    //[21]
            );
            
             
            Attributo[] colonne = {
                militare.vediAttributo(BASE_DATI.MILITARE.COGNOME),
                militare.vediAttributo(BASE_DATI.MILITARE.NOME),
                militare.vediAttributo(BASE_DATI.MILITARE.DATA_NASCITA),
                new Attributo("POLIO",new DatoStringa(2),false), 
                new Attributo("TETANO",new DatoStringa(2),false), 
                new Attributo("MEN",new DatoStringa(2),false), 
                new Attributo("VARICELLA",new DatoStringa(2),false), 
                new Attributo("MorParRos",new DatoStringa(2),false), 
                new Attributo("EpA",new DatoStringa(2),false), 
                new Attributo("EpB",new DatoStringa(2),false), 
            }; 
            copertura = DB.interrogazioneSQL(query, colonne);
            DB.chiudi();
             
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiVaccinazioni.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        /*
        


        */
        return copertura;
    } 
     
    /**
     * Metodo che permette di determinare il numero di vaccini fatti in un intervallo temporale.
     * 
     * @param inizio
     * @param fine
     * @return lista di record del tipo:
     * <ol>
     *  <li>tipo profilassi</li>
     *  <li>tipo dose</li> 
     *  <li>sesso (M o F)</li>
     *  <li>età (> 25 anni)</li>
     *  <li>quantità</li>
     * </ol>
     */
    public ArrayList<Object[]> situazioneVaccini(DataOraria inizio,DataOraria fine){
        ArrayList<Object[]> vaccini = null;
        try {
            DB.connetti();
            String query = String.format(
                    "SELECT v.`%s`, v.`%s`, m.`%s`, (datediff(now(),m.`%s`)/365.25 > 25) as f, count(*) "
                  + "FROM %s v, %s m "
                  + "WHERE v.`%s` = m.`%s` AND  v.`%s` = m.`%s` AND v.`%s` = m.`%s` "
                  + "AND v.`%s` >= '%s' AND v.`%s` <= '%s' "
                  + "GROUP BY v.`%s`, v.`%s`, m.`%s`, f ",
                    //SELECT
                    vaccinazioni.nomeAttributo(BASE_DATI.VACCINAZIONI.TIPO_PROFILASSI),
                    vaccinazioni.nomeAttributo(BASE_DATI.VACCINAZIONI.DOSE),
                    militare.nomeAttributo(BASE_DATI.MILITARE.SESSO),
                    militare.nomeAttributo(BASE_DATI.MILITARE.DATA_NASCITA),//per il calcolo dell'età ("f")
                    
                    //FROM
                    vaccinazioni.nome(), militare.nome(),
                    //WHERE
                    vaccinazioni.nomeAttributo(BASE_DATI.VACCINAZIONI.COGNOME),
                    militare.nomeAttributo(BASE_DATI.MILITARE.COGNOME),
                    vaccinazioni.nomeAttributo(BASE_DATI.VACCINAZIONI.NOME),
                    militare.nomeAttributo(BASE_DATI.MILITARE.NOME),
                    vaccinazioni.nomeAttributo(BASE_DATI.VACCINAZIONI.DATA_NASCITA),
                    militare.nomeAttributo(BASE_DATI.MILITARE.DATA_NASCITA),                 
                    vaccinazioni.nomeAttributo(BASE_DATI.VACCINAZIONI.DATA_SEDUTA),
                    inizio.stampaGiornoInverso(),
                    vaccinazioni.nomeAttributo(BASE_DATI.VACCINAZIONI.DATA_SEDUTA),
                    fine.stampaGiornoInverso(),
                    //GROUP BY
                    vaccinazioni.nomeAttributo(BASE_DATI.VACCINAZIONI.TIPO_PROFILASSI),
                    vaccinazioni.nomeAttributo(BASE_DATI.VACCINAZIONI.DOSE),
                    militare.nomeAttributo(BASE_DATI.MILITARE.SESSO)
            );
            
             
            Attributo[] colonne = {
                vaccinazioni.vediAttributo(BASE_DATI.VACCINAZIONI.TIPO_PROFILASSI),
                vaccinazioni.vediAttributo(BASE_DATI.VACCINAZIONI.DOSE),
                militare.vediAttributo(BASE_DATI.MILITARE.SESSO),
                new Attributo("f",new FunzioneSQL(4),false),   
                new Attributo("count(*)",new FunzioneSQL(5),false)        
             }; 
             vaccini = DB.interrogazioneSQL(query, colonne);
             DB.chiudi();
             
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiVaccinazioni.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return vaccini;
    }
     
    /**
     * Funzione che determina quanti militari  per compagnia si sono
     * presentati per la profilassi vaccinale. 
     * 
     * @param inizio
     * @param fine
     * @return 
     */
    public ArrayList<Object[]> situazioneVaccinati(DataOraria inizio,DataOraria fine){
        ArrayList<Object[]> vaccinazioniCp = null;
        try {
            DB.connetti();
            String query = String.format(
                    "SELECT a.`%s`, m.`%s`,count(*) "
                  + "FROM %s a, %s m "
                  + "WHERE a.`%s` = m.`%s` AND  a.`%s` = m.`%s` AND a.`%s` = m.`%s` "
                  + "AND a.`%s` >= '%s' AND a.`%s` <= '%s' "
                  + "GROUP BY a.`%s`, m.`%s` "
                  + "ORDER BY a.`%s`, m.`%s`",
                    //SELECT
                    anamnesiSedutaVaccinale.nomeAttributo(BASE_DATI.ANAMNESI_VACCINALE.DATA_VACCINAZIONE),
                    militare.nomeAttributo(BASE_DATI.MILITARE.COMPAGNIA),
                    //FROM
                    anamnesiSedutaVaccinale.nome(),
                    militare.nome(),
                    //WHERE
                    anamnesiSedutaVaccinale.nomeAttributo(BASE_DATI.ANAMNESI_VACCINALE.COGNOME),
                    militare.nomeAttributo(BASE_DATI.MILITARE.COGNOME),
                    anamnesiSedutaVaccinale.nomeAttributo(BASE_DATI.ANAMNESI_VACCINALE.NOME),
                    militare.nomeAttributo(BASE_DATI.MILITARE.NOME),
                    anamnesiSedutaVaccinale.nomeAttributo(BASE_DATI.ANAMNESI_VACCINALE.DATA_NASCITA),
                    militare.nomeAttributo(BASE_DATI.MILITARE.DATA_NASCITA),                 
                    anamnesiSedutaVaccinale.nomeAttributo(BASE_DATI.ANAMNESI_VACCINALE.DATA_VACCINAZIONE),
                    inizio.stampaGiornoInverso(),
                    anamnesiSedutaVaccinale.nomeAttributo(BASE_DATI.ANAMNESI_VACCINALE.DATA_VACCINAZIONE),
                    fine.stampaGiornoInverso(),
                    //GROUP BY
                    anamnesiSedutaVaccinale.nomeAttributo(BASE_DATI.ANAMNESI_VACCINALE.DATA_VACCINAZIONE),
                    militare.nomeAttributo(BASE_DATI.MILITARE.COMPAGNIA),
                    //ORDER BY
                    anamnesiSedutaVaccinale.nomeAttributo(BASE_DATI.ANAMNESI_VACCINALE.DATA_VACCINAZIONE),
                    militare.nomeAttributo(BASE_DATI.MILITARE.COMPAGNIA)
            );
            
             
            Attributo[] colonne = {
                anamnesiSedutaVaccinale.vediAttributo(BASE_DATI.ANAMNESI_VACCINALE.DATA_VACCINAZIONE),
                militare.vediAttributo(BASE_DATI.MILITARE.COMPAGNIA),
                new Attributo("count(*)",new FunzioneSQL(3),false)        
             };         
             vaccinazioniCp = DB.interrogazioneSQL(query, colonne);
             DB.chiudi();
             
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiVaccinazioni.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return vaccinazioniCp;
    }

     /***************************************************************
      * Funzione che determina quanti militari  per compagnia si sono
      * presentati per la profilassi vaccinale. 
      * 
      * @param data giorno della seduta
      * @return restituisce una lista di records contenente: la 
      * compagnia (String) e in mumerico dei vaccinati (Long)
      ***************************************************************/
     public ArrayList<Object[]> situazioneVaccinati(DataOraria data){
        ArrayList<Object[]> vaccinazioniCp = null;
        try {
            DB.connetti();
             String query = String.format(
                       "SELECT m.`%s`,count(*) "
                     + "FROM %s a, %s m "
                     + "WHERE a.`%s` = m.`%s` AND  a.`%s` = m.`%s` AND a.`%s` = m.`%s` "
                     + "AND a.`%s` = '%s' "
                     + "GROUP BY m.`%s` "
                     + "ORDER BY m.`%s`",
                     militare.nomeAttributo(6),//compagnia
                     //FROM
                     anamnesiSedutaVaccinale.nome(),
                     militare.nome(),
                     //WHERE
                     anamnesiSedutaVaccinale.nomeAttributo(0),
                     militare.nomeAttributo(0),
                     anamnesiSedutaVaccinale.nomeAttributo(1),
                     militare.nomeAttributo(1),
                     anamnesiSedutaVaccinale.nomeAttributo(2),
                     militare.nomeAttributo(2),                 
                     anamnesiSedutaVaccinale.nomeAttributo(3),
                     data.stampaGiornoInverso(),
                     //GROUP BY
                     militare.nomeAttributo(6),//compagnia
                     //ORDER BY
                     militare.nomeAttributo(6)//compagnia
                     );
            
             Attributo[] colonne = {
                new Attributo("compagnia",STRINGA_20,false),
                new Attributo("count(*)",new FunzioneSQL(2),false)        
             };         
             vaccinazioniCp = DB.interrogazioneSQL(query, colonne);
             DB.chiudi();
             
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiVaccinazioni.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return vaccinazioniCp;
     }
     
     /***************************************************
      * Determina il numero di vaccinati in DATA_ORA odierna
      *
      * @return
      ***************************************************/
     public int numeroVaccinati(DataOraria d) throws EccezioneBaseDati{
        int n = 0;
        DB.connetti();
            String SQL = String.format(
                     "SELECT COUNT(*) "
                   + "FROM %s "
                   + "WHERE [%s] = #%s# ",
                   anamnesiSedutaVaccinale.nome(),//from
                   anamnesiSedutaVaccinale.nomeAttributo(3),//campo [DATA_ORA vaccinazione]
                   d.stampaGiornoInverso() // #data_di_oggi#                
                   );
           Attributo[] dati = {
               new Attributo("count(*)",new FunzioneSQL(1),false)
           };        
           n = ((Long)DB.interrogazioneSQL(SQL, dati).get(0)[0]).intValue(); 
           DB.chiudi();
           
        
        return n;
     }

     
     public int numeroVaccinati(String reggimento,DataOraria d) throws EccezioneBaseDati{
        int n = 0;
        DB.connetti();
            String SQL = String.format(
                     "SELECT COUNT(*) "
                   + "FROM %s, %s "
                   + "WHERE `%s`.`%s` = #%s#  AND "
                   + " `%s`.`%s` = `%s`.`%s` AND `%s`.`%s` = `%s`.`%s` AND `%s`.`%s` = `%s`.`%s` "
                   + "  AND  `%s`.`%s` LIKE '%s' ",
                   anamnesiSedutaVaccinale.nome(),militare.nome(),//from
                   
                   anamnesiSedutaVaccinale.nome(),anamnesiSedutaVaccinale.nomeAttributo(3),d.stampaGiornoInverso() ,
                   
                   anamnesiSedutaVaccinale.nome(), anamnesiSedutaVaccinale.nomeAttributo(0),  militare.nome(), militare.nomeAttributo(0),
                   anamnesiSedutaVaccinale.nome(), anamnesiSedutaVaccinale.nomeAttributo(1),  militare.nome(), militare.nomeAttributo(1),
                   anamnesiSedutaVaccinale.nome(), anamnesiSedutaVaccinale.nomeAttributo(2),  militare.nome(), militare.nomeAttributo(2),

                   militare.nome(),militare.nomeAttributo(6),"%"+reggimento+"%"
                   
                   );
           Attributo[] dati = {
               new Attributo("count(*)",new FunzioneSQL(1),false)
           };        
           n = ((Long)DB.interrogazioneSQL(SQL, dati).get(0)[0]).intValue(); 
           DB.chiudi();
           
        
        return n;
     }
     
     
     
     /**********************************************
      * Aggiorna e modifica i valori dei recordReazioniVaccinali delle
      * tabelle reazione_precedente_vaccinazione e
      * anamnesi_seduta_vaccinale relativi alla DATA_ORA
      * odierna.
      * 
      * @deprecated 
      * @param cognome
      * @param nome
      * @param dataNascita
      * @param dati
      **********************************************/
     public void aggiornaAnamnesiControindicazioniOggi(String cognome,String nome,
             DataOraria dataNascita, Object[]dati){
        DataOraria oggi = new DataOraria();
        oggi.oggi();
        this.aggiornaAnamnesiControindicazioni(cognome, nome, dataNascita, oggi, dati);
     }
     
     
     
     
     /**********************************************
      * Aggiorna e modifica i valori dei recordReazioniVaccinali delle
      * tabelle reazione_precedente_vaccinazione e
      * anamnesi_seduta_vaccinale relativi alla DATA_ORA specificata.
      *
      * @deprecated 
      * @param cognome
      * @param nome
      * @param dataNascita
      * @param dati
      * @param seduta
      **********************************************/
     public void aggiornaAnamnesiControindicazioni(String cognome,String nome,
             DataOraria dataNascita, DataOraria seduta, Object[]dati){
        try {
            DB.connetti();
            //------------------eliminazione---------------------------------------
            
            String query = String.format(
                       "[%s] = '%s' AND [%s] = '%s' AND [%s] = #%s# AND [%s] = #%s# ",
                       anamnesiReazioneVaccinale.nomeAttributo(0),
                       correggi(cognome), 
                       anamnesiReazioneVaccinale.nomeAttributo(1),
                       correggi(nome), 
                       anamnesiReazioneVaccinale.nomeAttributo(2),
                       dataNascita.stampaGiornoInverso(),
                       anamnesiReazioneVaccinale.nomeAttributo(3),
                       seduta.stampaGiornoInverso()
                    );
           
            ArrayList<Object[]> reazioniVacc =
                   DB.interrogazioneSempliceTabella(anamnesiReazioneVaccinale,query);
            
        
            try{
                DB.eliminaTupla(anamnesiSedutaVaccinale,
                    new Object[]{cognome,nome,dataNascita,seduta});
                _evento.adesso();
                System.out.println(
                        _evento+
                        " -> eliminazione del record ["+ " "+
                        cognome+" "+nome+" "+dataNascita+" "+seduta
                        +"...] dalla tabella '"+anamnesiSedutaVaccinale.nome()+"'!"
                        );
                if(reazioniVacc !=null) {
                    for(Object[] x: reazioniVacc){
                        DB.eliminaTupla(anamnesiReazioneVaccinale,
                                new Object[]{cognome,nome,dataNascita,seduta,x[4]}); 
                        _evento.adesso();
                        System.out.println(
                                _evento+
                                " -> eliminazione del record ["+ " "+
                                cognome+" "+nome+" "+dataNascita+" "+seduta+" "+x[4]
                                +"...] dalla tabella '"+anamnesiReazioneVaccinale.nome()+"'!"
                                );
                    }
                }
            
            }catch(EccezioneBaseDati e){
                
            }
            
           //------------------aggiornamento---------------------------------------         
            ArrayList<Object[]> controindicazioni =
                    this.inizializzaValoriControindicazioni(
                    dati, cognome, nome, dataNascita,seduta);

            Object[] anamnesi = controindicazioni.get(0);

            try{
                DB.aggiungiTupla(anamnesiSedutaVaccinale,anamnesi);
                _evento.adesso();
                System.out.println(
                        _evento+
                        " -> aggiunta del record ["+ " "+
                        cognome+" "+nome+" "+dataNascita+" "+seduta
                        +"...] nella tabella '"+anamnesiSedutaVaccinale.nome()+"'!"
                        );
            }catch(EccezioneBaseDati e){
                 _evento.adesso();
                System.out.println(
                        _evento+
                        " -> ERRORE aggiunta del record ["+ " "+
                        cognome+" "+nome+" "+dataNascita+" "+seduta
                        +"...] nella tabella '"+anamnesiSedutaVaccinale.nome()+"'!"
                        );
                javax.swing.JOptionPane.showMessageDialog(
                        null,
                        "Anamnesi controindicazioni non aggiunta!\n",
                        "Errore",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
                Logger.getLogger(DatiVaccinazioni.class.getName()).log(Level.SEVERE, null, e);
            }
            
            int lim = controindicazioni.size();

            for(int i=0;i<lim-1;i++){
                Object[] reazioni = controindicazioni.get(1+i);
                try{
                    DB.aggiungiTupla(anamnesiReazioneVaccinale,reazioni);
                    _evento.adesso();
                    System.out.println(
                            _evento+
                            " -> aggiunta del record ["+ " "+
                            cognome+" "+nome+" "+dataNascita+" "+seduta
                            +"...] nella tabella '"+anamnesiReazioneVaccinale.nome()+"'!"
                            );
                }catch(EccezioneBaseDati e){
                    _evento.adesso();
                    System.out.println(
                            _evento+
                            " -> ERRORE aggiunta del record ["+ " "+
                            cognome+" "+nome+" "+dataNascita+" "+seduta
                            +"...] nella tabella '"+anamnesiReazioneVaccinale.nome()+"'!"
                            );
                    javax.swing.JOptionPane.showMessageDialog(
                        null,
                        "Anamnesi reazione a precendeti profilassi non aggiunta!",
                        "Errore",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
                     Logger.getLogger(DatiVaccinazioni.class.getName()).log(Level.SEVERE, null, e);
                   
                }
                
           }
           DB.chiudi();
        } catch (EccezioneBaseDati ex) {
            _evento.adesso();
            System.out.println(
                    _evento+
                    " -> ERRORE connessione alla tabella '"+anamnesiReazioneVaccinale.nome()+"' o alla tabella '"+anamnesiSedutaVaccinale.nome()+"'."
                    );
            Logger.getLogger(DatiVaccinazioni.class.getName()).log(Level.SEVERE, null, ex);
            
        }
     }

     

     /**
      * 
      * @deprecated 
      * @param dati
      * @param cognome
      * @param nome
      * @param dataNascita
      * @param seduta
      * @return 
      */
     private ArrayList<Object[]> inizializzaValoriControindicazioni(Object[] dati,
             String cognome,String nome,DataOraria dataNascita,DataOraria seduta){

         ArrayList<Object[]> controindicazioni = new ArrayList<Object[]>();

         DataOraria data;
         if(seduta != null){
             data = seduta;
         }else{
              data = new DataOraria();
            data.oggi();
         }
         

         int dim = anamnesiSedutaVaccinale.vediTuttiAttributi().size();
         Object[] anamnesi = new Object[dim];
        //trasferimento dati di chiave --------------------------------
         anamnesi[0] = cognome;
         anamnesi[1] = nome;
         anamnesi[2] = dataNascita;
         anamnesi[3] = data;
         //trasferimento dati "allergie" "conviventi" "ananmesi recenti"
        for(int i=0;i< 18;i++){
             if(dati[24+i]!=null) {
                if(((String)dati[24+i]).compareTo("X")==0){
                   anamnesi[4+i] = true;//per i dati booleana
                }else if(((String)dati[24+i]).compareTo("D")==0||
                         ((String)dati[24+i]).compareTo("S")==0){
                   anamnesi[4+i] = dati[24+i];//per i dati stringa
               }
            }
        }
         //trasferimento dati "ginecologici"---------------------------
        if(dati[42]!=null){
             if(dati[42]=="X"){
                 anamnesi[22]="regolare";
             }
        }
        if(dati[43]!=null){
             if(dati[43]=="X"){
                anamnesi[22]="irregolare";
             }
         }
         
        anamnesi[23] = dati[44];

        if(dati[45]!=null){
             if(dati[45]=="X"){
                 anamnesi[24]="non in corso";
             }
        }
        if(dati[46]!=null){
            if(dati[46]=="X"){
                anamnesi[24]="in corso";
             }
        }
        if(dati[47]!=null){
            if(dati[47]=="X"){
                anamnesi[24] = "non noto";
            }
        }

        if(dati[48]!=null){
             if(dati[48]=="X"){
                 anamnesi[25]="positivo";
             }
        }
        if(dati[49]!=null){
            if(dati[49]=="X"){
                anamnesi[25]="negativo";
             }
        }
        anamnesi[26] = dati[50];
        anamnesi[27] = dati[51];
        
        anamnesi[28] = dati[52];

        controindicazioni.add(anamnesi);
        //---------------------------------------------------
        
         dim = anamnesiReazioneVaccinale.vediTuttiAttributi().size();
         
         for(int i=0;i<4;i++){
             Object[] reazioni = new Object[dim];
             if(dati[4+(i*5)]!=null && ((String)dati[4+(i*5)]).compareTo("")!=0){
                 reazioni[0] = cognome;
                 reazioni[1] = nome;
                 reazioni[2] = dataNascita;
                 reazioni[3] = data;
                 reazioni[4] = dati[4+(i*5)];

                 for(int j=0;j<3;j++){
                     if(dati[5+j+(i*5)]!=null){
                         if(((String)dati[5+j+(i*5)]).compareTo("SI")==0){
                            reazioni[5+j] = true;
                         }else if(((String)dati[5+j+(i*5)]).compareTo("NO")==0){
                            reazioni[5+j] = false;
                         }
                     }
                 }
                 reazioni[8] = dati[8+(i*5)];
                 controindicazioni.add(reazioni);
             }             
         }
         return controindicazioni;
     }


 

     /*****************************************************
      * Metodo che aggiunge un nuovo record alla tabella BASE_DATI vaccini, con data di oggi.
      *
      * @param vaccino
      * @param cognome
      * @param nome
      * @param dataNascita
      * @param record
      * @return 
      *****************************************************/
     public boolean aggiungiVaccinazioneOggi(String vaccino,String cognome,String nome,
             DataOraria dataNascita,Object[]record){
         _evento.oggi();
        return aggiungiVaccinazione(vaccino, _evento, cognome, nome, dataNascita, record);
         
     }
     
     
    /**
     * Metodo che aggiunge un record alla tabella delle controindicazioni ai vaccini.
     * 
     * @param record
     * @return 
     */    
    public boolean aggiungiReazioneVaccino(Object[] record){
        try {
            DB.connetti();
            DB.aggiungiTupla(anamnesiReazioneVaccinale, record);
            DB.chiudi();
            System.out.println(String.format(" -> aggiunto record [%s %s %s %s %s ...] alla tabella %s",
                            record[BASE_DATI.REAZIONE_VACCINALE.COGNOME],
                            record[BASE_DATI.REAZIONE_VACCINALE.NOME],
                            record[BASE_DATI.REAZIONE_VACCINALE.DATA_NASCITA],
                            record[BASE_DATI.REAZIONE_VACCINALE.VACCINO],
                            record[BASE_DATI.REAZIONE_VACCINALE.DATA_VACCINAZIONE],
                            anamnesiReazioneVaccinale.nome()
                    )
            );
            return true;
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiVaccinazioni.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

     /**
      * Metodo che aggiunge un nuovo record alla tabella BASE_DATI vaccini.
      * 
      * @param vaccino
      * @param seduta
      * @param cognome
      * @param nome
      * @param dataNascita
      * @param record
      * @return 
      */
     public boolean aggiungiVaccinazione(String vaccino,DataOraria seduta,String cognome,String nome,
             DataOraria dataNascita,Object[]record){
        try {
            record[1]=vaccino;
            record[2]=cognome;
            record[3]=nome;
            record[4]=dataNascita;
            record[0]=seduta;
            this.DB.connetti();
            try{
                DB.aggiungiTupla(
                    vaccinazioni,
                    record);
                _evento.adesso();
                System.out.println(
                    _evento+
                    " -> aggiunto record ["+ " "+
                    cognome+" "+nome+" "+dataNascita+" "+seduta+" "+vaccino
                    +"...] alla tabella '"+vaccinazioni.nome()+"'!"
                );
                this.DB.chiudi();
                return true;   
            }catch(EccezioneBaseDati e){
                _evento.adesso();
                System.out.println(
                        _evento+
                        " -> ERRORE aggiunta record ["+ " "+
                        cognome+" "+nome+" "+dataNascita+" "+seduta+" "+vaccino
                        +"...] alla tabella '"+vaccinazioni.nome()+"'!"
                        );
                JOptionPane.showMessageDialog(
                               null,
                               "Nessun valore aggiunto alla tabella "+
                               vaccinazioni.nome()+"!",
                               "avviso",
                               JOptionPane.WARNING_MESSAGE);
                Logger.getLogger(DatiVaccinazioni.class.getName()).log(Level.SEVERE, null, e);
                
            }
            this.DB.chiudi();
        } catch (EccezioneBaseDati ex) {
            _evento.adesso();
            System.out.println(
                    _evento+
                    " -> ERRORE connessione alla tabella '"+vaccinazioni.nome()+"'!"
                    );
            Logger.getLogger(DatiVaccinazioni.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
     }
    
     /*****************************************************
      * Numero dei campi della tabella Vaccinazioni
      * 
      * @return
      *****************************************************/
     public int dimensioneVaccinazioni(){
         return this.vaccinazioni.vediTuttiAttributi().size();
     }


     /*****************************************************
      * Elimina il dati vaccinazioni
      *
      * @param vaccino
     * @param data
      * @param cognome
      * @param nome
      * @param dataNascita
     * @return 
      *****************************************************/
    public boolean eliminaVaccinazione(String vaccino,DataOraria data,
            String cognome, String nome, DataOraria dataNascita) {
        try {
            DB.connetti();
            try {
                
                DB.eliminaTupla(
                            this.vaccinazioni,
                             new Object[]{data,vaccino,cognome,nome,dataNascita});
                _evento.adesso();
               System.out.println(
                                _evento+
                                " -> elimina record ["+ " "+
                                cognome+" "+nome+" "+dataNascita+" "+data+" "+vaccino
                                +"...] dalla tabella '"+vaccinazioni.nome()+"'!"
                                );
            
            
                
                
            } catch (EccezioneBaseDati ex) {
                _evento.adesso();
                System.out.println(
                        _evento+
                        " -> ERRORE eliminazione record ["+ " "+
                        cognome+" "+nome+" "+dataNascita+" "+data+" "+vaccino
                        +"...] dalla tabella '"+vaccinazioni.nome()+"'!"
                        );
                Logger.getLogger(DatiVaccinazioni.class.getName()).log(Level.SEVERE, null, ex);
                DB.chiudi();
                return false;
               
            }
            DB.chiudi();
            return true;
        } catch (EccezioneBaseDati ex) {
            _evento.adesso();
                System.out.println(
                        _evento+
                        " -> ERRORE connesione alla tabella '"+vaccinazioni.nome()+"'!"
                        );
            Logger.getLogger(DatiVaccinazioni.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }



     /******************************************************
      * Funzione che determina il numero di vaccinazioni
      * fatte per medici
      *
     * @param dataVaccinazione
      * @return una lista contenete nome medici e numero
      * vaccinazioni
      ******************************************************/
     public ArrayList<String[]> vaccinazioniMedico(DataOraria dataVaccinazione){
       
         ArrayList<String[]> conteggioVaccini = new ArrayList<String[]>();
         try {            
               

               Relazione sql_visita = new Relazione("anamnesi_seduta_vaccinale");
               sql_visita.creaAttributo("medico", STRINGA_40, null, NON_CHIAVE);
               sql_visita.creaAttributo("data vaccinazione", DATA_ORA,null, CHIAVE);

                this.DB.connetti();

                ArrayList<Object[]> visite =
                       DB.interrogazioneSempliceTabella(
                       sql_visita,
                       "[data vaccinazione] = #" + dataVaccinazione.stampaGiornoInverso()+ "# " +
                       "ORDER BY [medico]");
                this.DB.chiudi();

               String m = "";
                int indice =1;
                if(visite == null){
                    return new ArrayList<String[]>();
                }
                for(Object[] x: visite){
                   if(m.compareTo((String)x[0])==0){
                       indice++;

                   }else{
                       if(m.compareTo("")!=0)
                           conteggioVaccini.add(new String[]{m,String.valueOf(indice)});
                       indice = 1;
                       m = (String) x[0];
                   }

                }
                conteggioVaccini.add(new String[]{m,String.valueOf(indice)});
            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiVaccinazioni.class.getName()).log(Level.SEVERE, null, ex);
           
        }
        return conteggioVaccini;

     }

    public ArrayList<Object[]> elencoCoperturaVaccinale(String vaccinazione, String dose, int coperturaAnni, String corso, String compagnia) {
        ArrayList<Object[]> lista = new ArrayList<Object[]>();
        try {
            DB.connetti();
            DataOraria data = new DataOraria();
            data.oggi();
            data.anno(data.anno()-coperturaAnni);
            String limiteCopertura = data.stampaGiornoInverso();

            ArrayList<Object[]> listaVaccinazioni =
                    DB.interrogazioneSempliceTabella(
                    this.storiaVaccinale,
                        " [tipo profilassi] = '" + vaccinazione + "' AND" +
                        " [tipo dose] = '" + dose +"' AND "
                        + "[data vaccinazione] >= #"+ limiteCopertura+"#" );
            DB.chiudi();

              
            ArrayList<Object[]> militari = this.tuttaLaCompagnia(compagnia, corso);
            
            if(militari == null || listaVaccinazioni == null)
                return lista;
            for(Object[] vaccinato: listaVaccinazioni){
                for(int i=0; i< militari.size();i++){
                    String cognome = (String)militari.get(i)[0];
                    String nome = (String)militari.get(i)[1];
                    String nascita = ((DataOraria)militari.get(i)[2]).stampaGiorno();
                    if(
                            ((String)vaccinato[0]).compareTo(cognome)==0 &&
                            ((String)vaccinato[1]).compareTo(nome)==0 &&
                            ((DataOraria)vaccinato[2]).stampaGiorno().compareTo(nascita)==0){

                        lista.add(vaccinato);
                    }
                }
             }
            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiVaccinazioni.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return lista;
    }

    /***********************************************
     * 
     * @return 
     ***********************************************/
    public ArrayList<Object[]> numericoVacciniPerSeduta(DataOraria inizio,DataOraria fine) {  
        ArrayList<Object[]> vaccini =null;
        try {
            this.DB.connetti();        
            vaccini =
                    DB.interrogazioneSempliceTabella(vaccinazioni,
                    new Attributo[]{
                        new Attributo("seduta vaccinale",DATA_ORA,false),
                         new Attributo("tipo profilassi",STRINGA_40,false),
                         new Attributo("count(*)",new FunzioneSQL(3),false)
                     },
                    " [seduta vaccinale] >= #"+inizio.stampaGiornoInverso()+"# AND"+
                    " [seduta vaccinale] <= #"+fine.stampaGiornoInverso()+"# "
                    + "AND  ( "
                    + "[tipo profilassi] = 'POLIO' "
                    + "OR [tipo profilassi]='EPATITE A' "
                    + "OR [tipo profilassi] = 'Morbillo/Parotite/Rosolia' "
                    + "OR [tipo profilassi]='MENINGITE'  "
                    + "OR [tipo profilassi]='Tetano/Difterite/Polio'"
                    + "OR [tipo profilassi]='EPATITE B' ) "
                    + "Group By [seduta vaccinale], [tipo profilassi]"
                    );
             this.DB.chiudi();
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiVaccinazioni.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return vaccini;
    }

    public String[] dateSedute(Object[] militare) {        
        
        String[] date=null;
        try {
            if(militare == null) return null;
            this.DB.connetti();         
            ArrayList<Object[]> sedute =
                    DB.interrogazioneSempliceTabella(this.anamnesiSedutaVaccinale,
                    new Attributo[]{
                        new Attributo("data vaccinazione",DATA_ORA,false)                     
                     },
                    " [cognome] = '"+correggi((String)militare[0])+"' AND "
                    + "[nome] = '"+correggi((String)militare[1])+"' AND "
                    + "[data di nascita] = #"+((DataOraria)militare[2]).stampaGiornoInverso()+"# " );
            
             this.DB.chiudi();
             
             if(sedute == null){             
                 return null;
             }
             
             date = new String[sedute.size()];
             int i=0;
             String precedente = "";
             for(Object[] record : sedute){  
                String attuale = ((DataOraria)record[0]).stampaGiorno();
                if(attuale.compareTo(precedente)!= 0){
                    date[i++]= attuale;
                }
                precedente = attuale;
             }
             
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiVaccinazioni.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return date;
        
    }

    
    public int dimensioneSedutaVaccinale() {
        return this.anamnesiSedutaVaccinale.vediTuttiAttributi().size();
    }
    
     public int dimensioneReazioniVaccinali() {
        return this.anamnesiReazioneVaccinale.vediTuttiAttributi().size();
    }
      
      
    public int dimensioneRicovero() {
     return this.ricovero.vediTuttiAttributi().size();
    }

    public void modificaVaccinazioni(String cognome, String nome, DataOraria dataNascita, Object[] vaccinazione) {
        try {
            DB.connetti();
            
                ArrayList<Object[]> listaVaccinazioni = this.trovaVaccinazioni(cognome, nome, dataNascita);
                
                if(listaVaccinazioni != null) {
                    if(!listaVaccinazioni.isEmpty()) {
                        for(Object[] record: listaVaccinazioni){ 
                            
                            try {
                                DB.connetti();
                                try {
                                    DB.modificaTupla(
                                            vaccinazioni,
                                            new Object[]{record[0],record[1],cognome,nome,dataNascita},
                                            vaccinazione);  
                                    _evento.adesso();
                                    System.out.println(
                                            _evento+
                                            " -> modifica al record ["+ " "+
                                            cognome+" "+nome+" "+dataNascita+" "+record[0]+" "+record[1]+" "
                                            +"...] della tabella '"+vaccinazioni.nome()+"'");

                                } catch (EccezioneBaseDati ex) {
                                    _evento.adesso();
                                    System.out.println(
                                                        _evento+
                                                        " -> ERRORE modifica al record ["+ " "+
                                                        cognome+" "+nome+" "+dataNascita+" ...] della tabella '"+vaccinazioni.nome()+"'");
                                    Logger.getLogger(DatiVaccinazioni.class.getName()).log(Level.SEVERE, null, ex);

                                }
                                DB.chiudi(); 
                            } catch (EccezioneBaseDati ex) {
                                _evento.adesso();
                                    System.out.println(_evento+" -> ERRORE connessione alla tabella '"+vaccinazioni.nome()+"'");
                                Logger.getLogger(DatiVaccinazioni.class.getName()).log(Level.SEVERE, null, ex);
                            }
                
                            
                        }
                    }
                }
                 
            
            DB.chiudi();             
        } catch (EccezioneBaseDati ex) {
           
            Logger.getLogger(DatiVaccinazioni.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void modificaAnamnesiVaccinale(String cognome, String nome, DataOraria dataNascita, Object[] anamnesi) {        
        try {
            DB.connetti();
            
                ArrayList<Object[]> listaSedute = this.trovaAnamnesiVaccinale(cognome, nome, dataNascita);
                
                if(listaSedute != null) {
                    if(!listaSedute.isEmpty()) {
                        for(Object[] record: listaSedute){ 
                            try {
                                DB.connetti();
                                try {
                                    DB.modificaTupla(
                                            anamnesiSedutaVaccinale,
                                            new Object[]{cognome,nome,dataNascita,record[3]},
                                            anamnesi);  
                                    _evento.adesso();
                                    System.out.println(
                                        _evento+
                                        " -> modifica al record ["+ " "+
                                        cognome+" "+nome+" "+dataNascita+" "+record[3]
                                        +"...] della tabella '"+anamnesiSedutaVaccinale.nome()+"'!"
                                        );
                                } catch (EccezioneBaseDati ex) {
                                    _evento.adesso();
                                    System.out.println(
                                                    _evento+
                                                    " -> ERRORE modifica al record ["+ " "+
                                                    cognome+" "+nome+" "+dataNascita+" "
                                                    +"...] della tabella '"+anamnesiSedutaVaccinale.nome()+"'!"
                                                    );
                                    Logger.getLogger(DatiVaccinazioni.class.getName()).log(Level.SEVERE, null, ex);

                                }
                                DB.chiudi();             
                            } catch (EccezioneBaseDati ex) {
                                _evento.adesso();
                                    System.out.println(
                                                    _evento+
                                                    " -> ERRORE connessione alla tabella '"+anamnesiSedutaVaccinale.nome()+"'!"
                                                    );
                                Logger.getLogger(DatiVaccinazioni.class.getName()).log(Level.SEVERE, null, ex);
                            }
                                
                        }
                    }
                }
                 
            
            DB.chiudi();             
        } catch (EccezioneBaseDati ex) {
            
            Logger.getLogger(DatiVaccinazioni.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public void modificaAnamnesiReazioneVaccinale(String cognome, String nome, DataOraria dataNascita, Object[] reazioniVaccinazioni) {        
        try {
            DB.connetti();
            try {
                ArrayList<Object[]> listaSedute = this.trovaAnamnesiReazioneVaccinale(cognome, nome, dataNascita);
                
                if(listaSedute != null) {
                    if(!listaSedute.isEmpty())
                        for(Object[] record: listaSedute){ 
                            DB.modificaTupla(
                                    this.anamnesiReazioneVaccinale,
                                    new Object[]{cognome,nome,dataNascita,record[3],record[4]},
                                    reazioniVaccinazioni); 
                            _evento.adesso();
                            System.out.println(
                                    _evento+
                                    " -> modifica al record ["+ " "+
                                    cognome+" "+nome+" "+dataNascita+" "+record[3]+" "+record[4]
                                    +"...] della tabella '"+anamnesiReazioneVaccinale.nome()+"'!"
                                    );
                            
                        }
                }
                 
            } catch (EccezioneBaseDati ex) {
                _evento.adesso();
                System.out.println(
                            _evento+
                            " -> ERRORE modifica al record ["+ " "+
                            cognome+" "+nome+" "+dataNascita+" "
                            +"...] della tabella '"+anamnesiReazioneVaccinale.nome()+"'!"
                            );
                Logger.getLogger(DatiVaccinazioni.class.getName()).log(Level.SEVERE, null, ex);
                
            }
            DB.chiudi();             
        } catch (EccezioneBaseDati ex) {
            _evento.adesso();
                System.out.println(
                            _evento+
                            " -> ERRORE CONNESSIONE della tabella '"+anamnesiReazioneVaccinale.nome()+"'!"
                            );
            Logger.getLogger(DatiVaccinazioni.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Metodo che restituise una lista di record della tabella BASE_DATI delle reazioni ai vaccini.
     * 
     * @param cognome
     * @param nome
     * @param nato
     * @return 
     */
    public ArrayList<Object[]> trovaAnamnesiReazioneVaccinale(String cognome, String nome, DataOraria nato) {
        return this.trovaPerNominativo(cognome, nome, nato, anamnesiReazioneVaccinale);
        
    }
    
    /**
     * Metodo che permette di trovare tutti i dati sull'anamnesi delle varie sedute 
     * vaccinali di un militare.
     * 
     * @param cognome
     * @param nome
     * @param nato
     * @return 
     */
    public ArrayList<Object[]> trovaAnamnesiVaccinale(String cognome, String nome, DataOraria nato) {
        return this.trovaPerNominativo(cognome, nome, nato, anamnesiSedutaVaccinale);
        
    }

    public void modificaVaccinazione(String vaccino,DataOraria seduta,String cognome,String nome,
             DataOraria dataNascita,Object[]record){
        try {
            DB.connetti();
            try {
                
                DB.modificaTupla(vaccinazioni, new Object[]{seduta,vaccino,cognome,nome,dataNascita}, record);
                _evento.adesso();
                System.out.println(
                                _evento+
                                " -> modifica al record ["+ " "+
                                cognome+" "+nome+" "+dataNascita+" "+seduta+" "+vaccino+" "
                                +"...] della tabella '"+vaccinazioni.nome()+"'"
            
                                );
                
            } catch (EccezioneBaseDati ex) {
                _evento.adesso();
                System.out.println(
                                _evento+
                                " -> ERRORE modifica al record ["+ " "+
                                cognome+" "+nome+" "+dataNascita+" "+seduta+" "+vaccino+" "
                                +"...] della tabella '"+vaccinazioni.nome()+"'"
                                );
                Logger.getLogger(DatiVaccinazioni.class.getName()).log(Level.SEVERE, null, ex);
            }
            DB.chiudi();
        } catch (EccezioneBaseDati ex) {
            _evento.adesso();
                System.out.println(
                                _evento+
                                " -> ERRORE CONNESSIONE alla tabella '"+vaccinazioni.nome()+"'"
                                );
            Logger.getLogger(DatiVaccinazioni.class.getName()).log(Level.SEVERE, null, ex);
        }
             
    }

    public ArrayList<Object[]> elencoVaccinati(DataOraria data) {
        ArrayList<Object[]> vaccinati = null;
        try {
            DB.connetti();
            String query = String.format(
                       "SELECT DISTINCT m.`%s`, m.`%s`, m.`%s`, m.`%s` "
                     + "FROM %s a, %s m "
                     + "WHERE a.`%s` = m.`%s` AND  a.`%s` = m.`%s` AND a.`%s` = m.`%s` "
                     + "AND a.`%s` = '%s' AND a.`%s` IS NULL "
                     + "",
                     militare.nomeAttributo(4),//grado
                     militare.nomeAttributo(0),//cognome
                     militare.nomeAttributo(1),//nome
                     militare.nomeAttributo(2),//nascita
                     
                     
                     //FROM
                     vaccinazioni.nome(),
                     militare.nome(),
                     //WHERE
                     vaccinazioni.nomeAttributo(2), militare.nomeAttributo(0),//cognome
                     vaccinazioni.nomeAttributo(3), militare.nomeAttributo(1),//nome
                     vaccinazioni.nomeAttributo(4), militare.nomeAttributo(2),//nascita                
                     vaccinazioni.nomeAttributo(0),//data seduta
                     data.stampaGiornoInverso(),
                     vaccinazioni.nomeAttributo(12)//inadempienza
                     );
            
             Attributo[] colonne = {
                 militare.vediAttributo(4),//grado
                 militare.vediAttributo(0),//cognome
                 militare.vediAttributo(1),//nome
                 militare.vediAttributo(2) //nascita     
             };         
             vaccinati = DB.interrogazioneSQL(query, colonne);
             DB.chiudi();
             
             
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiVaccinazioni.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return vaccinati;
    }
    
    public Object[] trovaVaccinazioniUltima(String profilassi, String cognome, String nome, DataOraria nato) {
        try {
            DB.connetti();
            ArrayList<Object[]> _vaccinazioni =
                    DB.interrogazioneSempliceTabella(
                    this.vaccinazioni,
                    String.format(
                    " [cognome] = '%s' AND [nome] = '%s' "
                    + "AND [data di nascita] = #%s# AND [tipo profilassi] = '%s' "
                    + "ORDER BY [seduta vaccinale]",
                    correggi(cognome), correggi(nome),
                    nato.stampaGiornoInverso(),profilassi));
     
            DB.chiudi();
            if(_vaccinazioni!=null)
                if(!_vaccinazioni.isEmpty())
                    return _vaccinazioni.get(_vaccinazioni.size()-1);
            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiGenerici.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return null;
    }

    /***************************************************************************
     * Metodo che verifica l'esistenza della vaccinazione effettuata.
     * 
     * @since 1.1.0
     * @param profilassi
     * @param cognome
     * @param nome
     * @param nato
     * @return 
     **************************************************************************/
    public boolean esisteVaccinazione(String profilassi, String cognome, String nome, DataOraria nato) {
        return this.trovaVaccinazioniUltima(profilassi, cognome, nome, nato) != null;
    }

    /**
     * Metodo che permette di trovare le informazioni sull'anamenesi vaccinale 
     * di una specifica seduta effettuata da un militare.
     * 
     * @param cognome
     * @param nome
     * @param nascita
     * @param data
     * @return 
     */
    public Object[] trovaAnamnesiVaccinale(String cognome, String nome, DataOraria nascita, DataOraria data) {
        ArrayList<Object[]> lista = trovaAnamnesiVaccinale(cognome, nome, nascita);
        if(lista != null){
            for(Object[] voce: lista){
                Object dataVoce = voce[BASE_DATI.ANAMNESI_VACCINALE.DATA_VACCINAZIONE];
                if(dataVoce instanceof DataOraria){
                    if(data.compareTo((DataOraria) dataVoce) == 0)
                        return voce;
                }
            }
        }
        return null;
    }

    /**
     * Metodo che aggiunge o modificare i dati dell'anamnesi vaccinale.
     * 
     * @param cognome
     * @param nome
     * @param nascita
     * @param data
     * @param record 
     * @return  true se è stato aggiunto il record.
     */
    public boolean aggiungiAnamnesiVaccinale(String cognome, String nome, DataOraria nascita, DataOraria data, Object[] record) {
        record[BASE_DATI.ANAMNESI_VACCINALE.DATA_VACCINAZIONE] = data;
        if(!aggiungi(cognome, nome, nascita, record, anamnesiSedutaVaccinale)){
            return modifica(new Object[]{cognome, nome, nascita,data}, record, anamnesiSedutaVaccinale);
        }
        return true;
    }
    
    
    

}

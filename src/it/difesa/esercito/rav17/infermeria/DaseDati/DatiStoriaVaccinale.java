package it.difesa.esercito.rav17.infermeria.DaseDati;


import it.quasar_x7.java.BaseDati.Attributo;
import it.quasar_x7.java.BaseDati.EccezioneBaseDati;
import it.quasar_x7.java.BaseDati.FunzioneSQL;
import it.quasar_x7.java.BaseDati.Relazione;
import it.quasar_x7.java.utile.DataOraria;
import it.quasar_x7.java.utile.Errore;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


/*******************************************************************************
 *Implementa la struttura della base di dati infermeria MySQL
 *
 * @author Domenico della Peruta
 * @version 2.0.0 ultima modifica 01/04/2016
 ******************************************************************************/
public class DatiStoriaVaccinale extends Dati {
    

    private String[] profilassi = new String[]{
            "VAIOLO","TETANO","DIFTERITE","POLIO","MORBILLO","ROSOLIA",
            "PAROTITE","EPATITE B","VARICELLA","INFLUENZA","MENINGITE",
            "EPATITE A","FEBB. TIFO","COLERA","FEBB. GIALLA","RABBIA",
            "ENC. ZECC.","ENC. GIAP."
        };

//---------------------------- costruttore ------------------------------------
    /******************************************************
     * Il costruttore crea le varie entita tabelle con i
     * vari campi e domini e si connette al database
     *****************************************************/
    public DatiStoriaVaccinale() {
        super();
        
    }

    /**
     * Metodo chre cerca le vaccinazioni precedenti l'arruolamento di un militare.
     * 
     * @param cognome
     * @param nome
     * @param dataNascita
     * @param profilassi
     * @return restituisce un record della tabella storia vaccinale o null
     */ 
    public Object[] trovaStoriaVaccinale(String cognome,String nome,DataOraria dataNascita,String profilassi){
        Object[] vaccinazione = null;
         try {
            DB.connetti();
            
            vaccinazione = DB.vediTupla(
                       storiaVaccinale,
                       new Object[]{cognome, nome, dataNascita,profilassi});
            this.DB.chiudi();
            System.out.println("accesso tabella "+storiaVaccinale.nome()+
                    " [militare: "+cognome+" "+nome+" "+dataNascita.stampaGiorno()+"; profilassi: "+profilassi+"]\n");
           
            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiStoriaVaccinale.class.getName()).log(Level.SEVERE, null, ex);
            
        }
          return vaccinazione;
     }
     
    public ArrayList<Object[]> storiaVaccinale(String cognome,String nome,DataOraria dataNascita){
        ArrayList<Object[]> tabella = new ArrayList<Object[]>();
        try {
            DB.connetti();
            for (String vaccino : profilassi) {
                Object[] vaccinazione = DB.vediTupla(storiaVaccinale, new Object[]{cognome, nome, dataNascita, vaccino});
                if(vaccinazione != null){
                    tabella.add(vaccinazione);
                }
            }
            DB.chiudi();
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiStoriaVaccinale.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return tabella;
    }
     /***************************************************
      * Funzione che interroga la tabbella storia_vaccinale
      * ed estrapola i dati relativi a tutte le vaccinazioni
      * del militare
      *  
      * @deprecated 
      * @param cognome
      * @param nome
      * @param dataNascita
      * @return dati vaccinali dello storico
      ***************************************************/
     public Object[] trovaStoriaVaccinale(String cognome,String nome,DataOraria dataNascita){         
        
         Object[] record = new Object[18*7+6];
         try {
            DB.connetti();
            
            record[0]=cognome;
            record[1]=nome;
            record[2]=dataNascita;
            String _medico ="",_utente="";
            DataOraria _modifica = null;
           for(int i=0;i<profilassi.length;i++){
               Object[] vaccinazione = DB.vediTupla(
                       storiaVaccinale,
                       new Object[]{cognome, nome, dataNascita,profilassi[i]});

               if(vaccinazione == null){
                   return null;
               }
               for(int j=0;j<7;j++){
                    record[3+(7*i)+j]= vaccinazione[4+j];
               }
               _medico= (String) vaccinazione[11];
               _utente = (String) vaccinazione[12];
               _modifica =(DataOraria) vaccinazione[13];
               
           }
           record[129]=_medico;
           record[130] = _utente;
           record[131] = _modifica;
           DB.chiudi();
           
        } catch (EccezioneBaseDati ex) {
            ex.printStackTrace();
            //Logger.getLogger(DatiStoriaVaccinale.class.getName()).log(Level.SEVERE, null, ex);
            
        }
         return record;
     }


     /*******************************************************
      * Funzione che restituisce tutti i records di una tabella
     /*****************************************************
      * Determina il numero di militari a cui sono 
      * state registrate le vaccinazioni
      * @param corso
      * @param cp
      * @return 
      ****************************************************/
     public int numeroStoricoVaccinale(String corso,String cp){
        int n=0;
        try {
            DB.connetti();
            String SQL = String.format(
                      "SELECT COUNT(*) "
                    + "FROM %s, %s "
                    + "WHERE [%s] = '%s' AND [%s] = '%s' "
                    + "AND %s.[%s] = %s.[%s] AND %s.[%s] = %s.[%s] AND %s.[%s] = %s.[%s]",
                    militare.nome(),//from
                    storiaVaccinale.nome(),
                    militare.nomeAttributo(24),//corso
                    corso,
                    militare.nomeAttributo(6),
                    cp,
                    militare.nome(),
                    militare.nomeAttributo(0),
                    storiaVaccinale.nome(),
                    storiaVaccinale.nomeAttributo(0),
                    militare.nome(),                
                    militare.nomeAttributo(1),
                    storiaVaccinale.nome(),
                    storiaVaccinale.nomeAttributo(1),
                    militare.nome(),                
                    militare.nomeAttributo(2),
                    storiaVaccinale.nome(),
                    storiaVaccinale.nomeAttributo(2)
                    
                    );
            
            Attributo[] dati = {
                new Attributo("count(*)",new FunzioneSQL(1),false)
            };
            n = ((Long)DB.interrogazioneSQL(SQL, dati).get(0)[0]).intValue();
            DB.connetti();
             
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiStoriaVaccinale.class.getName()).log(Level.SEVERE, null, ex);
            
        }
         return n/18;
     }
     
     
     /**
      * 
      * @param corso
      * @return 
      */
     public ArrayList<Object[]> situazioneInserimentoDati(String corso){
        
          ArrayList<Object[]> attivitaInserimento =null;
         try {
            this.DB.connetti();
           
            String SQL = " militare.[corso] = '"+corso+"' AND "
                    + "storia_vaccinale.[tipo profilassi] = 'MORBILLO' AND "
                    + "storia_vaccinale.[cognome] = militare.[cognome] AND "
                    + "storia_vaccinale.[nome] = militare.[nome] AND "
                    + "storia_vaccinale.[data di nascita] = militare.[data di nascita] "
                    + " GROUP BY date(storia_vaccinale.modifica), militare.compagnia ";
            
            attivitaInserimento =
                   DB.interrogazioneJoin(
                   new Relazione[]{militare,storiaVaccinale},
                   new Attributo[]{
                       new Attributo("date(storia_vaccinale.modifica)",DATA_ORA,false),
                        new Attributo("compagnia",STRINGA_40,militare.nome(),false),
                        new Attributo("count(*)",new FunzioneSQL(3),false)
                    },
                   SQL);
            this.DB.chiudi();

            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiStoriaVaccinale.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return attivitaInserimento;

     }

      /*****************************************************
      * Modifica i campi del dati della tabella
      * storia_vaccinale
      *
      * @param cognome
      * @param nome
      * @param dataNascita
      * @param dati
      *****************************************************/
     public void modificaStoriaVaccinale(String cognome,String nome,DataOraria dataNascita,
             Object[]record) throws EccezioneBaseDati{
        
            
         
         DB.connetti();
         for(int i=0; i < profilassi.length;i++){
                Object[] vaccinazione=new Object[]{
                    record[0],record[1],record[2],//nominativo del militare
                       profilassi[i],//nome vaccinazione
                       //dati vaccinazione
                       record[3+(7*i)],record[4+(7*i)],record[5+(7*i)],record[6+(7*i)],
                       record[7+(7*i)],record[8+(7*i)],record[9+(7*i)],
                       record[129], //medico
                       record[130], //utente
                       record[131] //modifica

                };
                DB.modificaTupla(
                        storiaVaccinale,
                        new Object[]{cognome,nome,dataNascita,profilassi[i]},
                        vaccinazione);
                _evento.adesso();
                System.out.println(
                        _evento+" -> modifica record ["+ " "+
                                        cognome+" "+nome+" "+dataNascita+" "+profilassi[i]+
                                "...] nella tabella '"+storiaVaccinale.nome()+"'."
                                        );
         }  
         DB.chiudi();
     }

     /*****************************************************
      * Crea nuovi dati nella tabella storia
      *
      * @param cognome
      * @param nome
      * @param dataNascita
     * @param record i record[0],record[1],record[2] vengono
      * sovrascitti da cognome, nome e dataNascita
      *****************************************************/
     public void aggiungiStoriaVaccinale(String cognome,String nome,DataOraria dataNascita,
             Object[]record){
        try {
            DB.connetti();
             try {
               
               for(int i=0; i < profilassi.length;i++){
                   Object[] vaccinazione=new Object[]{
           
                       cognome,nome,dataNascita,//nominativo del militare              /**/
                       profilassi[i],//nome vaccinazione
                       //dati vaccinazione
                       record[3+(7*i)],record[4+(7*i)],record[5+(7*i)],record[6+(7*i)],
                       record[7+(7*i)],record[8+(7*i)],record[9+(7*i)],
                       record[129], //medico
                       record[130], //utente
                       record[131], //modifica
                   };
                   DB.aggiungiTupla(storiaVaccinale, vaccinazione);
                   _evento.adesso();
                    System.out.println(
                    _evento+" -> aggiunto record ["+ " "+
                                    cognome+" "+nome+" "+dataNascita+" "+profilassi[i]+
                            "...] nella tabella '"+storiaVaccinale.nome()+"'."
                                    );
                   
               }
               
            } catch (EccezioneBaseDati ex) {
                _evento.adesso();
                    System.out.println(
                    _evento+" -> ERRORE nell'aggiungere il record ["+ " "+
                                    cognome+" "+nome+" "+dataNascita+"...] nella tabella '"+storiaVaccinale.nome()+"'."
                                    );
                Logger.getLogger(DatiStoriaVaccinale.class.getName()).log(Level.SEVERE, null, ex);
                
            }
             DB.chiudi();
        } catch (EccezioneBaseDati ex) {
            _evento.adesso();
            System.out.println(
                    _evento+" -> ERRORE connessione alla tabella '"+storiaVaccinale.nome()+"'."
                                    );
            Logger.getLogger(DatiStoriaVaccinale.class.getName()).log(Level.SEVERE, null, ex);
            
        }
     }
     
     /**
      * Aggiunge una singola voce di profilassi alla tabella dello storico vaccinale.
      * 
      * @param cognome
      * @param nome
      * @param dataNascita
      * @param profilassi
      * @param pregressa
      * @param pregressaDoc
      * @param ultimaVaccinazioneCivile di tipo Boolean perché può assumere tre valori:
      *  null, quando non è ne militare ne civile;
      *  true, quando è civile;
      *  false, quando è militare.
      * @param dose
      * @param vaccino
      * @param data
      * @param medico
      * @param utente 
      */
     public void aggiungiStoriaVaccinale(String cognome,String nome,DataOraria dataNascita,String profilassi, 
             boolean pregressa,boolean pregressaDoc,Boolean ultimaVaccinazioneCivile, String dose, String vaccino,
             DataOraria data,String medico,String utente){
         
        DataOraria modifica = new DataOraria();
        modifica.adesso();
        Object[] record = new Object[]{
            cognome,nome,dataNascita,//nominativo del militare        
            profilassi,//nome vaccinazione
            pregressa,pregressaDoc,
            ultimaVaccinazioneCivile == null ? null : ultimaVaccinazioneCivile ? "D" : "",
            ultimaVaccinazioneCivile == null ? null : !ultimaVaccinazioneCivile ? "D" : "",
            dose,
            vaccino,
            data,
            medico,
            utente,
            modifica
        };
        Object[] chiave = new Object[]{
            cognome,nome,dataNascita,      
            profilassi
        };
        try {
            DB.connetti();
            
            DB.aggiungiTupla(
                    storiaVaccinale, 
                    record
            );
            DB.chiudi();
        } catch (EccezioneBaseDati ex) {
            if(ex.getMessage().contains("Duplicate entry")){
                try {
                    DB.modificaTupla(storiaVaccinale, chiave, record);
                    DB.chiudi();
                } catch (EccezioneBaseDati ex1) {
                    Logger.getLogger(DatiStoriaVaccinale.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }else
                Logger.getLogger(DatiStoriaVaccinale.class.getName()).log(Level.SEVERE, null, ex);
        }
         
     }
    
    /**
     * Elimina una singola voce dello storico vaccinale dell militare in oggetto.
     * 
     * @param cognome
     * @param nome
     * @param dataNascita
     * @param profilassi 
     */
    public void eliminaStoriaVaccinale(String cognome,String nome,DataOraria dataNascita,String profilassi){
        try {
            DB.connetti();            
            try {
                DB.eliminaTupla(
                            storiaVaccinale, 
                            new Object[]{
                                cognome, 
                                nome, 
                                dataNascita,
                                profilassi
                            }
                    );
                    _evento.adesso();
                    System.out.println(
                        _evento+" -> eliminazione record ["+ " "+
                                        cognome+" "+nome+" "+dataNascita+" "+profilassi+
                                "...] nella tabella '"+storiaVaccinale.nome()+"'."
                                        );

                
                
            } catch (EccezioneBaseDati ex) {
                _evento.adesso();
                    System.out.println(
                        _evento+" -> ERRORE eliminazione record ["+ " "+
                                        cognome+" "+nome+" "+dataNascita+" "+profilassi+
                            " ...] nella tabella '"+storiaVaccinale.nome()+"'."
                                        );
                Logger.getLogger(DatiStoriaVaccinale.class.getName()).log(Level.SEVERE, null, ex);
                
            }
            DB.chiudi();            
        } catch (EccezioneBaseDati ex) {
             _evento.adesso();
            System.out.println(
                    _evento+" -> ERRORE connessione alla tabella '"+storiaVaccinale.nome()+"'."
                                    );
            Logger.getLogger(DatiStoriaVaccinale.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /******************************************************
     * Elimina il dati indicato in storia_vaccinale
     * 
     * @param cognome
     * @param nome
     * @param dataNascita
     * @throws Errore stringa data di nascita
     ******************************************************/
    public void eliminaStoriaVaccinale(String cognome, String nome, String dataNascita) throws Errore {
        try {
            DB.connetti();            
            try {
                

                for(int i=0; i < profilassi.length;i++){
                    
                    DB.eliminaTupla(
                            storiaVaccinale, 
                            new Object[]{
                                cognome, 
                                nome, 
                                new DataOraria(dataNascita),
                                profilassi[i]}
                    );
                    _evento.adesso();
                    System.out.println(
                        _evento+" -> eliminazione record ["+ " "+
                                        cognome+" "+nome+" "+dataNascita+" "+profilassi[i]+
                                "...] nella tabella '"+storiaVaccinale.nome()+"'."
                                        );
                    
                }
                
            } catch (EccezioneBaseDati ex) {
                _evento.adesso();
                    System.out.println(
                        _evento+" -> ERRORE eliminazione record ["+ " "+
                                        cognome+" "+nome+" "+dataNascita+
                            " ...] nella tabella '"+storiaVaccinale.nome()+"'."
                                        );
                Logger.getLogger(DatiStoriaVaccinale.class.getName()).log(Level.SEVERE, null, ex);
                
            }
            DB.chiudi();            
        } catch (EccezioneBaseDati ex) {
             _evento.adesso();
            System.out.println(
                    _evento+" -> ERRORE connessione alla tabella '"+storiaVaccinale.nome()+"'."
                                    );
            Logger.getLogger(DatiStoriaVaccinale.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int dimensioneStoriaMilitare() {
        return this.storiaVaccinale.vediTuttiAttributi().size();
    }

    public ArrayList<Object[]> trovaStoriaVaccinale(String compagnia,String corso, String profilassi){
        ArrayList<Object[]> vaccinazione =null;
        try {
            DB.connetti();         
             vaccinazione = DB.interrogazioneJoin(
                     new Relazione[]{militare,storiaVaccinale}, 
                     storiaVaccinale.vediTuttiAttributi().toArray(new Attributo[storiaVaccinale.vediTuttiAttributi().size()]),
                      "militare.[compagnia] = '"+compagnia+"' AND "
                     + "militare.[corso] = '"+corso+"' AND "
                     + "storia_vaccinale.[tipo profilassi] = '"+profilassi+"' AND "
                     + "storia_vaccinale.[cognome] = militare.[cognome] AND "
                     + "storia_vaccinale.[nome] = militare.[nome] AND "
                     + "storia_vaccinale.[data di nascita] = militare.[data di nascita] ");
             
             this.DB.chiudi(); 
             
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiStoriaVaccinale.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return vaccinazione;
    }
    
    
    
    public String[] profilassi(){
        return profilassi;
    }
    

}

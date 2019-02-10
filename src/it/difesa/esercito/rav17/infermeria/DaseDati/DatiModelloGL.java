/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package it.difesa.esercito.rav17.infermeria.DaseDati;

import it.quasar_x7.java.BaseDati.Attributo;
import it.quasar_x7.java.BaseDati.EccezioneBaseDati;
import it.quasar_x7.java.BaseDati.Relazione;
import it.quasar_x7.java.utile.DataOraria;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ninja
 */
public class DatiModelloGL extends Dati{

    public DatiModelloGL() {
        super();
    }
    
    public ArrayList<Object[]> trovaModelloGL(String cognome, String nome, DataOraria nato) {
        ArrayList<Object[]> modello =null;
        try {
            DB.connetti();
            
            modello =
                    DB.interrogazioneSempliceTabella(
                    modelloGL,
                    String.format(
                    " [cognome] = '%s' AND [nome] = '%s' "
                    + "AND [data di nascita] = #%s# ",
                    correggi(cognome), correggi(nome), nato.stampaGiornoInverso()));

            DB.chiudi();
            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiModelloGL.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return modello;
    }

    public ArrayList<Object[]> trovaCausaDiServizio(String cognome, String nome, DataOraria nato) {
        ArrayList<Object[]> elencoCauseDiServizio =null;
        try {
            DB.connetti();
            
            elencoCauseDiServizio =
                    DB.interrogazioneSempliceTabella(
                    causaServizio,
                    String.format(
                    " [cognome] = '%s' AND [nome] = '%s' "
                    + "AND [data di nascita] = #%s# ",
                    correggi(cognome), correggi(nome), nato.stampaGiornoInverso()));

            DB.chiudi();
            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiModelloGL.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return elencoCauseDiServizio;
    }
    
    public int dimensioneModelloGL() {
        return this.modelloGL.vediTuttiAttributi().size();
    }

    /**
     * 
     * @param data
     * @param cognome
     * @param nome
     * @param dataNascita
     * @return 
     */
    public boolean eliminaModelloGL(DataOraria data, String cognome, String nome, DataOraria dataNascita) {
        boolean errore = false;
        try {
            DB.connetti();
           DB.eliminaTupla(
                       modelloGL,
                        new Object[]{cognome,nome,dataNascita,data});
           
           DB.chiudi();
          
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiModelloGL.class.getName()).log(Level.SEVERE, null, ex);
            errore = true;
        }
        return !errore;
    }
    public void eliminaCausaDiServizio( String cognome, String nome, DataOraria dataNascita,String infermita) {
        try {
            DB.connetti();
           DB.eliminaTupla(
                       causaServizio,
                        new Object[]{cognome,nome,dataNascita,infermita});
           
           DB.chiudi();
          
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiModelloGL.class.getName()).log(Level.SEVERE, null, ex);
            
        }
    }

    public Object[] vediModelloGL(DataOraria data, String cognome, String nome, DataOraria dataNascita){
        Object[] x =null;
        try {
            DB.connetti();
            // campo del dati
            x = DB.vediTupla(
                    modelloGL, new Object[]{
                cognome, nome, dataNascita,data});
            DB.chiudi();
            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiModelloGL.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        
        return x;
    }
    
    public void aggiungiModelloGL(DataOraria data, String cognome, String nome, DataOraria dataNascita, Object[] record){
                
        try {
            record[0]=cognome;
            record[1]=nome;
            record[2]=dataNascita;
            record[3]=data;

            DB.connetti();
            try{
                DB.modificaTupla(modelloGL, new Object[]{cognome,nome,dataNascita,data},record);
            }catch(EccezioneBaseDati e){
                DB.aggiungiTupla(modelloGL,record);
            }
            
            DB.chiudi();
            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiModelloGL.class.getName()).log(Level.SEVERE, null, ex);
           
        }
    }
    
    public void aggiungiCausaDiServizio( String cognome, String nome, DataOraria dataNascita,String infermita, Object[] record){
                
        try {
            record[0]=cognome;
            record[1]=nome;
            record[2]=dataNascita;
            record[3]=infermita;

            DB.connetti();
            try{
                DB.eliminaTupla(causaServizio, new Object[]{cognome,nome,dataNascita,infermita});
            }catch(EccezioneBaseDati e){
                
            }
            DB.aggiungiTupla(causaServizio,record);
            DB.chiudi();
            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiModelloGL.class.getName()).log(Level.SEVERE, null, ex);
           
        }
    }

    public int dimensioneCausaDiServizio() {
        return causaServizio.vediTuttiAttributi().size();
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
    public ArrayList<Object[]> trovaVerbaliGL(DataOraria data) {
        ArrayList<Object[]> _verbale =null;
        try {
            DB.connetti();
            
            _verbale =
                    DB.interrogazioneJoin(
                        new Relazione[]{militare,modelloGL},
                        new Attributo[]{
                            militare.vediAttributo(BASE_DATI.MILITARE.GRADO),
                            militare.vediAttributo(BASE_DATI.MILITARE.COGNOME),
                            militare.vediAttributo(BASE_DATI.MILITARE.NOME),
                            militare.vediAttributo(BASE_DATI.MILITARE.DATA_NASCITA),
                            militare.vediAttributo(BASE_DATI.MILITARE.COMPAGNIA),
                            modelloGL.vediAttributo(BASE_DATI.MODELLO_GL.DIAGNOSI)
                        },
                        String.format(
                                  " `%s`.`%s` = `%s`.`%s` AND `%s`.`%s` = `%s`.`%s` AND `%s`.`%s` = `%s`.`%s` "
                                + " AND `%s` = '%s' ",
                                modelloGL.nome(), modelloGL.nomeAttributo(0),  militare.nome(), militare.nomeAttributo(0),
                                modelloGL.nome(), modelloGL.nomeAttributo(1),  militare.nome(), militare.nomeAttributo(1),
                                modelloGL.nome(), modelloGL.nomeAttributo(2),  militare.nome(), militare.nomeAttributo(2),
                                modelloGL.nomeAttributo(3),data.stampaGiornoInverso()
                        )
                    );

            DB.chiudi();
            
        } catch (EccezioneBaseDati ex) {
            Logger.getLogger(DatiModelloGL.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return _verbale;
    }
    
}

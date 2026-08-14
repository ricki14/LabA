package cinemax;

import java.io.*;
import java.time.LocalDate;
import java.util.LinkedList;

public class GestoreUtenti {

    //campi
    /*utilizziamo 2 variabili: una è la lista da salvare,
     l'altra specifica dove salvarla */

    private String percorsoFile;
    private LinkedList<Utente> utenti;

    //costruttore
    public GestoreUtenti(LinkedList<Utente> utenti){
        this.utenti=utenti;
        this.percorsoFile="data/utenti.csv";
    }

    //metodi
    public void salvaUtenti(){

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(percorsoFile))){
            writer.write("nome;cognome;username;password;dataDiNascita;" +
                    "via;numeroCivico;cap;citta;provincia;ruolo");
            writer.newLine();

            for(Utente tmp:utenti){

                Domicilio dom = tmp.getDomicilio();

                String via = (dom != null) ? dom.getVia() : "";
                String numCivico = (dom != null) ? dom.getNumeroCivico() : "";
                String cap = (dom != null) ? dom.getCap() : "";
                String citta = (dom != null) ? dom.getCitta() : "";
                String prov = (dom != null) ? dom.getProvincia() : "";

                writer.write(tmp.getNome() + ";" +
                                tmp.getCognome() + ";" +
                                tmp.getUsername() + ";" +
                                tmp.getPassword() + ";" +
                                tmp.getDataDiNascita() + ";" +
                                via + ";" +
                                numCivico + ";" +
                                cap + ";" +
                                citta + ";" +
                                prov + ";" +
                                tmp.getRuolo()
                );
                writer.newLine();
            }

        } catch(IOException e){
            System.out.println("Errore nel salvataggio degli utenti.");
        }

    }

    public void caricaUtenti(){
        try(BufferedReader reader = new BufferedReader(new FileReader(percorsoFile))){

            String riga = reader.readLine();

            while((riga=reader.readLine())!=null){

                if(riga.trim().isEmpty()){
                    continue;
                }

                String[] dati = riga.split(";", -1);

                if(dati.length>=11){
                    String nome = dati[0];
                    String cognome = dati[1];
                    String username = dati[2];
                    String password = dati[3];
                    LocalDate dataDiNascita = LocalDate.parse(dati[4]);

                    String via = dati[5];
                    String numCivico = dati[6];
                    String cap = dati[7];
                    String citta = dati[8];
                    String prov = dati[9];

                    String ruoloString = dati[10];

                    Domicilio domicilio = null;
                    if(!via.isEmpty() || !citta.isEmpty()){
                        domicilio = new Domicilio(via, numCivico, cap, citta, prov);
                    }

                    Ruolo ruolo = Ruolo.valueOf(ruoloString.toUpperCase());

                    Utente utente = null;
                    if(ruolo==Ruolo.CLIENTE){
                        utente=new ClienteRegistrato(nome, cognome, username, password,
                                dataDiNascita, domicilio, ruolo);
                    } else if(ruolo==Ruolo.BIGLIETTAIO){
                        utente=new Bigliettaio(nome, cognome, username, password,
                                dataDiNascita, domicilio);
                    }
                    if(utente!=null){
                        utenti.add(utente);
                    }
                }

            }
            System.out.println("Caricamento utenti completato con successo.");
        } catch(IOException e){
            System.out.println("Errore nella lettura del file degli utenti: " + e.getMessage());
        } catch(Exception e){
            System.out.println("Errore nel parsing dei dati degli utenti: " + e.getMessage());
        }
    }
    public LinkedList<Utente> getUtenti(){
        return utenti;
    }

}

package cinemax;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;

public class ClienteRegistrato extends Utente{

    //campi
    private LinkedList<Prenotazione> prenotazioniCliente;

    //costruttore con data di nascita
    public ClienteRegistrato (String nome, String cognome, String username, String password,
                              LocalDate dataDiNascita, Domicilio domicilio, Ruolo ruolo){
        super(nome, cognome, username, password, dataDiNascita, domicilio, Ruolo.CLIENTE);
        this.prenotazioniCliente=new LinkedList<Prenotazione>();
    }

    //costruttore senza data di nascita
    public ClienteRegistrato (String nome, String cognome, String username, String password,
                              Domicilio domicilio, Ruolo ruolo){
        super(nome, cognome, username, password, domicilio, Ruolo.CLIENTE);
        this.prenotazioniCliente=new LinkedList<Prenotazione>();
    }

    //metodi
    public String visualizzaPrenotazione(){
        if(prenotazioniCliente == null || prenotazioniCliente.isEmpty()) {
            return "Nessuna prenotazione trovata";
        }
        String prov = "";
        for(Prenotazione tmp : prenotazioniCliente){
            prov = prov + tmp.toString() + " | \n";
            }
        String output = "Prenotazioni a nome "  +this.getNome() +
                        " " +this.getCognome()+ ": " + prov;
        return output;
    }

    public String creaPrenotazione(Proiezione proiezione, LinkedList<Posto> postiPrenotati){
        if (proiezione == null || postiPrenotati == null || postiPrenotati.isEmpty()){
            return "Operazione non riuscita: proiezione o posti non validi";
        }
        Prenotazione nuovaPrenotazione = new Prenotazione(this,
                                                    proiezione, postiPrenotati);
        this.prenotazioniCliente.add(nuovaPrenotazione);
        return "Prenotazione " +nuovaPrenotazione.getIdPrenotazione()+ "completata";
    }

    public boolean modificaPrenotazione(Prenotazione modPrenotazione, LocalDateTime nuovaDataOra,
                                     ProgrammazioneCinema programmazione){

        if(nuovaDataOra.isBefore(LocalDateTime.now())) {
           System.out.println("Non è possibile cambiare la data attuale " +
                   "con una nuova data precedente a data odierna");
           return false;
       }

        if(modPrenotazione == null || !prenotazioniCliente.contains(modPrenotazione)) {
           System.out.println("La prenotazione che si vuole modificare " +
                   "non è presente tra le tue prenotazioni!");
           return false;
       }

        Proiezione nuovaProiezione = null;

        for(Proiezione tmp : programmazione.getElencoProiezioni()){
            if(tmp.getFilm().getTitolo().equalsIgnoreCase(modPrenotazione.getProiezione().getFilm().getTitolo()) &&
                tmp.getDataOra().equals(nuovaDataOra)){
                nuovaProiezione=tmp;
                break;
            }
        }
        if(nuovaProiezione==null){
            System.out.println("Nessuna proiezione del film " +modPrenotazione.getProiezione().getFilm().getTitolo()+
                    " è disponibile nella data " + nuovaDataOra);
            return false;
        }

        modPrenotazione.annullaPrenotazione();
        Prenotazione nuovaPrenotazione = new Prenotazione(this, nuovaProiezione,
                                                             modPrenotazione.getPostiPrenotati());
        prenotazioniCliente.remove(modPrenotazione);
        prenotazioniCliente.add(nuovaPrenotazione);
        System.out.println("Prenotazione modificata con successo!");
        return true;
    }

    public boolean eliminaPrenotazione(Prenotazione eliminPren){

        if (eliminPren == null || !prenotazioniCliente.contains(eliminPren)){
            System.out.println("La prenotazione che si vuole eliminare non è " +
                    "presente nella lista delle tue prenotazioni");
            return false;
        }

        LocalDateTime dataProiezione = eliminPren.getProiezione().getDataOra();
        if (dataProiezione.isBefore(LocalDateTime.now())){
            System.out.println("Non è possibile eliminare la prenotazione " +
                                "per una proiezione già avvenuta.");
            return false;
        }

        eliminPren.annullaPrenotazione();
        prenotazioniCliente.remove(eliminPren);
        System.out.println("La prenotazione è stata eliminata");
        return true;
    }
}

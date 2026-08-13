package cinemax;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;

/**
 * Rappresenta un cliente registrato del sistema Cinemax.
 *
 * <p>La classe estende {@link Utente} e identifica un utente con ruolo
 * di cliente. Il cliente registrato può essere creato specificando
 * oppure omettendo la data di nascita.</p>
 *
 * <p>Gestisce la lista delle prenotazioni effettuate dall'utente tramite
 * le funzionalità di creazione, visualizzazione, modifica ed eliminazione.</p>
 *
 * @author Riccardo, Edoardo
 * @version 1.0
 */
public class ClienteRegistrato extends Utente {

    /** Lista delle prenotazioni effettuate dal cliente. */
    private LinkedList<Prenotazione> prenotazioniCliente;

    /**
     * Costruisce un nuovo cliente registrato specificando la data di nascita.
     *
     * <p>Il ruolo viene impostato automaticamente a {@link Ruolo#CLIENTE}.</p>
     *
     * @param nome          il nome del cliente
     * @param cognome       il cognome del cliente
     * @param username      il nome utente utilizzato per l'accesso
     * @param password      la password utilizzata per l'accesso
     * @param dataDiNascita la data di nascita del cliente
     * @param domicilio     il domicilio del cliente
     * @param ruolo         il ruolo dell'utente
     */
    public ClienteRegistrato(String nome, String cognome, String username,
                             String password, LocalDate dataDiNascita,
                             Domicilio domicilio, Ruolo ruolo) {
        super(nome, cognome, username, password, dataDiNascita, domicilio, Ruolo.CLIENTE);
        this.prenotazioniCliente = new LinkedList<Prenotazione>();
    }

    /**
     * Costruisce un nuovo cliente registrato senza specificare la data di nascita.
     *
     * <p>Il ruolo viene impostato automaticamente a {@link Ruolo#CLIENTE}.</p>
     *
     * @param nome      il nome del cliente
     * @param cognome   il cognome del cliente
     * @param username  il nome utente utilizzato per l'accesso
     * @param password  la password utilizzata per l'accesso
     * @param domicilio il domicilio del cliente
     * @param ruolo     il ruolo dell'utente
     */
    public ClienteRegistrato(String nome, String cognome, String username,
                             String password, Domicilio domicilio,
                             Ruolo ruolo) {
        super(nome, cognome, username, password, domicilio, Ruolo.CLIENTE);
        this.prenotazioniCliente = new LinkedList<Prenotazione>();
    }

    /**
     * Visualizza la lista delle prenotazioni attive a nome del cliente.
     *
     * @return Una stringa contenente l'elenco delle prenotazioni o un messaggio se non ve ne sono.
     */
    public String visualizzaPrenotazione() {
        if (prenotazioniCliente == null || prenotazioniCliente.isEmpty()) {
            return "Nessuna prenotazione trovata";
        }
        String prov = "";
        for (Prenotazione tmp : prenotazioniCliente) {
            prov = prov + tmp.toString() + " | \n";
        }
        return "Prenotazioni a nome " + this.getNome() + " " + this.getCognome() + ": " + prov;
    }

    /**
     * Crea e aggiunge una nuova prenotazione alla lista del cliente.
     *
     * @param proiezione     la proiezione da prenotare
     * @param postiPrenotati i posti selezionati
     * @return Esito dell'operazione di prenotazione.
     */
    public String creaPrenotazione(Proiezione proiezione, LinkedList<Posto> postiPrenotati) {
        if (proiezione == null || postiPrenotati == null || postiPrenotati.isEmpty()) {
            return "Operazione non riuscita: proiezione o posti non validi";
        }
        Prenotazione nuovaPrenotazione = new Prenotazione(this, proiezione, postiPrenotati);
        this.prenotazioniCliente.add(nuovaPrenotazione);
        return "Prenotazione " + nuovaPrenotazione.getIdPrenotazione() + " completata";
    }

    /**
     * Modifica la data/ora di una prenotazione esistente cercandone una nuova valida nel programma del cinema.
     *
     * @param modPrenotazione  la prenotazione da modificare
     * @param nuovaDataOra     la nuova data e ora richieste
     * @param programmazione   il palinsesto delle proiezioni
     * @return true se la modifica va a buon fine, false altrimenti.
     */
    public boolean modificaPrenotazione(Prenotazione modPrenotazione, LocalDateTime nuovaDataOra,
                                        ProgrammazioneCinema programmazione) {

        if (nuovaDataOra.isBefore(LocalDateTime.now())) {
            System.out.println("Non è possibile cambiare la data attuale con una nuova data precedente a data odierna");
            return false;
        }

        if (modPrenotazione == null || !prenotazioniCliente.contains(modPrenotazione)) {
            System.out.println("La prenotazione che si vuole modificare non è presente tra le tue prenotazioni!");
            return false;
        }

        Proiezione nuovaProiezione = null;

        for (Proiezione tmp : programmazione.getElencoProiezioni()) {
            if (tmp.getFilm().getTitolo().equalsIgnoreCase(modPrenotazione.getProiezione().getFilm().getTitolo()) &&
                    tmp.getDataOra().equals(nuovaDataOra)) {
                nuovaProiezione = tmp;
                break;
            }
        }
        if (nuovaProiezione == null) {
            System.out.println("Nessuna proiezione del film " + modPrenotazione.getProiezione().getFilm().getTitolo() +
                    " è disponibile nella data " + nuovaDataOra);
            return false;
        }

        modPrenotazione.annullaPrenotazione();
        Prenotazione nuovaPrenotazione = new Prenotazione(this, nuovaProiezione, modPrenotazione.getPostiPrenotati());
        prenotazioniCliente.remove(modPrenotazione);
        prenotazioniCliente.add(nuovaPrenotazione);
        System.out.println("Prenotazione modificata con successo!");
        return true;
    }

    /**
     * Elimina una prenotazione del cliente, annullandola se la proiezione non è ancora avvenuta.
     *
     * @param eliminPren la prenotazione da eliminare
     * @return true se l'eliminazione ha successo, false altrimenti.
     */
    public boolean eliminaPrenotazione(Prenotazione eliminPren) {

        if (eliminPren == null || !prenotazioniCliente.contains(eliminPren)) {
            System.out.println("La prenotazione che si vuole eliminare non è presente nella lista delle tue prenotazioni");
            return false;
        }

        LocalDateTime dataProiezione = eliminPren.getProiezione().getDataOra();
        if (dataProiezione.isBefore(LocalDateTime.now())) {
            System.out.println("Non è possibile eliminare la prenotazione per una proiezione già avvenuta.");
            return false;
        }

        eliminPren.annullaPrenotazione();
        prenotazioniCliente.remove(eliminPren);
        System.out.println("La prenotazione è stata eliminata");
        return true;
    }
}
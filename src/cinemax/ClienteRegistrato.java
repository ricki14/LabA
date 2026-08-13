package cinemax;

import java.time.LocalDate;
import java.util.LinkedList;

/**
 * Rappresenta un cliente registrato del sistema Cinemax.
 *
 * <p>La classe estende {@link Utente} e identifica un utente con ruolo
 * di cliente. Il cliente registrato può essere creato specificando
 * oppure omettendo la data di nascita.</p>
 *
 * <p>La classe prevede inoltre i metodi necessari per la gestione
 * delle prenotazioni, attualmente non ancora implementati.</p>
 *
 * @author Riccardo
 * @version 1.0
 */
public class ClienteRegistrato extends Utente {

    /**
     * Costruisce un nuovo cliente registrato specificando la data
     * di nascita.
     *
     * <p>Il ruolo viene impostato automaticamente a
     * {@link Ruolo#CLIENTE}.</p>
     *
     * @param nome il nome del cliente
     * @param cognome il cognome del cliente
     * @param username il nome utente utilizzato per l'accesso
     * @param password la password utilizzata per l'accesso
     * @param dataDiNascita la data di nascita del cliente
     * @param domicilio il domicilio del cliente
     * @param ruolo il ruolo dell'utente
     */
    public ClienteRegistrato(String nome, String cognome, String username,
                             String password, LocalDate dataDiNascita,
                             Domicilio domicilio, Ruolo ruolo) {
        super(nome, cognome, username, password,
                dataDiNascita, domicilio, Ruolo.CLIENTE);
    }

    /**
     * Costruisce un nuovo cliente registrato senza specificare
     * la data di nascita.
     *
     * <p>Il ruolo viene impostato automaticamente a
     * {@link Ruolo#CLIENTE}.</p>
     *
     * @param nome il nome del cliente
     * @param cognome il cognome del cliente
     * @param username il nome utente utilizzato per l'accesso
     * @param password la password utilizzata per l'accesso
     * @param domicilio il domicilio del cliente
     * @param ruolo il ruolo dell'utente
     */
    public ClienteRegistrato(String nome, String cognome, String username,
                             String password, Domicilio domicilio,
                             Ruolo ruolo) {
        super(nome, cognome, username, password,
                domicilio, Ruolo.CLIENTE);
    }

    /**
     * Crea una nuova prenotazione per il cliente.
     *
     * <p>Il metodo è attualmente predisposto ma non ancora implementato.</p>
     */
    public void creaPrenotazione() {

    }

    /**
     * Visualizza le prenotazioni del cliente.
     *
     * <p>Il metodo è attualmente predisposto ma non ancora implementato.</p>
     */
    public void visualizzaPrenotazione() {

    }

    /**
     * Modifica una prenotazione del cliente.
     *
     * <p>Il metodo è attualmente predisposto ma non ancora implementato.</p>
     */
    public void modificaPrenotazione() {

    }

    /**
     * Elimina una prenotazione del cliente.
     *
     * <p>Il metodo è attualmente predisposto ma non ancora implementato.</p>
     */
    public void eliminaPrenotazione() {

    }
}
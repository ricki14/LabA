/*
 * Edoardo Carducci - 764215 - Varese
 * Daniele Rossetti - 767980 - Varese
 * Riccardo Palomba - 764224 - Varese
 */

package cinemax;

import java.util.LinkedList;
import java.util.List;

/**
 * Rappresenta il sistema principale del cinema.
 *
 * <p>La classe si occupa di inizializzare e gestire i dati principali
 * necessari al funzionamento del sistema, tra cui gli utenti,
 * le proiezioni, le prenotazioni e la programmazione del cinema.</p>
 *
 * @author Edoardo Carducci, Daniele Rossetti
 * @version 1.0
 */
public class Sistema {

    /**
     * Lista degli utenti registrati al sistema.
     */
    private LinkedList<Utente> utenti;

    /**
     * Lista delle proiezioni disponibili nel cinema.
     */
    private List<Proiezione> proiezioni;

    /**
     * Gestore degli utenti del sistema.
     */
    private GestoreUtenti gestoreUtenti;

    /**
     * Gestore delle prenotazioni del sistema.
     */
    private GestorePrenotazioni gestorePrenotazioni;

    /**
     * Programmazione delle proiezioni del cinema.
     */
    private ProgrammazioneCinema programmazione;

    /**
     * Costruisce e inizializza il sistema del cinema.
     *
     * <p>Il costruttore inizializza la lista degli utenti, carica gli utenti
     * e le proiezioni dai relativi file, carica le prenotazioni esistenti
     * e costruisce la programmazione del cinema inserendovi tutte le
     * proiezioni disponibili.</p>
     */
    public Sistema() {

        utenti = new LinkedList<Utente>();

        gestoreUtenti = new GestoreUtenti(utenti);
        gestoreUtenti.caricaUtenti();

        proiezioni = GestoreProiezioni.leggiProiezioni();

        gestorePrenotazioni = new GestorePrenotazioni();
        gestorePrenotazioni.caricaPrenotazioni(utenti, proiezioni);

        programmazione = new ProgrammazioneCinema();

        for (Proiezione p : proiezioni) {
            programmazione.aggiungiProiezione(p);
        }
    }

    /**
     * Restituisce la lista delle proiezioni disponibili nel cinema.
     *
     * @return lista delle proiezioni
     */
    public List<Proiezione> getProiezioni() {
        return proiezioni;
    }

    /**
     * Restituisce la programmazione del cinema.
     *
     * @return programmazione delle proiezioni
     */
    public ProgrammazioneCinema getProgrammazione() {
        return programmazione;
    }
}
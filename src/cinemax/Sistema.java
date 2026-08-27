package cinemax;

import java.util.LinkedList;
import java.util.List;

public class Sistema {

    private LinkedList<Utente> utenti;
    private List<Proiezione> proiezioni;
    private GestoreUtenti gestoreUtenti;
    private GestorePrenotazioni gestorePrenotazioni;
    private ProgrammazioneCinema programmazione;

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

    public LinkedList<Utente> getUtenti() {
        return utenti;
    }

    public List<Proiezione> getProiezioni() {
        return proiezioni;
    }

    public GestoreUtenti getGestoreUtenti() {
        return gestoreUtenti;
    }

    public GestorePrenotazioni getGestorePrenotazioni() {
        return gestorePrenotazioni;
    }

    public ProgrammazioneCinema getProgrammazione() {
        return programmazione;
    }
}
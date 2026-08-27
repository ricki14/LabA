package cinemax;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class InizialiazzaDati {

    LinkedList<Utente> utenti = new LinkedList<Utente>();
    GestoreUtenti gestoreUtenti = new GestoreUtenti(utenti);
    gestoreUtenti.caricaUtenti();

    List<Proiezione> proiezioni = GestoreProiezioni.leggiProiezioni();

    GestorePrenotazioni gestorePrenotazioni = new GestorePrenotazioni();
    gestorePrenotazioni.caricaPrenotazioni(utenti, proiezioni);

    ProgrammazioneCinema programmazione = new ProgrammazioneCinema();
    for (Proiezione p : proiezioni) {
        programmazione.aggiungiProiezione(p);
    }
}
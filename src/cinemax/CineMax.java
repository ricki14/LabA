package cinemax;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class CineMax {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("----BENVENUTO DA CINEMAX----");

        LinkedList<Utente> utenti = inizializzaUtenti();
        List<Proiezione> proiezioni = inizializzaProiezioni();
        GestorePrenotazioni gestorePrenotazioni = inizializzaPrenotazioni(utenti, proiezioni);
        ProgrammazioneCinema programmazione = inizializzaProgrammazione(proiezioni);
        GestoreUtenti gestoreUtenti = new GestoreUtenti(utenti);

        Menu menu = new Menu(
                scanner,
                utenti,
                proiezioni,
                programmazione,
                gestoreUtenti,
                gestorePrenotazioni
        );

        menu.avvia();

        scanner.close();
    }

    private static LinkedList<Utente> inizializzaUtenti() {
        LinkedList<Utente> utenti = new LinkedList<>();
        GestoreUtenti gestoreUtenti = new GestoreUtenti(utenti);
        gestoreUtenti.caricaUtenti();
        return utenti;
    }

    private static List<Proiezione> inizializzaProiezioni() {
        return GestoreProiezioni.leggiProiezioni();
    }

    private static GestorePrenotazioni inizializzaPrenotazioni(
            List<Utente> utenti,
            List<Proiezione> proiezioni) {

        GestorePrenotazioni gestorePrenotazioni = new GestorePrenotazioni();
        gestorePrenotazioni.caricaPrenotazioni(utenti, proiezioni);
        return gestorePrenotazioni;
    }

    public static char leggiFilaValida(Scanner scanner) {
        while (true) {
            System.out.print("Inserisci la lettera della fila (es. A): ");
            String input = scanner.nextLine().trim();

            if (input.length() == 1 && Character.isLetter(input.charAt(0))) {
                return Character.toUpperCase(input.charAt(0));
            }

            System.out.println(
                    "Errore: inserisci soltanto una singola lettera per la fila!"
            );
        }
    }

    private static ProgrammazioneCinema inizializzaProgrammazione(
            List<Proiezione> proiezioni) {

        ProgrammazioneCinema programmazione = new ProgrammazioneCinema();

        for (Proiezione proiezione : proiezioni) {
            programmazione.aggiungiProiezione(proiezione);
        }

        return programmazione;
    }
}

package cinemax;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Classe principale dell'applicazione CineMax.
 *
 * <p>Si occupa di avviare il sistema, inizializzare i dati necessari
 * al funzionamento del cinema e creare il menu principale dell'applicazione.</p>
 */
public class CineMax {

    /**
     * Metodo principale dell'applicazione.
     *
     * <p>Inizializza lo {@link Scanner}, carica gli utenti, le proiezioni
     * e le prenotazioni, costruisce la programmazione del cinema e avvia
     * il menu principale.</p>
     *
     * @param args argomenti passati da riga di comando
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("----BENVENUTO DA CINEMAX----");

        LinkedList<Utente> utenti = inizializzaUtenti();
        List<Proiezione> proiezioni = inizializzaProiezioni();
        GestorePrenotazioni gestorePrenotazioni = inizializzaPrenotazioni(utenti, proiezioni);
        ProgrammazioneCinema programmazione = inizializzaProgrammazione(proiezioni);
        GestoreUtenti gestoreUtenti = new GestoreUtenti(utenti);

        Menu menu = new Menu(scanner, utenti, proiezioni, programmazione, gestoreUtenti, gestorePrenotazioni);

        menu.avvia();

        scanner.close();
    }

    /**
     * Inizializza la lista degli utenti e carica gli utenti
     * precedentemente registrati.
     *
     * @return lista degli utenti caricati
     */
    private static LinkedList<Utente> inizializzaUtenti() {
        LinkedList<Utente> utenti = new LinkedList<>();
        GestoreUtenti gestoreUtenti = new GestoreUtenti(utenti);
        gestoreUtenti.caricaUtenti();
        return utenti;
    }

    /**
     * Carica le proiezioni del cinema dai dati memorizzati.
     *
     * @return lista delle proiezioni disponibili
     */
    private static List<Proiezione> inizializzaProiezioni() {
        return GestoreProiezioni.leggiProiezioni();
    }

    /**
     * Inizializza il gestore delle prenotazioni e carica
     * le prenotazioni esistenti.
     *
     * @param utenti lista degli utenti del sistema
     * @param proiezioni lista delle proiezioni disponibili
     * @return gestore delle prenotazioni inizializzato
     */
    private static GestorePrenotazioni inizializzaPrenotazioni(List<Utente> utenti, List<Proiezione> proiezioni) {
        GestorePrenotazioni gestorePrenotazioni = new GestorePrenotazioni();
        gestorePrenotazioni.caricaPrenotazioni(utenti, proiezioni);
        return gestorePrenotazioni;
    }

    /**
     * Legge una lettera inserita dall'utente e la utilizza
     * come identificativo della fila.
     *
     * <p>Il metodo continua a richiedere l'inserimento finché l'utente
     * non fornisce una singola lettera. La lettera viene convertita
     * automaticamente in maiuscolo.</p>
     *
     * @param scanner scanner utilizzato per leggere l'input dell'utente
     * @return lettera maiuscola corrispondente alla fila inserita
     */
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

    /**
     * Inizializza la programmazione del cinema inserendo
     * tutte le proiezioni disponibili.
     *
     * @param proiezioni lista delle proiezioni da inserire nella programmazione
     * @return programmazione del cinema inizializzata
     */
    private static ProgrammazioneCinema inizializzaProgrammazione(
            List<Proiezione> proiezioni) {

        ProgrammazioneCinema programmazione = new ProgrammazioneCinema();

        for (Proiezione proiezione : proiezioni) {
            programmazione.aggiungiProiezione(proiezione);
        }

        return programmazione;
    }
}

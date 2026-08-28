/*
 * Edoardo Carducci - 764215 - Varese
 * Daniele Rossetti - 767980 - Varese
 * Riccardo Palomba - 764224 - Varese
 */

package cinemax;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Gestisce la programmazione del cinema.
 *
 * <p>La classe mantiene un elenco di proiezioni e permette di
 * aggiungere, cercare e visualizzare le proiezioni. Inoltre consente
 * di caricare e salvare la programmazione attraverso file CSV.</p>
 * @author Riccardo Palomba
 * @version 1.0
 */
public class ProgrammazioneCinema {

    /**
     * Elenco delle proiezioni presenti nella programmazione del cinema.
     */
    private ArrayList<Proiezione> elencoProiezioni;

    /**
     * Costruisce una nuova programmazione del cinema.
     *
     * <p>L'elenco delle proiezioni viene inizializzato come lista vuota.</p>
     */
    public ProgrammazioneCinema() {
        elencoProiezioni = new ArrayList<>();
    }

    /**
     * Aggiunge una proiezione alla programmazione del cinema.
     *
     * @param proiezione la proiezione da aggiungere
     */
    public void aggiungiProiezione(Proiezione proiezione) {
        elencoProiezioni.add(proiezione);
    }

    /**
     * Restituisce l'elenco delle proiezioni presenti nella programmazione.
     *
     * @return la lista delle proiezioni
     */
    public ArrayList<Proiezione> getElencoProiezioni() {
        return elencoProiezioni;
    }


    /**
     * Permette all'utente di cercare una proiezione attraverso diversi criteri
     * e di selezionare una delle proiezioni trovate.
     *
     * <p>I criteri di ricerca sono il titolo del film, il genere,
     * un intervallo di date e il costo massimo del biglietto.
     * Un campo lasciato vuoto viene ignorato.</p>
     *
     * @param scanner scanner utilizzato per leggere gli input dell'utente
     * @return la proiezione selezionata dall'utente oppure {@code null}
     *         se non viene trovata o selezionata alcuna proiezione
     */
    public Proiezione cercaProiezione(Scanner scanner) {

        try {

            System.out.print(
                    "Titolo (Invio se non interessa): "
            );
            String titolo = scanner.nextLine();

            System.out.print(
                    "Genere (Invio se non interessa): "
            );
            String genere = scanner.nextLine();

            System.out.print(
                    "Data inizio (AAAA-MM-GG): "
            );
            String inputInizio = scanner.nextLine();

            System.out.print(
                    "Data fine (AAAA-MM-GG): "
            );
            String inputFine = scanner.nextLine();

            System.out.print(
                    "Costo massimo: "
            );
            String inputCosto = scanner.nextLine();

            LocalDate dataInizio =
                    inputInizio.isBlank()
                            ? null
                            : LocalDate.parse(inputInizio);

            LocalDate dataFine =
                    inputFine.isBlank()
                            ? null
                            : LocalDate.parse(inputFine);

            Double costo =
                    inputCosto.isBlank()
                            ? null
                            : Double.parseDouble(
                            inputCosto.replace(',', '.')
                    );

            ArrayList<Proiezione> risultati =
                    new ArrayList<>();

            for (Proiezione p : elencoProiezioni) {

                boolean ok = true;

                // Controllo titolo
                if (!titolo.isBlank()
                        && !p.getFilm()
                        .getTitolo()
                        .toLowerCase()
                        .contains(titolo.toLowerCase())) {

                    ok = false;
                }

                // Controllo genere
                if (!genere.isBlank()
                        && !p.getFilm()
                        .getGenere()
                        .equalsIgnoreCase(genere)) {

                    ok = false;
                }

                LocalDate data =
                        p.getDataOra().toLocalDate();

                // Controllo data iniziale
                if (dataInizio != null
                        && data.isBefore(dataInizio)) {

                    ok = false;
                }

                // Controllo data finale
                if (dataFine != null
                        && data.isAfter(dataFine)) {

                    ok = false;
                }

                // Controllo costo massimo
                if (costo != null
                        && p.getPrezzoBiglietto() > costo) {

                    ok = false;
                }

                if (ok) {
                    risultati.add(p);
                }
            }

            // Nessun risultato
            if (risultati.isEmpty()) {

                System.out.println(
                        "Nessuna proiezione trovata."
                );

                return null;
            }

            System.out.println(
                    "\n===== PROIEZIONI TROVATE ====="
            );

            for (int i = 0; i < risultati.size(); i++) {

                Proiezione p = risultati.get(i);

                System.out.println(
                        (i + 1) + ") "
                                + p.getFilm().getTitolo()
                                + " - "
                                + p.getDataOra()
                );
            }

            int scelta;

            while (true) {

                System.out.print(
                        "Seleziona una proiezione: "
                );

                String input = scanner.nextLine();

                try {

                    scelta = Integer.parseInt(input);

                    if (scelta >= 1
                            && scelta <= risultati.size()) {

                        break;
                    }

                    System.out.println(
                            "Selezione non valida."
                    );

                } catch (NumberFormatException e) {

                    System.out.println(
                            "Inserisci un numero valido."
                    );
                }
            }

            Proiezione proiezioneSelezionata =
                    risultati.get(scelta - 1);

            visualizzaProiezione(
                    proiezioneSelezionata
            );

            return proiezioneSelezionata;

        } catch (DateTimeParseException e) {

            System.out.println(
                    "Formato data non valido. "
                            + "Usa AAAA-MM-GG."
            );

            return null;

        } catch (NumberFormatException e) {

            System.out.println(
                    "Il costo massimo deve essere un numero valido."
            );

            return null;

        }
    }
    /**
     * Visualizza a video i dettagli di una proiezione.
     *
     * <p>Vengono mostrati i dati del film associato alla proiezione,
     * la data e l'ora, il prezzo del biglietto e il numero di posti
     * attualmente liberi.</p>
     *
     * @param p la proiezione di cui visualizzare i dettagli
     */
    public void visualizzaProiezione(Proiezione p) {

        Film film = p.getFilm();

        System.out.println("\n========== DETTAGLI ==========");
        System.out.println("Titolo: " + film.getTitolo());
        System.out.println("Genere: " + film.getGenere());
        System.out.println("Regista: " + film.getRegista());
        System.out.println("Anno: " + film.getAnno());
        System.out.println("Durata: " + film.getDurata() + " minuti");
        System.out.println("Età minima: " + film.getEtaMinima());
        System.out.println("Data e ora: " + p.getDataOra());
        System.out.println("Prezzo: " + p.getPrezzoBiglietto() + " €");
        System.out.println("Posti liberi: " + p.getPostiLiberi());
        System.out.println("==============================");
    }

}
package cinemax;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
     * Cerca una proiezione tramite il suo identificativo.
     *
     * <p>La ricerca non distingue tra lettere maiuscole e minuscole.</p>
     *
     * @param id l'identificativo della proiezione da cercare
     * @return la proiezione corrispondente all'ID indicato,
     *         oppure {@code null} se non viene trovata
     */
    public Proiezione cercaPerId(String id) {
        for (Proiezione p : elencoProiezioni) {
            if (p.getId().equalsIgnoreCase(id)) {
                return p;
            }
        }

        return null;
    }

    /**
     * Cerca una proiezione utilizzando diversi criteri di ricerca.
     *
     * <p>L'utente può filtrare le proiezioni in base al titolo,
     * al genere, a un intervallo di date e al costo massimo del biglietto.
     * I campi lasciati vuoti vengono ignorati durante la ricerca.</p>
     *
     * <p>Dopo aver effettuato la ricerca, vengono visualizzate le
     * proiezioni che soddisfano i criteri inseriti. L'utente può quindi
     * selezionare una delle proiezioni trovate per visualizzarne
     * i dettagli.</p>
     *
     * @param scanner oggetto utilizzato per leggere i dati inseriti
     *                dall'utente
     */
    public void cercaProiezione(Scanner scanner) {

        try {

            System.out.print("Titolo (Invio se non interessa): ");
            String titolo = scanner.nextLine();

            System.out.print("Genere (Invio se non interessa): ");
            String genere = scanner.nextLine();

            System.out.print("Data inizio (AAAA-MM-GG): ");
            String inputInizio = scanner.nextLine();

            System.out.print("Data fine (AAAA-MM-GG): ");
            String inputFine = scanner.nextLine();

            System.out.print("Costo massimo: ");
            String inputCosto = scanner.nextLine();

            LocalDate dataInizio =
                    inputInizio.isBlank() ? null : LocalDate.parse(inputInizio);

            LocalDate dataFine =
                    inputFine.isBlank() ? null : LocalDate.parse(inputFine);

            Double costo =
                    inputCosto.isBlank() ? null : Double.parseDouble(inputCosto);

            ArrayList<Proiezione> risultati = new ArrayList<>();

            for (Proiezione p : elencoProiezioni) {

                boolean ok = true;

                if (!titolo.isBlank() &&
                        !p.getFilm().getTitolo()
                                .toLowerCase()
                                .contains(titolo.toLowerCase()))
                    ok = false;

                if (!genere.isBlank() &&
                        !p.getFilm().getGenere().equalsIgnoreCase(genere))
                    ok = false;

                LocalDate data = p.getDataOra().toLocalDate();

                if (dataInizio != null && data.isBefore(dataInizio))
                    ok = false;

                if (dataFine != null && data.isAfter(dataFine))
                    ok = false;

                if (costo != null && p.getPrezzoBiglietto() > costo)
                    ok = false;

                if (ok)
                    risultati.add(p);
            }

            if (risultati.isEmpty()) {
                System.out.println("Nessuna proiezione trovata.");
                return;
            }

            System.out.println("\n===== PROIEZIONI TROVATE =====");

            for (int i = 0; i < risultati.size(); i++) {
                Proiezione p = risultati.get(i);
                System.out.println(
                        (i + 1) + ") " +
                                p.getFilm().getTitolo() + " - " +
                                p.getDataOra()
                );
            }

            System.out.print("Seleziona una proiezione: ");
            int scelta = scanner.nextInt();
            scanner.nextLine();

            if (scelta >= 1 && scelta <= risultati.size()) {
                visualizzaProiezione(risultati.get(scelta - 1));
            }

        } catch (DateTimeParseException e) {
            System.out.println("Formato data non valido.");
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

    /**
     * Percorso predefinito del file CSV contenente le proiezioni.
     */
   /*  String percorsoFile = "proiezioni.csv";

    /**
     * Carica le proiezioni da un file CSV.
     *
     * <p>Prima di caricare i dati, l'elenco delle proiezioni attualmente
     * presente viene svuotato. Il file CSV viene quindi letto riga per
     * riga e, per ogni riga, vengono creati un oggetto {@link Film} e
     * un oggetto {@link Proiezione}.</p>
     *
     * <p>La prima riga del file viene considerata come intestazione
     * e viene quindi ignorata.</p>
     *
     * @param percorsoFile percorso del file CSV da cui caricare
     *                     le proiezioni

    public void caricaDaCSV(String percorsoFile) {

        elencoProiezioni.clear();

        try (BufferedReader br =
                     new BufferedReader(new FileReader(percorsoFile))) {

            // Salta l'intestazione
            br.readLine();

            String riga;
            int id = 1;

            while ((riga = br.readLine()) != null) {

                String[] campi = riga.split(",");

                // Converte la data nel formato corretto per LocalDateTime
                LocalDateTime dataOra = LocalDateTime.parse(
                        campi[0].replace("\"", "").replace(" ", "T")
                );

                // Crea il film togliendo le virgolette
                Film film = new Film(
                        campi[1].replace("\"", ""),
                        campi[2].replace("\"", ""),
                        campi[3].replace("\"", ""),
                        Integer.parseInt(campi[4]),
                        Integer.parseInt(campi[5]),
                        Integer.parseInt(campi[6])
                );

                // Crea la proiezione
                Proiezione proiezione = new Proiezione(
                        "P" + id,
                        film,
                        dataOra,
                        Double.parseDouble(campi[7])
                );

                elencoProiezioni.add(proiezione);
                id++;
            }

        } catch (IOException e) {
            System.out.println("Errore durante la lettura del file.");
        } catch (Exception e) {
            System.out.println(
                    "Errore nel formato del file: " + e.getMessage()
            );
        }
    }

    /**
     * Salva tutte le proiezioni presenti nella programmazione
     * all'interno di un file CSV.
     *
     * <p>Il metodo crea o sovrascrive il file indicato e inserisce
     * una riga di intestazione seguita dai dati di ogni proiezione.</p>
     *
     * @param percorsoFile percorso del file CSV in cui salvare
     *                     le proiezioni

    public void salvaSuCSV(String percorsoFile) {

        try (BufferedWriter bw =
                     new BufferedWriter(new FileWriter(percorsoFile))) {

            // Scrive l'intestazione
            bw.write(
                    "data_ora_proiezione,titolo_film,genere,regista," +
                            "anno,durata_minuti,eta_minima,prezzo_biglietto"
            );
            bw.newLine();

            // Scrive tutte le proiezioni
            for (Proiezione p : elencoProiezioni) {

                Film f = p.getFilm();

                bw.write(
                        "\"" + p.getDataOra()
                                .toString()
                                .replace("T", " ") + "\"," +
                                "\"" + f.getTitolo() + "\"," +
                                f.getGenere() + "," +
                                "\"" + f.getRegista() + "\"," +
                                f.getAnno() + "," +
                                f.getDurata() + "," +
                                f.getEtaMinima() + "," +
                                p.getPrezzoBiglietto()
                );

                bw.newLine();
            }

        } catch (IOException e) {
            System.out.println("Errore durante il salvataggio del file.");
        }
    }*/
}
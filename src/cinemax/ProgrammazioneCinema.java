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

public class ProgrammazioneCinema {

    private ArrayList<Proiezione> elencoProiezioni;

    public ProgrammazioneCinema() {
        elencoProiezioni = new ArrayList<>();
    }

    public void aggiungiProiezione(Proiezione proiezione) {
        elencoProiezioni.add(proiezione);
    }

    public ArrayList<Proiezione> getElencoProiezioni() {
        return elencoProiezioni;
    }

    public Proiezione cercaPerId(String id) {
        for (Proiezione p : elencoProiezioni) {
            if (p.getId().equalsIgnoreCase(id)) {
                return p;
            }
        }
        return null;
    }

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

            LocalDate dataInizio = inputInizio.isBlank() ? null : LocalDate.parse(inputInizio);
            LocalDate dataFine = inputFine.isBlank() ? null : LocalDate.parse(inputFine);
            Double costo = inputCosto.isBlank() ? null : Double.parseDouble(inputCosto);

            ArrayList<Proiezione> risultati = new ArrayList<>();

            for (Proiezione p : elencoProiezioni) {

                boolean ok = true;

                if (!titolo.isBlank() &&
                        !p.getFilm().getTitolo().toLowerCase().contains(titolo.toLowerCase()))
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

            System.out.println("\\n===== PROIEZIONI TROVATE =====");

            for (int i = 0; i < risultati.size(); i++) {
                Proiezione p = risultati.get(i);
                System.out.println((i + 1) + ") " + p.getFilm().getTitolo() + " - " + p.getDataOra());
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

    public void visualizzaProiezione(Proiezione p) {

        Film film = p.getFilm();

        System.out.println("\\n========== DETTAGLI ==========");
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

    public void caricaDaCSV(String percorsoFile) {

        elencoProiezioni.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(percorsoFile))) {

            br.readLine();
            String riga;
            int id = 1;

            while ((riga = br.readLine()) != null) {

                String[] campi = riga.split(",");

                Film film = new Film(
                        campi[1],
                        campi[2],
                        campi[3],
                        Integer.parseInt(campi[4]),
                        Integer.parseInt(campi[5]),
                        Integer.parseInt(campi[6]));

                Proiezione proiezione = new Proiezione(
                        "P" + id,
                        film,
                        LocalDateTime.parse(campi[0]),
                        Double.parseDouble(campi[7]));

                elencoProiezioni.add(proiezione);
                id++;
            }

        } catch (Exception e) {
            System.out.println("Errore: " + e.getMessage());
        }
    }

    public void salvaSuCSV(String percorsoFile) {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(percorsoFile))) {

            bw.write("data_ora_proiezione,titolo_film,genere,regista,anno,durata_minuti,eta_minima,prezzo_biglietto");
            bw.newLine();

            for (Proiezione p : elencoProiezioni) {

                Film f = p.getFilm();

                bw.write(p.getDataOra() + "," +
                        f.getTitolo() + "," +
                        f.getGenere() + "," +
                        f.getRegista() + "," +
                        f.getAnno() + "," +
                        f.getDurata() + "," +
                        f.getEtaMinima() + "," +
                        p.getPrezzoBiglietto());

                bw.newLine();
            }

        } catch (IOException e) {
            System.out.println("Errore nel salvataggio del file.");
        }
    }
}
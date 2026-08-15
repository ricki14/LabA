package cinemax;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestisce la lettura e la scrittura delle proiezioni
 * nel file CSV <code>data/proiezioni.csv</code>.
 *
 * <p>La classe permette di caricare le proiezioni dal file
 * e di salvare su file una lista di proiezioni.</p>
 *
 * @author Riccardo Palomba
 * @version 1.0
 */
public class GestoreProiezioni {

    /**
     * Percorso del file CSV contenente le proiezioni.
     */
    private static String file = "data/proiezioni.csv";

    /**
     * Formato utilizzato per rappresentare data e ora
     * delle proiezioni nel file CSV.
     */
    private static DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Legge dal file CSV tutte le proiezioni presenti
     * e le converte in oggetti <code>Proiezione</code>.
     *
     * <p>Per ogni riga del file vengono estratti i dati del film,
     * la data e l'ora della proiezione e il prezzo del biglietto.</p>
     *
     * @return una lista contenente le proiezioni lette dal file
     */
    public static List<Proiezione> leggiProiezioni() {

        List<Proiezione> proiezioni = new ArrayList<>();

        try (BufferedReader br =
                     new BufferedReader(new FileReader(file))) {

            // Salta l'intestazione
            br.readLine();

            String riga;

            while ((riga = br.readLine()) != null) {

                String[] dati = riga.split(",");

                String data = dati[0].replace("\"", "");
                String titolo = dati[1].replace("\"", "");
                String genere = dati[2].replace("\"", "");
                String regista = dati[3].replace("\"", "");

                int anno = Integer.parseInt(dati[4]);
                int durata = Integer.parseInt(dati[5]);
                int etaMinima = Integer.parseInt(dati[6]);
                double prezzo = Double.parseDouble(dati[7]);

                LocalDateTime dataOra =
                        LocalDateTime.parse(data, formatter);

                Film film = new Film(
                        titolo,
                        genere,
                        regista,
                        anno,
                        durata,
                        etaMinima
                );

                Proiezione proiezione = new Proiezione(
                        String.valueOf(proiezioni.size() + 1),
                        film,
                        dataOra,
                        prezzo
                );

                proiezioni.add(proiezione);
            }

        } catch (IOException e) {
            System.out.println(
                    "Errore durante la lettura del file"
                            + e.getMessage()
            );
        }

        return proiezioni;
    }

    /**
     * Scrive nel file CSV tutte le proiezioni contenute
     * nella lista fornita.
     *
     * <p>Il file viene sovrascritto e viene inserita
     * automaticamente l'intestazione delle colonne.</p>
     *
     * @param proiezioni lista delle proiezioni da salvare nel file
     */
    public static void scriviProiezioni(
            List<Proiezione> proiezioni) {

        try (PrintWriter pw =
                     new PrintWriter(new FileWriter(file))) {

            pw.println(
                    "data_ora_proiezione,titolo_film,genere,regista,"
                            + "anno,durata,eta_minima,prezzo_biglietto"
            );

            for (Proiezione p : proiezioni) {

                Film film = p.getFilm();

                pw.println(
                        "\"" +
                                p.getDataOra().format(formatter) +
                                "\"," +

                                "\"" +
                                film.getTitolo() +
                                "\"," +

                                "\"" +
                                film.getGenere() +
                                "\"," +

                                "\"" +
                                film.getRegista() +
                                "\"," +

                                film.getAnno() +
                                "," +

                                film.getDurata() +
                                "," +

                                film.getEtaMinima() +
                                "," +

                                p.getPrezzoBiglietto()
                );
            }

        } catch (IOException e) {
            System.out.println(
                    "Errore nella scrittura: "
                            + e.getMessage()
            );
        }
    }
}
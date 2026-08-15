package cinemax;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestisce la lettura e la scrittura delle prenotazioni
 * nel file CSV <code>data/prenotazioni.csv</code>.
 *
 * @author Riccardo Palomba
 */
public class GestorePrenotazioni {

    /**
     * Percorso del file CSV utilizzato per memorizzare
     * le prenotazioni.
     */
    private final String percorso_file = "data/prenotazioni.csv";

    /**
     * Legge dal file CSV tutte le prenotazioni presenti
     * e le restituisce sotto forma di lista di array di stringhe.
     *
     * <p>La prima riga del file, contenente l'intestazione,
     * viene ignorata.</p>
     *
     * @return lista contenente le prenotazioni lette dal file
     * @throws IOException se si verifica un errore durante la lettura
     * del file
     */
    public List<String[]> caricaPrenotazioni() throws IOException {

        List<String[]> prenotazioni = new ArrayList<>();

        File file = new File(percorso_file);

        if (!file.exists()) {
            return prenotazioni;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            // Salta l'intestazione
            br.readLine();

            String riga;

            while ((riga = br.readLine()) != null) {

                if (riga.isBlank()) {
                    continue;
                }

                String[] campi = riga.split(",");

                prenotazioni.add(campi);
            }
        }

        return prenotazioni;
    }

    /**
     * Salva le prenotazioni nel file CSV.
     * Se il file non esiste, viene creata la directory
     * necessaria.
     *
     * <p>Il file viene sovrascritto e viene inserita
     * automaticamente la riga di intestazione.</p>
     *
     * @param prenotazioni lista delle prenotazioni da salvare
     * @throws IOException se si verifica un errore durante
     * la scrittura del file
     */
    public void salvaPrenotazioni(List<String[]> prenotazioni)
            throws IOException {

        File file = new File(percorso_file);

        file.getParentFile().mkdirs();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {

            bw.write(
                    "codice,username,nome,cognome,codiceProiezione,numeroBiglietti"
            );
            bw.newLine();

            for (String[] prenotazione : prenotazioni) {

                bw.write(String.join(",", prenotazione));
                bw.newLine();
            }
        }
    }
}
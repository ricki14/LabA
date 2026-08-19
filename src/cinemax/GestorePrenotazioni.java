package cinemax;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Gestisce la lettura, la scrittura e la ricerca delle prenotazioni
 * nel file CSV <code>data/prenotazioni.csv</code>.
 *
 * <p>Il file memorizza l'ID della prenotazione, lo username
 * del cliente, l'ID della proiezione e i posti prenotati.</p>
 *
 * @author Riccardo Palomba
 * @version 1.0
 */
public class GestorePrenotazioni {

    /**
     * Costruisce il gestore delle prenotazioni.
     */
    public GestorePrenotazioni() {
    }

    /**
     * Percorso del file CSV utilizzato per memorizzare
     * le prenotazioni.
     */
    private final String percorso_file =
            "data/prenotazioni.csv";


    /**
     * Legge dal file CSV tutte le prenotazioni presenti.
     *
     * @return lista contenente le righe delle prenotazioni
     * @throws IOException se si verifica un errore durante la lettura
     */
    public List<String[]> caricaPrenotazioni() throws IOException {

        List<String[]> prenotazioni = new ArrayList<>();
        File file = new File(percorso_file);

        if (!file.exists()) {
            return prenotazioni;
        }

        try (BufferedReader br =
                     new BufferedReader(new FileReader(file))) {

            br.readLine();

            String riga;

            while ((riga = br.readLine()) != null) {
                if (riga.isBlank()) {
                    continue;
                }

                prenotazioni.add(
                        riga.split(",", -1)
                );
            }
        }

        return prenotazioni;
    }

    /**
     * Ricostruisce le prenotazioni presenti nel file CSV e le associa
     * ai relativi clienti e alle relative proiezioni.
     *
     * @param utenti lista degli utenti caricati
     * @param proiezioni lista delle proiezioni caricate
     */
    public void caricaPrenotazioni(
            List<Utente> utenti,
            List<Proiezione> proiezioni) {

        try {
            List<String[]> dati = caricaPrenotazioni();

            for (String[] campi : dati) {

                if (campi.length < 4) {
                    continue;
                }

                String id = campi[0];
                String username = campi[1];
                String codiceProiezione = campi[2];
                String postiStringa = campi[3];
                java.time.LocalDate dataAcquisto = java.time.LocalDate.now();

                if (campi.length >= 5 && !campi[4].isBlank()) {
                    try {
                        dataAcquisto = java.time.LocalDate.parse(campi[4]);
                    } catch (java.time.format.DateTimeParseException ignored) {
                        // Mantiene la data corrente se il campo non è valido.
                    }
                }

                ClienteRegistrato cliente =
                        cercaCliente(utenti, username);

                Proiezione proiezione =
                        cercaProiezione(proiezioni, codiceProiezione);

                if (cliente == null || proiezione == null) {
                    continue;
                }

                LinkedList<Posto> postiPrenotati =
                        new LinkedList<>();

                if (!postiStringa.isBlank()) {

                    String[] posti =
                            postiStringa.split("\\|");

                    for (String codicePosto : posti) {

                        if (codicePosto.length() < 2) {
                            continue;
                        }

                        char fila =
                                codicePosto.charAt(0);

                        int numero;

                        try {
                            numero = Integer.parseInt(
                                    codicePosto.substring(1)
                            );
                        } catch (NumberFormatException e) {
                            continue;
                        }

                        Posto posto =
                                trovaPosto(
                                        proiezione,
                                        fila,
                                        numero
                                );

                        if (posto != null
                                && !posto.isOccupato()
                                && !postiPrenotati.contains(posto)) {
                            postiPrenotati.add(posto);
                        }
                    }
                }

                if (!postiPrenotati.isEmpty()
                        && cercaPerId(utenti, id) == null) {

                    Prenotazione prenotazione =
                            new Prenotazione(
                                    cliente,
                                    proiezione,
                                    postiPrenotati,
                                    id,
                                    dataAcquisto
                            );

                    cliente.getPrenotazioniCliente()
                            .add(prenotazione);
                }
            }

        } catch (IOException e) {
            System.out.println(
                    "Errore durante il caricamento delle prenotazioni: "
                            + e.getMessage()
            );
        }
    }

    /**
     * Cerca una prenotazione tramite il suo ID.
     *
     * @param utenti lista degli utenti del sistema
     * @param id identificativo della prenotazione
     * @return la prenotazione trovata oppure {@code null}
     */
    public Prenotazione cercaPerId(
            List<Utente> utenti,
            String id) {

        if (id == null) {
            return null;
        }

        for (Utente utente : utenti) {

            if (!(utente instanceof ClienteRegistrato)) {
                continue;
            }

            ClienteRegistrato cliente =
                    (ClienteRegistrato) utente;

            for (Prenotazione prenotazione :
                    cliente.getPrenotazioniCliente()) {

                if (prenotazione.getIdPrenotazione()
                        .equalsIgnoreCase(id.trim())) {
                    return prenotazione;
                }
            }
        }

        return null;
    }

    /**
     * Cerca tutte le prenotazioni associate a un cliente
     * tramite nome e cognome.
     *
     * @param utenti lista degli utenti del sistema
     * @param nome nome del cliente
     * @param cognome cognome del cliente
     * @return lista delle prenotazioni trovate
     */
    public List<Prenotazione> cercaPerNomeECognome(
            List<Utente> utenti,
            String nome,
            String cognome) {

        List<Prenotazione> risultati =
                new ArrayList<>();

        if (nome == null || cognome == null) {
            return risultati;
        }

        for (Utente utente : utenti) {

            if (!(utente instanceof ClienteRegistrato)) {
                continue;
            }

            ClienteRegistrato cliente =
                    (ClienteRegistrato) utente;

            if (cliente.getNome().equalsIgnoreCase(nome.trim())
                    && cliente.getCognome()
                    .equalsIgnoreCase(cognome.trim())) {

                risultati.addAll(
                        cliente.getPrenotazioniCliente()
                );
            }
        }

        return risultati;
    }

    /**
     * Cerca una prenotazione tramite ID, nome e cognome del cliente.
     *
     * @param utenti lista degli utenti del sistema
     * @param id identificativo della prenotazione
     * @param nome nome del cliente
     * @param cognome cognome del cliente
     * @return la prenotazione trovata oppure {@code null}
     */
    public Prenotazione cercaPerIdNomeECognome(
            List<Utente> utenti,
            String id,
            String nome,
            String cognome) {

        Prenotazione prenotazione =
                cercaPerId(utenti, id);

        if (prenotazione == null) {
            return null;
        }

        ClienteRegistrato cliente =
                prenotazione.getClienteRegistrato();

        if (cliente.getNome().equalsIgnoreCase(nome.trim())
                && cliente.getCognome()
                .equalsIgnoreCase(cognome.trim())) {

            return prenotazione;
        }

        return null;
    }

    /**
     * Salva tutte le prenotazioni dei clienti nel file CSV.
     *
     * @param utenti lista degli utenti del sistema
     * @throws IOException se si verifica un errore durante la scrittura
     */
    public void salvaPrenotazioni(
            List<Utente> utenti) throws IOException {

        File file = new File(percorso_file);
        File cartella = file.getParentFile();

        if (cartella != null && !cartella.exists()) {
            cartella.mkdirs();
        }

        try (PrintWriter writer =
                     new PrintWriter(
                             new BufferedWriter(
                                     new FileWriter(file)))) {

            writer.println(
                    "codice,username,codiceProiezione,posti,dataAcquisto"
            );

            for (Utente utente : utenti) {

                if (!(utente instanceof ClienteRegistrato)) {
                    continue;
                }

                ClienteRegistrato cliente =
                        (ClienteRegistrato) utente;

                for (Prenotazione prenotazione :
                        cliente.getPrenotazioniCliente()) {

                    StringBuilder posti =
                            new StringBuilder();

                    for (Posto posto :
                            prenotazione.getPostiPrenotati()) {

                        if (posti.length() > 0) {
                            posti.append("|");
                        }

                        posti.append(
                                posto.getLetteraFila()
                        );
                        posti.append(
                                posto.getNumeroPosto()
                        );
                    }

                    writer.println(
                            prenotazione.getIdPrenotazione()
                                    + ","
                                    + cliente.getUsername()
                                    + ","
                                    + prenotazione
                                    .getProiezione()
                                    .getId()
                                    + ","
                                    + posti
                                    + ","
                                    + prenotazione.getDataAcquisto()
                    );
                }
            }
        }
    }

    /**
     * Cerca un cliente tramite username.
     *
     * @param utenti lista degli utenti
     * @param username username da cercare
     * @return cliente trovato oppure {@code null}
     */
    private ClienteRegistrato cercaCliente(
            List<Utente> utenti,
            String username) {

        for (Utente utente : utenti) {

            if (utente instanceof ClienteRegistrato
                    && utente.getUsername()
                    .equalsIgnoreCase(username)) {

                return (ClienteRegistrato) utente;
            }
        }

        return null;
    }

    /**
     * Cerca una proiezione tramite ID.
     *
     * @param proiezioni lista delle proiezioni
     * @param id identificativo della proiezione
     * @return proiezione trovata oppure {@code null}
     */
    private Proiezione cercaProiezione(
            List<Proiezione> proiezioni,
            String id) {

        for (Proiezione proiezione : proiezioni) {

            if (proiezione.getId()
                    .equalsIgnoreCase(id)) {

                return proiezione;
            }
        }

        return null;
    }

    /**
     * Cerca un posto all'interno di una proiezione.
     *
     * @param proiezione proiezione in cui effettuare la ricerca
     * @param fila fila del posto
     * @param numero numero del posto
     * @return posto trovato oppure {@code null}
     */
    private Posto trovaPosto(
            Proiezione proiezione,
            char fila,
            int numero) {

        for (Posto posto : proiezione.getPosti()) {

            if (posto.getLetteraFila() == fila
                    && posto.getNumeroPosto() == numero) {
                return posto;
            }
        }

        return null;
    }
}

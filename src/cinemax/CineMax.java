package cinemax;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Classe principale dell'applicazione CineMax.
 *
 * <p>Gestisce l'avvio del programma, il caricamento e il salvataggio
 * dei dati e l'interazione con l'utente attraverso i menu testuali.</p>
 *
 * <p>In base al ruolo dell'utente autenticato viene mostrato
 * il relativo menu operativo.</p>
 *
 * @author Riccardo Palomba
 * @version 1.0
 */
public class CineMax {

    /**
     * Scanner utilizzato per leggere gli input dell'utente.
     */
    private final Scanner scanner;

    /**
     * Lista degli utenti registrati nel sistema.
     */
    private final LinkedList<Utente> utenti;

    /**
     * Lista delle proiezioni presenti nella programmazione.
     */
    private final List<Proiezione> proiezioni;

    /**
     * Gestore della persistenza degli utenti.
     */
    private final GestoreUtenti gestoreUtenti;

    /**
     * Costruisce una nuova istanza di CineMax.
     */
    public CineMax() {
        scanner = new Scanner(System.in);
        utenti = new LinkedList<>();
        proiezioni = new ArrayList<>();
        gestoreUtenti = new GestoreUtenti(utenti);
    }

    /**
     * Avvia l'applicazione CineMax.
     */
    public void avvia() {

        caricaDati();

        boolean continua = true;

        while (continua) {
            continua = menuPrincipale();
        }

        salvaDati();
        salvaPrenotazioni();

        scanner.close();

        System.out.println("\nGrazie per aver utilizzato CineMax!");
    }

    /**
     * Carica gli utenti e le proiezioni dai relativi file.
     */
    private void caricaDati() {

        System.out.println("\nCaricamento dati...");

        gestoreUtenti.caricaUtenti();

        List<Proiezione> proiezioniCaricate =
                GestoreProiezioni.leggiProiezioni();

        proiezioni.clear();
        proiezioni.addAll(proiezioniCaricate);

        GestorePrenotazioni gestorePrenotazioni =
                new GestorePrenotazioni();

        gestorePrenotazioni.caricaPrenotazioni(
                utenti,
                proiezioni
        );

        System.out.println("Proiezioni caricate: "
                        + proiezioni.size()
        );
    }
/**
     * Salva sui file i dati modificati durante l'esecuzione.
     */
    private void salvaDati() {

        System.out.println("\nSalvataggio dati...");

        gestoreUtenti.salvaUtenti();

        GestoreProiezioni.scriviProiezioni(proiezioni);
    }

    /**
     * Mostra il menu principale dell'applicazione.
     *
     * @return true se il programma deve continuare,
     *         false se l'utente sceglie di uscire
     */
    private boolean menuPrincipale() {

        System.out.println();
        System.out.println("================================");
        System.out.println("          CINEMAX");
        System.out.println("================================");
        System.out.println("1. Login");
        System.out.println("2. Registrazione");
        System.out.println("3. Esci");
        System.out.println("================================");

        int scelta = leggiIntero("Scelta: ");

        switch (scelta) {

            case 1:
                login();
                return true;

            case 2:
                registrazione();
                return true;

            case 3:
                return false;

            default:
                System.out.println("Scelta non valida.");
                return true;
        }
    }

    /**
     * Gestisce la procedura di autenticazione dell'utente.
     */
    private void login() {

        System.out.println("\n========== LOGIN ==========");

        String username = leggiStringa("Username: ");

        String password = PasswordUtil.hashPassword(
                leggiStringa("Password: ")
        );

        Utente utente = trovaUtente(username);

        if (utente == null) {
            System.out.println("Username non trovato.");
            return;
        }

        if (!utente.getPassword().equals(password)) {
            System.out.println("Password errata.");
            return;
        }

        utente.setLoggato(true);

        System.out.println(
                "\nBenvenuto/a "
                        + utente.getNome()
                        + " "
                        + utente.getCognome()
                        + "!"
        );

        switch (utente.getRuolo()) {

            case CLIENTE:
                menuCliente((ClienteRegistrato) utente);
                break;

            case PROIEZIONISTA:
                menuProiezionista((Proiezionista) utente);
                break;

            case BIGLIETTAIO:
                menuBigliettaio((Bigliettaio) utente);
                break;

            default:
                System.out.println("Ruolo non riconosciuto.");
        }
    }

    /**
     * Gestisce la registrazione di un nuovo cliente.
     */
    private void registrazione() {

        System.out.println("\n========== REGISTRAZIONE ==========");

        String nome = leggiStringa("Nome: ");
        String cognome = leggiStringa("Cognome: ");

        String username;

        while (true) {

            username = leggiStringa("Username: ");

            if (trovaUtente(username) == null) {
                break;
            }

            System.out.println(
                    "Username già utilizzato. Scegline un altro."
            );
        }

        String password = PasswordUtil.hashPassword(
                leggiStringa("Password: ")
        );

        LocalDate dataDiNascita =
                leggiData("Data di nascita (AAAA-MM-GG): ");

        System.out.println("\n========== DOMICILIO ==========");

        String via = leggiStringa("Via: ");
        String numeroCivico = leggiStringa("Numero civico: ");
        String cap = leggiStringa("CAP: ");
        String citta = leggiStringa("Città: ");
        String provincia = leggiStringa("Provincia: ");

        Domicilio domicilio = new Domicilio(
                via,
                numeroCivico,
                cap,
                citta,
                provincia
        );

        ClienteRegistrato cliente =
                new ClienteRegistrato(
                        nome,
                        cognome,
                        username,
                        password,
                        dataDiNascita,
                        domicilio,
                        Ruolo.CLIENTE
                );

        utenti.add(cliente);

        gestoreUtenti.salvaUtenti();

        System.out.println(
                "\nRegistrazione completata con successo!"
        );
    }

    /**
     * Gestisce la ricerca di una proiezione e la prenotazione
     * dei posti da parte del cliente.
     *
     * @param cliente cliente che effettua la prenotazione
     */
    private void prenotaProiezione(ClienteRegistrato cliente) {

        Proiezione proiezione = cercaProiezione();

        if (proiezione == null) {
            return;
        }

        System.out.println("\nHai selezionato la proiezione:");
        System.out.println(proiezione);

        String conferma = leggiStringa(
                "Vuoi procedere con la prenotazione? (s/n): "
        );

        if (!conferma.equalsIgnoreCase("s")) {
            System.out.println("Prenotazione annullata.");
            return;
        }

        System.out.println("\n========== POSTI ==========");

        for (char fila = 'A'; fila <= 'T'; fila++) {

            System.out.print(fila + ": ");

            for (int numero = 1; numero <= 10; numero++) {

                Posto posto = trovaPosto(
                        proiezione,
                        fila,
                        numero
                );

                if (posto.isOccupato()) {
                    System.out.print("[XX] ");
                } else {
                    System.out.print(
                            "[" + fila + numero + "] "
                    );
                }
            }

            System.out.println();
        }

        int numeroPosti = leggiIntero(
                "\nQuanti posti vuoi prenotare? "
        );

        if (numeroPosti <= 0) {
            System.out.println(
                    "Il numero di posti deve essere maggiore di 0."
            );
            return;
        }

        if (numeroPosti > proiezione.getPostiLiberi()) {
            System.out.println(
                    "Non ci sono abbastanza posti liberi."
            );
            return;
        }

        LinkedList<Posto> postiPrenotati =
                new LinkedList<>();

        for (int i = 0; i < numeroPosti; i++) {

            while (true) {

                String scelta = leggiStringa(
                        "Inserisci il posto " + (i + 1)
                                + " (es. A1): "
                ).toUpperCase();

                if (scelta.length() < 2) {
                    System.out.println(
                            "Formato posto non valido."
                    );
                    continue;
                }

                char fila = scelta.charAt(0);
                int numero;

                try {

                    numero = Integer.parseInt(
                            scelta.substring(1)
                    );

                } catch (NumberFormatException e) {

                    System.out.println(
                            "Numero del posto non valido."
                    );

                    continue;
                }

                Posto posto = trovaPosto(
                        proiezione,
                        fila,
                        numero
                );

                if (posto == null) {
                    System.out.println(
                            "Posto inesistente."
                    );
                    continue;
                }

                if (posto.isOccupato()) {
                    System.out.println(
                            "Questo posto è già occupato."
                    );
                    continue;
                }

                if (postiPrenotati.contains(posto)) {
                    System.out.println(
                            "Hai già selezionato questo posto."
                    );
                    continue;
                }

                postiPrenotati.add(posto);
                break;
            }
        }

        String risultato = cliente.creaPrenotazione(
                proiezione,
                postiPrenotati
        );

        if (risultato.startsWith("Prenotazione ")) {

            salvaPrenotazioni();

            System.out.println("\n" + risultato);

            System.out.println(
                    "Posti prenotati: "
                            + formattaPosti(postiPrenotati)
            );

            System.out.println(
                    "Posti liberi rimasti: "
                            + proiezione.getPostiLiberi()
            );

        } else {

            System.out.println("\n" + risultato);
        }
    }

    /**
     * Cerca un posto nella sala associata a una proiezione.
     *
     * @param proiezione proiezione nella quale cercare
     * @param fila lettera della fila
     * @param numero numero del posto
     * @return il posto trovato oppure null
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

    /**
     * Restituisce in formato leggibile i posti selezionati.
     *
     * @param posti lista dei posti
     * @return stringa contenente i posti
     */
    private String formattaPosti(
            LinkedList<Posto> posti) {

        StringBuilder risultato =
                new StringBuilder();

        for (int i = 0; i < posti.size(); i++) {

            if (i > 0) {
                risultato.append(", ");
            }

            risultato.append(
                    posti.get(i).getLetteraFila()
            );

            risultato.append(
                    posti.get(i).getNumeroPosto()
            );
        }

        return risultato.toString();
    }

    /**
     * Salva nel file CSV tutte le prenotazioni presenti
     * nei clienti registrati.
     */
    private void salvaPrenotazioni() {

        GestorePrenotazioni gestore =
                new GestorePrenotazioni();

        try {

            gestore.salvaPrenotazioni(utenti);

        } catch (IOException e) {

            System.out.println(
                    "Errore durante il salvataggio delle prenotazioni: "
                            + e.getMessage()
            );
        }
    }
    /**
     * Gestisce il menu riservato ai clienti registrati.
     *
     * @param cliente cliente autenticato
     */
    private void menuCliente(ClienteRegistrato cliente) {

        boolean continua = true;

        while (continua && cliente.isLoggato()) {

            System.out.println();
            System.out.println("================================");
            System.out.println("       MENU CLIENTE");
            System.out.println("================================");
            System.out.println("1. Cerca proiezione e prenota");
            System.out.println("2. Visualizza prenotazioni");
            System.out.println("3. Modifica prenotazione");
            System.out.println("4. Elimina prenotazione");
            System.out.println("5. Logout");
            System.out.println("================================");

            int scelta = leggiIntero("Scelta: ");

            switch (scelta) {

                case 1:
                    prenotaProiezione(cliente);
                    break;

                case 2:
                    visualizzaPrenotazioni(cliente);
                    break;

                case 3:
                    modificaPrenotazione(cliente);
                    break;

                case 4:
                    eliminaPrenotazione(cliente);
                    break;

                case 5:
                    cliente.setLoggato(false);
                    System.out.println(
                            "Logout effettuato."
                    );
                    continua = false;
                    break;

                default:
                    System.out.println(
                            "Scelta non valida."
                    );
            }
        }
    }

    /**
     * Visualizza le prenotazioni effettuate dal cliente.
     *
     * @param cliente cliente autenticato
     */
    private void visualizzaPrenotazioni(ClienteRegistrato cliente) {

        LinkedList<Prenotazione> prenotazioni =
                cliente.getPrenotazioniCliente();

        if (prenotazioni == null
                || prenotazioni.isEmpty()) {

            System.out.println(
                    "\nNon hai effettuato nessuna prenotazione."
            );

            return;
        }

        System.out.println(
                "\n========== PRENOTAZIONI =========="
        );

        for (Prenotazione prenotazione :
                prenotazioni) {

            System.out.println(prenotazione);
        }
    }

    /**
     * Gestisce il menu riservato al proiezionista.
     *
     * @param proiezionista proiezionista autenticato
     */
    private void menuProiezionista(
            Proiezionista proiezionista) {

        boolean continua = true;

        while (continua && proiezionista.isLoggato()) {

            System.out.println();
            System.out.println("================================");
            System.out.println("    MENU PROIEZIONISTA");
            System.out.println("================================");
            System.out.println("1. Visualizza proiezioni");
            System.out.println("2. Aggiungi proiezione");
            System.out.println("3. Modifica data proiezione");
            System.out.println("4. Elimina proiezione");
            System.out.println("5. Logout");
            System.out.println("================================");

            int scelta = leggiIntero("Scelta: ");

            switch (scelta) {

                case 1:
                    visualizzaProiezioni();
                    break;

                case 2:
                    aggiungiProiezione(proiezionista);
                    break;

                case 3:
                    modificaDataProiezione(
                            proiezionista
                    );
                    break;

                case 4:
                    eliminaProiezione(
                            proiezionista
                    );
                    break;

                case 5:
                    proiezionista.logout();
                    continua = false;
                    System.out.println(
                            "Logout effettuato."
                    );
                    break;

                default:
                    System.out.println(
                            "Scelta non valida."
                    );
            }
        }
    }

    /**
     * Gestisce il menu riservato al bigliettaio.
     *
     * @param bigliettaio bigliettaio autenticato
     */
    private void menuBigliettaio(
            Bigliettaio bigliettaio) {

        boolean continua = true;

        while (continua && bigliettaio.isLoggato()) {

            System.out.println();
            System.out.println("================================");
            System.out.println("      MENU BIGLIETTAIO");
            System.out.println("================================");
            System.out.println(
                    "1. Visualizza prenotazioni di oggi"
            );
            System.out.println(
                    "2. Cerca prenotazione per ID"
            );
            System.out.println(
                    "3. Cerca prenotazione per nome e cognome"
            );
            System.out.println(
                    "4. Cerca prenotazione per ID, nome e cognome"
            );
            System.out.println("5. Logout");
            System.out.println("================================");

            int scelta = leggiIntero("Scelta: ");

            switch (scelta) {

                case 1:
                    ArrayList<Prenotazione> prenotazioni =
                            new ArrayList<>();

                    for (Utente utente : utenti) {
                        if (utente instanceof ClienteRegistrato) {
                            prenotazioni.addAll(
                                    ((ClienteRegistrato) utente)
                                            .getPrenotazioniCliente()
                            );
                        }
                    }

                    bigliettaio.visualizzaPrenotazioniOggi(
                            prenotazioni
                    );
                    break;

                case 2:
                    bigliettaio.cercaPerId(
                            utenti,
                            scanner
                    );
                    break;

                case 3:
                    bigliettaio.cercaPerNomeECognome(
                            utenti,
                            scanner
                    );
                    break;

                case 4:
                    bigliettaio.cercaPerIdNomeECognome(
                            utenti,
                            scanner
                    );
                    break;

                case 5:
                    bigliettaio.logout();
                    continua = false;
                    break;

                default:
                    System.out.println(
                            "Scelta non valida."
                    );
            }
        }
    }

    /**
     * Gestisce la ricerca delle proiezioni.
     *
     * @return proiezione trovata oppure null
     */
    private Proiezione cercaProiezione() {

        ProgrammazioneCinema programmazione =
                new ProgrammazioneCinema();

        for (Proiezione proiezione : proiezioni) {

            programmazione.aggiungiProiezione(
                    proiezione
            );
        }

        return programmazione.cercaProiezione(
                scanner
        );
    }

    /**
     * Visualizza tutte le proiezioni presenti nella programmazione.
     */
    private void visualizzaProiezioni() {

        if (proiezioni.isEmpty()) {

            System.out.println(
                    "Non ci sono proiezioni."
            );

            return;
        }

        System.out.println(
                "\n========== PROIEZIONI =========="
        );

        for (Proiezione proiezione :
                proiezioni) {

            System.out.println(proiezione);
        }
    }

    /**
     * Aggiunge una nuova proiezione alla programmazione.
     *
     * @param proiezionista proiezionista autenticato
     */
    private void aggiungiProiezione(
            Proiezionista proiezionista) {

        System.out.println(
                "\n========== NUOVA PROIEZIONE =========="
        );

        String titolo =
                leggiStringa("Titolo film: ");

        String genere =
                leggiStringa("Genere: ");

        String regista =
                leggiStringa("Regista: ");

        int anno =
                leggiIntero("Anno: ");

        int durata =
                leggiIntero(
                        "Durata in minuti: "
                );

        int etaMinima =
                leggiIntero(
                        "Età minima: "
                );

        LocalDateTime dataOra =
                leggiDataOra(
                        "Data e ora (AAAA-MM-GG HH:MM): "
                );

        double prezzo =
                leggiDouble(
                        "Prezzo biglietto: "
                );

        Film film = new Film(
                titolo,
                genere,
                regista,
                anno,
                durata,
                etaMinima
        );

        String id =
                generaNuovoIdProiezione();

        Proiezione proiezione =
                new Proiezione(
                        id,
                        film,
                        dataOra,
                        prezzo
                );

        if (proiezionista.aggiungiProiezioni(
                proiezione,
                proiezioni
        )) {
            System.out.println(
                    "Proiezione aggiunta con ID: "
                            + id
            );
        }
    }

    /**
     * Modifica la data e l'ora di una proiezione.
     *
     * @param proiezionista proiezionista autenticato
     */
    private void modificaDataProiezione(
            Proiezionista proiezionista) {

        visualizzaProiezioni();

        String id =
                leggiStringa(
                        "\nInserisci l'ID della proiezione da modificare: "
                );

        Proiezione proiezione =
                trovaProiezione(id);

        if (proiezione == null) {

            System.out.println(
                    "Proiezione non trovata."
            );

            return;
        }

        LocalDateTime nuovaDataOra =
                leggiDataOra(
                        "Nuova data e ora (AAAA-MM-GG HH:MM): "
                );

        if (proiezionista.cambiaData(
                proiezione,
                proiezioni,
                nuovaDataOra
        )) {
            System.out.println(
                    "Data della proiezione modificata con successo."
            );
        }
    }

    /**
     * Elimina una proiezione dalla programmazione.
     *
     * @param proiezionista proiezionista autenticato
     */
    private void eliminaProiezione(
            Proiezionista proiezionista) {

        visualizzaProiezioni();

        String id =
                leggiStringa(
                        "\nInserisci l'ID della proiezione da eliminare: "
                );

        Proiezione proiezione =
                trovaProiezione(id);

        if (proiezione == null) {

            System.out.println(
                    "Proiezione non trovata."
            );

            return;
        }

        String conferma =
                leggiStringa(
                        "Sei sicuro di voler eliminare "
                                + "la proiezione? (s/n): "
                );

        if (!conferma.equalsIgnoreCase("s")) {

            System.out.println(
                    "Operazione annullata."
            );

            return;
        }

        proiezionista.eliminaProiezione(
                proiezione,
                proiezioni
        );

        System.out.println(
                "Proiezione eliminata con successo."
        );
    }

    /**
     * Modifica una prenotazione del cliente.
     *
     * @param cliente cliente autenticato
     */
    private void modificaPrenotazione(
            ClienteRegistrato cliente) {

        System.out.println(
                "\n========== MODIFICA PRENOTAZIONE =========="
        );

        String id =
                leggiStringa(
                        "ID prenotazione: "
                );

        Prenotazione prenotazione =
                trovaPrenotazioneCliente(
                        cliente,
                        id
                );

        if (prenotazione == null) {

            System.out.println(
                    "Prenotazione non trovata."
            );

            return;
        }

        LocalDateTime nuovaDataOra =
                leggiDataOra(
                        "Nuova data e ora (AAAA-MM-GG HH:MM): "
                );

        ProgrammazioneCinema programmazione =
                new ProgrammazioneCinema();

        for (Proiezione proiezione :
                proiezioni) {

            programmazione.aggiungiProiezione(
                    proiezione
            );
        }

        cliente.modificaPrenotazione(
                prenotazione,
                nuovaDataOra,
                programmazione
        );
    }

    /**
     * Elimina una prenotazione effettuata dal cliente.
     *
     * @param cliente cliente autenticato
     */
    private void eliminaPrenotazione(
            ClienteRegistrato cliente) {

        System.out.println(
                "\n========== ELIMINA PRENOTAZIONE =========="
        );

        String id =
                leggiStringa(
                        "ID prenotazione da eliminare: "
                );

        Prenotazione prenotazione =
                trovaPrenotazioneCliente(
                        cliente,
                        id
                );

        if (prenotazione == null) {

            System.out.println(
                    "Prenotazione non trovata."
            );

            return;
        }

        String conferma =
                leggiStringa(
                        "Confermare eliminazione? (s/n): "
                );

        if (conferma.equalsIgnoreCase("s")) {

            boolean eliminata =
                    cliente.eliminaPrenotazione(
                            prenotazione
                    );

            if (eliminata) {

                salvaPrenotazioni();

                System.out.println(
                        "Prenotazione eliminata."
                );
            }

        } else {

            System.out.println(
                    "Operazione annullata."
            );
        }
    }

    /**
     * Cerca un utente tramite username.
     *
     * @param username username da cercare
     * @return l'utente trovato oppure null
     */
    private Utente trovaUtente(
            String username) {

        for (Utente utente : utenti) {

            if (utente.getUsername()
                    .equalsIgnoreCase(username)) {

                return utente;
            }
        }

        return null;
    }

    /**
     * Cerca una proiezione tramite identificativo.
     *
     * @param id identificativo della proiezione
     * @return la proiezione trovata oppure null
     */
    private Proiezione trovaProiezione(
            String id) {

        for (Proiezione proiezione :
                proiezioni) {

            if (proiezione.getId()
                    .equalsIgnoreCase(id)) {

                return proiezione;
            }
        }

        return null;
    }

    /**
     * Cerca una prenotazione tra quelle effettuate dal cliente.
     *
     * @param cliente cliente proprietario della prenotazione
     * @param id identificativo della prenotazione
     * @return la prenotazione trovata oppure null
     */
    private Prenotazione trovaPrenotazioneCliente(
            ClienteRegistrato cliente,
            String id) {

        LinkedList<Prenotazione> prenotazioni =
                cliente.getPrenotazioniCliente();

        if (prenotazioni == null) {
            return null;
        }

        for (Prenotazione prenotazione :
                prenotazioni) {

            if (prenotazione.getIdPrenotazione()
                    .equalsIgnoreCase(id)) {

                return prenotazione;
            }
        }

        return null;
    }

    /**
     * Genera un nuovo identificativo numerico per una proiezione.
     *
     * @return nuovo identificativo della proiezione
     */
    private String generaNuovoIdProiezione() {

        int massimo = 0;

        for (Proiezione proiezione :
                proiezioni) {

            try {

                int id = Integer.parseInt(
                        proiezione.getId()
                );

                if (id > massimo) {
                    massimo = id;
                }

            } catch (NumberFormatException ignored) {
                // Ignora eventuali ID non numerici.
            }
        }

        return String.valueOf(
                massimo + 1
        );
    }

    /**
     * Legge un numero intero da input.
     *
     * @param messaggio messaggio mostrato all'utente
     * @return intero inserito dall'utente
     */
    private int leggiIntero(
            String messaggio) {

        while (true) {

            System.out.print(messaggio);

            String input =
                    scanner.nextLine();

            try {

                return Integer.parseInt(
                        input
                );

            } catch (NumberFormatException e) {

                System.out.println(
                        "Inserisci un numero intero valido."
                );
            }
        }
    }

    /**
     * Legge un numero decimale da input.
     *
     * @param messaggio messaggio mostrato all'utente
     * @return numero decimale inserito dall'utente
     */
    private double leggiDouble(
            String messaggio) {

        while (true) {

            System.out.print(messaggio);

            String input =
                    scanner.nextLine()
                            .replace(',', '.');

            try {

                return Double.parseDouble(
                        input
                );

            } catch (NumberFormatException e) {

                System.out.println(
                        "Inserisci un numero valido."
                );
            }
        }
    }

    /**
     * Legge una stringa non vuota da input.
     *
     * @param messaggio messaggio mostrato all'utente
     * @return stringa inserita dall'utente
     */
    private String leggiStringa(
            String messaggio) {

        while (true) {

            System.out.print(messaggio);

            String input =
                    scanner.nextLine()
                            .trim();

            if (!input.isEmpty()) {
                return input;
            }

            System.out.println(
                    "Il campo non può essere vuoto."
            );
        }
    }

    /**
     * Legge una data nel formato ISO.
     *
     * @param messaggio messaggio mostrato all'utente
     * @return data inserita dall'utente
     */
    private LocalDate leggiData(
            String messaggio) {

        while (true) {

            String input =
                    leggiStringa(messaggio);

            try {

                return LocalDate.parse(
                        input
                );

            } catch (DateTimeParseException e) {

                System.out.println(
                        "Formato non valido. "
                                + "Usa AAAA-MM-GG."
                );
            }
        }
    }

    /**
     * Legge una data e ora nel formato utilizzato dall'applicazione.
     *
     * @param messaggio messaggio mostrato all'utente
     * @return data e ora inserite dall'utente
     */
    private LocalDateTime leggiDataOra(
            String messaggio) {

        while (true) {

            String input =
                    leggiStringa(messaggio);

            try {

                return LocalDateTime.parse(
                        input.replace(
                                ' ',
                                'T'
                        )
                );

            } catch (DateTimeParseException e) {

                System.out.println(
                        "Formato non valido. "
                                + "Usa AAAA-MM-GG HH:MM."
                );
            }
        }
    }

    /**
     * Metodo principale dell'applicazione CineMax.
     *
     * @param args argomenti passati da riga di comando
     */
    public static void main(String[] args) {

        CineMax cineMax =
                new CineMax();

        cineMax.avvia();
    }
}
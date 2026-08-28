/*
 * Edoardo Carducci - 764215 - Varese
 * Daniele Rossetti - 767980 - Varese
 * Riccardo Palomba - 764224 - Varese
 */

package cinemax;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Gestisce i menu e le principali interazioni dell'utente con il sistema CineMax.
 *
 * <p>La classe gestisce il menu principale e i menu specifici per ciascun
 * tipo di utente, permettendo di effettuare operazioni come login,
 * registrazione, gestione delle prenotazioni e gestione delle proiezioni.</p>
 *
 * @author Edoardo Carducci, Daniele Rossetti
 * @version 1.0
 */
public class Menu {

    /**
     * Scanner utilizzato per leggere gli input dell'utente.
     */
    private final Scanner scanner;

    /**
     * Lista degli utenti registrati al sistema.
     */
    private final List<Utente> utenti;

    /**
     * Lista delle proiezioni disponibili nel cinema.
     */
    private final List<Proiezione> proiezioni;

    /**
     * Programmazione delle proiezioni del cinema.
     */
    private final ProgrammazioneCinema programmazione;

    /**
     * Gestore degli utenti del sistema.
     */
    private final GestoreUtenti gestoreUtenti;

    /**
     * Gestore delle prenotazioni del sistema.
     */
    private final GestorePrenotazioni gestorePrenotazioni;

    /**
     * Costruisce il menu principale del sistema.
     *
     * @param scanner scanner utilizzato per leggere gli input
     * @param utenti lista degli utenti registrati
     * @param proiezioni lista delle proiezioni disponibili
     * @param programmazione programmazione del cinema
     * @param gestoreUtenti gestore degli utenti
     * @param gestorePrenotazioni gestore delle prenotazioni
     */
    public Menu(Scanner scanner, List<Utente> utenti, List<Proiezione> proiezioni,
            ProgrammazioneCinema programmazione, GestoreUtenti gestoreUtenti, GestorePrenotazioni gestorePrenotazioni) {

        this.scanner = scanner;
        this.utenti = utenti;
        this.proiezioni = proiezioni;
        this.programmazione = programmazione;
        this.gestoreUtenti = gestoreUtenti;
        this.gestorePrenotazioni = gestorePrenotazioni;
    }

    /**
     * Avvia il menu principale dell'applicazione.
     *
     * <p>Permette all'utente di effettuare il login, registrarsi,
     * continuare come ospite oppure uscire dall'applicazione.</p>
     */
    public void avvia() {
        boolean esci = false;

        while (!esci) {
            stampaMenuPrincipale();

            if (!scanner.hasNextInt()) {
                System.out.println("Carattere inserito non valido!");
                scanner.nextLine();
                continue;
            }

            int scelta = scanner.nextInt();
            scanner.nextLine();

            switch (scelta) {
                case 1:
                    gestisciLogin();
                    break;
                case 2:
                    registraCliente();
                    break;
                case 3:
                    menuOspite();
                    break;
                case 4:
                    esci = true;
                    break;
                default:
                    System.out.println("Scelta non valida");
            }
        }
    }

    /**
     * Visualizza il menu principale dell'applicazione.
     */
    private void stampaMenuPrincipale() {

        System.out.println("\n1. Login");
        System.out.println("2. Registrazione");
        System.out.println("3. Continua come ospite");
        System.out.println("4. Esci");
        System.out.println("Scegli un'opzione: ");
    }

    /**
     * Gestisce la procedura di login dell'utente.
     *
     * <p>Richiede username e password, verifica le credenziali
     * e, in caso di autenticazione corretta, avvia il menu
     * corrispondente al tipo di utente.</p>
     */
    private void gestisciLogin() {
        System.out.println("\n----LOGIN----");
        System.out.println("Inserisci username: ");
        String username = scanner.nextLine();

        System.out.println("Inserisci password: ");
        String password = scanner.nextLine();

        String passwordHash = PasswordUtil.hashPassword(password);
        Utente utenteLoggato = trovaUtente(username, passwordHash);

        if (utenteLoggato == null) {
            System.out.println("Username o password errati");
            return;
        }

        utenteLoggato.setLoggato(true);
        System.out.println("Login effettuato");

        if (utenteLoggato instanceof ClienteRegistrato) {
            menuCliente((ClienteRegistrato) utenteLoggato);
        } else if (utenteLoggato instanceof Proiezionista) {
            menuProiezionista((Proiezionista) utenteLoggato);
        } else if (utenteLoggato instanceof Bigliettaio) {
            menuBigliettaio((Bigliettaio) utenteLoggato);
        }

        utenteLoggato.setLoggato(false);
    }

    /**
     * Cerca un utente nella lista utilizzando username e password cifrata.
     *
     * @param username username dell'utente da cercare
     * @param passwordHash hash della password dell'utente
     * @return l'utente corrispondente alle credenziali oppure {@code null}
     *         se non viene trovato
     */
    private Utente trovaUtente(String username, String passwordHash) {
        for (Utente tmp : utenti) {
            if (tmp.getUsername().equals(username)
                    && tmp.getPassword().equals(passwordHash)) {
                return tmp;
            }
        }

        return null;
    }

    /**
     * Gestisce il menu riservato ai clienti registrati.
     *
     * <p>Permette di cercare proiezioni, creare, visualizzare,
     * modificare ed eliminare prenotazioni e effettuare il logout.</p>
     *
     * @param cliente cliente registrato che ha effettuato il login
     */
    private void menuCliente(ClienteRegistrato cliente) {
        boolean logout = false;

        while (!logout) {
            System.out.println("\n----MENU' CLIENTE----");
            System.out.println("1. Cerca una Proiezione");
            System.out.println("2. Crea una prenotazione");
            System.out.println("3. Visualizza le tue prenotazioni");
            System.out.println("4. Modifica una prenotazione");
            System.out.println("5. Elimina una prenotazione");
            System.out.println("6. Logout");
            System.out.println("Scegli un'opzione: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Inserisci un numero valido!");
                scanner.nextLine();
                continue;
            }

            int scelta = scanner.nextInt();
            scanner.nextLine();

            switch (scelta) {
                case 1:
                    programmazione.cercaProiezione(scanner);
                    break;
                case 2:
                    creaPrenotazione(cliente);
                    break;
                case 3:
                    visualizzaPrenotazioni(cliente);
                    break;
                case 4:
                    modificaPrenotazione(cliente);
                    break;
                case 5:
                    eliminaPrenotazione(cliente);
                    break;
                case 6:
                    logout = true;
                    System.out.println("Logout effettuato");
                    break;
                default:
                    System.out.println("Opzione non valida!");
            }
        }
    }

    /**
     * Gestisce la creazione di una nuova prenotazione per un cliente.
     *
     * <p>Permette al cliente di selezionare una proiezione e i posti
     * da prenotare, verificandone la validità prima di creare
     * la prenotazione.</p>
     *
     * @param cliente cliente che effettua la prenotazione
     */
    private void creaPrenotazione(ClienteRegistrato cliente) {
        System.out.println("----PRENOTA UNA PROIEZIONE----");

        if (proiezioni == null || proiezioni.isEmpty()) {
            System.out.println("Non ci sono proiezioni disponibili al momento.");
            return;
        }

        System.out.println("Proiezioni disponibili:");

        for (int i = 0; i < proiezioni.size(); i++) {
            Proiezione p = proiezioni.get(i);
            System.out.println((i + 1) + ") " + p.getFilm().getTitolo() + " - " + p.getDataOra());
        }

        System.out.println("Seleziona il numero della proiezione:");

        if (!scanner.hasNextInt()) {
            System.out.println("Numero non valido!");
            scanner.nextLine();
            return;
        }

        int indicePro = scanner.nextInt() - 1;
        scanner.nextLine();

        if (indicePro < 0 || indicePro >= proiezioni.size()) {
            System.out.println("Proiezione non valida!");
            return;
        }

        Proiezione proiezioneSelezionata = proiezioni.get(indicePro);

        System.out.println("Quanti posti vuoi prenotare?");

        if (!scanner.hasNextInt()) {
            System.out.println("Numero non valido!");
            scanner.nextLine();
            return;
        }

        int numPosti = scanner.nextInt();
        scanner.nextLine();

        if (numPosti <= 0) {
            System.out.println("Il numero di posti deve essere maggiore di zero.");
            return;
        }

        LinkedList<Posto> postiDaPrenotare = new LinkedList<>();
        boolean postiValidi = true;

        for (int i = 0; i < numPosti; i++) {
            System.out.println("Posto " + (i + 1) + " - Inserisci Fila (es. A): ");

            char fila = CineMax.leggiFilaValida(scanner);

            System.out.println("Posto " + (i + 1) + " - Inserisci Numero posto:");

            if (!scanner.hasNextInt()) {
                System.out.println("Numero non valido!");
                scanner.nextLine();
                postiValidi = false;
                break;
            }

            int numero = scanner.nextInt();
            scanner.nextLine();

            Posto postoTrovato = null;

            for (Posto tmp : proiezioneSelezionata.getPosti()) {
                if (tmp.getLetteraFila() == fila && tmp.getNumeroPosto() == numero) {
                    postoTrovato = tmp;
                    break;
                }
            }

            if (postoTrovato == null) {
                System.out.println("Il posto " + fila + numero + " non esiste!");
                postiValidi = false;
                break;
            }

            postiDaPrenotare.add(postoTrovato);
        }

        if (!postiValidi) {
            System.out.println("Prenotazione annullata a causa di un errore nei posti inseriti");
            return;
        }

        String esito = cliente.creaPrenotazione(proiezioneSelezionata, postiDaPrenotare);

        System.out.println(esito);

        if (esito.contains("completata")) {
            salvaPrenotazioni();
        }
    }

    /**
     * Visualizza le prenotazioni effettuate dal cliente.
     *
     * @param cliente cliente di cui visualizzare le prenotazioni
     */
    private void visualizzaPrenotazioni(ClienteRegistrato cliente) {
        System.out.println("----LE TUE PRENOTAZIONI----");
        System.out.println(cliente.visualizzaPrenotazione());
    }

    /**
     * Gestisce la modifica di una prenotazione del cliente.
     *
     * <p>Permette al cliente di selezionare una delle proprie
     * prenotazioni e modificarne la data e l'ora.</p>
     *
     * @param cliente cliente proprietario della prenotazione
     */
    private void modificaPrenotazione(ClienteRegistrato cliente) {
        LinkedList<Prenotazione> prenotazioni =
                cliente.getPrenotazioniCliente();

        System.out.println("----MODIFICA PRENOTAZIONE----");

        if (prenotazioni.isEmpty()) {
            System.out.println("Non hai prenotazioni da modificare");
            return;
        }

        for (int i = 0; i < prenotazioni.size(); i++) {
            System.out.println((i + 1) + ") " + prenotazioni.get(i));
        }

        System.out.println("Seleziona il numero della prenotazione da modificare:");

        if (!scanner.hasNextInt()) {
            System.out.println("Inserisci un numero valido.");
            scanner.nextLine();
            return;
        }

        int scelta = scanner.nextInt();
        scanner.nextLine();

        if (scelta < 1 || scelta > prenotazioni.size()) {
            System.out.println("Numero prenotazione non valido");
            return;
        }

        Prenotazione prenotazione = prenotazioni.get(scelta - 1);

        System.out.println("Inserisci la nuova data e ora (AAAA-MM-GG HH:MM):");

        String nuovaDataString = scanner.nextLine();
        LocalDateTime nuovaDataOra;

        try {
            nuovaDataOra = LocalDateTime.parse(nuovaDataString.replace(" ", "T"));
        } catch (Exception e) {
            System.out.println("Formato data non valido.");
            return;
        }

        boolean modificata = cliente.modificaPrenotazione(prenotazione, nuovaDataOra, programmazione);

        if (modificata) {
            salvaPrenotazioni();
        }
    }

    /**
     * Elimina una prenotazione appartenente al cliente.
     *
     * @param cliente cliente proprietario della prenotazione
     */
    private void eliminaPrenotazione(ClienteRegistrato cliente) {
        LinkedList<Prenotazione> prenotazioni = cliente.getPrenotazioniCliente();

        if (prenotazioni.isEmpty()) {
            System.out.println("Non hai prenotazioni da eliminare");
            return;
        }

        for (int i = 0; i < prenotazioni.size(); i++) {
            System.out.println((i + 1) + ") " + prenotazioni.get(i));
        }

        System.out.println("Seleziona il numero della prenotazione da eliminare:");

        if (!scanner.hasNextInt()) {
            System.out.println("Inserisci un numero valido.");
            scanner.nextLine();
            return;
        }

        int scelta = scanner.nextInt();
        scanner.nextLine();

        if (scelta < 1 || scelta > prenotazioni.size()) {
            System.out.println("Numero prenotazione non valido");
            return;
        }

        Prenotazione prenotazione = prenotazioni.get(scelta - 1);

        cliente.eliminaPrenotazione(prenotazione);
        salvaPrenotazioni();
    }

    /**
     * Gestisce il menu riservato al proiezionista.
     *
     * <p>Permette di aggiungere, modificare ed eliminare proiezioni
     * e di effettuare il logout.</p>
     *
     * @param proiezionista proiezionista che ha effettuato il login
     */
    private void menuProiezionista(Proiezionista proiezionista) {
        boolean logoutPro = false;

        while (!logoutPro) {
            System.out.println("\n----MENU' PROIEZIONISTA----");
            System.out.println("1. Aggiungi proiezione");
            System.out.println("2. Modifica proiezione");
            System.out.println("3. Elimina proiezione");
            System.out.println("4. Logout");
            System.out.println("Scegli un'opzione:");

            if (!scanner.hasNextInt()) {
                System.out.println("Inserisci un numero valido!");
                scanner.nextLine();
                continue;
            }

            int scelta = scanner.nextInt();
            scanner.nextLine();

            switch (scelta) {
                case 1:
                    aggiungiProiezione(proiezionista);
                    break;
                case 2:
                    modificaProiezione(proiezionista);
                    break;
                case 3:
                    eliminaProiezione(proiezionista);
                    break;
                case 4:
                    logoutPro = true;
                    System.out.println("Logout effettuato");
                    break;
                default:
                    System.out.println("Opzione non valida!");
            }
        }
    }

    /**
     * Permette al proiezionista di aggiungere una nuova proiezione.
     *
     * <p>Richiede i dati del film, della proiezione e del prezzo,
     * crea gli oggetti necessari e aggiunge la nuova proiezione
     * al sistema.</p>
     *
     * @param proiezionista proiezionista che aggiunge la proiezione
     */
    private void aggiungiProiezione(Proiezionista proiezionista) {
        System.out.println("\n----AGGIUNGI PROIEZIONE----");

        System.out.println("Titolo del film: ");
        String titolo = scanner.nextLine();
        if (!controllaInput(titolo)) {
            return;
        }

        System.out.println("Genere: ");
        String genere = scanner.nextLine();
        if (!controllaInput(genere)) {
            return;
        }

        System.out.println("Regista: ");
        String regista = scanner.nextLine();
        if (!controllaInput(regista)) {
            return;
        }

        System.out.print("Anno di uscita: ");
        if (!scanner.hasNextInt()) {
            System.out.println("Anno non valido!");
            scanner.nextLine();
            return;
        }

        int anno = scanner.nextInt();
        scanner.nextLine();
        if (anno <= 0) {
            System.out.println("Anno non valido!");
            return;
        }

        System.out.println("Durata del film (in minuti): ");
        if (!scanner.hasNextInt()) {
            System.out.println("Durata non valida!");
            scanner.nextLine();
            return;
        }

        int durata = scanner.nextInt();
        scanner.nextLine();
        if (durata <= 0) {
            System.out.println("Durata non valida!");
            return;
        }

        System.out.print("Età minima richiesta: ");
        if (!scanner.hasNextInt()) {
            System.out.println("Età minima non valida!");
            scanner.nextLine();
            return;
        }

        int etaMinima = scanner.nextInt();
        scanner.nextLine();
        if (etaMinima < 0) {
            System.out.println("Età minima non valida!");
            return;
        }

        System.out.print("ID proiezione : ");
        String idProiezione = scanner.nextLine();
        if (!controllaInput(idProiezione)) {
            return;
        }

        System.out.print("Prezzo biglietto: ");
        if (!scanner.hasNextDouble()) {
            System.out.println("Prezzo non valido!");
            scanner.nextLine();
            return;
        }

        double prezzo = scanner.nextDouble();
        scanner.nextLine();
        if (prezzo <= 0) {
            System.out.println("Prezzo non valido!");
            return;
        }

        System.out.print("Inserisci data e ora (AAAA-MM-GG HH:MM): ");

        String dataOraString = scanner.nextLine();
        LocalDateTime dataOra;

        try {
            dataOra = LocalDateTime.parse(dataOraString.replace(" ", "T"));
        } catch (Exception e) {
            System.out.println("Formato data non valido!");
            return;
        }

        Film film = new Film(titolo, genere, regista, anno, durata, etaMinima);

        Proiezione nuovaProiezione = new Proiezione(idProiezione, film, dataOra, prezzo);

        boolean aggiunta = proiezionista.aggiungiProiezioni(nuovaProiezione, proiezioni);

        if (aggiunta) {
            System.out.println("Proiezione aggiunta con successo!");
        }
    }

    /**
     * Gestisce la modifica di una proiezione esistente.
     *
     * @param proiezionista proiezionista che modifica la proiezione
     */
    private void modificaProiezione(Proiezionista proiezionista) {
        System.out.println("\n----MODIFICA PROIEZIONE----");

        if (proiezioni.isEmpty()) {
            System.out.println(
                    "Nessuna proiezione disponibile da modificare."
            );
            return;
        }

        stampaProiezioni();

        System.out.print("Seleziona il numero della proiezione da modificare: ");

        if (!scanner.hasNextInt()) {
            System.out.println("Numero non valido!");
            scanner.nextLine();
            return;
        }

        int indice = scanner.nextInt() - 1;
        scanner.nextLine();

        if (indice < 0 || indice >= proiezioni.size()) {
            System.out.println("Proiezione non valida!");
            return;
        }

        Proiezione proiezione = proiezioni.get(indice);

        System.out.println("\nChe cosa vuoi modificare?");
        System.out.println("1. Data e Ora");
        System.out.println("2. Prezzo del biglietto");
        System.out.print("Scegli un'opzione: ");

        if (!scanner.hasNextInt()) {
            System.out.println("Opzione non valida!");
            scanner.nextLine();
            return;
        }

        int scelta = scanner.nextInt();
        scanner.nextLine();

        switch (scelta) {
            case 1:
                modificaDataProiezione(proiezionista, proiezione);
                break;
            case 2:
                modificaPrezzoProiezione(proiezione);
                break;
            default:
                System.out.println("Scelta non valida!");
        }
    }

    /**
     * Modifica la data e l'ora di una proiezione.
     *
     * @param proiezionista proiezionista che effettua la modifica
     * @param proiezione proiezione di cui modificare data e ora
     */
    private void modificaDataProiezione(
            Proiezionista proiezionista,
            Proiezione proiezione) {

        System.out.print("Inserisci la nuova data e ora (AAAA-MM-GG HH:MM): ");

        String nuovaDataString = scanner.nextLine();
        LocalDateTime nuovaData;

        try {
            nuovaData = LocalDateTime.parse(nuovaDataString.replace(" ", "T"));
        } catch (Exception e) {
            System.out.println("Formato data non valido!");
            return;
        }

        boolean dataCambiata = proiezionista.cambiaData(proiezione, proiezioni, nuovaData);

        if (dataCambiata) {
            System.out.println("Data e ora aggiornate con successo!");
        }
    }

    /**
     * Modifica il prezzo del biglietto di una proiezione.
     *
     * @param proiezione proiezione di cui modificare il prezzo
     */
    private void modificaPrezzoProiezione(Proiezione proiezione) {
        System.out.print("Inserisci il nuovo prezzo: ");

        if (!scanner.hasNextDouble()) {
            System.out.println("Prezzo non valido!");
            scanner.nextLine();
            return;
        }

        double nuovoPrezzo = scanner.nextDouble();
        scanner.nextLine();

        proiezione.setPrezzoBiglietto(nuovoPrezzo);
        GestoreProiezioni.scriviProiezioni(proiezioni);

        System.out.println("Prezzo aggiornato e salvato con successo!");
    }

    /**
     * Elimina una proiezione dalla lista delle proiezioni disponibili.
     *
     * @param proiezionista proiezionista che effettua l'eliminazione
     */
    private void eliminaProiezione(Proiezionista proiezionista) {
        System.out.println("\n----ELIMINA PROIEZIONE----");

        if (proiezioni.isEmpty()) {
            System.out.println("Nessuna proiezione presente");
            return;
        }

        for (int i = 0; i < proiezioni.size(); i++) {
            Proiezione p = proiezioni.get(i);
            System.out.println((i + 1) + ") " + p.getFilm().getTitolo() + " - " + p.getDataOra());
        }

        System.out.println("Seleziona la proiezione da eliminare: ");

        if (!scanner.hasNextInt()) {
            System.out.println("Opzione non valida!");
            scanner.nextLine();
            return;
        }

        int indice = scanner.nextInt() - 1;
        scanner.nextLine();

        if (indice < 0 || indice >= proiezioni.size()) {
            System.out.println("Proiezione non valida!");
            return;
        }

        Proiezione proiezione = proiezioni.get(indice);

        proiezionista.eliminaProiezione(proiezione, proiezioni);

        System.out.println("Proiezione eliminata con successo");
    }

    /**
     * Stampa a video l'elenco delle proiezioni disponibili,
     * mostrando ID, titolo, data, prezzo e durata.
     */
    private void stampaProiezioni() {
        for (int i = 0; i < proiezioni.size(); i++) {
            Proiezione p = proiezioni.get(i);

            System.out.println(
                    (i + 1) +
                            ") ID: " + p.getId() +
                            " - " + p.getFilm().getTitolo() +
                            " | Data: " + p.getDataOra() +
                            " | Prezzo: " + p.getPrezzoBiglietto() +
                            "€ | Durata: " + p.getFilm().getDurata()
            );
        }
    }

    /**
     * Gestisce il menu riservato al bigliettaio.
     *
     * <p>Permette di cercare proiezioni, visualizzare e cercare
     * prenotazioni ed effettuare il logout.</p>
     *
     * @param bigliettaio bigliettaio che ha effettuato il login
     */
    private void menuBigliettaio(Bigliettaio bigliettaio) {
        boolean logoutBig = false;

        while (!logoutBig) {
            System.out.println("\n----MENU' BIGLIETTAIO----");
            System.out.println("1. Cerca proiezione");
            System.out.println("2. Visualizza prenotazioni");
            System.out.println("3. Logout");
            System.out.println("Scegli un'opzione:");

            if (!scanner.hasNextInt()) {
                System.out.println("Inserisci un numero valido!");
                scanner.nextLine();
                continue;
            }

            int scelta = scanner.nextInt();
            scanner.nextLine();

            switch (scelta) {
                case 1:
                    programmazione.cercaProiezione(scanner);
                    break;
                case 2:
                    ricercaPrenotazioniBigliettaio(bigliettaio);
                    break;
                case 3:
                    logoutBig = true;
                    System.out.println("Logout effettuato");
                    break;
                default:
                    System.out.println("Opzione non valida!");
            }
        }
    }

    /**
     * Gestisce le diverse modalità di ricerca delle prenotazioni
     * disponibili per il bigliettaio.
     *
     * @param bigliettaio bigliettaio che effettua la ricerca
     */
    private void ricercaPrenotazioniBigliettaio(
            Bigliettaio bigliettaio) {

        System.out.println(
                "\n----VISUALIZZA / CERCA PRENOTAZIONI----"
        );
        System.out.println("1. Visualizza prenotazioni di oggi");
        System.out.println("2. Cerca per ID");
        System.out.println("3. Cerca per nome e cognome");
        System.out.println("4. Cerca per ID, nome e cognome");
        System.out.println("Scegli un'opzione: ");

        if (!scanner.hasNextInt()) {
            System.out.println("Inserisci un numero valido!");
            scanner.nextLine();
            return;
        }

        int scelta = scanner.nextInt();
        scanner.nextLine();

        switch (scelta) {
            case 1:
                LinkedList<Prenotazione> tuttePrenotazioni = new LinkedList<>();

                for (Utente utente : utenti) {
                    if (utente instanceof ClienteRegistrato) {
                        tuttePrenotazioni.addAll(((ClienteRegistrato) utente).getPrenotazioniCliente());
                    }
                }

                bigliettaio.visualizzaPrenotazioniOggi(tuttePrenotazioni);
                break;

            case 2:
                bigliettaio.cercaPerId(utenti, scanner);
                break;

            case 3:
                bigliettaio.cercaPerNomeECognome(utenti, scanner);
                break;

            case 4:
                bigliettaio.cercaPerIdNomeECognome(utenti, scanner);
                break;

            default:
                System.out.println("Opzione non valida!");
        }
    }

    /**
     * Gestisce il menu disponibile agli utenti ospiti.
     *
     * <p>L'ospite può cercare e visualizzare i dettagli delle
     * proiezioni disponibili oppure tornare al menu principale.</p>
     */
    private void menuOspite() {
        boolean back = false;

        while (!back) {
            System.out.println("\n----MENU' OSPITE----");
            System.out.println("1. Cerca una proiezione");
            System.out.println("2. Torna al menù principale");
            System.out.println("Scegli un'opzione: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Inserisci un valore valido!");
                scanner.nextLine();
                continue;
            }

            int scelta = scanner.nextInt();
            scanner.nextLine();

            switch (scelta) {
                case 1:
                    System.out.println("----CERCA UNA PROIEZIONE E VISUALIZZA I DETTAGLI----");
                    programmazione.cercaProiezione(scanner);
                    break;
                case 2:
                    back = true;
                    break;

                default:
                    System.out.println("Opzione non valida!");
            }
        }
    }

    /**
     * Gestisce la registrazione di un nuovo cliente.
     *
     * <p>Richiede i dati personali, le credenziali e il domicilio
     * del nuovo cliente, verificando la validità degli input e
     * l'unicità dello username.</p>
     */
    private void registraCliente() {
        System.out.println("---- REGISTRAZIONE ----");

        System.out.println("Nome: ");
        String nome = scanner.nextLine();

        if (!controllaInput(nome)) {
            return;
        }

        System.out.println("Cognome: ");
        String cognome = scanner.nextLine();

        if (!controllaInput(cognome)) {
            return;
        }

        System.out.println("Username: ");
        String username = scanner.nextLine();

        if (!controllaInput(username)) {
            return;
        }

        for (Utente utente : utenti) {
            if (utente.getUsername().equals(username)) {
                System.out.println("Username già presente");
                return;
            }
        }

        System.out.println("Password: ");
        String password = scanner.nextLine();

        String hashPassword = PasswordUtil.hashPassword(password);

        LocalDate dataDiNascita;

        try {
            System.out.println("Data di nascita (AAAA-MM-GG): ");
            dataDiNascita = LocalDate.parse(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("Data non valida.");
            return;
        }

        System.out.println("Via (Facoltativo): ");
        String via = scanner.nextLine();

        if (!controllaInput(via)) {
            return;
        }

        System.out.println("Numero civico (Facoltativo): ");
        String numeroCivico = scanner.nextLine();

        if (!controllaInput(numeroCivico)) {
            return;
        }

        System.out.println("CAP (Facoltativo): ");
        String cap = scanner.nextLine();

        if (!controllaInput(cap)) {
            return;
        }

        System.out.println("Città (Facoltativo): ");
        String citta = scanner.nextLine();

        if (!controllaInput(citta)) {
            return;
        }

        System.out.println("Provincia (Facoltativo): ");
        String provincia = scanner.nextLine();

        if (!controllaInput(provincia)) {
            return;
        }

        Domicilio domicilio = new Domicilio(via, numeroCivico, cap, citta, provincia);

        ClienteRegistrato nuovoCliente = new ClienteRegistrato(nome, cognome, username,
                                hashPassword, dataDiNascita, domicilio, Ruolo.CLIENTE);

        utenti.add(nuovoCliente);
        gestoreUtenti.salvaUtenti();

        System.out.println("Registrazione completata");
    }

    /**
     * Salva su file le prenotazioni presenti nel sistema.
     *
     * <p>In caso di errore durante il salvataggio, viene mostrato
     * un messaggio informativo all'utente.</p>
     */
    private void salvaPrenotazioni() {
        try {
            gestorePrenotazioni.salvaPrenotazioni(utenti);
        } catch (Exception e) {
            System.out.println("Errore durante il salvataggio della prenotazione.");
        }
    }

    /**
     * Controlla che una stringa non contenga i caratteri utilizzati
     * come separatori nei dati memorizzati.
     *
     * @param s stringa da controllare
     * @return {@code true} se la stringa è valida, {@code false}
     *         se contiene una virgola o un punto e virgola
     */
    private static boolean controllaInput(String s){
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i)==';' || s.charAt(i)==','){
                System.out.println("La stringa inserita contiente caratteri non validi");
                return false;
            }
        }
        return true;
    }
}

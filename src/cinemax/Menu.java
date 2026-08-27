package cinemax;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Menu {

    private final Scanner scanner;
    private final List<Utente> utenti;
    private final List<Proiezione> proiezioni;
    private final ProgrammazioneCinema programmazione;
    private final GestoreUtenti gestoreUtenti;
    private final GestorePrenotazioni gestorePrenotazioni;

    public Menu(
            Scanner scanner,
            List<Utente> utenti,
            List<Proiezione> proiezioni,
            ProgrammazioneCinema programmazione,
            GestoreUtenti gestoreUtenti,
            GestorePrenotazioni gestorePrenotazioni) {

        this.scanner = scanner;
        this.utenti = utenti;
        this.proiezioni = proiezioni;
        this.programmazione = programmazione;
        this.gestoreUtenti = gestoreUtenti;
        this.gestorePrenotazioni = gestorePrenotazioni;
    }

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

    private void stampaMenuPrincipale() {
        System.out.println();
        System.out.println("1. Login");
        System.out.println("2. Registrazione");
        System.out.println("3. Continua come ospite");
        System.out.println("4. Esci");
        System.out.println("Scegli un'opzione: ");
    }

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

    private Utente trovaUtente(String username, String passwordHash) {
        for (Utente utente : utenti) {
            if (utente.getUsername().equals(username)
                    && utente.getPassword().equals(passwordHash)) {
                return utente;
            }
        }

        return null;
    }

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

    private void creaPrenotazione(ClienteRegistrato cliente) {
        System.out.println("----PRENOTA UNA PROIEZIONE----");

        if (proiezioni == null || proiezioni.isEmpty()) {
            System.out.println("Non ci sono proiezioni disponibili al momento.");
            return;
        }

        System.out.println("Proiezioni disponibili:");

        for (int i = 0; i < proiezioni.size(); i++) {
            Proiezione p = proiezioni.get(i);
            System.out.println(
                    (i + 1) + ") " +
                            p.getFilm().getTitolo() + " - " +
                            p.getDataOra()
            );
        }

        System.out.println("Seleziona il numero della proiezione:");

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

        Proiezione proiezioneSelezionata = proiezioni.get(indice);

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
            System.out.println(
                    "Posto " + (i + 1) +
                            " - Inserisci Fila (es. A):"
            );

            char fila = CineMax.leggiFilaValida(scanner);

            System.out.println(
                    "Posto " + (i + 1) +
                            " - Inserisci Numero posto:"
            );

            if (!scanner.hasNextInt()) {
                System.out.println("Numero non valido!");
                scanner.nextLine();
                postiValidi = false;
                break;
            }

            int numero = scanner.nextInt();
            scanner.nextLine();

            Posto postoTrovato = null;

            for (Posto posto : proiezioneSelezionata.getPosti()) {
                if (posto.getLetteraFila() == fila
                        && posto.getNumeroPosto() == numero) {
                    postoTrovato = posto;
                    break;
                }
            }

            if (postoTrovato == null) {
                System.out.println(
                        "Il posto " + fila + numero + " non esiste!"
                );
                postiValidi = false;
                break;
            }

            postiDaPrenotare.add(postoTrovato);
        }

        if (!postiValidi) {
            return;
        }

        String esito = cliente.creaPrenotazione(
                proiezioneSelezionata,
                postiDaPrenotare
        );

        System.out.println(esito);

        if (esito.contains("completata")) {
            salvaPrenotazioni();
        }
    }

    private void visualizzaPrenotazioni(ClienteRegistrato cliente) {
        System.out.println("----LE TUE PRENOTAZIONI----");
        System.out.println(cliente.visualizzaPrenotazione());
    }

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

        System.out.println(
                "Seleziona il numero della prenotazione da modificare:"
        );

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

        System.out.println(
                "Inserisci la nuova data e ora (AAAA-MM-GG HH:MM):"
        );

        String nuovaDataString = scanner.nextLine();
        LocalDateTime nuovaDataOra;

        try {
            nuovaDataOra = LocalDateTime.parse(
                    nuovaDataString.replace(" ", "T")
            );
        } catch (Exception e) {
            System.out.println("Formato data non valido.");
            return;
        }

        boolean modificata = cliente.modificaPrenotazione(
                prenotazione,
                nuovaDataOra,
                programmazione
        );

        if (modificata) {
            salvaPrenotazioni();
        }
    }

    private void eliminaPrenotazione(ClienteRegistrato cliente) {
        LinkedList<Prenotazione> prenotazioni =
                cliente.getPrenotazioniCliente();

        if (prenotazioni.isEmpty()) {
            System.out.println("Non hai prenotazioni da eliminare");
            return;
        }

        for (int i = 0; i < prenotazioni.size(); i++) {
            System.out.println((i + 1) + ") " + prenotazioni.get(i));
        }

        System.out.println(
                "Seleziona il numero della prenotazione da eliminare:"
        );

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

    private void menuProiezionista(Proiezionista proiezionista) {
        boolean logout = false;

        while (!logout) {
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
                    logout = true;
                    proiezionista.logout();
                    System.out.println("Logout effettuato");
                    break;
                default:
                    System.out.println("Opzione non valida!");
            }
        }
    }

    private void aggiungiProiezione(Proiezionista proiezionista) {
        System.out.println("\n----AGGIUNGI PROIEZIONE----");

        System.out.println("Titolo del film: ");
        String titolo = scanner.nextLine();

        System.out.println("Genere: ");
        String genere = scanner.nextLine();

        System.out.println("Regista: ");
        String regista = scanner.nextLine();

        System.out.print("Anno di uscita: ");
        if (!scanner.hasNextInt()) {
            System.out.println("Anno non valido!");
            scanner.nextLine();
            return;
        }

        int anno = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Durata del film (in minuti): ");
        if (!scanner.hasNextInt()) {
            System.out.println("Durata non valida!");
            scanner.nextLine();
            return;
        }

        int durata = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Età minima richiesta: ");
        if (!scanner.hasNextInt()) {
            System.out.println("Età minima non valida!");
            scanner.nextLine();
            return;
        }

        int etaMinima = scanner.nextInt();
        scanner.nextLine();

        System.out.print("ID proiezione (4 cifre ES. 1111) : ");
        String idProiezione = scanner.nextLine();

        System.out.print("Prezzo biglietto: ");
        if (!scanner.hasNextDouble()) {
            System.out.println("Prezzo non valido!");
            scanner.nextLine();
            return;
        }

        double prezzo = scanner.nextDouble();
        scanner.nextLine();

        System.out.print(
                "Inserisci data e ora (AAAA-MM-GG HH:MM): "
        );

        String dataOraString = scanner.nextLine();
        LocalDateTime dataOra;

        try {
            dataOra = LocalDateTime.parse(
                    dataOraString.replace(" ", "T")
            );
        } catch (Exception e) {
            System.out.println("Formato data non valido!");
            return;
        }

        Film film = new Film(
                titolo,
                genere,
                regista,
                anno,
                durata,
                etaMinima
        );

        Proiezione nuovaProiezione = new Proiezione(
                idProiezione,
                film,
                dataOra,
                prezzo
        );

        boolean aggiunta = proiezionista.aggiungiProiezioni(
                nuovaProiezione,
                proiezioni
        );

        if (aggiunta) {
            System.out.println("Proiezione aggiunta con successo!");
        }
    }

    private void modificaProiezione(Proiezionista proiezionista) {
        System.out.println("\n----MODIFICA PROIEZIONE----");

        if (proiezioni.isEmpty()) {
            System.out.println(
                    "Nessuna proiezione disponibile da modificare."
            );
            return;
        }

        stampaProiezioni();

        System.out.print(
                "Seleziona il numero della proiezione da modificare: "
        );

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

    private void modificaDataProiezione(
            Proiezionista proiezionista,
            Proiezione proiezione) {

        System.out.print(
                "Inserisci la nuova data e ora (AAAA-MM-GG HH:MM): "
        );

        String nuovaDataString = scanner.nextLine();
        LocalDateTime nuovaData;

        try {
            nuovaData = LocalDateTime.parse(
                    nuovaDataString.replace(" ", "T")
            );
        } catch (Exception e) {
            System.out.println("Formato data non valido!");
            return;
        }

        boolean cambiata = proiezionista.cambiaData(
                proiezione,
                proiezioni,
                nuovaData
        );

        if (cambiata) {
            System.out.println(
                    "Data e ora aggiornate con successo!"
            );
        }
    }

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

        System.out.println(
                "Prezzo aggiornato e salvato con successo!"
        );
    }

    private void eliminaProiezione(Proiezionista proiezionista) {
        System.out.println("\n----ELIMINA PROIEZIONE----");

        if (proiezioni.isEmpty()) {
            System.out.println("Nessuna proiezione presente");
            return;
        }

        for (int i = 0; i < proiezioni.size(); i++) {
            Proiezione p = proiezioni.get(i);
            System.out.println(
                    (i + 1) + ") " +
                            p.getFilm().getTitolo() + " - " +
                            p.getDataOra()
            );
        }

        System.out.println(
                "Seleziona la proiezione da eliminare: "
        );

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

        proiezionista.eliminaProiezione(
                proiezione,
                proiezioni
        );

        System.out.println(
                "Proiezione eliminata con successo"
        );
    }

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

    private void menuBigliettaio(Bigliettaio bigliettaio) {
        boolean logout = false;

        while (!logout) {
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
                    logout = true;
                    bigliettaio.setLoggato(false);
                    System.out.println("Logout effettuato");
                    break;
                default:
                    System.out.println("Opzione non valida!");
            }
        }
    }

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
                LinkedList<Prenotazione> tuttePrenotazioni =
                        new LinkedList<>();

                for (Utente utente : utenti) {
                    if (utente instanceof ClienteRegistrato) {
                        tuttePrenotazioni.addAll(
                                ((ClienteRegistrato) utente)
                                        .getPrenotazioniCliente()
                        );
                    }
                }

                bigliettaio.visualizzaPrenotazioniOggi(
                        tuttePrenotazioni
                );
                break;

            case 2:
                bigliettaio.cercaPerId(utenti, scanner);
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

            default:
                System.out.println("Opzione non valida!");
        }
    }

    private void menuOspite() {
        boolean back = false;

        while (!back) {
            System.out.println("\n----MENU' OSPITE----");
            System.out.println("1. Cerca una proiezione");
            System.out.println(
                    "2. Visualizza dettagli di una proiezione"
            );
            System.out.println("3. Torna al menù principale");
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
                    System.out.println(
                            "----CERCA UNA PROIEZIONE----"
                    );
                    programmazione.cercaProiezione(scanner);
                    break;

                case 2:
                    System.out.println(
                            "----VISUALIZZA DETTAGLI DI UNA PROIEZIONE----"
                    );
                    programmazione.cercaProiezione(scanner);
                    break;

                case 3:
                    back = true;
                    break;

                default:
                    System.out.println(
                            "Opzione non valida!. " +
                                    "Inserisci un numero da 1 a 3 "
                    );
            }
        }
    }

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

        String hashPassword =
                PasswordUtil.hashPassword(password);

        LocalDate dataDiNascita;

        try {
            System.out.println(
                    "Data di nascita (AAAA-MM-GG): "
            );
            dataDiNascita =
                    LocalDate.parse(scanner.nextLine());
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

        Domicilio domicilio = new Domicilio(
                via,
                numeroCivico,
                cap,
                citta,
                provincia
        );

        ClienteRegistrato nuovoCliente =
                new ClienteRegistrato(
                        nome,
                        cognome,
                        username,
                        hashPassword,
                        dataDiNascita,
                        domicilio,
                        Ruolo.CLIENTE
                );

        utenti.add(nuovoCliente);
        gestoreUtenti.salvaUtenti();

        System.out.println("Registrazione completata");
    }

    private void salvaPrenotazioni() {
        try {
            gestorePrenotazioni.salvaPrenotazioni(utenti);
        } catch (Exception e) {
            System.out.println(
                    "Errore durante il salvataggio della prenotazione."
            );
        }
    }

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

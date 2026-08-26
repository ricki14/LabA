package cinemax;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class CineMax {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);

        System.out.println("----BENVENUTO DA CINEMAX----");

        //caricamento utenti

        LinkedList<Utente> utenti = new LinkedList<Utente>();
        GestoreUtenti gestoreUtenti = new GestoreUtenti(utenti);
        gestoreUtenti.caricaUtenti();

        //caricamento proiezioni
        List<Proiezione> proiezioni = GestoreProiezioni.leggiProiezioni();

        //caricamento prenotazioni
        GestorePrenotazioni gestorePrenotazioni = new GestorePrenotazioni();
        gestorePrenotazioni.caricaPrenotazioni(utenti, proiezioni);


        //menù principale
        boolean esci=false;
        ProgrammazioneCinema programmazione = new ProgrammazioneCinema();
        for (Proiezione p : proiezioni) {
            programmazione.aggiungiProiezione(p);
        }

        while(!esci){

            System.out.println("1. Login");
            System.out.println("2. Registrazione");
            System.out.println("3. Continua come ospite");
            System.out.println("4. Esci");
            System.out.println("Scegli un'opzione: ");

            if(scanner.hasNextInt()) {
                int numeroScelto = scanner.nextInt();
                scanner.nextLine();

                switch (numeroScelto) {
                    case 1: {
                        System.out.println("\n----LOGIN----");
                        System.out.println("Inserisci username: ");
                        String username = scanner.nextLine();
                        System.out.println("Inserisci password: ");
                        String password = scanner.nextLine();

                        String passwordHash = PasswordUtil.hashPassword(password);
                        boolean loginEffettuato = false;
                        Utente utenteLoggato = null;

                        for (Utente utente : utenti) {
                            if (utente.getUsername().equals(username) && utente.getPassword().equals(passwordHash)) {
                                loginEffettuato = true;
                                utenteLoggato = utente;
                                utenteLoggato.setLoggato(true);
                                System.out.println("Login effettuato");
                                break;
                            }
                        }
                        if (!loginEffettuato) {
                            System.out.println("Username o password errati");
                        } else {

                            boolean logout = false;
                            while(!logout){
                                if (utenteLoggato instanceof ClienteRegistrato) {

                                    ClienteRegistrato cliente = (ClienteRegistrato) utenteLoggato;

                                    System.out.println("\n----MENU' CLIENTE----");
                                    System.out.println("1. Cerca una Proiezione");
                                    System.out.println("2. Crea una prenotazione");
                                    System.out.println("3. Visualizza le tue prenotazioni");
                                    System.out.println("4. Modifica una prenotazione");
                                    System.out.println("5. Elimina una prenotazione");
                                    System.out.println("6. Logout");
                                    System.out.println("Scegli un'opzione: ");

                                    if (scanner.hasNextInt()){
                                        int sceltaCliente = scanner.nextInt();
                                        scanner.nextLine();

                                        switch (sceltaCliente){
                                            case 1 :
                                            //cerca proiezione
                                                programmazione.cercaProiezione(scanner);
                                                break;
                                            case 2 :
                                                //crea prenotazione
                                                System.out.println("----PRENOTA UNA PROIEZIONE----");

                                                if (proiezioni == null || proiezioni.isEmpty()) {
                                                    System.out.println("Non ci sono proiezioni disponibili al momento.");
                                                    break;
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
                                                    break;
                                                }
                                                int idxProiezione = scanner.nextInt() - 1;
                                                scanner.nextLine();

                                                if (idxProiezione < 0 || idxProiezione >= proiezioni.size()) {
                                                    System.out.println("Proiezione non valida!");
                                                    break;
                                                }

                                                Proiezione proiezioneSelezionata = proiezioni.get(idxProiezione);

                                                System.out.println("Quanti posti vuoi prenotare?");
                                                if (!scanner.hasNextInt()) {
                                                    System.out.println("Numero non valido!");
                                                    scanner.nextLine();
                                                    break;
                                                }
                                                int numPosti = scanner.nextInt();
                                                scanner.nextLine();

                                                LinkedList<Posto> postiDaPrenotare = new LinkedList<>();
                                                boolean postiValidi = true;

                                                for (int i = 0; i < numPosti; i++) {
                                                    System.out.println("Posto " + (i + 1) + " - Inserisci Fila (es. A):");
                                                    char fila = leggiFilaValida(scanner);

                                                    System.out.println("Posto " + (i + 1) + " - Inserisci Numero posto:");
                                                    if (!scanner.hasNextInt()) {
                                                        System.out.println("Numero non valido!");
                                                        scanner.nextLine();
                                                        postiValidi = false;
                                                        break;
                                                    }
                                                    int num = scanner.nextInt();
                                                    scanner.nextLine();

                                                    Posto pTrovato = null;
                                                    for (Posto p : proiezioneSelezionata.getPosti()) {
                                                        if (p.getLetteraFila() == fila && p.getNumeroPosto() == num) {
                                                            pTrovato = p;
                                                            break;
                                                        }
                                                    }

                                                    if (pTrovato == null) {
                                                        System.out.println("Il posto " + fila + num + " non esiste!");
                                                        postiValidi = false;
                                                        break;
                                                    }
                                                    postiDaPrenotare.add(pTrovato);
                                                }

                                                if (postiValidi) {
                                                    String esito = cliente.creaPrenotazione(proiezioneSelezionata, postiDaPrenotare);
                                                    System.out.println(esito);

                                                    if (esito.contains("completata")) {
                                                        try {
                                                            gestorePrenotazioni.salvaPrenotazioni(utenti);
                                                        } catch (Exception e) {
                                                            System.out.println("Errore nel salvataggio della prenotazione.");
                                                        }
                                                    }
                                                }

                                                break;
                                            case 3 :
                                            //visualizza prenotazioni
                                                System.out.println("----LE TUE PRENOTAZIONI----");
                                                System.out.println(cliente.visualizzaPrenotazione());
                                                break;
                                            case 4 :
                                                //modifica prenotazione
                                                LinkedList<Prenotazione> prenotazioniMod = cliente.getPrenotazioniCliente();

                                                System.out.println("----MODIFICA PRENOTAZIONE----");

                                                if(cliente.getPrenotazioniCliente().isEmpty()){
                                                    System.out.println("Non hai prenotazioni da modificare");
                                                    break;
                                                }
                                                for (int i = 0; i < prenotazioniMod.size(); i++) {

                                                    System.out.println(
                                                            (i + 1) + ") " + prenotazioniMod.get(i));
                                                }

                                                System.out.println(
                                                        "Seleziona il numero della prenotazione da modificare:"
                                                );

                                                if (!scanner.hasNextInt()) {
                                                    System.out.println("Inserisci un numero valido.");
                                                    scanner.nextLine();
                                                    break;
                                                }

                                                int sceltaPrenotazione = scanner.nextInt();
                                                scanner.nextLine();

                                                if(sceltaPrenotazione < 1 || sceltaPrenotazione > prenotazioniMod.size()){
                                                    System.out.println("Numero prenotazione non valido");
                                                    break;
                                                }

                                                Prenotazione prenotazione = prenotazioniMod.get(sceltaPrenotazione - 1);

                                                System.out.println("Inserisci la nuova data e ora " + "(AAAA-MM-GG HH:MM):");

                                                String nuovaDataString = scanner.nextLine();
                                                LocalDateTime nuovaDataOra;

                                                try {
                                                    nuovaDataOra = LocalDateTime.parse(nuovaDataString.replace(" ", "T"));
                                                } catch (Exception e) {
                                                    System.out.println("Formato data non valido.");
                                                    break;
                                                }

                                                boolean modificata = cliente.modificaPrenotazione(prenotazione,
                                                        nuovaDataOra, programmazione);

                                                if (modificata) {
                                                    try {
                                                        gestorePrenotazioni.salvaPrenotazioni(utenti);
                                                    } catch (Exception e) {
                                                        System.out.println("Errore durante il salvataggio.");
                                                    }
                                                }
                                                break;
                                            case 5 :
                                                //elimina prenotazione
                                                LinkedList<Prenotazione> prenotazioniDel = cliente.getPrenotazioniCliente();

                                                if(cliente.getPrenotazioniCliente().isEmpty()){
                                                    System.out.println("Non hai prenotazioni da eliminare");
                                                    break;
                                                }

                                                for (int i = 0; i < prenotazioniDel.size(); i++) {

                                                    System.out.println(
                                                            (i + 1) + ") " + prenotazioniDel.get(i));
                                                }

                                                System.out.println(
                                                        "Seleziona il numero della prenotazione da eliminare:"
                                                );

                                                if (!scanner.hasNextInt()) {
                                                    System.out.println("Inserisci un numero valido.");
                                                    scanner.nextLine();
                                                    break;
                                                }

                                                int sceltaPrenotazioneDel = scanner.nextInt();
                                                scanner.nextLine();

                                                if(sceltaPrenotazioneDel < 1 || sceltaPrenotazioneDel > prenotazioniDel.size()){
                                                    System.out.println("Numero prenotazione non valido");
                                                    break;
                                                }

                                                Prenotazione prenotazioneDaEliminare = prenotazioniDel.get(sceltaPrenotazioneDel - 1);
                                                boolean eliminata = cliente.eliminaPrenotazione(prenotazioneDaEliminare);

                                                try {
                                                    gestorePrenotazioni.salvaPrenotazioni(utenti);
                                                } catch (Exception e) {
                                                    System.out.println("Errore durante il salvataggio della prenotazione.");
                                                }

                                                break;
                                            case 6 :
                                             //logout
                                                logout=true;
                                                utenteLoggato.setLoggato(false);
                                                System.out.println("Logout effettuato");
                                                break;
                                            default:
                                            System.out.println("Opzione non valida!");
                                                break;
                                        }

                                    } else {
                                        System.out.println("Inserisci un numero valido!");
                                        scanner.nextLine();
                                    }
                                }
                                else if (utenteLoggato instanceof Proiezionista){
                                    //sotto-menù proiezionista
                                    logout=gestioneMenuProiezionista((Proiezionista) utenteLoggato, proiezioni,scanner);

                                } else {
                                    //sotto-menù bigliettaio

                                    System.out.println("\n----MENU' BIGLIETTAIO----");
                                    System.out.println("1. Cerca proiezione");
                                    System.out.println("2. Visualizza prenotazioni");
                                    System.out.println("3. Logout");
                                    System.out.println("Scegli un'opzione:");

                                    if(scanner.hasNextInt()){
                                        int sceltaBigliettaio = scanner.nextInt();
                                        scanner.nextLine();

                                        switch(sceltaBigliettaio){
                                            case 1 :
                                                //cerca proiezione
                                                programmazione.cercaProiezione(scanner);
                                                break;
                                            case 2 :
                                                //visualizza prenotazioni
                                                Bigliettaio bigliettaio = (Bigliettaio) utenteLoggato;
                                                ricercaPrenotazioniBigliettaio(bigliettaio, utenti, scanner);
                                                break;
                                            case 3 :
                                                //logout
                                                logout = true;
                                                utenteLoggato.setLoggato(false);
                                                System.out.println("Logout effettuato");
                                                break;
                                            default:
                                                System.out.println("Opzione non valida!");
                                                break;
                                        }
                                    } else {
                                        System.out.println("Inserisci un numero valido!");
                                        scanner.nextLine();
                                    }

                                }
                            }
                        }


                    }
                    break;
                    case 2: {
                        System.out.println("---- REGISTRAZIONE ----");
                        System.out.println("Nome: ");
                        String nome = scanner.nextLine();
                        System.out.println("Cognome: ");
                        String cognome= scanner.nextLine();
                        System.out.println("Username: ");
                        String username=scanner.nextLine();

                        boolean usernameEsistente = false;
                        for (Utente utente: utenti){
                            if (utente.getUsername().equals(username)){
                                usernameEsistente=true;
                                break;
                            }
                        }
                        if (usernameEsistente){
                            System.out.println("Username già presente");
                            break;
                        }
                        System.out.println("Password: ");
                        String password=scanner.nextLine();

                        String hashPassword=PasswordUtil.hashPassword(password);

                        System.out.println("Data di nascita (AAAA-MM-GG): ");
                        LocalDate dataDiNascita = LocalDate.parse(scanner.nextLine());
                        System.out.println("Via (Facoltativo): ");
                        String via = scanner.nextLine();
                        System.out.println("Numero civico (Facoltativo): ");
                        String numeroCivico = scanner.nextLine();
                        System.out.println("CAP (Facoltativo): ");
                        String cap = scanner.nextLine();
                        System.out.println("Città (Facoltativo): ");
                        String citta=scanner.nextLine();
                        System.out.println("Provincia (Facoltativo): ");
                        String provincia = scanner.nextLine();

                        Domicilio domicilio = new Domicilio(via,numeroCivico,cap,citta,provincia);

                        ClienteRegistrato nuovoCliente= new ClienteRegistrato(nome,cognome,username,hashPassword,dataDiNascita,domicilio,Ruolo.CLIENTE);

                        utenti.add(nuovoCliente);
                        gestoreUtenti.salvaUtenti();
                        System.out.println("Registrazione completata");
                        break;
                    }
                    case 3: {
                        //Continua come ospite
                        boolean back = false;
                        while(!back) {
                            System.out.println("\n----MENU' OSPITE----");
                            System.out.println("1. Cerca una proiezione");
                            System.out.println("2. Visualizza dettagli di una proiezione");
                            System.out.println("3. Torna al menù principale");
                            System.out.println("Scegli un'opzione: ");

                            if(scanner.hasNextInt()){
                                int sceltaGuest = scanner.nextInt();
                                scanner.nextLine();

                                switch(sceltaGuest){
                                    case 1 :
                                        //cerca proiezione
                                        System.out.println("----CERCA UNA PROIEZIONE----");
                                        programmazione.cercaProiezione(scanner);
                                        break;
                                    case 2 :
                                        //visualizza dettagli proiezione
                                        System.out.println("----VISUALIZZA DETTAGLI DI UNA PROIEZIONE----");
                                        programmazione.cercaProiezione(scanner);
                                        break;
                                    case 3 :
                                        //torna al menù principale
                                        back = true;
                                        break;
                                    default:
                                        System.out.println("Opzione non valida!. " +
                                                "Inserisci un numero da 1 a 3 ");
                                        break;
                                }

                            }else {
                                System.out.println("Inserisci un valore valido!");
                                scanner.nextLine();
                            }
                        }
                    }break;
                    case 4: {
                        esci = true;
                        break;
                    }
                    default:
                        System.out.println("Scelta non valida");
                        break;

                }
            } else {
                System.out.println("Carattere inserito non valido!");
                scanner.nextLine();
            }
        }

        scanner.close();
    }

    private static void ricercaPrenotazioniBigliettaio(Bigliettaio bigliettaio,
                                                       List<Utente> utenti, Scanner scanner){
        System.out.println("\n----VISUALIZZA / CERCA PRENOTAZIONI----");
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

        int sceltaRicerca = scanner.nextInt();
        scanner.nextLine();

        switch (sceltaRicerca){
            case 1 :
                LinkedList<Prenotazione> listTuttePren = new LinkedList<>();
                for (Utente tmp : utenti){
                    if (tmp instanceof ClienteRegistrato)
                        listTuttePren.addAll(((ClienteRegistrato) tmp).getPrenotazioniCliente());
                }
                bigliettaio.visualizzaPrenotazioniOggi(listTuttePren);
                break;
            case 2 :
                bigliettaio.cercaPerId(utenti, scanner);
                break;
            case 3 :
                bigliettaio.cercaPerNomeECognome(utenti, scanner);
                break;
            case 4 :
                bigliettaio.cercaPerIdNomeECognome(utenti, scanner);
                break;
            default:
                System.out.println("Opzione non valida!");
                break;
        }
    }

    private static boolean gestioneMenuProiezionista(Proiezionista proiezionista,
                                                  List<Proiezione> proiezioni, Scanner scanner) {
        System.out.println("\n----MENU' PROIEZIONISTA----");
        System.out.println("1. Aggiungi proiezione");
        System.out.println("2. Modifica proiezione");
        System.out.println("3. Elimina proiezione");
        System.out.println("4. Logout");
        System.out.println("Scegli un'opzione:");

        boolean logoutP=false;

        if (scanner.hasNextInt()) {
            int sceltaProiezionista = scanner.nextInt();
            scanner.nextLine();

            switch (sceltaProiezionista) {
                case 1:
                    //aggiunta proiezione
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
                        break;
                    }
                    int anno = scanner.nextInt();
                    scanner.nextLine();

                    System.out.println("Durata del film (in minuti): ");
                    if (!scanner.hasNextInt()) {
                        System.out.println("Durata non valida!");
                        scanner.nextLine();
                        break;
                    }
                    int durata = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Età minima richiesta: ");
                    if (!scanner.hasNextInt()) {
                        System.out.println("Età minima non valida!");
                        scanner.nextLine();
                        break;
                    }
                    int etaMinima = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("ID proiezione (4 cifre ES. 1111) : ");
                    String idProiezione = scanner.nextLine();

                    System.out.print("Prezzo biglietto: ");
                    if (!scanner.hasNextDouble()) {
                        System.out.println("Prezzo non valido!");
                        scanner.nextLine();
                        break;
                    }
                    double prezzo = scanner.nextDouble();
                    scanner.nextLine();

                    System.out.print("Inserisci data e ora (AAAA-MM-GG HH:MM): ");
                    String dataOraString = scanner.nextLine();
                    LocalDateTime dataOra;
                    try {
                        dataOra = LocalDateTime.parse(dataOraString.replace(" ", "T"));
                    } catch (Exception e) {
                        System.out.println("Formato data non valido!");
                        break;
                    }

                    Film filmDaAggiungere = new Film(titolo, genere, regista, anno, durata, etaMinima);
                    Proiezione nuovaProiezione = new Proiezione(idProiezione, filmDaAggiungere, dataOra, prezzo);

                    boolean aggiunta = proiezionista.aggiungiProiezioni(nuovaProiezione, proiezioni);
                    if (aggiunta) {
                        System.out.println("Proiezione aggiunta con successo!");
                    }
                    break;
                case 2:
                    //modifica proiezione
                    System.out.println("\n----MODIFICA PROIEZIONE----");

                    if (proiezioni.isEmpty()) {
                        System.out.println("Nessuna proiezione disponibile da modificare.");
                        break;
                    }

                    for (int i = 0; i < proiezioni.size(); i++) {
                        Proiezione p = proiezioni.get(i);
                        System.out.println((i + 1) + ") ID: " + p.getId() + " - " + p.getFilm().getTitolo()
                                + " | Data: " + p.getDataOra() + " | Prezzo: " + p.getPrezzoBiglietto() + "€"+ "| Durata: " + p.getFilm().getDurata());
                    }

                    System.out.print("Seleziona il numero della proiezione da modificare: ");
                    if (!scanner.hasNextInt()) {
                        System.out.println("Numero non valido!");
                        scanner.nextLine();
                        break;
                    }
                    int indiceProiezDaMod = scanner.nextInt() - 1;
                    scanner.nextLine();

                    if (indiceProiezDaMod < 0 || indiceProiezDaMod >= proiezioni.size()) {
                        System.out.println("Proiezione non valida!");
                        break;
                    }

                    Proiezione proiezioneDaModificare = proiezioni.get(indiceProiezDaMod);

                    System.out.println("\nChe cosa vuoi modificare?");
                    System.out.println("1. Data e Ora");
                    System.out.println("2. Prezzo del biglietto");
                    System.out.print("Scegli un'opzione: ");

                    if (!scanner.hasNextInt()) {
                        System.out.println("Opzione non valida!");
                        scanner.nextLine();
                        break;
                    }
                    int sceltaModifica = scanner.nextInt();
                    scanner.nextLine();

                    switch (sceltaModifica){
                        case 1 :
                            System.out.print("Inserisci la nuova data e ora (AAAA-MM-GG HH:MM): ");
                            String nuovaDataString = scanner.nextLine();
                            LocalDateTime nuovaData;
                            try {
                                nuovaData = LocalDateTime.parse(nuovaDataString.replace(" ", "T"));
                            } catch (Exception e) {
                                System.out.println("Formato data non valido!");
                                break;
                            }

                            boolean dataCambiata = proiezionista.cambiaData(proiezioneDaModificare,
                                                                            proiezioni, nuovaData);
                            if (dataCambiata) {
                                System.out.println("Data e ora aggiornate con successo!");
                            }
                            break;
                        case 2 :
                            System.out.print("Inserisci il nuovo prezzo: ");
                            if (!scanner.hasNextDouble()) {
                                System.out.println("Prezzo non valido!");
                                scanner.nextLine();
                                break;
                            }
                            double nuovoPrezzo = scanner.nextDouble();
                            scanner.nextLine();

                            proiezioneDaModificare.setPrezzoBiglietto(nuovoPrezzo);
                            GestoreProiezioni.scriviProiezioni(proiezioni);
                            System.out.println("Prezzo aggiornato e salvato con successo!");
                            break;
                        default:
                            System.out.println("Scelta non valida!");
                            break;
                    }

                    break;
                case 3:
                    //elimina proiezione
                    System.out.println("\n----ELIMINA PROIEZIONE----");

                    if(proiezioni.isEmpty()){
                        System.out.println("Nessuna proiezione presente");
                        break;
                    }

                    for(int i=0; i<proiezioni.size(); i++){
                        Proiezione p = proiezioni.get(i);
                        System.out.println((i + 1) + ") " + p.getFilm().getTitolo() + " - " + p.getDataOra());
                    }
                    System.out.println("Seleziona la proiezione da eliminare: ");

                    if (!scanner.hasNextInt()) {
                        System.out.println("Opzione non valida!");
                        scanner.nextLine();
                        break;
                    }

                    int indiceProiezDaElim = scanner.nextInt()-1;
                    scanner.nextLine();

                    if (indiceProiezDaElim < 0 || indiceProiezDaElim >= proiezioni.size()) {
                        System.out.println("Proiezione non valida!");
                        break;
                    }

                    Proiezione proiezioneDaEliminare = proiezioni.get(indiceProiezDaElim);
                    proiezionista.eliminaProiezione(proiezioneDaEliminare, proiezioni);
                    System.out.println("Proiezione eliminata con successo");
                    break;
                case 4:
                    //logout
                    logoutP = true;
                    proiezionista.logout();
                    System.out.println("Logout effettuato");
                    break;
                default:
                    System.out.println("Opzione non valida!");
                    break;
            }
        }
        return logoutP;
    }
    public static char leggiFilaValida(Scanner scanner) {
        while (true) {
            System.out.print("Inserisci la lettera della fila (es. A): ");
            String input = scanner.nextLine().trim();

            if (input.length() == 1 && Character.isLetter(input.charAt(0))) {
                return Character.toUpperCase(input.charAt(0));
            }

            System.out.println("Errore: inserisci soltanto una singola lettera per la fila!");
        }
    }
}
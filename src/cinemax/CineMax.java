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
                        System.out.println("----LOGIN----");
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

                                    System.out.println("----MENU' CLIENTE----");
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
                                                    String filaStr = scanner.nextLine().trim().toUpperCase();
                                                    if (filaStr.isEmpty()) {
                                                        postiValidi = false;
                                                        break;
                                                    }
                                                    char fila = filaStr.charAt(0);

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
                                                        "Seleziona il numero della prenotazione da eliminare:"
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
                            }
                        }


                    }
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
                        System.out.println("Via: ");
                        String via = scanner.nextLine();
                        System.out.println("Numero civico: ");
                        String numeroCivico = scanner.nextLine();
                        System.out.println("CAP: ");
                        String cap = scanner.nextLine();
                        System.out.println("Città: ");
                        String citta=scanner.nextLine();
                        System.out.println("Provincia: ");
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
                            System.out.println("----MENU' OSPITE----");
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
}
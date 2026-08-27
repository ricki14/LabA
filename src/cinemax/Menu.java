package cinemax;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Menu {
    private static void gestisciLogin(List<Utente> utenti,Scanner scanner) {
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
            }
    }

    private static boolean menuCliente(ClienteRegistrato cliente, List<Proiezione> proiezioni, ProgrammazioneCinema programmazione, GestorePrenotazioni gestorePrenotazioni, List<Utente> utenti, Scanner scanner) {
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


            if (scanner.hasNextInt()) {
                int sceltaCliente = scanner.nextInt();
                scanner.nextLine();


                switch (sceltaCliente) {
                    case 1:
                        //cerca proiezione
                        programmazione.cercaProiezione(scanner);
                        break;
                    case 2:
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
                    case 3:
                        //visualizza prenotazioni
                        System.out.println("----LE TUE PRENOTAZIONI----");
                        System.out.println(cliente.visualizzaPrenotazione());
                        break;
                    case 4:
                        //modifica prenotazione
                        LinkedList<Prenotazione> prenotazioniMod = cliente.getPrenotazioniCliente();

                        System.out.println("----MODIFICA PRENOTAZIONE----");

                        if (cliente.getPrenotazioniCliente().isEmpty()) {
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

                        if (sceltaPrenotazione < 1 || sceltaPrenotazione > prenotazioniMod.size()) {
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
                    case 5:
                        //elimina prenotazione
                        LinkedList<Prenotazione> prenotazioniDel = cliente.getPrenotazioniCliente();

                        if (cliente.getPrenotazioniCliente().isEmpty()) {
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

                        if (sceltaPrenotazioneDel < 1 || sceltaPrenotazioneDel > prenotazioniDel.size()) {
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
                    case 6:
                        //logout
                        logout = true;
                        cliente.setLoggato(false);
                        System.out.println("Logout effettuato");
                        break;
                    default:
                        System.out.println("Opzione non valida!");
                        break;
                }
            }
        }
        return logout;
    }

    private static Sistema inizializzaDati() {
    ...
    }

    private static void menuPrincipale(...) {
    ...
    }

    private static void gestisciLogin(...) {
    ...
    }

    private static boolean menuCliente(...) {
    ...
    }

    private static boolean gestioneMenuProiezionista(...) {
    ...
    }

    private static boolean menuBigliettaio(...) {
    ...
    }

    private static boolean menuOspite(...) {
    ...
    }

    private static void registraCliente(...) {
    ...
    }
}

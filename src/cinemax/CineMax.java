package cinemax;

import javax.sound.midi.Soundbank;
import java.net.ServerSocket;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
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

        //menù principale
        boolean esci=false;
        GestorePrenotazioni programmazione = new GestorePrenotazioni();
        programmazione.caricaPrenotazioni("data/prenotazioni.csv");

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

                        for (Utente utente : utenti) {
                            if (utente.getUsername().equals(username) && utente.getPassword().equals(passwordHash)) {
                                loginEffettuato = true;
                                System.out.println("Login effettuato");
                                break;
                            }
                        }
                        if (!loginEffettuato) {
                            System.out.println("Username o password errati");
                        }
                        break;
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
                                        programmazione.cercaProiezione(scanner);
                                        break;
                                    case 2 :
                                        //visualizza dettagli proiezione

                                        break;
                                    case 3 :
                                        //torna al menù principale
                                        back = true;
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
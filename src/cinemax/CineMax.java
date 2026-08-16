package cinemax;

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

        //menù principale
        boolean esci=false;
        ProgrammazioneCinema programmazione = new ProgrammazioneCinema();
        programmazione.caricaDaCSV("proiezioni.csv");

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
                    case 1:
                        //Login
                        break;
                    case 2:
                        //Registrazione
                        break;
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

                            }
                        } else {
                            System.out.println("Inserisci un valore valido!");
                            scanner.nextLine();
                        }
                        }break;
                    case 4:
                        esci = true;
                        break;
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

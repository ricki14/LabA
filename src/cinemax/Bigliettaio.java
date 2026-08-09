package cinemax;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class Bigliettaio extends Utente {

    // Costruttore con data di nascita
    public Bigliettaio(String nome, String cognome, String username,
                       String password, LocalDate dataDiNascita,
                       Domicilio domicilio) {

        super(nome, cognome, username, password,
                dataDiNascita, domicilio, Ruolo.BIGLIETTAIO);
    }

    // Costruttore senza data di nascita
    public Bigliettaio(String nome, String cognome, String username,
                       String password, Domicilio domicilio) {

        super(nome, cognome, username, password,
                domicilio, Ruolo.BIGLIETTAIO);
    }

    /**
     * Visualizza tutte le prenotazioni effettuate nella data odierna.
     */
    public void visualizzaPrenotazioniOggi(ArrayList<Prenotazione> prenotazioni) {

        LocalDate oggi = LocalDate.now();
        boolean trovata = false;

        System.out.println("\n===== PRENOTAZIONI DI OGGI =====");

        for (Prenotazione p : prenotazioni) {

            if (p.getDataAcquisto().equals(oggi)) {
                System.out.println(p);
                trovata = true;
            }
        }

        if (!trovata) {
            System.out.println("Nessuna prenotazione effettuata oggi.");
        }
    }

    /**
     * Cerca una prenotazione tramite ID.
     */
    public void cercaPrenotazione(ArrayList<Prenotazione> prenotazioni,
                                  Scanner scanner) {

        System.out.print("Inserisci l'ID della prenotazione: ");
        String id = scanner.nextLine();

        for (Prenotazione p : prenotazioni) {

            if (p.getIdPrenotazione().equalsIgnoreCase(id)) {

                System.out.println("\n===== PRENOTAZIONE TROVATA =====");
                System.out.println(p);
                return;
            }
        }

        System.out.println("Prenotazione non trovata.");
    }

    /**
     * Logout del bigliettaio.
     */
    public void logout() {

        System.out.println("Logout effettuato.");
    }
}
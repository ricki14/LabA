package cinemax;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Rappresenta un utente con il ruolo di bigliettaio del sistema Cinemax.
 *
 * <p>Il bigliettaio può visualizzare le prenotazioni effettuate
 * nella giornata corrente, cercare una prenotazione tramite il
 * relativo identificativo ed effettuare il logout.</p>
 *
 * @version 1.0
 */
public class Bigliettaio extends Utente {

    /**
     * Costruisce un nuovo bigliettaio specificando anche la data di nascita.
     *
     * @param nome il nome del bigliettaio
     * @param cognome il cognome del bigliettaio
     * @param username il nome utente utilizzato per l'accesso
     * @param password la password utilizzata per l'accesso
     * @param dataDiNascita la data di nascita del bigliettaio
     * @param domicilio il domicilio del bigliettaio
     */
    public Bigliettaio(String nome, String cognome, String username,
                       String password, LocalDate dataDiNascita,
                       Domicilio domicilio) {

        super(nome, cognome, username, password,
                dataDiNascita, domicilio, Ruolo.BIGLIETTAIO, false);
    }

    /**
     * Costruisce un nuovo bigliettaio senza specificare la data di nascita.
     *
     * @param nome il nome del bigliettaio
     * @param cognome il cognome del bigliettaio
     * @param username il nome utente utilizzato per l'accesso
     * @param password la password utilizzata per l'accesso
     * @param domicilio il domicilio del bigliettaio
     */
    public Bigliettaio(String nome, String cognome, String username,
                       String password, Domicilio domicilio) {

        super(nome, cognome, username, password,
                domicilio, Ruolo.BIGLIETTAIO);
    }

    /**
     * Visualizza tutte le prenotazioni effettuate nella data odierna.
     *
     * <p>Il metodo confronta la data di acquisto di ogni prenotazione
     * con la data corrente. Se vengono trovate prenotazioni, queste
     * vengono visualizzate a video. In caso contrario viene mostrato
     * un messaggio che indica l'assenza di prenotazioni.</p>
     *
     * @param prenotazioni lista delle prenotazioni da controllare
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
     * Cerca una prenotazione tramite il suo identificativo.
     *
     * <p>Il metodo richiede all'utente di inserire l'ID della prenotazione
     * e confronta tale valore con gli identificativi presenti nella lista.
     * La ricerca non distingue tra lettere maiuscole e minuscole.</p>
     *
     * @param prenotazioni lista delle prenotazioni in cui effettuare la ricerca
     * @param scanner oggetto utilizzato per leggere l'ID inserito dall'utente
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
     * Effettua il logout del bigliettaio.
     *
     * <p>Imposta lo stato di accesso del bigliettaio a {@code false}
     * e visualizza un messaggio di conferma.</p>
     */
    public void logout() {

        setLoggato(false);
        System.out.println("Logout effettuato.");
    }
}
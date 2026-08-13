package cinemax;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

/**
 * Rappresenta un utente con il ruolo di proiezionista del sistema Cinemax.
 *
 * <p>Il proiezionista può aggiungere ed eliminare proiezioni dalla
 * programmazione, modificare la data e l'ora di una proiezione
 * ed effettuare il logout.</p>
 * @author Daniele Rossetti
 * @version 1.0
 */
public class Proiezionista extends Utente {

    /**
     * Costruisce un nuovo proiezionista.
     *
     * @param nome il nome del proiezionista
     * @param cognome il cognome del proiezionista
     * @param username il nome utente del proiezionista
     * @param password la password del proiezionista
     * @param dataDiNascita la data di nascita del proiezionista
     * @param domicilio il domicilio del proiezionista
     * @param ruolo il ruolo dell'utente
     * @param loggato indica se l'utente è attualmente autenticato
     */
    public Proiezionista(String nome, String cognome, String username,
                         String password, LocalDate dataDiNascita,
                         Domicilio domicilio, Ruolo ruolo, boolean loggato) {
        super(nome, cognome, username, password,
                dataDiNascita, domicilio, ruolo, loggato);
    }

    /**
     * Aggiunge una proiezione alla lista delle proiezioni.
     *
     * @param proiezione la proiezione da aggiungere
     * @param proiezioni lista di proiezioni a cui aggiungere la nuova proiezione
     */
    public void aggiungiProiezioni(Proiezione proiezione,
                                   List<Proiezione> proiezioni) {
        proiezioni.add(proiezione);
    }

    /**
     * Modifica la data e l'ora di una proiezione.
     *
     * <p>Il metodo richiede all'utente di inserire una nuova data e ora
     * nel formato {@code dd/MM/yyyy HH:mm}. La data inserita viene
     * convertita in un oggetto {@link LocalDateTime} e assegnata
     * alla proiezione.</p>
     *
     * @param proiezione la proiezione di cui modificare data e ora
     * @return la proiezione con la nuova data e ora
     */
    public Proiezione cambiaData(Proiezione proiezione) {
        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        System.out.print(
                "Inserisci data e ora (es. 01/08/2026 14:30): "
        );

        String input = scanner.nextLine();
        LocalDateTime dataOra =
                LocalDateTime.parse(input, formatter);

        proiezione.setDataOra(dataOra);

        return proiezione;
    }

    /**
     * Elimina una proiezione dalla lista delle proiezioni.
     *
     * @param proiezione la proiezione da eliminare
     * @param proiezioni lista di proiezioni da cui rimuovere la proiezione
     */
    public void eliminaProiezione(Proiezione proiezione,
                                  List<Proiezione> proiezioni) {
        proiezioni.remove(proiezione);
    }

    /**
     * Effettua il logout del proiezionista.
     *
     * <p>Imposta lo stato di autenticazione dell'utente a
     * {@code false}.</p>
     */
    public void logout() {
        setLoggato(false);
    }
}
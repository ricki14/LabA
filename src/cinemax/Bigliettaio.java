/*
 * Edoardo Carducci - 764215 - Varese
 * Daniele Rossetti - 767980 - Varese
 * Riccardo Palomba - 764224 - Varese
 */

package cinemax;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Rappresenta un utente con il ruolo di bigliettaio del sistema Cinemax.
 *
 * <p>Il bigliettaio può visualizzare le prenotazioni effettuate
 * nella giornata corrente e cercare una prenotazione tramite
 * diversi criteri.</p>
 *
 * @author Riccardo Palomba
 * @version 1.0
 */
public class Bigliettaio extends Utente {

    /**
     * Costruisce un nuovo bigliettaio specificando la data di nascita.
     *
     * @param nome nome del bigliettaio
     * @param cognome cognome del bigliettaio
     * @param username username del bigliettaio
     * @param password password del bigliettaio
     * @param dataDiNascita data di nascita
     * @param domicilio domicilio del bigliettaio
     */
    public Bigliettaio(
            String nome,
            String cognome,
            String username,
            String password,
            LocalDate dataDiNascita,
            Domicilio domicilio) {

        super(
                nome,
                cognome,
                username,
                password,
                dataDiNascita,
                domicilio,
                Ruolo.BIGLIETTAIO,
                false
        );
    }

    /**
     * Costruisce un nuovo bigliettaio senza data di nascita.
     *
     * @param nome nome del bigliettaio
     * @param cognome cognome del bigliettaio
     * @param username username del bigliettaio
     * @param password password del bigliettaio
     * @param domicilio domicilio del bigliettaio
     */
    public Bigliettaio(
            String nome,
            String cognome,
            String username,
            String password,
            Domicilio domicilio) {

        super(
                nome,
                cognome,
                username,
                password,
                domicilio,
                Ruolo.BIGLIETTAIO
        );
    }

    /**
     * Visualizza le prenotazioni effettuate nella data odierna.
     *
     * @param prenotazioni lista delle prenotazioni
     */
    public void visualizzaPrenotazioniOggi(
            List<Prenotazione> prenotazioni) {

        LocalDate oggi = LocalDate.now();
        boolean trovata = false;

        System.out.println(
                "\n===== PRENOTAZIONI DI OGGI ====="
        );

        for (Prenotazione prenotazione :
                prenotazioni) {

            if (prenotazione.getDataAcquisto()
                    .equals(oggi)) {

                System.out.println(
                        prenotazione
                );

                trovata = true;
            }
        }

        if (!trovata) {
            System.out.println(
                    "Nessuna prenotazione effettuata oggi."
            );
        }
    }

    /**
     * Cerca una prenotazione tramite ID.
     *
     * @param utenti lista degli utenti del sistema
     * @param scanner scanner utilizzato per l'input
     */
    public void cercaPerId(
            List<Utente> utenti,
            Scanner scanner) {

        System.out.print(
                "Inserisci l'ID della prenotazione: "
        );

        String id =
                scanner.nextLine();

        GestorePrenotazioni gestore =
                new GestorePrenotazioni();

        Prenotazione prenotazione =
                gestore.cercaPerId(
                        utenti,
                        id
                );

        if (prenotazione == null) {

            System.out.println(
                    "Prenotazione non trovata."
            );

            return;
        }

        System.out.println(
                "\n===== PRENOTAZIONE TROVATA ====="
        );

        System.out.println(
                prenotazione
        );
    }

    /**
     * Cerca le prenotazioni tramite nome e cognome del cliente.
     *
     * @param utenti lista degli utenti del sistema
     * @param scanner scanner utilizzato per l'input
     */
    public void cercaPerNomeECognome(
            List<Utente> utenti,
            Scanner scanner) {

        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("Cognome: ");
        String cognome = scanner.nextLine();

        GestorePrenotazioni gestore =
                new GestorePrenotazioni();

        List<Prenotazione> risultati =
                gestore.cercaPerNomeECognome(
                        utenti,
                        nome,
                        cognome
                );

        if (risultati.isEmpty()) {

            System.out.println(
                    "Nessuna prenotazione trovata."
            );

            return;
        }

        System.out.println(
                "\n===== PRENOTAZIONI TROVATE ====="
        );

        for (Prenotazione prenotazione :
                risultati) {

            System.out.println(
                    prenotazione
            );
        }
    }

    /**
     * Cerca una prenotazione tramite ID, nome e cognome.
     *
     * @param utenti lista degli utenti del sistema
     * @param scanner scanner utilizzato per l'input
     */
    public void cercaPerIdNomeECognome(
            List<Utente> utenti,
            Scanner scanner) {

        System.out.print("ID prenotazione: ");
        String id = scanner.nextLine();

        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("Cognome: ");
        String cognome = scanner.nextLine();

        GestorePrenotazioni gestore =
                new GestorePrenotazioni();

        Prenotazione prenotazione =
                gestore.cercaPerIdNomeECognome(
                        utenti,
                        id,
                        nome,
                        cognome
                );

        if (prenotazione == null) {

            System.out.println(
                    "Prenotazione non trovata."
            );

            return;
        }

        System.out.println(
                "\n===== PRENOTAZIONE TROVATA ====="
        );

        System.out.println(
                prenotazione
        );
    }
}

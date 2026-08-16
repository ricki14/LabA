package cinemax;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Rappresenta un utente con il ruolo di proiezionista del sistema Cinemax.
 *
 * <p>Il proiezionista può aggiungere ed eliminare proiezioni dalla
 * programmazione e modificare la data e l'ora di una proiezione.</p>
 *
 * @author Daniele Rossetti
 * @version 1.0
 */
public class Proiezionista extends Utente {

    /**
     * Costruisce un nuovo proiezionista.
     *
     * @param nome nome del proiezionista
     * @param cognome cognome del proiezionista
     * @param username username del proiezionista
     * @param password password del proiezionista
     * @param dataDiNascita data di nascita
     * @param domicilio domicilio
     * @param ruolo ruolo dell'utente
     * @param loggato stato di autenticazione
     */
    public Proiezionista(
            String nome,
            String cognome,
            String username,
            String password,
            LocalDate dataDiNascita,
            Domicilio domicilio,
            Ruolo ruolo,
            boolean loggato) {

        super(
                nome,
                cognome,
                username,
                password,
                dataDiNascita,
                domicilio,
                ruolo,
                loggato
        );
    }

    /**
     * Aggiunge una proiezione alla programmazione.
     *
     * <p>Prima dell'inserimento viene controllato che la nuova
     * proiezione non si sovrapponga alle proiezioni già presenti.
     * Nel controllo viene considerata anche la durata del film.</p>
     *
     * @param proiezione proiezione da aggiungere
     * @param proiezioni lista delle proiezioni
     * @return {@code true} se la proiezione viene aggiunta
     */
    public boolean aggiungiProiezioni(
            Proiezione proiezione,
            List<Proiezione> proiezioni) {

        if (proiezione == null
                || proiezioni == null) {
            return false;
        }

        if (proiezione.getDataOra()
                .isBefore(LocalDateTime.now())) {

            System.out.println(
                    "Non è possibile aggiungere una proiezione "
                            + "con una data già trascorsa."
            );

            return false;
        }

        if (ceSovrapposizione(
                proiezione,
                proiezione.getDataOra(),
                proiezioni)) {

            System.out.println(
                    "La proiezione si sovrappone a una "
                            + "proiezione già presente."
            );

            return false;
        }

        proiezioni.add(proiezione);
        GestoreProiezioni.scriviProiezioni(proiezioni);

        return true;
    }

    /**
     * Modifica la data e l'ora di una proiezione.
     *
     * @param proiezione proiezione da modificare
     * @param proiezioni lista delle proiezioni
     * @param nuovaDataOra nuova data e ora
     * @return {@code true} se la modifica viene effettuata
     */
    public boolean cambiaData(
            Proiezione proiezione,
            List<Proiezione> proiezioni,
            LocalDateTime nuovaDataOra) {

        if (proiezione == null
                || proiezioni == null
                || nuovaDataOra == null) {
            return false;
        }

        if (proiezione.getDataOra()
                .isBefore(LocalDateTime.now())) {

            System.out.println(
                    "Non è possibile modificare una "
                            + "proiezione già iniziata."
            );

            return false;
        }

        if (nuovaDataOra.isBefore(LocalDateTime.now())) {

            System.out.println(
                    "La nuova data deve essere successiva "
                            + "alla data attuale."
            );

            return false;
        }

        if (ceSovrapposizione(
                proiezione,
                nuovaDataOra,
                proiezioni)) {

            System.out.println(
                    "La nuova data si sovrappone a una "
                            + "proiezione già presente."
            );

            return false;
        }

        proiezione.setDataOra(
                nuovaDataOra
        );

        GestoreProiezioni.scriviProiezioni(
                proiezioni
        );

        return true;
    }

    /**
     * Controlla se una proiezione si sovrappone a una delle
     * proiezioni già presenti nella programmazione.
     *
     * @param proiezione proiezione da controllare
     * @param inizio nuovo orario di inizio
     * @param proiezioni lista delle proiezioni
     * @return {@code true} se esiste una sovrapposizione
     */
    public boolean ceSovrapposizione(
            Proiezione proiezione,
            LocalDateTime inizio,
            List<Proiezione> proiezioni) {

        LocalDateTime fine =
                inizio.plusMinutes(
                        proiezione
                                .getFilm()
                                .getDurata()
                );

        for (Proiezione altra :
                proiezioni) {

            if (altra == proiezione) {
                continue;
            }

            LocalDateTime altroInizio =
                    altra.getDataOra();

            LocalDateTime altroFine =
                    altroInizio.plusMinutes(
                            altra.getFilm()
                                    .getDurata()
                    );

            if (inizio.isBefore(altroFine)
                    && fine.isAfter(altroInizio)) {

                return true;
            }
        }

        return false;
    }

    /**
     * Elimina una proiezione dalla programmazione.
     *
     * @param proiezione proiezione da eliminare
     * @param proiezioni lista delle proiezioni
     */
    public void eliminaProiezione(
            Proiezione proiezione,
            List<Proiezione> proiezioni) {

        proiezioni.remove(proiezione);
        GestoreProiezioni.scriviProiezioni(
                proiezioni
        );
    }

    /**
     * Effettua il logout del proiezionista.
     */
    public void logout() {
        setLoggato(false);
    }
}

package cinemax;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;

/**
 * Rappresenta un cliente registrato del sistema Cinemax.
 *
 * <p>La classe estende {@link Utente} e gestisce le prenotazioni
 * effettuate dal cliente.</p>
 *
 * @author Edoardo Carducci
 * @version 1.0
 */
public class ClienteRegistrato extends Utente {

    /** Lista delle prenotazioni effettuate dal cliente. */
    private LinkedList<Prenotazione> prenotazioniCliente;

    /**
     * Costruisce un nuovo cliente registrato.
     *
     * @param nome nome del cliente
     * @param cognome cognome del cliente
     * @param username username del cliente
     * @param password password del cliente
     * @param dataDiNascita data di nascita
     * @param domicilio domicilio del cliente
     * @param ruolo ruolo dell'utente
     */
    public ClienteRegistrato(
            String nome,
            String cognome,
            String username,
            String password,
            LocalDate dataDiNascita,
            Domicilio domicilio,
            Ruolo ruolo) {

        super(
                nome,
                cognome,
                username,
                password,
                dataDiNascita,
                domicilio,
                Ruolo.CLIENTE
        );

        prenotazioniCliente =
                new LinkedList<>();
    }

    /**
     * Costruisce un cliente registrato senza data di nascita.
     *
     * @param nome nome del cliente
     * @param cognome cognome del cliente
     * @param username username del cliente
     * @param password password del cliente
     * @param domicilio domicilio del cliente
     * @param ruolo ruolo dell'utente
     */
    public ClienteRegistrato(
            String nome,
            String cognome,
            String username,
            String password,
            Domicilio domicilio,
            Ruolo ruolo) {

        super(
                nome,
                cognome,
                username,
                password,
                domicilio,
                Ruolo.CLIENTE
        );

        prenotazioniCliente =
                new LinkedList<>();
    }

    /**
     * Restituisce le prenotazioni del cliente.
     *
     * @return lista delle prenotazioni
     */
    public LinkedList<Prenotazione> getPrenotazioniCliente() {
        return prenotazioniCliente;
    }

    /**
     * Visualizza le prenotazioni del cliente.
     *
     * @return stringa contenente le prenotazioni
     */
    public String visualizzaPrenotazione() {

        if (prenotazioniCliente == null
                || prenotazioniCliente.isEmpty()) {
            return "Nessuna prenotazione trovata";
        }

        StringBuilder risultato =
                new StringBuilder();

        for (Prenotazione prenotazione :
                prenotazioniCliente) {

            risultato.append(
                    prenotazione
            );
            risultato.append(" | \n");
        }

        return "Prenotazioni a nome "
                + getNome()
                + " "
                + getCognome()
                + ": "
                + risultato;
    }

    /**
     * Crea una nuova prenotazione.
     *
     * <p>Prima della creazione viene controllato che tutti i posti
     * appartengano alla proiezione e che siano ancora liberi.</p>
     *
     * @param proiezione proiezione da prenotare
     * @param postiPrenotati posti selezionati
     * @return esito dell'operazione
     */
    public String creaPrenotazione(
            Proiezione proiezione,
            LinkedList<Posto> postiPrenotati) {

        if (proiezione == null
                || postiPrenotati == null
                || postiPrenotati.isEmpty()) {

            return "Operazione non riuscita: proiezione o posti non validi";
        }

        for (Posto posto : postiPrenotati) {

            if (!proiezione.getPosti().contains(posto)) {
                return "Operazione non riuscita: posto non appartenente alla proiezione";
            }

            if (posto.isOccupato()) {
                return "Operazione non riuscita: uno dei posti selezionati è già occupato";
            }
        }

        Prenotazione nuovaPrenotazione =
                new Prenotazione(
                        this,
                        proiezione,
                        postiPrenotati
                );

        prenotazioniCliente.add(
                nuovaPrenotazione
        );

        return "Prenotazione "
                + nuovaPrenotazione.getIdPrenotazione()
                + " completata";
    }

    /**
     * Modifica la data e l'ora di una prenotazione.
     *
     * <p>Vengono controllate sia la data della prenotazione attuale
     * sia la nuova data. Prima di liberare i posti della vecchia
     * proiezione viene inoltre verificata la disponibilità degli
     * stessi posti nella nuova proiezione.</p>
     *
     * @param modPrenotazione prenotazione da modificare
     * @param nuovaDataOra nuova data e ora
     * @param programmazione programmazione del cinema
     * @return {@code true} se la modifica è riuscita
     */
    public boolean modificaPrenotazione(
            Prenotazione modPrenotazione,
            LocalDateTime nuovaDataOra,
            ProgrammazioneCinema programmazione) {

        if (modPrenotazione == null
                || !prenotazioniCliente.contains(modPrenotazione)) {

            System.out.println(
                    "La prenotazione non è presente tra le tue prenotazioni."
            );
            return false;
        }

        LocalDateTime vecchiaData =
                modPrenotazione
                        .getProiezione()
                        .getDataOra();

        LocalDateTime adesso =
                LocalDateTime.now();

        if (vecchiaData.isBefore(adesso)) {

            System.out.println(
                    "Non è possibile modificare una prenotazione "
                            + "per una proiezione già iniziata."
            );
            return false;
        }

        if (nuovaDataOra == null
                || nuovaDataOra.isBefore(adesso)) {

            System.out.println(
                    "La nuova data deve essere successiva alla data attuale."
            );
            return false;
        }

        Proiezione nuovaProiezione = null;

        for (Proiezione proiezione :
                programmazione.getElencoProiezioni()) {

            if (proiezione.getFilm()
                    .getTitolo()
                    .equalsIgnoreCase(
                            modPrenotazione
                                    .getProiezione()
                                    .getFilm()
                                    .getTitolo()
                    )
                    && proiezione.getDataOra()
                    .equals(nuovaDataOra)) {

                nuovaProiezione = proiezione;
                break;
            }
        }

        if (nuovaProiezione == null) {

            System.out.println(
                    "Nessuna proiezione del film "
                            + modPrenotazione
                            .getProiezione()
                            .getFilm()
                            .getTitolo()
                            + " è disponibile nella data "
                            + nuovaDataOra
            );

            return false;
        }

        if (nuovaProiezione
                == modPrenotazione.getProiezione()) {

            System.out.println(
                    "La nuova proiezione coincide con quella attuale."
            );

            return false;
        }

        LinkedList<Posto> nuoviPosti =
                new LinkedList<>();

        for (Posto vecchioPosto :
                modPrenotazione.getPostiPrenotati()) {

            Posto nuovoPosto =
                    trovaPosto(
                            nuovaProiezione,
                            vecchioPosto.getLetteraFila(),
                            vecchioPosto.getNumeroPosto()
                    );

            if (nuovoPosto == null) {

                System.out.println(
                        "Il posto "
                                + vecchioPosto.getLetteraFila()
                                + vecchioPosto.getNumeroPosto()
                                + " non esiste nella nuova proiezione."
                );

                return false;
            }

            if (nuovoPosto.isOccupato()) {

                System.out.println(
                        "Il posto "
                                + nuovoPosto.getLetteraFila()
                                + nuovoPosto.getNumeroPosto()
                                + " non è disponibile nella nuova proiezione."
                );

                return false;
            }

            nuoviPosti.add(nuovoPosto);
        }

        modPrenotazione.annullaPrenotazione();

        Prenotazione nuovaPrenotazione =
                new Prenotazione(
                        this,
                        nuovaProiezione,
                        nuoviPosti,
                        modPrenotazione.getIdPrenotazione(),
                        modPrenotazione.getDataAcquisto()
                );

        prenotazioniCliente.remove(
                modPrenotazione
        );

        prenotazioniCliente.add(
                nuovaPrenotazione
        );

        System.out.println(
                "Prenotazione modificata con successo!"
        );

        return true;
    }

    /**
     * Elimina una prenotazione del cliente.
     *
     * @param eliminPren prenotazione da eliminare
     * @return {@code true} se l'eliminazione è riuscita
     */
    public boolean eliminaPrenotazione(
            Prenotazione eliminPren) {

        if (eliminPren == null
                || !prenotazioniCliente.contains(eliminPren)) {

            System.out.println(
                    "La prenotazione non è presente nella lista."
            );
            return false;
        }

        if (eliminPren.getProiezione()
                .getDataOra()
                .isBefore(LocalDateTime.now())) {

            System.out.println(
                    "Non è possibile eliminare la prenotazione "
                            + "per una proiezione già avvenuta."
            );

            return false;
        }

        eliminPren.annullaPrenotazione();
        prenotazioniCliente.remove(eliminPren);

        System.out.println(
                "La prenotazione è stata eliminata."
        );

        return true;
    }

    /**
     * Cerca un posto in una proiezione.
     *
     * @param proiezione proiezione in cui cercare
     * @param fila fila del posto
     * @param numero numero del posto
     * @return posto trovato oppure {@code null}
     */
    private Posto trovaPosto(
            Proiezione proiezione,
            char fila,
            int numero) {

        for (Posto posto :
                proiezione.getPosti()) {

            if (posto.getLetteraFila() == fila
                    && posto.getNumeroPosto() == numero) {

                return posto;
            }
        }
        return null;
    }
}

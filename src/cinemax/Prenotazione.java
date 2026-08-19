package cinemax;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.UUID;

/**
 * Rappresenta una prenotazione effettuata da un cliente registrato
 * per una determinata proiezione cinematografica.
 *
 * <p>Una prenotazione contiene il cliente che l'ha effettuata,
 * la proiezione scelta, i posti prenotati, un identificativo univoco
 * e la data in cui è stata effettuata.</p>
 *
 * <p>Alla creazione della prenotazione, tutti i posti presenti nella
 * lista vengono automaticamente impostati come occupati.</p>
 *
 * @author Edoardo Carducci
 * @version 1.0
 */
public class Prenotazione {

    /** Cliente che ha effettuato la prenotazione. */
    private ClienteRegistrato clienteRegistrato;

    /** Proiezione associata alla prenotazione. */
    private Proiezione proiezione;

    /** Posti prenotati. */
    private LinkedList<Posto> postiPrenotati;

    /** Identificativo della prenotazione. */
    private String idPrenotazione;

    /** Data di acquisto della prenotazione. */
    private LocalDate dataAcquisto;

    /**
     * Costruisce una nuova prenotazione.
     *
     * @param clienteRegistrato cliente che effettua la prenotazione
     * @param proiezione proiezione prenotata
     * @param postiPrenotati posti da prenotare
     */
    public Prenotazione(
            ClienteRegistrato clienteRegistrato,
            Proiezione proiezione,
            LinkedList<Posto> postiPrenotati) {

        this(
                clienteRegistrato,
                proiezione,
                postiPrenotati,
                "ID-" + UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase(),
                LocalDate.now()
        );
    }

    /**
     * Costruisce una prenotazione utilizzando un ID già esistente.
     * Viene utilizzato quando una prenotazione viene caricata dal file.
     *
     * @param clienteRegistrato cliente associato
     * @param proiezione proiezione prenotata
     * @param postiPrenotati posti prenotati
     * @param idPrenotazione ID della prenotazione
     */
    public Prenotazione(
            ClienteRegistrato clienteRegistrato,
            Proiezione proiezione,
            LinkedList<Posto> postiPrenotati,
            String idPrenotazione) {

        this(
                clienteRegistrato,
                proiezione,
                postiPrenotati,
                idPrenotazione,
                LocalDate.now()
        );
    }

    /**
     * Costruisce una prenotazione specificando anche ID e data.
     *
     * @param clienteRegistrato cliente associato
     * @param proiezione proiezione prenotata
     * @param postiPrenotati posti prenotati
     * @param idPrenotazione ID della prenotazione
     * @param dataAcquisto data di acquisto
     */
    public Prenotazione(
            ClienteRegistrato clienteRegistrato,
            Proiezione proiezione,
            LinkedList<Posto> postiPrenotati,
            String idPrenotazione,
            LocalDate dataAcquisto) {

        this.clienteRegistrato = clienteRegistrato;
        this.proiezione = proiezione;
        this.postiPrenotati = postiPrenotati;
        this.idPrenotazione = idPrenotazione;
        this.dataAcquisto = dataAcquisto;

        for (Posto posto : postiPrenotati) {
            posto.prenota();
        }
    }

    /**
     * Calcola il costo totale della prenotazione.
     *
     * @return costo totale
     */
    public double costoTotale() {
        return postiPrenotati.size()
                * proiezione.getPrezzoBiglietto();
    }

    /**
     * Annulla la prenotazione e libera i posti.
     */
    public void annullaPrenotazione() {

        if (postiPrenotati != null) {

            for (Posto posto : postiPrenotati) {
                posto.liberaPosto();
            }
        }
    }

    /**
     * Restituisce il cliente della prenotazione.
     *
     * @return cliente associato
     */
    public ClienteRegistrato getClienteRegistrato() {
        return clienteRegistrato;
    }

    /**
     * Restituisce la proiezione della prenotazione.
     *
     * @return proiezione associata
     */
    public Proiezione getProiezione() {
        return proiezione;
    }

    /**
     * Restituisce i posti prenotati.
     *
     * @return lista dei posti
     */
    public LinkedList<Posto> getPostiPrenotati() {
        return postiPrenotati;
    }

    /**
     * Restituisce l'ID della prenotazione.
     *
     * @return identificativo
     */
    public String getIdPrenotazione() {
        return idPrenotazione;
    }

    /**
     * Restituisce la data di acquisto.
     *
     * @return data di acquisto
     */
    public LocalDate getDataAcquisto() {
        return dataAcquisto;
    }

    /**
     * Restituisce una rappresentazione testuale della prenotazione.
     *
     * @return informazioni della prenotazione
     */
    @Override
    public String toString() {

        StringBuilder posti =
                new StringBuilder();

        for (int i = 0;
             i < postiPrenotati.size();
             i++) {

            if (i > 0) {
                posti.append(", ");
            }

            Posto posto =
                    postiPrenotati.get(i);

            posti.append(
                    posto.getLetteraFila()
            );
            posti.append(
                    posto.getNumeroPosto()
            );
        }

        return "Prenotazione #" + idPrenotazione +
                " | Cliente: "
                + clienteRegistrato.getUsername() +
                " | Film: "
                + (proiezione != null
                ? proiezione.getFilm().getTitolo()
                : "Not available") +
                " | N° Posti: "
                + postiPrenotati.size() +
                " | Posti: " + posti +
                " | Data: " + dataAcquisto +
                " | Totale: "
                + costoTotale() + "€";

    }
}

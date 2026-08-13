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
 * @author Edoardo Carducci
 * @version 1.0
 */
public class Prenotazione {

    // Campi
    private ClienteRegistrato clienteRegistrato;
    private Proiezione proiezione;
    private LinkedList<Posto> postiPrenotati;
    private String idPrenotazione;
    private LocalDate dataAcquisto;

    /**
     * Costruisce una nuova prenotazione.
     *
     * <p>Viene generato automaticamente un identificativo per la
     * prenotazione utilizzando un UUID. L'identificativo è composto
     * dal prefisso {@code ID-} seguito dai primi otto caratteri
     * dell'UUID in maiuscolo.</p>
     *
     * <p>La data di acquisto viene impostata automaticamente alla
     * data corrente. Inoltre, tutti i posti presenti nella lista
     * vengono impostati come occupati.</p>
     *
     * @param clienteRegistrato il cliente che effettua la prenotazione
     * @param proiezione la proiezione per cui viene effettuata la prenotazione
     * @param postiPrenotati lista dei posti da prenotare
     */
    public Prenotazione(ClienteRegistrato clienteRegistrato, Proiezione proiezione,
                        LinkedList<Posto> postiPrenotati) {

        this.clienteRegistrato = clienteRegistrato;
        this.proiezione = proiezione;
        this.postiPrenotati = postiPrenotati;

        /*
         * Generazione dell'identificativo della prenotazione.
         * L'UUID viene abbreviato utilizzando i primi 8 caratteri.
         */
        this.idPrenotazione =
                "ID-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        this.dataAcquisto = LocalDate.now();

        for (Posto tmp : postiPrenotati) {
            tmp.prenota();
        }
    }

    /**
     * Calcola il costo totale della prenotazione.
     *
     * <p>Il costo viene calcolato moltiplicando il numero di posti
     * prenotati per il prezzo del singolo biglietto della proiezione.</p>
     *
     * @return il costo totale della prenotazione
     */
    public double costoTotale() {
        return postiPrenotati.size() * proiezione.getPrezzoBiglietto();
    }

    /**
     * Annulla la prenotazione e libera tutti i posti precedentemente
     * prenotati.
     *
     * <p>Se la lista dei posti non è {@code null}, ogni posto viene
     * impostato come libero.</p>
     */
    public void annullaPrenotazione() {
        if (postiPrenotati != null) {
            for (Posto tmp : postiPrenotati) {
                tmp.liberaPosto();
            }
        }
    }

    /**
     * Restituisce il cliente registrato che ha effettuato la prenotazione.
     *
     * @return il cliente registrato associato alla prenotazione
     */
    public ClienteRegistrato getClienteRegistrato() {
        return clienteRegistrato;
    }

    /**
     * Restituisce la proiezione associata alla prenotazione.
     *
     * @return la proiezione prenotata
     */
    public Proiezione getProiezione() {
        return proiezione;
    }

    /**
     * Restituisce la lista dei posti prenotati.
     *
     * @return la lista dei posti associati alla prenotazione
     */
    public LinkedList<Posto> getPostiPrenotati() {
        return postiPrenotati;
    }

    /**
     * Restituisce l'identificativo della prenotazione.
     *
     * @return l'ID della prenotazione
     */
    public String getIdPrenotazione() {
        return idPrenotazione;
    }

    /**
     * Restituisce la data in cui è stata effettuata la prenotazione.
     *
     * @return la data di acquisto
     */
    public LocalDate getDataAcquisto() {
        return dataAcquisto;
    }

    /**
     * Restituisce una rappresentazione testuale della prenotazione.
     *
     * <p>La rappresentazione contiene l'identificativo della prenotazione,
     * il cliente, il film, il numero di posti, la data di acquisto
     * e il costo totale.</p>
     *
     * @return una stringa contenente le informazioni della prenotazione
     */
    @Override
    public String toString() {
        return "Prenotazione #" + idPrenotazione +
                " | Cliente: " + clienteRegistrato.getUsername() +
                " | Film: " +
                (proiezione != null
                        ? proiezione.getFilm().getTitolo()
                        : "Not available") +
                " | N° Posti: " + postiPrenotati.size() +
                " | Data: " + dataAcquisto +
                " | Totale: " + costoTotale() + "€";
    }
}
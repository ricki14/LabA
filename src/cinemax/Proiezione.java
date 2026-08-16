package cinemax;

import java.time.LocalDateTime;
import java.util.LinkedList;

/**
 * Rappresenta una singola proiezione di un film.
 *
 * <p>Ogni proiezione contiene le informazioni relative a una specifica
 * programmazione del cinema, tra cui il film proiettato, la data e l'ora
 * della proiezione, il prezzo del biglietto e i posti disponibili.</p>
 *
 * <p>Ogni proiezione possiede una propria lista di 200 posti,
 * organizzati in 20 file da 10 posti ciascuna. I posti sono inizialmente
 * tutti liberi.</p>
 *
 * @author Riccardo Palomba
 * @version 1.0
 */
public class Proiezione {

    /**
     * Identificativo univoco della proiezione.
     */
    private String id;

    /**
     * Film associato alla proiezione.
     */
    private Film film;

    /**
     * Data e ora in cui avrà luogo la proiezione.
     */
    private LocalDateTime dataOra;

    /**
     * Prezzo del biglietto per la proiezione.
     */
    private double prezzoBiglietto;

    /**
     * Lista dei posti disponibili nella sala della proiezione.
     */
    private LinkedList<Posto> posti;

    /**
     * Costruisce una nuova proiezione.
     *
     * <p>Durante la costruzione vengono creati automaticamente
     * 200 posti, organizzati nelle file dalla A alla T,
     * con 10 posti per ogni fila.</p>
     *
     * @param id identificativo della proiezione
     * @param film film associato alla proiezione
     * @param dataOra data e ora della proiezione
     * @param prezzoBiglietto prezzo del biglietto
     */
    public Proiezione(String id, Film film, LocalDateTime dataOra,
                      double prezzoBiglietto) {

        this.id = id;
        this.film = film;
        this.dataOra = dataOra;
        this.prezzoBiglietto = prezzoBiglietto;

        posti = new LinkedList<>();

        for (char fila = 'A'; fila <= 'T'; fila++) {
            for (int numero = 1; numero <= 10; numero++) {
                posti.add(new Posto(numero, fila));
            }
        }
    }

    /**
     * Restituisce l'identificativo della proiezione.
     *
     * @return identificativo della proiezione
     */
    public String getId() {
        return id;
    }

    /**
     * Restituisce il film associato alla proiezione.
     *
     * @return film della proiezione
     */
    public Film getFilm() {
        return film;
    }

    /**
     * Restituisce la data e l'ora della proiezione.
     *
     * @return data e ora della proiezione
     */
    public LocalDateTime getDataOra() {
        return dataOra;
    }

    /**
     * Modifica la data e l'ora della proiezione.
     *
     * @param dataOra nuova data e ora della proiezione
     */
    public void setDataOra(LocalDateTime dataOra) {
        this.dataOra = dataOra;
    }

    /**
     * Restituisce il prezzo del biglietto.
     *
     * @return prezzo del biglietto
     */
    public double getPrezzoBiglietto() {
        return prezzoBiglietto;
    }

    /**
     * Modifica il prezzo del biglietto.
     *
     * @param prezzoBiglietto nuovo prezzo del biglietto
     */
    public void setPrezzoBiglietto(double prezzoBiglietto) {
        this.prezzoBiglietto = prezzoBiglietto;
    }

    /**
     * Restituisce la lista dei posti della proiezione.
     *
     * @return lista dei posti
     */
    public LinkedList<Posto> getPosti() {
        return posti;
    }

    /**
     * Restituisce il numero di posti attualmente liberi.
     *
     * <p>Il numero viene calcolato controllando lo stato di ogni posto,
     * evitando di mantenere un contatore separato che potrebbe non essere
     * sincronizzato con lo stato reale dei posti.</p>
     *
     * @return numero di posti liberi
     */
    public int getPostiLiberi() {

        int liberi = 0;

        for (Posto posto : posti) {
            if (!posto.isOccupato()) {
                liberi++;
            }
        }

        return liberi;
    }

    /**
     * Restituisce una rappresentazione testuale della proiezione.
     *
     * <p>La rappresentazione contiene l'identificativo della proiezione,
     * il titolo del film, la data e l'ora, il prezzo del biglietto
     * e il numero di posti liberi.</p>
     *
     * @return stringa contenente le informazioni della proiezione
     */
    @Override
    public String toString() {
        return "Proiezione{" +
                "id='" + id + '\'' +
                ", film=" + film.getTitolo() +
                ", dataOra=" + dataOra +
                ", prezzo=" + prezzoBiglietto +
                ", postiLiberi=" + getPostiLiberi() +
                '}';
    }
}

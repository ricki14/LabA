package cinemax;

import java.time.LocalDateTime;

/**
 * Rappresenta una singola proiezione di un film.
 *
 * <p>Ogni proiezione contiene le informazioni relative a una specifica
 * programmazione del cinema, tra cui il film proiettato, la data e l'ora
 * della proiezione, il prezzo del biglietto e il numero di posti liberi.</p>
 *
 * <p>Una stessa pellicola può avere più proiezioni in giorni o orari
 * differenti. Per questo motivo possono esistere più oggetti
 * {@code Proiezione} associati allo stesso {@link Film}.</p>
 *
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
     * Numero di posti attualmente liberi nella sala.
     */
    private int postiLiberi = 100;

    /**
     * Costruisce una nuova proiezione.
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
    }

    /**
     * Restituisce l'identificativo della proiezione.
     *
     * @return l'identificativo della proiezione
     */
    public String getId() {
        return id;
    }

    /**
     * Restituisce il film associato alla proiezione.
     *
     * @return il film della proiezione
     */
    public Film getFilm() {
        return film;
    }

    /**
     * Restituisce la data e l'ora della proiezione.
     *
     * @return la data e l'ora della proiezione
     */
    public LocalDateTime getDataOra() {
        return dataOra;
    }

    /**
     * Modifica la data e l'ora della proiezione.
     *
     * @param dataOra la nuova data e ora della proiezione
     */
    public void setDataOra(LocalDateTime dataOra) {
        this.dataOra = dataOra;
    }

    /**
     * Restituisce il prezzo del biglietto.
     *
     * @return il prezzo del biglietto
     */
    public double getPrezzoBiglietto() {
        return prezzoBiglietto;
    }

    /**
     * Modifica il prezzo del biglietto.
     *
     * @param prezzoBiglietto il nuovo prezzo del biglietto
     */
    public void setPrezzoBiglietto(double prezzoBiglietto) {
        this.prezzoBiglietto = prezzoBiglietto;
    }

    /**
     * Restituisce il numero di posti liberi.
     *
     * @return il numero di posti liberi
     */
    public int getPostiLiberi() {
        return postiLiberi;
    }

    /**
     * Modifica il numero di posti liberi.
     *
     * @param postiLiberi il nuovo numero di posti liberi
     */
    public void setPostiLiberi(int postiLiberi) {
        this.postiLiberi = postiLiberi;
    }

    /**
     * Restituisce una rappresentazione testuale della proiezione.
     *
     * <p>La rappresentazione contiene l'identificativo della proiezione,
     * il titolo del film, la data e l'ora, il prezzo del biglietto
     * e il numero di posti liberi.</p>
     *
     * @return una stringa contenente le informazioni della proiezione
     */
    @Override
    public String toString() {
        return "Proiezione{" +
                "id='" + id + '\'' +
                ", film=" + film.getTitolo() +
                ", dataOra=" + dataOra +
                ", prezzo=" + prezzoBiglietto +
                ", postiLiberi=" + postiLiberi +
                '}';
    }
}
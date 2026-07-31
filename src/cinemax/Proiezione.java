/**
 * La classe Proiezione rappresenta una singola proiezione di un film.
 * Ogni oggetto contiene tutte le informazioni relative a una specifica
 * programmazione del cinema, come il film proiettato, la data e l'ora
 * della proiezione e il prezzo del biglietto.
 * Una stessa pellicola può avere più proiezioni in giorni o orari diversi,
 * quindi esisteranno più oggetti Proiezione associati allo stesso Film.
 */
package cinemax;

import java.time.LocalDateTime;

public class Proiezione {

    private String id;
    private Film film;
    private LocalDateTime dataOra;
    private double prezzoBiglietto;

    // Posti liberi (temporaneo)
    private int postiLiberi = 100;

    public Proiezione(String id, Film film, LocalDateTime dataOra, double prezzoBiglietto) {
        this.id = id;
        this.film = film;
        this.dataOra = dataOra;
        this.prezzoBiglietto = prezzoBiglietto;
    }

    public String getId() {
        return id;
    }

    public Film getFilm() {
        return film;
    }

    public LocalDateTime getDataOra() {
        return dataOra;
    }

    public void setDataOra(LocalDateTime dataOra) {
        this.dataOra = dataOra;
    }

    public double getPrezzoBiglietto() {
        return prezzoBiglietto;
    }

    public void setPrezzoBiglietto(double prezzoBiglietto) {
        this.prezzoBiglietto = prezzoBiglietto;
    }

    public int getPostiLiberi() {
        return postiLiberi;
    }

    public void setPostiLiberi(int postiLiberi) {
        this.postiLiberi = postiLiberi;
    }

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
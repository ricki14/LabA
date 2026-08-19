package cinemax;

/**
 * Rappresenta un film presente nel sistema Cinemax.
 *
 * <p>Un film è caratterizzato dal titolo, dal genere, dal regista,
 * dall'anno di uscita, dalla durata e dall'età minima richiesta
 * per la visione.</p>
 *
 * @author Riccardo Palomba
 * @version 1.0
 */
public class Film {

    // Campi
    private String titolo;
    private String genere;
    private String regista;
    private int anno;
    private int durata;
    private int etaMinima;

    /**
     * Costruisce un nuovo film.
     *
     * @param titolo il titolo del film
     * @param genere il genere del film
     * @param regista il regista del film
     * @param anno l'anno di uscita del film
     * @param durata la durata del film
     * @param etaMinima l'età minima richiesta per la visione del film
     */
    public Film(String titolo, String genere, String regista,
                int anno, int durata, int etaMinima) {

        this.titolo = titolo;
        this.genere = genere;
        this.regista = regista;
        this.anno = anno;
        this.durata = durata;
        this.etaMinima = etaMinima;
    }

    /**
     * Restituisce il titolo del film.
     *
     * @return il titolo del film
     */
    public String getTitolo() {
        return titolo;
    }

    /**
     * Restituisce il genere del film.
     *
     * @return il genere del film
     */
    public String getGenere() {
        return genere;
    }

    /**
     * Restituisce il regista del film.
     *
     * @return il regista del film
     */
    public String getRegista() {
        return regista;
    }

    /**
     * Restituisce l'anno di uscita del film.
     *
     * @return l'anno di uscita del film
     */
    public int getAnno() {
        return anno;
    }

    /**
     * Restituisce la durata del film.
     *
     * @return la durata del film
     */
    public int getDurata() {
        return durata;
    }

    /**
     * Restituisce l'età minima richiesta per la visione del film.
     *
     * @return l'età minima richiesta
     */

    public int getEtaMinima() {
        return etaMinima;
    }
}
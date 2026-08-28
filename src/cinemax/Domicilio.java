/*
 * Edoardo Carducci - 764215 - Varese
 * Daniele Rossetti - 767980 - Varese
 * Riccardo Palomba - 764224 - Varese
 */

package cinemax;

/**
 * Rappresenta il domicilio di un utente del sistema CineMax.
 *
 * <p>Il domicilio contiene le informazioni relative all'indirizzo,
 * tra cui via, numero civico, CAP, città e provincia.</p>
 *
 * @author Edoardo Carducci
 * @version 1.0
 */
public class Domicilio {

    /**
     * Nome della via del domicilio.
     */
    private String via;

    /**
     * Numero civico del domicilio.
     */
    private String numeroCivico;

    /**
     * Codice di avviamento postale del domicilio.
     */
    private String cap;

    /**
     * Città del domicilio.
     */
    private String citta;

    /**
     * Provincia del domicilio.
     */
    private String provincia;

    /**
     * Costruisce un nuovo domicilio con i dati specificati.
     *
     * @param via la via del domicilio
     * @param numeroCivico il numero civico del domicilio
     * @param cap il codice di avviamento postale
     * @param citta la città del domicilio
     * @param provincia la provincia del domicilio
     */
    public Domicilio(String via, String numeroCivico, String cap,
                     String citta, String provincia) {
        this.via = via;
        this.numeroCivico = numeroCivico;
        this.cap = cap;
        this.citta = citta;
        this.provincia = provincia;
    }

    /**
     * Restituisce la via del domicilio.
     *
     * @return la via del domicilio
     */
    public String getVia() {
        return via;
    }

    /**
     * Restituisce il numero civico del domicilio.
     *
     * @return il numero civico del domicilio
     */
    public String getNumeroCivico() {
        return numeroCivico;
    }

    /**
     * Restituisce il CAP del domicilio.
     *
     * @return il codice di avviamento postale
     */
    public String getCap() {
        return cap;
    }

    /**
     * Restituisce la città del domicilio.
     *
     * @return la città del domicilio
     */
    public String getCitta() {
        return citta;
    }

    /**
     * Restituisce la provincia del domicilio.
     *
     * @return la provincia del domicilio
     */
    public String getProvincia() {
        return provincia;
    }

    /**
     * Restituisce una rappresentazione testuale del domicilio.
     *
     * <p>La stringa contiene la via, il numero civico, il CAP,
     * la città e la provincia.</p>
     *
     * @return una stringa contenente i dati del domicilio
     */
    @Override
    public String toString() {
        return via + " " + numeroCivico + ", (" + cap + "), "
                + citta + ", " + provincia;}
}
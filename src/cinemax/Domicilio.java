package cinemax;

/**
 * Rappresenta il domicilio di un utente del sistema Cinemax.
 *
 * <p>Il domicilio contiene le informazioni relative all'indirizzo,
 * tra cui via, numero civico, CAP, città e provincia.</p>
 *
 * @version 1.0
 */
public class Domicilio {

    /**
     * Nome della via.
     */
    private String via;

    /**
     * Numero civico dell'abitazione.
     */
    private String numeroCivico;

    /**
     * Codice di avviamento postale.
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
     * Costruisce un nuovo domicilio.
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
                + citta + ", " + provincia;
    }
}
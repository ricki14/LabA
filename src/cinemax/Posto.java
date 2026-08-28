/*
 * Edoardo Carducci - 764215 - Varese
 * Daniele Rossetti - 767980 - Varese
 * Riccardo Palomba - 764224 - Varese
 */

package cinemax;

/**
 * Rappresenta un posto a sedere all'interno di una sala cinematografica.
 *
 * <p>Ogni posto è identificato da un numero e dalla lettera della fila
 * a cui appartiene. Inoltre, il posto può essere libero oppure occupato.</p>
 *
 * @author Edoardo Carducci
 * @version 1.0
 */
public class Posto {

    // Campi

    /**
     * Numero del posto.
     */
    private int numeroPosto;

    /**
     * Lettera della fila.
     */
    private char letteraFila;

    /**
     * Indica se il posto è occupato.
     */
    private boolean occupato;

    /**
     * Costruisce un nuovo posto.
     *
     * <p>Il posto viene inizialmente impostato come libero.</p>
     *
     * @param numeroPosto il numero del posto
     * @param letteraFila la lettera della fila a cui appartiene il posto
     */
    public Posto(int numeroPosto, char letteraFila) {
        this.numeroPosto = numeroPosto;
        this.letteraFila = letteraFila;
        occupato = false;
    }

    /**
     * Restituisce il numero del posto.
     *
     * @return il numero del posto
     */
    public int getNumeroPosto() {
        return numeroPosto;
    }

    /**
     * Restituisce la lettera della fila del posto.
     *
     * @return la lettera della fila
     */
    public char getLetteraFila() {
        return letteraFila;
    }

    /**
     * Verifica se il posto è occupato.
     *
     * @return {@code true} se il posto è occupato,
     *         {@code false} se il posto è libero
     */
    public boolean isOccupato() {
        return occupato;
    }

    /**
     * Prenota il posto.
     *
     * <p>Imposta lo stato del posto come occupato.</p>
     */
    public void prenota() {
        occupato = true;
    }


    /**
     * Libera il posto.
     *
     * <p>Imposta lo stato del posto come libero.</p>
     */
    public void liberaPosto() {
        occupato = false;
    }

    /**
     * Imposta lo stato di occupazione del posto.
     *
     * @param occupato {@code true} se il posto è occupato,
     *                 {@code false} altrimenti
     */
    public void setOccupato(boolean occupato) {
        this.occupato = occupato;
    }

    /**
     * Restituisce una rappresentazione testuale del posto,
     * indicando la fila, il numero e lo stato di occupazione.
     *
     * @return una stringa contenente le informazioni del posto
     */
    @Override
    public String toString() {
        return "fila " + letteraFila + ", posto numero " + numeroPosto + " (" +
                (occupato ? "occupato" : "libero") + ")";
    }
}
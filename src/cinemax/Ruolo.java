package cinemax;

/**
 * Enum che rappresenta i possibili ruoli degli utenti
 * all'interno del sistema Cinemax.
 *
 * <p>Ogni utente del sistema è associato a uno dei ruoli
 * disponibili.</p>
 *
 * @author Edoardo Carducci
 * @version 1.0
 */
public enum Ruolo {

    /**
     * Rappresenta un cliente registrato del cinema.
     */
    CLIENTE,

    /**
     * Rappresenta un proiezionista del cinema.
     */
    PROIEZIONISTA,

    /**
     * Rappresenta un bigliettaio del cinema.
     */
    BIGLIETTAIO
}
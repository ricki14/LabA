package cinemax;

import java.time.LocalDate;

/**
 * Rappresenta un utente generico del sistema Cinemax.
 *
 * <p>La classe è astratta e costituisce la classe base per le diverse
 * tipologie di utenti del sistema. Contiene le informazioni personali
 * dell'utente, le credenziali di accesso, il domicilio, il ruolo e
 * lo stato di autenticazione.</p>
 *
 * <p>La data di nascita è un campo facoltativo e per questo motivo
 * sono disponibili due costruttori: uno che permette di specificarla
 * e uno che permette di creare un utente senza data di nascita.</p>
 *
 * @version 1.0
 */
public abstract class Utente {

    /**
     * Nome dell'utente.
     */
    private String nome;

    /**
     * Cognome dell'utente.
     */
    private String cognome;

    /**
     * Nome utente utilizzato per l'accesso al sistema.
     */
    private String username;

    /**
     * Password utilizzata per l'accesso al sistema.
     *
     * <p>Attualmente la password viene memorizzata senza cifratura.</p>
     */
    private String password;

    /**
     * Data di nascita dell'utente.
     *
     * <p>Il campo è facoltativo.</p>
     */
    private LocalDate dataDiNascita;

    /**
     * Domicilio dell'utente.
     */
    private Domicilio domicilio;

    /**
     * Ruolo dell'utente all'interno del sistema.
     */
    private Ruolo ruolo;

    /**
     * Indica se l'utente è attualmente autenticato.
     */
    private boolean loggato;

    /**
     * Costruisce un nuovo utente specificando anche la data di nascita
     * e lo stato di autenticazione.
     *
     * @param nome il nome dell'utente
     * @param cognome il cognome dell'utente
     * @param username il nome utente utilizzato per l'accesso
     * @param password la password utilizzata per l'accesso
     * @param dataDiNascita la data di nascita dell'utente
     * @param domicilio il domicilio dell'utente
     * @param ruolo il ruolo dell'utente
     * @param loggato indica se l'utente è autenticato
     */
    public Utente(String nome, String cognome, String username,
                  String password, LocalDate dataDiNascita,
                  Domicilio domicilio, Ruolo ruolo, boolean loggato) {

        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.password = password;
        this.dataDiNascita = dataDiNascita;
        this.domicilio = domicilio;
        this.ruolo = ruolo;
        this.loggato = loggato;
    }

    /**
     * Costruisce un nuovo utente senza specificare la data di nascita.
     *
     * <p>Questo costruttore viene utilizzato quando la data di nascita
     * non è disponibile, essendo un campo facoltativo.</p>
     *
     * @param nome il nome dell'utente
     * @param cognome il cognome dell'utente
     * @param username il nome utente utilizzato per l'accesso
     * @param password la password utilizzata per l'accesso
     * @param domicilio il domicilio dell'utente
     * @param ruolo il ruolo dell'utente
     */
    public Utente(String nome, String cognome, String username,
                  String password, Domicilio domicilio, Ruolo ruolo) {

        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.password = password;
        this.domicilio = domicilio;
        this.ruolo = ruolo;
    }

    /**
     * Restituisce il nome dell'utente.
     *
     * @return il nome dell'utente
     */
    public String getNome() {
        return nome;
    }

    /**
     * Restituisce il cognome dell'utente.
     *
     * @return il cognome dell'utente
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * Restituisce il nome utente.
     *
     * @return lo username dell'utente
     */
    public String getUsername() {
        return username;
    }

    /**
     * Restituisce la password dell'utente.
     *
     * @return la password dell'utente
     */
    public String getPassword() {
        return password;
    }

    /**
     * Modifica il nome dell'utente.
     *
     * @param nome il nuovo nome dell'utente
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Modifica il cognome dell'utente.
     *
     * @param cognome il nuovo cognome dell'utente
     */
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    /**
     * Modifica il nome utente.
     *
     * @param username il nuovo username dell'utente
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Modifica la password dell'utente.
     *
     * @param password la nuova password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Restituisce la data di nascita dell'utente.
     *
     * @return la data di nascita dell'utente
     */
    public LocalDate getDataDiNascita() {
        return dataDiNascita;
    }

    /**
     * Modifica la data di nascita dell'utente.
     *
     * @param dataDiNascita la nuova data di nascita
     */
    public void setDataDiNascita(LocalDate dataDiNascita) {
        this.dataDiNascita = dataDiNascita;
    }

    /**
     * Restituisce il domicilio dell'utente.
     *
     * @return il domicilio dell'utente
     */
    public Domicilio getDomicilio() {
        return domicilio;
    }

    /**
     * Modifica il domicilio dell'utente.
     *
     * @param domicilio il nuovo domicilio dell'utente
     */
    public void setDomicilio(Domicilio domicilio) {
        this.domicilio = domicilio;
    }

    /**
     * Restituisce il ruolo dell'utente.
     *
     * @return il ruolo dell'utente
     */
    public Ruolo getRuolo() {
        return ruolo;
    }

    /**
     * Modifica il ruolo dell'utente.
     *
     * @param ruolo il nuovo ruolo dell'utente
     */
    public void setRuolo(Ruolo ruolo) {
        this.ruolo = ruolo;
    }

    /**
     * Modifica lo stato di autenticazione dell'utente.
     *
     * @param loggato {@code true} se l'utente deve essere considerato
     *                autenticato, {@code false} altrimenti
     */
    public void setLoggato(boolean loggato) {
        this.loggato = loggato;
    }
}
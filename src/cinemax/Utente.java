/*
 * Edoardo Carducci - 764215 - Varese
 * Daniele Rossetti - 767980 - Varese
 * Riccardo Palomba - 764224 - Varese
 */

package cinemax;


import java.time.LocalDate;

/**
 * Rappresenta un utente generico del sistema Cinemax.
 *
 * <p>La classe è astratta e costituisce la classe base per le diverse
 * tipologie di utenti del sistema. Contiene le informazioni personali
 * dell'utente, le credenziali di accesso, il domicilio, il ruolo e
 * lo stato di autenticazione.</p>

 * <p>La data di nascita è un campo facoltativo e per questo motivo
 * sono disponibili più costruttori per la creazione delle istanze.</p>
 *
 * @author Edoardo Carducci
 * @version 1.0
 */
public abstract class Utente {

    /** Nome dell'utente. */
    private String nome;

    /** Cognome dell'utente. */
    private String cognome;

    /** Nome utente utilizzato per l'accesso al sistema. */
    private String username;

    /** Password utilizzata per l'accesso al sistema. */
    private String password;

    /** Data di nascita dell'utente (campo facoltativo). */
    private LocalDate dataDiNascita;

    /** Domicilio dell'utente. */
    private Domicilio domicilio;

    /** Ruolo dell'utente all'interno del sistema. */
    private Ruolo ruolo;

    /** Indica se l'utente è attualmente autenticato. */
    private boolean loggato;

    /**
     * Costruisce un nuovo utente specificando data di nascita e stato di autenticazione.
     *
     * @param nome          il nome dell'utente
     * @param cognome       il cognome dell'utente
     * @param username      il nome utente utilizzato per l'accesso
     * @param password      la password utilizzata per l'accesso
     * @param dataDiNascita la data di nascita dell'utente
     * @param domicilio     il domicilio dell'utente
     * @param ruolo         il ruolo dell'utente
     * @param loggato       indica se l'utente è autenticato
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
     * Costruisce un nuovo utente specificando la data di nascita (con loggato impostato a false di default).
     *
     * @param nome          il nome dell'utente
     * @param cognome       il cognome dell'utente
     * @param username      il nome utente utilizzato per l'accesso
     * @param password      la password utilizzata per l'accesso
     * @param dataDiNascita la data di nascita dell'utente
     * @param domicilio     il domicilio dell'utente
     * @param ruolo         il ruolo dell'utente
     */
    public Utente(String nome, String cognome, String username,
                  String password, LocalDate dataDiNascita,
                  Domicilio domicilio, Ruolo ruolo) {
        this(nome, cognome, username, password, dataDiNascita, domicilio, ruolo, false);
    }

    /**
     * Costruisce un nuovo utente senza specificare la data di nascita.
     *
     * @param nome      il nome dell'utente
     * @param cognome   il cognome dell'utente
     * @param username  il nome utente utilizzato per l'accesso
     * @param password  la password utilizzata per l'accesso
     * @param domicilio il domicilio dell'utente
     * @param ruolo     il ruolo dell'utente
     */
    public Utente(String nome, String cognome, String username,
                  String password, Domicilio domicilio, Ruolo ruolo) {
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.password = password;
        this.domicilio = domicilio;
        this.ruolo = ruolo;
        this.loggato = false;
    }

    // --- GETTER E SETTER ---

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getDataDiNascita() {
        return dataDiNascita;
    }

    public void setDataDiNascita(LocalDate dataDiNascita) {
        this.dataDiNascita = dataDiNascita;
    }

    public Domicilio getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(Domicilio domicilio) {
        this.domicilio = domicilio;
    }

    public Ruolo getRuolo() {
        return ruolo;
    }

    public void setRuolo(Ruolo ruolo) {
        this.ruolo = ruolo;
    }


    public void setLoggato(boolean loggato) {
        this.loggato = loggato;
    }

    /**
     * Confronta due utenti verificando l'uguaglianza dello username (case-insensitive).
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof Utente)) return false;
        Utente altroUtente = (Utente) o;
        if (this.getUsername() == null || altroUtente.getUsername() == null) return false;
        return this.getUsername().equalsIgnoreCase(altroUtente.getUsername());
    }
}
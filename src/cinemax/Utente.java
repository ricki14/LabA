package cinemax;
import java.time.LocalDate;

public abstract class Utente {

    //campi
    private String nome;
    private String cognome;
    private String username;
    private String password;
    private LocalDate dataDiNascita;
    private Domicilio domicilio;
    private Ruolo ruolo;

    public Utente(String nome, String cognome, String username, String password, LocalDate dataDiNascita,
                  Domicilio domicilio, Ruolo ruolo){
        this.nome=nome;
        this.cognome=cognome;
        this.username=username;
        this.password=password; //la password non è cifrata al momento
        this.dataDiNascita=dataDiNascita;
        this.domicilio=domicilio;
        this.ruolo=ruolo;
    }

    //utente senza data di nascita (è un campo facoltativo)
    public Utente(String nome, String cognome, String username, String password,
                  Domicilio domicilio, Ruolo ruolo){
        this.nome=nome;
        this.cognome=cognome;
        this.username=username;
        this.password=password; //la password non è cifrata al momento
        this.domicilio=domicilio;
        this.ruolo=ruolo;
    }

    //metodi

    public String getNome() {
        return nome;
    }
    public String getCognome() {
        return cognome;
    }
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public void setUsername(String username) {
        this.username = username;
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
}

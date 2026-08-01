package cinemax;

import java.time.LocalDate;
import java.util.LinkedList;

public class ClienteRegistrato extends Utente{

    //campi
    private LinkedList<>

    //costruttore con data di nascita
    public ClienteRegistrato (String nome, String cognome, String username, String password,
                              LocalDate dataDiNascita, Domicilio domicilio, Ruolo ruolo){
        super(nome, cognome, username, password, dataDiNascita, domicilio, Ruolo.CLIENTE);
    }

    //costruttore senza data di nascita
    public ClienteRegistrato (String nome, String cognome, String username, String password,
                              Domicilio domicilio, Ruolo ruolo){
        super(nome, cognome, username, password, domicilio, Ruolo.CLIENTE);
    }

    //metodi
    public void creaPrenotazione(){

    }

    public void visualizzaPrenotazione(){

    }

    public void modificaPrenotazione(){

    }

    public void eliminaPrenotazione(){

    }
}

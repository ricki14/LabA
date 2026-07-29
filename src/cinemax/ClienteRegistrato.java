package cinemax;

import java.time.LocalDate;

public class ClienteRegistrato extends Utente{

    //costruttore
    public ClienteRegistrato (String nome, String cognome, String username, String password,
                              LocalDate dataDiNascita, Domicilio domicilio, Ruolo ruolo){
        super(nome, cognome, username, password, dataDiNascita, domicilio, Ruolo.CLIENTE);
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

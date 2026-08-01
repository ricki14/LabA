package cinemax;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Proiezionista extends Utente {


    public Proiezionista(String nome, String cognome, String username, String password, LocalDate dataDiNascita, Domicilio domicilio, Ruolo ruolo, boolean loggato) {
        super(nome, cognome, username, password, dataDiNascita, domicilio, ruolo, loggato);
    }

    public void aggiungiProiezioni(Proiezione proiezione, List<Proiezione> proiezioni){
        proiezioni.add(proiezione);
    }

    public Proiezione cambiaData(Proiezione proiezione){
        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        System.out.print("Inserisci data e ora (es. 01/08/2026 14:30): ");
        String input = scanner.nextLine();
        LocalDateTime dataOra = LocalDateTime.parse(input, formatter);
        proiezione.setDataOra(dataOra);
        return proiezione;
    }

    public void eliminaProiezione(Proiezione proiezione, List<Proiezione> proiezioni){
        proiezioni.remove(proiezione);
    }

    public void logout(){
        setLoggato(false);
    }

}

package cinemax;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.UUID;

public class Prenotazione {

    //campi
    private ClienteRegistrato clienteRegistrato;
    private Proiezione proiezione;
    private LinkedList<Posto> postiPrenotati;
    private String idPrenotazione;
    private LocalDate dataAcquisto;

    //costruttore
    public Prenotazione(ClienteRegistrato clienteRegistrato, Proiezione proiezione,
                        LinkedList<Posto> postiPrenotati){
        this.clienteRegistrato=clienteRegistrato;
        this.proiezione=proiezione;
        this.postiPrenotati=postiPrenotati;

        //ha 4,3 miliardi di combinazioni, quindi è impossibile che ci siano collisioni.
        // Se dovessimo scalare su milioni di prenotazioni al secondo su un server globale,
        // basterebbe togliere il .substring(0, 8) e usare l'UUID completo a 128 bit.
        this.idPrenotazione= "ID-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        this.dataAcquisto=LocalDate.now();
        for(Posto tmp : postiPrenotati){
            tmp.prenota();
        }
    }

    //metodi
    public double costoTotale(){
        return postiPrenotati.size() * proiezione.getPrezzoBiglietto();
    }

    public void annullaPrenotazione(){
        if(postiPrenotati != null) {
            for (Posto tmp : postiPrenotati) {
                tmp.liberaPosto();
            }
        }
    }

    public ClienteRegistrato getClienteRegistrato() {
        return clienteRegistrato;
    }

    public Proiezione getProiezione() {
        return proiezione;
    }

    public LinkedList<Posto> getPostiPrenotati() {
        return postiPrenotati;
    }

    public String getIdPrenotazione() {
        return idPrenotazione;
    }

    public LocalDate getDataAcquisto() {
        return dataAcquisto;
    }

    @Override
    public String toString(){
        return "Prenotazione #" + idPrenotazione +
                " | Cliente: " + clienteRegistrato.getUsername() +
                " | Film: " + (proiezione != null ? proiezione.getFilm().getTitolo() : "Not available") +
                " | N° Posti: " + postiPrenotati.size() +
                " | Data: " + dataAcquisto +
                " | Totale: " + costoTotale() + "€";
    }
}

/**
 * La classe Proiezioni gestisce l'insieme di tutte le proiezioni del cinema.
 * Contiene una collezione di oggetti Proiezione e mette a disposizione
 * i metodi per aggiungere, cercare e visualizzare le proiezioni disponibili.
 * Questa classe centralizza la gestione delle programmazioni del cinema,
 * evitando che le operazioni di ricerca e consultazione siano distribuite
 * in altre classi del progetto.
 */
package cinemax;

import java.util.ArrayList;

public class Proiezioni {

    private ArrayList<Proiezione> elencoProiezioni;
    public Proiezioni() {
        elencoProiezioni = new ArrayList<>();
    }
    public void aggiungiProiezione(Proiezione proiezione) {
        elencoProiezioni.add(proiezione);
    }
    public ArrayList<Proiezione> getElencoProiezioni() {
        return elencoProiezioni;
    }
    public Proiezione cercaPerId(String id) {
        for (Proiezione p : elencoProiezioni) {
            if (p.getId().equalsIgnoreCase(id)) {
                return p;
            }
        }
        return null;
    }

    public ArrayList<Proiezione> cercaPerTitolo(String titolo) {

        ArrayList<Proiezione> risultati = new ArrayList<>();

        for (Proiezione p : elencoProiezioni) {
            if (p.getFilm().getTitolo().toLowerCase()
                    .contains(titolo.toLowerCase())) {

                risultati.add(p);
            }
        }

        return risultati;
    }

    public void visualizzaProiezioni() {

        for (Proiezione p : elencoProiezioni) {
            System.out.println(p);
        }
    }
}
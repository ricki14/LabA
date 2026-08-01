package cinemax;

public class Posto {

    //campi
    private int numeroPosto;
    private char letteraFila;
    private boolean occupato;


    //costruttore
    public Posto(int numeroPosto, char letteraFila){
        this.numeroPosto=numeroPosto;
        this.letteraFila=letteraFila;
        occupato=false;
    }

    //metodi
    public int getNumeroPosto() {
        return numeroPosto;
    }

    public char getLetteraFila() {
        return letteraFila;
    }

    public boolean isOccupato(){
        return occupato;
    }

    public void prenota(){
        occupato=true;
    }

    public void liberaPosto(){
        occupato=false;
    }

    @Override
    public String toString(){
        return "fila " +letteraFila+ ", posto numero " +numeroPosto+ " (" +
                (occupato? "occupato" : "libero")+ ")";
    }


}

package cinemax;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class GestoreProiezioni {
    private static String file = "proiezioni.csv";
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static List<Proiezione> leggiProiezioni(){
        List<Proiezione> proiezioni = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine();
            String riga;

            while ((riga= br.readLine()) != null){
                String[] dati = riga.split(",");
                String data = dati[0].replace("\"","");
                String titolo = dati[1].replace("\"","");
                String genere = dati[2].replace("\"","");
                String regista = dati[3].replace("\"","");
                int anno = Integer.parseInt(dati[4]);
                int durata = Integer.parseInt(dati[5]);
                int etaMinima = Integer.parseInt(dati[6]);
                double prezzo = Double.parseDouble(dati[7]);
                LocalDateTime dataOra = LocalDateTime.parse(data,formatter);

                Film film = new Film(titolo,genere,regista,anno,durata,etaMinima);
                Proiezione proiezione = new Proiezione(String.valueOf(proiezioni.size()+1),film,dataOra,prezzo);
                proiezioni.add(proiezione);
            }

        } catch (IOException e) {
            System.out.println("Errore durante la lettura del file"+ e.getMessage());
        }
        return proiezioni;
    }

    public static void scriviProiezioni(List<Proiezione> proiezioni){
        try(PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            pw.println("data_ora_proiezione,titolo_film,genere,regista,"+"anno,durata,eta_minima,prezzo_biglietto");
            for (Proiezione p : proiezioni){
                Film film = p.getFilm();

                pw.println(
                        "\"" +
                                p.getDataOra().format(formatter) +
                                "\"," +

                                "\"" +
                                film.getTitolo() +
                                "\"," +

                                "\"" +
                                film.getGenere() +
                                "\"," +

                                "\"" +
                                film.getRegista() +
                                "\"," +

                                film.getAnno() +
                                "," +

                                film.getDurata() +
                                "," +

                                film.getEtaMinima() +
                                "," +

                                p.getPrezzoBiglietto()
                );
            }
        }catch (IOException e){
            System.out.println("Errore nella scrittura: "+e.getMessage());
        }

    }
}
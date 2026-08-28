/*
 * Edoardo Carducci - 764215 - Varese
 * Daniele Rossetti - 767980 - Varese
 * Riccardo Palomba - 764224 - Varese
 */

package cinemax;

import java.io.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * Gestisce il caricamento e il salvataggio degli utenti
 * nel file CSV <code>data/utenti.csv</code>.
 *
 * <p>Le password presenti nel file sono memorizzate tramite
 * hash SHA-256 e gli username vengono mantenuti univoci.</p>
 *
 * @author Riccardo Palomba
 * @version 1.0
 */
public class GestoreUtenti {

    /** Percorso del file degli utenti. */
    private String percorsoFile;

    /** Lista degli utenti del sistema. */
    private LinkedList<Utente> utenti;

    /**
     * Costruisce il gestore degli utenti.
     *
     * @param utenti lista degli utenti da gestire
     */
    public GestoreUtenti(
            LinkedList<Utente> utenti) {

        this.utenti = utenti;
        this.percorsoFile =
                "data/utenti.csv";
    }

    /**
     * Salva gli utenti nel file CSV.
     *
     * <p>Se nella lista sono presenti utenti con lo stesso username,
     * viene salvata una sola occorrenza.</p>
     */
    public void salvaUtenti() {

        try (BufferedWriter writer =
                     new BufferedWriter(
                             new FileWriter(percorsoFile))) {

            writer.write(
                    "nome;cognome;username;password;dataDiNascita;"
                            + "via;numeroCivico;cap;citta;provincia;ruolo"
            );
            writer.newLine();

            Set<String> usernameSalvati =
                    new HashSet<>();

            for (Utente utente : utenti) {

                String username =
                        utente.getUsername();

                if (username == null
                        || !usernameSalvati.add(
                        username.toLowerCase())) {
                    continue;
                }

                Domicilio domicilio =
                        utente.getDomicilio();

                String via =
                        domicilio != null
                                ? domicilio.getVia()
                                : "";

                String numeroCivico =
                        domicilio != null
                                ? domicilio.getNumeroCivico()
                                : "";

                String cap =
                        domicilio != null
                                ? domicilio.getCap()
                                : "";

                String citta =
                        domicilio != null
                                ? domicilio.getCitta()
                                : "";

                String provincia =
                        domicilio != null
                                ? domicilio.getProvincia()
                                : "";

                writer.write(
                        utente.getNome() + ";"
                                + utente.getCognome() + ";"
                                + username + ";"
                                + utente.getPassword() + ";"
                                + utente.getDataDiNascita() + ";"
                                + via + ";"
                                + numeroCivico + ";"
                                + cap + ";"
                                + citta + ";"
                                + provincia + ";"
                                + utente.getRuolo()
                );

                writer.newLine();
            }

        } catch (IOException e) {

            System.out.println(
                    "Errore nel salvataggio degli utenti: "
                            + e.getMessage()
            );
        }
    }

    /**
     * Carica gli utenti dal file CSV.
     *
     * <p>Gli utenti con username già presente vengono ignorati
     * per evitare duplicati.</p>
     */
    public void caricaUtenti() {

        utenti.clear();

        File file =
                new File(percorsoFile);

        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader =
                     new BufferedReader(
                             new FileReader(file))) {

            reader.readLine();

            String riga;

            while ((riga = reader.readLine()) != null) {

                if (riga.trim().isEmpty()) {
                    continue;
                }

                String[] dati =
                        riga.split(";", -1);

                if (dati.length < 11) {
                    continue;
                }

                String username = dati[2];

                if (trovaUsername(username) != null) {
                    continue;
                }

                String nome = dati[0];
                String cognome = dati[1];
                String password = dati[3];

                LocalDate dataDiNascita =
                        LocalDate.parse(dati[4]);

                String via = dati[5];
                String numeroCivico = dati[6];
                String cap = dati[7];
                String citta = dati[8];
                String provincia = dati[9];

                Domicilio domicilio = null;

                if (!via.isEmpty()
                        || !citta.isEmpty()) {

                    domicilio =
                            new Domicilio(
                                    via,
                                    numeroCivico,
                                    cap,
                                    citta,
                                    provincia
                            );
                }

                Ruolo ruolo =
                        Ruolo.valueOf(
                                dati[10].toUpperCase()
                        );


                Utente utente = null;

                if (ruolo == Ruolo.CLIENTE) {

                    utente =
                            new ClienteRegistrato(
                                    nome,
                                    cognome,
                                    username,
                                    password,
                                    dataDiNascita,
                                    domicilio,
                                    ruolo
                            );

                } else if (ruolo == Ruolo.BIGLIETTAIO) {

                    utente =
                            new Bigliettaio(
                                    nome,
                                    cognome,
                                    username,
                                    password,
                                    dataDiNascita,
                                    domicilio
                            );

                } else if (ruolo == Ruolo.PROIEZIONISTA) {

                    utente =
                            new Proiezionista(
                                    nome,
                                    cognome,
                                    username,
                                    password,
                                    dataDiNascita,
                                    domicilio,
                                    ruolo,
                                    false
                            );
                }

                if (utente != null) {
                    utenti.add(utente);
                }
            }

            System.out.println(
                    "Caricamento utenti completato con successo."
            );

        } catch (IOException e) {

            System.out.println(
                    "Errore nella lettura del file degli utenti: "
                            + e.getMessage()
            );

        } catch (Exception e) {

            System.out.println(
                    "Errore nel parsing dei dati degli utenti: "
                            + e.getMessage()
            );
        }
    }


    /**
     * Cerca uno username nella lista degli utenti.
     *
     * @param username username da cercare
     * @return utente trovato oppure {@code null}
     */
    private Utente trovaUsername(
            String username) {

        if (username == null) {
            return null;
        }

        for (Utente utente : utenti) {

            if (utente.getUsername()
                    .equalsIgnoreCase(username)) {

                return utente;
            }
        }

        return null;
    }
}

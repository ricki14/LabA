package cinemax;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Fornisce metodi per la gestione sicura delle password.
 *
 * <p>Le password vengono trasformate tramite l'algoritmo
 * SHA-256 prima di essere memorizzate o confrontate.</p>
 *
 * @author Riccardo Palomba
 * @version 1.0
 */
public final class PasswordUtil {

    /**
     * Costruttore privato per impedire la creazione
     * di oggetti della classe.
     */
    private PasswordUtil() {
    }

    /**
     * Calcola l'hash SHA-256 della password fornita.
     *
     * @param password password da trasformare
     * @return rappresentazione esadecimale dell'hash
     */
    public static String hashPassword(String password) {

        try {

            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            byte[] hash =
                    digest.digest(
                            password.getBytes(StandardCharsets.UTF_8)
                    );

            StringBuilder risultato = new StringBuilder();

            for (byte b : hash) {
                risultato.append(
                        String.format("%02x", b)
                );
            }

            return risultato.toString();

        } catch (NoSuchAlgorithmException e) {

            throw new IllegalStateException(
                    "Algoritmo SHA-256 non disponibile.",
                    e
            );
        }
    }
}
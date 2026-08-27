package cinemax;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class CineMax {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        Sistema sistema = inizializzaDati();

        System.out.println("----BENVENUTO DA CINEMAX----");

        menuPrincipale(scanner, sistema);

        scanner.close();
    }
}
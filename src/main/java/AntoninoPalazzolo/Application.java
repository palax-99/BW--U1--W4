package AntoninoPalazzolo;

import AntoninoPalazzolo.entities.Scanne;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.Scanner;

import static AntoninoPalazzolo.databasepopulator.DatabasePopulator.populate;

public class Application {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("bwu1w4");
    //collegato correttamente al database

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        //inserisco la scelta relativa al popolamento del database

        first:
        while (true) {
            System.out.println("Premi y per popolare il database con 30 utenti, 30 tessere, 10 distributori automatici, 5 rivenditori, 40 veicoli ognuno con un massimo di 10 status di manutenzione per veicolo, 100 biglietti, 20 abbonamenti, 20 tratte e 500 corse.\n Oppure premi n per continuare senza generare dati o p per personalizzare il numero dei popolamenti del database");
            String choice = scanner.nextLine();

            if ("n".equals(choice)) {
                break;
            } else if ("y".equals(choice)) {
                populate();
                break;
            } else if ("p".equals(choice)) {
                while (true) {
                    try {

                        System.out.println("Digita uno per riga: \n il numero di utenti che vuoi generare\n il numero di distributori \n il numero di rivenditori \n il numero di veicoli \n il numero di log massimi per ogni veicolo \n il numero di biglietti\n il numero di abbonamenti \n il numero di tratte \n il numero di corse ");
                        int numberOfUsers = Integer.parseInt(scanner.nextLine());
                        int numberOfVendingMachines = Integer.parseInt(scanner.nextLine());
                        int numberOfResellers = Integer.parseInt(scanner.nextLine());
                        int numberOfVehicles = Integer.parseInt(scanner.nextLine());
                        int maxNumberOfLogPerVehicle = Integer.parseInt(scanner.nextLine());
                        int numberOfTickets = Integer.parseInt(scanner.nextLine());
                        int numberOfPasses = Integer.parseInt(scanner.nextLine());
                        int numberOfRoutes = Integer.parseInt(scanner.nextLine());
                        int numberOfRuns = Integer.parseInt(scanner.nextLine());

                        System.out.println("Popolazione in corso");

                        populate(numberOfUsers, numberOfVendingMachines, numberOfResellers, numberOfVehicles, maxNumberOfLogPerVehicle,
                                numberOfTickets, numberOfPasses, numberOfRoutes, numberOfRuns);
                        break first;
                    } catch (RuntimeException e) {
                        System.out.println("inserimento non valido");
                        System.out.println();
                        break;
                    }

                }
            } else {
                System.out.println("Scelta non valida, riprova.");
            }

        }

        //fine del popolamento del database
        Scanne.start(emf);
        System.out.println("Hello World!");
    }
}

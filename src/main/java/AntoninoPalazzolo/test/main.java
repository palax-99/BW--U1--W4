package AntoninoPalazzolo.test;

import AntoninoPalazzolo.dao.RouteDAO;
import AntoninoPalazzolo.dao.RunDAO;
import AntoninoPalazzolo.dao.VehicleDAO;
import AntoninoPalazzolo.entities.Route;
import AntoninoPalazzolo.entities.Run;
import AntoninoPalazzolo.entities.Vehicle;
import AntoninoPalazzolo.entities.VehicleType;
import AntoninoPalazzolo.exception.NotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalTime;

public class main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("bwu1w4");
        EntityManager em = emf.createEntityManager();
        VehicleDAO vd = new VehicleDAO(em);
        RouteDAO rd = new RouteDAO(em);
        RunDAO runDAO = new RunDAO(em);

        // --- CREAZIONE VEICOLI ---
        Vehicle tram1 = new Vehicle("AB 123 CD", VehicleType.TRAM);
        Vehicle bus1 = new Vehicle("EF 456 GH", VehicleType.BUS);
        Vehicle bus2 = new Vehicle("IL 789 MN", VehicleType.BUS);
        // --- CREAZIONE TRATTE ---
        Route r1 = new Route("Milano Centrale", "Cadorna", 20,
                LocalTime.of(8, 0), LocalTime.of(8, 20));
        Route r2 = new Route("Cadorna", "Loreto", 15,
                LocalTime.of(9, 0), LocalTime.of(9, 15));

        try {
            // Salvo i veicoli nel DB
            //vd.saveVehicle(tram1);
            //vd.saveVehicle(bus1);
            //vd.saveVehicle(bus2);

            // Recupero un veicolo per id
            Vehicle trovato = vd.findById("3c7c295f-9ad3-4643-9d81-0bb9d9cdb03f");
            System.out.println("Veicolo trovato: " + trovato);
            // Stampo tutti i veicoli
            vd.findAll().forEach(System.out::println);

        } catch (NotFoundException e) {
            System.out.println("Errore: " + e.getMessage());
        }

        //try {
        // Recupero il veicolo già esistente nel DB tramite id
        //Vehicle trovato = vd.findById("3c7c295f-9ad3-4643-9d81-0bb9d9cdb03f");
        // Il tram va in manutenzione
        //vd.updateVehicleStatus(trovato, false, false);

        // Il tram torna in servizio
        //vd.updateVehicleStatus(trovato, true, false);

        // Il tram viene ritirato definitivamente
        //vd.updateVehicleStatus(trovato, false, true);

        //} catch (NotFoundException e) {
        //System.out.println("Errore: " + e.getMessage());
        //}

        try {
            // Recupero il tram dal DB
            Vehicle trovato = vd.findById("3c7c295f-9ad3-4643-9d81-0bb9d9cdb03f");

            // Stampo lo storico completo dei cambi di stato del veicolo
            System.out.println("Storico stati del veicolo " + trovato.getLicensePlate() + ":");
            vd.getVehicleStatusHistory(trovato).forEach(System.out::println);

        } catch (NotFoundException e) {
            System.out.println("Errore: " + e.getMessage());
        }
        try {

            // Salvo le tratte nel DB
            //rd.saveRoute(r1);
            //rd.saveRoute(r2);

            // Stampo tutte le tratte
            rd.findAll().forEach(System.out::println);

        } catch (NotFoundException e) {
            System.out.println("Errore: " + e.getMessage());
        }
        try {
            // Recupero veicolo e tratta già esistenti nel DB
            Vehicle trovato = vd.findById("3c7c295f-9ad3-4643-9d81-0bb9d9cdb03f");
            Route tratta = rd.findById("149df989-6fae-4327-91a8-6c3fa6d080bd");

            // Creo e salvo una corsa
            Run corsa = new Run(trovato, tratta, LocalTime.of(8, 0), LocalTime.of(8, 20));
            //runDAO.saveRun(corsa);

            // Stampo tutte le corse
            runDAO.findAll().forEach(System.out::println);

        } catch (NotFoundException e) {
            System.out.println("Errore: " + e.getMessage());
        }


        em.close();
        emf.close();

    }
}


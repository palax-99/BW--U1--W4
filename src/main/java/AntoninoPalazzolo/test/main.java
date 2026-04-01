package AntoninoPalazzolo.test;

import AntoninoPalazzolo.DAO.VehicleDAO;
import AntoninoPalazzolo.entities.Vehicle;
import AntoninoPalazzolo.entities.VehicleType;
import AntoninoPalazzolo.exception.NotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("bwu1w4");
        EntityManager em = emf.createEntityManager();
        VehicleDAO vd = new VehicleDAO(em);

        // --- CREAZIONE VEICOLI ---
        Vehicle tram1 = new Vehicle("AB 123 CD", VehicleType.TRAM);
        Vehicle bus1 = new Vehicle("EF 456 GH", VehicleType.BUS);
        Vehicle bus2 = new Vehicle("IL 789 MN", VehicleType.BUS);

        try {
            // Salvo i veicoli nel DB
            //vd.saveVehicle(tram1);
            //vd.saveVehicle(bus1);
            //vd.saveVehicle(bus2);

            // Recupero un veicolo per id
            Vehicle trovato = vd.findById(tram1.getIdVehicle().toString());
            System.out.println("Veicolo trovato: " + trovato);
            // Stampo tutti i veicoli
            vd.findAll().forEach(System.out::println);

        } catch (NotFoundException e) {
            System.out.println("Errore: " + e.getMessage());
        }
        em.close();
        emf.close();
    }
}


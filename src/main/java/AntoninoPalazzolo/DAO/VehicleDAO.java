package AntoninoPalazzolo.DAO;

import AntoninoPalazzolo.entities.Vehicle;
import AntoninoPalazzolo.exception.NotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.UUID;

public class VehicleDAO {

    private final EntityManager em;

    public VehicleDAO(EntityManager em) {
        this.em = em;
    }

    // Salvo un nuovo veicolo nel database
    public void saveVehicle(Vehicle vehicle) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.persist(vehicle);
        transaction.commit();
        System.out.println("Veicolo " + vehicle.getLicensePlate() + " salvato correttamente!");
    }

    // Cerco un veicolo per id — lancia eccezione se non trovato
    public Vehicle findById(String id) {
        Vehicle found = em.find(Vehicle.class, UUID.fromString(id));
        if (found == null) throw new NotFoundException(id);
        return found;
    }

    // Restituisco tutti i veicoli presenti nel database
    public List<Vehicle> findAll() {
        TypedQuery<Vehicle> query = em.createQuery("SELECT v FROM Vehicle v", Vehicle.class);
        return query.getResultList();
    }

}

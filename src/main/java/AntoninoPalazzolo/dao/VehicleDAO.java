package AntoninoPalazzolo.dao;

import AntoninoPalazzolo.entities.Vehicle;
import AntoninoPalazzolo.entities.VehicleStatusLog;
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

    // Aggiorno lo stato del veicolo creando un nuovo record nel log.
// Non modifico il record esistente per mantenere lo storico completo dei cambi di stato.
    public void updateVehicleStatus(Vehicle vehicle, boolean inService, boolean permanentlyOutOfService) {

        // Apro la transazione come sempre quando scrivo nel DB
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        // Creo un nuovo record di log con il veicolo e il nuovo stato.
        // Il costruttore di VehicleStatusLog imposta automaticamente LocalDateTime.now()
        // registrando il momento esatto del cambio di stato
        VehicleStatusLog log = new VehicleStatusLog(vehicle, inService, permanentlyOutOfService);

        // Persisto il log nel DB — non tocco il veicolo, aggiungo solo un nuovo record
        em.persist(log);

        // Confermo la transazione
        transaction.commit();

        System.out.println("Stato del veicolo " + vehicle.getLicensePlate() + " aggiornato correttamente!");
    }

    // Recupero lo storico completo dei cambi di stato di un veicolo
    // ordinato per data — dal più vecchio al più recente
    public List<VehicleStatusLog> getVehicleStatusHistory(Vehicle vehicle) {
        TypedQuery<VehicleStatusLog> query = em.createQuery(
                "SELECT v FROM VehicleStatusLog v WHERE v.vehicle = :vehicle ORDER BY v.vehicleAvailabilityUpdatedOn ASC",
                VehicleStatusLog.class
        );
        query.setParameter("vehicle", vehicle);
        return query.getResultList();
    }

}

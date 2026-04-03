package AntoninoPalazzolo.DAO;

import AntoninoPalazzolo.entities.Route;
import AntoninoPalazzolo.entities.Run;
import AntoninoPalazzolo.entities.Vehicle;
import AntoninoPalazzolo.exception.NotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.UUID;

public class RunDAO {

    private final EntityManager em;

    public RunDAO(EntityManager em) {
        this.em = em;
    }

    // Salvo una nuova corsa nel DB
    public void saveRun(Run run) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.persist(run);
        transaction.commit();
        System.out.println("Corsa salvata correttamente!");
    }

    // Cerco una corsa per id — lancia eccezione se non trovata
    public Run findById(String id) {
        Run found = em.find(Run.class, UUID.fromString(id));
        if (found == null) throw new NotFoundException(id);
        return found;
    }

    // Restituisco tutte le corse presenti nel DB
    public List<Run> findAll() {
        TypedQuery<Run> query = em.createQuery("SELECT r FROM Run r", Run.class);
        return query.getResultList();
    }

    // Restituisco tutte le corse effettuate su una specifica tratta
// con il numero totale di corse e il tempo effettivo di ognuna
    public List<Run> getRunsByRoute(Route route) {
        TypedQuery<Run> query = em.createQuery(
                "SELECT r FROM Run r WHERE r.route = :route ORDER BY r.actualDepartureTime ASC",
                Run.class
        );
        query.setParameter("route", route);
        return query.getResultList();
    }

    // Calcolo il tempo medio di percorrenza effettivo di una tratta
    public double getAverageTravelTime(Route route) {
        TypedQuery<Double> query = em.createQuery(
                "SELECT AVG(r.actualTravelTime) FROM Run r WHERE r.route = :route",
                Double.class
        );
        query.setParameter("route", route);
        Double result = query.getSingleResult();
        // Se non ci sono corse per questa tratta restituisco 0
        return result != null ? result : 0;
    }

    // Restituisco il numero di volte che un veicolo ha percorso una specifica tratta
    public long getRunCountByVehicleAndRoute(Vehicle vehicle, Route route) {
        TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(r) FROM Run r WHERE r.vehicle = :vehicle AND r.route = :route",
                Long.class
        );
        query.setParameter("vehicle", vehicle);
        query.setParameter("route", route);
        return query.getSingleResult();
    }

    // Calcolo il tempo medio di percorrenza di una tratta da parte di un singolo veicolo
    public double getAverageTravelTimeByVehicleAndRoute(Vehicle vehicle, Route route) {
        TypedQuery<Double> query = em.createQuery(
                "SELECT AVG(r.actualTravelTime) FROM Run r WHERE r.vehicle = :vehicle AND r.route = :route",
                Double.class
        );
        query.setParameter("vehicle", vehicle);
        query.setParameter("route", route);
        Double result = query.getSingleResult();
        // Se non ci sono corse restituisco 0
        return result != null ? result : 0;
    }
}

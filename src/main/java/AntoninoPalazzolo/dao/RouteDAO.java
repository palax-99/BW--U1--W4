package AntoninoPalazzolo.dao;

import AntoninoPalazzolo.entities.Route;
import AntoninoPalazzolo.exception.NotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.UUID;

public class RouteDAO {

    private final EntityManager em;

    public RouteDAO(EntityManager em) {
        this.em = em;
    }

    // Salvo una nuova tratta nel DB
    public void saveRoute(Route route) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.persist(route);
        transaction.commit();
        System.out.println("Tratta da " + route.getDeparture() + " a " + route.getTerminus() + " salvata correttamente!");
    }

    // Cerco una tratta per id — lancia eccezione se non trovata
    public Route findById(String id) {
        Route found = em.find(Route.class, UUID.fromString(id));
        if (found == null) throw new NotFoundException(id);
        return found;
    }

    // Restituisco tutte le tratte presenti nel DB
    public List<Route> findAll() {
        TypedQuery<Route> query = em.createQuery("SELECT r FROM Route r", Route.class);
        return query.getResultList();
    }
}

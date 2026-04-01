package AntoninoPalazzolo.DAO;

import AntoninoPalazzolo.entities.Run;
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
}

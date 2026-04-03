package AntoninoPalazzolo.dao;
import AntoninoPalazzolo.entities.AuthorizedIssuer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AuthorizedIssuerDAO {

    private final EntityManagerFactory emf;

    public AuthorizedIssuerDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    //Salva un nuovo Issuer nel database
    public void save(AuthorizedIssuer issuer) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(issuer);
        em.getTransaction().commit();
        em.close();
        //System.out.println("AuthorizedIssuer " + issuer.getIssuerName() + " saved");
    }

    //Effettua la ricerca tramite ID
    public Optional<AuthorizedIssuer> findById(UUID id) {
        EntityManager em = emf.createEntityManager();
        AuthorizedIssuer issuer = em.find(AuthorizedIssuer.class, id);
        em.close();
        return Optional.ofNullable(issuer);
    }

    //Ricerca e restituisce tutti gli Issuer autorizzati del database
    public List<AuthorizedIssuer> findAll() {
        EntityManager em = emf.createEntityManager();
        List<AuthorizedIssuer> issuers = em.createQuery(
                "SELECT i FROM AuthorizedIssuer i", AuthorizedIssuer.class).getResultList();
        em.close();
        return issuers;
    }

    //elimina un Issuer dal database
    public void delete(AuthorizedIssuer issuer) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.remove(em.contains(issuer) ? issuer : em.merge(issuer));
        em.getTransaction().commit();
        em.close();
    }
}
package AntoninoPalazzolo.entities.dao;
import AntoninoPalazzolo.entities.VendingMachine;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class VendingMachineDAO {

    private final EntityManagerFactory emf;

    public VendingMachineDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    //Salva un nuovo distributore nel database
    public void save(VendingMachine vendingMachine) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(vendingMachine);
        em.getTransaction().commit();
        em.close();
    }

    //Cerca un distributore tramite id
    public Optional<VendingMachine> findById(UUID id) {
        EntityManager em = emf.createEntityManager();
        VendingMachine vm = em.find(VendingMachine.class, id);
        em.close();
        return Optional.ofNullable(vm);
    }

    //ricerca di tutti i distributori nel database
    public List<VendingMachine> findAll() {
        EntityManager em = emf.createEntityManager();
        List<VendingMachine> vms = em.createQuery(
                "SELECT vm FROM VendingMachine vm", VendingMachine.class).getResultList();
        em.close();
        return vms;
    }

    //elimina un distributore dal database
    public void delete(VendingMachine vendingMachine) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.remove(em.contains(vendingMachine) ? vendingMachine : em.merge(vendingMachine));
        em.getTransaction().commit();
        em.close();
    }

    // Aggiorna lo stato di disponibilità di un distributore (attivo/fuori servizio)
    public void updateAvailability(UUID id, boolean available) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        VendingMachine vm = em.find(VendingMachine.class, id);
        if (vm != null) {
            vm.vendingMachineAvailability = available;
            em.merge(vm);
        }
        em.getTransaction().commit();
        em.close();
    }
}
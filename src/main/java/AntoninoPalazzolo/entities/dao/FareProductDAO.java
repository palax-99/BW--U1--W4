package AntoninoPalazzolo.entities.dao;

import AntoninoPalazzolo.entities.FareProduct;
import AntoninoPalazzolo.entities.Pass;
import AntoninoPalazzolo.entities.Ticket;
import AntoninoPalazzolo.exception.NotFoundException;
import AntoninoPalazzolo.exception.NotSavedException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class FareProductDAO {
    private final EntityManager entityManager;

    public FareProductDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // save può essere utilizzato per salvare biglietti e abbonamenti
    // per validare biglietti è sufficiente fare: save(ticketValidation(ticketDaDB, veicolo));
    // per sapere se un abbonamento valido è associato ad una tessera è sufficiente fare:
    // isThereAValidPass(entityManager, userCardNumber) (metodo statico)
    // per sapere biglietti e abbonamenti emessi in un dato periodo è sufficiente fare:
    // ticketsAndPassesIssued(startLocalDate, endLocalDate)
    // egualmente per i soli biglietti si può utilizzare ticketsIssued e per gli abbonamenti
    // passesIssued

    public void save(FareProduct newFareproduct) {
        try {
            EntityTransaction transaction = this.entityManager.getTransaction();
            transaction.begin();
            entityManager.persist(newFareproduct);
            transaction.commit();
            System.out.println("Il titolo di viaggio " + newFareproduct.getIdFareProduct() + " è stato emesso con successo!");
        } catch (Exception e) {
            throw new NotSavedException(newFareproduct);
        }

    }

    public FareProduct getById(UUID idFareProduct) {
        FareProduct found = entityManager.find(FareProduct.class, idFareProduct);
        if (found == null) throw new NotFoundException(idFareProduct.toString());
        return found;

    }

    public Ticket ticketGetById(UUID idTicket) {
        Ticket found = entityManager.find(Ticket.class, idTicket);
        if (found == null) throw new NotFoundException(idTicket.toString());
        return found;

    }

    public Pass passGetById(UUID idPass) {
        Pass found = entityManager.find(Pass.class, idPass);
        if (found == null) throw new NotFoundException(idPass.toString());
        return found;

    }

    public void deleteById(UUID idFareProduct) {
        FareProduct found = this.getById(idFareProduct);
        EntityTransaction transaction = this.entityManager.getTransaction();
        transaction.begin();
        entityManager.remove(found);
        transaction.commit();
        System.out.println("Il titolo di viaggio " + found.getIdFareProduct() + " è stato eliminato con successo!");

    }

    public Map<String, Long> ticketsAndPassesIssued (LocalDate startDate, LocalDate endDate){
        return entityManager.createQuery("SELECT f FROM FareProduct f WHERE issueDate>=:startdate AND issueDate<=:enddate", FareProduct.class).setParameter("startdate", startDate.atStartOfDay()).setParameter("enddate", endDate.plusDays(1).atStartOfDay()).getResultStream().collect(Collectors.groupingBy(f->f.getClass().getSimpleName(), Collectors.counting()));
    }

    public Map<String, Long> ticketsIssued (LocalDate startDate, LocalDate endDate){
        return entityManager.createQuery("SELECT t FROM Ticket t WHERE issueDate>=:startdate AND issueDate<=:enddate", Ticket.class).setParameter("startdate", startDate.atStartOfDay()).setParameter("enddate", endDate.plusDays(1).atStartOfDay()).getResultStream().collect(Collectors.groupingBy(f->f.getClass().getSimpleName(), Collectors.counting()));
    }

    public Map<String, Long> passesIssued (LocalDate startDate, LocalDate endDate){
        return entityManager.createQuery("SELECT p FROM Pass p WHERE issueDate>=:startdate AND issueDate<=:enddate", Pass.class).setParameter("startdate", startDate.atStartOfDay()).setParameter("enddate", endDate.plusDays(1).atStartOfDay()).getResultStream().collect(Collectors.groupingBy(f->f.getClass().getSimpleName(), Collectors.counting()));
    }

    public static Boolean isThereAValidPass (EntityManager entityManager, Long userCardNumber){
        try {
        return entityManager.createQuery("SELECT p FROM Pass p WHERE p.userCard.userCardNumber = :numero AND p.passExpiryDate >= CURRENT_DATE", Pass.class).setParameter("numero", userCardNumber).getSingleResult() != null;}
        catch (NoResultException e){
            return false;
        }
    }
}

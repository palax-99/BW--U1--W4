package AntoninoPalazzolo.DAO;

import AntoninoPalazzolo.entities.FareProduct;
import AntoninoPalazzolo.entities.Pass;
import AntoninoPalazzolo.entities.Ticket;
import AntoninoPalazzolo.entities.Vehicle;
import AntoninoPalazzolo.exception.NotFoundException;
import AntoninoPalazzolo.exception.NotSavedException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class FareProductDAO {
    private final EntityManager entityManager;

    public FareProductDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // save può essere utilizzato per salvare biglietti e abbonamenti
    // Per validare biglietti è sufficiente fare: save(ticketValidation(ticketDaDB, veicolo));
    // Per sapere se un abbonamento valido è associato ad una tessera è sufficiente fare:
    // isThereAValidPass(entityManager, userCardNumber) (metodo statico)
    // Per sapere biglietti e abbonamenti emessi in un dato periodo è sufficiente fare:
    // ticketsAndPassesIssued(startLocalDate, endLocalDate)
    // Egualmente per i soli biglietti si può utilizzare ticketsIssued e per gli abbonamenti
    // passesIssued
    // Con la stessa logica è possibile avere i dati per ogni venditore:
    // ticketsAndPassesIssuedGroupedByIssuer
    // ticketsIssuedGroupedByIssuer
    // passesIssuedGroupedByIssuer
    // È poi possibile avere il numero di biglietti validati in un periodo con ticketsValidated
    // e lo stesso numero diviso per mezzi con: ticketsValidatedGroupedByVehicle
    // Per vedere il numero di biglietti validati su un veicolo specifico:
    // ticketsValidatedOnAVehicle(vehicle)
    // Infine per avere le statistiche di tutti i veicoli si può usare:
    // ticketsValidatedOnVehicles()

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

    public Map<String, Map<String, Long>> ticketsAndPassesIssuedGroupedByIssuer (LocalDate startDate, LocalDate endDate){
        return entityManager.createQuery("SELECT f FROM FareProduct f WHERE issueDate>=:startdate AND issueDate<=:enddate", FareProduct.class).setParameter("startdate", startDate.atStartOfDay()).setParameter("enddate", endDate.plusDays(1).atStartOfDay()).getResultStream().collect(Collectors.groupingBy(f->f.getAuthorizedIssuer().getIssuerName(), Collectors.groupingBy(f->f.getClass().getSimpleName(), Collectors.counting())));
    }

    public Map<String, Long> ticketsIssuedGroupedByIssuer  (LocalDate startDate, LocalDate endDate){
        return entityManager.createQuery("SELECT t FROM Ticket t WHERE issueDate>=:startdate AND issueDate<=:enddate", Ticket.class).setParameter("startdate", startDate.atStartOfDay()).setParameter("enddate", endDate.plusDays(1).atStartOfDay()).getResultStream().collect(Collectors.groupingBy(f->f.getAuthorizedIssuer().getIssuerName(), Collectors.counting()));
    }

    public Map<String, Long> passesIssuedGroupedByIssuer  (LocalDate startDate, LocalDate endDate){
        return entityManager.createQuery("SELECT p FROM Pass p WHERE issueDate>=:startdate AND issueDate<=:enddate", Pass.class).setParameter("startdate", startDate.atStartOfDay()).setParameter("enddate", endDate.plusDays(1).atStartOfDay()).getResultStream().collect(Collectors.groupingBy(f->f.getAuthorizedIssuer().getIssuerName(), Collectors.counting()));
    }

    public Map<String, Long> ticketsValidated (LocalDate startDate, LocalDate endDate){
        return entityManager.createQuery("SELECT t FROM Ticket t WHERE validatedAt>=:startdate AND validatedAt<=:enddate", Ticket.class).setParameter("startdate", startDate.atStartOfDay()).setParameter("enddate", endDate.plusDays(1).atStartOfDay()).getResultStream().collect(Collectors.groupingBy(f->{if (f instanceof Ticket) return "Ticket validati"; return "Altro: ";}, Collectors.counting()));
    }

    public Map<String, Long> ticketsValidatedGroupedByVehicle (LocalDate startDate, LocalDate endDate){
        return entityManager.createQuery("SELECT t FROM Ticket t WHERE validatedAt>=:startdate AND validatedAt<=:enddate", Ticket.class).setParameter("startdate", startDate.atStartOfDay()).setParameter("enddate", endDate.plusDays(1).atStartOfDay()).getResultStream().collect(Collectors.groupingBy(f->f.getVehicle().getLicensePlate(), Collectors.counting()));
    }

    public Map<String, Long> ticketsValidatedOnAVehicle (Vehicle vehicle){
        return entityManager.createQuery("SELECT t FROM Ticket t WHERE validatedAt IS NOT NULL AND vehicle.idVehicle=:idvehicle", Ticket.class).setParameter("idvehicle", vehicle.getIdVehicle()).getResultStream().collect(Collectors.groupingBy(f->f.getVehicle().getLicensePlate(), Collectors.counting()));
    }

    public Map<String, Long> ticketsValidatedOnVehicles (){
        return entityManager.createQuery("SELECT t FROM Ticket t WHERE validatedAt IS NOT NULL", Ticket.class).getResultStream().collect(Collectors.groupingBy(f->f.getVehicle().getLicensePlate(), Collectors.counting()));
    }

    public static Boolean isThereAValidPass (EntityManager entityManager, Long userCardNumber){
        try {
        return entityManager.createQuery("SELECT p FROM Pass p WHERE p.userCard.userCardNumber = :numero AND p.passExpiryDate >= CURRENT_DATE", Pass.class).setParameter("numero", userCardNumber).getSingleResult() != null;}
        catch (NoResultException e){
            return false;
        }
    }
}

package AntoninoPalazzolo.test;

import AntoninoPalazzolo.dao.UserCardDAO;
import AntoninoPalazzolo.dao.UserDAO;
import AntoninoPalazzolo.entities.User;
import AntoninoPalazzolo.entities.UserRole;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDate;

public class testDave {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("bwu1w4");
        EntityManager em = emf.createEntityManager();
        UserDAO userDAO = new UserDAO(em);
        UserCardDAO userCardDAO = new UserCardDAO(em);

        try {
            //Salvataggio utente
            User user1 = new User("Ugo", "Sacco", LocalDate.parse("1998-10-30"), UserRole.USER, "ugosacco@gmail.com");
            userDAO.save(user1);
            System.out.println("Utente " + user1.getUserName() + " " + user1.getUserSurname() + " salvato con successo!");

            // Ricerca Utente tramite email
            User foundUser = userDAO.findByEmail("ugosacco@gmail.com");
            System.out.println("Utente " + foundUser.getUserSurname() + " trovato con email: " + foundUser.getUserEmail());

            // Emissione tessera
            userCardDAO.issueCardToUser(foundUser.getIdUser());
            System.out.println("Tessera emessa all'utente " + foundUser.getUserName());

            // Controllo della validità della tessera
            long cardNumber = userCardDAO.findByUserId(foundUser.getIdUser()).getUserCardNumber();
            boolean isValid = userCardDAO.isCardValid(cardNumber);
            System.out.println("La tessera è valida? " + isValid);

            //Rinnovo tessera
            userCardDAO.renewCard(cardNumber);
            System.out.println("Tessera rinnovata!");
            System.out.println(userCardDAO.findByCardNumber(cardNumber));
        } catch (Exception e) {
            System.out.println("Errore: " + e.getMessage());
        } finally {
            em.close();
            emf.close();
        }

    }
}

package AntoninoPalazzolo.dao;

import AntoninoPalazzolo.entities.User;
import AntoninoPalazzolo.entities.UserCard;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class UserCardDAO {

    private EntityManager em;

    public UserCardDAO(EntityManager em){
        this.em = em;
    }

    public UserCard findById(UUID id) {
        return em.find(UserCard.class, id);
    }

    //Metodo per generare numero tessera
    private long generateCardNumber(){
        long cardNumber;

        // crea un numero randomico a 8 cifre e lo rifà fintanto che il cardNumber esiste già. Quando non lo trova già nel DB, esce.
        do {
            cardNumber = 10000000L + (long) (Math.random() * 90000000L);
        } while (findByCardNumber(cardNumber) != null);
        return cardNumber;
    }

    public UserCard findByCardNumber (long cardNumber){
        TypedQuery<UserCard> query = em.createQuery("SELECT uc FROM UserCard uc WHERE uc.userCardNumber = :cardNumber", UserCard.class);
        query.setParameter("cardNumber", cardNumber);

        try {
            return query.getSingleResult();
        } catch (NoResultException e){
            return null;
        }
    }

    public UserCard findByUserId (UUID userId){
        TypedQuery<UserCard> query = em.createQuery("SELECT uc FROM UserCard uc WHERE uc.user.idUser = :userId", UserCard.class);
        query.setParameter("userId",userId);

        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public void issueCardToUser (UUID userId){
        User user = em.find(User.class, userId);

        //Controlla che l'User esista. Se non viene trovato, lancia exception.
        if (user == null) {
            throw new IllegalArgumentException("Utente non trovato!");
        }

        //Utilizza il metodo creato precedentemente per trovare una tessera dall'ID dell'Utente
        UserCard existingCard = findByUserId(userId);

        //Controlla se esiste una tessera collegata a quell'ID utente. Se la trova, vuol dire che c'è già una tessera collegata e lancia eccezione.
        if(existingCard != null) {
            throw new IllegalArgumentException("L'utente scelto ha già una tessera collegata!");
        }

        //Metodo privato per generare un numero tessera randomico
        long cardNumber = generateCardNumber();
        //crea nuova carta con il numero tessera generato, la data attuale e l'user corrispondente all'ID scelto all'inizio
        UserCard newCard = new UserCard(cardNumber, LocalDateTime.now(), user);

        //Transaction e persist
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.persist(newCard);
        transaction.commit();

    }


}

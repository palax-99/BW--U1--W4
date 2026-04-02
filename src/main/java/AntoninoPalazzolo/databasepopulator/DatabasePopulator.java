package AntoninoPalazzolo.databasepopulator;

import AntoninoPalazzolo.DAO.*;
import AntoninoPalazzolo.entities.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.time.*;
import java.util.List;
import java.util.Random;



public class DatabasePopulator {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("bwu1w4");

    private void save(UserCard userCard){
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(userCard);
        transaction.commit();
    }

    public static void populate(){
        usersGenerator(30);
        userCardsGenerator();
    }

    public static void populate(int numberOfUsers){
        usersGenerator(numberOfUsers);
        userCardsGenerator();
    }

    private static void usersGenerator(int numberOfUsers) {
        EntityManager entityManager = emf.createEntityManager();
        UserDAO userDAO = new UserDAO(entityManager);

       String[] NAME_START = {
               "Ma", "Mar", "Mat", "Mi", "Mic", "Lu", "Lui", "Lo",
               "Gi", "Gio", "Gia", "Giu",
               "Fra", "Fran", "Ste", "Sta",
               "Ale", "Al", "An", "Ant",
               "Da", "Dan", "De", "Do",
               "Pa", "Pie", "Pie", "Pe",
               "Ro", "Ric", "Ra",
               "Sa", "Sim", "Se",
               "Chi", "Cla", "Cle",
               "El", "Eli", "Em",
               "Te", "Tom", "Ti",
               "Vi", "Vit", "Va"
       };

       String[] NAME_CENTER = {
       "ri", "ra", "ro", "re",
               "ca", "co", "ce",
               "na", "no", "ni",
               "ta", "to", "ti",
               "la", "lo", "le",
               "sa", "so", "se",
               "mi", "mo",
               "li", "lo",
               "va", "ve",
               "ni", "ne",
               "di", "do",
               "gi", "ge",
               "fi", "fo",
               "vi", "vo",
               "bia", "gio", "lia",
               "ria", "zio", "nio",
               "leo", "rea"
   };

       String[] NAME_END = {
               "o", "io", "e", "eo",
               "ano", "ino", "aro",
               "etto", "ello", "one",
               "ide", "ale", "a", "ia", "ea",
               "ina", "ella", "etta",
               "ara", "ora",
               "ina", "ita"
       };

       String[] SURNAME_START = {"Ros", "Ross", "Rus", "Ferr", "Fer",
               "Bern", "Ber", "Bert",
               "Vit", "Vitt", "Vi",
               "Col", "Colom", "Co",
               "Mar", "Mari", "Mor",
               "Ric", "Ricc", "Ri",
               "Gre", "Grec",
               "Bru", "Brun",
               "Lomb", "Lom",
               "Bar", "Bart",
               "Cont", "Con",
               "Mont", "Mon",
               "Sant", "San",
               "De", "Del", "Della", "Di",
               "Ser", "Serr",
               "Pal", "Palm",
               "Bell", "Bel",
               "Car", "Carr",
               "Pag", "Pagg", "Val"};
       String[] SURNAME_CENTER = { "an", "en", "in", "on",
               "ar", "er", "or",
               "ell", "ett", "ott",
               "ucc", "icc",
               "agn", "egn", "ign",
               "and", "end",
               "ast", "est",
               "ald", "old",
               "ard", "ord",
               "isc", "esc",
               "ugn", "ugn",
               "am", "em", "im",
               "at", "it", "ot", "ent"};
       String[] SURNAME_END = {"i", "o", "a",
               "ini", "etti", "etti",
               "oni", "oni",
               "ano", "ana",
               "ese", "esi",
               "aro", "ari",
               "one", "oni",
               "ale", "ali",
               "ato", "ati",
               "uto", "uti",
               "ello", "ella",
               "ante", "anti"};

       Random random = new Random();
       for (int i = 0; i<numberOfUsers; i++){


           String userName = NAME_START[random.nextInt(NAME_START.length)] + NAME_CENTER[random.nextInt(NAME_CENTER.length)]+NAME_END[random.nextInt(NAME_END.length)];
           String userSurname = SURNAME_START[random.nextInt(SURNAME_START.length)] + SURNAME_CENTER[random.nextInt(SURNAME_CENTER.length)]+SURNAME_END[random.nextInt(SURNAME_END.length)];
           LocalDate now = LocalDate.now();
           LocalDate userDateOfBirth = LocalDate.ofEpochDay((now.minusYears(80)).toEpochDay()+ random.nextLong(now.minusYears(18).toEpochDay()-now.minusYears(80).toEpochDay()));
           UserRole[] roles = UserRole.values();
           UserRole userRole = roles[random.nextInt(roles.length)];
           String userEmail = (userName+"."+userSurname+"@"+SURNAME_START[random.nextInt(SURNAME_START.length)] + SURNAME_CENTER[random.nextInt(SURNAME_CENTER.length)]+SURNAME_END[random.nextInt(SURNAME_END.length)]+"."+(random.nextBoolean()?"it":"com")).toLowerCase();

           User user = new User(userName, userSurname, userDateOfBirth, userRole, userEmail);

           userDAO.save(user);




       }


   }
    private static void userCardsGenerator(){
        EntityManager entityManager = emf.createEntityManager();
        UserCardDAO userCardDAO = new UserCardDAO(entityManager);
        List<User> usersWithoutCard = entityManager.createQuery("SELECT u FROM User u WHERE NOT EXISTS (SELECT uc FROM UserCard uc WHERE uc.user = u)", User.class).getResultList();
        Random random = new Random();

        for (int i = 0; i<usersWithoutCard.size(); i++){
            long userCardNumber = userCardDAO.generateCardNumber();
            LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Rome"));
            LocalDateTime cardIssueDate = LocalDateTime.ofInstant((Instant.ofEpochSecond(now.minusYears(2).atZone(ZoneId.of("Europe/Rome")).toEpochSecond() + random.nextLong(now.atZone(ZoneId.of("Europe/Rome")).toEpochSecond() - now.minusYears(2).atZone(ZoneId.of("Europe/Rome")).toEpochSecond()))),ZoneId.of("Europe/Rome"));
            User user = usersWithoutCard.get(i);
            UserCard userCard = new UserCard(userCardNumber, cardIssueDate, user);

            DatabasePopulator populator = new DatabasePopulator();
            populator.save(userCard);

        }
    }
}

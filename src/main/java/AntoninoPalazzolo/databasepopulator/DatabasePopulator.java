package AntoninoPalazzolo.databasepopulator;

import AntoninoPalazzolo.DAO.*;
import AntoninoPalazzolo.entities.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.time.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static AntoninoPalazzolo.entities.Ticket.ticketValidation;


public class DatabasePopulator {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("bwu1w4");

    private void save(Object obj){
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(obj);
        transaction.commit();
    }

    public static void populate(){
        usersGenerator(30);
        userCardsGenerator();
        authorizedIssuersGenerator(10,5);
        vehiclesGenerator(40);
        vehicleStatusLogGenerator(10);
        ticketsAndPassesGenerator(100, 20);
        routesGenerator(20);
    }

    public static void populate(int numberOfUsers, int numberOfVendingMachines, int numberOfResellers, int numberOfVehicles, int maxNumberOfLogPerVehicle,
                                int numberOfTickets, int numberOfPasses){
        usersGenerator(numberOfUsers);
        userCardsGenerator();
        authorizedIssuersGenerator(numberOfVendingMachines, numberOfResellers);
        vehiclesGenerator(numberOfVehicles);
        vehicleStatusLogGenerator(maxNumberOfLogPerVehicle);
        ticketsAndPassesGenerator(numberOfTickets, numberOfPasses);
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

    private static void authorizedIssuersGenerator(int numberOfVendingMachines, int numberOfResellers){
        EntityManager entityManager = emf.createEntityManager();
        VendingMachineDAO vendingMachineDAO = new VendingMachineDAO(emf);
        AuthorizedIssuerDAO authorizedIssuerDAO = new AuthorizedIssuerDAO(emf);

        String[] SPEED = {
                "Fast", "Quick", "Speed", "Rapid", "Express",
                "Flash", "Sprint", "Turbo", "Veloce", "Subito"
        };
        String[] SERVICE = {
                "Point", "Box", "Hub", "Stop", "Station", "Desk", "Self", "Go", "Express", "Kiosk"
        };
        String[] TRAVEL = {"Ticket", "Ride", "Entrance", "Travel", "Service"};
        String[] SHOP = {"Shop", "Ticket", "Mobilità", "Biglietti", "Servizi", "Vendita", "Rivendita",
                "Trasporti", "Point", "Tabacchi" };

        String[] STREET_NAME = {"Roma", "Milano", "Torino", "Napoli", "Verona",
                "Firenze", "Bologna", "Genova", "Venezia", "Palermo",
                "Dante", "Verdi", "Manzoni", "Leopardi", "Carducci",
                "Pascoli", "Foscolo", "Ariosto", "Petrarca", "Boccaccio",
                "Garibaldi", "Mazzini", "Cavour", "Marconi", "Battisti",
                "Colombo", "Kennedy", "Matteotti", "De Gasperi", "Rossini",
                "Europa", "Liberta", "Repubblica", "Unita", "Indipendenza",
                "Pace", "Speranza", "Progresso", "Lavoro", "Giustizia",
                "Stazione", "Mercato", "Castello", "Duomo", "Municipio",
                "Porto", "Borgo", "Centro", "Giardino", "Parco",
                "Lago", "Monte", "Colle", "Fiume", "Ponte",
                "Sole", "Luna", "Stella", "Aurora", "Tramonto",
                "Primavera", "Estate", "Autunno", "Inverno", "Alba",
                "Rose", "Tulipani", "Violette", "Girasoli", "Orchidee",
                "Tigli", "Pini", "Platani", "Olmi", "Cipressi",
                "Cedri", "Magnolie", "Acacie", "Betulle", "Allori",
                "Artigiani", "Commercianti", "Viaggiatori", "Naviganti", "Poeti",
                "Pittori", "Musicisti", "Scultori", "Medici", "Maestri",
                "della Pace", "della Liberta", "della Repubblica", "della Stazione", "del Mercato",
                "del Sole", "della Luna", "delle Stelle", "dell'Alba", "del Tramonto",
                "del Parco", "del Lago", "del Monte", "del Ponte", "del Porto",
                "dei Tigli", "dei Pini", "dei Platani", "delle Rose", "delle Violette",
                "dei Gelsi", "dei Cedri", "delle Acacie", "dei Giardini", "dei Colli",
                "San Marco", "San Paolo", "San Pietro", "San Luca", "Santa Lucia",
                "Santa Chiara", "Santa Maria", "San Giovanni", "San Michele", "San Francesco"};

        String[] STREET_TYPE = {"Via", "Viale", "Piazza", "Corso", "Largo", "Piazzale"};

        String CITY = "Roma";



        Random random = new Random();

        for (int i=0; i<numberOfVendingMachines; i++){
            String issuerName= SPEED[random.nextInt(SPEED.length)]+SERVICE[random.nextInt(SERVICE.length)]+" "+TRAVEL[random.nextInt(TRAVEL.length)];
            String issuerAddress =  STREET_TYPE[random.nextInt(STREET_TYPE.length)]+" "+STREET_NAME[random.nextInt(STREET_NAME.length)]+", "+random.nextInt(200)+" - "+CITY;
            String serialNumber = String.valueOf(random.nextLong());
            Boolean vendingMachineAvailability= random.nextBoolean();
            VendingMachine vendingMachine = new VendingMachine(issuerName, issuerAddress, serialNumber, vendingMachineAvailability);

            try {
                vendingMachineDAO.save(vendingMachine);
            } catch (RuntimeException e) {
                System.out.println("Distributore n. "+i+" non salvato. "+e);
            }


        }
        for (int i=0; i<numberOfResellers; i++){
            String issuerName= SPEED[random.nextInt(SPEED.length)]+SERVICE[random.nextInt(SERVICE.length)]+" "+SHOP[random.nextInt(SHOP.length)];
            String issuerAddress =  STREET_TYPE[random.nextInt(STREET_TYPE.length)]+" "+STREET_NAME[random.nextInt(STREET_NAME.length)]+", "+random.nextInt(200)+" - Roma";
            String vatNumber = "IT"+(random.nextLong((99999999999L-10000000000L)+1)+10000000000L);
            Reseller reseller= new Reseller(issuerName, issuerAddress, vatNumber);
            try {
            authorizedIssuerDAO.save(reseller);
            } catch (RuntimeException e) {
                System.out.println("Distributore n. "+i+" non salvato. "+e);
            }
        }
    }
    private static void vehiclesGenerator(int numberOfVehicles){
        EntityManager entityManager = emf.createEntityManager();
        VehicleDAO vehicleDAO = new VehicleDAO(entityManager);
        Random random = new Random();
        char[] lettere = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
                'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

        for (int i = 0; i<numberOfVehicles; i++){
                String licensePlate = ""+lettere[random.nextInt(lettere.length)]+lettere[random.nextInt(lettere.length)]+ random.nextInt(10)+ random.nextInt(10)+ random.nextInt(10)+lettere[random.nextInt(lettere.length)]+lettere[random.nextInt(lettere.length)];
                VehicleType vehicleType= VehicleType.values()[random.nextInt(VehicleType.values().length)];
                Vehicle vehicle=new Vehicle(licensePlate, vehicleType);

                try {

                    vehicleDAO.saveVehicle(vehicle);

                } catch (RuntimeException e) {
                    System.out.println("Veicolo "+i+" non salvato");
                }

        }
    }

    private static void vehicleStatusLogGenerator(int maxNumberOfLogPerVehicle){
        EntityManager entityManager = emf.createEntityManager();
        List<Vehicle> vehicleWithoutStatus = entityManager.createQuery("SELECT v FROM Vehicle v WHERE NOT EXISTS (SELECT vs FROM VehicleStatusLog vs WHERE vs.vehicle = v)", Vehicle.class).getResultList();
        VehicleDAO vehicleDAO = new VehicleDAO(entityManager);
        DatabasePopulator populator = new DatabasePopulator();
        Random random = new Random();
        ZoneId rome = ZoneId.of("Europe/Rome");
        LocalDateTime end = LocalDateTime.now(rome);
        LocalDateTime start = end.minusYears(2);
        long startMillis = start.atZone(rome).toInstant().toEpochMilli();
        long endMillis = end.atZone(rome).toInstant().toEpochMilli();



        for (int i = 0; i < vehicleWithoutStatus.size(); i++) {
            int numberOfLogs = random.nextInt(maxNumberOfLogPerVehicle)+1;
            boolean vehicleInService = random.nextBoolean();
            List<LocalDateTime> randomTime=new ArrayList<>();


            for (int j = 0; j < numberOfLogs; j++) {
                long randomMillis = random.longs(1, startMillis, endMillis).findFirst().getAsLong();
                randomTime.add(LocalDateTime.ofInstant(Instant.ofEpochMilli(randomMillis), rome));
            }

            randomTime.sort(Comparator.naturalOrder());


            for (int j = 0; j < numberOfLogs; j++) {
                VehicleStatusLog vehicleStatusLog = new VehicleStatusLog(vehicleWithoutStatus.get(i),randomTime.get(j),vehicleInService);

                try {
                    populator.save(vehicleStatusLog);
                } catch (RuntimeException e) {
                    System.out.println("Registro di manutenzione di "+vehicleWithoutStatus.get(i).getLicensePlate()+" non salvato");
                }

                vehicleInService= !vehicleInService;
            }

        }
    }
    private static void ticketsAndPassesGenerator(int numberOfTickets, int numberOfPasses){
        EntityManager entityManager = emf.createEntityManager();
        FareProductDAO fareProductDAO = new FareProductDAO(entityManager);
                List<AuthorizedIssuer> authorizedIssuersAvailable = entityManager.createQuery("SELECT a FROM AuthorizedIssuer a WHERE a NOT IN (SELECT vm FROM VendingMachine vm WHERE vm.vendingMachineAvailability=FALSE)", AuthorizedIssuer.class).getResultList();
        if (authorizedIssuersAvailable.isEmpty()) {
            throw new IllegalStateException("Nessun AuthorizedIssuer disponibile");
        }
        int[] minutes = {30, 60, 90, 300, 1440};
        Random random = new Random();
        ZoneId rome = ZoneId.of("Europe/Rome");
        LocalDateTime end = LocalDateTime.now(rome);
        LocalDateTime start = end.minusYears(2);
        long startMillis = start.atZone(rome).toInstant().toEpochMilli();
        long endMillis = end.atZone(rome).toInstant().toEpochMilli();

        for (int i = 0; i < numberOfTickets; i++) {

            long randomMillis = random.longs(1, startMillis, endMillis).findFirst().getAsLong();
            LocalDateTime issueDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(randomMillis), rome);
            AuthorizedIssuer authorizedIssuer = authorizedIssuersAvailable.get(random.nextInt(authorizedIssuersAvailable.size()));
            int validityMinutes = minutes[random.nextInt(minutes.length)];

            Ticket ticket = new Ticket(issueDate, authorizedIssuer, validityMinutes);

            try {
                fareProductDAO.save(ticket);
            } catch (RuntimeException e) {
                System.out.println("Ticket n. "+i+" non salvato");
                continue;
            }

            if (random.nextBoolean()){
                long randomMillisForValidation = random.longs(1, randomMillis, endMillis).findFirst().getAsLong();
                LocalDateTime validatedAt = LocalDateTime.ofInstant(Instant.ofEpochMilli(randomMillisForValidation), rome);
                List<Vehicle> vehicles = entityManager.createQuery("SELECT v FROM Vehicle v WHERE EXISTS (SELECT 1 FROM VehicleStatusLog vs WHERE vs.vehicle=v AND vs.vehicleAvailabilityUpdatedOn=(SELECT MAX(vs2.vehicleAvailabilityUpdatedOn) FROM VehicleStatusLog vs2 WHERE vs2.vehicle=v AND vs2.vehicleAvailabilityUpdatedOn <= :validatedAt) AND vs.vehicleInService = TRUE)", Vehicle.class).setParameter("validatedAt", validatedAt).getResultList();
                if (vehicles.isEmpty()) {
                    System.out.println("Nessun veicolo disponibile per l'obliterazione");
                    continue;
                }
                Vehicle vehicle = vehicles.get(random.nextInt(vehicles.size()));
                fareProductDAO.update(ticketValidation(ticket, validatedAt,vehicle));


            }



        }

        for (int i = 0; i < numberOfPasses; i++) {
            long randomMillis = random.longs(1, startMillis, endMillis).findFirst().getAsLong();
            LocalDateTime issueDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(randomMillis), rome);
            AuthorizedIssuer authorizedIssuer = authorizedIssuersAvailable.get(random.nextInt(authorizedIssuersAvailable.size()));
            PassType passType = PassType.values()[random.nextInt(PassType.values().length)];
            List<UserCard> activeUserCards = entityManager.createQuery("SELECT u FROM UserCard u WHERE u.cardActivationDate<=:issueDate AND u.cardExpiryDate>=:issueDate", UserCard.class).setParameter("issueDate", issueDate).getResultList();
            if (activeUserCards.isEmpty()) {
                System.out.println("Nessuna UserCard attiva per la data " + issueDate);
                continue;
            }
            UserCard userCard = activeUserCards.get(random.nextInt(activeUserCards.size()));

            Pass pass = new Pass(issueDate, authorizedIssuer, passType, userCard);

            try {
                fareProductDAO.save(pass);
            } catch (RuntimeException e) {
                System.out.println("L'abbonamento n. "+i+" non è stato salvato");
            }

        }

    }
    private static void routesGenerator(int numberOfRoutes){
        EntityManager entityManager = emf.createEntityManager();
        RouteDAO routeDAO = new RouteDAO(entityManager);

        Random random = new Random();
        String[] STREET_NAME = {"Roma", "Milano", "Torino", "Napoli", "Verona",
                "Firenze", "Bologna", "Genova", "Venezia", "Palermo",
                "Dante", "Verdi", "Manzoni", "Leopardi", "Carducci",
                "Pascoli", "Foscolo", "Ariosto", "Petrarca", "Boccaccio",
                "Garibaldi", "Mazzini", "Cavour", "Marconi", "Battisti",
                "Colombo", "Kennedy", "Matteotti", "De Gasperi", "Rossini",
                "Europa", "Liberta", "Repubblica", "Unita", "Indipendenza",
                "Pace", "Speranza", "Progresso", "Lavoro", "Giustizia",
                "Stazione", "Mercato", "Castello", "Duomo", "Municipio",
                "Porto", "Borgo", "Centro", "Giardino", "Parco",
                "Lago", "Monte", "Colle", "Fiume", "Ponte",
                "Sole", "Luna", "Stella", "Aurora", "Tramonto",
                "Primavera", "Estate", "Autunno", "Inverno", "Alba",
                "Rose", "Tulipani", "Violette", "Girasoli", "Orchidee",
                "Tigli", "Pini", "Platani", "Olmi", "Cipressi",
                "Cedri", "Magnolie", "Acacie", "Betulle", "Allori",
                "Artigiani", "Commercianti", "Viaggiatori", "Naviganti", "Poeti",
                "Pittori", "Musicisti", "Scultori", "Medici", "Maestri",
                "della Pace", "della Liberta", "della Repubblica", "della Stazione", "del Mercato",
                "del Sole", "della Luna", "delle Stelle", "dell'Alba", "del Tramonto",
                "del Parco", "del Lago", "del Monte", "del Ponte", "del Porto",
                "dei Tigli", "dei Pini", "dei Platani", "delle Rose", "delle Violette",
                "dei Gelsi", "dei Cedri", "delle Acacie", "dei Giardini", "dei Colli",
                "San Marco", "San Paolo", "San Pietro", "San Luca", "Santa Lucia",
                "Santa Chiara", "Santa Maria", "San Giovanni", "San Michele", "San Francesco"};

        String[] STREET_TYPE = {"Via", "Viale", "Piazza", "Corso", "Largo", "Piazzale"};

        String CITY = "Roma";

        for (int i = 0; i < numberOfRoutes; i++) {
            String departure =  STREET_TYPE[random.nextInt(STREET_TYPE.length)]+" "+STREET_NAME[random.nextInt(STREET_NAME.length)]+", "+random.nextInt(200)+" - "+CITY;
            String terminus =  STREET_TYPE[random.nextInt(STREET_TYPE.length)]+" "+STREET_NAME[random.nextInt(STREET_NAME.length)]+", "+random.nextInt(200)+" - "+CITY;
            int travelTimeMinutes = random.nextInt(10, 200);
            LocalTime scheduledDepartureTime = LocalTime.of(random.nextInt(24), random.nextInt(60), 0);
            Route route = new Route(departure, terminus, travelTimeMinutes, scheduledDepartureTime);
            try {
                routeDAO.saveRoute(route);
            } catch (RuntimeException e){
                System.out.println("Errore nel salvataggio della tratta");
            }
        }



    }
}

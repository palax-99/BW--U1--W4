package AntoninoPalazzolo.entities;

import AntoninoPalazzolo.dao.*;
import AntoninoPalazzolo.dao.FareProductDAO;
import AntoninoPalazzolo.exception.NotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import java.util.Scanner;

public class Scanne {
    public static void main(String[] args) {

        // Creo la connessione al database tramite il persistence unit definito nel persistence.xml
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("bwu1w4");
        EntityManager em = emf.createEntityManager();

        // Istanzio tutti i DAO che mi servono, passando l'EntityManager a ciascuno
        UserDAO userDAO = new UserDAO(em);
        UserCardDAO userCardDAO = new UserCardDAO(em);
        VehicleDAO vehicleDAO = new VehicleDAO(em);
        RouteDAO routeDAO = new RouteDAO(em);
        RunDAO runDAO = new RunDAO(em);
        FareProductDAO fareProductDAO = new FareProductDAO(em);

        // Creo lo Scanner per leggere l'input dell'utente da tastiera
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== SISTEMA TRASPORTO PUBBLICO ===");

        // Chiedo all'utente di inserire la propria email per autenticarsi
        System.out.print("Inserisci la tua email: ");
        String email = scanner.nextLine();

        // Cerco l'utente nel DB tramite email
        User user = userDAO.findByEmail(email);

        // Se l'utente non esiste nel DB, chiedo se vuole crearne uno nuovo
        if (user == null) {
            System.out.println("Utente non trovato!");
            System.out.println("Vuoi creare un nuovo profilo Utente? y/n");
            boolean choice = readYOrN(scanner);
            if (choice){
                System.out.println("Inserire nome...");
                String userName = scanner.nextLine();
                System.out.println("Inserire cognome...");
                String userSurname = scanner.nextLine();
                System.out.println("Inserire data di nascita (formato YYYY-MM-DD)...");
                LocalDate dateOfBirth = readDate(scanner);
                user = new User(userName,userSurname,dateOfBirth,UserRole.USER, email);

                System.out.println("Creerai il seguente utente");
                System.out.println(user);
                System.out.println("Vuoi confermare? y/n");
                boolean confirm = readYOrN(scanner);

                if (confirm){
                    userDAO.save(user);
                    System.out.println("Utente creato correttamente!");

                    userCardDAO.issueCardToUser(user.getIdUser());
                    UserCard card = userCardDAO.findByUserId(user.getIdUser());
                    System.out.println("Tessera emessa correttamente con numero: " + card.getUserCardNumber());
                } else {
                    em.close();
                    emf.close();
                    scanner.close();
                    return;
                }
            } else {
                em.close();
                emf.close();
                scanner.close();
                return;
            }

        }

        System.out.println("Benvenuto, " + user.getUserName() + "!");

        // In base al ruolo dell'utente mostro il menu corretto —
        // se è ADMIN accede al menu amministratore, altrimenti al menu utente semplice
        if (user.getUserRole() == UserRole.ADMIN) {
            menuAdmin(scanner, vehicleDAO, routeDAO, runDAO, fareProductDAO, userCardDAO);
        } else {
            menuUser(scanner, userCardDAO, fareProductDAO, em, user );
        }

        // Chiudo scanner, EntityManager e EntityManagerFactory per liberare le risorse
        scanner.close();
        em.close();
        emf.close();
    }

    // ==================== MENU UTENTE ====================
    private static void menuUser(Scanner scanner, UserCardDAO userCardDAO,
                                 FareProductDAO fareProductDAO, EntityManager em, User user) {

        // Il while mantiene il menu attivo finché l'utente non sceglie di uscire
        boolean running = true;
        while (running) {
            System.out.println("\n=== MENU UTENTE ===");
            System.out.println("1. Verifica validità abbonamento");
            System.out.println("2. Acquista biglietto");
            System.out.println("3. Acquista abbonamento");
            System.out.println("5. Controllo Validità Tessera");
            System.out.println("6. Rinnovo Tessera");
            System.out.println("0. Esci");
            System.out.print("Scelta: ");

            String scelta = scanner.nextLine();

            // Lo switch gestisce ogni opzione del menu —
            // ogni case corrisponde a una funzionalità specifica
            switch (scelta) {
                case "1" -> {
                    // Chiedo il numero tessera e verifico tramite il metodo statico
                    // isThereAValidPass se esiste un abbonamento valido associato
                    System.out.print("Inserisci il numero tessera: ");
                    long cardNumber = Long.parseLong(scanner.nextLine());
                    boolean valido = FareProductDAO.isThereAValidPass(em, cardNumber);
                    if (valido) {
                        System.out.println("L'abbonamento è valido!");
                    } else {
                        System.out.println("Nessun abbonamento valido trovato per questa tessera.");
                    }
                }
                case "2" -> {
                    // Funzionalità da implementare con i colleghi
                    System.out.println("Funzionalità acquisto biglietto — da implementare!");
                }
                case "3" -> {
                    // Funzionalità da implementare con i colleghi
                    System.out.println("Funzionalità acquisto abbonamento — da implementare!");
                }
                case "5" -> {
                    try {
                        UserCard userCard = userCardDAO.findByUserId(user.getIdUser());

                        if (userCard == null){
                            System.out.println("Nessuna tessera trovata per l'utente loggato!");
                        }else {
                            long cardNumber = userCard.getUserCardNumber();
                            boolean isValid = userCardDAO.isCardValid(cardNumber);
                            LocalDateTime expiryDate = userCard.getCardExpiryDate();

                            if (isValid){
                                System.out.println("La tessera è valida, con scadenza in data(yy-mm-dd): " + expiryDate.toLocalDate());
                            }else {
                                System.out.println("La tessera non è valida. Scadenza in data(yy-mm-dd): " + expiryDate.toLocalDate());
                            }

                        }
                    } catch (IllegalArgumentException e){
                        System.out.println("Errore: " + e.getMessage());
                    }

                }
                case "6" -> {
                    try {
                        UserCard userCard = userCardDAO.findByUserId(user.getIdUser());
                        if (userCard == null) {
                            System.out.println("Nessuna tessera trovata per l'utente loggato!");
                        } else {
                            userCardDAO.renewCard(userCard.getUserCardNumber());
                            System.out.println("Tessera rinnovata correttamente!");
                            System.out.println(userCardDAO.findByCardNumber(userCard.getUserCardNumber()));
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println("Errore: " + e.getMessage());
                    }
                }
                case "0" -> {
                    // Imposto running a false per uscire dal while e terminare il menu
                    System.out.println("Arrivederci!");
                    running = false;
                }
                // Se l'utente inserisce un valore non previsto, lo notifico e ripropongo il menu
                default -> System.out.println("Scelta non valida, riprova.");
            }
        }
    }

    // ==================== MENU ADMIN ====================
    private static void menuAdmin(Scanner scanner, VehicleDAO vehicleDAO,
                                  RouteDAO routeDAO, RunDAO runDAO,
                                  FareProductDAO fareProductDAO, UserCardDAO userCardDAO) {

        // Stesso meccanismo del menu utente — while attivo finché non si esce
        boolean running = true;
        while (running) {
            System.out.println("\n=== MENU AMMINISTRATORE ===");
            System.out.println("1. Aggiungi veicolo");
            System.out.println("2. Aggiorna stato veicolo");
            System.out.println("3. Visualizza storico stati veicolo");
            System.out.println("4. Crea tratta");
            System.out.println("5. Crea corsa");
            System.out.println("6. Statistiche biglietti emessi per periodo");
            System.out.println("7. Statistiche biglietti validati per veicolo");
            System.out.println("8. Tempo medio percorrenza tratta");
            System.out.println("0. Esci");
            System.out.print("Scelta: ");

            String scelta = scanner.nextLine();

            switch (scelta) {
                case "1" -> {
                    // Chiedo targa e tipo veicolo, converto il tipo in enum tramite valueOf
                    // e creo il veicolo — la capacity viene calcolata automaticamente dal costruttore
                    System.out.print("Targa: ");
                    String targa = scanner.nextLine();
                    System.out.print("Tipo (TRAM/BUS): ");
                    VehicleType tipo = VehicleType.valueOf(scanner.nextLine().toUpperCase());
                    Vehicle newVehicle = new Vehicle(targa, tipo);
                    vehicleDAO.saveVehicle(newVehicle);
                }
                case "2" -> {
                    // Chiedo l'ID del veicolo e i nuovi valori di stato,
                    // recupero il veicolo dal DB e aggiorno il suo stato tramite updateVehicleStatus
                    // che crea un nuovo record nel log mantenendo lo storico completo
                    System.out.print("Inserisci ID veicolo: ");
                    String id = scanner.nextLine();
                    System.out.print("In servizio? (true/false): ");
                    boolean inService = Boolean.parseBoolean(scanner.nextLine());
                    System.out.print("Ritirato definitivamente? (true/false): ");
                    boolean permanently = Boolean.parseBoolean(scanner.nextLine());
                    try {
                        Vehicle v = vehicleDAO.findById(id);
                        vehicleDAO.updateVehicleStatus(v, inService, permanently);
                    } catch (NotFoundException e) {
                        System.out.println("Errore: " + e.getMessage());
                    }
                }
                case "3" -> {
                    // Recupero il veicolo tramite ID e stampo tutti i record del log
                    // in ordine cronologico — mostro lo storico completo dei cambi di stato
                    System.out.print("Inserisci ID veicolo: ");
                    String id = scanner.nextLine();
                    try {
                        Vehicle v = vehicleDAO.findById(id);
                        System.out.println("Storico stati di " + v.getLicensePlate() + ":");
                        vehicleDAO.getVehicleStatusHistory(v).forEach(System.out::println);
                    } catch (NotFoundException e) {
                        System.out.println("Errore: " + e.getMessage());
                    }
                }
                case "4" -> {
                    // Chiedo tutti i dati della tratta — partenza, capolinea, tempo previsto
                    // e orari. LocalTime.parse converte la stringa "HH:mm" in LocalTime automaticamente
                    System.out.print("Partenza: ");
                    String partenza = scanner.nextLine();
                    System.out.print("Capolinea: ");
                    String capolinea = scanner.nextLine();
                    System.out.print("Tempo previsto (minuti): ");
                    int minuti = Integer.parseInt(scanner.nextLine());
                    System.out.print("Orario partenza (HH:mm): ");
                    LocalTime orarioPartenza = LocalTime.parse(scanner.nextLine());
                    System.out.print("Orario arrivo (HH:mm): ");
                    LocalTime orarioArrivo = LocalTime.parse(scanner.nextLine());
                    Route newRoute = new Route(partenza, capolinea, minuti, orarioPartenza, orarioArrivo);
                    routeDAO.saveRoute(newRoute);
                }
                case "5" -> {
                    // Recupero veicolo e tratta tramite ID, poi creo la corsa con gli orari effettivi.
                    // Il tempo effettivo di percorrenza viene calcolato automaticamente dal costruttore di Run
                    System.out.print("ID veicolo: ");
                    String idVehicle = scanner.nextLine();
                    System.out.print("ID tratta: ");
                    String idRoute = scanner.nextLine();
                    System.out.print("Orario partenza effettivo (HH:mm): ");
                    LocalTime partenza = LocalTime.parse(scanner.nextLine());
                    System.out.print("Orario arrivo effettivo (HH:mm): ");
                    LocalTime arrivo = LocalTime.parse(scanner.nextLine());
                    try {
                        Vehicle v = vehicleDAO.findById(idVehicle);
                        Route r = routeDAO.findById(idRoute);
                        Run newRun = new Run(v, r, partenza, arrivo);
                        runDAO.saveRun(newRun);
                    } catch (NotFoundException e) {
                        System.out.println("Errore: " + e.getMessage());
                    }
                }
                case "6" -> {
                    // Chiedo il periodo di riferimento e mostro il numero di biglietti
                    // e abbonamenti emessi, raggruppati per tipo tramite ticketsAndPassesIssued
                    System.out.print("Data inizio (yyyy-MM-dd): ");
                    LocalDate inizio = LocalDate.parse(scanner.nextLine());
                    System.out.print("Data fine (yyyy-MM-dd): ");
                    LocalDate fine = LocalDate.parse(scanner.nextLine());
                    Map<String, Long> stats = fareProductDAO.ticketsAndPassesIssued(inizio, fine);
                    System.out.println("Titoli emessi nel periodo:");
                    stats.forEach((tipo, count) -> System.out.println("  " + tipo + ": " + count));
                }
                case "7" -> {
                    // Mostro il numero di biglietti validati raggruppati per targa del veicolo —
                    // utile per capire quali mezzi hanno più traffico
                    System.out.println("Biglietti validati per veicolo:");
                    fareProductDAO.ticketsValidatedOnVehicles()
                            .forEach((targa, count) -> System.out.println("  " + targa + ": " + count));
                }
                case "8" -> {
                    // Recupero la tratta tramite ID e calcolo il tempo medio effettivo
                    // di percorrenza tramite getAverageTravelTime che usa AVG() direttamente nel DB
                    System.out.print("ID tratta: ");
                    String idRoute = scanner.nextLine();
                    try {
                        Route r = routeDAO.findById(idRoute);
                        double media = runDAO.getAverageTravelTime(r);
                        System.out.println("Tempo medio percorrenza tratta " +
                                r.getDeparture() + " -> " + r.getTerminus() +
                                ": " + media + " minuti");
                    } catch (NotFoundException e) {
                        System.out.println("Errore: " + e.getMessage());
                    }
                }
                case "0" -> {
                    // Imposto running a false per uscire dal while e terminare il menu
                    System.out.println("Arrivederci!");
                    running = false;
                }
                default -> System.out.println("Scelta non valida, riprova.");
            }
        }
    }

    private static LocalDate readDate(Scanner scanner){
        while (true){
            try {
                LocalDate date = LocalDate.parse(scanner.nextLine());
                if (date.isAfter(LocalDate.now())){
                    System.out.println("La data non può essere nel futuro! Svegliati!");
                    continue;
                }
                if (date.isBefore(LocalDate.now().minusYears(120))) {
                    System.out.println("Data non valida! Non sei un vampiro! Riprova.");
                    continue;
                }
                return date;
            } catch (Exception e) {
                System.out.println("Formato non valido (YYYY-MM-DD), riprova!");
            }
        }
    }

    private static boolean readYOrN(Scanner scanner){
        while (true){
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("y")) return true;
            if (input.equals("n")) return false;

            System.out.println("Per favore, inserire soltanto 'y' oppure 'n'! Riprova...");
        }
    }
}

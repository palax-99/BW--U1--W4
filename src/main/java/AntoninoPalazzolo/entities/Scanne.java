package AntoninoPalazzolo.entities;

import AntoninoPalazzolo.DAO.*;
import AntoninoPalazzolo.DAO.FareProductDAO;
import AntoninoPalazzolo.exception.NotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import static AntoninoPalazzolo.DAO.FareProductDAO.isThereAValidPass;
import static AntoninoPalazzolo.entities.Ticket.ticketValidation;

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

        // Se l'utente non esiste nel DB, mostro un errore e termino il programma
        if (user == null) {
            System.out.println("Utente non trovato!");
            em.close();
            emf.close();
            return;
        }

        System.out.println("Benvenuto, " + user.getUserName() + "!");

        // In base al ruolo dell'utente mostro il menu corretto —
        // se è ADMIN accede al menu amministratore, altrimenti al menu utente semplice
        if (user.getUserRole() == UserRole.ADMIN) {
            menuAdmin(scanner, vehicleDAO, routeDAO, runDAO, fareProductDAO, userCardDAO);
        } else {
            menuUser(scanner, userCardDAO, fareProductDAO, em);
        }

        // Chiudo scanner, EntityManager e EntityManagerFactory per liberare le risorse
        scanner.close();
        em.close();
        emf.close();
    }

    // ==================== MENU UTENTE ====================
    private static void menuUser(Scanner scanner, UserCardDAO userCardDAO,
                                 FareProductDAO fareProductDAO, EntityManager em) {

        // Il while mantiene il menu attivo finché l'utente non sceglie di uscire
        boolean running = true;
        while (running) {
            System.out.println("\n=== MENU UTENTE ===");
            System.out.println("1. Verifica validità abbonamento");
            System.out.println("2. Acquista biglietto");
            System.out.println("3. Valida il biglietto");
            System.out.println("4. Acquista abbonamento");
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
                    boolean valido = isThereAValidPass(em, cardNumber);
                    if (valido) {
                        System.out.println("L'abbonamento è valido!");
                    } else {
                        System.out.println("Nessun abbonamento valido trovato per questa tessera.");
                    }
                }
                case "2" -> {
                    // Funzionalità acquisto biglietto start
                    System.out.println("Funzionalità acquisto biglietto");
                    ticket: while (true)
                    {
                        System.out.println("Premi 1 per biglietto da 30 minuti\nPremi 2 per biglietto da 60 minuti \nPremi 3 per biglietto da 90 minuti \nPremi 4 per biglietto da 5 ore \nPremi 5 per biglietto giornaliero");
                        int choice;
                        int validityMinutes;
                        try {
                        choice = Integer.parseInt(scanner.nextLine());}
                        catch (IllegalArgumentException e){
                            System.out.println("Inserimento non corretto");
                            continue;
                        }

                        switch (choice){
                            case 1: {
                                validityMinutes = 30;
                                System.out.println("Hai scelto il biglietto da 30 minuti");
                                break;
                            }
                            case 2: {
                                validityMinutes = 60;
                                System.out.println("Hai scelto il biglietto da 60 minuti");
                                break;
                            }
                            case 3: {
                                validityMinutes = 90;
                                System.out.println("Hai scelto il biglietto da 90 minuti");
                                break;
                            }
                            case 4: {
                                validityMinutes = 300;
                                System.out.println("Hai scelto il biglietto da 5 ore");
                                break;
                            }
                            case 5: {
                                validityMinutes = 1440;
                                System.out.println("Hai scelto il biglietto giornaliero");
                                break;
                            }
                            default:{
                                System.out.println("Inserimento errato, riprova");
                                continue;
                            }
                        }
                        System.out.println("Scegli dove vuoi acquistare il biglietto digitando il numero corrispondente");
                        List<AuthorizedIssuer> authorizedIssuersAvailable = em.createQuery("SELECT a FROM AuthorizedIssuer a WHERE a NOT IN (SELECT vm FROM VendingMachine vm WHERE vm.vendingMachineAvailability=FALSE)", AuthorizedIssuer.class).getResultList();
                        for (int i = 0; i < authorizedIssuersAvailable.size(); i++) {
                            System.out.println(i + ". "+authorizedIssuersAvailable.get(i).getIssuerName()+" per scegliere digita: "+i);
                        }
                        int aiNumber;

                        try {
                            aiNumber = Integer.parseInt(scanner.nextLine());}
                        catch (IllegalArgumentException e){
                            System.out.println("Inserimento non corretto");
                            continue;
                        }

                        AuthorizedIssuer authorizedIssuer = authorizedIssuersAvailable.get(aiNumber);
                        System.out.println("Hai scelto "+authorizedIssuer.getIssuerName());

                        Ticket ticket = new Ticket(LocalDateTime.now(), authorizedIssuer, validityMinutes);

                        try {
                            fareProductDAO.save(ticket);
                        } catch (RuntimeException e) {
                            System.out.println("C'è stato un errore nell'emissione del biglietto, riprova");
                        }

                        System.out.println("Il biglietto richiesto, è stato emesso, ricordati di validarlo - " + ticket.getIdFareProduct());
                        break ticket;

                    }
                    // Funzionalità acquisto biglietto end
                }
                case "3" -> {
                    // Funzionalità validazione biglietto start
                    System.out.println("Funzionalità validazione biglietto");
                    System.out.println("Se procedi il tuo biglietto potrà essere utilizzato sui vari mezzi pubblici per il tempo indicato. La validazione non può essere annullata.");
                    String idFareProduct="";
                   validation: while (!"n".equals(idFareProduct)){
                        System.out.println("Inserisci l'id del biglietto che vuoi validare. Premi n per uscire.");
                        idFareProduct=scanner.nextLine();
                        if ("n".equals(idFareProduct)){
                            System.out.println("Ritorno al menù principale...");
                            break validation;
                        }
                       Ticket ticket;
                       try {
                        ticket = fareProductDAO.ticketGetById(UUID.fromString(idFareProduct));} catch (
                                RuntimeException e) {

                           System.out.println("Biglietto non trovato");
                           continue;

                       }

                       if(ticket.getValidatedAt()!=null) {
                           System.out.println("Biglietto già validato in passato");
                           System.out.println("Valido fino a "+ticket.getValidUntil());
                           continue;
                       }

                        System.out.println("Il biglietto "+ticket.getIdFareProduct()+" sta per essere validato");
                        List<Vehicle> vehicles = em.createQuery("SELECT v FROM Vehicle v WHERE EXISTS (SELECT 1 FROM VehicleStatusLog vs WHERE vs.vehicle=v AND vs.vehicleAvailabilityUpdatedOn=(SELECT MAX(vs2.vehicleAvailabilityUpdatedOn) FROM VehicleStatusLog vs2 WHERE vs2.vehicle=v AND vs2.vehicleAvailabilityUpdatedOn <= CURRENT_TIMESTAMP) AND vs.vehicleInService = TRUE)", Vehicle.class).getResultList();
                        if (vehicles.isEmpty()) {
                            System.out.println("Nessun veicolo disponibile per l'obliterazione");
                            continue;
                        }

                        System.out.println("Scegli il veicolo fra quelli attualmente in servizio");

                        for (int i = 0; i < vehicles.size(); i++) {
                            System.out.println(i + ". Veicolo targa "+vehicles.get(i).getLicensePlate()+" per scegliere digita: "+i);
                        }

                        int vNumber;

                        try {
                            vNumber = Integer.parseInt(scanner.nextLine());}
                        catch (IllegalArgumentException e){
                            System.out.println("Inserimento non corretto");
                            continue;
                        }

                        if (vNumber>=vehicles.size()||vNumber<0){
                            System.out.println("Scelta non valida");
                            continue;

                        }

                        Vehicle vehicle = vehicles.get(vNumber);
                        System.out.println("Hai scelto il veicolo targa: "+vehicle.getLicensePlate());

                        fareProductDAO.save(ticketValidation(ticket, vehicle));

                        System.out.println("Il biglietto è stato validato, hai ancora "+ticket.getValidityMinutes()+" minuti di utilizzo possibile");
                        break validation;

                    }
                    // Funzionalità validazione biglietto end
                }
                case "4" -> {
                    // Funzionalità acquisto abbonamento start
                    System.out.println("Funzionalità acquisto abbonamento");
                    passStart: while(true){

                        System.out.println("Per acquistare un abbonamento inserisci il tuo numero di tessera, inserisci n per uscire");
                        String answer = scanner.nextLine();
                        if ("n".equals(answer)){
                            break;
                        }

                        long userCardNumber=0;

                        try
                        {
                            userCardNumber = Long.parseLong(answer);
                            if (isThereAValidPass(em, userCardNumber)) {
                                System.out.println("Questa tessera ha già un abbonamento attivo in corso di validità");
                                Pass actualPass = em.createQuery("SELECT p FROM Pass p WHERE p.userCard.userCardNumber = :numero AND p.passExpiryDate >= CURRENT_DATE", Pass.class).setParameter("numero", userCardNumber).getSingleResult();
                                System.out.println("L'abbonamento attuale scadrà il "+ actualPass.getPassExpiryDate());
                                break;
                            }

                        } catch (RuntimeException e){
                            System.out.println("Inserimento non valido");
                            continue;
                        }

                        UserCard userCard = userCardDAO.findByCardNumber(userCardNumber);

                        if (userCard==null) {
                            System.out.println("Tessera non trovata");
                            break;
                        }

                        if(userCard.getCardExpiryDate().isBefore(LocalDateTime.now())){
                            System.out.println("La tessera è scaduta, devi rinnovarla");
                            break;
                        }

                        pass: while (true)
                        {
                            System.out.println("Premi 1 per l'abbonamento settimanale\nPremi 2 l'abbonamento mensile \nPremi 3 l'abbonamento semestrale");
                            int choice;
                            PassType passType;
                            try {
                                choice = Integer.parseInt(scanner.nextLine());}
                            catch (IllegalArgumentException e){
                                System.out.println("Inserimento non corretto");
                                continue;
                            }

                            switch (choice){
                                case 1: {
                                    passType = PassType.WEEKLY;
                                    System.out.println("Hai scelto l'abbonamento settimanale");
                                    break;
                                }
                                case 2: {
                                    passType = PassType.MONTHLY;
                                    System.out.println("Hai scelto l'abbonamento mensile");
                                    break;
                                }
                                case 3: {
                                    passType = PassType.HALF_YEARLY;
                                    System.out.println("Hai scelto l'abbonamento semestrale");
                                    break;
                                }
                                default:{
                                    System.out.println("Inserimento errato, riprova");
                                    continue;
                                }
                            }
                            System.out.println("Scegli dove vuoi acquistare l'abbonamento digitando il numero corrispondente");
                            List<AuthorizedIssuer> authorizedIssuersAvailable = em.createQuery("SELECT a FROM AuthorizedIssuer a WHERE a NOT IN (SELECT vm FROM VendingMachine vm WHERE vm.vendingMachineAvailability=FALSE)", AuthorizedIssuer.class).getResultList();
                            for (int i = 0; i < authorizedIssuersAvailable.size(); i++) {
                                System.out.println(i + ". "+authorizedIssuersAvailable.get(i).getIssuerName()+" per scegliere digita: "+i);
                            }
                            int aiNumber;

                            try {
                                aiNumber = Integer.parseInt(scanner.nextLine());}
                            catch (IllegalArgumentException e){
                                System.out.println("Inserimento non corretto");
                                continue;
                            }

                            AuthorizedIssuer authorizedIssuer = authorizedIssuersAvailable.get(aiNumber);
                            System.out.println("Hai scelto "+authorizedIssuer.getIssuerName());

                            Pass pass = new Pass(LocalDateTime.now(), authorizedIssuer, passType, userCard);

                            try {
                                fareProductDAO.save(pass);
                            } catch (RuntimeException e) {
                                System.out.println("C'è stato un errore nell'emissione dell'abbonamento, riprova");
                            }

                            System.out.println("L'abbonamento richiesto, è stato emesso, ricordati che scadrà: "+pass.getPassExpiryDate());
                            break passStart;

                        }


                    }
                    // Funzionalità acquisto abbonamento end
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
}

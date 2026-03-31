package AntoninoPalazzolo;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Application {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("bwu1w4");
    //collegato correttamente al database

    public static void main(String[] args) {
        System.out.println("Hello World!");
    }
}

package AntoninoPalazzolo.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_cards")
public class UserCard {

    @Id
    @GeneratedValue
    @Column(name = "id_user_card")
    private UUID idUserCard;

    @Column(name = "user_card_number", nullable = false, unique = true)
    private long userCardNumber;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "user_surname", nullable = false)
    private String userSurname;

    @Column(name = "user_date_of_birth", nullable = false)
    private LocalDate userDateOfBirth;

    @Column(name = "card_issue_date", nullable = false)
    private LocalDateTime cardIssueDate;
    // La data di emissione viene impostata automaticamente al momento della creazione
    // della tessera — non viene passata dall'utente perché è il sistema a registrarla

    @Column(name = "card_activation_date")
    private LocalDateTime cardActivationDate;
    // La data di attivazione è null alla creazione — viene impostata solo quando
    // l'utente attiva fisicamente la tessera (momento successivo alla creazione)

    @Column(name = "card_expiry_date")
    private LocalDateTime cardExpiryDate;
    // La data di scadenza è null alla creazione — viene calcolata al momento
    // dell'attivazione come cardActivationDate + 1 anno (tessera validità annuale)

    protected UserCard() {
    }

    public UserCard(long userCardNumber, String userName, String userSurname, LocalDate userDateOfBirth) {
        this.userCardNumber = userCardNumber;
        this.userName = userName;
        this.userSurname = userSurname;
        this.userDateOfBirth = userDateOfBirth;
        this.cardIssueDate = LocalDateTime.now();
        // Il costruttore riceve solo i dati anagrafici dell'utente e il numero tessera.
        // Le date di attivazione e scadenza restano null — verranno impostate
        // successivamente tramite un metodo dedicato quando la tessera viene attivata
    }

    public UUID getIdUserCard() {
        return idUserCard;
    }

    public long getUserCardNumber() {
        return userCardNumber;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserSurname() {
        return userSurname;
    }

    public LocalDate getUserDateOfBirth() {
        return userDateOfBirth;
    }

    public LocalDateTime getCardIssueDate() {
        return cardIssueDate;
    }

    public LocalDateTime getCardActivationDate() {
        return cardActivationDate;
    }

    public LocalDateTime getCardExpiryDate() {
        return cardExpiryDate;
    }

    @Override
    public String toString() {
        return "UserCard{" +
                "idUserCard=" + idUserCard +
                ", userCardNumber=" + userCardNumber +
                ", userName='" + userName + '\'' +
                ", userSurname='" + userSurname + '\'' +
                ", userDateOfBirth=" + userDateOfBirth +
                ", cardIssueDate=" + cardIssueDate +
                ", cardActivationDate=" + cardActivationDate +
                ", cardExpiryDate=" + cardExpiryDate +
                '}';
    }
}

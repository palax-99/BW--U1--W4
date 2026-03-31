package AntoninoPalazzolo.entities;

import jakarta.persistence.*;

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
    @OneToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    protected UserCard() {
    }

    public UserCard(long userCardNumber, LocalDateTime cardIssueDate, User user) {
        this.userCardNumber = userCardNumber;
        this.cardIssueDate = cardIssueDate;
        this.cardActivationDate = cardIssueDate;
        this.cardExpiryDate = cardIssueDate.plusYears(1);
        this.user = user;
    }

    public UUID getIdUserCard() {
        return idUserCard;
    }

    public long getUserCardNumber() {
        return userCardNumber;
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
                ", cardIssueDate=" + cardIssueDate +
                ", cardActivationDate=" + cardActivationDate +
                ", cardExpiryDate=" + cardExpiryDate +
                '}';
    }
}

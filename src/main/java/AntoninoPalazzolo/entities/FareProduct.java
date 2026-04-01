package AntoninoPalazzolo.entities;

import AntoninoPalazzolo.exception.AvailabilityException;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "fare_products")
@Inheritance(strategy = InheritanceType.JOINED)
public class FareProduct {
    @Id
    @GeneratedValue
    @Column(name = "id_fare_product")
    private UUID idFareProduct;

    @Column(name = "issue_date")
    private LocalDateTime issueDate;

    @ManyToOne
    @JoinColumn(name = "issued_by")
    private AuthorizedIssuer authorizedIssuer;

    public FareProduct(LocalDateTime issueDate, AuthorizedIssuer authorizedIssuer) {
        this.issueDate = issueDate;
        if (authorizedIssuer instanceof VendingMachine && ((VendingMachine) authorizedIssuer).getVendingMachineAvailability()==false) {throw new AvailabilityException(authorizedIssuer);
        }
        this.authorizedIssuer = authorizedIssuer;
    }

    protected FareProduct() {
    }

    public UUID getIdFareProduct() {
        return idFareProduct;
    }

    public LocalDateTime getIssueDate() {
        return issueDate;
    }

    public AuthorizedIssuer getAuthorizedIssuer() {
        return authorizedIssuer;
    }

    @Override
    public String toString() {
        return "FareProduct{" +
                "idFareProduct=" + idFareProduct +
                ", issueDate=" + issueDate +
                ", authorizedIssuer=" + authorizedIssuer +
                '}';
    }
}

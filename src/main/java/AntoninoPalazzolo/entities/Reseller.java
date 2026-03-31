package AntoninoPalazzolo.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "resellers")
public class Reseller extends AuthorizedIssuer {
    @Column(name = "VAT_number", unique = true, nullable = false)
    private String vatNumber;

    public Reseller(String issuerName, String issuerAddress, String vatNumber) {
        super(issuerName, issuerAddress);
        this.vatNumber = vatNumber;
    }

    protected Reseller() {
    }

    public String getVatNumber() {
        return vatNumber;
    }

    @Override
    public String toString() {
        return "Reseller{" +
                "vatNumber='" + vatNumber + '\'' +
                "} " + super.toString();
    }
}

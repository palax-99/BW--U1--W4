package AntoninoPalazzolo.entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "authorized_issuers")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class AuthorizedIssuer {
    @Id
    @GeneratedValue
    @Column(name = "id_authorized_issuer")
    private UUID idAuthorizedIssuer;

    @Column(name = "issuer_name", unique = true)
    private String issuerName;

    @Column(name = "issuer_address")
    private String issuerAddress;

    public AuthorizedIssuer(String issuerName, String issuerAddress) {
        this.issuerName = issuerName;
        this.issuerAddress = issuerAddress;
    }

    protected AuthorizedIssuer() {
    }

    public String getIssuerAddress() {
        return issuerAddress;
    }

    public UUID getIdAuthorizedIssuer() {
        return idAuthorizedIssuer;
    }

    public String getIssuerName() {
        return issuerName;
    }

    @Override
    public String toString() {
        return "AuthorizedIssuer{" +
                "idAuthorizedIssuer=" + idAuthorizedIssuer +
                ", issuerName='" + issuerName + '\'' +
                ", issuerAddress='" + issuerAddress + '\'' +
                '}';
    }
}

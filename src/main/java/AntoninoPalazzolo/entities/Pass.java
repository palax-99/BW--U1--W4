package AntoninoPalazzolo.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "passes")
public class Pass extends FareProduct {
    @Column(name = "pass_type")
    @Enumerated(EnumType.STRING)
    private PassType passType;

    @ManyToOne
    @JoinColumn(name = "id_user_card", nullable = false)
    private UserCard userCard;

    @Column(name = "pass_expiry_date", nullable = false)
    private LocalDateTime passExpiryDate;

    public Pass(LocalDateTime issueDate, AuthorizedIssuer authorizedIssuer, PassType passType, UserCard userCard) {
        super(issueDate, authorizedIssuer);
        this.passType = passType;
        this.userCard = userCard;
        if (passType==PassType.WEEKLY) {
            this.passExpiryDate = issueDate.plusWeeks(1);
        } else if (passType==PassType.MONTHLY) {
        this.passExpiryDate = issueDate.plusMonths(1);
    } else if (passType==PassType.HALF_YEARLY){
            this.passExpiryDate = issueDate.plusMonths(6);
        }
    }

    protected Pass() {
    }

    public PassType getPassType() {
        return passType;
    }

    public UserCard getUserCard() {
        return userCard;
    }

    public LocalDateTime getPassExpiryDate() {
        return passExpiryDate;
    }

    @Override
    public String toString() {
        return "Pass{" +
                "passType=" + passType +
                ", userCard=" + userCard +
                ", passExpiryDate=" + passExpiryDate +
                "} " + super.toString();
    }


}

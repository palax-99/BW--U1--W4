package AntoninoPalazzolo.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
public class Ticket extends FareProduct {
    @Column(name = "validity_minutes", nullable = false)
    private int validityMinutes;

    @Column(name = "validated_at")
    private LocalDateTime validatedAt;

    @Column(name = "valid_until")
    private LocalDateTime validUntil;

    @ManyToOne
    @JoinColumn(name = "validated_on_vehicle")
    private Vehicle vehicle;

    public Ticket(LocalDateTime issueDate, AuthorizedIssuer authorizedIssuer, int validityMinutes) {
        super(issueDate, authorizedIssuer);
        this.validityMinutes = validityMinutes;
//        this.validatedAt = validatedAt;
//        this.validUntil = validUntil;
//        this.vehicle = vehicle;
    }

    public void setValidatedAt(LocalDateTime validatedAt) {
        this.validatedAt = validatedAt;
    }

    public void setValidUntil(LocalDateTime validUntil) {
        this.validUntil = validUntil;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    protected Ticket() {
    }

    public int getValidityMinutes() {
        return validityMinutes;
    }

    public LocalDateTime getValidatedAt() {
        return validatedAt;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public LocalDateTime getValidUntil() {
        return validUntil;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "validityMinutes=" + validityMinutes +
                ", validatedAt=" + validatedAt +
                ", validUntil=" + validUntil +
                ", vehicle=" + vehicle +
                "} " + super.toString();
    }

    public static Ticket ticketValidation(Ticket ticket, Vehicle vehicle ){
        ticket.setValidatedAt(LocalDateTime.now());
        ticket.setValidUntil(LocalDateTime.now().plusMinutes(ticket.getValidityMinutes()));
        ticket.setVehicle(vehicle);
        return ticket;
    }
}

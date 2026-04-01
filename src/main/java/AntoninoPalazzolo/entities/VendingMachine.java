package AntoninoPalazzolo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "vending_machine")
public class VendingMachine extends AuthorizedIssuer {
    @Column(name = "serial_number", unique = true, nullable = false)
    String serialNumber;

    @Column(name = "vending_machine_availability", nullable = false)
    Boolean vendingMachineAvailability;

    public VendingMachine(String issuerName, String issuerAddress, String serialNumber, Boolean vendingMachineAvailability) {
        super(issuerName, issuerAddress);
        this.serialNumber = serialNumber;
        this.vendingMachineAvailability = vendingMachineAvailability;
    }

    protected VendingMachine() {
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public Boolean getVendingMachineAvailability() {
        return vendingMachineAvailability;
    }

    public boolean setVendingMachineAvailability(Boolean vendingMachineAvailability) {
        this.vendingMachineAvailability = vendingMachineAvailability;
    }

    @Override
    public String toString() {
        return "VendingMachine{" +
                "serialNumber='" + serialNumber + '\'' +
                ", vendingMachineAvailability=" + vendingMachineAvailability +
                "} " + super.toString();
    }
}

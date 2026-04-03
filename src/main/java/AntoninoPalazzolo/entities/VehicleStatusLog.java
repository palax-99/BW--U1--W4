package AntoninoPalazzolo.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "vehicle_status_log")
public class VehicleStatusLog {

    @Id
    @GeneratedValue
    @Column(name = "id_vehicle_status_log")
    private UUID idVehicleStatusLog;

    @ManyToOne
    @JoinColumn(name = "id_vehicle", nullable = false)
    private Vehicle vehicle;

    @Column(name = "vehicle_availability_updated_on", nullable = false)
    private LocalDateTime vehicleAvailabilityUpdatedOn;

    @Column(name = "vehicle_in_service", nullable = false)
    private boolean vehicleInService;

    @Column(name = "permanently_out_of_service", nullable = false)
    private boolean permanentlyOutOfService;

    @Column(name="description", columnDefinition = "TEXT")
    private String description;


    protected VehicleStatusLog() {
    }

    public VehicleStatusLog(Vehicle vehicle, boolean vehicleInService, String description) {
        this.vehicle = vehicle;
        this.vehicleInService = vehicleInService;
        this.description=description;
        this.vehicleAvailabilityUpdatedOn = LocalDateTime.now();
        //La data di aggiornamento viene impostata automaticamente al momento
        // della creazione del log
    }

    public VehicleStatusLog(Vehicle vehicle, boolean vehicleInService, boolean permanentlyOutOfService) {
        this.vehicle = vehicle;
        this.vehicleInService = vehicleInService;
        this.permanentlyOutOfService = permanentlyOutOfService;
        this.vehicleAvailabilityUpdatedOn = LocalDateTime.now();
        //La data di aggiornamento viene impostata automaticamente al momento
        // della creazione del log
    }

    public VehicleStatusLog(Vehicle vehicle, boolean vehicleInService) {
        this.vehicle = vehicle;
        this.vehicleInService = vehicleInService;
        this.permanentlyOutOfService = false;
        this.vehicleAvailabilityUpdatedOn = LocalDateTime.now();
        //La data di aggiornamento viene impostata automaticamente al momento
        // della creazione del log
    }

    public VehicleStatusLog(Vehicle vehicle, LocalDateTime vehicleAvailabilityUpdatedOn, boolean vehicleInService) {
        this.vehicle = vehicle;
        this.vehicleAvailabilityUpdatedOn = vehicleAvailabilityUpdatedOn;
        this.vehicleInService = vehicleInService;
    }

    public UUID getIdVehicleStatusLog() {
        return idVehicleStatusLog;
    }

    public LocalDateTime getVehicleAvailabilityUpdatedOn() {
        return vehicleAvailabilityUpdatedOn;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public boolean isVehicleInService() {
        return vehicleInService;
    }

    public boolean isPermanentlyOutOfService() {
        return permanentlyOutOfService;
    }

    @Override
    public String toString() {
        return "VehicleStatusLog{" +
                "idVehicleStatusLog=" + idVehicleStatusLog +
                ", vehicle=" + vehicle +
                ", vehicleAvailabilityUpdatedOn=" + vehicleAvailabilityUpdatedOn +
                ", vehicleInService=" + vehicleInService +
                ", permanentlyOutOfService=" + permanentlyOutOfService +
                '}';
    }
}
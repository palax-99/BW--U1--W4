package AntoninoPalazzolo.entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @GeneratedValue
    @Column(name = "id_vehicle")
    private UUID idVehicle;

    @Column(name = "license_plate", nullable = false, unique = true)
    private String licensePlate;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type", nullable = false)
    private VehicleType vehicleType;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    protected Vehicle() {
    }

    public Vehicle(String licensePlate, VehicleType vehicleType) {
        this.licensePlate = licensePlate;
        this.vehicleType = vehicleType;
        this.capacity = switch (vehicleType) {
            case TRAM -> 150;
            case BUS -> 90;
            // La capacity non viene passata dall'utente ma calcolata automaticamente
            // in base al tipo di veicolo tramite switch — in questo modo garantiamo
            // che ogni TRAM abbia sempre 150 posti e ogni BUS sempre 90
        };
    }

    public UUID getIdVehicle() {
        return idVehicle;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    } // lo useremo successivamente

    @Override
    public String toString() {
        return "Vehicle{" +
                "idVehicle=" + idVehicle +
                ", licensePlate='" + licensePlate + '\'' +
                ", vehicleType=" + vehicleType +
                ", capacity=" + capacity +
                '}';
    }
}

package AntoninoPalazzolo.entities;

import jakarta.persistence.*;

import java.time.Duration;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "runs")
public class Run {
    @Id
    @GeneratedValue
    @Column(name = "id_run")
    private UUID idRun;

    @ManyToOne
    @JoinColumn(name = "id_vehicle", nullable = false)
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "id_route", nullable = false)
    private Route route;

    @Column(name = "actual_departure_time", nullable = false)
    private LocalTime actualDepartureTime;

    @Column(name = "actual_arrival_time", nullable = false)
    private LocalTime actualArrivalTime;

    @Column(name = "actual_travel_time")
    private int actualTravelTime;

    public Run(Vehicle vehicle, Route route, LocalTime actualDepartureTime, LocalTime actualArrivalTime) {
        this.vehicle = vehicle;
        this.route = route;
        this.actualDepartureTime = actualDepartureTime;
        this.actualArrivalTime = actualArrivalTime;
        // Calcolo automaticamente la durata in minuti dalla differenza tra arrivo e partenza
        this.actualTravelTime = (int) Duration.between(actualDepartureTime, actualArrivalTime).toMinutes();
    }

    protected Run() {
    }

    public UUID getIdRun() {
        return idRun;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public Route getRoute() {
        return route;
    }

    public LocalTime getActualDepartureTime() {
        return actualDepartureTime;
    }

    public LocalTime getActualArrivalTime() {
        return actualArrivalTime;
    }

    public int getActualTravelTime() {
        return actualTravelTime;
    }

    @Override
    public String toString() {
        return "Run{" +
                "idRun=" + idRun +
                ", vehicle=" + vehicle +
                ", route=" + route +
                ", actualDepartureTime=" + actualDepartureTime +
                ", actualArrivalTime=" + actualArrivalTime +
                ", actualTravelTime=" + actualTravelTime +
                '}';
    }
}

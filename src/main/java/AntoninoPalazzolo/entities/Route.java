package AntoninoPalazzolo.entities;

import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "routes")
public class Route {

    @Id
    @GeneratedValue
    @Column(name = "id_route")
    private UUID idRoute;

    @ManyToOne
    @JoinColumn(name = "departure", nullable = false)
    private Stop departure;

    @ManyToOne
    @JoinColumn(name = "terminus", nullable = false)
    private Stop terminus;

    @Column(name = "travel_time_minutes", nullable = false)
    private int travelTimeMinutes;

    @Column(name = "scheduled_departure_time", nullable = false)
    private LocalTime scheduledDepartureTime;

    @Column(name = "scheduled_arrival_time", nullable = false)
    private LocalTime scheduledArrivalTime;

    protected Route() {
    }

    public Route(Stop departure, Stop terminus, int travelTimeMinutes,
                 LocalTime scheduledDepartureTime, LocalTime scheduledArrivalTime) {
        this.departure = departure;
        this.terminus = terminus;
        this.travelTimeMinutes = travelTimeMinutes;
        this.scheduledDepartureTime = scheduledDepartureTime;
        this.scheduledArrivalTime = scheduledArrivalTime;
    }

    public UUID getIdRoute() {
        return idRoute;
    }

    public Stop getDeparture() {
        return departure;
    }

    public Stop getTerminus() {
        return terminus;
    }

    public int getTravelTimeMinutes() {
        return travelTimeMinutes;
    }

    public LocalTime getScheduledDepartureTime() {
        return scheduledDepartureTime;
    }

    public LocalTime getScheduledArrivalTime() {
        return scheduledArrivalTime;
    }

    @Override
    public String toString() {
        return "Route{" +
                "idRoute=" + idRoute +
                ", departure=" + departure +
                ", terminus=" + terminus +
                ", travelTimeMinutes=" + travelTimeMinutes +
                ", scheduledDepartureTime=" + scheduledDepartureTime +
                ", scheduledArrivalTime=" + scheduledArrivalTime +
                '}';
    }
}

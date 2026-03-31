package AntoninoPalazzolo.entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "stops")
public class Stop {

    @Id
    @GeneratedValue
    @Column(name = "id_stop")
    private UUID idStop;

    @Column(name = "stop_name", nullable = false, unique = true)
    private String stopName;

    @Column(name = "stop_address")
    private String stopAddress;

    @ManyToOne
    @JoinColumn(name = "id_zone", nullable = false)
    private Zone zone;

    protected Stop() {
    }

    public Stop(String stopName, String stopAddress, Zone zone) {
        this.stopName = stopName;
        this.stopAddress = stopAddress;
        this.zone = zone;
    }

    public UUID getIdStop() {
        return idStop;
    }

    public String getStopName() {
        return stopName;
    }

    public String getStopAddress() {
        return stopAddress;
    }

    public Zone getZone() {
        return zone;
    }

    @Override
    public String toString() {
        return "Stop{" +
                "idStop=" + idStop +
                ", stopName='" + stopName + '\'' +
                ", stopAddress='" + stopAddress + '\'' +
                ", zone=" + zone +
                '}';
    }
}
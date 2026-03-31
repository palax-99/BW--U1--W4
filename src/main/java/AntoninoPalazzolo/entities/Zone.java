package AntoninoPalazzolo.entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "zones")
public class Zone {

    @Id
    @GeneratedValue
    @Column(name = "id_zone")
    private UUID idZone;

    @Column(name = "zone_name", nullable = false, unique = true)
    private String zoneName;

    protected Zone() {
    }

    public Zone(String zoneName) {
        this.zoneName = zoneName;
    }

    public UUID getIdZone() {
        return idZone;
    }

    public String getZoneName() {
        return zoneName;
    }

    @Override
    public String toString() {
        return "Zone{" +
                "idZone=" + idZone +
                ", zoneName='" + zoneName + '\'' +
                '}';
    }
}
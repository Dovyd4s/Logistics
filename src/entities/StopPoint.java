package entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;
@Data
@Entity
public class StopPoint implements Serializable {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int ID;
    private String stopName;
    private String stopDescription;
    private String address;
    private String coordinates;
    private LocalDateTime timeOfArrival;
    private LocalDateTime timeOfDeparture;
    private int odometer;
    private double volumeOfDiesel;

    @Override
    public String toString() {
        return "ID: " + ID + ", " + stopName + ", address: " + address;
    }
}

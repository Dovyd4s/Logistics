package entities;

import lombok.Data;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.List;
@Data
@Entity
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @OneToMany(orphanRemoval = true)
    private List<StopPoint> allRestPoints;
    @ManyToOne
    @Cascade(value = org.hibernate.annotations.CascadeType.MERGE)
    private Driver assignedDriver;
    @ManyToOne
    @Cascade(value = org.hibernate.annotations.CascadeType.MERGE)
    private Manager assignedManager;
    @ManyToOne
    @Cascade(value = org.hibernate.annotations.CascadeType.MERGE)
    private Manager assigned2ndManager;
    private float distance;
    private float fuelConsumed;
    private float averageSpeed;
    @OneToOne
    private Cargo cargo;
    @ManyToOne
    private Truck assignedTruck;
    private boolean inProcess;
    private boolean complete;
}

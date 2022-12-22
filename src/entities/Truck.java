package entities;

import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDate;
@Data
@Entity
public class Truck {
    @Id
    private String VIN;
    private String plateNumber;
    private String Make;
    private String Model;
    private float weightTonnes;
    private float maxCapacityTonnes;
    private float averageFuelConsumption;
    @ColumnDefault(value = "5")
    private float tankSizeLiters;
    private LocalDate manufacturedDate;
    private LocalDate lastTechnicalStateCheckDate;

    @Override
    public String toString() {
        return "VIN: " + VIN + " " + plateNumber + " " + Make + " " + Model;
    }
}

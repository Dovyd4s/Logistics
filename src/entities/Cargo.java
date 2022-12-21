package entities;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
@Data
@Entity
public class Cargo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;
    @Enumerated(EnumType.STRING)
    private CargoType cargoType;
    private LocalDate mustBeDeliveredUntilDate;
    private LocalDate readyForPickUpDate;
    private float valueOfCargoEUR;
    private float totalCargoWeightTonnes;
    private String pickupAddress;
    private String deliveryAddress;

    @Override
    public String toString() {
        return "Id: " + Id + ", " + pickupAddress + " - " + deliveryAddress + ", pickup: " + readyForPickUpDate + ", delivery: " + mustBeDeliveredUntilDate;
    }
}

package entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
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
}

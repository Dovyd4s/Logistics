package entities;

import lombok.Data;

import javax.persistence.Entity;
import java.io.Serializable;

@Data
@Entity
public class Manager extends User {
    private boolean isAdmin;
    private float bonusAmountPerKmEUR;
    private int minDistanceToGetBonus;
}

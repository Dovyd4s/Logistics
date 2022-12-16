package entities;

import lombok.Data;

import javax.persistence.Entity;
import java.io.Serializable;
import java.time.LocalDate;
@Data
@Entity
public class Driver extends User implements Serializable {
    private LocalDate HealthCheckValidUntilDate;
    private LocalDate DriverLicenseValidUntilDate;

    @Override
    public String toString() {
        return getName() + " " + getLastName() + " " + getClass().getSimpleName();
    }
}

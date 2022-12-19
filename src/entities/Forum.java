package entities;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
@Data
@Entity
public class Forum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    @ManyToOne
    private Forum parentForum;


    @Override
    public String toString() {
        return title;
    }
}

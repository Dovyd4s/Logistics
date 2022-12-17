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
    @OneToMany
    private List<Forum> subForums;
    @OneToMany
    private List<Comment> comments;

    @Override
    public String toString() {
        return title;
    }
}

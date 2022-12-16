package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
@Entity
public class Forum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToMany
    private List<Forum> subforums;
    private LocalDateTime lastActivityDateTime;
    @OneToMany
    private List<Comment> comments;
}

package entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
@Data
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne
    private User author;
    private LocalDateTime createdDateTime;
    private String title;
    private String commentText;
    @OneToMany (cascade = CascadeType.MERGE)
    private List<Comment> replies;

}

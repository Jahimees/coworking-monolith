package by.bsuir.antonovich.backend.data;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Table(name = "offices")
@Entity
public class Office implements SimpleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userOwner;


    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "offices_owners",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "office_id", referencedColumnName = "id")
    )
    private List<User> users;
}

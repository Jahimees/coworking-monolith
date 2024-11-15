package by.bsuir.antonovich.backend.data;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Table(name = "offices")
@Entity
@NoArgsConstructor
public class Office implements SimpleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "offices_owners",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "office_id", referencedColumnName = "id")
    )
    private List<User> userOwnerList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "office")
    private List<Floor> floors;

    public Office(Integer id) {
        this.id = id;
    }
}

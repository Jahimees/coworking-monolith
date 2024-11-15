package by.bsuir.antonovich.backend.data;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Table(name = "floors")
@NoArgsConstructor
public class Floor implements SimpleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "office_id")
    private Office office;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "floor")
    private List<Room> rooms;

    public Floor(final Integer id) {
        this.id = id;
    }
}

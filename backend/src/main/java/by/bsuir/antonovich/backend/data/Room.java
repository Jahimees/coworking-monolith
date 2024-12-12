package by.bsuir.antonovich.backend.data;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Table(name = "room")
@NoArgsConstructor
public class Room implements SimpleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "length")
    private Double length;

    @Column(name = "width")
    private Double width;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_type_id")
    private RoomType roomType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "room", cascade = CascadeType.ALL)
    private List<WorkSpace> workSpaces;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "floor_id")
    private Floor floor;

    public Room(final Integer id) {
        this.id = id;
    }
}

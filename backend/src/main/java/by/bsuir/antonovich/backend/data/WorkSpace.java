package by.bsuir.antonovich.backend.data;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "work_spaces")
public class WorkSpace implements SimpleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}

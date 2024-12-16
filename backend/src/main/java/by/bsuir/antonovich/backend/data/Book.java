package by.bsuir.antonovich.backend.data;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Table(name = "book")
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "theme")
    private String theme;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;
}

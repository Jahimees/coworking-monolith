package by.bsuir.antonovich.backend.repository;

import by.bsuir.antonovich.backend.data.Book;
import by.bsuir.antonovich.backend.data.Room;
import by.bsuir.antonovich.backend.data.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    List<Book> findAllByRoom(Room room);

    List<Book> findAllByUser(User user);

    @Query(value = "SELECT b.room_id, COUNT(*) as usage_count " +
            "FROM Book b " +
            "JOIN Room r ON b.room_id = r.id " +
            "JOIN Floors f ON r.floor_id = f.id " +
            "JOIN Offices o ON f.office_id = o.id " +
            "WHERE b.start_date >= :startDate " +
            "AND b.end_date <= :endDate " +
            "AND o.id = :myOfficeId " +
            "GROUP BY b.room_id " +
            "ORDER BY usage_count DESC " +
            "LIMIT 10",
            nativeQuery = true)
    List<Object[]> findMostUsedRoomsByOffice(
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("myOfficeId") Integer myOfficeId
    );

    @Query(value = "SELECT TIME_FORMAT(TIME(b.start_date), '%H:%i') as booking_time, COUNT(*) as booking_count " +
            "FROM book b " +
            "JOIN room r ON b.room_id = r.id " +
            "JOIN floors f ON r.floor_id = f.id " +
            "JOIN offices o ON f.office_id = o.id " +
            "WHERE b.start_date >= :startDate " +
            "AND b.end_date <= :endDate " +
            "AND o.id = :myOfficeId " +
            "GROUP BY booking_time " +
            "ORDER BY booking_count DESC " +
            "LIMIT 48",
            nativeQuery = true)
    List<Object[]> findMostFrequentBookingTimesByOffice(
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("myOfficeId") Integer myOfficeId
    );
}

package by.bsuir.antonovich.backend.repository;

import by.bsuir.antonovich.backend.data.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomStatusRepository extends JpaRepository<RoomStatus, Integer> {
}

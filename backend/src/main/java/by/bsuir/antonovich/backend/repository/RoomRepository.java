package by.bsuir.antonovich.backend.repository;

import by.bsuir.antonovich.backend.data.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
}

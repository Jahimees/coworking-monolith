package by.bsuir.antonovich.backend.repository;

import by.bsuir.antonovich.backend.data.Floor;
import by.bsuir.antonovich.backend.data.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {

    List<Room> findAllByFloor(Floor floor);
}

package by.bsuir.antonovich.backend.repository;

import by.bsuir.antonovich.backend.data.Floor;
import by.bsuir.antonovich.backend.data.Room;
import by.bsuir.antonovich.backend.data.RoomStatus;
import by.bsuir.antonovich.backend.data.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, String> {

    List<Room> findAllByFloor(Floor floor);

    List<Room> findAllByRoomType(RoomType roomType);
    List<Room> findAllByRoomStatus(RoomStatus roomStatus);

    void deleteAllByFloor(Floor floor);
}

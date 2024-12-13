package by.bsuir.antonovich.backend.service;

import by.bsuir.antonovich.backend.data.Floor;
import by.bsuir.antonovich.backend.data.Room;
import by.bsuir.antonovich.backend.data.RoomStatus;
import by.bsuir.antonovich.backend.data.RoomType;
import by.bsuir.antonovich.backend.repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    public List<Room> findAllByRoomType(RoomType roomType) {
        return roomRepository.findAllByRoomType(roomType);
    }

    public List<Room> findAllByRoomStatus(RoomStatus roomStatus) {
        return roomRepository.findAllByRoomStatus(roomStatus);
    }

    @Transactional
    public List<Room> saveAll(List<Room> roomList) {
        return roomRepository.saveAll(roomList);
    }

    public Room save(Room room) {
        return roomRepository.save(room);
    }

}


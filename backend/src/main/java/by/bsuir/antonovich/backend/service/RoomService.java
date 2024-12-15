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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final FloorService floorService;

    public List<Room> findAllByRoomType(RoomType roomType) {
        return roomRepository.findAllByRoomType(roomType);
    }

    public List<Room> findAllByRoomStatus(RoomStatus roomStatus) {
        return roomRepository.findAllByRoomStatus(roomStatus);
    }

    public void deleteByFloorId(Integer floorId) {
        Optional<Floor> floorOptional = floorService.findById(floorId);

        if (floorOptional.isEmpty()) {
            throw new IllegalArgumentException("Invalid floorId");
        }

        roomRepository.deleteAllByFloor(floorOptional.get());
    }

    @Transactional
    public List<Room> saveAll(List<Room> roomList, Integer floorId) {
        deleteByFloorId(floorId);

        roomList.forEach(room -> {
                    room.setFloor(new Floor(floorId));
                    room.getWorkspaces().forEach(workspace -> {
                        workspace.setRoom(room);
                    });
                }
        );

        return roomRepository.saveAll(roomList);
    }

    @Transactional
    public List<Room> saveAll(List<Room> roomList) {
        return roomRepository.saveAll(roomList);
    }

}


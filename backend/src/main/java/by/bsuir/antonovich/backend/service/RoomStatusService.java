package by.bsuir.antonovich.backend.service;

import by.bsuir.antonovich.backend.data.Room;
import by.bsuir.antonovich.backend.data.RoomStatus;
import by.bsuir.antonovich.backend.repository.BookInfoRepository;
import by.bsuir.antonovich.backend.repository.RoomStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomStatusService {

    private final RoomStatusRepository roomStatusRepository;
    private final RoomService roomService;
    private final BookInfoRepository bookInfoRepository;

    public List<RoomStatus> findAll() {
        return roomStatusRepository.findAll();
    }

    public RoomStatus create(RoomStatus roomStatus) {

        return roomStatusRepository.save(roomStatus);
    }

    public Optional<RoomStatus> findById(Integer id) {
        return roomStatusRepository.findById(id);
    }

    public void delete(Integer id) {
        Optional<RoomStatus> roomStatusOptional = roomStatusRepository.findById(id);

        if (roomStatusOptional.isEmpty()) {
            throw new IllegalArgumentException("Room type not found");
        }

        List<RoomStatus> allRoomStatuses = roomStatusRepository.findAll();

        List<Room> allRooms = roomService.findAllByRoomStatus(roomStatusOptional.get());

//        TODO DEFAULT
        for (RoomStatus status : allRoomStatuses) {
            if (!status.getId().equals(roomStatusOptional.get().getId())) {
                allRooms.forEach(room -> {
                    room.setRoomStatus(status);
                });

                roomService.saveAll(allRooms);
                break;
            }
        }

        roomStatusRepository.delete(roomStatusOptional.get());
    }

    public RoomStatus update(RoomStatus roomStatus) {
        if (roomStatus == null || roomStatus.getId() == null) {
            throw new IllegalArgumentException("Room type not found");
        }

        if (roomStatus.getName() == null || roomStatus.getName().isEmpty()) {
            throw new IllegalArgumentException("Room type name cannot be empty");
        }

        if (roomStatus.getColor() == null || roomStatus.getColor().isEmpty()) {
            throw new IllegalArgumentException("Room type color cannot be empty");
        }

        Optional<RoomStatus> oldRoomStatusOptional = roomStatusRepository.findById(roomStatus.getId());

        if (oldRoomStatusOptional.isEmpty()) {
            throw new IllegalArgumentException("Room type not found");
        }

        RoomStatus oldRoomStatus = oldRoomStatusOptional.get();
        oldRoomStatus.setName(roomStatus.getName());
        oldRoomStatus.setColor(roomStatus.getColor());

        return roomStatusRepository.save(oldRoomStatus);
    }
}

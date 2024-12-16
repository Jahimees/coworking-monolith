package by.bsuir.antonovich.backend.service;

import by.bsuir.antonovich.backend.data.Room;
import by.bsuir.antonovich.backend.data.RoomType;
import by.bsuir.antonovich.backend.repository.RoomTypeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RoomTypeService {

    private final RoomTypeRepository roomTypeRepository;
    private final RoomService roomService;

    public List<RoomType> findAll() {
        return roomTypeRepository.findAll();
    }

    public RoomType create(RoomType roomType) {

        return roomTypeRepository.save(roomType);
    }

    public Optional<RoomType> findById(Integer id) {
        return roomTypeRepository.findById(id);
    }

    public void delete(Integer id) {
        Optional<RoomType> roomTypeOptional = roomTypeRepository.findById(id);

        if (roomTypeOptional.isEmpty()) {
            throw new IllegalArgumentException("Room type not found");
        }

        List<RoomType> allRoomTypes = roomTypeRepository.findAll();

        List<Room> allRooms = roomService.findAllByRoomType(roomTypeOptional.get());

        for (RoomType type : allRoomTypes) {
            if (!type.getId().equals(roomTypeOptional.get().getId())) {
                allRooms.forEach(room -> {
                    room.setRoomType(type);
                });
                roomService.saveAll(allRooms);
                break;
            }
        }

        roomTypeRepository.delete(roomTypeOptional.get());
    }

    public RoomType update(RoomType roomType) {
        if (roomType == null || roomType.getId() == null) {
            throw new IllegalArgumentException("Room type not found");
        }

        if (roomType.getName() == null || roomType.getName().isEmpty()) {
            throw new IllegalArgumentException("Room type name cannot be empty");
        }

        if (roomType.getColor() == null || roomType.getColor().isEmpty()) {
            throw new IllegalArgumentException("Room type color cannot be empty");
        }

        Optional<RoomType> oldRoomTypeOptional = roomTypeRepository.findById(roomType.getId());

        if (oldRoomTypeOptional.isEmpty()) {
            throw new IllegalArgumentException("Room type not found");
        }

        RoomType oldRoomType = oldRoomTypeOptional.get();
        oldRoomType.setName(roomType.getName());
        oldRoomType.setColor(roomType.getColor());

        return roomTypeRepository.save(oldRoomType);
    }
}

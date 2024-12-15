package by.bsuir.antonovich.backend.service.dto;

import by.bsuir.antonovich.backend.data.Room;
import by.bsuir.antonovich.backend.data.dto.RoomDto;
import by.bsuir.antonovich.backend.service.RoomService;
import by.bsuir.antonovich.backend.service.converter.Direction;
import by.bsuir.antonovich.backend.service.converter.RoomDtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomDtoService {

    private final RoomService roomService;

    public List<RoomDto> saveAll(List<RoomDto> roomDtoList, Integer floorId) {

        List<Room> createdEntity = roomService.saveAll(roomDtoList
                .stream()
                .map(roomDto -> RoomDtoConverter.convertToEntity(roomDto, Direction.DOWN)).toList(), floorId);

        return createdEntity.stream()
                .map(room -> RoomDtoConverter.convertToDto(room, Direction.DOWN)).toList();
    }
}

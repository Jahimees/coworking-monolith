package by.bsuir.antonovich.backend.service.dto;

import by.bsuir.antonovich.backend.data.RoomStatus;
import by.bsuir.antonovich.backend.data.dto.RoomStatusDto;
import by.bsuir.antonovich.backend.service.RoomStatusService;
import by.bsuir.antonovich.backend.service.converter.Direction;
import by.bsuir.antonovich.backend.service.converter.RoomStatusDtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomStatusDtoService {

    private final RoomStatusService roomStatusService;

    public List<RoomStatusDto> findAll() {
        return roomStatusService.findAll().stream().map(
                roomStatus -> RoomStatusDtoConverter.convertToDto(roomStatus, Direction.DOWN)).toList();
    }

    public RoomStatusDto create(RoomStatusDto roomStatusDto) {

        RoomStatus createdRoomStatus = roomStatusService.create(RoomStatusDtoConverter.convertToEntity(roomStatusDto, Direction.DOWN));

        return RoomStatusDtoConverter.convertToDto(createdRoomStatus, Direction.DOWN);
    }

    public void delete(Integer id) {
        roomStatusService.delete(id);
    }

    public RoomStatusDto update(RoomStatusDto roomStatusDto) {
        RoomStatus roomStatus = roomStatusService.update(RoomStatusDtoConverter.convertToEntity(roomStatusDto, Direction.DOWN));

        return RoomStatusDtoConverter.convertToDto(roomStatus, Direction.DOWN);
    }
}

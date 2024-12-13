package by.bsuir.antonovich.backend.service.dto;

import by.bsuir.antonovich.backend.data.RoomType;
import by.bsuir.antonovich.backend.data.dto.RoomTypeDto;
import by.bsuir.antonovich.backend.service.RoomTypeService;
import by.bsuir.antonovich.backend.service.converter.Direction;
import by.bsuir.antonovich.backend.service.converter.RoomTypeDtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomTypeDtoService {

    private final RoomTypeService roomTypeService;

    public List<RoomTypeDto> findAll() {
        return roomTypeService.findAll().stream().map(
                roomType -> RoomTypeDtoConverter.convertToDto(roomType, Direction.DOWN)).toList();
    }

    public RoomTypeDto create(RoomTypeDto roomTypeDto) {

        RoomType createdRoom = roomTypeService.create(RoomTypeDtoConverter.convertToEntity(roomTypeDto, Direction.DOWN));

        return RoomTypeDtoConverter.convertToDto(createdRoom, Direction.DOWN);
    }

    public void delete(Integer id) {
        roomTypeService.delete(id);
    }

    public RoomTypeDto update(RoomTypeDto roomTypeDto) {
        RoomType roomType = roomTypeService.update(RoomTypeDtoConverter.convertToEntity(roomTypeDto, Direction.DOWN));

        return RoomTypeDtoConverter.convertToDto(roomType, Direction.DOWN);
    }
}

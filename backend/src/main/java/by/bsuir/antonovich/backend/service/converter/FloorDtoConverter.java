package by.bsuir.antonovich.backend.service.converter;

import by.bsuir.antonovich.backend.data.Floor;
import by.bsuir.antonovich.backend.data.Room;
import by.bsuir.antonovich.backend.data.dto.FloorDto;
import by.bsuir.antonovich.backend.data.dto.RoomDto;

import java.util.ArrayList;
import java.util.List;

import static by.bsuir.antonovich.backend.service.converter.Direction.DOWN;

public final class FloorDtoConverter {

    private FloorDtoConverter() {
    }

    public static Floor convertToEntity(FloorDto dto, Direction direction) {
        Floor floor = new Floor();

        floor.setId(dto.getId());
        floor.setName(dto.getName());
        floor.setDescription(dto.getDescription());


        List<Room> roomList = new ArrayList<>();
        if (dto.getRooms() != null) {
            dto.getRooms().forEach(roomDto ->
                    roomList.add(direction == DOWN
                            ? RoomDtoConverter.convertToEntity(roomDto, direction)
                            : RoomDtoConverter.convertToEntityIdOnly(roomDto)
                    )
            );
        }

        if (dto.getOffice() != null) {
            floor.setOffice(direction == DOWN
                    ? OfficeDtoConverter.convertToEntityIdOnly(dto.getOffice())
                    : OfficeDtoConverter.convertToEntity(dto.getOffice(), direction)
            );
        }
        floor.setRooms(roomList);

        return floor;
    }

    public static FloorDto convertToDto(Floor entity, Direction direction) {
        FloorDto dto = new FloorDto();

        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());

        if (entity.getOffice() != null) {
            dto.setOffice(direction == DOWN
                    ? OfficeDtoConverter.convertToDtoIdOnly(entity.getOffice())
                    : OfficeDtoConverter.convertToDto(entity.getOffice(), direction));
        }

        if (entity.getRooms() != null) {
            List<RoomDto> roomDtoList = new ArrayList<>();
            entity.getRooms().forEach(roomDto ->
                    roomDtoList.add(direction == DOWN
                            ? RoomDtoConverter.convertToDto(roomDto, direction)
                            : RoomDtoConverter.convertToDtoIdOnly(roomDto)));

            dto.setRooms(roomDtoList);
        }

        return dto;
    }

    public static Floor convertToEntityIdOnly(FloorDto dto) {
        return new Floor(dto.getId());
    }

    public static FloorDto convertToDtoIdOnly(Floor entity) {
        return new FloorDto(entity.getId());
    }
}

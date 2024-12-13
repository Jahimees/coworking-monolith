package by.bsuir.antonovich.backend.service.converter;

import by.bsuir.antonovich.backend.data.RoomStatus;
import by.bsuir.antonovich.backend.data.dto.RoomStatusDto;

public final class RoomStatusDtoConverter {

    private RoomStatusDtoConverter() {
    }

    public static RoomStatus convertToEntity(RoomStatusDto dto, Direction direction) {
        RoomStatus roomStatus = new RoomStatus();

        roomStatus.setId(dto.getId());
        roomStatus.setName(dto.getName());
        roomStatus.setColor(dto.getColor());

        return roomStatus;
    }

    public static RoomStatusDto convertToDto(RoomStatus entity, Direction direction) {
        RoomStatusDto roomStatusDto = new RoomStatusDto();

        roomStatusDto.setId(entity.getId());
        roomStatusDto.setName(entity.getName());
        roomStatusDto.setColor(entity.getColor());

        return roomStatusDto;
    }
}

package by.bsuir.antonovich.backend.service.converter;

import by.bsuir.antonovich.backend.data.RoomType;
import by.bsuir.antonovich.backend.data.dto.RoomTypeDto;

public final class RoomTypeDtoConverter {

    private RoomTypeDtoConverter() {
    }

    public static RoomType convertToEntity(RoomTypeDto dto, Direction direction) {
        RoomType roomType = new RoomType();

        roomType.setId(dto.getId());
        roomType.setName(dto.getName());

        return roomType;
    }

    public static RoomTypeDto convertToDto(RoomType entity, Direction direction) {
        RoomTypeDto roomTypeDto = new RoomTypeDto();

        roomTypeDto.setId(entity.getId());
        roomTypeDto.setName(entity.getName());

        return roomTypeDto;
    }
}

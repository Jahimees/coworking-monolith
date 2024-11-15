package by.bsuir.antonovich.backend.service.converter;

import by.bsuir.antonovich.backend.data.Floor;
import by.bsuir.antonovich.backend.data.Office;
import by.bsuir.antonovich.backend.data.dto.FloorDto;
import by.bsuir.antonovich.backend.data.dto.OfficeDto;

import java.util.ArrayList;
import java.util.List;

import static by.bsuir.antonovich.backend.service.converter.Direction.DOWN;

public final class OfficeDtoConverter {

    private OfficeDtoConverter() {
    }

    public static Office convertToEntity(OfficeDto dto, Direction direction) {
        Office office = new Office();

        office.setId(dto.getId());
        office.setName(dto.getName());
        office.setAddress(dto.getAddress());

        if (dto.getFloors() != null) {
            List<Floor> floorList = new ArrayList<>();
            if (direction == DOWN) {
                dto.getFloors().forEach(floorDto -> floorList.add(FloorDtoConverter.convertToEntity(floorDto, direction)));
            } else {
                dto.getFloors().forEach(floorDto -> floorList.add(FloorDtoConverter.convertToEntityIdOnly(floorDto)));
            }
            office.setFloors(floorList);
        }

        return office;
    }

    public static OfficeDto convertToDto(Office entity, Direction direction) {
        OfficeDto dto = new OfficeDto();

        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setAddress(entity.getAddress());

        if (entity.getFloors() != null) {
            List<FloorDto> floorDtoList = new ArrayList<>();

            if (direction == DOWN) {
                entity.getFloors().forEach(floor -> floorDtoList.add(FloorDtoConverter.convertToDto(floor, direction)));
            } else {
                entity.getFloors().forEach(floor -> floorDtoList.add(FloorDtoConverter.convertToDtoIdOnly(floor)));
            }
            dto.setFloors(floorDtoList);

        }

        return dto;
    }

    public static Office convertToEntityIdOnly(OfficeDto dto) {
        return new Office(dto.getId());
    }

    public static OfficeDto convertToDtoIdOnly(Office entity) {
        return new OfficeDto(entity.getId());
    }
}

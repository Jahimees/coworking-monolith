package by.bsuir.antonovich.backend.service.converter;

import by.bsuir.antonovich.backend.data.WorkSpace;
import by.bsuir.antonovich.backend.data.dto.WorkSpaceDto;

import static by.bsuir.antonovich.backend.service.converter.Direction.DOWN;

public final class WorkSpaceDtoConverter {

    private WorkSpaceDtoConverter() {
    }

    public static WorkSpace convertToEntity(WorkSpaceDto dto, Direction direction) {
        WorkSpace workSpace = new WorkSpace();

        workSpace.setId(dto.getId());
        workSpace.setName(dto.getName());
        workSpace.setX(dto.getX());
        workSpace.setY(dto.getY());

        if (dto.getRoom() != null) {
            workSpace.setRoom(direction == DOWN
                    ? RoomDtoConverter.convertToEntityIdOnly(dto.getRoom())
                    : RoomDtoConverter.convertToEntity(dto.getRoom(), direction)
            );
        }

        return workSpace;
    }

    public static WorkSpaceDto convertToDto(WorkSpace entity, Direction direction) {
        WorkSpaceDto dto = new WorkSpaceDto();

        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setX(entity.getX());
        dto.setY(entity.getY());

        if (entity.getRoom() != null) {
            dto.setRoom(direction == DOWN
                    ? RoomDtoConverter.convertToDtoIdOnly(entity.getRoom())
                    : RoomDtoConverter.convertToDto(entity.getRoom(), direction)
            );
        }

        return dto;
    }

    public static WorkSpace convertToEntityIdOnly(WorkSpaceDto dto) {
        return new WorkSpace(dto.getId());
    }

    public static WorkSpaceDto convertToDtoIdOnly(WorkSpace entity) {
        return new WorkSpaceDto(entity.getId());
    }
}

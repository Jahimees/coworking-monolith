package by.bsuir.antonovich.backend.service.converter;

import by.bsuir.antonovich.backend.data.Room;
import by.bsuir.antonovich.backend.data.WorkSpace;
import by.bsuir.antonovich.backend.data.dto.RoomDto;
import by.bsuir.antonovich.backend.data.dto.WorkSpaceDto;

import java.util.ArrayList;
import java.util.List;

import static by.bsuir.antonovich.backend.service.converter.Direction.DOWN;

public final class RoomDtoConverter {

    private RoomDtoConverter() {
    }

    public static Room convertToEntity(RoomDto dto, Direction direction) {
        Room room = new Room();

        room.setId(dto.getId());
        room.setName(dto.getName());
        room.setSquare(dto.getSquare());

        if (dto.getDepartment() != null) {
            room.setDepartment(DepartmentDtoConverter.convertToEntity(dto.getDepartment(), direction));
        }

        if (dto.getRoomType() != null) {
            room.setRoomType(RoomTypeDtoConverter.convertToEntity(dto.getRoomType(), direction));
        }

        if (dto.getFloor() != null) {
            room.setFloor(direction == DOWN
                    ? FloorDtoConverter.convertToEntityIdOnly(dto.getFloor())
                    : FloorDtoConverter.convertToEntity(dto.getFloor(), direction));
        }

        if (dto.getWorkSpaces() != null) {
            List<WorkSpace> workSpaceList = new ArrayList<>();
            dto.getWorkSpaces().forEach(workSpace ->
                    workSpaceList.add(direction == DOWN
                            ? WorkSpaceDtoConverter.convertToEntity(workSpace, direction)
                            : WorkSpaceDtoConverter.convertToEntityIdOnly(workSpace)));

            room.setWorkSpaces(workSpaceList);
        }


        return room;
    }

    public static RoomDto convertToDto(Room entity, Direction direction) {
        RoomDto roomDto = new RoomDto();

        roomDto.setId(entity.getId());
        roomDto.setName(entity.getName());
        roomDto.setSquare(entity.getSquare());

        if (entity.getDepartment() != null) {
            roomDto.setDepartment(DepartmentDtoConverter.convertToDto(entity.getDepartment(), direction));
        }

        if (entity.getRoomType() != null) {
            roomDto.setRoomType(RoomTypeDtoConverter.convertToDto(entity.getRoomType(), direction));
        }

        if (entity.getFloor() != null) {
            roomDto.setFloor(direction == DOWN
                    ? FloorDtoConverter.convertToDtoIdOnly(entity.getFloor())
                    : FloorDtoConverter.convertToDto(entity.getFloor(), direction)
            );
        }

        if (entity.getWorkSpaces() != null) {
            List<WorkSpaceDto> workSpaceDtoList = new ArrayList<>();
            entity.getWorkSpaces().forEach(workSpace ->
                    workSpaceDtoList.add(direction == DOWN
                            ? WorkSpaceDtoConverter.convertToDto(workSpace, direction)
                            : WorkSpaceDtoConverter.convertToDtoIdOnly(workSpace)));

            roomDto.setWorkSpaces(workSpaceDtoList);
        }

        return roomDto;
    }

    public static Room convertToEntityIdOnly(RoomDto dto) {
        return new Room(dto.getId());
    }

    public static RoomDto convertToDtoIdOnly(Room entity) {
        return new RoomDto(entity.getId());
    }
}

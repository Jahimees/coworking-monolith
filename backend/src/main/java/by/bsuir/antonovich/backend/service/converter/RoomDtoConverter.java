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
        room.setWidth(dto.getWidth());
        room.setLength(dto.getLength());
        room.setX(dto.getX());
        room.setY(dto.getY());

        if (dto.getDepartment() != null) {
            room.setDepartment(DepartmentDtoConverter.convertToEntity(dto.getDepartment(), direction));
        }

        if (dto.getType() != null) {
            room.setRoomType(RoomTypeDtoConverter.convertToEntity(dto.getType(), direction));
        }

        if (dto.getStatus() != null) {
            room.setRoomStatus(RoomStatusDtoConverter.convertToEntity(dto.getStatus(), direction));
        }

        if (dto.getFloor() != null) {
            room.setFloor(direction == DOWN
                    ? FloorDtoConverter.convertToEntityIdOnly(dto.getFloor())
                    : FloorDtoConverter.convertToEntity(dto.getFloor(), direction));
        }

        if (dto.getWorkspaces() != null) {
            List<WorkSpace> workSpaceList = new ArrayList<>();
            dto.getWorkspaces().forEach(workSpace ->
                    workSpaceList.add(direction == DOWN
                            ? WorkSpaceDtoConverter.convertToEntity(workSpace, direction)
                            : WorkSpaceDtoConverter.convertToEntityIdOnly(workSpace)));

            room.setWorkspaces(workSpaceList);
        }


        return room;
    }

    public static RoomDto convertToDto(Room entity, Direction direction) {
        RoomDto roomDto = new RoomDto();

        roomDto.setId(entity.getId());
        roomDto.setName(entity.getName());
        roomDto.setWidth(entity.getWidth());
        roomDto.setLength(entity.getLength());
        roomDto.setX(entity.getX());
        roomDto.setY(entity.getY());

        if (entity.getDepartment() != null) {
            roomDto.setDepartment(DepartmentDtoConverter.convertToDto(entity.getDepartment(), direction));
        }

        if (entity.getRoomType() != null) {
            roomDto.setType(RoomTypeDtoConverter.convertToDto(entity.getRoomType(), direction));
        }

        if (entity.getRoomStatus() != null) {
            roomDto.setStatus(RoomStatusDtoConverter.convertToDto(entity.getRoomStatus(), direction));
        }

        if (entity.getFloor() != null) {
            roomDto.setFloor(direction == DOWN
                    ? FloorDtoConverter.convertToDtoIdOnly(entity.getFloor())
                    : FloorDtoConverter.convertToDto(entity.getFloor(), direction)
            );
        }

        if (entity.getWorkspaces() != null) {
            List<WorkSpaceDto> workSpaceDtoList = new ArrayList<>();
            entity.getWorkspaces().forEach(workSpace ->
                    workSpaceDtoList.add(direction == DOWN
                            ? WorkSpaceDtoConverter.convertToDto(workSpace, direction)
                            : WorkSpaceDtoConverter.convertToDtoIdOnly(workSpace)));

            roomDto.setWorkspaces(workSpaceDtoList);
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

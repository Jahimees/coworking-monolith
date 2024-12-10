package by.bsuir.antonovich.backend.service.converter;

import by.bsuir.antonovich.backend.data.Department;
import by.bsuir.antonovich.backend.data.dto.DepartmentDto;
import by.bsuir.antonovich.backend.data.dto.UserDto;

public final class DepartmentDtoConverter {

    private DepartmentDtoConverter() {
    }

    public static Department convertToEntity(DepartmentDto dto, Direction direction) {
        Department department = new Department();

        department.setId(dto.getId());
        department.setName(dto.getName());

        if (dto.getBoss() != null) {
            department.setBoss(UserDtoConverter.convertToEntity(dto.getBoss(), direction));
        }

        return department;
    }

    public static DepartmentDto convertToDto(Department entity, Direction direction) {
        DepartmentDto dto = new DepartmentDto();

        dto.setId(entity.getId());
        dto.setName(entity.getName());

        if (entity.getBoss() != null) {
            dto.setBoss(UserDtoConverter.convertToDto(entity.getBoss(), direction));
        }

        return dto;
    }
}

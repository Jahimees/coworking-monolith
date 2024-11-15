package by.bsuir.antonovich.backend.service.converter;

import by.bsuir.antonovich.backend.data.Role;
import by.bsuir.antonovich.backend.data.dto.RoleDto;

public final class RoleDtoConverter {

    private RoleDtoConverter() {}

    public static RoleDto convertToDto(Role role, Direction direction) {
        RoleDto roleDto = new RoleDto();

        roleDto.setId(role.getId());
        roleDto.setName(role.getName());

        return roleDto;
    }

    public static Role convertToEntity(RoleDto roleDto, Direction direction) {
        Role role = new Role();

        roleDto.setId(roleDto.getId());
        role.setName(roleDto.getName());

        return role;
    }
}

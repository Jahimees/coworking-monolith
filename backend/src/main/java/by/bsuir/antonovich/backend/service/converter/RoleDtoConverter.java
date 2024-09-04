package by.bsuir.antonovich.backend.service.converter;

import by.bsuir.antonovich.backend.data.Role;
import by.bsuir.antonovich.backend.data.dto.RoleDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoleDtoConverter {

    public RoleDto convertToDto(Role role) {
        RoleDto roleDto = new RoleDto();

        roleDto.setId(role.getId());
        roleDto.setName(role.getName());

        return roleDto;
    }

    public Role convertToEntity(RoleDto roleDto) {
        Role role = new Role();

        roleDto.setId(roleDto.getId());
        role.setName(roleDto.getName());

        return role;
    }
}

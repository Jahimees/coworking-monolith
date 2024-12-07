package by.bsuir.antonovich.backend.service.dto;

import by.bsuir.antonovich.backend.data.Role;
import by.bsuir.antonovich.backend.data.dto.RoleDto;
import by.bsuir.antonovich.backend.exception.RoleNotFoundException;
import by.bsuir.antonovich.backend.service.RoleService;
import by.bsuir.antonovich.backend.service.converter.Direction;
import by.bsuir.antonovich.backend.service.converter.RoleDtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleDtoService {

    private final RoleService roleService;

    public RoleDto getId(Integer id) throws RoleNotFoundException {
        Optional<Role> roleOptional = roleService.findById(id);

        if (roleOptional.isEmpty()) {
            throw new RoleNotFoundException("Role with id %s not found".formatted(id));
        }

        return RoleDtoConverter.convertToDto(roleOptional.get(), Direction.DOWN);
    }

    public List<RoleDto> getAll() {
        List<Role> roles = roleService.findAll();

        List<RoleDto> resultList = new ArrayList<>();
        roles.forEach(role -> resultList.add(RoleDtoConverter.convertToDto(role, Direction.DOWN)));

        return resultList;
    }
}

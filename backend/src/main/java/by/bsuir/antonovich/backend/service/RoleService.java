package by.bsuir.antonovich.backend.service;

import by.bsuir.antonovich.backend.data.Role;
import by.bsuir.antonovich.backend.exception.RoleNotFoundException;
import by.bsuir.antonovich.backend.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role findByName(String name) throws RoleNotFoundException {
        Optional<Role> roleOptional = roleRepository.findRoleByName(name);

        return roleOptional.orElseThrow(() -> new RoleNotFoundException("Role with name %s not found".formatted(name)));
    }

}

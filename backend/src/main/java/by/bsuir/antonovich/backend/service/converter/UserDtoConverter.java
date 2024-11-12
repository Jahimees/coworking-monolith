package by.bsuir.antonovich.backend.service.converter;

import by.bsuir.antonovich.backend.data.Role;
import by.bsuir.antonovich.backend.data.User;
import by.bsuir.antonovich.backend.data.dto.RoleDto;
import by.bsuir.antonovich.backend.data.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UserDtoConverter {

    private final RoleDtoConverter roleDtoConverter;
    private final PasswordEncoder passwordEncoder;

    public UserDto convertToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setRawPassword(null);
        userDto.setEmail(user.getEmail());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setMiddleName(user.getMiddleName());

        List<RoleDto> roleDtoList = new ArrayList<>();
        user.getRoles().forEach(role -> roleDtoList.add(roleDtoConverter.convertToDto(role)));

        userDto.setRoles(roleDtoList);

        return userDto;
    }

    public User convertToEntity(UserDto userDto) {
        User user = new User();

        user.setId(userDto.getId());
        user.setUsername(userDto.getUsername());
        if (userDto.getRawPassword() != null && !userDto.getRawPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDto.getRawPassword()));
        } else {
            user.setPassword(null);
        }
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setMiddleName(userDto.getMiddleName());

        List<Role> roleList = new ArrayList<>();
        if (userDto.getRoles() != null) {
            userDto.getRoles().forEach(role -> roleList.add(roleDtoConverter.convertToEntity(role)));

            user.setRoles(roleList);
        }

        return user;
    }
}

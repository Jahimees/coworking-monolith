package by.bsuir.antonovich.backend.service.converter;

import by.bsuir.antonovich.backend.data.Role;
import by.bsuir.antonovich.backend.data.User;
import by.bsuir.antonovich.backend.data.dto.RoleDto;
import by.bsuir.antonovich.backend.data.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        List<RoleDto> roleDtoList = new ArrayList<>();
        user.getRoles().forEach(role -> roleDtoList.add(roleDtoConverter.convertToDto(role)));

        userDto.setRoles(roleDtoList);

        return userDto;
    }

    public User convertToEntity(UserDto userDto) {
        User user = new User();

        user.setId(userDto.getId());
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getRawPassword()));
        user.setEmail(userDto.getEmail());

        List<Role> roleList = new ArrayList<>();
        if (userDto.getRoles() != null) {
            userDto.getRoles().forEach(role -> roleList.add(roleDtoConverter.convertToEntity(role)));

            user.setRoles(roleList);
        }

        return user;
    }

    public Map<String, Object> convertEntityToMap(User user) {
        Map<String, Object> params = new HashMap<>();

        params.put("id", user.getId());
        params.put("email", user.getEmail());
        params.put("username", user.getUsername());

        return params;
    }
}

package by.bsuir.antonovich.backend.service.dto;

import by.bsuir.antonovich.backend.data.User;
import by.bsuir.antonovich.backend.data.dto.UserDto;
import by.bsuir.antonovich.backend.exception.EmailAlreadyExistsException;
import by.bsuir.antonovich.backend.exception.NotEnoughRegistrationData;
import by.bsuir.antonovich.backend.exception.RoleNotFoundException;
import by.bsuir.antonovich.backend.exception.UsernameAlreadyExistsException;
import by.bsuir.antonovich.backend.service.UserService;
import by.bsuir.antonovich.backend.service.converter.UserDtoConverter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDtoService {

    private final UserService userService;
    private final UserDtoConverter userDtoConverter;

    public UserDto create(UserDto userDto) throws UsernameAlreadyExistsException, NotEnoughRegistrationData,
            EmailAlreadyExistsException, RoleNotFoundException {

        User user = userDtoConverter.convertToEntity(userDto);
        User createdUser = userService.create(user);

        return userDtoConverter.convertToDto(createdUser);
    }

    public UserDto loadUserByUsername(String username) {
        return userDtoConverter.convertToDto((User) userService.loadUserByUsername(username));
    }

}
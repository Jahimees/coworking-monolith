package by.bsuir.antonovich.backend.service.dto;

import by.bsuir.antonovich.backend.data.User;
import by.bsuir.antonovich.backend.data.dto.UserDto;
import by.bsuir.antonovich.backend.exception.*;
import by.bsuir.antonovich.backend.service.UserService;
import by.bsuir.antonovich.backend.service.converter.UserDtoConverter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public UserDto findUserByUsername(String username) throws UserNotFoundException {
        Optional<User> optionalUser = userService.findUserByUsername(username);

        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("User with username %s not found".formatted(username));
        }

        return userDtoConverter.convertToDto(optionalUser.get());
    }

    public UserDto patchUser(String username, UserDto userDto) throws UserNotFoundException {
        User user = userDtoConverter.convertToEntity(userDto);
        return userDtoConverter.convertToDto(userService.patchUser(username, user));
    }

}
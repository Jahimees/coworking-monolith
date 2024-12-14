package by.bsuir.antonovich.backend.service.dto;

import by.bsuir.antonovich.backend.data.User;
import by.bsuir.antonovich.backend.data.dto.UserDto;
import by.bsuir.antonovich.backend.exception.*;
import by.bsuir.antonovich.backend.service.UserService;
import by.bsuir.antonovich.backend.service.converter.UserDtoConverter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static by.bsuir.antonovich.backend.service.converter.Direction.DOWN;

@Service
@AllArgsConstructor
public class UserDtoService {

    private final UserService userService;

    public List<UserDto> findAll() {
        List<User> userList = userService.findAll();

        if (userList == null || userList.isEmpty()) {
            return Collections.emptyList();
        }

        List<UserDto> userDtoList = new ArrayList<>();
        userList.forEach(user -> userDtoList.add(UserDtoConverter.convertToDto(user, DOWN)));

        return userDtoList;
    }

    public UserDto create(UserDto userDto) throws UsernameAlreadyExistsException, NotEnoughRegistrationData,
            EmailAlreadyExistsException, RoleNotFoundException {

        User user = UserDtoConverter.convertToEntity(userDto, DOWN);
        User createdUser = userService.create(user);

        return UserDtoConverter.convertToDto(createdUser, DOWN);
    }

    public UserDto loadUserByUsername(String username) {
        return UserDtoConverter.convertToDto((User) userService.loadUserByUsername(username), DOWN);
    }

    public UserDto findUserByUsername(String username) throws UserNotFoundException {
        Optional<User> optionalUser = userService.findUserByUsername(username);

        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("User with username %s not found".formatted(username));
        }

        return UserDtoConverter.convertToDto(optionalUser.get(), DOWN);
    }

    public UserDto patchUser(String username, UserDto userDto) throws UserNotFoundException, RoleNotFoundException, DepartmentNotFoundException {
        User user = UserDtoConverter.convertToEntity(userDto, DOWN);
        return UserDtoConverter.convertToDto(userService.patchUser(username, user), DOWN);
    }


    public void deleteUser(Integer id) {
        userService.deleteUser(id);
    }

    public List<UserDto> findUsersWithoutWorkspace() {
        return null;
    }

}
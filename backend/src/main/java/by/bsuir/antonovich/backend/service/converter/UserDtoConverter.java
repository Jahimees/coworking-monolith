package by.bsuir.antonovich.backend.service.converter;

import by.bsuir.antonovich.backend.data.Department;
import by.bsuir.antonovich.backend.data.Office;
import by.bsuir.antonovich.backend.data.Role;
import by.bsuir.antonovich.backend.data.User;
import by.bsuir.antonovich.backend.data.dto.DepartmentDto;
import by.bsuir.antonovich.backend.data.dto.RoleDto;
import by.bsuir.antonovich.backend.data.dto.UserDto;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

public class UserDtoConverter {

    public static UserDto convertToDto(User user, Direction direction) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setPassword(null);
        userDto.setEmail(user.getEmail());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setMiddleName(user.getMiddleName());

        if (user.getDepartment() != null) {
            if (direction != Direction.UP) {
                userDto.setDepartment(DepartmentDtoConverter.convertToDto(user.getDepartment(), direction));
            } else {
                userDto.setDepartment(new DepartmentDto(user.getDepartment().getId()));
            }
        }

        List<RoleDto> roleDtoList = new ArrayList<>();
        user.getRoles().forEach(role -> roleDtoList.add(RoleDtoConverter.convertToDto(role, direction)));

        userDto.setRoles(roleDtoList);

        return userDto;
    }

    public static User convertToEntity(UserDto userDto, Direction direction) {
        User user = new User();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        user.setId(userDto.getId());
        user.setUsername(userDto.getUsername());
        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        } else {
            user.setPassword(null);
        }
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setMiddleName(userDto.getMiddleName());

        if (userDto.getDepartment() != null) {
            if (direction != Direction.UP) {
                user.setDepartment(DepartmentDtoConverter.convertToEntity(userDto.getDepartment(), direction));
            } else {
                user.setDepartment(new Department(userDto.getDepartment().getId()));
            }
        }

        if (userDto.getWorkSpace() != null) {
            user.setWorkSpace(WorkSpaceDtoConverter.convertToEntity(userDto.getWorkSpace(), direction));
        }

        List<Office> officeList = new ArrayList<>();
        if (userDto.getOffices() != null && !userDto.getOffices().isEmpty()) {
            userDto.getOffices().forEach(officeDto ->
                    officeList.add(OfficeDtoConverter.convertToEntity(officeDto, direction)));
            user.setOffices(officeList);
        }

        List<Role> roleList = new ArrayList<>();
        if (userDto.getRoles() != null) {
            userDto.getRoles().forEach(role -> roleList.add(RoleDtoConverter.convertToEntity(role, direction)));

            user.setRoles(roleList);
        }

        return user;
    }
}

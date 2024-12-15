package by.bsuir.antonovich.backend.service;

import by.bsuir.antonovich.backend.data.Department;
import by.bsuir.antonovich.backend.data.Role;
import by.bsuir.antonovich.backend.data.User;
import by.bsuir.antonovich.backend.data.WorkSpace;
import by.bsuir.antonovich.backend.exception.DepartmentNotFoundException;
import by.bsuir.antonovich.backend.exception.EmailAlreadyExistsException;
import by.bsuir.antonovich.backend.exception.NotEnoughRegistrationData;
import by.bsuir.antonovich.backend.exception.RoleNotFoundException;
import by.bsuir.antonovich.backend.exception.UserNotFoundException;
import by.bsuir.antonovich.backend.exception.UsernameAlreadyExistsException;
import by.bsuir.antonovich.backend.repository.DepartmentRepository;
import by.bsuir.antonovich.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static by.bsuir.antonovich.backend.util.Constants.ROLE_USER;

@Data
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final DepartmentRepository departmentRepository;
    private final WorkSpaceService workSpaceService;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    public User create(User user) throws UsernameAlreadyExistsException, NotEnoughRegistrationData,
            EmailAlreadyExistsException, RoleNotFoundException {
        if (user.getUsername() == null || user.getUsername().isEmpty()
                || user.getPassword() == null || user.getPassword().isEmpty()
                || user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new NotEnoughRegistrationData("User data hasn't enough data for registration");
        }

        checkUserExistence(user);

        user.setRoles(List.of(roleService.findByName(ROLE_USER)));

        if (user.getDepartment() == null || user.getDepartment().getId() == -1) {
            user.setDepartment(null);
        }

        return userRepository.saveAndFlush(user);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findUserByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("User not found"));
    }

    @Transactional
    public Optional<User> findUserByUsername(String username) {
        return userRepository.getUserByUsername(username);
    }

    private void checkUserExistence(User user) throws UsernameAlreadyExistsException, EmailAlreadyExistsException {
        Optional<User> userOptional = userRepository.getUserByUsername(user.getUsername());

        if (userOptional.isPresent()) {
            throw new UsernameAlreadyExistsException("User with this username already exists");
        }

        userOptional = userRepository.getUserByEmail(user.getEmail());

        if (userOptional.isPresent()) {
            throw new EmailAlreadyExistsException("User with this email already exists");
        }
    }

    public User patchUser(String username, User newUser) throws UserNotFoundException, RoleNotFoundException, DepartmentNotFoundException {
        Optional<User> oldUserOptional = findUserByUsername(username);

        if (oldUserOptional.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        User oldUser = oldUserOptional.get();

        oldUser.setEmail(newUser.getEmail() != null && !newUser.getEmail().isEmpty() ? newUser.getEmail() : oldUser.getEmail());
        oldUser.setPassword(newUser.getPassword() != null && !newUser.getPassword().isEmpty() ? newUser.getPassword() : oldUser.getPassword());
        oldUser.setUsername(newUser.getUsername() != null && !newUser.getUsername().isEmpty() ? newUser.getUsername() : oldUser.getUsername());
        oldUser.setFirstName(newUser.getFirstName() != null && !newUser.getFirstName().isEmpty() ? newUser.getFirstName() : oldUser.getFirstName());
        oldUser.setLastName(newUser.getLastName() != null && !newUser.getLastName().isEmpty() ? newUser.getLastName() : oldUser.getLastName());
        oldUser.setMiddleName(newUser.getMiddleName() != null && !newUser.getMiddleName().isEmpty() ? newUser.getMiddleName() : oldUser.getMiddleName());

        Department oldDepartment = oldUser.getDepartment();
        Department newDepartment = newUser.getDepartment();

        if (oldDepartment == null && newDepartment == null ||
                oldDepartment == null && newDepartment.getId() != null && newDepartment.getId() == -1) {
            oldUser.setDepartment(null);
        } else {
            if (newDepartment.getId() != null) {
                Optional<Department> departmentOptional = departmentRepository.findById(newDepartment.getId());
                if (departmentOptional.isPresent()) {
                    oldUser.setDepartment(departmentOptional.get());
                } else {
                    oldUser.setDepartment(null);
                }
            }
        }

        if (newUser.getRoles() != null && newUser.getRoles().get(0).getId() != null) {
            Integer newRoleId = newUser.getRoles().get(0).getId();
            if (!Objects.equals(oldUser.getRoles().get(0).getId(), newRoleId)) {
                Optional<Role> roleOptional = roleService.findById(newRoleId);

                if (roleOptional.isEmpty()) {
                    throw new RoleNotFoundException("Role with id %s not found".formatted(newRoleId));
                }

                oldUser.setRoles(List.of(roleOptional.get()));
            }
        }

        try {
            return userRepository.saveAndFlush(oldUser);
        } catch (Exception e) {
            return userRepository.saveAndFlush(oldUser);
        }
    }

    public Optional<User> getById(Integer id) {
        return userRepository.getUserById(id);
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    protected List<User> findAllByDepartment(Department department) {
        return userRepository.findAllByDepartment(department);
    }

    protected List<User> setNullDepartmentForUsers(List<User> users) {

        List<User> newUsers = new ArrayList<>();

        users.forEach(user -> {
            user.setDepartment(null);
            newUsers.add(userRepository.save(user));
        });

        return newUsers;
    }

    public List<User> findAllWithoutWorkspace() {
        List<WorkSpace> workSpacesWithUsers = workSpaceService.findAllWithUsers();
        List<User> users = findAll();

        List<Integer> workspaceUserId = new ArrayList<>();
        workSpacesWithUsers.forEach(workSpace -> workspaceUserId.add(workSpace.getUser().getId()));

        return users.stream().filter(user -> !workspaceUserId.contains(user.getId())).toList();
    }

}

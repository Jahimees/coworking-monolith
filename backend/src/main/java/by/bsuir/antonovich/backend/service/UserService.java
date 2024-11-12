package by.bsuir.antonovich.backend.service;

import by.bsuir.antonovich.backend.data.User;
import by.bsuir.antonovich.backend.exception.*;
import by.bsuir.antonovich.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static by.bsuir.antonovich.backend.util.Constants.ROLE_USER;

@Data
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleService roleService;

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

    public User patchUser(String username, User newUser) throws UserNotFoundException {
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
        oldUser.setMiddleName(newUser.getMiddleName() != null && newUser.getMiddleName().isEmpty() ? newUser.getMiddleName() : oldUser.getMiddleName());

         return userRepository.saveAndFlush(oldUser);
    }

}

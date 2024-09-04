package by.bsuir.antonovich.backend.service;

import by.bsuir.antonovich.backend.data.User;
import by.bsuir.antonovich.backend.exception.EmailAlreadyExistsException;
import by.bsuir.antonovich.backend.exception.NotEnoughRegistrationData;
import by.bsuir.antonovich.backend.exception.RoleNotFoundException;
import by.bsuir.antonovich.backend.exception.UsernameAlreadyExistsException;
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

    private Optional<User> findUserByUsername(String username) {
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

}

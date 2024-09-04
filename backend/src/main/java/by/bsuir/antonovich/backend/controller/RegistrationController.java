package by.bsuir.antonovich.backend.controller;

import by.bsuir.antonovich.backend.data.dto.UserDto;
import by.bsuir.antonovich.backend.exception.EmailAlreadyExistsException;
import by.bsuir.antonovich.backend.exception.NotEnoughRegistrationData;
import by.bsuir.antonovich.backend.exception.RoleNotFoundException;
import by.bsuir.antonovich.backend.exception.UsernameAlreadyExistsException;
import by.bsuir.antonovich.backend.service.dto.UserDtoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class RegistrationController {

    private final UserDtoService userDtoService;

    @PostMapping("/reg")
    public ResponseEntity<?> register(@RequestBody UserDto userDto) {
        UserDto createdUser;

        try {
            createdUser = userDtoService.create(userDto);
        } catch (NotEnoughRegistrationData e) {
            return ResponseEntity.badRequest().body("User object is empty. Check username, email and password");
        } catch (UsernameAlreadyExistsException e) {
            return ResponseEntity.badRequest().body("User with this username already exists");
        } catch (EmailAlreadyExistsException e) {
            return ResponseEntity.badRequest().body("User with this email already exists");
        } catch (RoleNotFoundException e) {
            return ResponseEntity.badRequest().body("Internal server error (role not found)");
        }

        return ResponseEntity.ok(createdUser);
    }
}

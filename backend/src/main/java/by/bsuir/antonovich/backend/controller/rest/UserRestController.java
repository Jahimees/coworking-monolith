package by.bsuir.antonovich.backend.controller.rest;

import by.bsuir.antonovich.backend.data.dto.UserDto;
import by.bsuir.antonovich.backend.exception.*;
import by.bsuir.antonovich.backend.service.dto.UserDtoService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserRestController {

    private final UserDtoService userDtoService;

    @GetMapping("/{username}")
    public ResponseEntity<?> findUser(@PathVariable String username) {

        UserDto userDto = null;
        try {
            userDto = userDtoService.findUserByUsername(username);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), NOT_FOUND);
        }

        return ResponseEntity.ok(userDto);
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserDto userDto) {
        try {
            return ResponseEntity.ok(userDtoService.create(userDto));
        } catch (UsernameAlreadyExistsException e) {
            return ResponseEntity.badRequest().body("Something went wrong");
        } catch (NotEnoughRegistrationData e) {
            return ResponseEntity.badRequest().body("Not enough registration data");
        } catch (EmailAlreadyExistsException e) {
            return ResponseEntity.badRequest().body("Email already exists");
        } catch (RoleNotFoundException e) {
            return ResponseEntity.badRequest().body("Role not found");
        }
    }

    @PatchMapping("/{username}")
    public ResponseEntity<?> patchUser(@PathVariable String username, @RequestBody UserDto userDto) {
        try {
            return ResponseEntity.ok(userDtoService.patchUser(username, userDto));
        } catch (UserNotFoundException | DepartmentNotFoundException | RoleNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers(@RequestParam(name = "without_workspace", required = false) boolean withoutWorkspace) {

        if (withoutWorkspace) {
            return ResponseEntity.ok(userDtoService.findUsersWithoutWorkspace());
        }

        return ResponseEntity.ok(userDtoService.findAll());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer userId) {
        userDtoService.deleteUser(userId);

        return ResponseEntity.ok().build();
    }
}

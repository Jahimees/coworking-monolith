package by.bsuir.antonovich.backend.controller.rest;

import by.bsuir.antonovich.backend.data.dto.UserDto;
import by.bsuir.antonovich.backend.exception.UserNotFoundException;
import by.bsuir.antonovich.backend.service.dto.UserDtoService;
import lombok.RequiredArgsConstructor;
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

    @PatchMapping("/{username}")
    public ResponseEntity<?> patchUser(@PathVariable String username, @RequestBody UserDto userDto) {
        try {
            return ResponseEntity.ok(userDtoService.patchUser(username, userDto));
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userDtoService.findAll());
    }
}

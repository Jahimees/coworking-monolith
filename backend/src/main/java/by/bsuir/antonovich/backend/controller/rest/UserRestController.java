package by.bsuir.antonovich.backend.controller.rest;

import by.bsuir.antonovich.backend.service.dto.UserDtoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserDtoService userDtoService;

    //    TODO roles
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userDtoService.findAll());
    }
}

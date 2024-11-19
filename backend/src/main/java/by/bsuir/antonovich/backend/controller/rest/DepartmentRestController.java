package by.bsuir.antonovich.backend.controller.rest;

import by.bsuir.antonovich.backend.service.dto.DepartmentDtoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
public class DepartmentRestController {

    private final DepartmentDtoService departmentDtoService;

    @GetMapping
    public ResponseEntity<?> getAllDepartments() {

        return ResponseEntity.ok(departmentDtoService.findAll());
    }
}

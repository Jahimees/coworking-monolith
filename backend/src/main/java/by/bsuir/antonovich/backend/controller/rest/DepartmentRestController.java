package by.bsuir.antonovich.backend.controller.rest;

import by.bsuir.antonovich.backend.data.dto.DepartmentDto;
import by.bsuir.antonovich.backend.service.dto.DepartmentDtoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    //TODO role check
    @PostMapping
    public ResponseEntity<?> createDepartment(@RequestBody DepartmentDto departmentDto) {

        DepartmentDto createdDepartmentDto = null;
        if (departmentDto != null) {
            createdDepartmentDto = departmentDtoService.create(departmentDto);
        }

        if (createdDepartmentDto == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(createdDepartmentDto);
    }
}

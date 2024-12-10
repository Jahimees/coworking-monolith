package by.bsuir.antonovich.backend.controller.rest;

import by.bsuir.antonovich.backend.data.dto.DepartmentDto;
import by.bsuir.antonovich.backend.exception.DepartmentNotFoundException;
import by.bsuir.antonovich.backend.service.dto.DepartmentDtoService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("{departmentId}")
    public ResponseEntity<?> patchDepartment(@PathVariable Integer departmentId, @RequestBody DepartmentDto departmentDto) {

        if (departmentDto == null || departmentId == null || !departmentId.equals(departmentDto.getId())) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(departmentDtoService.patch(departmentDto));
    }

    @DeleteMapping("/{departmentId}")
    public ResponseEntity<?> deleteDepartment(@PathVariable Integer departmentId) {
        try {
            departmentDtoService.deleteAndSetNullDepartmentForUsers(departmentId);

            return ResponseEntity.ok().build();
        } catch (DepartmentNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

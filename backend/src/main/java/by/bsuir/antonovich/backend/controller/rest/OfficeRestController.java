package by.bsuir.antonovich.backend.controller.rest;

import by.bsuir.antonovich.backend.data.dto.OfficeDto;
import by.bsuir.antonovich.backend.service.dto.FloorDtoService;
import by.bsuir.antonovich.backend.service.dto.OfficeDtoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/offices")
public class OfficeRestController {

    private final OfficeDtoService officeDtoService;
    private final FloorDtoService floorDtoService;

    @GetMapping
    public ResponseEntity<?> findAllOffices() {
        return ResponseEntity.ok(officeDtoService.findAll());
    }

    @PostMapping
    public ResponseEntity<?> createOffice(@RequestBody OfficeDto officeDto) {
        try {
            return ResponseEntity.ok(officeDtoService.create(officeDto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{officeId}")
    public ResponseEntity<?> saveOfficeInfo(@PathVariable Integer officeId, @RequestBody OfficeDto officeDto) {
        if (officeId == null) {
            return ResponseEntity.badRequest().body("Office id body null");
        }

        if (officeDto == null) {
            return ResponseEntity.badRequest().body("Office dto body null");
        }

        try {
            return ResponseEntity.ok(officeDtoService.updateOfficeInfo(officeDto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Something went wrong. Are you sure that object exists");
        }
    }

    @DeleteMapping("/{officeId}")
    public ResponseEntity<?> deleteOffice(@PathVariable Integer officeId) {
        if (officeId == null) {
            return ResponseEntity.badRequest().body("Office id body null");
        }

        try {
            officeDtoService.deleteOffice(officeId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{officeId}/floors")
    public ResponseEntity<?> findOfficeFloors(@PathVariable Integer officeId) {
        if (officeId == null) {
            return ResponseEntity.badRequest().body("Office id body null");
        }

        return ResponseEntity.ok(floorDtoService.findAllByOfficeId(officeId));
    }
}

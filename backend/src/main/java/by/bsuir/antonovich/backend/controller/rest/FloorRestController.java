package by.bsuir.antonovich.backend.controller.rest;

import by.bsuir.antonovich.backend.data.dto.FloorDto;
import by.bsuir.antonovich.backend.service.dto.FloorDtoService;
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
@RequestMapping("/api/v1/floors")
public class FloorRestController {

    private final FloorDtoService floorDtoService;

    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(floorDtoService.findAll());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody final FloorDto floorDto) {

        if (floorDto == null || floorDto.getName() == null || floorDto.getName().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(floorDtoService.create(floorDto));
    }

    @PutMapping("/{floorId}")
    public ResponseEntity<?> saveFloorInfo(@PathVariable final Integer floorId, @RequestBody final FloorDto floorDto) {
        if (floorId == null || floorDto == null || floorDto.getName() == null || floorDto.getName().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        if (!floorId.equals(floorDto.getId())) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(floorDtoService.updateFloorInfo(floorDto));
    }

    @DeleteMapping("/{floorId}")
    public ResponseEntity<?> deleteFloor(@PathVariable final Integer floorId) {
        if (floorId == null || floorId < 0) {
            return ResponseEntity.badRequest().build();
        }

        floorDtoService.deleteById(floorId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{floorId}")
    public ResponseEntity<?> findById(@PathVariable final Integer floorId) {
        return ResponseEntity.ok(floorDtoService.findById(floorId));
    }
}

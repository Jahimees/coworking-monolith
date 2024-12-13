package by.bsuir.antonovich.backend.controller.rest;

import by.bsuir.antonovich.backend.data.dto.RoomTypeDto;
import by.bsuir.antonovich.backend.service.dto.RoomTypeDtoService;
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
@RequestMapping("api/v1/room-types")
public class RoomTypeRestController {

    private final RoomTypeDtoService roomTypeDtoService;

    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(roomTypeDtoService.findAll());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody final RoomTypeDto roomTypeDto) {
        return ResponseEntity.ok(roomTypeDtoService.create(roomTypeDto));
    }

    @PutMapping("/{typeId}")
    public ResponseEntity<?> update(@PathVariable final Integer typeId, @RequestBody final RoomTypeDto roomTypeDto) {

        if (roomTypeDto.getId() == null) {
            return ResponseEntity.badRequest().build();
        }

        if (!roomTypeDto.getId().equals(typeId)) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(roomTypeDtoService.update(roomTypeDto));
    }

    @DeleteMapping("/{roomTypeId}")
    public ResponseEntity<?> delete(@PathVariable final Integer roomTypeId) {
        roomTypeDtoService.delete(roomTypeId);
        return ResponseEntity.ok().build();
    }
}

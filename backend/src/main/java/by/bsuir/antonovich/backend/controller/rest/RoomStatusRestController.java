package by.bsuir.antonovich.backend.controller.rest;

import by.bsuir.antonovich.backend.data.dto.RoomStatusDto;
import by.bsuir.antonovich.backend.service.dto.RoomStatusDtoService;
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
@RequestMapping("/api/v1/room-statuses")
public class RoomStatusRestController {

    private final RoomStatusDtoService roomStatusDtoService;

    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(roomStatusDtoService.findAll());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody final RoomStatusDto roomStatusDto) {
        return ResponseEntity.ok(roomStatusDtoService.create(roomStatusDto));
    }

    @PutMapping("/{statusId}")
    public ResponseEntity<?> update(@PathVariable final Integer statusId, @RequestBody final RoomStatusDto roomStatusDto) {

        if (roomStatusDto.getId() == null) {
            return ResponseEntity.badRequest().build();
        }

        if (!roomStatusDto.getId().equals(statusId)) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(roomStatusDtoService.update(roomStatusDto));
    }

    @DeleteMapping("/{roomStatusId}")
    public ResponseEntity<?> delete(@PathVariable final Integer roomStatusId) {
        roomStatusDtoService.delete(roomStatusId);
        return ResponseEntity.ok().build();
    }
}

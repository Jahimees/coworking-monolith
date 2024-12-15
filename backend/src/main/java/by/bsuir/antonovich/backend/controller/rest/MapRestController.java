package by.bsuir.antonovich.backend.controller.rest;

import by.bsuir.antonovich.backend.data.dto.MapDto;
import by.bsuir.antonovich.backend.service.dto.RoomDtoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/maps")
public class MapRestController {

    private final RoomDtoService roomDtoService;

    @PostMapping
    public ResponseEntity<?> saveMap(@RequestBody final MapDto mapDto) {
        Integer floorId = mapDto.getFloorId();
        Integer officeId = mapDto.getOfficeId();

        if (floorId == null || officeId == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(roomDtoService.saveAll(mapDto.getRooms(), floorId));
    }
}

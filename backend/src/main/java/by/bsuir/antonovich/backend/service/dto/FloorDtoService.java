package by.bsuir.antonovich.backend.service.dto;

import by.bsuir.antonovich.backend.data.Floor;
import by.bsuir.antonovich.backend.data.dto.FloorDto;
import by.bsuir.antonovich.backend.service.FloorService;
import by.bsuir.antonovich.backend.service.converter.Direction;
import by.bsuir.antonovich.backend.service.converter.FloorDtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FloorDtoService {

    private final FloorService floorService;

    public List<FloorDto> findAll() {
        return floorService.findAll()
                .stream()
                .map(floor -> FloorDtoConverter.convertToDto(floor, Direction.DOWN))
                .toList();
    }


    public List<FloorDto> findAllByOfficeId(Integer officeId) {

        return floorService.findAllByOfficeId(officeId)
                .stream()
                .map(floor -> FloorDtoConverter.convertToDto(floor, Direction.DOWN))
                .toList();
    }

    public FloorDto create(FloorDto floorDto) {
        Floor floor = FloorDtoConverter.convertToEntity(floorDto, Direction.DOWN);

        return FloorDtoConverter.convertToDto(floorService.create(floor), Direction.DOWN);
    }

    public FloorDto updateFloorInfo(FloorDto floorDto) {

        Floor newFloor = FloorDtoConverter.convertToEntity(floorDto, Direction.DOWN);

        return FloorDtoConverter.convertToDto(floorService.updateFloorInfo(newFloor), Direction.DOWN);
    }

    public void deleteById(Integer id) {
        floorService.deleteById(id);
    }
}

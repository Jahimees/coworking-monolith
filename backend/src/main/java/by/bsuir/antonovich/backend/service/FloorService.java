package by.bsuir.antonovich.backend.service;

import by.bsuir.antonovich.backend.data.Floor;
import by.bsuir.antonovich.backend.data.Office;
import by.bsuir.antonovich.backend.repository.FloorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FloorService {

    private final FloorRepository floorRepository;
    private final OfficeService officeService;

    public List<Floor> findAll() {
        return floorRepository.findAll();
    }

    public List<Floor> findAllByOfficeId(Integer officeId) {
        Optional<Office> officeOptional = officeService.findById(officeId);

        if (officeOptional.isEmpty()) {
            throw new IllegalArgumentException("Office not found");
        }

        Office office = officeOptional.get();

        return floorRepository.findAllByOffice(office);
    }

    public Floor create(Floor floor) {
        Optional<Office> officeOptional = officeService.findById(floor.getOffice().getId());

        if (officeOptional.isEmpty()) {
            throw new IllegalArgumentException("Office not found");
        }

        Office office = officeOptional.get();

        floor.setOffice(office);

        return floorRepository.save(floor);
    }

    @Transactional
    public Floor updateFloorInfo(Floor newFloor) {
        Optional<Floor> oldFloorOptional = floorRepository.findById(newFloor.getId());

        if (oldFloorOptional.isEmpty()) {
            throw new IllegalArgumentException("Floor not found");
        }

        Floor oldFloor = oldFloorOptional.get();

        if (newFloor.getName() == null || newFloor.getName().isEmpty()) {
            throw new IllegalArgumentException("Floor name not found");
        }

        oldFloor.setName(newFloor.getName());
        oldFloor.setDescription(newFloor.getDescription());

        return floorRepository.save(oldFloor);
    }

    public void deleteById(Integer id) {
        floorRepository.deleteById(id);
    }
}

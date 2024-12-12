package by.bsuir.antonovich.backend.service.dto;

import by.bsuir.antonovich.backend.data.Office;
import by.bsuir.antonovich.backend.data.dto.OfficeDto;
import by.bsuir.antonovich.backend.service.OfficeService;
import by.bsuir.antonovich.backend.service.converter.Direction;
import by.bsuir.antonovich.backend.service.converter.OfficeDtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OfficeDtoService {

    private final OfficeService officeService;

    public List<OfficeDto> findAll() {
        List<Office> officeList = officeService.findAll();
        return officeList.stream()
                .map(office -> OfficeDtoConverter.convertToDto(office, Direction.DOWN))
                .toList();
    }

    public OfficeDto create(OfficeDto officeDto) {
        Office newOffice = OfficeDtoConverter.convertToEntity(officeDto, Direction.DOWN);

        newOffice = officeService.create(newOffice);

        return OfficeDtoConverter.convertToDto(newOffice, Direction.DOWN);
    }

    public OfficeDto updateOfficeInfo(OfficeDto officeDto) {
        Office officeEntity = OfficeDtoConverter.convertToEntity(officeDto, Direction.DOWN);

        Office updatedOffice = officeService.updateOfficeInfo(officeEntity);

        return OfficeDtoConverter.convertToDto(updatedOffice, Direction.DOWN);
    }

    public void deleteOffice(Integer officeId) {
        officeService.deleteById(officeId);
    }
}

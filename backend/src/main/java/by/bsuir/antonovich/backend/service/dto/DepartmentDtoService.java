package by.bsuir.antonovich.backend.service.dto;

import by.bsuir.antonovich.backend.data.dto.DepartmentDto;
import by.bsuir.antonovich.backend.service.DepartmentService;
import by.bsuir.antonovich.backend.service.converter.DepartmentDtoConverter;
import by.bsuir.antonovich.backend.service.converter.Direction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentDtoService {

    private final DepartmentService departmentService;

    public List<DepartmentDto> findAll() {

        List<DepartmentDto> departmentDtoList = new ArrayList<>();
        departmentService.findAll()
                .forEach(department -> departmentDtoList.add(
                        DepartmentDtoConverter.convertToDto(department, Direction.DOWN)));

        return departmentDtoList;
    }
}

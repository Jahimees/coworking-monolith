package by.bsuir.antonovich.backend.service;

import by.bsuir.antonovich.backend.data.Department;
import by.bsuir.antonovich.backend.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public List<Department> findAll() {
        return departmentRepository.findAll();
    }
}

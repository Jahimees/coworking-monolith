package by.bsuir.antonovich.backend.service;

import by.bsuir.antonovich.backend.data.Department;
import by.bsuir.antonovich.backend.data.User;
import by.bsuir.antonovich.backend.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final UserService userService;

    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    public Department create(Department department) {
        Department newDepartment = new Department();

        if (department.getUser() != null && department.getUser().getId() != 0
                && department.getUser().getId() != -1) {

            Optional<User> userOptional = userService.getById(department.getUser().getId());
            userOptional.ifPresent(newDepartment::setUser);
        }

        if (department.getName() != null && !department.getName().isEmpty()) {
            newDepartment.setName(department.getName());

        } else {
            throw new IllegalArgumentException("Department name cannot be null");
        }

        return departmentRepository.save(newDepartment);
    }

    public Department patch(Department department) {
        if (department.getId() == null || department.getId() == -1) {
            throw new IllegalArgumentException("Department id cannot be null");
        }

        Optional<Department> oldDepartment = departmentRepository.findById(department.getId());

        if (!oldDepartment.isPresent()) {
            throw new IllegalArgumentException("Department not found");
        }

        Department newDepartment = oldDepartment.get();

        if (department.getUser() != null && department.getUser().getId() != 0 && department.getUser().getId() != -1) {
            userService.getById(department.getUser().getId()).ifPresent(newDepartment::setUser);
        }

        if (department.getName() != null && !department.getName().isEmpty()) {
            newDepartment.setName(department.getName());
        }

        return departmentRepository.save(newDepartment);
    }
}

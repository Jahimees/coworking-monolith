package by.bsuir.antonovich.backend.service;

import by.bsuir.antonovich.backend.data.Department;
import by.bsuir.antonovich.backend.data.User;
import by.bsuir.antonovich.backend.exception.DepartmentNotFoundException;
import by.bsuir.antonovich.backend.repository.DepartmentRepository;
import jakarta.transaction.Transactional;
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

        if (department.getName() != null && !department.getName().isEmpty()) {
            newDepartment.setName(department.getName());
        } else {
            throw new IllegalArgumentException("Department name cannot be null");
        }

        if (department.getBoss() != null && department.getBoss().getId() != null) {
            Optional<User> dbBoss = userService.getById(department.getBoss().getId());

            dbBoss.ifPresent(newDepartment::setBoss);
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

        if (department.getName() != null && !department.getName().isEmpty()) {
            newDepartment.setName(department.getName());
        }

        if (department.getBoss() != null && department.getBoss().getId() != null) {
            Optional<User> dbBossOptional = userService.getById(department.getBoss().getId());

            dbBossOptional.ifPresent(newDepartment::setBoss);
        }

        return departmentRepository.save(newDepartment);
    }

    public Optional<Department> findById(Integer id) {
        return departmentRepository.findById(id);
    }

    @Transactional
    public void deleteAndSetNullDepartmentForUsers(Integer id) throws DepartmentNotFoundException {
        Optional<Department> departmentOptional = departmentRepository.findById(id);

        if (departmentOptional.isEmpty()) {
            throw new DepartmentNotFoundException("Department with id %s not found".formatted(id));
        }

        Department dbDepartment = departmentOptional.get();

        List<User> departmentUsers = userService.findAllByDepartment(dbDepartment);
        userService.setNullDepartmentForUsers(departmentUsers);

        departmentRepository.delete(dbDepartment);
    }
}

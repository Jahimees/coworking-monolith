package by.bsuir.antonovich.backend.repository;

import by.bsuir.antonovich.backend.data.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
}

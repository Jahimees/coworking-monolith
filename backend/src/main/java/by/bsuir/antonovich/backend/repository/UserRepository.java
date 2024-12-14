package by.bsuir.antonovich.backend.repository;

import by.bsuir.antonovich.backend.data.Department;
import by.bsuir.antonovich.backend.data.User;
import by.bsuir.antonovich.backend.data.WorkSpace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> getUserByUsername(String username);

    Optional<User> getUserByEmail(String email);

    Optional<User> getUserById(Integer id);

    List<User> findAllByDepartment(Department department);

    List<User> findAllByWorkSpace(WorkSpace workSpace);

}

package by.bsuir.antonovich.backend.repository;

import by.bsuir.antonovich.backend.data.WorkSpace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkSpaceRepository extends JpaRepository<WorkSpace, String> {

    List<WorkSpace> findAllByUserNotNull();
}

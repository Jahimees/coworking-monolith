package by.bsuir.antonovich.backend.repository;

import by.bsuir.antonovich.backend.data.WorkSpace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkSpaceRepository extends JpaRepository<WorkSpace, Integer> {
}

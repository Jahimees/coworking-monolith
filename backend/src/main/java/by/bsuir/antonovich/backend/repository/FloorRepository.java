package by.bsuir.antonovich.backend.repository;

import by.bsuir.antonovich.backend.data.Floor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FloorRepository extends JpaRepository<Floor, Integer> {
}

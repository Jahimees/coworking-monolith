package by.bsuir.antonovich.backend.repository;

import by.bsuir.antonovich.backend.data.Floor;
import by.bsuir.antonovich.backend.data.Office;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FloorRepository extends JpaRepository<Floor, Integer> {

    List<Floor> findAllByOffice(Office office);
}

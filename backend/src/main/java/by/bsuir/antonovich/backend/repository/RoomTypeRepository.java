package by.bsuir.antonovich.backend.repository;

import by.bsuir.antonovich.backend.data.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, Integer> {
}

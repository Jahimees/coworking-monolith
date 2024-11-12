package by.bsuir.antonovich.backend.repository;

import by.bsuir.antonovich.backend.data.Office;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfficeRepository extends JpaRepository<Office, Integer> {
}

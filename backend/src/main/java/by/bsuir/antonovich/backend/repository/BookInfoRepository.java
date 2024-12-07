package by.bsuir.antonovich.backend.repository;

import by.bsuir.antonovich.backend.data.BookInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookInfoRepository extends JpaRepository<BookInfo, Integer> {
}

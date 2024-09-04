package by.bsuir.antonovich.backend.repository;

import by.bsuir.antonovich.backend.data.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> getUserByUsername(String username);

    Optional<User> getUserByEmail(String email);

}

package vshop.repository;

import org.springframework.data.repository.CrudRepository;
import vshop.domain.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmailAndPassword(String email, String password);
    Optional<User> findByEmail(String email);
}

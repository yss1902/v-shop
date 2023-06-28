package vshop.repository;

import org.springframework.data.repository.CrudRepository;
import vshop.domain.Session;

import java.util.Optional;

public interface SessionRepository extends CrudRepository<Session, Long> {
    Optional<Session> findByAccessToken(String accessToken);
}

package dev.dwidi.jhipster.repository;

import dev.dwidi.jhipster.domain.UserConfiguration;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserConfigurationRepository extends MongoRepository<UserConfiguration, String> {
    Optional<UserConfiguration> findByUserId(String userId);
    boolean existsByUserId(String userId);
    void deleteByUserId(String userId);
}

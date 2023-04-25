package me.mppombo.synchronyapi.repository;

import me.mppombo.synchronyapi.model.ApiUser;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface ApiUserRepository extends ListCrudRepository<ApiUser, Long> {
    List<ApiUser> findByLastNameIgnoreCase(String lastName);
    Optional<ApiUser> findByUsername(String username);
    Boolean existsByUsername(String username);
}

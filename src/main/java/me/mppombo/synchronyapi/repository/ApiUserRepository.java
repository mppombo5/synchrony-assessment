package me.mppombo.synchronyapi.repository;

import me.mppombo.synchronyapi.domain.ApiUser;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface ApiUserRepository extends ListCrudRepository<ApiUser, Long> {
    List<ApiUser> findByUsername(String username);
    List<ApiUser> findByLastNameIgnoreCase(String lastName);
}

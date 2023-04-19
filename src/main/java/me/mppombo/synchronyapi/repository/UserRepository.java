package me.mppombo.synchronyapi.repository;

import me.mppombo.synchronyapi.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findByUsername(String username);
    List<User> findByLastNameIgnoreCase(String lastName);
}

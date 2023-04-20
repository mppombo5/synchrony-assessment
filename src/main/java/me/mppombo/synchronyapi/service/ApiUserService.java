package me.mppombo.synchronyapi.service;

import me.mppombo.synchronyapi.domain.ApiUser;
import me.mppombo.synchronyapi.exception.ApiUserNotFoundException;
import me.mppombo.synchronyapi.repository.ApiUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiUserService {
    private final ApiUserRepository repository;

    public ApiUserService(ApiUserRepository repo) {
        this.repository = repo;
    }


    // Return a single user object.
    public ApiUser getSingleUser(Long id) {
        return repository.findById(id).orElseThrow(() -> new ApiUserNotFoundException(id));
    }

    // Returns a list of all currently registered users.
    public List<ApiUser> getAllRegisteredUsers() {
        return repository.findAll();
    }

    // Create a new user and save it to the repository.
    // TODO: check for existing username, create new exception for if it does
    public ApiUser registerNewUser(ApiUser newUser) {
        return repository.save(newUser);
    }
}

package me.mppombo.synchronyapi.service;

import me.mppombo.synchronyapi.exception.ApiUserNotFoundException;
import me.mppombo.synchronyapi.exception.ApiUserUsernameTakenException;
import me.mppombo.synchronyapi.models.ApiUser;
import me.mppombo.synchronyapi.repository.ApiUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiUserService {
    private final static Logger logger = LoggerFactory.getLogger(ApiUserService.class);

    private final ApiUserRepository repository;

    public ApiUserService(ApiUserRepository repo) {
        this.repository = repo;
    }


    // Returns a list of all currently registered users.
    public List<ApiUser> getAllRegisteredUsers() {
        return repository.findAll();
    }

    /*
     * Queries and returns a single user object by ID.
     * Returns a 404 if a user with the specified ID does not exist.
     */
    public ApiUser getSingleUser(Long id) {
        ApiUser user = repository.findById(id)
                .orElseThrow(() -> new ApiUserNotFoundException(id));
        logger.info("Found user {}", user);

        return user;
    }

    /*
     * Create a new user and save it to the repository.
     * The single constraint is that a new user cannot be created with a username that is already registered.
     */
    public ApiUser registerNewUser(ApiUser newUser) {
        List<ApiUser> sameUsernames = repository.findByUsername(newUser.getUsername());
        if (!sameUsernames.isEmpty()) {
            ApiUser existingUser = sameUsernames.get(0);
            throw new ApiUserUsernameTakenException(existingUser.getUsername());
        }

        ApiUser savedUser = repository.save(newUser);
        logger.info("Created new user {}", savedUser);

        return savedUser;
    }
}

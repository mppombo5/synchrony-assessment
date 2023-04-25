package me.mppombo.synchronyapi.service;

import me.mppombo.synchronyapi.exception.apiuser.ApiUserNotFoundException;
import me.mppombo.synchronyapi.exception.apiuser.ApiUserUsernameTakenException;
import me.mppombo.synchronyapi.models.ApiUser;
import me.mppombo.synchronyapi.repository.ApiUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiUserService {
    private final static Logger logger = LoggerFactory.getLogger(ApiUserService.class);

    private final ApiUserRepository userRepository;

    public ApiUserService(ApiUserRepository repo) {
        this.userRepository = repo;
    }


    // Returns a list of all currently registered users.
    public List<ApiUser> getAllRegisteredUsers() {
        return userRepository.findAll();
    }

    /*
     * Queries and returns a single user object by ID.
     * Returns a 404 if a user with the specified ID does not exist.
     */
    public ApiUser getSingleUser(Long id) {
        ApiUser user = userRepository.findById(id)
                .orElseThrow(() -> new ApiUserNotFoundException(id));
        logger.info("Found user {}", user);

        return user;
    }

    /*
     * Create a new user and save it to the repository.
     * The single constraint is that a new user cannot be created with a username that is already registered.
     */
    public ApiUser registerNewUser(ApiUser newUser) {
        String username = newUser.getUsername();
        if (userRepository.existsByUsername(username)) throw new ApiUserUsernameTakenException(username);

        ApiUser savedUser = userRepository.save(newUser);
        logger.info("Created new user {}", savedUser);

        return savedUser;
    }
}

package me.mppombo.synchronyapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.mppombo.synchronyapi.assembler.ApiUserDtoModelAssembler;
import me.mppombo.synchronyapi.dto.ApiUserDto;
import me.mppombo.synchronyapi.dto.LoginReqDto;
import me.mppombo.synchronyapi.dto.LoginResDto;
import me.mppombo.synchronyapi.dto.RegisterDto;
import me.mppombo.synchronyapi.model.ApiUser;
import me.mppombo.synchronyapi.security.JwtIssuer;
import me.mppombo.synchronyapi.security.UserPrincipal;
import me.mppombo.synchronyapi.service.ApiUserService;
import me.mppombo.synchronyapi.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final static Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final ApiUserService userService;
    private final AuthService authService;
    private final ApiUserDtoModelAssembler userDtoModelAssembler;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<EntityModel<ApiUserDto>> registerUser(
            @Valid
            @RequestBody
            RegisterDto newUserDto) {
        logger.info("Request to register new user w/ username='{}'", newUserDto.username());

        ApiUser newUser = ApiUser.fromRegisterDto(newUserDto);

        // Password encoding is done here and not in ApiUserService for exactly one reason, and that's because trying to
        // inject a PasswordEncoder bean into ApiUserService creates a circular dependency no less than four components
        // long. It *can* be fixed with some tricky uses of lazy construction, but it's just less of a headache to do it
        // here.
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        ApiUser savedUser = userService.registerNewUser(newUser);
        // Just registered, they won't have any pictures. So build with empty list
        var savedUserModel = userDtoModelAssembler.toModel(savedUser.toDto(List.of()));

        return ResponseEntity
                .created(savedUserModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(savedUserModel);
    }

    @PostMapping("/login")
    public LoginResDto login(@RequestBody LoginReqDto dto) {
        logger.info("Login attempt for username='{}'", dto.username());
        return authService.attemptLogin(dto.username(), dto.password());
    }
}

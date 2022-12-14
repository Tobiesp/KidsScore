package com.tspdevelopment.kidsscore.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.tspdevelopment.kidsscore.data.model.User;
import com.tspdevelopment.kidsscore.data.repository.UserRepository;
import com.tspdevelopment.kidsscore.helpers.JwtToken;
import com.tspdevelopment.kidsscore.helpers.JwtTokenUtil;
import com.tspdevelopment.kidsscore.provider.sqlprovider.UserProviderImpl;
import com.tspdevelopment.kidsscore.views.AuthRequest;
import com.tspdevelopment.kidsscore.views.UserView;

/**
 *
 * @author tobiesp
 */
@RestController 
@RequestMapping(path = "api/public")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserProviderImpl userProvider;
    private final Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController(AuthenticationManager authenticationManager,
                   JwtTokenUtil jwtTokenUtil,
                   UserRepository userRepo) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userProvider = new UserProviderImpl(userRepo);
    }
    
    @PostMapping("login")
    public ResponseEntity<UserView> login(@RequestBody @Validated AuthRequest request){
        try {
            logger.info("Auth Request: " + request.toString());
            Authentication authenticate = authenticationManager
                .authenticate(
                    new UsernamePasswordAuthenticationToken(
                        request.getUsername(), request.getPassword()
                    )
                );

            User user = (User) authenticate.getPrincipal();
            JwtToken token = jwtTokenUtil.generateAccessToken(user);
            user.setTokenId(token.getId());
            userProvider.updateJwtTokenId(user.getId(), token.getId());
            return ResponseEntity.ok()
                .header(
                    HttpHeaders.AUTHORIZATION,
                    token.getToken()
                )
                .body(toUserView(user));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    
    
    @PostMapping("logout")
    public ResponseEntity logout(@RequestHeader HttpHeaders headers){
        List<String> authHeader = headers.get(HttpHeaders.AUTHORIZATION);
        if(authHeader == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not allowed to access method.");
        }
        try {
            UUID userId = this.jwtTokenUtil.getUserId(authHeader.get(0).split(" ")[1]);
            this.userProvider.updateJwtTokenId(userId, null);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not allowed to access method.");
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    
    private UserView toUserView(User user) {
        UUID id = user.getId();
        if (id == null) {
            return null;
        }
        Optional<User> u = userProvider.findById(id);
        if(u.isPresent()) {
            UserView view = new UserView();
            view.setFullName(u.get().getFullName());
            view.setUsername(u.get().getUsername());
            view.setId(u.get().getId().toString());
            return view;
        } else {
            return null;
        }
    }
}

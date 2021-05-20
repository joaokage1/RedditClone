package com.joao.reddit.clone.services;

import com.joao.reddit.clone.dto.AuthenticationResponse;
import com.joao.reddit.clone.dto.LoginRequest;
import com.joao.reddit.clone.dto.RefreshTokenRequest;
import com.joao.reddit.clone.dto.RegisterRequest;
import com.joao.reddit.clone.exceptions.SpringRedditException;
import com.joao.reddit.clone.model.NotificationEmail;
import com.joao.reddit.clone.model.User;
import com.joao.reddit.clone.model.VerificationToken;
import com.joao.reddit.clone.repository.UserRepository;
import com.joao.reddit.clone.repository.VerificationTokenRepository;
import com.joao.reddit.clone.security.JwtProvider;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Data
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    public void signup(RegisterRequest request){

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(getPasswordEncoder().encode(request.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);

        getUserRepository().save(user);

        String token = generateVerificationToken(user);
        mailService.sendMail(new NotificationEmail("Please Activate your account"
                ,user.getEmail()
                ,"Thank you for signing up to Toddy Reddit, " +
                        "Please click on the bellow url to activate your account : " +
                        "http://localhost:8080/api/auth/accountVerification/" + token));
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        getVerificationTokenRepository().save(verificationToken);
        return token;
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> tokenOptional = getVerificationTokenRepository().findByToken(token);
        tokenOptional.orElseThrow(() -> new SpringRedditException("Invalid Token"));
        fetchUserAndEnable(tokenOptional.get());
    }

    private void fetchUserAndEnable(VerificationToken verificationToken) {
        @NotBlank(message = "Username is required") String username = verificationToken.getUser().getUsername();
        User user = getUserRepository().findByUsername(username).orElseThrow(() -> new SpringRedditException("User not found with name - " + username));
        user.setEnabled(true);

        getUserRepository().save(user);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authenticate = getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = getJwtProvider().generateToken(authenticate);
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(getRefreshTokenService().generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(getJwtProvider().getJwtExpirationInMillis()))
                .username(loginRequest.getUsername())
                .build();
    }

    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }

    public User getCurrentUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return getUserRepository().findByUsername(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getUsername()));
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        getRefreshTokenService().validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = getJwtProvider().generateTokenWithUserName(refreshTokenRequest.getUsername());
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(getJwtProvider().getJwtExpirationInMillis()))
                .username(refreshTokenRequest.getUsername())
                .build();
    }
}

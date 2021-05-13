package com.joao.reddit.clone.services;

import com.joao.reddit.clone.dto.RegisterRequest;
import com.joao.reddit.clone.model.NotificationEmail;
import com.joao.reddit.clone.model.User;
import com.joao.reddit.clone.model.VerificationToken;
import com.joao.reddit.clone.repository.UserRepository;
import com.joao.reddit.clone.repository.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
@Data
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;

    @Transactional
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
}

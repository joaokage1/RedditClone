package com.joao.reddit.clone.controller;

import com.joao.reddit.clone.dto.AuthenticationResponse;
import com.joao.reddit.clone.dto.LoginRequest;
import com.joao.reddit.clone.dto.RegisterRequest;
import com.joao.reddit.clone.services.AuthService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Data
public class AuthController {

    private  final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest request){
        getAuthService().signup(request);
        return new ResponseEntity<>("User Registration Successful", HttpStatus.OK);
    }

    @GetMapping("accountVerification/{token}")
    private  ResponseEntity<String> verifyAccount(@PathVariable String token){
        getAuthService().verifyAccount(token);

        return new ResponseEntity<>("Account Activated Successfully",HttpStatus.OK);
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest){
        return getAuthService().login(loginRequest);
    }
}

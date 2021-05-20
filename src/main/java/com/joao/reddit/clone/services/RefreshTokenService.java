package com.joao.reddit.clone.services;

import com.joao.reddit.clone.exceptions.SpringRedditException;
import com.joao.reddit.clone.model.RefreshToken;
import com.joao.reddit.clone.repository.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
@Data
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken generateRefreshToken() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedDate(Instant.now());

        return getRefreshTokenRepository().save(refreshToken);
    }

    void validateRefreshToken(String token) {
        getRefreshTokenRepository().findByToken(token)
                .orElseThrow(() -> new SpringRedditException("Invalid refresh Token"));
    }

    public void deleteRefreshToken(String token) {
        getRefreshTokenRepository().deleteByToken(token);
    }
}

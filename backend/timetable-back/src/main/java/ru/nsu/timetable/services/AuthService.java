package ru.nsu.timetable.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.nsu.timetable.configuration.security.jwt.JwtUtils;
import ru.nsu.timetable.exceptions.AuthException;
import ru.nsu.timetable.models.entities.RefreshToken;
import ru.nsu.timetable.models.entities.Role;
import ru.nsu.timetable.payload.requests.AuthRequest;
import ru.nsu.timetable.payload.response.JwtAuthResponse;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public JwtAuthResponse authenticate(AuthRequest authRequest) {
        String userEmail = authRequest.getEmail();

        if (!userService.existByEmailCheck(userEmail)) {
            throw new AuthException("Error: please check entered data");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userEmail, authRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String accessToken = jwtUtils.generateJwtToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        long expiresIn = refreshToken.getExpiryDate().getEpochSecond() - Instant.now().getEpochSecond();

        var user = userService.getUser(userDetails.getId());
        List<String> userRoles = user.getRoles().stream()
                .map(Role::toString)
                .toList();

        return new JwtAuthResponse(accessToken, refreshToken.getToken(), "Bearer", expiresIn, userRoles);
    }
}

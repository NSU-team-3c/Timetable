package ru.nsu.timetable.models.mappers;

import org.springframework.stereotype.Component;
import ru.nsu.timetable.models.dto.RefreshTokenDTO;
import ru.nsu.timetable.models.entities.RefreshToken;
import ru.nsu.timetable.repositories.UserRepository;

@Component
public class RefreshTokenMapper {
    private final UserRepository userRepository;

    public RefreshTokenMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public RefreshTokenDTO toRefreshTokenDTO(RefreshToken refreshToken) {
        return new RefreshTokenDTO(refreshToken.getId(), refreshToken.getUser().getId(),
                refreshToken.getToken(), refreshToken.getExpiryDate());
    }

    public RefreshToken toRefreshToken(RefreshTokenDTO refreshTokenDTO) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setId(refreshTokenDTO.id());
        refreshToken.setToken(refreshTokenDTO.token());
        refreshToken.setExpiryDate(refreshTokenDTO.expiryDate());
        userRepository.findById(refreshTokenDTO.userId())
                .ifPresent(refreshToken::setUser);
        return refreshToken;
    }
}

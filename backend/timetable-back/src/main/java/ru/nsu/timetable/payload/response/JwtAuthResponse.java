package ru.nsu.timetable.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class JwtAuthResponse {

    private String access_token;

    private String refresh_token;

    private String token_type = "Bearer";

    private Long expires_in;

    private String roles;
}

package ru.nsu.timetable.configuration.security.jwt;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.nsu.timetable.exceptions.AuthException;
import ru.nsu.timetable.exceptions.InvalidTokenException;
import ru.nsu.timetable.services.UserDetailsServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                log.debug("vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");

                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                log.debug("hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh" + username);


                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                log.debug("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                        userDetails.getAuthorities());
                log.debug("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                log.debug("ffffffffffffffffffffffffffffffffffffffffffffffff");

                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeu");

            }
        } catch (Exception e) {
            log.error("Can't set user authentication: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {

        String token = request.getParameter("token");

        if (token != null && !token.isEmpty()) {
            log.debug("User token: " + token);
            return token;
        }


        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}

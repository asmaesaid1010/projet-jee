package com.sid.authservice.sec.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sid.authservice.sec.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JwtAuthorizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers,authorization");
        response.addHeader("Access-Control-Expose-Headers", "Access-Control-Allow-Origin, Access-Control-Allow-Credentials, authorization");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PATCH, PUT, DELETE, OPTIONS");
        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {


            if (request.getServletPath().equals("/refreshToken") || request.getRequestURI().equals("/login")) {
                filterChain.doFilter(request, response);
            } else {
                String authToken = request.getHeader(JwtUtil.AUTH_HEADER);
                System.out.println("authToken");
                System.out.println(authToken);
                if (authToken != null && authToken.startsWith(JwtUtil.HEADER_PREFIX)) {
                    try {
                        String jwtToken = authToken.substring(JwtUtil.HEADER_PREFIX.length());
                        Algorithm algorithm = Algorithm.HMAC256(JwtUtil.SECRET);
                        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
                        DecodedJWT decodedJWT = jwtVerifier.verify(jwtToken);
                        String username = decodedJWT.getSubject();
                        System.out.println(username);
                        List<String> roles = decodedJWT.getClaim("roles").asList(String.class);
                        Collection<GrantedAuthority> authorities = new ArrayList<>();
                        roles.forEach(r ->
                                authorities.add(new SimpleGrantedAuthority(r))
                        );
                        System.out.println(authorities.toString());
                        //Collection<GrantedAuthority> authorities = roles.stream().map(r -> new SimpleGrantedAuthority(r)).collect(Collectors.toList());
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);

                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        //passer au filtre suivant
                        filterChain.doFilter(request, response);
                    } catch (Exception e) {
                        throw new RuntimeException(e.getMessage());
                    }
                } else {
                    filterChain.doFilter(request, response);
                }
            }
        }

    }
}

package com.sid.authservice.sec.web;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sid.authservice.sec.JwtUtil;
import com.sid.authservice.sec.dao.RoleUserForm;
import com.sid.authservice.sec.entites.AppRole;
import com.sid.authservice.sec.entites.AppUser;
import com.sid.authservice.sec.repo.AppRoleRepository;
import com.sid.authservice.sec.repo.AppUserRepository;
import com.sid.authservice.sec.service.AccountService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
public class AccountRestController {
    private AccountService accountService;
    private AppRoleRepository appRoleRepository;
    private AppUserRepository appUserRepository;

    public AccountRestController(AccountService accountService, AppRoleRepository appRoleRepository, AppUserRepository appUserRepository) {
        this.accountService = accountService;
        this.appRoleRepository = appRoleRepository;
        this.appUserRepository = appUserRepository;
    }

    @GetMapping(path = "/users")
    @PreAuthorize("hasAuthority('USER')")
    public List<AppUser> appUsers() {
        return accountService.allUser();
    }

    @GetMapping(path = "/roles")
    @PreAuthorize("hasAuthority('USER')")
    public List<AppRole> appRoles() {
        return accountService.allRole();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(path = "/users")
    public AppUser saveUser(@RequestBody AppUser appUser) {
        return accountService.addNewUser(appUser);
    }

    @PostMapping(path = "/roles")
    @PreAuthorize("hasAuthority('ADMIN')")
    public AppRole saveRole(@RequestBody AppRole appRole) {
        return accountService.addNewRole(appRole);
    }


    @DeleteMapping(path = "/deleteRole/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteRole(@PathVariable Long id) {
        appRoleRepository.deleteById(id);
    }

    @DeleteMapping(path = "/deleteUser/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteUser(@PathVariable Long id) {
        appUserRepository.deleteById(id);
    }

    @PostMapping(path = "/removeRoleFromUser")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void removeRoleFromUser(@RequestBody RoleUserForm roleUserForm) {
        accountService.removeRoleFromUser(roleUserForm.getUsername(), roleUserForm.getRoleName());
    }

    @PostMapping(path = "/addRoleToUser")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void addRoleToUser(@RequestBody RoleUserForm roleUserForm) {
        accountService.addRoleToUser(roleUserForm.getUsername(), roleUserForm.getRoleName());
    }

    @GetMapping(path = "/refreshToken")
    public Map<String, String> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String authToken = request.getHeader(JwtUtil.AUTH_HEADER);
        if (authToken != null && authToken.startsWith(JwtUtil.HEADER_PREFIX)) {
            try {
                String jwtRefreshToken = authToken.substring(JwtUtil.HEADER_PREFIX.length());
                Algorithm algorithm = Algorithm.HMAC256(JwtUtil.SECRET);
                JWTVerifier jwtVerifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = jwtVerifier.verify(jwtRefreshToken);
                String username = decodedJWT.getSubject();
                //Révocation de token
                AppUser appUser = accountService.loadUserByUsername(username);

                String jwtAccessToken = JWT.create()
                        .withSubject(appUser.getUsername())
                        .withIssuer(request.getRequestURI()
                                .toString()).withExpiresAt(new Date(System.currentTimeMillis() + JwtUtil.ACCESS_TOKEN_TIMEOUT))
                        .withClaim("role", appUser.getAppRoles().stream().map(r -> r.getRoleName()).collect(Collectors.toList()))
                        .sign(algorithm);
                Map<String, String> idToken = new HashMap<>();
                idToken.put("access-token", jwtAccessToken);
                idToken.put("refresh-token", jwtRefreshToken);
                return idToken;
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        } else {
            throw new RuntimeException("Refresh Token required");
        }

    }
}



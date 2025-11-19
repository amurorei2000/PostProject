package com.github.postproject.service;

import com.github.postproject.config.JwtTokenProvider;
import com.github.postproject.repository.role.Roles;
import com.github.postproject.repository.role.RolesRepository;
import com.github.postproject.repository.user.UserRole;
import com.github.postproject.repository.user.UserRoleRepository;
import com.github.postproject.repository.user.Users;
import com.github.postproject.repository.user.UsersRepository;
import com.github.postproject.service.exceptions.NotAcceptException;
import com.github.postproject.service.exceptions.NotFoundException;
import com.github.postproject.web.dto.auth.Login;
import com.github.postproject.web.dto.auth.SignUp;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsersRepository usersRepository;
    private final UserRoleRepository userRoleRepository;
    private final RolesRepository rolesRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public boolean signUp(SignUp signUpRequest) {
        String email = signUpRequest.getEmail();
        String password = signUpRequest.getPassword();

        // 이미 등록된 이메일이 있는지 확인
        if (usersRepository.existsByEmail(email)) {
            return false;
        }

        // 해당 유저 정보가 DB에 없으면 DB에 유저 정보 새로 등록
        Users user = usersRepository.findByEmail(email)
                .orElseGet(() -> usersRepository.save(Users.builder()
                                .email(email)
                                .password(password)
                                .build())
                );

        Roles foundRole = rolesRepository.findByRole("ROLE_USER")
                .orElseThrow(() -> new NotFoundException("해당하는 Role이 없습니다."));

        // 유저 Role 정보를 DB에 등록
        UserRole userRole = userRoleRepository.save(UserRole.builder()
                        .users(user)
                        .roles(foundRole)
                        .build()
        );

        return true;
    }


    public String login(Login loginRequest) {
        try {
            String email = loginRequest.getEmail();
            String password = loginRequest.getPassword();

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            Users foundUser = usersRepository.findByEmailFetchJoin(email)
                    .orElseThrow(() -> new NotFoundException("해당 유저를 찾을 수 없습니다."));

            List<String> roles = foundUser.getUserRoles().stream()
                    .map(UserRole::getRoles)
                    .map(Roles::getRole)
                    .toList();

            return jwtTokenProvider.createToken(foundUser.getEmail(), roles);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NotAcceptException("로그인 할 수 없습니다.");
        }
    }
}

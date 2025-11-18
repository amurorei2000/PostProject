package com.github.postproject.service.security;

import com.github.postproject.repository.role.Roles;
import com.github.postproject.repository.user.UserRole;
import com.github.postproject.repository.user.Users;
import com.github.postproject.repository.user.UsersRepository;
import com.github.postproject.service.exceptions.NotFoundException;
import com.github.postproject.userDetails.CustomUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Primary
public class CustomUserDetailService implements UserDetailsService {

    private final UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users foundUser = usersRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("해당 이메일을 가진 유저가 없습니다." + email));

        // 아이디, 이메일, 패스워드, 롤 정보를 반환
        return CustomUserDetails.builder()
                .userId(foundUser.getId())
                .email(foundUser.getEmail())
                .password(foundUser.getPassword())
                .authorities(foundUser.getUserRoles().stream()
                        .map(UserRole::getRoles)
                        .map(Roles::getRole)
                        .toList()
                )
                .build();
    }
}

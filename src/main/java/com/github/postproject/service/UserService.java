package com.github.postproject.service;

import com.github.postproject.repository.role.Roles;
import com.github.postproject.repository.role.RolesRepository;
import com.github.postproject.repository.user.UserRole;
import com.github.postproject.repository.user.UserRoleRepository;
import com.github.postproject.repository.user.Users;
import com.github.postproject.repository.user.UsersRepository;
import com.github.postproject.service.exceptions.AlreadyExistException;
import com.github.postproject.service.exceptions.NotAcceptException;
import com.github.postproject.service.exceptions.NotFoundException;
import com.github.postproject.web.dto.request.UserReq;
import com.github.postproject.web.dto.response.UserRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UsersRepository usersRepository;
    private final RolesRepository rolesRepository;
    private final UserRoleRepository userRoleRepository;

    @Transactional
    public UserRes signIn(UserReq userReq) {
        String email = userReq.getEmail();
        String password = userReq.getPassword();

        try {
            // 기 가입된 이메일 확인
            if (usersRepository.existsByEmail(email)) {
                throw new AlreadyExistException("같은 이름의 이메일이 이미 존재합니다!");
            }

            // 유저 정보 저장
            Users createdUser = Users.builder()
                    .email(email)
                    .password(password)
                    .build();

            usersRepository.save(createdUser);

            Roles foundRoles = rolesRepository.findByRole("ROLE_USER")
                    .orElseThrow(() -> new NotFoundException("ROLE 정보를 찾을 수 없습니다."));

            userRoleRepository.save(UserRole.builder()
                    .users(createdUser)
                    .roles(foundRoles)
                    .build());

            return UserRes.builder()
                    .id(createdUser.getId())
                    .email(createdUser.getEmail())
                    .role(foundRoles.getRole())
                    .build();

        } catch (RuntimeException ex) {
            throw new NotAcceptException("사용자 정보를 DB에 저장하는 도중에 에러가 발생했습니다!");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("사용자를 생성하는 도중에 에러가 발생했습니다!");
        }
    }

    public Boolean signOut() {
        // access token 블랙리스트에 등록


        return true;
    }
}

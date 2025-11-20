package com.github.postproject.repository.user;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {
    boolean existsByEmail(String email);

    Optional<Users> findByEmail(String email);

    @Query("SELECT u FROM Users u " +
           "JOIN FETCH u.userRoles ur " +
           "JOIN FETCH ur.roles r " +
           "WHERE u.email = :email")
    Optional<Users> findByEmailFetchJoin(String email);

    // 락온 모드로 조회
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u FROM Users u WHERE u.email = :userEmail")
    Optional<Users> findByEmailWithLock(@Param("userEmail") String userEmail);
}

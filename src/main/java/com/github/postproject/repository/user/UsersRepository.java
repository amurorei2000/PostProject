package com.github.postproject.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
}

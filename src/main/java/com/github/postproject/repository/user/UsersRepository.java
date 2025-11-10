package com.github.postproject.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {
    boolean existsByEmail(String email);

    Users findByEmail(String email);
}

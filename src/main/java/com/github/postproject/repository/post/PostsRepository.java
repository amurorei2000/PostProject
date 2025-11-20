package com.github.postproject.repository.post;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostsRepository extends JpaRepository<Posts, Integer> {

    List<Posts> findAllByWriterName(String email);

    // 락온 상태로 조회 쿼리(동시성 문제)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Posts p WHERE p.id=:postId")
    Optional<Posts> findByIdWithLock(@Param("postId") Integer postId);
}

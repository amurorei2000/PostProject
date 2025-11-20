package com.github.postproject.repository.post;

import com.github.postproject.repository.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Integer> {

    boolean existsByUsersAndPosts(Users users, Posts posts);
    void deleteByUsersAndPosts(Users users, Posts posts);
}

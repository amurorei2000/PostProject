package com.github.postproject.service;

import com.github.postproject.repository.post.PostLike;
import com.github.postproject.repository.post.PostLikeRepository;
import com.github.postproject.repository.post.Posts;
import com.github.postproject.repository.post.PostsRepository;
import com.github.postproject.repository.user.Users;
import com.github.postproject.repository.user.UsersRepository;
import com.github.postproject.service.exceptions.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final PostLikeRepository postLikeRepository;
    private final PostsRepository postsRepository;
    private final UsersRepository usersRepository;

    @Transactional
    public boolean toggleLike(Integer postId, String userEmail) {
        Posts likePost = postsRepository.findByIdWithLock(postId).orElseThrow(() -> new NotFoundException("해당 아이디로 게시글을 찾을 수 없습니다."));
        Users likeUser = usersRepository.findByEmailWithLock(userEmail).orElseThrow(() -> new NotFoundException("해당 아이디로 사용자를 찾을 수 없습니다."));

        if (postLikeRepository.existsByUsersAndPosts(likeUser, likePost)) {
            postLikeRepository.deleteByUsersAndPosts(likeUser, likePost);
            likePost.decreaseLikeCnt();
            return false;
        } else {
            postLikeRepository.save(PostLike.builder().posts(likePost).users(likeUser).build());
            likePost.increaseLikeCnt();
            return true;
        }
    }
}

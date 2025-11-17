package com.github.postproject.service;

import com.github.postproject.repository.post.Posts;
import com.github.postproject.repository.post.PostsRepository;
import com.github.postproject.repository.user.Users;
import com.github.postproject.repository.user.UsersRepository;
import com.github.postproject.service.exceptions.NotFoundException;
import com.github.postproject.web.dto.request.PostReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostsRepository postsRepository;
    private final UsersRepository usersRepository;

    public List<Posts> viewAllPosts() {
        return postsRepository.findAll();
    }

    public List<Posts> viewPostsByEmail(String email) {
        return postsRepository.findAllByWriterName(email);
    }

    public Posts createPost(PostReq postReq) {
        Users foundUser = usersRepository.findByEmail(postReq.getWriterId())
                .orElseThrow(() -> new NotFoundException("해당 아이디를 가진 유저가 없습니다."));

        Posts postEntity = Posts.builder()
                .title(postReq.getTitle())
                .content(postReq.getContent())
                .users(foundUser)
                .writerName(postReq.getWriterId())
                .viewCnt(0)
                .likeCnt(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return postsRepository.save(postEntity);
    }

    @Transactional
    public Posts updatePost(Integer postId, PostReq postReq) {
        Posts foundPost = postsRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("해당 게시물을 찾을 수 없습니다."));

        foundPost.setTitle(postReq.getTitle());
        foundPost.setContent(postReq.getContent());
        foundPost.setUpdatedAt(LocalDateTime.now());

        return foundPost;
    }

    public boolean deletePost(Integer postId) {
        Posts foundPost = postsRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("해당 아이디를 가진 게시물이 없습니다."));

        if (foundPost == null) {
            return false;
        }

        postsRepository.deleteById(postId);
        return true;
    }
}

package com.github.postproject.service;

import com.github.postproject.repository.post.Posts;
import com.github.postproject.repository.post.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostsRepository postsRepository;

    public List<Posts> viewAllPosts() {
        return postsRepository.findAll();
    }

    public List<Posts> viewPostsByEmail(String email) {
        return postsRepository.findAllByWriterName(email);
    }
}

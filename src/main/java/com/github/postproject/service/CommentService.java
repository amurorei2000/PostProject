package com.github.postproject.service;

import com.github.postproject.repository.comment.Comments;
import com.github.postproject.repository.comment.CommentsRepository;
import com.github.postproject.repository.post.Posts;
import com.github.postproject.repository.post.PostsRepository;
import com.github.postproject.repository.user.Users;
import com.github.postproject.repository.user.UsersRepository;
import com.github.postproject.service.exceptions.NotAcceptException;
import com.github.postproject.service.exceptions.NotFoundException;
import com.github.postproject.web.dto.request.CommentReq;
import com.github.postproject.web.dto.response.CommentRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentsRepository commentsRepository;
    private final UsersRepository usersRepository;
    private final PostsRepository postsRepository;

    @Transactional
    public CommentRes createComment(CommentReq commentReq) {
        try {
            Users writer = usersRepository.findByEmail(commentReq.getWriterId())
                    .orElseThrow(() -> new NotFoundException("해당 아이디를 가진 유저가 없습니다."));

            Posts foundPost = postsRepository.findById(commentReq.getPostId())
                    .orElseThrow(() -> new NotFoundException("해당 아이디를 가진 포스팅을 찾을 수 없습니다."));

            Comments commentEntity = Comments.builder()
                    .content(commentReq.getContent())
                    .users(writer)
                    .writerName(commentReq.getWriterId())
                    .posts(foundPost)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            log.info("댓글 엔티티: {}", commentEntity.toString());

            Comments savedComment = commentsRepository.save(commentEntity);

            return CommentRes.builder()
                    .id(savedComment.getId())
                    .content(savedComment.getContent())
                    .writer(savedComment.getWriterName())
                    .postId(savedComment.getPosts().getId())
                    .postedAt(savedComment.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                    .build();

        } catch (RuntimeException ex) {
            throw new NotAcceptException("댓글을 db에 저장하는 도중에 에러가 발생했습니다!");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("댓글을 생성하는 도중에 에러가 발생했습니다!");
        }
    }

    @Transactional
    public CommentRes updateComment(Integer commentId, String newContent) {
        try {
            Comments foundComment = commentsRepository.findById(commentId)
                    .orElseThrow(() -> new NotFoundException("해당 아이디로 댓글을 찾을 수 없습니다."));

            foundComment.setContent(newContent);
            foundComment.setUpdatedAt(LocalDateTime.now());

            String updateTime = foundComment.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            return CommentRes.builder()
                    .id(foundComment.getId())
                    .content(foundComment.getContent())
                    .writer(foundComment.getWriterName())
                    .postId(foundComment.getPosts().getId())
                    .postedAt(updateTime)
                    .build();

        } catch (RuntimeException ex) {
            throw new NotAcceptException("댓글을 db에 저장하는 도중에 에러가 발생했습니다!");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("댓글을 생성하는 도중에 에러가 발생했습니다!");
        }
    }

    public boolean deleteComment(Integer commentId) {
        try {
            commentsRepository.deleteById(commentId);
            return true;
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}

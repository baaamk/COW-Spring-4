package practice.banghyunwoocrudapi.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practice.banghyunwoocrudapi.comment.controller.dto.request.CreateCommentRequest;
import practice.banghyunwoocrudapi.comment.controller.dto.request.UpdateCommentRequest;
import practice.banghyunwoocrudapi.comment.controller.dto.response.CommentResponse;
import practice.banghyunwoocrudapi.comment.entity.Comment;
import practice.banghyunwoocrudapi.comment.repository.CommentRepository;
import practice.banghyunwoocrudapi.member.entity.Member;
import practice.banghyunwoocrudapi.member.repository.MemberJpaRepository;
import practice.banghyunwoocrudapi.post.entity.Post;
import practice.banghyunwoocrudapi.post.repository.PostRepository;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberJpaRepository memberJpaRepository;

    public void create(Long postId, CreateCommentRequest createCommentRequest) {
        Member member = memberJpaRepository.findById(createCommentRequest.getUserId())
                .orElseThrow(() -> new IndexOutOfBoundsException("존재하지 않는 userID입니다."));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 ID입니다."));
        Comment comment = createCommentRequest.toEntity(post, member);
        commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getComments(Long id) {
        return commentRepository.findByPostId(id).stream()
                .map(comment -> CommentResponse.from(comment))
                .collect(Collectors.toList());
    }

    public void update(Long id, UpdateCommentRequest updateCommentRequest) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IndexOutOfBoundsException("해당하는 ID가 없습니다."));
        comment.update(updateCommentRequest);
    }

    public void delete(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 댓글이 없습니다."));
        commentRepository.delete(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getByUserId(Long userId) {
        return commentRepository.findByMemberId(userId).stream()
                .map(comment -> CommentResponse.from(comment))
                .collect(Collectors.toList());
    }
}

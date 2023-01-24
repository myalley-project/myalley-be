package com.myalley.comment.repository;

import com.myalley.comment.domain.Comment;
import com.myalley.mate.domain.Mate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = "SELECT c FROM Comment c WHERE c.mate.id = :mateId and c.parent.id is null")
    List<Comment> findCommentsByMateId(@Param("mateId") Long mateId);

    List<Comment> findRepliesByParent(Comment parent);

    void deleteAllByMate(Mate mate);

//    Optional<Comment> findCommentByIdWithParent(Long commentId);
}

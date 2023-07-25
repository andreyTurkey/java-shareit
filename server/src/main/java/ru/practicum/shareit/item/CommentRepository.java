package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByItemId(Long itemId);

    @Query("select c from Comment as c join Item i on c.item.id = i.id where i.owner =:id order by c.id DESC")
    List<Comment> findCommentByAllItemByUserId(@Param("id") Long userId);
}

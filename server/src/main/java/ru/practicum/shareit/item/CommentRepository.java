package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByItemId(Long itemId);

    @Query(value = "select *\n" +
            "from comments as c\n" +
            "join items i on c.item_id = i.id\n" +
            "where owner = ?1\n" +
            "order by id DESC;", nativeQuery = true)
    List<Comment> findCommentByAllItemByUserId(Long userId);
}

package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Optional<Item> findById(Long itemId);

    List<Item> findAllByOwner(Long userId);

    List<Item> findByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(String name, String description);

    @Query("select i from Item i where i.requestId in :ids")
    List<Item> findAllByRequestId(@Param("ids") List<Long> ids);
}

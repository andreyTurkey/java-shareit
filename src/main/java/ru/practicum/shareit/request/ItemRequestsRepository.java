package ru.practicum.shareit.request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRequestsRepository extends JpaRepository<ItemRequest, Long> {

    Page<ItemRequest> findAll(Pageable page);

    List<ItemRequest> findAll();
}

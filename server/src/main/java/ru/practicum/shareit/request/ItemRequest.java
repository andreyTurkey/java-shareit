package ru.practicum.shareit.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests", schema = "public")
@RequiredArgsConstructor
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemRequest {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "description", nullable = false)
    String description;

    @Column(name = "user_id")
    Long userId;

    LocalDateTime created;
}

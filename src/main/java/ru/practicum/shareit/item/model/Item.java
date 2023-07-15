package ru.practicum.shareit.item.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "items", schema = "public")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Item {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank(message = "NAME can't be empty.")
    @Column(name = "name", nullable = false)
    String name;

    @NotNull(message = "DESCRIPTION can't be empty.")
    @Column(name = "description", nullable = false)
    String description;

    @Column(name = "owner")
    Long owner;

    @NotNull
    @Column(name = "available", nullable = false)
    Boolean available;

    @Column(name = "requestId")
    Long requestId;
}

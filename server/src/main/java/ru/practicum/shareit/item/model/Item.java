package ru.practicum.shareit.item.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "items", schema = "public")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Item {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "description", nullable = false)
    String description;

    @Column(name = "owner")
    Long owner;

    @Column(name = "available", nullable = false)
    Boolean available;

    @Column(name = "requestId")
    Long requestId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

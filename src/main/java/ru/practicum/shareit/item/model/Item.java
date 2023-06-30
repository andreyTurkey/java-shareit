package ru.practicum.shareit.item.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "items", schema = "public")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
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

    @ElementCollection
    @CollectionTable(name="comments", joinColumns=@JoinColumn(name="item_id"))
    @Column(name="id")
    List<Long> comments = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        return id != null && id.equals(((Item) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

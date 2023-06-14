package ru.practicum.shareit.user.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.model.Item;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {

    Long id;

    @NotBlank(message = "NAME can't be empty.") String name;

    @Email @NotBlank String email;

    final List<Long> items = new ArrayList<>();

    public List<Long> addItems(Item item) {
        items.add(item.getId());
        return items;
    }

    public List<Long> deleteItem(Long itemId) {
        items.remove(itemId);
        return items;
    }
}

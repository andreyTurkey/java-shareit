package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.booking.dto.BookingGetOwnerDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public class ItemMapperGetOwnerDto {

    public static ItemPublicDto getPublicItemDto(Item item, Long userId, List<CommentPublicDto> publicComments,
                                                 List<BookingGetOwnerDto> lastAndNextBookings) {
        if (item.getOwner().equals(userId)) {
            return new ItemPublicDto.ItemPublicDtoBuilder()
                    .id(item.getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .owner(item.getOwner())
                    .available(item.getAvailable())
                    .lastBooking(lastAndNextBookings.get(0))
                    .nextBooking(lastAndNextBookings.get(1))
                    .comments(publicComments)
                    .build();
        } else {
            return new ItemPublicDto.ItemPublicDtoBuilder()
                    .id(item.getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .owner(item.getOwner())
                    .available(item.getAvailable())
                    .lastBooking(null)
                    .nextBooking(null)
                    .comments(publicComments)
                    .build();
        }
    }
}




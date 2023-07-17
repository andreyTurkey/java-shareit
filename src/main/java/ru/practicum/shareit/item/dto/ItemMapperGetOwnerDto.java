package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.booking.dto.BookingGetOwnerDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public class ItemMapperGetOwnerDto {

    public static ItemPublicDto getPublicItemDto(Item item, Long userId, List<CommentPublicDto> publicComments,
                                                 List<BookingGetOwnerDto> lastAndNextBookings) {
        if (item.getOwner().equals(userId)) {
            ItemPublicDto itemPublicDto = new ItemPublicDto();
            itemPublicDto.setId(item.getId());
            itemPublicDto.setName(item.getName());
            itemPublicDto.setDescription(item.getDescription());
            itemPublicDto.setAvailable(item.getAvailable());
            itemPublicDto.setOwner(item.getOwner());
            itemPublicDto.setRequestId(item.getRequestId());
            itemPublicDto.setComments(publicComments);
            itemPublicDto.setLastBooking(lastAndNextBookings.get(0));
            itemPublicDto.setNextBooking(lastAndNextBookings.get(1));

            return itemPublicDto;
        } else {
            ItemPublicDto itemPublicDto = new ItemPublicDto();
            itemPublicDto.setId(item.getId());
            itemPublicDto.setName(item.getName());
            itemPublicDto.setDescription(item.getDescription());
            itemPublicDto.setAvailable(item.getAvailable());
            itemPublicDto.setOwner(item.getOwner());
            itemPublicDto.setRequestId(item.getRequestId());
            itemPublicDto.setComments(publicComments);
            itemPublicDto.setLastBooking(null);
            itemPublicDto.setNextBooking(null);

            return itemPublicDto;
        }
    }
}




package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.dto.BookingGetOwnerDto;
import ru.practicum.shareit.booking.dto.BookingMapperGetOwnerDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.CommentRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Component
public class ItemMapperGetOwnerDto {

    BookingRepository bookingRepository;

    BookingMapperGetOwnerDto bookingMapperGetOwnerDto;

    CommentRepository commentRepository;

    CommentMapper commentMapper;

    @Autowired
    public ItemMapperGetOwnerDto(BookingRepository bookingRepository,
                                 BookingMapperGetOwnerDto bookingMapperGetOwnerDto, CommentRepository commentRepository, CommentMapper commentMapper) {
        this.bookingRepository = bookingRepository;
        this.bookingMapperGetOwnerDto = bookingMapperGetOwnerDto;
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
    }

    public ItemPublicDto getItemGetOwnerDto(Item item, Long userId) {
        List<CommentPublicDto> publicComments = item.getComments().stream()
                .map(commentRepository::getById)
                .map(commentMapper::getPublicCommentDto)
                .collect(Collectors.toList());

        if (item.getOwner().equals(userId)) {
            List<Booking> orderByIdBookings = bookingRepository.findBookingByOwnerIdAndItemIdAndOrderById(
                    item.getOwner(), item.getId());

            BookingGetOwnerDto bookingGetOwnerDto;

            if (!(orderByIdBookings.size() == 0) && !orderByIdBookings.get(0).getStatus()
                    .equals(BookingState.REJECTED)) {
                bookingGetOwnerDto = bookingMapperGetOwnerDto.getBookingGetOwnerDto(orderByIdBookings.get(0));
            } else {
                bookingGetOwnerDto = null;
            }

            List<Booking> orderByStartBookings = bookingRepository.findBookingByOwnerIdAndOrderByStart(
                    item.getOwner(), item.getId());

            BookingGetOwnerDto bookingGetOwnerDto2;
            if (!(orderByStartBookings.size() == 0) && !orderByStartBookings.get(0).getStatus()
                    .equals(BookingState.REJECTED)) {
                bookingGetOwnerDto2 = bookingMapperGetOwnerDto.getBookingGetOwnerDto(orderByStartBookings.get(0));
            } else {
                bookingGetOwnerDto2 = null;
            }
            return new ItemPublicDto.ItemPublicDtoBuilder()
                    .id(item.getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .owner(item.getOwner())
                    .available(item.getAvailable())
                    .lastBooking(bookingGetOwnerDto)
                    .nextBooking(bookingGetOwnerDto2)
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

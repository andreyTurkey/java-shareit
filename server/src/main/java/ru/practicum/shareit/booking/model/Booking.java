package ru.practicum.shareit.booking.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings", schema = "public")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Booking {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    Item item;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "booking_state")
    BookingState status;

    @Column(name = "booking_start")
    LocalDateTime start;

    @Column(name = "booking_end")
    LocalDateTime end;
}

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.booking.BookingClient;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.BookingAddDto;
import ru.practicum.shareit.booking.BookingDto;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.user.UserDto;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {

    @Mock
    private BookingClient bookingClient;

    @InjectMocks
    private BookingController bookingController;

    private MockMvc mvc;

    private UserDto userDto;

    private ItemDto itemDto;

    private UserDto ownerDto;

    private BookingDto bookingDto;

    private ResponseEntity<Object> objectResponseEntity = new ResponseEntity<>(HttpStatus.OK);

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(bookingController)
                .build();

        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("John");
        userDto.setEmail("john.doe@mail.com");

        ownerDto = new UserDto();
        ownerDto.setId(2L);
        ownerDto.setName("Bill");
        ownerDto.setEmail("bill@mail.com");

        itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Test");
        itemDto.setDescription("Text for test");
        itemDto.setAvailable(true);
        itemDto.setOwner(1L);

        bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setItem(itemDto);
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));

    }

    @Test
    void addBookingTest() throws Exception {
        BookingAddDto bookingAddDto = new BookingAddDto();
        bookingAddDto.setId(1L);
        bookingAddDto.setStart(LocalDateTime.now().plusDays(1));
        bookingAddDto.setEnd(LocalDateTime.now().plusDays(2));


        Mockito.when(bookingClient.addBooking(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(objectResponseEntity);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        mvc.perform(MockMvcRequestBuilders.post("/bookings")
                        .header("X-Sharer-User-Id", String.valueOf(1L))
                        .content(mapper.writeValueAsString(bookingAddDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void updateStatusTest() throws Exception {
        ItemDto itemUpdateDtoName = new ItemDto();
        itemUpdateDtoName.setId(1L);
        itemUpdateDtoName.setDescription("Text for test");
        itemUpdateDtoName.setAvailable(true);
        itemUpdateDtoName.setOwner(1L);
        itemUpdateDtoName.setName("UpdateName");

        Mockito.when(bookingClient.updateStatus(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong(), ArgumentMatchers.any()))
                .thenReturn(objectResponseEntity);

        mvc.perform(MockMvcRequestBuilders.patch("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", String.valueOf(1L))
                        .param("approved", String.valueOf(true))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getItemByIdByUserIdTest() throws Exception {
        Mockito.when(bookingClient.getBooking(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong()))
                .thenReturn(objectResponseEntity);

        mvc.perform(MockMvcRequestBuilders.get("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", String.valueOf(1L))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getAllBookingsByBookerIdTest() throws Exception {
        Mockito.when(bookingClient.getBookingsByUserId(ArgumentMatchers.anyLong(), ArgumentMatchers.any(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt()))
                .thenReturn(objectResponseEntity);

        mvc.perform(MockMvcRequestBuilders.get("/bookings")
                        .header("X-Sharer-User-Id", String.valueOf(1L))
                        .param("from", "1")
                        .param("size", "2")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getAllBookingsByOwnerIdTest() throws Exception {
        Mockito.when(bookingClient.getBookingsByOwnerId(ArgumentMatchers.anyLong(), ArgumentMatchers.any(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt()))
                .thenReturn(objectResponseEntity);

        mvc.perform(MockMvcRequestBuilders.get("/bookings/owner")
                        .header("X-Sharer-User-Id", String.valueOf(1L))
                        .param("from", "1")
                        .param("size", "2")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}

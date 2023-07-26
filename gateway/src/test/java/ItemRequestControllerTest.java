import com.fasterxml.jackson.databind.ObjectMapper;
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
import ru.practicum.shareit.dto.ItemRequestDto;
import ru.practicum.shareit.dto.ItemRequestPublicDto;
import ru.practicum.shareit.request.ItemRequestClient;
import ru.practicum.shareit.request.ItemRequestController;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class ItemRequestControllerTest {

    @Mock
    private ItemRequestClient itemRequestsService;

    @InjectMocks
    private ItemRequestController itemRequestController;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;

    private ItemRequestDto itemRequestDto;

    private ResponseEntity<Object> objectResponseEntity = new ResponseEntity<>(HttpStatus.OK);

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(itemRequestController)
                .build();

        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(1L);
        itemRequestDto.setUserId(2L);
        itemRequestDto.setCreated(LocalDateTime.now());
        itemRequestDto.setDescription("Text for test");
    }

    @Test
    void saveNewRequestTest() throws Exception {
        Mockito.when(itemRequestsService.addRequest(ArgumentMatchers.anyLong(), ArgumentMatchers.any()))
                .thenReturn(objectResponseEntity);

        mapper.registerModule(new JavaTimeModule());

        mvc.perform(MockMvcRequestBuilders.post("/requests")
                        .header("X-Sharer-User-Id", String.valueOf(2L))
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getRequestByIdTest() throws Exception {
        ItemRequestPublicDto itemRequestPublicDto = new ItemRequestPublicDto();
        itemRequestPublicDto.setId(1L);

        Mockito.when(itemRequestsService.getRequestById(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong()))
                .thenReturn(objectResponseEntity);

        mvc.perform(MockMvcRequestBuilders.get("/requests/{requestId}", 1L)
                        .header("X-Sharer-User-Id", String.valueOf(1L))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getAllRequestByUserIdTest() throws Exception {
        ItemRequestPublicDto itemRequestPublicDto = new ItemRequestPublicDto();
        itemRequestPublicDto.setId(1L);

        Mockito.when(itemRequestsService.getAllRequestByUserId(ArgumentMatchers.anyLong()))
                .thenReturn(objectResponseEntity);

        mvc.perform(MockMvcRequestBuilders.get("/requests", 1L)
                        .header("X-Sharer-User-Id", String.valueOf(1L))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getRequestByParamTest() throws Exception {
        ItemRequestPublicDto itemRequestPublicDto = new ItemRequestPublicDto();
        itemRequestPublicDto.setId(1L);

        Mockito.when(itemRequestsService.getAllRequestByPageable(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyLong()))
                .thenReturn(objectResponseEntity);

        mvc.perform(MockMvcRequestBuilders.get("/requests/all")
                        .header("X-Sharer-User-Id", String.valueOf(1L))
                        .param("from", "1")
                        .param("size", "2")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}

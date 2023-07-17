package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.ItemRequestsService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestPublicDto;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ItemRequestControllerTest {

    @Mock
    private ItemRequestsService itemRequestsService;

    @InjectMocks
    private ItemRequestController itemRequestController;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;

    private ItemRequestDto itemRequestDto;

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
        when(itemRequestsService.addRequest(any()))
                .thenReturn(itemRequestDto);

        mapper.registerModule(new JavaTimeModule());

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", String.valueOf(2L))
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.userId", is(itemRequestDto.getUserId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())));
    }

    @Test
    void getRequestByIdTest() throws Exception {
        ItemRequestPublicDto itemRequestPublicDto = new ItemRequestPublicDto();
        itemRequestPublicDto.setId(1L);

        when(itemRequestsService.getRequestById(anyLong(), anyLong()))
                .thenReturn(itemRequestPublicDto);

        mvc.perform(get("/requests/{requestId}", 1L)
                        .header("X-Sharer-User-Id", String.valueOf(1L))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestPublicDto.getId()), Long.class));
    }

    @Test
    void getAllRequestByUserIdTest() throws Exception {
        ItemRequestPublicDto itemRequestPublicDto = new ItemRequestPublicDto();
        itemRequestPublicDto.setId(1L);

        when(itemRequestsService.getAllRequestByUserId(anyLong()))
                .thenReturn(List.of(itemRequestPublicDto));

        mvc.perform(get("/requests", 1L)
                        .header("X-Sharer-User-Id", String.valueOf(1L))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemRequestPublicDto.getId()), Long.class));
    }

    @Test
    void getRequestByParamTest() throws Exception {
        ItemRequestPublicDto itemRequestPublicDto = new ItemRequestPublicDto();
        itemRequestPublicDto.setId(1L);

        when(itemRequestsService.getAllRequestByPageable(anyInt(), anyInt(), anyLong()))
                .thenReturn(List.of(itemRequestPublicDto));

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", String.valueOf(1L))
                        .param("from", "1")
                        .param("size", "2")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemRequestPublicDto.getId()), Long.class));
    }
}

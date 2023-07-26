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
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ItemControllerTest {

    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemController itemController;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;

    private UserDto userDto;

    private ItemDto itemDto;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(itemController)
                .build();

        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("John");
        userDto.setEmail("john.doe@mail.com");

        itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Test");
        itemDto.setDescription("Text for test");
        itemDto.setAvailable(true);
        itemDto.setOwner(1L);
    }

    @Test
    void saveNewItemTest() throws Exception {
        when(itemService.addItem(any()))
                .thenReturn(itemDto);

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", String.valueOf(1L))
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())));
    }

    @Test
    void partialUpdateTest() throws Exception {
        ItemDto itemUpdateDtoName = new ItemDto();
        itemUpdateDtoName.setId(1L);
        itemUpdateDtoName.setDescription("Text for test");
        itemUpdateDtoName.setAvailable(true);
        itemUpdateDtoName.setOwner(1L);
        itemUpdateDtoName.setName("UpdateName");

        when(itemService.updateItem(any(), anyLong(), anyLong()))
                .thenReturn(itemUpdateDtoName);

        mvc.perform(patch("/items/{itemId}", 1L)
                        .header("X-Sharer-User-Id", String.valueOf(1L))
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemUpdateDtoName.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemUpdateDtoName.getName())))
                .andExpect(jsonPath("$.description", is(itemUpdateDtoName.getDescription())))
                .andExpect(jsonPath("$.available", is(itemUpdateDtoName.getAvailable())));
    }

    @Test
    void getAllByUserTest() throws Exception {
        when(itemService.findAllByUser(any()))
                .thenReturn(List.of(ItemMapper.getItemPublicDtoFromItem(ItemMapper.getItem(itemDto))));

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", String.valueOf(1L))
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Test")));
    }

    @Test
    void getItemByIdByUserIdTest() throws Exception {
        ItemPublicDto itemPublicDto = ItemMapper.getItemPublicDtoFromItem(ItemMapper.getItem(itemDto));

        when(itemService.findByIdAndOwner(anyLong(), anyLong()))
                .thenReturn(itemPublicDto);

        mvc.perform(get("/items/{itemId}", 1L)
                        .header("X-Sharer-User-Id", String.valueOf(1L))
                        .content(mapper.writeValueAsString(itemPublicDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemPublicDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemPublicDto.getName())))
                .andExpect(jsonPath("$.description", is(itemPublicDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemPublicDto.getAvailable())));
    }

    @Test
    void getItemByParamTest() throws Exception {
        when(itemService.getItemByParam(anyString()))
                .thenReturn(List.of(itemDto));

        mvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", String.valueOf(1L))
                        .param("text", "Text")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Test")));
    }

    @Test
    void addCommentTest() throws Exception {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setUser(new User());
        commentDto.setItem(ItemMapper.getItem(itemDto));
        commentDto.setText("For Test");

        CommentAddDto commentAddDto = new CommentAddDto();
        commentAddDto.setText("For Test");

        when(itemService.addComment(any()))
                .thenReturn(commentDto);

        mvc.perform(post("/items/{itemId}/comment", 1L)
                        .header("X-Sharer-User-Id", String.valueOf(1L))
                        .content(mapper.writeValueAsString(commentAddDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId()), Long.class))
                .andExpect(jsonPath("$.item.name", is(commentDto.getItem().getName())))
                .andExpect(jsonPath("$.text", is(commentDto.getText())));
    }
}

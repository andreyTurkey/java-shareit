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
import ru.practicum.shareit.item.*;
import ru.practicum.shareit.user.UserDto;

import java.nio.charset.StandardCharsets;

@ExtendWith(MockitoExtension.class)
public class ItemControllerTest {

    @Mock
    private ItemClient itemService;

    @InjectMocks
    private ItemController itemController;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;

    private UserDto userDto;

    private ItemDto itemDto;

    private ResponseEntity<Object> objectResponseEntity = new ResponseEntity<>(HttpStatus.OK);

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
        Mockito.when(itemService.addItem(ArgumentMatchers.anyLong(), ArgumentMatchers.any()))
                .thenReturn(objectResponseEntity);

        mvc.perform(MockMvcRequestBuilders.post("/items")
                        .header("X-Sharer-User-Id", String.valueOf(1L))
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void partialUpdateTest() throws Exception {
        ItemDto itemUpdateDtoName = new ItemDto();
        itemUpdateDtoName.setId(1L);
        itemUpdateDtoName.setDescription("Text for test");
        itemUpdateDtoName.setAvailable(true);
        itemUpdateDtoName.setOwner(1L);
        itemUpdateDtoName.setName("UpdateName");

        Mockito.when(itemService.updateItem(ArgumentMatchers.any(), ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong()))
                .thenReturn(objectResponseEntity);

        mvc.perform(MockMvcRequestBuilders.patch("/items/{itemId}", 1L)
                        .header("X-Sharer-User-Id", String.valueOf(1L))
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getAllByUserTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/items")
                        .header("X-Sharer-User-Id", String.valueOf(1L))
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /*@Test
    void getItemByIdByUserIdTest() throws Exception {
        ItemPublicDto itemPublicDto = ItemMapper.getItemPublicDtoFromItem(ItemMapper.getItem(itemDto));

        Mockito.when(itemService.findByIdAndOwner(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong()))
                .thenReturn(objectResponseEntity);

        mvc.perform(MockMvcRequestBuilders.get("/items/{itemId}", 1L)
                        .header("X-Sharer-User-Id", String.valueOf(1L))
                        .content(mapper.writeValueAsString(itemPublicDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }*/

    @Test
    void getItemByParamTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/items/search")
                        .header("X-Sharer-User-Id", String.valueOf(1L))
                        .param("text", "Text")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /*@Test
    void addCommentTest() throws Exception {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setUser(new User());
        commentDto.setItem(ItemMapper.getItem(itemDto));
        commentDto.setText("For Test");

        CommentAddDto commentAddDto = new CommentAddDto();
        commentAddDto.setText("For Test");

        Mockito.when(itemService.addComment(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong(), ArgumentMatchers.any()))
                .thenReturn(objectResponseEntity);

        mvc.perform(MockMvcRequestBuilders.post("/items/{itemId}/comment", 1L)
                        .header("X-Sharer-User-Id", String.valueOf(1L))
                        .content(mapper.writeValueAsString(commentAddDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }*/
}

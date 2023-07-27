package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.CheckRentHistory;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import javax.persistence.EntityManager;

@ExtendWith(MockitoExtension.class)
public class ItemServiceUnitTest {

    @Mock
    ItemRepository mockItemRepository;

    @Mock
    CheckRentHistory mockCheckRentHistory;

    @Mock
    CommentRepository mockCommentRepository;

    @Mock
    UserService mockUserService;

    @Mock
    EntityManager mockEm;

    ItemService itemService;

    @BeforeEach
    void setItemService() {
        itemService = new ItemService(mockItemRepository,
                mockUserService,
                mockCheckRentHistory,
                mockCommentRepository);
    }

    @Test
    void testAddItemWithMockito() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Item for Test");
        item.setDescription("For test");
        item.setRequestId(0L);
        item.setOwner(1L);

        Mockito
                .when(mockItemRepository.save(item)).
                thenReturn(item);

        Assertions.assertEquals(item, ItemMapper.getItem(itemService.addItem(ItemMapper.getItemDto(item))));
    }

    @Test
    void testUpdateItemWithMockito() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Item for Test");
        item.setDescription("For test");
        item.setRequestId(0L);
        item.setOwner(1L);

        Item updatedItemName = new Item();
        updatedItemName.setId(1L);
        updatedItemName.setName("ItemUpdate");
        updatedItemName.setDescription("For test");
        updatedItemName.setRequestId(0L);
        updatedItemName.setOwner(1L);

        Item updatedItemDescription = new Item();
        updatedItemDescription.setId(1L);
        updatedItemDescription.setName("ItemUpdate");
        updatedItemDescription.setDescription("Updated Description");
        updatedItemDescription.setRequestId(0L);
        updatedItemDescription.setOwner(1L);

        Item updatedItemAvailable = new Item();
        updatedItemAvailable.setId(1L);
        updatedItemAvailable.setName("ItemUpdate");
        updatedItemAvailable.setDescription("Updated Description");
        updatedItemAvailable.setAvailable(false);
        updatedItemAvailable.setRequestId(0L);
        updatedItemAvailable.setOwner(1L);

        ItemUpdateDto itemUpdateDtoName = new ItemUpdateDto();
        itemUpdateDtoName.setName("ItemUpdate");

        Mockito
                .when(mockItemRepository.getReferenceById(Mockito.anyLong())).
                thenReturn(item);

        Mockito
                .when(mockItemRepository.save(item)).
                thenReturn(item);

        Assertions.assertEquals(ItemMapper.getItemDto(updatedItemName), itemService.updateItem(
                itemUpdateDtoName, 1L, 1L));

        ItemUpdateDto itemUpdateDtoDescription = new ItemUpdateDto();
        itemUpdateDtoDescription.setDescription("Updated Description");

        Assertions.assertEquals(ItemMapper.getItemDto(updatedItemDescription), itemService.updateItem(
                itemUpdateDtoDescription, 1L, 1L));

        ItemUpdateDto itemUpdateDtoAvailable = new ItemUpdateDto();
        itemUpdateDtoAvailable.setAvailable(false);

        Assertions.assertEquals(ItemMapper.getItemDto(updatedItemAvailable), itemService.updateItem(
                itemUpdateDtoAvailable, 1L, 1L));
    }
}

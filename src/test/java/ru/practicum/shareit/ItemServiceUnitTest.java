package ru.practicum.shareit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.CheckRentHistory;
import ru.practicum.shareit.item.CommentRepository;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import javax.persistence.EntityManager;

@ExtendWith(MockitoExtension.class)
public class ItemServiceUnitTest {

    @Mock
    ItemRepository mockItemRepository;

    @Mock
    UserRepository mockUserRepository;

    @Mock
    CheckRentHistory mockCheckRentHistory;

    @Mock
    CommentRepository mockCommentRepository;

    @Mock
    EntityManager mockEm;

    ItemService itemService;

    @BeforeEach
    void setItemService() {
        itemService = new ItemService(mockItemRepository,
                mockUserRepository,
                mockCheckRentHistory,
                mockCommentRepository,
                mockEm);
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
                .when(mockUserRepository.existsById(Mockito.anyLong())).
                thenReturn(true);

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
                .when(mockUserRepository.existsById(Mockito.anyLong())).
                thenReturn(true);


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

        ItemUpdateDto itemUpdateDtoAvailable = new  ItemUpdateDto();
                itemUpdateDtoAvailable.setAvailable(false);

        Assertions.assertEquals(ItemMapper.getItemDto(updatedItemAvailable), itemService.updateItem(
                itemUpdateDtoAvailable, 1L, 1L));
    }

    /*@Test
    void testFindByIdAndOwnerWithMockito() {
        ItemService itemService = new ItemService();
        itemService.setItemRepository(mockItemRepository);
        itemService.setUserRepository(mockUserRepository);
        itemService.setCommentRepository(mockCommentRepository);

        Item item = new Item();
        item.setId(1L);
        item.setName("Item for Test");
        item.setDescription("For test");
        item.setRequestId(0L);
        item.setOwner(1L);

        User user = new User();
        user.setId(1L);
        user.setName("UserForTest");
        user.setEmail("mail@mail.ru");

      Comment comment =  new Comment();
      comment.setId(1L);
      comment.setItem(item);
      comment.setUser(user);
      comment.setText("testText");
      comment.setCreated(LocalDateTime.of(
              1986, Month.APRIL, 8, 12, 30));

        List.of(comment).stream()
                .map(CommentMapper::getPublicCommentDto)
                .collect(Collectors.toList());

        Mockito
                .when(mockCommentRepository.findAllByItemId(Mockito.anyLong())).
                thenReturn(List.of(comment));

      *//*  Item updatedItemName = new Item();
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
        updatedItemAvailable.setOwner(1L);*//*

        Mockito
                .when(mockItemRepository.getReferenceById(Mockito.anyLong())).
                thenReturn(item);

        Mockito
                .when(mockItemRepository.save(item)).
                thenReturn(item);

        Assertions.assertEquals(ItemMapper.getItemDto(updatedItemName), itemService.findByIdAndOwner(item.getId(), 1L));

        *//*ItemUpdateDto itemUpdateDtoDescription = ItemUpdateDto.builder()
                .description("Updated Description")
                .build();

        Assertions.assertEquals(ItemMapper.getItemDto(updatedItemDescription), itemService.updateItem(
                itemUpdateDtoDescription, 1L, 1L));

        ItemUpdateDto itemUpdateDtoAvailable = ItemUpdateDto.builder()
                .available(false)
                .build();

        Assertions.assertEquals(ItemMapper.getItemDto(updatedItemAvailable), itemService.updateItem(
                itemUpdateDtoAvailable, 1L, 1L));*//*
    }*/
}

package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.CheckRentHistory;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemServiceImpl implements ItemServiceDB {

    final ItemRepository itemRepository;

    final UserRepository userRepository;

    final CheckRentHistory checkRentHistory;

    final ItemMapper itemMapper;

    final ItemMapperGetOwnerDto itemMapperGetOwnerDto;

    final CommentMapper commentMapper;

    final CommentRepository commentRepository;

    final CommentAddMapper commentAddMapper;

    @Override
    public ItemDto addItem(ItemDto itemDto) {
        existsById(itemDto.getOwner());
        return itemMapper.getItemDto(itemRepository.save(itemMapper.getItem(itemDto)));
    }

    @Override
    public ItemDto updateItem(ItemUpdateDto itemUpdateDto, Long userId, Long itemId) {
        existsById(userId);
        Item item = itemRepository.getById(itemId);
        if (itemUpdateDto.getName() != null) item.setName(itemUpdateDto.getName());
        if (itemUpdateDto.getDescription() != null) item.setDescription(itemUpdateDto.getDescription());
        if (itemUpdateDto.getAvailable() != null) item.setAvailable(itemUpdateDto.getAvailable());
        return itemMapper.getItemDto(itemRepository.save(item));
    }

    @Override
    public ItemPublicDto findByIdAndOwner(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new EntityNotFoundException("Вещь не найдена"));
        return itemMapperGetOwnerDto.getItemGetOwnerDto(item, userId);
    }

    @Override
    public List<ItemPublicDto> findAllByOwner(Long userId) {
        List<Item> itemsByOwner = itemRepository.findAllByOwner(userId);
        List<ItemPublicDto> itemsGetOwnerDto = itemsByOwner.stream().map((Item item) -> itemMapperGetOwnerDto
                .getItemGetOwnerDto(item, userId)).collect(Collectors.toList());
        return itemsGetOwnerDto;
    }

    @Override
    public List<ItemDto> getItemByParam(String text) {
        if (text.isBlank()) return new ArrayList<>();
        return itemRepository.findByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(text, text)
                .stream()
                .map(itemMapper::getItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new EntityNotFoundException(
                "Вещь не найдена."));
        return itemMapper.getItemDto(item);
    }

    @Override
    public CommentDto addComment(CommentAddDto commentAddDto) {
        existsById(commentAddDto.getUserId());

        checkRentHistory.userTookItem(commentAddDto);

        return commentMapper.getCommentDto(commentRepository.save(commentMapper.getComment(
                commentAddMapper.getCommentDto(commentAddDto))));
    }

    private void existsById(Long userId) {
        if (!userRepository.existsById(userId)) throw new EntityNotFoundException("Пользователь не найден.");
    }
}

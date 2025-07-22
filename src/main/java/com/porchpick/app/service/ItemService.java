package com.porchpick.app.service;

import com.porchpick.app.dto.ItemRequestDto;
import com.porchpick.app.dto.ItemResponseDto;
import com.porchpick.app.exception.ResourceNotFoundException;
import com.porchpick.app.model.Item;
import com.porchpick.app.model.User;
import com.porchpick.app.repository.ItemRepository;
import com.porchpick.app.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ItemService implements IItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ItemResponseDto createItem(ItemRequestDto itemRequestDto) {
        User user = userRepository.findById(itemRequestDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", itemRequestDto.getUserId()));
        
        Item item = modelMapper.map(itemRequestDto, Item.class);
        item.setUser(user);

        Item savedItem = itemRepository.save(item);
        return modelMapper.map(savedItem, ItemResponseDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemResponseDto> getAllItemsByUserId(UUID userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        List<Item> items = itemRepository.findAllByUserId(userId);
        
        return items.stream()
                .map(item -> modelMapper.map(item, ItemResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemResponseDto> getAllItems() {
        List<Item> items = itemRepository.findAll();
        return items.stream()
                .map(item -> modelMapper.map(item, ItemResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ItemResponseDto getItemById(UUID itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item", "id", itemId));
        return modelMapper.map(item, ItemResponseDto.class);
    }

    @Override
    public ItemResponseDto updateItem(UUID itemId, ItemRequestDto itemRequestDto) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item", "id", itemId));
        
        if (!item.getUser().getId().equals(itemRequestDto.getUserId())) {
            throw new IllegalStateException("You cannot change the owner of an item.");
        }

        modelMapper.map(itemRequestDto, item);

        Item updatedItem = itemRepository.save(item);
        return modelMapper.map(updatedItem, ItemResponseDto.class);
    }

    @Override
    public void deleteItem(UUID itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item", "id", itemId));
        itemRepository.delete(item);
    }
}

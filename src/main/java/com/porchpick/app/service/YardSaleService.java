package com.porchpick.app.service;

import com.porchpick.app.dto.YardSaleRequestDto;
import com.porchpick.app.dto.YardSaleResponseDto;
import com.porchpick.app.exception.ResourceNotFoundException;
import com.porchpick.app.model.Item;
import com.porchpick.app.model.User;
import com.porchpick.app.model.YardSale;
import com.porchpick.app.repository.ItemRepository;
import com.porchpick.app.repository.UserRepository;
import com.porchpick.app.repository.YardSaleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class YardSaleService implements IYardSaleService {

    @Autowired
    private YardSaleRepository yardSaleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public YardSaleResponseDto createYardSale(YardSaleRequestDto yardSaleRequestDto) {
        User user = userRepository.findById(yardSaleRequestDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", yardSaleRequestDto.getUserId()));

        YardSale yardSale = modelMapper.map(yardSaleRequestDto, YardSale.class);
        yardSale.setUser(user);

        if (yardSaleRequestDto.getItemIds() != null && !yardSaleRequestDto.getItemIds().isEmpty()) {
            List<Item> items = itemRepository.findAllById(yardSaleRequestDto.getItemIds());
            yardSale.setItems(items);
        }

        YardSale savedYardSale = yardSaleRepository.save(yardSale);
        return modelMapper.map(savedYardSale, YardSaleResponseDto.class);
    }

    @Override
    public List<YardSaleResponseDto> getAllYardSalesByUserId(UUID userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        List<YardSale> yardSales = yardSaleRepository.findAllByUserId(userId);
        
        return yardSales.stream()
                .map(yardSale -> modelMapper.map(yardSale, YardSaleResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<YardSaleResponseDto> getAllYardSales() {
        List<YardSale> yardSales = yardSaleRepository.findAll();
        List<YardSaleResponseDto> yardSaleResponseDtos = yardSales.stream()
                .map(yardSale -> modelMapper.map(yardSale, YardSaleResponseDto.class)).toList();
                
        return yardSaleResponseDtos;
        // return yardSaleRepository.findAll().stream()
        //         .map(yardSale -> modelMapper.map(yardSale, YardSaleResponseDto.class))
        //         .collect(Collectors.toList());
    }

    @Override
    public YardSaleResponseDto getYardSaleById(UUID yardSaleId) {
        YardSale yardSale = yardSaleRepository.findById(yardSaleId)
                .orElseThrow(() -> new ResourceNotFoundException("YardSale", "id", yardSaleId));
        return modelMapper.map(yardSale, YardSaleResponseDto.class);
    }

    @Override
    public YardSaleResponseDto updateYardSale(UUID yardSaleId, YardSaleRequestDto yardSaleRequestDto) {
        YardSale yardSale = yardSaleRepository.findById(yardSaleId)
                .orElseThrow(() -> new ResourceNotFoundException("YardSale", "id", yardSaleId));

        if (!yardSale.getUser().getId().equals(yardSaleRequestDto.getUserId())) {
            throw new IllegalStateException("You are not authorized to update this yard sale.");
        }
        
        modelMapper.map(yardSaleRequestDto, yardSale);

        if (yardSaleRequestDto.getItemIds() != null) {
            List<Item> items = itemRepository.findAllById(yardSaleRequestDto.getItemIds());
            yardSale.setItems(items);
        }

        YardSale updatedYardSale = yardSaleRepository.save(yardSale);
        return modelMapper.map(updatedYardSale, YardSaleResponseDto.class);
    }

    @Override
    public void deleteYardSale(UUID userId, UUID yardSaleId) {
        YardSale yardSale = yardSaleRepository.findById(yardSaleId)
                .orElseThrow(() -> new ResourceNotFoundException("YardSale", "id", yardSaleId));

        if (!yardSale.getUser().getId().equals(userId)) {
            throw new IllegalStateException("You are not authorized to delete this yard sale.");
        }

        yardSaleRepository.delete(yardSale);
    }

    @Override
    public YardSaleResponseDto addItemToYardSale(UUID userId, UUID yardSaleId, UUID itemId) {
        YardSale yardSale = yardSaleRepository.findById(yardSaleId)
                .orElseThrow(() -> new ResourceNotFoundException("YardSale", "id", yardSaleId));
        if (!yardSale.getUser().getId().equals(userId)) {
            throw new IllegalStateException("You are not authorized to modify this yard sale.");
        }
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item", "id", itemId));

        yardSale.getItems().add(item);
        YardSale updatedYardSale = yardSaleRepository.save(yardSale);
        return modelMapper.map(updatedYardSale, YardSaleResponseDto.class);
    }

    @Override
    public YardSaleResponseDto removeItemFromYardSale(UUID userId, UUID yardSaleId, UUID itemId) {
        YardSale yardSale = yardSaleRepository.findById(yardSaleId)
                .orElseThrow(() -> new ResourceNotFoundException("YardSale", "id", yardSaleId));
        if (!yardSale.getUser().getId().equals(userId)) {
            throw new IllegalStateException("You are not authorized to modify this yard sale.");
        }
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item", "id", itemId));

        yardSale.getItems().remove(item);
        YardSale updatedYardSale = yardSaleRepository.save(yardSale);
        return modelMapper.map(updatedYardSale, YardSaleResponseDto.class);
    }
}

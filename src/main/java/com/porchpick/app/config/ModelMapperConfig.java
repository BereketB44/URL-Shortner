package com.porchpick.app.config;

import com.porchpick.app.dto.ItemRequestDto;
import com.porchpick.app.dto.YardSaleRequestDto;
import com.porchpick.app.dto.YardSaleResponseDto;
import com.porchpick.app.model.Item;
import com.porchpick.app.model.YardSale;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);

        // Add custom mapping to prevent the "unsaved-value" error.
        // This rule tells ModelMapper to skip setting the 'id' when mapping from ItemRequestDto to Item.
        modelMapper.createTypeMap(ItemRequestDto.class, Item.class)
                .addMappings(mapper -> mapper.skip(Item::setId));
        modelMapper.createTypeMap(YardSaleRequestDto.class, YardSale.class)
                .addMappings(mapper -> mapper.skip(YardSale::setId));
        modelMapper.createTypeMap(YardSale.class, YardSaleResponseDto.class)
                .addMappings(mapper -> mapper.map(YardSale::getId, YardSaleResponseDto::setId));
        return modelMapper;
    }
}
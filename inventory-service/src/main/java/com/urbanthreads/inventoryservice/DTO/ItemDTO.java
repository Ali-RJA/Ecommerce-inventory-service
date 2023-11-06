package com.urbanthreads.inventoryservice.DTO;

import com.urbanthreads.inventoryservice.model.Image;
import com.urbanthreads.inventoryservice.model.Item;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class ItemDTO {


    public ItemDTO(Item item) {
        this.id = item.getId();
        this.itemName = item.getItemName();
        this.price = item.getPrice();
        this.category = item.getCategory();
        this.description = item.getDescription();
        this.stockQuantity = item.getStockQuantity();
        this.images = item.getImages();
    }

    private Long id;

    private String itemName;

    private String description;

    private BigDecimal price;

    private int stockQuantity;

    private String category;

    Set<String> images = new HashSet<>();

}

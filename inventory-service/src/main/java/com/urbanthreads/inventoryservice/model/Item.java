package com.urbanthreads.inventoryservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
public class Item {

    private Long itemId;
    private String itemName;
    private String description;
    private BigDecimal price;
    private int stockQuantity;
    private String category;
    private String imageUrl;

}

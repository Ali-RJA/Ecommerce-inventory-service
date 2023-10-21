package com.urbanthreads.inventoryservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@Entity
@Table(name= "Item")
public class Item {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "item_name", nullable = false)
    private String itemName;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "price", nullable = false)
    private BigDecimal price;
    @Column(name = "stock_quantity", nullable = false)
    private int stockQuantity;
    @Column(name = "category")
    private String category;
    @Column(name = "image_url")
    private String imageUrl;


    public Item() {

    }
}

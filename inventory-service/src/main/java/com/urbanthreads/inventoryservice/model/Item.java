package com.urbanthreads.inventoryservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Check;

import java.util.HashSet;
import java.util.Set;
import java.math.BigDecimal;

@Setter
@Getter
@Entity
@AllArgsConstructor
@Table(name= "Item")
@Check(constraints = "stock_quantity >= 0")
public class Item {

    public Item(Long id, String itemName, String description, BigDecimal price, int stockQuantity, String category) {
        this.id = id;
        this.itemName = itemName;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.category = category;
    }

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

    public Set<String> getImages() {
        return images;
    }

    public void setImages(Set<String> images) {
        this.images = images;
    }
    public void addImage(String image) {
        this.images.add(image);
    }

    @Transient
    private Set<String> images = new HashSet<>();


    public Item() {

    }
}

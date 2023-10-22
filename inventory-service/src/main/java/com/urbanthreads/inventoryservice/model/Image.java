package com.urbanthreads.inventoryservice.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@Entity
@Table(name= "Image")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;



    @Column(name = "image_url", nullable = false)
    private String imageUrl;


    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    public Image() {

    }
}

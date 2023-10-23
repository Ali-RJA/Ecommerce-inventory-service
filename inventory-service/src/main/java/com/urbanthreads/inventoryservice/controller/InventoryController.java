package com.urbanthreads.inventoryservice.controller;


import com.urbanthreads.inventoryservice.model.Item;
import com.urbanthreads.inventoryservice.repo.ItemRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/urban-threads")
public class InventoryController {



    @PostConstruct
    public void loadItems() {
        List<Item> items = new ArrayList<>();

        items.add(new Item(1L, "Nike Dress Shirt", "Classic white Nike dress shirt.", new BigDecimal("59.99"), 50, "Shirts"));
        items.add(new Item(2L, "Adidas Yeezy V1", "Limited edition Adidas Yeezy sneakers.", new BigDecimal("299.99"), 25, "Shoes"));
        items.add(new Item(3L, "Puma Running Tee", "Breathable running tee by Puma.", new BigDecimal("29.99"), 60, "Sportswear"));
        items.add(new Item(4L, "Reebok Gym Shorts", "Comfortable shorts for workouts.", new BigDecimal("24.99"), 80, "Sportswear"));
        items.add(new Item(5L, "Under Armour Hoodie", "Warm and cozy hoodie for chilly days.", new BigDecimal("69.99"), 40, "Hoodies"));
        items.add(new Item(6L, "Vans Skater Shoes", "Stylish skater shoes by Vans.", new BigDecimal("79.99"), 90, "Shoes"));
        items.add(new Item(7L, "Ralph Lauren Polo", "Elegant polo shirt from Ralph Lauren.", new BigDecimal("89.99"), 70, "Shirts"));
        items.add(new Item(8L, "Supreme Snapback Cap", "Trendy snapback cap by Supreme.", new BigDecimal("49.99"), 100, "Accessories"));
        items.add(new Item(9L, "Levi's Jeans", "Classic blue jeans by Levi's.", new BigDecimal("99.99"), 65, "Pants"));
        items.add(new Item(10L, "Tommy Hilfiger Jacket", "Stylish jacket for winter.", new BigDecimal("149.99"), 20, "Outerwear"));
        items.add(new Item(11L, "Calvin Klein Underwear", "Comfortable underwear by Calvin Klein.", new BigDecimal("19.99"), 85, "Underwear"));
        items.add(new Item(12L, "Gucci Sunglasses", "Luxury sunglasses from Gucci.", new BigDecimal("349.99"), 15, "Accessories"));
        items.add(new Item(13L, "Burberry Scarf", "Premium quality scarf by Burberry.", new BigDecimal("129.99"), 45, "Accessories"));
        items.add(new Item(14L, "Lacoste Tennis Shoes", "Professional tennis shoes from Lacoste.", new BigDecimal("109.99"), 35, "Shoes"));
        items.add(new Item(15L, "Zara Trousers", "Elegant trousers for formal events.", new BigDecimal("69.99"), 55, "Pants"));
        items.add(new Item(16L, "Oakley Sports Sunglasses", "Durable sunglasses for sports activities.", new BigDecimal("139.99"), 30, "Accessories"));
        items.add(new Item(17L, "Jordan Bred 11", "Iconic basketball shoes by Jordan.", new BigDecimal("249.99"), 22, "Shoes"));
        items.add(new Item(18L, "Diesel Leather Belt", "High-quality leather belt by Diesel.", new BigDecimal("39.99"), 75, "Accessories"));
        items.add(new Item(19L, "Fila Sports Socks", "Breathable socks for athletic performance.", new BigDecimal("9.99"), 95, "Sportswear"));
        items.add(new Item(20L, "Champion Gym Bag", "Spacious bag for gym essentials.", new BigDecimal("44.99"), 48, "Accessories"));
        itemRepository.saveAll(items);
    }


    private final ItemRepository itemRepository;



    public InventoryController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @GetMapping("/items")
    List<Item> all() {
        return itemRepository.findAll();
    }

    @GetMapping("/items/{id}")
    Item one(@PathVariable Long id) {
        return itemRepository.findById(id).orElseThrow();
    }

    @PostMapping("/items")
    Item newItem(@RequestBody Item newItem) {
        return itemRepository.save(newItem);
    }



    @DeleteMapping("/items/{id}")
    void deleteItem(@PathVariable Long id) {
        itemRepository.deleteById(id);
    }



}

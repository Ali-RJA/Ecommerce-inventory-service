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

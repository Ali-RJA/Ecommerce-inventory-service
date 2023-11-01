package com.urbanthreads.inventoryservice.controller;


import com.urbanthreads.inventoryservice.DTO.ItemDTO;
import com.urbanthreads.inventoryservice.model.Item;
import com.urbanthreads.inventoryservice.repo.ItemRepository;
import com.urbanthreads.inventoryservice.service.InventoryService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/urban-threads")
public class InventoryController {


    @Autowired
    InventoryService inventoryService;
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
    Page<Item> all(@RequestParam int pageNumber, int sizeOfPage) {
        Pageable pageable = PageRequest.of(pageNumber, sizeOfPage); // you should set table sort ordering here
        Optional<Page<Item>> page = inventoryService.itemPage(pageable); // returns Page<Item>
        return page.get();
    }

    @GetMapping("/items/{id}")
    Item one(@PathVariable Long id) {
        return itemRepository.findById(id).orElseThrow();
    }

    @GetMapping("/items/availableitems")
    public ResponseEntity<?> stockItemsById(@RequestParam List<Long> requestedItems) {
        try {
            Map<Long, Integer> availablity = inventoryService.stockQuantity(requestedItems).get();
            return ResponseEntity.ok().body(availablity);
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body("Failed to get stock availability for items sent." +
                    " Check request");
        }
    }
    @GetMapping("/items/byname")
    List<ItemDTO> itemsByName(@RequestParam String name) {
        System.out.println("The name input is: "+name);
        Optional<List<ItemDTO>> optional = inventoryService.itemsByName(name);
        return optional.get();
    }

    @PostMapping("/items/reducestock")
    public ResponseEntity<?> reduceStock(@RequestBody Map<Long, Integer> itemsRequested) {
        try {
            inventoryService.reduceStock(itemsRequested);
            return ResponseEntity.ok().build(); // Return 200 OK with no content
        } catch (Exception e) {
            // Log the exception message or stack trace if needed
            // Depending on the exception type, you might want to return different status codes
            // For this example, I am returning 500 Internal Server Error
            return ResponseEntity.internalServerError().body("Stock reduction failed: " + e.getMessage() +
                    ". Check stock quantity again for this order.");
        }
    }

    @PostMapping("/items/add")
    public ResponseEntity<?> addItem(@RequestBody ItemDTO newItem) {
        try {
            Optional<Long> id = inventoryService.addItem(newItem);
            return ResponseEntity.ok().body(id.get());
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body("Item insertion failed.");
        }
    }



    @DeleteMapping("/items/delete")
    public ResponseEntity<?> deleteItem(@RequestBody List<Long> ids) {
        try {
            inventoryService.removeItems(ids);
            return ResponseEntity.ok().build();
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body("Delete failed.");
        }
    }
    @PutMapping("/items/{id}/edit")
    public ResponseEntity<?> updateItem(@PathVariable Long id, @RequestBody ItemDTO itemDTO) {
        try {
            Optional<Long> itemId = inventoryService.editItem(itemDTO);
            return ResponseEntity.ok().body(itemId.get());
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body("Item update failed.");
        }
    }



}

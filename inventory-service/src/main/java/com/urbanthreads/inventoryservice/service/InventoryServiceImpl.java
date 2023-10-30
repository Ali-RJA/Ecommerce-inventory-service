package com.urbanthreads.inventoryservice.service;

import com.urbanthreads.inventoryservice.DTO.ItemDTO;
import com.urbanthreads.inventoryservice.model.Item;
import com.urbanthreads.inventoryservice.repo.ItemRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.*;
import software.amazon.awssdk.services.s3.S3Client;


import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class InventoryServiceImpl implements InventoryService{

    @Autowired
    private S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Autowired
    ItemRepository repo;

    /*

    - Return all items available in the inventory (pages)
    - Return all items in the inventory for a given match case RegEX item name.
    - Return all items in the inventory for a given list<id>
    - Return all <stock quantities, id> for a given list<id>
    - Reduce and Return all <id> for a given list <id, reduceAmount> [repeatable read]
    - Remove all items for a given list<id> [repeatable read]
    - Add item
    - Edit item [repeatable read]
     */

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Optional<Page> itemPage(Pageable pageable) {
        return Optional.empty(); // S3 Bucket
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Optional<List<Item>> itemsByName(String name) {
        return Optional.empty(); // S3 Bucket
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Optional<List<Item>> itemsByIds(List<Long> ids) {
        return Optional.empty(); // S3 Bucket
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Optional<Map<Long, Integer>> stockQuantity(List<Long> ids) {
        return Optional.empty();
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED,rollbackFor = Exception.class)
    public Optional<List<Long>> reduceStock(Map<Long, Integer> purchaseItems) {
        return Optional.empty();
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED,rollbackFor = Exception.class)
    public Optional<Integer> removeItems(List<Long> ids) {
        return Optional.empty();
    }

    @Override
    public Optional<Long> addItem(ItemDTO item) {
        return Optional.empty(); // S3 Bucket
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED,rollbackFor = Exception.class)
    public Optional<Long> editItem(ItemDTO item) {
        return Optional.empty();
    }
}

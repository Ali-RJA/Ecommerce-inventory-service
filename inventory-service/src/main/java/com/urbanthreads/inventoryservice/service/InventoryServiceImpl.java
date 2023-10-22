package com.urbanthreads.inventoryservice.service;

import com.urbanthreads.inventoryservice.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryServiceImpl implements InventoryService{

    @Autowired
    private S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;




    @Override
    public Optional<List<Item>> getAllAvailableItems() {

        return Optional.empty();
    }

    @Override
    public Optional<List<Item>> getAvailableItemsOnId(long id) {
        return Optional.empty();
    }

    @Override
    public List<Item> getAvailableItemsOnName(String itemName) {
        return null;
    }
}

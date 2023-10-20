package com.urbanthreads.inventoryservice.service;

import com.urbanthreads.inventoryservice.model.Item;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryServiceImpl implements InventoryService{


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

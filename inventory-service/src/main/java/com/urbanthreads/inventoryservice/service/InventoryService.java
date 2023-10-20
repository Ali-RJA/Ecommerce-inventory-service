package com.urbanthreads.inventoryservice.service;
import com.urbanthreads.inventoryservice.model.Item;

import java.util.List;
import java.util.Optional;

public interface InventoryService {

    /*

    - Return all items available in the inventory
    - Return all available items in the inventory for a given name or id.
     */


    public Optional<List<Item>> getAllAvailableItems();
    public Optional<List<Item>> getAvailableItemsOnId(long id);
    public List<Item> getAvailableItemsOnName(String itemName);



}

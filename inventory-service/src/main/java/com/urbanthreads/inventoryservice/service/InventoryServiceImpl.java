package com.urbanthreads.inventoryservice.service;

import com.urbanthreads.inventoryservice.DTO.ItemDTO;
import com.urbanthreads.inventoryservice.model.Item;
import com.urbanthreads.inventoryservice.repo.ItemRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.*;
import software.amazon.awssdk.services.s3.S3Client;


import java.math.BigDecimal;
import java.util.*;

@Service
public class InventoryServiceImpl implements InventoryService{

    @Autowired
    private S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Autowired
    ItemRepository repo;

    @PersistenceContext
    EntityManager entityManager;

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
    public Optional<Page<Item>> itemPage(Pageable pageable) {
        Page<Item> page = repo.findAll(pageable);
        return Optional.of(page); // S3 Bucket
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Optional<List<ItemDTO>> itemsByName(String name) {
        String hql = "SELECT e FROM Item e WHERE e.itemName LIKE :namePattern";
        System.out.println("The name input is: "+name);
        TypedQuery<Item> query = entityManager.createQuery(hql, Item.class);
        query.setParameter("namePattern",name+"%");
        List<Item> items = query.getResultList();
        List<ItemDTO> itemDTOS = new ArrayList<>();
        for (Item item : items) {
            itemDTOS.add(new ItemDTO(item));
        }
        return Optional.of(itemDTOS); // S3 Bucket
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Optional<List<ItemDTO>> itemsByIds(List<Long> ids) {
        // S3 Bucket
        List<Item> items = repo.findAllById(ids);
        List<ItemDTO> itemDTOS = new ArrayList<>();
        for (Item item : items) {
            itemDTOS.add(new ItemDTO(item));
        }
        return Optional.of(itemDTOS);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Optional<Map<Long, Integer>> stockQuantity(List<Long> ids) {
        String hql = "SELECT e.id, e.stockQuantity FROM Item e WHERE e.id IN (:listOfIds)";
        TypedQuery<Object[]> query = entityManager.createQuery(hql, Object[].class);
        query.setParameter("listOfIds",ids);
        List<Object[]> results = query.getResultList();
        Map<Long,Integer> map = new HashMap<>();
        for (Object[] result : results) {
            Long id = (Long) result[0];
            int stockQuantity = (int) result[1];
            map.put(id,stockQuantity);
        }

        return Optional.of(map);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, rollbackFor = Exception.class)
    public void reduceStock(Map<Long, Integer> purchaseItems) throws Exception {
        String hql = "UPDATE Item e SET e.stockQuantity = e.stockQuantity - :reduceAmount WHERE e.id = :id AND e.stockQuantity >= :reduceAmount";
        for (Map.Entry<Long, Integer> entry : purchaseItems.entrySet()) {
            int updateCount = entityManager.createQuery(hql)
                    .setParameter("reduceAmount", entry.getValue())
                    .setParameter("id", entry.getKey())
                    .executeUpdate();
            // If any of the updates fails to update a row, throw an exception to trigger rollback
            if (updateCount == 0) {
                throw new Exception("Stock reduction failed for item ID " + entry.getKey());
            }
        }
        // No need to return anything, method execution success means all stock was reduced
    }


    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ,rollbackFor = Exception.class)
    public void removeItems(List<Long> ids) {
            repo.deleteAllById(ids);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED,rollbackFor = Exception.class)
    public Optional<Long> addItem(ItemDTO itemDTO) {
        Item item = repo.save(itemDTO.Item());
        return Optional.of(item.getId()); // S3 Bucket
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ,rollbackFor = Exception.class)
    public Optional<Long> editItem(ItemDTO itemDTO) {
        Item item = repo.save(itemDTO.Item());
        return Optional.of(item.getId()); // S3 Bucket
    }
}

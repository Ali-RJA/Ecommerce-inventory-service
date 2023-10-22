package com.urbanthreads.inventoryservice.other;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class DatabaseConnectionChecker {

    @PersistenceContext
    private EntityManager entityManager;

    @PostConstruct
    public void init() {
        try {
            entityManager.createNativeQuery("SELECT 1").getSingleResult();
            System.out.println("Successfully connected to the database!");
        } catch(Exception e) {
            System.out.println("Failed to connect to the database.");
            e.printStackTrace();
        }
    }
}

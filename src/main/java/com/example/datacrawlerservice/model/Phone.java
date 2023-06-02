package com.example.datacrawlerservice.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "phone")
public class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(columnDefinition = "VARCHAR(30)")
    @Enumerated(EnumType.STRING)
    private Brand brand;

    @Column
    private String model;

    @Column
    private int price; // Todo decimal

    @Column
    private boolean inStock;

    @Column
    private int memory;
}

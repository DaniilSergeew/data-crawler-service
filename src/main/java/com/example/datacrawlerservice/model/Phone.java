package com.example.datacrawlerservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "phone")
public class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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

    @ManyToOne
    @JoinColumn(name = "crawl_id")
    private Crawl crawl;
}

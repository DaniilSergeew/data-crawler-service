package com.example.datacrawlerservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "report")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    Integer id;

    @Column
    int averagePrice;

    @Column
    int minPrice;

    @Column
    int maxPrice;

    @Column
    float inStockFactor;

    // and more some metrics
}

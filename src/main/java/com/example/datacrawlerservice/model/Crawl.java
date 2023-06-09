package com.example.datacrawlerservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="crawl")
public class Crawl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private Instant date;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "crawl_id")
    private List<Phone> phones;
}

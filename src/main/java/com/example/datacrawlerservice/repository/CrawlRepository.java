package com.example.datacrawlerservice.repository;

import com.example.datacrawlerservice.model.Crawl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrawlRepository extends JpaRepository<Crawl, Integer> {
}

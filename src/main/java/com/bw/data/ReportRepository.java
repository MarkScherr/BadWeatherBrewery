package com.bw.data;

import java.util.*;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.*;
import org.springframework.stereotype.*;

import com.bw.DTO.*;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer>{

    @Query("select distinct product from Report")
    public List<String> getBeers();

    @Query("select r from Report r WHERE (:beerType = '' OR r.product " +
        "LIKE CONCAT('%', UPPER(:beerType), '%')) and " +
        "(:from = '' OR r.reportDate > :from) and " +
        "(:to = '' OR r.reportDate < :to) ORDER BY city, store")
    public List<Report> getMatchingResults(@Param("beerType") String beerType,
                                           @Param("from") String from,
                                           @Param("to") String to);
}

package com.bw.service;

import java.util.*;

import org.springframework.web.multipart.*;

import com.bw.DTO.*;

public interface ReportService {

    public Result<List<Report>> insertReport (MultipartFile file);

    public List<String> getBeers();

    public List<Report> getBeerInformation(String beerType,
                                           String from,
                                           String to);
}

package com.bw.controllers;

import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;

import com.bw.DTO.*;
import com.bw.service.*;

@RestController
@RequestMapping("/bw")
public class mainController {
    @Autowired
    private ReportService rs;


    @PostMapping("/hello")
    public ResponseEntity<List<String>> upload(@RequestParam("file") MultipartFile file) {

        Result<List<Report>> result = rs.insertReport(file);
        System.out.println(file.getName());

        if (result.isSuccess()) {
            return ResponseEntity.ok(result.getPositiveResponseMessages());
        } else {
            return ResponseEntity.ok(result.getMessages());
        }

    }

    @GetMapping("/beers")
    public @ResponseBody List<String> getBeers() {
        List<String> returnList = rs.getBeers();
        return returnList;

    }

    @GetMapping("/beerInformation/{beerType}/{from}/{to}/")
    public @ResponseBody List<Report> getBeerInformation(
        @PathVariable("beerType") String beerType,
        @PathVariable("from") String from,
        @PathVariable("to") String to) {

        List<Report> returnList = rs.getBeerInformation(beerType, from, to);
        return returnList;

    }
}

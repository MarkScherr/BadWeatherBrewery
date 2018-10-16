package com.bw.service;

import java.io.*;
import java.util.*;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.text.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.web.multipart.*;

import com.bw.DTO.*;
import com.bw.data.*;
@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReportRepository reportRepo;
    @Autowired
    private OrderDateRepository orderDateRepo;

    @Override
    public Result<List<Report>> insertReport(MultipartFile file) {
        Result<List<Report>> result = new Result<>();
        File convFile = new File(file.getOriginalFilename());
        List<Report> reportList = new ArrayList<>();
        try {
            convFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();
        } catch ( IOException x1 ) {
            x1.printStackTrace();
            result.addMessage("Error with file!");
        }

        try (PDDocument document = PDDocument.load(convFile)) {

            document.getClass();

            if (!document.isEncrypted()) {

                PDFTextStripperByArea stripper = new PDFTextStripperByArea();
                stripper.setSortByPosition(true);

                PDFTextStripper tStripper = new PDFTextStripper();

                String pdfFileInText = tStripper.getText(document);
                //System.out.println("Text:" + st);

                // split by whitespace
                String lines[] = pdfFileInText.split("\\r?\\n");
                String id = "";
                String store = "";
                String city = "";
                String product = "";
                String productType = "";
                String quantity = "";
                String equivalence = "";
                boolean hasReadDate = false;
                int pastDotAmount = 0;
                String reportDate = "";
                Report order = new Report();
                OrderDate orderDate = new OrderDate();
                boolean hasCommaAlready = false;

//                for (String line : lines) {
//                    System.out.println(line);
//                }
                int position = 0;

                for (String line : lines) {
                    System.out.println(line);
                }
                for (String line : lines) {
                    if (line.contains("Report from") && !hasReadDate) {
                        reportDate = line.substring(12, 22);
                        orderDate.setReportDate(reportDate);
                        hasReadDate = true;
                        System.out.println(line.substring(12, 22) + "  HERE IS REPORT DATE");
                    }
//
//                    List<OrderDate> orderDateResult = orderDateRepo.findAll();
//
//                    if(!hasReadDate && !orderDateResult.isEmpty()) {
//                        hasReadDate = true;
//                        for (OrderDate oDate : orderDateResult) {
//                            if (oDate != null) {
//                                if (oDate.getReportDate().equals(reportDate)) {
//                                    result.addMessage("File already entered into Database");
//                                    return result;
//                                }
//                            }
//                        }
//
//                        orderDateRepo.save(orderDate);
//
//
//                    }


                    if(line.contains("Customer:")) {
                        order = new Report();
                        id = "";
                        store = "";
                        city = "";
                        position = 0;

                        char[] idArray = line.toCharArray();
                        for(char x : idArray) {
                            if(Character.isDigit(x) && position == 0) {
                                id += x;
                            }
                            if (x == '-') {
                                position = 1;
                            }
                            if (x == ',') {
                                if(!hasCommaAlready) {
                                    position = 2;
                                    hasCommaAlready = true;
                                } else {
                                    store += city;
                                    city = "";
                                    position = 2;
                                    hasCommaAlready = true;
                                }
                            }
                            if (position == 1 && x != '-' ) {
                                store += x;
                            }
                            if (position == 2 && x != ',' ) {
                                city += x;
                            }

                        }

                        order.setStoreId(id.trim());
                        order.setStore(store.trim());
                        order.setCity(city.trim());

                    }

                    if(line.contains("BW") && line.contains("/")) {
                        char[] productArray = line.toCharArray();
                        position = 0;
                        product = "";
                        productType = "";
                        quantity = "";
                        equivalence = "";

                        pastDotAmount = 0;
                        for(char x : productArray) {
                            if(x == ' ') {
                                position++;

                            }
                            if(position == 3 && Character.isDigit(x) == false &&
                                    x != ' ' && productType.trim().length() == 0 ) {
                                position = 2;
                                product += ' ';
                            }

                            if(position == 2) {
                                if(Character.isDigit(x) == false) {
                                    product += x;
                                } else {
                                    position++;
                                }
                            }

                            if (position == 3 ) {
                                productType += x;
                            }

                            if (position == 4) {
                                quantity += x;
                            }

                            if(position == 5) {

                                if(pastDotAmount > 0 && pastDotAmount < 3) {
                                    pastDotAmount ++;

                                } else if(x == '.') {
                                    pastDotAmount ++;
                                }

                                if (pastDotAmount > 2) {
                                    equivalence += x;
                                }
                            }



                        }
                        quantity = quantity.trim();
                        equivalence = equivalence.trim();
                        StringBuilder sb = new StringBuilder(equivalence);
                        if(equivalence.charAt(0) != 1) {
                            sb.deleteCharAt(0);
                        }
                        System.out.println(quantity);
                        System.out.println(sb);
                        order.setReportDate(reportDate.trim());
                        order.setProduct(product.trim());
                        order.setProductType(productType.trim());
                        order.setQuantity(Integer.parseInt(quantity));
                        order.setEquivalence(Double.parseDouble(sb.toString()));

                        reportList.add(order);
                        order = reportRepo.save(order);
                        order = new Report();
                        order.setStoreId(id.trim());
                        order.setStore(store.trim());
                        order.setCity(city.trim());

                    }
                }

            }

        } catch(IOException x) {
            x.printStackTrace();
            result.addMessage("Incorrect file type, please use PDF");
        }

        if(reportList.size() > 0) {
            for(Report order : reportList) {
                System.out.println(order.getStore());
                System.out.println(order.getStoreId());
                System.out.println(order.getProduct());
            }
        }

        result.setPayload(reportList);
        result.setPositiveResponseMessages("Data Properly Added to Database :)");
        return result;
    }

    @Override
    public List<String> getBeers() {
        List<String> beerList = reportRepo.getBeers();
        return beerList;
    }

    @Override
    public List<Report> getBeerInformation(String beerType, String from, String to) {
        List<Report> beerList = reportRepo.getMatchingResults(beerType, from, to);
        return beerList;
    }

}

package com.bw.DTO;

import javax.persistence.*;
import javax.validation.constraints.*;


@Entity
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @NotNull
    private String storeId;

    @NotNull
    private String store;

    @NotNull
    private String reportDate;

    @NotNull
    private String city;

    @NotNull
    private String product;

    @NotNull
    private String productType;

    @NotNull
    private int quantity;

    @NotNull
    private double equivalence;


    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getEquivalence() {
        return equivalence;
    }

    public void setEquivalence(double equivalence) {
        this.equivalence = equivalence;
    }


}

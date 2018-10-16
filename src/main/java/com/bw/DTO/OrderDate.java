package com.bw.DTO;

import javax.persistence.*;
import javax.validation.constraints.*;


@Entity
public class OrderDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @NotNull
    private String reportDate;

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }
}

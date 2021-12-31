package com.example.myhackaton.model;


import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sales implements Serializable {
    private String addedByUser;
    private String storeName;
    private String menu;
    private String saleType;
    private String sSaleTime;
    private String eSaleTime;
    private String price;
    private String salePercent;
    private String totalPrice;
    private String storeLocation;
    private String saleUniqueKey;
}

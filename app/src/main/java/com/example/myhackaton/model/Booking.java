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
public class Booking implements Serializable {
    private String addedByHost;
    private String guestUniqueKey;

    private String storeName;
    private String menu;
    private String saleType;
    private String salePercent;
    private String saleUniqueKey;

}

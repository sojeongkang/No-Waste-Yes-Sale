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
public class Account{
    //common
    private String addedByUser;
    private String accountType;
    private String email;

    //host
    private String storeName;
    private String storeLocation;
    private String detailAddress;
    private String documentPhoto;
    private String menuList;
    //guest
    private String phoneNumber;


}

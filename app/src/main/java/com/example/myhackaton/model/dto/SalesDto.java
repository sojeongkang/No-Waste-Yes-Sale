package com.example.myhackaton.model.dto;

import com.example.myhackaton.model.Sales;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SalesDto implements Serializable {
    private String mSeq;
    private Sales sale;
}

package com.example.myhackaton.model.dto;

import com.example.myhackaton.model.Account;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AccountDto implements Serializable {
    private String seq;
    private Account course;
}

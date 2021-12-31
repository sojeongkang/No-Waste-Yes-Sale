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
public class Schedule implements Serializable {
    private String addedByUser;
    private String subject;
    private String startTime;
    private String endTime;
    private String concept;
    private String foodType;
    private String condition;
    private String region;
    private String place;
    private String withFriend;
    private String memo;
    private String date;
}

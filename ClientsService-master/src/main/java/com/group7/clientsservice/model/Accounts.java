package com.group7.clientsservice.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class Accounts {

    private String id;
    private String client;
    private String clientType;
    private String clientProfile;
    private String type;
    private Double balance;
    private Double maintenanceFee;
    private Integer movementsLimit;
    private List<String> holders;
    private List<String> signers;
    private Integer movementDay;

}

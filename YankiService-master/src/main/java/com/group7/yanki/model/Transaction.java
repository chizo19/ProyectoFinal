package com.group7.yanki.model;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Slf4j
public class Transaction {
    private String id;
    private Long from;
    private Long to;
    private Double amount;
    private LocalDate date;

    private String paymentType;
    private String status;
    private Double quantity;
    private Double sellRate;
    private Double unitPrice;
}
